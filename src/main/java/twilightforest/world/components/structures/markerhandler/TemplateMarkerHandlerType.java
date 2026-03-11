package twilightforest.world.components.structures.markerhandler;

import com.mojang.serialization.MapCodec;

@FunctionalInterface
public interface TemplateMarkerHandlerType {
	MapCodec<? extends TemplateMarkerHandler> getCodec();
}
