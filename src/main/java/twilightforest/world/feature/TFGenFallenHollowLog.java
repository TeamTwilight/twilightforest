package twilightforest.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.Random;
import java.util.function.Function;

public class TFGenFallenHollowLog<T extends NoFeatureConfig> extends Feature<T> {

	final BlockState mossPatch = TFBlocks.moss_patch.get().getDefaultState();
	final BlockState oakLeaves = TFBlocks.oak_leaves.get().getDefaultState()/*.with(LeavesBlock.CHECK_DECAY, false)*/;
	final BlockState oakLogWithZAxis = TFBlocks.oak_log.get().getDefaultState().with(LogBlock.AXIS, Direction.Axis.Z);
	final BlockState oakLogWithXAxis = TFBlocks.oak_log.get().getDefaultState().with(LogBlock.AXIS, Direction.Axis.X);
	final BlockState dirt = Blocks.DIRT.getDefaultState();
	final BlockState firefly = TFBlocks.firefly.get().getDefaultState();

	public TFGenFallenHollowLog(Function<Dynamic<?>, T> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, T config) {
		return rand.nextBoolean() ? makeLog4Z(world.getWorld(), rand, pos) : makeLog4X(world.getWorld(), rand, pos);
	}

	private boolean makeLog4Z(World world, Random rand, BlockPos pos) {
		// +Z 4x4 log
		if (!FeatureUtil.isAreaSuitable(world, rand, pos, 9, 3, 4)) {
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
				world.setBlockState(pos.add(1, -1, dz + 3), oakLogWithZAxis);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(1, 0, dz + 3), mossPatch);
				}
			} else {
				world.setBlockState(pos.add(1, -1, dz + 3), dirt);
				world.setBlockState(pos.add(1, 0, dz + 3), mossPatch);
			}
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(2, -1, dz + 3), oakLogWithZAxis);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(2, 0, dz + 3), mossPatch);
				}
			} else {
				world.setBlockState(pos.add(2, -1, dz + 3), dirt);
				world.setBlockState(pos.add(2, 0, dz + 3), mossPatch);
			}

			// log part
			world.setBlockState(pos.add(0, 0, dz + 3), oakLogWithZAxis);
			world.setBlockState(pos.add(3, 0, dz + 3), oakLogWithZAxis);
			world.setBlockState(pos.add(0, 1, dz + 3), oakLogWithZAxis);
			world.setBlockState(pos.add(3, 1, dz + 3), oakLogWithZAxis);
			world.setBlockState(pos.add(1, 2, dz + 3), oakLogWithZAxis);
			world.setBlockState(pos.add(2, 2, dz + 3), oakLogWithZAxis);
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(1, 3, dz + 3), mossPatch);
			}
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(2, 3, dz + 3), mossPatch);
			}
		}

		// a few leaves?
		int offZ = rand.nextInt(3) + 2;
		boolean plusX = rand.nextBoolean();
		for (int dz = 0; dz < 3; dz++) {
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(plusX ? 3 : 0, 2, dz + offZ), oakLeaves);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(plusX ? 3 : 0, 3, dz + offZ), oakLeaves);
				}
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(plusX ? 4 : -1, 2, dz + offZ), oakLeaves);
				}
			}
		}


		// firefly
		world.setBlockState(pos.add(plusX ? 0 : 3, 2, rand.nextInt(4) + 3), firefly);


		return true;
	}

	private void makeNegativeZJaggy(World world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = -length; dz < 0; dz++) {
			world.setBlockState(pos.add(dx, dy, dz + 3), oakLogWithZAxis);
		}
	}

	private void makePositiveZJaggy(World world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = 0; dz < length; dz++) {
			world.setBlockState(pos.add(dx, dy, dz + 7), oakLogWithZAxis);
		}
	}

	/**
	 * Make a 4x4 log in the +X direction
	 */
	private boolean makeLog4X(World world, Random rand, BlockPos pos) {
		// +Z 4x4 log
		if (!FeatureUtil.isAreaSuitable(world, rand, pos, 4, 3, 9)) {
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
				world.setBlockState(pos.add(dx + 3, -1, 1), oakLogWithXAxis);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(dx + 3, 0, 1), mossPatch);
				}
			} else {
				world.setBlockState(pos.add(dx + 3, -1, 1), dirt);
				world.setBlockState(pos.add(dx + 3, 0, 1), mossPatch);
			}
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(dx + 3, -1, 2), oakLogWithXAxis);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(dx + 3, 0, 2), mossPatch);
				}
			} else {
				world.setBlockState(pos.add(dx + 3, -1, 2), dirt);
				world.setBlockState(pos.add(dx + 3, 0, 2), mossPatch);
			}

			// log part
			world.setBlockState(pos.add(dx + 3, 0, 0), oakLogWithXAxis);
			world.setBlockState(pos.add(dx + 3, 0, 3), oakLogWithXAxis);
			world.setBlockState(pos.add(dx + 3, 1, 0), oakLogWithXAxis);
			world.setBlockState(pos.add(dx + 3, 1, 3), oakLogWithXAxis);
			world.setBlockState(pos.add(dx + 3, 2, 1), oakLogWithXAxis);
			world.setBlockState(pos.add(dx + 3, 2, 2), oakLogWithXAxis);
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(dx + 3, 3, 1), mossPatch);
			}
			if (rand.nextBoolean()) {
				world.setBlockState(pos.add(dx + 3, 3, 2), mossPatch);
			}

		}

		// a few leaves?
		int offX = rand.nextInt(3) + 2;
		boolean plusZ = rand.nextBoolean();
		for (int dx = 0; dx < 3; dx++) {
			if (rand.nextBoolean()) {

				world.setBlockState(pos.add(dx + offX, 2, plusZ ? 3 : 0), oakLeaves);
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(dx + offX, 3, plusZ ? 3 : 0), oakLeaves);
				}
				if (rand.nextBoolean()) {
					world.setBlockState(pos.add(dx + offX, 2, plusZ ? 4 : -1), oakLeaves);
				}
			}
		}


		// firefly
		world.setBlockState(pos.add(rand.nextInt(4) + 3, 2, plusZ ? 0 : 3), firefly);

		return true;
	}

	private void makeNegativeXJaggy(World world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = -length; dx < 0; dx++) {
			world.setBlockState(pos.add(dx + 3, dy, dz), oakLogWithXAxis);
		}
	}

	private void makePositiveXJaggy(World world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = 0; dx < length; dx++) {
			world.setBlockState(pos.add(dx + 7, dy, dz), oakLogWithXAxis);
		}
	}
}
