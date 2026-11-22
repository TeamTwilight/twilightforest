package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public record WebFeature() implements Feature {

	public static final MapCodec<WebFeature> MAP_CODEC = MapCodec.unit(WebFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	private static boolean isValidMaterial(BlockState state) {
		return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		BlockPos pos = blockPos.above(randomSource.nextInt(worldGenLevel.getMaxY() - blockPos.getY()));
		while (pos.getY() > blockPos.getY()) {
			pos = pos.below();
			BlockState state = worldGenLevel.getBlockState(pos);
			if (worldGenLevel.isEmptyBlock(pos.below()) && isValidMaterial(state)) {
				worldGenLevel.setBlock(state.is(BlockTags.LEAVES) && randomSource.nextBoolean() ? pos : pos.below(), Blocks.COBWEB.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				return true;
			}
		}

		return false;
	}
}
