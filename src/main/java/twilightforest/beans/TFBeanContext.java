package twilightforest.beans;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.TwilightForestMod;
import twilightforest.beans.processors.*;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TFBeanContext {

	static TFBeanContext INSTANCE = new TFBeanContext();

	private final Logger logger = LogManager.getLogger(TFBeanContext.class);

	private final Map<BeanDefinition<?>, Object> BEANS = new HashMap<>();
	private boolean frozen = false;

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
	public static void init(@Nullable Consumer<TFBeanContextRegistrar> context) {
		INSTANCE.initInternal(context);
	}

	private void initInternal(@Nullable Consumer<TFBeanContextRegistrar> context) {
		logger.info("Starting Bean Context");
		if (frozen)
			throw new IllegalStateException("Bean Context already frozen");
		BEANS.clear();

		registerInternal(TFBeanContext.class, null, this);

		if (context != null)
			context.accept(TFBeanContextRegistrar.SINGLETON);

		ModContainer modContainer = ModList.get().getModContainerById(TwilightForestMod.ID).orElseThrow();
		ModFileScanData scanData = modContainer.getModInfo().getOwningFile().getFile().getScanResult();
		AtomicReference<Field> currentInjection = new AtomicReference<>();
		try {
			logger.info("Registering Bean annotation processors");
			List<AnnotationDataProcessor> annotationDataProcessors = new ArrayList<>();
			List<AnnotationDataPostProcessor> annotationDataPostProcessors = new ArrayList<>();

			for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(BeanProcessor.class, ElementType.TYPE).iterator(); it.hasNext(); ) {
				Class<?> c = Class.forName(it.next().clazz().getClassName());
				if (AnnotationDataProcessor.class.isAssignableFrom(c)) {
					annotationDataProcessors.add((AnnotationDataProcessor) c.getConstructor().newInstance());
					logger.info("Registered Bean annotation processor: {}", c);
				} else if (AnnotationDataPostProcessor.class.isAssignableFrom(c)) {
					annotationDataPostProcessors.add((AnnotationDataPostProcessor) c.getConstructor().newInstance());
					logger.info("Registered Bean annotation post processor: {}", c);
				}
			}

			for (AnnotationDataProcessor annotationDataProcessor : annotationDataProcessors) {
				logger.info("Running processor {}", annotationDataProcessor.getClass());
				annotationDataProcessor.process(TFBeanContextInternalRegistrar.SINGLETON, modContainer, scanData);
			}

			frozen = true;

			for (Object bean : BEANS.values()) {
				for (AnnotationDataPostProcessor annotationDataPostProcessor : annotationDataPostProcessors) {
					annotationDataPostProcessor.process(TFBeanContextInternalInjector.SINGLETON, bean, currentInjection);
				}
			}

			for (AnnotationDataPostProcessor annotationDataPostProcessor : annotationDataPostProcessors) {
				logger.info("Running post processor {}", annotationDataPostProcessor.getClass());
				annotationDataPostProcessor.process(TFBeanContextInternalInjector.SINGLETON, modContainer, scanData, currentInjection);
			}

			logger.info("Bean Context loaded");
		} catch (Throwable e) {
			throw new RuntimeException(
				"Bean injection failed." + (currentInjection.get() == null ? "" : (
					" At field: " + currentInjection.get().getDeclaringClass() + "#" + currentInjection.get().getName()
				)), e);
		}
	}

	private void registerInternal(Class<?> type, @Nullable String name, Object instance) {
		logger.info("Registering Bean {} {}", type, name == null ? "" : ("with name: " + name));
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
	}

	public static <T> T inject(Class<T> type) {
		return inject(type, null);
	}

	public static <T> T inject(Class<T> type, @Nullable String name) {
		return INSTANCE.injectInternal(type, name);
	}

	<T> T injectInternal(Class<T> type, @Nullable String name) {
		if (!frozen)
			throw new IllegalStateException("Bean Context has not been initialized yet");
		return type.cast(Objects.requireNonNull(BEANS.get(new BeanDefinition<>(type, name)), "Trying to inject Bean: " + type + (name == null ? "" : " (" + name + ")")));
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

	public static final class TFBeanContextRegistrar {

		static final TFBeanContextRegistrar SINGLETON = new TFBeanContextRegistrar();

		private TFBeanContextRegistrar() {

		}

		public <T> void register(Class<T> type, T instance) {
			register(type, null, instance);
		}

		public <T> void register(Class<T> type, @Nullable String name, T instance) {
			INSTANCE.registerInternal(type, name, instance);
		}

	}

	public static final class TFBeanContextInternalRegistrar {

		static final TFBeanContextInternalRegistrar SINGLETON = new TFBeanContextInternalRegistrar();

		private TFBeanContextInternalRegistrar() {

		}

		public void register(Class<?> type, @Nullable String name, Object instance) {
			INSTANCE.registerInternal(type, name, instance);
		}

	}

	public static final class TFBeanContextInternalInjector {

		static final TFBeanContextInternalInjector SINGLETON = new TFBeanContextInternalInjector();

		private TFBeanContextInternalInjector() {

		}

		public <T> T inject(Class<T> type, @Nullable String name) {
			return INSTANCE.injectInternal(type, name);
		}

		public boolean contains(Class<?> type, @Nullable String name) {
			return INSTANCE.BEANS.containsKey(new BeanDefinition<>(type, name));
		}

	}

}
