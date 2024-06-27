package twilightforest.world.components.layer.vanillalegacy;

import com.mojang.serialization.MapCodec;

@FunctionalInterface
public interface BiomeLayerType {
	MapCodec<? extends BiomeLayerFactory> getCodec();
}
