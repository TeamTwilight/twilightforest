package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenThorns extends Feature<NoFeatureConfig> {

	private static final int MAX_SPREAD = 7;
	private static final int CHANCE_OF_BRANCH = 3;
	private static final int CHANCE_OF_LEAF = 3;
	private static final int CHANCE_LEAF_IS_ROSE = 50;

	public TFGenThorns(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {

		// make a 3-5 long stack going up
		int nextLength = 2 + rand.nextInt(4);
		int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

		placeThorns(world, rand, pos, nextLength, Direction.UP, maxLength, pos);

		return true;
	}

	private void placeThorns(ISeedReader world, Random rand, BlockPos pos, int length, Direction dir, int maxLength, BlockPos oPos) {
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (world.canBlockSeeSky(pos)) {
				if (Math.abs(dPos.getX() - oPos.getX()) < MAX_SPREAD && Math.abs(dPos.getZ() - oPos.getZ()) < MAX_SPREAD && canPlaceThorns(world, dPos)) {
					world.setBlockState(dPos, TFBlocks.brown_thorns.get().getDefaultState().with(RotatedPillarBlock.AXIS, dir.getAxis()), 1 | 2);

					// did we make it to the end?
					if (i == length - 1) {
						complete = true;
						// maybe a leaf?  or a rose?
						if (rand.nextInt(CHANCE_OF_LEAF) == 0 && world.isAirBlock(dPos.offset(dir))) {
							if (rand.nextInt(CHANCE_LEAF_IS_ROSE) > 0) {
								// leaf
								world.setBlockState(dPos.offset(dir), TFBlocks.thorn_leaves.get().getDefaultState(), 3/*.with(LeavesBlock.CHECK_DECAY, false)*/);
							} else {
								// rose
								world.setBlockState(dPos.offset(dir), TFBlocks.thorn_rose.get().getDefaultState(), 3);
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

			Direction nextDir = Direction.getRandomDirection(rand);

			BlockPos nextPos = pos.offset(dir, length - 1).offset(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe another branch off the middle
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_BRANCH) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandomDirection(rand);

			BlockPos nextPos = pos.offset(dir, middle).offset(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe a leaf
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_LEAF) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandomDirection(rand);

			BlockPos nextPos = pos.offset(dir, middle).offset(nextDir);

			if (world.isAirBlock(nextPos)) {
				world.setBlockState(nextPos, TFBlocks.thorn_leaves.get().getDefaultState(), 3/*.with(LeavesBlock.CHECK_DECAY, false)*/);
			}
		}
	}

	private boolean canPlaceThorns(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos)
				|| state.getBlock().isIn(BlockTags.LEAVES);
	}
}
