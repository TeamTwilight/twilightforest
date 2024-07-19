package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.PostConstruct;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

@BeanProcessor(priority = 1)
public class PostConstructAnnotationDataPostProcessor implements AnnotationDataPostProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModFileScanData scanData, Object bean, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(PostConstruct.class, ElementType.METHOD).filter(a -> a.clazz().getClass().equals(bean.getClass())).iterator(); it.hasNext(); ) {
			Method method = bean.getClass().getDeclaredMethod(it.next().memberName());
			if (method.isAnnotationPresent(PostConstruct.class)) {
				currentInjectionTarget.set(method);

				if (Modifier.isStatic(method.getModifiers())) {
					throw new IllegalStateException("@PostConstruct methods must be non-static");
				}

				if (method.getParameterCount() != 0) {
					throw new IllegalStateException("@PostConstruct methods must not have parameters");
				}

				method.trySetAccessible();
				method.invoke(bean);
			}
		}
	}

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Object> currentInjectionTarget) throws Throwable {

	}

}
