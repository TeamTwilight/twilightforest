package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Component;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.util.Iterator;
import java.util.Objects;

public class ComponentAnnotationDataProcessor implements AnnotationDataProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalRegistrar context, ModContainer modContainer, ModFileScanData scanData) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = scanData.getAnnotatedBy(Component.class, ElementType.TYPE).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			final String name = (String) data.annotationData().get("value");
			Class<?> c = Class.forName(data.clazz().getClassName());
			context.register(c, Objects.equals(Component.DEFAULT_VALUE, name) ? null : name, c.getConstructor().newInstance());
		}
	}

}
