package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.beans.TFBeanContext;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AutowiredAnnotationDataProcessor implements AnnotationDataPostProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, Object bean, AtomicReference<Field> currentInjectionTarget) throws Throwable {
		for (Field field : bean.getClass().getDeclaredFields()) {
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
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Field> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(Autowired.class, ElementType.FIELD).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			final String value = (String) data.annotationData().get("value");
			final @Nullable String name = Objects.equals(Component.DEFAULT_VALUE, value) ? null : value;
			Class<?> type = Class.forName(data.clazz().getClassName());
			Field field = type.getDeclaredField(data.memberName());
			currentInjectionTarget.set(field);
			if (Modifier.isStatic(field.getModifiers())) {
				field.trySetAccessible();
				field.set(null, context.inject(field.getType(), name));
			} else if (!context.contains(type, name)) {
				throw new IllegalStateException("@Autowired fields must be static outside of Beans");
			}
		}
	}

}
