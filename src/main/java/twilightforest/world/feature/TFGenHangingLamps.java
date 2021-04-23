package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenHangingLamps extends Feature<NoFeatureConfig> {

	private static final int MAX_HANG = 8;

	public TFGenHangingLamps(Codec<NoFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		// this must be an air block, surrounded by air
		if (!world.isAirBlock(pos) || !FeatureUtil.surroundedByAir(world, pos)) {
			return false;
		}

		// we need to be at least 4 above ground
		if (!isClearBelow(world, pos)) {
			return false;
		}

		// there should be leaves or wood within 12 blocks above
		int dist = findLeavesAbove(world, pos);
		if (dist < 0) {
			return false;
		}

		// generate lamp
		world.setBlockState(pos, TFBlocks.firefly_jar.get().getDefaultState(), 16 | 2);
		for (int cy = 1; cy < dist; cy++) {
			world.setBlockState(pos.up(cy), Blocks.OAK_FENCE.getDefaultState(), 16 | 2);
		}

		return true;
	}

	private int findLeavesAbove(IWorld world, BlockPos pos) {
		for (int cy = 1; cy < MAX_HANG; cy++) {
			Material above = world.getBlockState(pos.up(cy)).getMaterial();
			if (above.isSolid() || above == Material.LEAVES) {
				return cy;
			}
		}
		return -1;
	}

	private boolean isClearBelow(IWorld world, BlockPos pos) {
		for (int cy = 1; cy < 4; cy++) {
			if (world.getBlockState(pos.down(cy)).isSolidSide(world, pos, Direction.UP)) {
				return false;
			}
		}
		return true;
	}
}
