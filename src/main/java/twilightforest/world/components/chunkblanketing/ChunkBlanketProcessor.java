package twilightforest.world.components.chunkblanketing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Function;

/**
 * An alternative for features, surface rules, or an entirely custom chunk generator.
 * Useful for ensuring modifications (largely blanketing for particular biomes) are applied to chunk while ensuring generation order is respected.
 * Such modifications making best use of this interface typically involve adding new blocks not part of the
 * original terrain landmass, and have nothing to do with the surface rules.
 * <br><br>
 * For now, running multiple ChunkBlanketProcessors in a single biome should be considered as relying on undefined behavior.
 * */
public interface ChunkBlanketProcessor {
    /**
     * @return Biomes that permits the backend to run the implemented processor on this chunk
     */
    HolderSet<Biome> biomesForApplication();

    /**
     * Guaranteed, this method will run once and never again for each chunk in world generation.
     * @param random RNG for passing into any BlockStateProviders. Deterministically random but different for each chunk and also changes based on stack order of ChunkBlanketProcessor for the current chunk.
     * @param biomeGetter A function for obtaining biomes per-block, in respect to noise per-block distortion of biomes existing in 4x4x4 in a chunk section.
     * @param chunkAccess The chunk to modify blocks.
     */
    void processChunk(RandomSource random, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunkAccess);

    /**
     * @return Supplier[Codec[? extends ChunkBlanketProcessor]]
     */
    ChunkBlanketType getType();

    /**
     * A simplification of ChunkBlanketProcessor where biome-masking is applied, useful if desired implementation only
     * cares to blindly place blocks among X-Z columns based off of the terrain level.
     */
    interface SimpleProcessor extends ChunkBlanketProcessor {
        @Override
        default void processChunk(RandomSource random, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunkAccess) {
            for (int dX = 0; dX < 16; dX++) {
                for (int dZ = 0; dZ < 16; dZ++) {
                    BlockPos firstAvailableBlock = chunkAccess.getPos().getBlockAt(dX, chunkAccess.getHeight(this.heightmap(), dX, dZ) + 1, dZ);

                    if (!this.biomesForApplication().contains(biomeGetter.apply(firstAvailableBlock)))
                        continue;

                    this.processColumn(random, chunkAccess, firstAvailableBlock);
                }
            }
        }

        void processColumn(RandomSource random, ChunkAccess chunkAccess, BlockPos aboveFloor);

        Heightmap.Types heightmap();
    }
}
