package twilightforest.beans;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.IModBusEvent;

/**
 * Can be posted to the Mod {@link net.neoforged.bus.api.IEventBus} immediately in your main mod class constructor to be processed by {@link twilightforest.beans.processors.AnnotationDataPostProcessor}
 */
public class ProcessBeanAnnotationsEvent extends Event implements IModBusEvent {

	private final Object toProcess;

	public ProcessBeanAnnotationsEvent(Object mainModClass) {
		if (!mainModClass.getClass().isAnnotationPresent(Mod.class))
			throw new IllegalArgumentException("Only classes annotated with @Mod can be supplied");

		toProcess = mainModClass;
	}

	public Object getObjectToProcess() {
		return toProcess;
	}

}
