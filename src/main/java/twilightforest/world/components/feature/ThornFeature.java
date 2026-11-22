package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;
import twilightforest.util.WorldUtil;

public record ThornFeature(int maxSpread, int chanceOfBranch, int chanceOfLeaf, int chanceLeafIsRose) implements Feature {

	public static final MapCodec<ThornFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT
			.fieldOf("max_spread")
			.forGetter(ThornFeature::maxSpread),
		Codec.INT
			.fieldOf("chance_of_branch")
			.forGetter(ThornFeature::chanceOfBranch),
		Codec.INT
			.fieldOf("chance_of_leaf")
			.forGetter(ThornFeature::chanceOfLeaf),
		Codec.INT.
			fieldOf("chance_leaf_is_rose")
			.forGetter(ThornFeature::chanceLeafIsRose)
	).apply(instance, ThornFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		// make a 3-5 long stack going up
		int nextLength = 2 + randomSource.nextInt(4);
		int maxLength = 2 + randomSource.nextInt(4) + randomSource.nextInt(4) + randomSource.nextInt(4);

		placeThorns(worldGenLevel, randomSource, blockPos, nextLength, Direction.UP, maxLength, blockPos, maxSpread(), chanceOfBranch(), chanceOfLeaf(), chanceLeafIsRose(), true);

		return true;
	}

	private void placeThorns(WorldGenLevel world, RandomSource rand, BlockPos pos, int length, Direction dir, int maxLength, BlockPos oPos, int maxSpread, int chanceOfBranch, int chanceOfLeaf, int chanceLeafIsRose, boolean avoidGiantCloud) {
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			// Makes it avoid the troll clouds
			if (!avoidGiantCloud || checkIsUnderCloud(world, pos, dPos)) {
				if (Math.abs(dPos.getX() - oPos.getX()) < maxSpread && Math.abs(dPos.getZ() - oPos.getZ()) < maxSpread && canPlaceThorns(world, dPos)) {
					world.setBlock(dPos, TFBlocks.BROWN_THORNS.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), Block.UPDATE_CLIENTS);
					world.getChunk(dPos).markPosForPostProcessing(dPos);

					// did we make it to the end?
					if (i == length - 1) {
						complete = true;
						// maybe a leaf?  or a rose?
						if (rand.nextInt(chanceOfLeaf) == 0 && world.isEmptyBlock(dPos.relative(dir))) {
							if (rand.nextInt(chanceLeafIsRose) > 0) {
								// leaf
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 1), Block.UPDATE_ALL);
							} else {
								// rose
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_ROSE.get().defaultBlockState().setValue(DirectionalBlock.FACING, dir), Block.UPDATE_ALL);
							}
						}
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		// add another off the end
		if (complete && maxLength > 1) {

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, length - 1).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, maxSpread, chanceOfBranch, chanceOfLeaf, chanceLeafIsRose, false);
		}

		// maybe another branch off the middle
		if (complete && length > 3 && rand.nextInt(chanceOfBranch) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, maxSpread, chanceOfBranch, chanceOfLeaf, chanceLeafIsRose, false);
		}

		// maybe a leaf
		if (complete && length > 3 && rand.nextInt(chanceOfLeaf) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);

			if (world.isEmptyBlock(nextPos)) {
				world.setBlock(nextPos, TFBlocks.THORN_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 1), Block.UPDATE_ALL/*.with(LeavesBlock.CHECK_DECAY, false)*/);
			}
		}
	}

	private static boolean checkIsUnderCloud(WorldGenLevel world, BlockPos pos, BlockPos dPos) {
		return world.hasChunk(pos.getX() >> 4, pos.getZ() >> 4)
			&& Math.max(dPos.getY(), world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dPos.getX(), dPos.getZ())) <= WorldUtil.getGeneratorSeaLevel(world) + 150;
	}

	private boolean canPlaceThorns(LevelAccessor world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.isAir()
			|| state.is(BlockTags.LEAVES);
	}
}
