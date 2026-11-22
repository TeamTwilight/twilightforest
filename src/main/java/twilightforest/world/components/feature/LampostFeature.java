package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;

public record LampostFeature(BlockState state) implements Feature {

	private static final Rotation[] ROTATIONS = Rotation.values();

	public static final MapCodec<LampostFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(LampostFeature::state)
	).apply(instance, LampostFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		// we should start on a grass block
		if (worldGenLevel.getBlockState(blockPos.below()).getBlock() != Blocks.GRASS_BLOCK) {
			return false;
		}

		// generate a height
		int height = 1 + randomSource.nextInt(4);

		// is it air or replaceable above our grass block
		for (int dy = 0; dy <= height; dy++) {
			BlockState state = worldGenLevel.getBlockState(blockPos.above(dy));
			if (!state.isAir() && !state.canBeReplaced()) {
				return false;
			}
		}

		// generate lamp
		for (int dy = 0; dy < height; dy++) {
			worldGenLevel.setBlock(blockPos.above(dy), TFBlocks.CANOPY_FENCE.get().defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		}
		worldGenLevel.setBlock(blockPos.above(height), state().rotate(ROTATIONS[randomSource.nextInt(ROTATIONS.length)]), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		return true;
	}
}
