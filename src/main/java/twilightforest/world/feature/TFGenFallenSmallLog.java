package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenFallenSmallLog extends Feature<NoFeatureConfig> {

	public TFGenFallenSmallLog(Codec<NoFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		// determine direction
		boolean goingX = rand.nextBoolean();

		// length
		int length = rand.nextInt(4) + 3;

		// check area clear
		if (goingX) {
			if (!FeatureUtil.isAreaSuitable(world, rand, pos, length, 3, 2)) {
				return false;
			}
		} else {
			if (!FeatureUtil.isAreaSuitable(world, rand, pos, 3, length, 2)) {
				return false;
			}
		}

		// determine wood type
		BlockState logState;
		BlockState branchState;

		switch (rand.nextInt(7)) {
			case 0:
			default:
				logState = TFBlocks.oak_log.get().getDefaultState();
				break;
			case 1:
				logState = TFBlocks.canopy_log.get().getDefaultState();
				break;
			case 2:
				logState = TFBlocks.mangrove_log.get().getDefaultState();
				break;
			case 3:
				logState = Blocks.OAK_LOG.getDefaultState();
				break;
			case 4:
				logState = Blocks.SPRUCE_LOG.getDefaultState();
				break;
			case 5:
				logState = Blocks.BIRCH_LOG.getDefaultState();
				break;
			case 6:
				logState = Blocks.JUNGLE_LOG.getDefaultState();
				break;
		}
		branchState = logState;

		// check biome
		// Androsa: Uh...what are we checking?


		// make log
		if (goingX) {
			logState = logState.with(RotatedPillarBlock.AXIS, Direction.Axis.X);
			branchState = logState.with(RotatedPillarBlock.AXIS, Direction.Axis.Z);

			for (int lx = 0; lx < length; lx++) {
				world.setBlockState(pos.add(lx, 0, 1), logState, 3);
				if (rand.nextInt(3) > 0) {
					world.setBlockState(pos.add(lx, 1, 1), TFBlocks.moss_patch.get().getDefaultState(), 3);
				}
			}
		} else {
			logState = logState.with(RotatedPillarBlock.AXIS, Direction.Axis.Z);
			branchState = logState.with(RotatedPillarBlock.AXIS, Direction.Axis.X);

			for (int lz = 0; lz < length; lz++) {
				world.setBlockState(pos.add(1, 0, lz), logState, 3);
				if (rand.nextInt(3) > 0) {
					world.setBlockState(pos.add(1, 1, lz), TFBlocks.moss_patch.get().getDefaultState(), 3);
				}
			}
		}

		// possibly make branch
		if (rand.nextInt(3) > 0) {
			if (goingX) {
				int bx = rand.nextInt(length);
				int bz = rand.nextBoolean() ? 2 : 0;

				world.setBlockState(pos.add(bx, 0, bz), branchState, 3);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(bx, 1, bz), TFBlocks.moss_patch.get().getDefaultState(), 3);
				}
			} else {
				int bx = rand.nextBoolean() ? 2 : 0;
				int bz = rand.nextInt(length);

				world.setBlockState(pos.add(bx, 0, bz), branchState, 3);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(bx, 1, bz), TFBlocks.moss_patch.get().getDefaultState(), 3);
				}
			}
		}

		return true;
	}
}
