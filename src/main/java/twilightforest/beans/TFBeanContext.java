package twilightforest.beans;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.TwilightForestMod;
import twilightforest.beans.processors.*;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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
		INSTANCE.initInternal(context, false);
	}

	@SuppressWarnings("SameParameterValue")
	private void initInternal(@Nullable Consumer<TFBeanContextRegistrar> context, boolean forceInjectRegistries) {
		logger.info("Starting Bean Context");
		if (frozen)
			throw new IllegalStateException("Bean Context already frozen");
		BEANS.clear();

		registerInternal(TFBeanContext.class, null, this);

		if (context != null)
			context.accept(TFBeanContextRegistrar.SINGLETON);

		ModContainer modContainer = ModList.get().getModContainerById(TwilightForestMod.ID).orElseThrow();
		ModFileScanData scanData = modContainer.getModInfo().getOwningFile().getFile().getScanResult();
		AtomicReference<Object> currentInjection = new AtomicReference<>();
		try {
			logger.info("Registering Bean annotation processors");
			List<AnnotationDataProcessor> annotationDataProcessors = new ArrayList<>();
			List<AnnotationDataPostProcessor> annotationDataPostProcessors = new ArrayList<>();

			for (Iterator<? extends Class<?>> it = scanData.getAnnotatedBy(BeanProcessor.class, ElementType.TYPE)
				.map(a -> {
					try {
						return Class.forName(a.clazz().getClassName());
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				})
				.sorted(Comparator.comparingInt(c -> c.getAnnotation(BeanProcessor.class).priority()))
				.iterator(); it.hasNext(); ) {
				Class<?> c = it.next();
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
					annotationDataPostProcessor.process(TFBeanContextInternalInjector.SINGLETON, scanData, bean, currentInjection);
				}
			}

			currentInjection.set(null);

			for (AnnotationDataPostProcessor annotationDataPostProcessor : annotationDataPostProcessors) {
				logger.info("Running post processor {}", annotationDataPostProcessor.getClass());
				annotationDataPostProcessor.process(TFBeanContextInternalInjector.SINGLETON, modContainer, scanData, currentInjection);
			}

			currentInjection.set(null);

			Objects.requireNonNull(modContainer.getEventBus()).addListener(FMLCommonSetupEvent.class, event -> injectRegistries(scanData, annotationDataPostProcessors));
			if (forceInjectRegistries)
				injectRegistries(scanData, annotationDataPostProcessors);

			logger.info("Bean Context loaded");
		} catch (Throwable e) {
			throwInjectionFailedException(currentInjection, e);
		}
	}

	private void throwInjectionFailedException(AtomicReference<Object> o, Throwable e) {
		throw new RuntimeException("Bean injection failed." + (o.get() == null ? "" : (" At: " + o)), e);
	}

	private void injectRegistries(ModFileScanData scanData, List<AnnotationDataPostProcessor> annotationDataPostProcessors) {
		logger.info("Processing registry objects");
		AtomicReference<Object> curInj = new AtomicReference<>();
		BuiltInRegistries.REGISTRY.holders().flatMap(r -> r.value().holders()).forEach(holder -> {
			try {
				Object o = holder.value();
				if (o.getClass().isAnnotationPresent(Configurable.class)) {
					for (AnnotationDataPostProcessor annotationDataPostProcessor : annotationDataPostProcessors) {
						annotationDataPostProcessor.process(TFBeanContextInternalInjector.SINGLETON, scanData, o, curInj);
					}
				}
			} catch (Throwable e) {
				throwInjectionFailedException(curInj, e);
			}
		});
		logger.info("Finished processing registry objects");
	}

	public boolean isFrozen() {
		return frozen;
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

	record BeanDefinition<T>(Class<T> type, @Nullable String name) {

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
