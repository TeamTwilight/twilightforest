package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import twilightforest.beans.*;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@BeanProcessor
public class AutowiredAnnotationDataPostProcessor implements AnnotationDataPostProcessor {

	private boolean classOrSuperEquals(Type clazz, Class<?> c) {
		return clazz.equals(Type.getType(c)) || (c.getSuperclass() instanceof Class<?> sup && classOrSuperEquals(clazz, sup));
	}

	private List<Field> getAllConfigurableFieldsIncludingSuper(Class<?> c, String name, String value) {
		List<Field> list =  new ArrayList<>();
		getAllConfigurableFieldsIncludingSuper(c, name, value, list);
		return list;
	}

	private void getAllConfigurableFieldsIncludingSuper(Class<?> c, String name, String value, List<Field> list) {
		try {
			Field f = c.getDeclaredField(name);
			if (f.isAnnotationPresent(Autowired.class) && Objects.equals(f.getAnnotation(Autowired.class).value(), value))
				list.add(f);
		} catch (NoSuchFieldException ex) {
			// NO-OP
		}
		Class<?> sup = c.getSuperclass();
		if (sup != null)
			getAllConfigurableFieldsIncludingSuper(sup, name, value, list);
	}

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, Object bean, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = DistAnnotationRetriever.retrieve(scanData, Autowired.class, ElementType.FIELD)
			.filter(a -> classOrSuperEquals(a.clazz(), bean.getClass())).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			Optional<String> name = Optional.ofNullable(data.annotationData().get("value"))
				.filter(String.class::isInstance)
				.map(String.class::cast);
			for (Field field : getAllConfigurableFieldsIncludingSuper(bean.getClass(), data.memberName(), name.orElse(Component.DEFAULT_VALUE))) {
				currentInjectionTarget.set(field);
				if (Modifier.isStatic(field.getModifiers())) {
					throw new IllegalStateException("@Autowired fields must be non-static inside Beans");
				}
				field.trySetAccessible();
				field.set(bean, context.inject(field.getType(), name.filter(s -> !s.equals(Component.DEFAULT_VALUE)).orElse(null)));
			}
		}
	}

	@Override
	public void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Object> currentInjectionTarget) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = DistAnnotationRetriever.retrieve(scanData, Autowired.class, ElementType.FIELD).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			currentInjectionTarget.set(data.clazz());
			Class<?> type = Class.forName(data.clazz().getClassName());
			if (type.isAnnotationPresent(Configurable.class) || type.isAnnotationPresent(Component.class))
				continue;
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
