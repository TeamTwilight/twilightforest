package twilightforest.beans;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public class TFBeanContext {

	static TFBeanContext INSTANCE = new TFBeanContext();

	private final Map<BeanDefinition<?>, Object> BEANS = new HashMap<>();
	boolean frozen = false;

	private TFBeanContext() {

	}

	/**
	 * @see #init(Consumer)
	 */
	public static void init() {
		init(null);
	}

	/**
	 * Should be called as early as possible to avoid null bean injections
	 */
	public static void init(@Nullable Consumer<TFBeanContext> context) {
		INSTANCE.initInternal(context);
	}

	private void initInternal(@Nullable Consumer<TFBeanContext> context) {
		if (frozen)
			throw new IllegalStateException("Bean Context already frozen");
		BEANS.clear();
		if (context != null)
			context.accept(this);
		ModFileScanData scanData = ModList.get().getModContainerById(TwilightForestMod.ID).orElseThrow().getModInfo().getOwningFile().getFile().getScanResult();
		try {

			List<Object> beans = new ArrayList<>(BEANS.values());

			for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(Component.class, ElementType.TYPE).toList()) {
				final String name = (String) data.annotationData().get("value");
				Class<?> c = Class.forName(data.clazz().getClassName());
				if (Objects.equals(Component.DEFAULT_VALUE, name)) {
					beans.add(registerInternal(c, null, c.getConstructor().newInstance()));
				} else {
					beans.add(registerInternal(c, name, c.getConstructor().newInstance()));
				}
			}

			for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(Bean.class, ElementType.METHOD).toList()) {
				final String name = (String) data.annotationData().get("value");
				Method method = Class.forName(data.clazz().getClassName()).getDeclaredMethod(data.memberName());
				if (Objects.equals(Component.DEFAULT_VALUE, name)) {
					beans.add(registerInternal(method.getReturnType(), null, method.invoke(null)));
				} else {
					beans.add(registerInternal(method.getReturnType(), name, method.invoke(null)));
				}
			}

			frozen = true;

			for (Object bean : beans) {
				for (Field field : bean.getClass().getDeclaredFields()) {
					if (field.isAnnotationPresent(Autowired.class) && !Modifier.isStatic(field.getModifiers())) {
						String name = field.getAnnotation(Autowired.class).value();
						field.trySetAccessible();
						if (Objects.equals(Component.DEFAULT_VALUE, name)) {
							field.set(null, inject(field.getType()));
						} else {
							field.set(null, inject(field.getType(), name));
						}
					}
				}
			}

			for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(Autowired.class, ElementType.FIELD).toList()) {
				final String name = (String) data.annotationData().get("value");
				Field field = Class.forName(data.clazz().getClassName()).getDeclaredField(data.memberName());
				if (Modifier.isStatic(field.getModifiers())) {
					field.trySetAccessible();
					if (Objects.equals(Component.DEFAULT_VALUE, name)) {
						field.set(null, inject(field.getType()));
					} else {
						field.set(null, inject(field.getType(), name));
					}
				}
			}

		} catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> void register(Class<T> type, T instance) {
		register(type, null, instance);
	}

	public <T> void register(Class<T> type, @Nullable String name, T instance) {
		registerInternal(type, name, instance);
	}

	private Object registerInternal(Class<?> type, @Nullable String name, Object instance) {
		if (frozen)
			throw new IllegalStateException("Bean Context already frozen");
		BeanDefinition<?> beanDefinition = new BeanDefinition<>(type, name);
		if (BEANS.containsKey(beanDefinition)) {
			final StringBuilder error = new StringBuilder("Class: ").append(type);
			if (name != null) {
				error.append(", Name: ").append(name);
			}
			throw new RuntimeException("Attempted to register a duplicate Bean." + error);
		}
		BEANS.put(beanDefinition, instance);
		return instance;
	}

	public static <T> T inject(Class<T> type) {
		return inject(type, null);
	}

	public static <T> T inject(Class<T> type, @Nullable String name) {
		return INSTANCE.injectInternal(type, name);
	}

	public <T> T injectInternal(Class<T> type, @Nullable String name) {
		if (!frozen)
			throw new IllegalStateException("Bean Context has not been initialized yet");
		return type.cast(Objects.requireNonNull(BEANS.get(new BeanDefinition<>(type, name))));
	}

	@SuppressWarnings("DataFlowIssue")
	public static <T> T blank() {
		return null;
	}

	private record BeanDefinition<T>(Class<T> type, @Nullable String name) {

		@Override
		public int hashCode() {
			return Objects.hash(type, name);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof BeanDefinition<?> other && type.equals(other.type()) && (name == null || Objects.equals(name, other.name));
		}
	}

}
