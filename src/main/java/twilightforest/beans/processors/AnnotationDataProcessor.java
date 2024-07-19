package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.TFBeanContext;

public interface AnnotationDataProcessor {

	void process(TFBeanContext.TFBeanContextInternalRegistrar context, ModContainer modContainer, ModFileScanData scanData) throws Throwable;

}
