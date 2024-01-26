package twilightforest.world.components.chunkblanketing;

import com.mojang.serialization.Codec;

@FunctionalInterface
public interface ChunkBlanketType {
    Codec<? extends ChunkBlanketProcessor> getCodec();
}
