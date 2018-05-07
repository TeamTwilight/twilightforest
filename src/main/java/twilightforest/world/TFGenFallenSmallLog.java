package twilightforest.world;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLog;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.enums.WoodVariant;

import java.util.Random;

public class TFGenFallenSmallLog extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		// determine direction
		boolean goingX = rand.nextBoolean();

		// length
		int length = rand.nextInt(4) + 3;

		// check area clear
		if (goingX) {
			if (!isAreaSuitable(world, rand, pos, length, 3, 2)) {
				return false;
			}
		} else {
			if (!isAreaSuitable(world, rand, pos, 3, length, 2)) {
				return false;
			}
		}

		// determine wood type
		IBlockState logState;
		IBlockState branchState;

		switch (rand.nextInt(7)) {
			case 0:
			default:
				logState = TFBlocks.twilight_log.getDefaultState();
				break;
			case 1:
				logState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.CANOPY);
				break;
			case 2:
				logState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.MANGROVE);
				break;
			case 3:
				logState = Blocks.LOG.getDefaultState();
				break;
			case 4:
				logState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
				break;
			case 5:
				logState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
				break;
			case 6:
				logState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
				break;
		}
		branchState = logState;

		// check biome


		// make log
		if (goingX) {
			logState = logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
			branchState = logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);

			for (int lx = 0; lx < length; lx++) {
				this.setBlockAndNotifyAdequately(world, pos.add(lx, 0, 1), logState);
				if (rand.nextInt(3) > 0) {
					this.setBlockAndNotifyAdequately(world, pos.add(lx, 1, 1), TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MOSSPATCH));
				}
			}
		} else {
			logState = logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
			branchState = logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);

			for (int lz = 0; lz < length; lz++) {
				this.setBlockAndNotifyAdequately(world, pos.add(1, 0, lz), logState);
				if (rand.nextInt(3) > 0) {
					this.setBlockAndNotifyAdequately(world, pos.add(1, 1, lz), TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MOSSPATCH));
				}
			}
		}

		// possibly make branch
		if (rand.nextInt(3) > 0) {
			if (goingX) {
				int bx = rand.nextInt(length);
				int bz = rand.nextBoolean() ? 2 : 0;

				this.setBlockAndNotifyAdequately(world, pos.add(bx, 0, bz), branchState);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(bx, 1, bz), TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MOSSPATCH));
				}
			} else {
				int bx = rand.nextBoolean() ? 2 : 0;
				int bz = rand.nextInt(length);

				this.setBlockAndNotifyAdequately(world, pos.add(bx, 0, bz), branchState);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(bx, 1, bz), TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MOSSPATCH));
				}
			}
		}

		return true;
	}

}
