package twilightforest.forge.datamap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chargement et lecture des tables de donnees.
 *
 * <p>Le format de fichier est celui de NeoForge, pour que la generation de donnees du mod reste
 * utilisable sans modification : {@code data/<espace>/data_maps/<registre>/<nom>.json}, contenant
 * un objet {@code values} qui associe un identifiant d'entree a sa valeur.</p>
 *
 * <p>NeoForge ajoute {@code getData} sur {@code Holder}. Java ne permettant pas d'ajouter une
 * methode a une classe existante, la lecture passe par {@link #get} — meme choix que pour les
 * attachements.</p>
 */
public final class DataMaps extends SimplePreparableReloadListener<List<DataMaps.Loaded>> {
	private DataMaps() {}

	public static final DataMaps INSTANCE = new DataMaps();

	private static final List<DataMapType<?, ?>> DECLARED = new ArrayList<>();

	/** Une entree lue dans un fichier : un identifiant, ou un tag si la cle commence par {@code #}. */
	record Loaded(DataMapType<?, ?> type, @Nullable ResourceLocation entry, @Nullable TagKey<?> tag, Object value) {}

	static <R, V> DataMapType<R, V> declare(DataMapType<R, V> type) {
		DECLARED.add(type);
		return type;
	}

	/**
	 * La valeur associee a une entree de registre, ou {@code null}.
	 *
	 * <p>L'entree est designee par son {@link Holder}, comme chez NeoForge.</p>
	 */
	@Nullable
	public static <R, V> V get(Holder<R> holder, DataMapType<R, V> type) {
		return type.lookup(holder);
	}

	/** Le chemin du fichier d'une table, dans la convention de NeoForge. */
	private static String pathOf(DataMapType<?, ?> type) {
		ResourceLocation registry = type.registry().location();
		String folder = registry.getNamespace().equals("minecraft")
				? registry.getPath()
				: registry.getNamespace() + "/" + registry.getPath();
		return "data_maps/" + folder + "/" + type.id().getPath() + ".json";
	}

	@Override
	protected List<Loaded> prepare(ResourceManager manager, ProfilerFiller profiler) {
		List<Loaded> loaded = new ArrayList<>();

		for (DataMapType<?, ?> type : DECLARED) {
			String path = pathOf(type);

			// N'importe quel espace de noms peut alimenter une table : on prend toutes les piles.
			for (Map.Entry<ResourceLocation, List<Resource>> e :
					manager.listResourceStacks("data_maps", rl -> rl.getPath().equals(path)).entrySet()) {
				for (Resource resource : e.getValue()) {
					read(type, resource, loaded);
				}
			}
		}

		return loaded;
	}

	private static <R, V> void read(DataMapType<R, V> type, Resource resource, List<Loaded> out) {
		try (BufferedReader reader = resource.openAsReader()) {
			JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

			if (!root.has("values")) {
				return;
			}

			for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject("values").entrySet()) {
				type.codec().parse(com.mojang.serialization.JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(err -> TwilightForestMod.LOGGER.warn(
								"Table {} : entree {} illisible ({})", type.id(), entry.getKey(), err))
						.ifPresent(v -> {
							String key = entry.getKey();
							// NeoForge accepte un tag comme cle (« #minecraft:coal_ores ») : le lire tel
							// quel comme identifiant faisait echouer tout le fichier.
							if (key.startsWith("#")) {
								out.add(new Loaded(type, null, TagKey.create(type.registry(), ResourceLocation.parse(key.substring(1))), v));
							} else {
								out.add(new Loaded(type, ResourceLocation.parse(key), null, v));
							}
						});
			}
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Table {} : fichier illisible", type.id(), e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void apply(List<Loaded> loaded, ResourceManager manager, ProfilerFiller profiler) {
		DECLARED.forEach(DataMapType::clear);

		for (Loaded l : loaded) {
			DataMapType<Object, Object> type = (DataMapType<Object, Object>) l.type();
			if (l.tag() != null) {
				type.putTag((TagKey<Object>) l.tag(), l.value());
			} else {
				type.put(l.entry(), l.value());
			}
		}
	}
}
