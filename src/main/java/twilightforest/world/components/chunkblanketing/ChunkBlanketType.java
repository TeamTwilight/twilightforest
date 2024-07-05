package twilightforest.world.components.chunkblanketing;

import com.mojang.serialization.MapCodec;

@FunctionalInterface
public interface ChunkBlanketType {
	MapCodec<? extends ChunkBlanketProcessor> getCodec();
}
