package twilightforest.world;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import java.util.Random;


public class TFGenHangingLamps extends TFGenerator {
	private static final int MAX_HANG = 8;

	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos) {
		// this must be an air block, surrounded by air
		if (par1World.isAirBlock(pos) && TFGenerator.surroundedByAir(par1World, pos)) {
			// there should be leaves or wood within 12 blocks above
			if (areLeavesAbove(par1World, pos)) {
				// we need to be at least 4 above ground
				if (isClearBelow(par1World, pos)) {
					// generate lamp
					par1World.setBlockState(pos, TFBlocks.firefly_jar.getDefaultState());

					for (int cy = 1; cy < MAX_HANG; cy++) {
						Material above = par1World.getBlockState(pos.up(cy)).getMaterial();
						if (above.isSolid() || above == Material.LEAVES) {
							break;
						} else {
							par1World.setBlockState(pos.up(cy), Blocks.OAK_FENCE.getDefaultState());
						}
					}
				}
			}

		}

		return false;
	}

	private boolean areLeavesAbove(World par1World, BlockPos pos) {
		boolean areLeavesAbove = false;
		for (int cy = 1; cy < MAX_HANG; cy++) {
			Material above = par1World.getBlockState(pos.up(cy)).getMaterial();
			if (above.isSolid() || above == Material.LEAVES) {
				areLeavesAbove = true;
			}
		}
		return areLeavesAbove;
	}

	private boolean isClearBelow(World par1World, BlockPos pos) {
		boolean isClearBelow = true;
		for (int cy = 1; cy < 4; cy++) {
			if (par1World.getBlockState(pos.down(cy)).isSideSolid(par1World, pos, EnumFacing.UP)) {
				isClearBelow = false;
			}
		}
		return isClearBelow;
	}
}
