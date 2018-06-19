package twilightforest.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.loot.TFTreasure;

import java.util.Random;


public class TFGenWell extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (rand.nextInt(4) == 0) {
			return generate4x4Well(world, rand, pos);
		} else {
			return generate3x3Well(world, rand, pos);
		}
	}


	/**
	 * make a cute little well
	 */
	public boolean generate3x3Well(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos, 3, 4, 3)) {
			return false;
		}


		// make a cute well!
		setBlockAndNotifyAdequately(world, pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 2), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 2), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 2), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 0, 1), Blocks.WATER.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 1, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 1, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 1, 2), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 1, 2), Blocks.OAK_FENCE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 2, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 2, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 2), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 2, 2), Blocks.OAK_FENCE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 2), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 2), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 2), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 1), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 1), Blocks.WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 3, 1), Blocks.PLANKS.getDefaultState());


		boolean madeTreasure = false;
		// now drill each the well square down 20 squares, or until we hit something
		for (int dy = -1; dy >= -20; dy--) {
			Block dblock = world.getBlockState(pos.add(1, dy, 1)).getBlock();
			// we only drill through dirt, grass, gravel and stone
			if (dblock != Blocks.DIRT && dblock != Blocks.GRASS && dblock != Blocks.GRAVEL && dblock != Blocks.STONE) {
				break;
			}
			// we also need a solid block under where we're digging
			if (!world.getBlockState(pos.add(1, dy - 1, 1)).getMaterial().isSolid()) {
				break;
			}

			// okay, we're good to dig.
			setBlockAndNotifyAdequately(world, pos.add(1, dy, 1), Blocks.WATER.getDefaultState());

			// if we're below 15 squares, there's a small chance of treasure
			if (dy < -15 && madeTreasure == false && rand.nextInt(8) == 0) {
				//TODO: more directions
				setBlockAndNotifyAdequately(world, pos.add(2, dy, 1), Blocks.WATER.getDefaultState());
				setBlockAndNotifyAdequately(world, pos.add(3, dy + 1, 1), Blocks.AIR.getDefaultState());
				setBlockAndNotifyAdequately(world, pos.add(3, dy, 1), Blocks.AIR.getDefaultState());

				//TODO: unique treasure table that is themed for underwater well exploration
				TFTreasure.basement.generateChest(world, pos.add(3, dy, 1), false);

				// set flag so we only get one chest
				madeTreasure = true;
			}
		}

		return true;
	}

	private boolean generate4x4Well(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos, 4, 4, 4)) {
			return false;
		}

		// make a cute well!
		setBlockAndNotifyAdequately(world, pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 3), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 3), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 3), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 3), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 2), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 2), Blocks.MOSSY_COBBLESTONE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 0, 1), Blocks.WATER.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 1), Blocks.WATER.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 2), Blocks.WATER.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 2), Blocks.WATER.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 1, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 1, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 1, 3), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 1, 3), Blocks.OAK_FENCE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 2, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 2, 0), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 3), Blocks.OAK_FENCE.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 2, 3), Blocks.OAK_FENCE.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 3), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 3), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 3), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 3), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 1), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 2), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 1), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 2), Blocks.WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 3, 1), Blocks.PLANKS.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 1), Blocks.PLANKS.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 2), Blocks.PLANKS.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 2), Blocks.PLANKS.getDefaultState());


		// now drill each of the 4 well squares down 20 squares, or until we hit something
		for (int dx = 1; dx <= 2; dx++) {
			for (int dz = 1; dz <= 2; dz++) {
				for (int dy = -1; dy >= -20; dy--) {
					BlockPos dPos = pos.add(dx, dy, dz);
					IBlockState dState = world.getBlockState(dPos);
					Block dblock = dState.getBlock();

					// we only drill through dirt, grass, gravel and stone
					if (dblock != Blocks.DIRT && dblock != Blocks.GRASS && dblock != Blocks.GRAVEL && dblock != Blocks.STONE) {
						break;
					}
					// we also need a solid block under where we're digging
					if (!world.getBlockState(dPos.down()).getMaterial().isSolid()) {
						break;
					}

					// okay, we're good to dig.
					setBlockAndNotifyAdequately(world, dPos, Blocks.WATER.getDefaultState());
				}

			}

		}

		return true;
	}

}
