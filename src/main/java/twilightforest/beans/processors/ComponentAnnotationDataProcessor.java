package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.Component;
import twilightforest.beans.DistAnnotationRetriever;
import twilightforest.beans.TFBeanContext;

import java.lang.annotation.ElementType;
import java.util.Iterator;
import java.util.Objects;

@BeanProcessor
public class ComponentAnnotationDataProcessor implements AnnotationDataProcessor {

	@Override
	public void process(TFBeanContext.TFBeanContextInternalRegistrar context, ModContainer modContainer, ModFileScanData scanData) throws Throwable {
		for (Iterator<ModFileScanData.AnnotationData> it = DistAnnotationRetriever.retrieve(scanData, Component.class, ElementType.TYPE).iterator(); it.hasNext(); ) {
			ModFileScanData.AnnotationData data = it.next();
			Class<?> c = Class.forName(data.clazz().getClassName());
			Component annotation = c.getAnnotation(Component.class);
			context.register(c, Objects.equals(Component.DEFAULT_VALUE, annotation.value()) ? null : annotation.value(), c.getConstructor().newInstance());
		}
	}

}
