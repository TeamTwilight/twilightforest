package twilightforest.beans.processors;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import twilightforest.beans.DistAnnotationRetriever;
import twilightforest.beans.PostConstruct;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@BeanProcessor(priority = 1)
public class PostConstructAnnotationDataPostProcessor implements AnnotationDataPostProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, Object bean, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = DistAnnotationRetriever.retrieve(scanData, PostConstruct.class, ElementType.METHOD).filter(a -> a.clazz().equals(Type.getType(bean.getClass()))).iterator(); it.hasNext(); ) {
			String name = it.next().memberName().split("\\(")[0];
			List<Method> methods = new ArrayList<>();
			try {
				methods.add(bean.getClass().getDeclaredMethod(name));
			} catch (NoSuchMethodException ex) {
				// NO-OP
			}
			try {
				methods.add(bean.getClass().getDeclaredMethod(name, IEventBus.class));
			} catch (NoSuchMethodException ex) {
				// NO-OP
			}
			for (Method method : methods) {
				if (method.isAnnotationPresent(PostConstruct.class)) {
					currentInjectionTarget.set(method);

					if (Modifier.isStatic(method.getModifiers())) {
						throw new IllegalStateException("@PostConstruct methods must be non-static");
					}

					method.trySetAccessible();

					if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(IEventBus.class)) {
						method.invoke(bean, modContainer.getEventBus());
					} else {
						if (method.getParameterCount() != 0) {
							throw new IllegalStateException("@PostConstruct methods must not have parameters or only have one IEventBus parameter");
						}

						method.invoke(bean);
					}
				}
			}
		}
	}

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Object> currentInjectionTarget) throws Throwable {

	}

}
