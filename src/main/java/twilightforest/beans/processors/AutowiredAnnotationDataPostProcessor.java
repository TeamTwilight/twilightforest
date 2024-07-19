package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.beans.PostConstruct;
import twilightforest.beans.TFBeanContext;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@BeanProcessor
public class AutowiredAnnotationDataPostProcessor implements AnnotationDataPostProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModFileScanData scanData, Object bean, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(Autowired.class, ElementType.FIELD).filter(a -> a.clazz().getClass().equals(bean.getClass())).iterator(); it.hasNext(); ) {
			Field field = bean.getClass().getDeclaredField(it.next().memberName());
			if (field.isAnnotationPresent(Autowired.class)) {
				currentInjectionTarget.set(field);
				if (Modifier.isStatic(field.getModifiers())) {
					throw new IllegalStateException("@Autowired fields must be non-static inside Beans");
				}
				String name = field.getAnnotation(Autowired.class).value();
				field.trySetAccessible();
				field.set(null, context.inject(field.getType(), Objects.equals(Component.DEFAULT_VALUE, name) ? null : name));
			}
		}
	}

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(Autowired.class, ElementType.FIELD).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			Class<?> type = Class.forName(data.clazz().getClassName());
			Field field = type.getDeclaredField(data.memberName());
			currentInjectionTarget.set(field);
			Autowired annotation = field.getAnnotation(Autowired.class);
			final @Nullable String name = Objects.equals(Component.DEFAULT_VALUE, annotation.value()) ? null : annotation.value();
			if (Modifier.isStatic(field.getModifiers())) {
				field.trySetAccessible();
				field.set(null, context.inject(field.getType(), name));
			} else if (!context.contains(type, name)) {
				throw new IllegalStateException("@Autowired fields must be static outside of Beans");
			}
		}
	}

}
