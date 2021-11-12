package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import twilightforest.block.TFBlocks;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.feature.config.ThornsConfig;

import java.util.Random;

public class TwilightThorns extends Feature<ThornsConfig> {
	public TwilightThorns(Codec<ThornsConfig> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<ThornsConfig> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();


		// make a 3-5 long stack going up
		int nextLength = 2 + rand.nextInt(4);
		int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

		placeThorns(world, rand, pos, nextLength, Direction.UP, maxLength, pos, ctx.config(), true);

		return true;
	}

	private void placeThorns(WorldGenLevel world, Random rand, BlockPos pos, int length, Direction dir, int maxLength, BlockPos oPos, ThornsConfig config, boolean avoidGiantCloud) {
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			// Makes it avoid the troll clouds
			if (!avoidGiantCloud || dPos.getY() - 64 <= WorldUtil.getBaseHeight(world, dPos.getX(), dPos.getZ(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)) {
				if (Math.abs(dPos.getX() - oPos.getX()) < config.maxSpread() && Math.abs(dPos.getZ() - oPos.getZ()) < config.maxSpread() && canPlaceThorns(world, dPos)) {
					world.setBlock(dPos, TFBlocks.BROWN_THORNS.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), 1 | 2);

					// did we make it to the end?
					if (i == length - 1) {
						complete = true;
						// maybe a leaf?  or a rose?
						if (rand.nextInt(config.chanceOfLeaf()) == 0 && world.isEmptyBlock(dPos.relative(dir))) {
							if (rand.nextInt(config.chanceLeafIsRose()) > 0) {
								// leaf
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 3);
							} else {
								// rose
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_ROSE.get().defaultBlockState(), 3);
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

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, config, false);
		}

		// maybe another branch off the middle
		if (complete && length > 3 && rand.nextInt(config.chanceOfBranch()) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, config, false);
		}

		// maybe a leaf
		if (complete && length > 3 && rand.nextInt(config.chanceOfLeaf()) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);

			if (world.isEmptyBlock(nextPos)) {
				world.setBlock(nextPos, TFBlocks.THORN_LEAVES.get().defaultBlockState(), 3/*.with(LeavesBlock.CHECK_DECAY, false)*/);
			}
		}
	}

	private boolean canPlaceThorns(LevelAccessor world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.isAir()
				|| state.is(BlockTags.LEAVES);
	}
}
