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
			final String name = (String) data.annotationData().get("value");
			Method method = Class.forName(data.clazz().getClassName()).getDeclaredMethod(data.memberName());
			context.register(method.getReturnType(), Objects.equals(Component.DEFAULT_VALUE, name) ? null : name, method.invoke(null));
		}
	}

}
