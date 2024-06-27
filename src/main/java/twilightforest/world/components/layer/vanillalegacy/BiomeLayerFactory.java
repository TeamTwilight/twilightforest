package twilightforest.world.components.layer.vanillalegacy;

import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;

import java.util.function.LongFunction;

public interface BiomeLayerFactory {
	LazyArea build(LongFunction<LazyAreaContext> contextFactory);

	BiomeLayerType getType();
}
