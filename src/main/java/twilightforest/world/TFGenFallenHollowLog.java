package twilightforest.world;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLog;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;

import java.util.Random;

public class TFGenFallenHollowLog extends TFGenerator {

	final IBlockState mossPatch = TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MOSSPATCH);
	final IBlockState oakLeaves = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false);
	final IBlockState oakLogWithZAxis = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.Z);
	final IBlockState oakLogWithXAxis = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.X);
	final IBlockState dirt = Blocks.DIRT.getDefaultState();
	final IBlockState firefly = TFBlocks.firefly.getDefaultState();

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		return rand.nextBoolean() ? makeLog4Z(world, rand, pos) : makeLog4X(world, rand, pos);
	}

	private boolean makeLog4Z(World world, Random rand, BlockPos pos) {
		// +Z 4x4 log
		if (!isAreaSuitable(world, rand, pos, 9, 3, 4)) {
			return false;
		}

		// jaggy parts
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 0, 0);
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 3, 0);
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 0, 1);
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 3, 1);
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 1, 2);
		makeNegativeZJaggy(world, pos, rand.nextInt(3), 2, 2);

		makePositiveZJaggy(world, pos, rand.nextInt(3), 0, 0);
		makePositiveZJaggy(world, pos, rand.nextInt(3), 3, 0);
		makePositiveZJaggy(world, pos, rand.nextInt(3), 0, 1);
		makePositiveZJaggy(world, pos, rand.nextInt(3), 3, 1);
		makePositiveZJaggy(world, pos, rand.nextInt(3), 1, 2);
		makePositiveZJaggy(world, pos, rand.nextInt(3), 2, 2);

		// center
		for (int dz = 0; dz < 4; dz++) {
			// floor
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(1, -1, dz + 3), oakLogWithZAxis);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(1, 0, dz + 3), mossPatch);
				}
			} else {
				this.setBlockAndNotifyAdequately(world, pos.add(1, -1, dz + 3), dirt);
				this.setBlockAndNotifyAdequately(world, pos.add(1, 0, dz + 3), mossPatch);
			}
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(2, -1, dz + 3), oakLogWithZAxis);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(2, 0, dz + 3), mossPatch);
				}
			} else {
				this.setBlockAndNotifyAdequately(world, pos.add(2, -1, dz + 3), dirt);
				this.setBlockAndNotifyAdequately(world, pos.add(2, 0, dz + 3), mossPatch);
			}

			// log part
			this.setBlockAndNotifyAdequately(world, pos.add(0, 0, dz + 3), oakLogWithZAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(3, 0, dz + 3), oakLogWithZAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(0, 1, dz + 3), oakLogWithZAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(3, 1, dz + 3), oakLogWithZAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(1, 2, dz + 3), oakLogWithZAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(2, 2, dz + 3), oakLogWithZAxis);
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(1, 3, dz + 3), mossPatch);
			}
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(2, 3, dz + 3), mossPatch);
			}
		}

		// a few leaves?
		int offZ = rand.nextInt(3) + 2;
		boolean plusX = rand.nextBoolean();
		for (int dz = 0; dz < 3; dz++) {
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(plusX ? 3 : 0, 2, dz + offZ), oakLeaves);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(plusX ? 3 : 0, 3, dz + offZ), oakLeaves);
				}
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(plusX ? 4 : -1, 2, dz + offZ), oakLeaves);
				}
			}
		}


		// firefly
		this.setBlockAndNotifyAdequately(world, pos.add(plusX ? 0 : 3, 2, rand.nextInt(4) + 3), firefly);


		return true;
	}

	private void makeNegativeZJaggy(World world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = -length; dz < 0; dz++) {
			this.setBlockAndNotifyAdequately(world, pos.add(dx, dy, dz + 3), oakLogWithZAxis);
		}
	}

	private void makePositiveZJaggy(World world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = 0; dz < length; dz++) {
			this.setBlockAndNotifyAdequately(world, pos.add(dx, dy, dz + 7), oakLogWithZAxis);
		}
	}

	/**
	 * Make a 4x4 log in the +X direction
	 */
	private boolean makeLog4X(World world, Random rand, BlockPos pos) {
		// +Z 4x4 log
		if (!isAreaSuitable(world, rand, pos, 4, 3, 9)) {
			return false;
		}

		// jaggy parts
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 0, 0);
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 3, 0);
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 0, 1);
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 3, 1);
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 1, 2);
		makeNegativeXJaggy(world, pos, rand.nextInt(3), 2, 2);

		makePositiveXJaggy(world, pos, rand.nextInt(3), 0, 0);
		makePositiveXJaggy(world, pos, rand.nextInt(3), 3, 0);
		makePositiveXJaggy(world, pos, rand.nextInt(3), 0, 1);
		makePositiveXJaggy(world, pos, rand.nextInt(3), 3, 1);
		makePositiveXJaggy(world, pos, rand.nextInt(3), 1, 2);
		makePositiveXJaggy(world, pos, rand.nextInt(3), 2, 2);

		// center
		for (int dx = 0; dx < 4; dx++) {
			// floor
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, -1, 1), oakLogWithXAxis);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 1), mossPatch);
				}
			} else {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, -1, 1), dirt);
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 1), mossPatch);
			}
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, -1, 2), oakLogWithXAxis);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 2), mossPatch);
				}
			} else {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, -1, 2), dirt);
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 2), mossPatch);
			}

			// log part
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 0), oakLogWithXAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 0, 3), oakLogWithXAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 1, 0), oakLogWithXAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 1, 3), oakLogWithXAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 2, 1), oakLogWithXAxis);
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 2, 2), oakLogWithXAxis);
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 3, 1), mossPatch);
			}
			if (rand.nextBoolean()) {
				this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, 3, 2), mossPatch);
			}

		}

		// a few leaves?
		int offX = rand.nextInt(3) + 2;
		boolean plusZ = rand.nextBoolean();
		for (int dx = 0; dx < 3; dx++) {
			if (rand.nextBoolean()) {

				this.setBlockAndNotifyAdequately(world, pos.add(dx + offX, 2, plusZ ? 3 : 0), oakLeaves);
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(dx + offX, 3, plusZ ? 3 : 0), oakLeaves);
				}
				if (rand.nextBoolean()) {
					this.setBlockAndNotifyAdequately(world, pos.add(dx + offX, 2, plusZ ? 4 : -1), oakLeaves);
				}
			}
		}


		// firefly
		this.setBlockAndNotifyAdequately(world, pos.add(rand.nextInt(4) + 3, 2, plusZ ? 0 : 3), firefly);

		return true;
	}

	private void makeNegativeXJaggy(World world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = -length; dx < 0; dx++) {
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 3, dy, dz), oakLogWithXAxis);
		}
	}

	private void makePositiveXJaggy(World world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = 0; dx < length; dx++) {
			this.setBlockAndNotifyAdequately(world, pos.add(dx + 7, dy, dz), oakLogWithXAxis);
		}
	}
}
