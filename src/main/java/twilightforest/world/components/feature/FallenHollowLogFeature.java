package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureUtil;

public class FallenHollowLogFeature extends Feature<NoneFeatureConfiguration> {

	final BlockState mossPatch = TFBlocks.MOSS_PATCH.get().defaultBlockState();
	final BlockState oakLeaves = TFBlocks.TWILIGHT_OAK_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
	final BlockState oakLogWithZAxis = TFBlocks.TWILIGHT_OAK_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
	final BlockState oakLogWithXAxis = TFBlocks.TWILIGHT_OAK_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
	final BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
	final BlockState firefly = TFBlocks.FIREFLY.get().defaultBlockState();

	public FallenHollowLogFeature(Codec<NoneFeatureConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();

		return rand.nextBoolean() ? makeLog4Z(world, rand, pos) : makeLog4X(world, rand, pos);
	}

	private boolean makeLog4Z(WorldGenLevel world, RandomSource rand, BlockPos pos) {
		// +Z 4x4 log
		if (!FeatureUtil.isAreaSuitable(world, pos, 4, 3, 9)) {
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
				world.setBlock(pos.offset(1, -1, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(1, 0, dz + 3), mossPatch, Block.UPDATE_ALL);
					this.markAboveForPostProcessing(world, pos.offset(1, -1, dz + 3));
				}
			} else {
				world.setBlock(pos.offset(1, -1, dz + 3), grass, Block.UPDATE_ALL);
				world.setBlock(pos.offset(1, 0, dz + 3), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(1, -1, dz + 3));
			}
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(2, -1, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(2, 0, dz + 3), mossPatch, Block.UPDATE_ALL);
					this.markAboveForPostProcessing(world, pos.offset(2, -1, dz + 3));
				}
			} else {
				world.setBlock(pos.offset(2, -1, dz + 3), grass, Block.UPDATE_ALL);
				world.setBlock(pos.offset(2, 0, dz + 3), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(2, -1, dz + 3));
			}

			// log part
			world.setBlock(pos.offset(0, 0, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(3, 0, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(0, 1, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(3, 1, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(1, 2, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(2, 2, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(1, 3, dz + 3), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(1, 2, dz + 3));
			}
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(2, 3, dz + 3), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(2, 2, dz + 3));
			}
		}

		// a few leaves?
		int offZ = rand.nextInt(3) + 2;
		boolean plusX = rand.nextBoolean();
		for (int dz = 0; dz < 3; dz++) {
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(plusX ? 3 : 0, 2, dz + offZ), oakLeaves, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(plusX ? 3 : 0, 3, dz + offZ), oakLeaves, Block.UPDATE_ALL);
				}
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(plusX ? 4 : -1, 2, dz + offZ), oakLeaves, Block.UPDATE_ALL);
				}
			}
		}


		// firefly
		world.setBlock(pos.offset(plusX ? 0 : 3, 2, rand.nextInt(4) + 3), firefly, Block.UPDATE_ALL);


		return true;
	}

	private void makeNegativeZJaggy(LevelAccessor world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = -length; dz < 0; dz++) {
			world.setBlock(pos.offset(dx, dy, dz + 3), oakLogWithZAxis, Block.UPDATE_ALL);
		}
	}

	private void makePositiveZJaggy(LevelAccessor world, BlockPos pos, int length, int dx, int dy) {
		for (int dz = 0; dz < length; dz++) {
			world.setBlock(pos.offset(dx, dy, dz + 7), oakLogWithZAxis, Block.UPDATE_ALL);
		}
	}

	/**
	 * Make a 4x4 log in the +X direction
	 */
	private boolean makeLog4X(WorldGenLevel world, RandomSource rand, BlockPos pos) {
		// +Z 4x4 log
		if (!FeatureUtil.isAreaSuitable(world, pos, 9, 3, 4)) {
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
				world.setBlock(pos.offset(dx + 3, -1, 1), oakLogWithXAxis, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(dx + 3, 0, 1), mossPatch, Block.UPDATE_ALL);
					this.markAboveForPostProcessing(world, pos.offset(dx + 3, -1, 1));
				}
			} else {
				world.setBlock(pos.offset(dx + 3, -1, 1), grass, Block.UPDATE_ALL);
				world.setBlock(pos.offset(dx + 3, 0, 1), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(dx + 3, -1, 1));
			}
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(dx + 3, -1, 2), oakLogWithXAxis, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(dx + 3, 0, 2), mossPatch, Block.UPDATE_ALL);
					this.markAboveForPostProcessing(world, pos.offset(dx + 3, -1, 2));
				}
			} else {
				world.setBlock(pos.offset(dx + 3, -1, 2), grass, Block.UPDATE_ALL);
				world.setBlock(pos.offset(dx + 3, 0, 2), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(dx + 3, -1, 2));
			}

			// log part
			world.setBlock(pos.offset(dx + 3, 0, 0), oakLogWithXAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(dx + 3, 0, 3), oakLogWithXAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(dx + 3, 1, 0), oakLogWithXAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(dx + 3, 1, 3), oakLogWithXAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(dx + 3, 2, 1), oakLogWithXAxis, Block.UPDATE_ALL);
			world.setBlock(pos.offset(dx + 3, 2, 2), oakLogWithXAxis, Block.UPDATE_ALL);
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(dx + 3, 3, 1), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(dx + 3, 2, 1));
			}
			if (rand.nextBoolean()) {
				world.setBlock(pos.offset(dx + 3, 3, 2), mossPatch, Block.UPDATE_ALL);
				this.markAboveForPostProcessing(world, pos.offset(dx + 3, 2, 2));
			}

		}

		// a few leaves?
		int offX = rand.nextInt(3) + 2;
		boolean plusZ = rand.nextBoolean();
		for (int dx = 0; dx < 3; dx++) {
			if (rand.nextBoolean()) {

				world.setBlock(pos.offset(dx + offX, 2, plusZ ? 3 : 0), oakLeaves, Block.UPDATE_ALL);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(dx + offX, 3, plusZ ? 3 : 0), oakLeaves, Block.UPDATE_ALL);
				}
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(dx + offX, 2, plusZ ? 4 : -1), oakLeaves, Block.UPDATE_ALL);
				}
			}
		}


		// firefly
		world.setBlock(pos.offset(rand.nextInt(4) + 3, 2, plusZ ? 0 : 3), firefly, Block.UPDATE_ALL);

		return true;
	}

	private void makeNegativeXJaggy(LevelAccessor world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = -length; dx < 0; dx++) {
			world.setBlock(pos.offset(dx + 3, dy, dz), oakLogWithXAxis, Block.UPDATE_ALL);
		}
	}

	private void makePositiveXJaggy(LevelAccessor world, BlockPos pos, int length, int dz, int dy) {
		for (int dx = 0; dx < length; dx++) {
			world.setBlock(pos.offset(dx + 7, dy, dz), oakLogWithXAxis, Block.UPDATE_ALL);
		}
	}
}
