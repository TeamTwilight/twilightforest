package twilightforest.beans;

import twilightforest.beans.multipart.MultipartEntityUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO: look into annotation processing
public class TFBeanContext {

	private static final Map<BeanDefinition<?>, Object> BEANS = new HashMap<>();

	private TFBeanContext() {

	}

	public static void init() {
		register(MultipartEntityUtil.class, new MultipartEntityUtil());
	}

	private static <T> void register(Class<T> type, T instance) {
		register(type, null, instance);
	}

	private static <T> void register(Class<T> type, @Nullable String name, T instance) {
		BeanDefinition<T> beanDefinition = new BeanDefinition<>(type, name);
		if (BEANS.containsKey(beanDefinition)) {
			final StringBuilder error = new StringBuilder("Class: ").append(type);
			if (name != null) {
				error.append(", Name: ").append(name);
			}
			throw new RuntimeException("Attempted to register a duplicate Bean." + error);
		}
		BEANS.put(beanDefinition, instance);
	}

	public static <T> T lookup(Class<T> type) {
		return lookup(type, null);
	}

	public static <T> T lookup(Class<T> type, @Nullable String name) {
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
