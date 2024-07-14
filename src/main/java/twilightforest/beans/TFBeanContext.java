package twilightforest.beans;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TFBeanContext {

	private static final Map<BeanDefinition<?>, Object> BEANS = new HashMap<>();

	private TFBeanContext() {

	}

	public static void init() {
		BEANS.clear();
		ModFileScanData scanData = ModList.get().getModContainerById(TwilightForestMod.ID).orElseThrow().getModInfo().getOwningFile().getFile().getScanResult();
		try {

			for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(Component.class, ElementType.TYPE).toList()) {
				final String name = (String) data.annotationData().get("value");
				Class<?> c = Class.forName(data.clazz().getClassName());
				if (Objects.equals(Component.DEFAULT_VALUE, name)) {
					registerInternal(c, null, c.getConstructor().newInstance());
				} else {
					registerInternal(c, name, c.getConstructor().newInstance());
				}
			}

			for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(Bean.class, ElementType.METHOD).toList()) {
				final String name = (String) data.annotationData().get("value");
				Method method = Class.forName(data.clazz().getClassName()).getDeclaredMethod(data.memberName());
				if (Objects.equals(Component.DEFAULT_VALUE, name)) {
					registerInternal(method.getReturnType(), null, method.invoke(null));
				} else {
					registerInternal(method.getReturnType(), name, method.invoke(null));
				}
			}

		} catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> void register(Class<T> type, T instance) {
		register(type, null, instance);
	}

	public static <T> void register(Class<T> type, @Nullable String name, T instance) {
		registerInternal(type, name, instance);
	}

	private static void registerInternal(Class<?> type, @Nullable String name, Object instance) {
		BeanDefinition<?> beanDefinition = new BeanDefinition<>(type, name);
		if (BEANS.containsKey(beanDefinition)) {
			final StringBuilder error = new StringBuilder("Class: ").append(type);
			if (name != null) {
				error.append(", Name: ").append(name);
			}
			throw new RuntimeException("Attempted to register a duplicate Bean." + error);
		}
		BEANS.put(beanDefinition, instance);
	}

	public static <T> T inject(Class<T> type) {
		return inject(type, null);
	}

	public static <T> T inject(Class<T> type, @Nullable String name) {
		return type.cast(Objects.requireNonNull(BEANS.get(new BeanDefinition<>(type, name))));
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
