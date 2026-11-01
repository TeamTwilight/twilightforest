package twilightforest.forge.beans;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Le petit conteneur d'injection du portage Forge.
 *
 * <p>La version NeoForge du mod s'appuie sur Beanification, une bibliotheque de Tamaized construite
 * sur l'API de scan de mods de NeoForge ({@code net.neoforged.fml.ModList},
 * {@code ModFileScanData}...) : sous Forge, elle plante des le premier appel. Elle n'a pas non plus
 * de licence, donc on ne la modifie pas. Ce conteneur est ecrit de zero et ne couvre que ce que
 * Twilight Forest utilise vraiment :</p>
 * <ul>
 *   <li>{@link Component} : 56 classes, toutes avec un constructeur vide ;</li>
 *   <li>{@link Autowired} : 72 champs, statiques ou d'instance, tous resolus par leur type ;</li>
 *   <li>{@link PostConstruct} : 13 methodes, sans parametre ou avec le bus du mod / du jeu ;</li>
 *   <li>{@link Configurable} : les objets crees avec {@code new} appellent {@link #inject(Object)}.</li>
 * </ul>
 * <p>Aucun attribut d'annotation (nom, dist, priorite) n'est utilise par le mod : ils ne sont pas
 * repris.</p>
 *
 * <p>Les composants sont trouves dans les donnees de scan que Forge produit pour le fichier du mod,
 * qui enregistrent toutes les annotations sans filtre.</p>
 */
public final class BeanContext {

	private static final Logger LOGGER = LogManager.getLogger("twilightforest/beans");

	private static final Type COMPONENT = Type.getType(Component.class);
	private static final Type AUTOWIRED = Type.getType(Autowired.class);
	private static final Type POST_CONSTRUCT = Type.getType(PostConstruct.class);

	/**
	 * Quatre composants vivent ici et touchent aux classes du client (rendus, calques, couleurs).
	 * Sur un serveur dedie ils ne sont ni crees, ni remplis : les charger suffirait a planter.
	 */
	private static final String CLIENT_PACKAGE = "twilightforest.client.";

	private static final Map<Class<?>, Object> BEANS = new LinkedHashMap<>();

	private static boolean initialising;
	private static boolean ready;
	private static boolean dedicatedServer;

	private BeanContext() {
	}

	/**
	 * Cree tous les composants du mod, remplit les champs {@link Autowired}, puis appelle les
	 * methodes {@link PostConstruct}.
	 *
	 * <p>A appeler une fois, en tete du constructeur du mod : c'est la que Beanification tournait
	 * (dans l'initialiseur statique de la classe du mod), donc avant tout le reste du constructeur.</p>
	 */
	public static synchronized void init(String modId, IEventBus modBus) {
		if (ready || initialising) {
			throw new IllegalStateException("BeanContext deja initialise pour " + modId);
		}
		initialising = true;

		IModFileInfo modFile = ModList.get().getModFileById(modId);
		if (modFile == null) {
			throw new IllegalStateException("Aucun fichier de mod pour " + modId);
		}
		dedicatedServer = FMLEnvironment.dist == Dist.DEDICATED_SERVER;

		// Ordre fixe : la meme sequence de creation et d'appels a chaque lancement.
		List<ModFileScanData.AnnotationData> annotations = new ArrayList<>(modFile.getFile().getScanResult().getAnnotations());
		annotations.sort(Comparator
			.comparing((ModFileScanData.AnnotationData a) -> a.clazz().getClassName())
			.thenComparing(a -> String.valueOf(a.memberName())));

		// 1. Creer les composants.
		for (ModFileScanData.AnnotationData a : annotations) {
			if (!COMPONENT.equals(a.annotationType()) || a.targetType() != ElementType.TYPE) continue;
			String name = a.clazz().getClassName();
			if (dedicatedServer && name.startsWith(CLIENT_PACKAGE)) continue;
			Class<?> type = load(name);
			BEANS.put(type, construct(type));
		}

		// 2. Remplir leurs champs d'instance.
		for (Object bean : BEANS.values()) {
			injectInstanceFields(bean);
		}

		// 3. Remplir les champs statiques, dans toutes les classes du mod.
		int statics = 0;
		for (ModFileScanData.AnnotationData a : annotations) {
			if (!AUTOWIRED.equals(a.annotationType()) || a.targetType() != ElementType.FIELD) continue;
			String owner = a.clazz().getClassName();
			if (dedicatedServer && owner.startsWith(CLIENT_PACKAGE)) continue;
			Field field = field(load(owner), a.memberName());
			if (!Modifier.isStatic(field.getModifiers()) || skipOnThisSide(field)) continue;
			set(field, null, resolve(field));
			statics++;
		}

		initialising = false;
		ready = true;

		// 4. Les PostConstruct en dernier : ils peuvent s'appuyer sur n'importe quel composant.
		// Ils sont lus dans les donnees de scan plutot que par getDeclaredMethods() : celle-ci charge
		// les types de TOUTES les signatures de la classe, et une seule methode qui mentionne une
		// classe de rendu suffit a faire refuser le chargement sur un serveur dedie.
		Map<String, List<ModFileScanData.AnnotationData>> callbacksByClass = new HashMap<>();
		for (ModFileScanData.AnnotationData a : annotations) {
			if (POST_CONSTRUCT.equals(a.annotationType()) && a.targetType() == ElementType.METHOD) {
				callbacksByClass.computeIfAbsent(a.clazz().getClassName(), k -> new ArrayList<>()).add(a);
			}
		}
		int callbacks = 0;
		for (Object bean : BEANS.values()) {
			callbacks += postConstruct(bean, modBus, callbacksByClass);
		}

		LOGGER.info("{} composants crees, {} champs statiques remplis, {} PostConstruct appeles{}",
			BEANS.size(), statics, callbacks, dedicatedServer ? " (serveur dedie : composants client ignores)" : "");
	}

	/**
	 * Remplit les champs {@link Autowired} d'un objet cree avec {@code new}.
	 *
	 * <p>Pendant l'initialisation, l'appel ne fait rien : un composant qui l'appelle depuis son
	 * propre constructeur sera rempli a l'etape 2, une fois tous les composants crees.</p>
	 */
	public static void inject(Object target) {
		if (initialising) return;
		if (!ready) {
			LOGGER.warn("{} cree avant l'initialisation du conteneur : ses champs @Autowired restent vides", target.getClass().getName());
			return;
		}
		injectInstanceFields(target);
	}

	private static void injectInstanceFields(Object target) {
		for (Class<?> c = target.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
			for (Field field : c.getDeclaredFields()) {
				if (!field.isAnnotationPresent(Autowired.class) || Modifier.isStatic(field.getModifiers()) || skipOnThisSide(field)) continue;
				set(field, target, resolve(field));
			}
		}
	}

	private static int postConstruct(Object bean, IEventBus modBus, Map<String, List<ModFileScanData.AnnotationData>> callbacksByClass) {
		int calls = 0;
		for (Class<?> c = bean.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
			for (ModFileScanData.AnnotationData a : callbacksByClass.getOrDefault(c.getName(), List.of())) {
				String member = a.memberName();
				int paren = member.indexOf('(');
				// Valeur absente = defaut MOD ; sinon Forge l'a enregistree comme EnumData.
				boolean game = a.annotationData().get("value") instanceof ModFileScanData.EnumData e && "GAME".equals(e.value());
				invoke(c, member.substring(0, paren), member.substring(paren), bean, game ? MinecraftForge.EVENT_BUS : modBus);
				calls++;
			}
		}
		return calls;
	}

	/**
	 * Un champ du code commun peut viser un composant client (BlockHooks garde ainsi le gestionnaire
	 * de couleur du feuillage). Sur un serveur dedie ce composant n'existe pas : le champ reste vide,
	 * comme sous Beanification, et le code qui s'en sert ne tourne que cote client.
	 */
	private static boolean skipOnThisSide(Field field) {
		return dedicatedServer && field.getType().getName().startsWith(CLIENT_PACKAGE);
	}

	private static Object resolve(Field field) {
		Class<?> wanted = field.getType();
		Object exact = BEANS.get(wanted);
		if (exact != null) return exact;

		Object found = null;
		for (Map.Entry<Class<?>, Object> entry : BEANS.entrySet()) {
			if (!wanted.isAssignableFrom(entry.getKey())) continue;
			if (found != null) {
				throw new IllegalStateException("Plusieurs composants conviennent pour " + describe(field) + " : type " + wanted.getName() + " ambigu");
			}
			found = entry.getValue();
		}
		if (found == null) {
			throw new IllegalStateException("Aucun composant de type " + wanted.getName() + " pour " + describe(field));
		}
		return found;
	}

	private static Class<?> load(String name) {
		try {
			return Class.forName(name, true, BeanContext.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Classe introuvable : " + name, e);
		}
	}

	private static Object construct(Class<?> type) {
		try {
			Constructor<?> constructor = type.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Le composant " + type.getName() + " n'a pas de constructeur vide", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Le constructeur du composant " + type.getName() + " a echoue", e.getCause());
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Impossible de creer le composant " + type.getName(), e);
		}
	}

	private static Field field(Class<?> owner, String name) {
		try {
			return owner.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Champ introuvable : " + owner.getName() + "." + name, e);
		}
	}

	private static void set(Field field, Object target, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Impossible de remplir " + describe(field), e);
		}
	}

	/**
	 * Appelle une methode par sa poignee : seule sa propre signature est resolue, pas celles du
	 * reste de la classe.
	 */
	private static void invoke(Class<?> owner, String name, String descriptor, Object bean, IEventBus bus) {
		String where = "@PostConstruct " + owner.getName() + "." + name + descriptor;
		MethodHandle handle;
		MethodType type;
		try {
			type = MethodType.fromMethodDescriptorString(descriptor, owner.getClassLoader());
			handle = MethodHandles.privateLookupIn(owner, MethodHandles.lookup()).findVirtual(owner, name, type);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Impossible de trouver " + where, e);
		}
		try {
			switch (type.parameterCount()) {
				case 0 -> handle.invoke(bean);
				case 1 -> {
					if (!type.parameterType(0).isAssignableFrom(IEventBus.class)) {
						throw new IllegalStateException(where + " : seul un IEventBus peut etre passe en parametre");
					}
					handle.invoke(bean, bus);
				}
				default -> throw new IllegalStateException(where + " : au plus un parametre");
			}
		} catch (IllegalStateException e) {
			throw e;
		} catch (Throwable e) {
			throw new IllegalStateException(where + " a echoue", e);
		}
	}

	private static String describe(Field field) {
		return field.getDeclaringClass().getName() + "." + field.getName();
	}
}
