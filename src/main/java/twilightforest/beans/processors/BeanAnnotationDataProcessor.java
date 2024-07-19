package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Bean;
import twilightforest.beans.Component;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Objects;

public class BeanAnnotationDataProcessor implements AnnotationDataProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalRegistrar context, ModContainer modContainer, ModFileScanData scanData) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(Bean.class, ElementType.METHOD).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			Method method = Class.forName(data.clazz().getClassName()).getDeclaredMethod(data.memberName());
			Bean annotation = method.getAnnotation(Bean.class);
			context.register(method.getReturnType(), Objects.equals(Component.DEFAULT_VALUE, annotation.value()) ? null : annotation.value(), method.invoke(null));
		}
	}

}
