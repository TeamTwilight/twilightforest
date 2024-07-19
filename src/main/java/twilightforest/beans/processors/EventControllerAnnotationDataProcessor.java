package twilightforest.beans.processors;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Component;
import twilightforest.beans.EventController;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.Consumer;

public class EventControllerAnnotationDataProcessor implements AnnotationDataProcessor {

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

	@Override
	public void process(TFBeanContext.TFBeanContextInternalRegistrar context, ModContainer modContainer, ModFileScanData scanData) throws Throwable {
		for (ModFileScanData.AnnotationData data : scanData.getAnnotatedBy(EventController.class, ElementType.TYPE).toList()) {
			final String name = (String) data.annotationData().get("value");
			Class<?> c = Class.forName(data.clazz().getClassName());
			Object instance = c.getConstructor().newInstance();
			context.register(c, Objects.equals(Component.DEFAULT_VALUE, name) ? null : name, instance);
			for (Method method : c.getDeclaredMethods()) {
				if (method.isAnnotationPresent(SubscribeEvent.class)) {
					if (Modifier.isStatic(method.getModifiers())) {
						throw new IllegalStateException("@EventController beans must use non-static methods when using @SubscribeEvent. At: " + c + "#" + method.getName());
					}
					Class<?>[] params = method.getParameterTypes();
					if (params.length != 1) {
						throw new IllegalStateException("@EventController beans must use single parameter methods when using @SubscribeEvent. At: " + c + "#" + method.getName());
					}
					Class<Event> eventClass = fakeCast(params[0]);
					MethodType consumerType = MethodType.methodType(void.class, eventClass);
					MethodHandle handle = LOOKUP.unreflect(method);
					final CallSite site = LambdaMetafactory.metafactory(LOOKUP, "accept",
						MethodType.methodType(Consumer.class, c),
						consumerType.changeParameterType(0, Object.class),
						handle,
						consumerType);
					Consumer<Event> ref = fakeCast(site.getTarget().invoke(instance));
					SubscribeEvent subscribeEventData = method.getAnnotation(SubscribeEvent.class);
					switch ((EventBusSubscriber.Bus) data.annotationData().get("bus")) {
						case GAME -> NeoForge.EVENT_BUS.addListener(subscribeEventData.priority(), subscribeEventData.receiveCanceled(), eventClass, ref);
						case MOD -> Objects.requireNonNull(modContainer.getEventBus()).addListener(subscribeEventData.priority(), subscribeEventData.receiveCanceled(), eventClass, ref);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T fakeCast(Object o) {
		return (T) o;
	}

}
