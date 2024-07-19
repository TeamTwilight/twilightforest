package twilightforest.beans.processors;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;
import twilightforest.beans.TFBeanContext;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public interface AnnotationDataPostProcessor {

	void process(TFBeanContext.TFBeanContextInternalInjector context, Object bean, AtomicReference<Field> currentInjectionTarget) throws Throwable;

	void process(TFBeanContext.TFBeanContextInternalInjector context, ModContainer modContainer, ModFileScanData scanData, AtomicReference<Field> currentInjectionTarget) throws Throwable;

}
