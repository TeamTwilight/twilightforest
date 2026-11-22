package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public record HugeWaterLilyFeature() implements Feature {

	public static final MapCodec<HugeWaterLilyFeature> MAP_CODEC = MapCodec.unit(HugeWaterLilyFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		for (int i = 0; i < 4; i++) {
			BlockPos pos = blockPos.offset(
				randomSource.nextInt(8) - randomSource.nextInt(8),
				randomSource.nextInt(4) - randomSource.nextInt(4),
				randomSource.nextInt(8) - randomSource.nextInt(8)
			);

			if (shouldPlacePadAt(worldGenLevel, pos)) {
				worldGenLevel.setBlock(pos, TFBlocks.HUGE_WATER_LILY.get().defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER);
	}
}
