package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public record SnowUnderTreeFeature() implements Feature {

	public static final MapCodec<SnowUnderTreeFeature> MAP_CODEC = MapCodec.unit(SnowUnderTreeFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mPosDown = new BlockPos.MutableBlockPos();

		for (int xi = 0; xi < 16; xi++) {
			for (int zi = 0; zi < 16; zi++) {
				int x = blockPos.getX() + xi;
				int z = blockPos.getZ() + zi;
				mPos.set(x, worldGenLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);

				if (worldGenLevel.getBlockState(mPos).getBlock() instanceof LeavesBlock) {
					BlockState state;
					mPos.set(x, worldGenLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
					state = worldGenLevel.getBlockState(mPos);

					if (state.isAir()) {
						BlockState stateBelow;
						mPosDown.set(mPos).move(Direction.DOWN);
						stateBelow = worldGenLevel.getBlockState(mPosDown);

						if (stateBelow.isFaceSturdy(worldGenLevel, mPosDown, Direction.UP)) {
							worldGenLevel.setBlock(mPos, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

							if (stateBelow.hasProperty(SnowyBlock.SNOWY)) {
								worldGenLevel.setBlock(mPosDown, stateBelow.setValue(SnowyBlock.SNOWY, true), Block.UPDATE_CLIENTS);
							}
						}
					}
				}
			}
		}
		return true;
	}
}