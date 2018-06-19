package twilightforest.world.feature;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.world.TFWorld;

import java.util.Random;

/**
 * This class fixes the vanilla WorldGenVines, which appears to be nonfunctional in 1.11
 */
public class TFGenVines extends WorldGenerator {
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		Chunk c = worldIn.getChunkFromBlockCoords(position);
		BlockPos original = new BlockPos(c.x * 16 + 8, position.getY(), c.z * 16 + 8);
		
		for (; position.getY() > TFWorld.SEALEVEL; position = position.down()) {
			if(isOutsideBounds(7, original, position)) return false;
			if (worldIn.isAirBlock(position)) {
				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
					if (Blocks.VINE.canPlaceBlockOnSide(worldIn, position, enumfacing)) {
						IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, enumfacing == EnumFacing.NORTH).withProperty(BlockVine.WEST, enumfacing == EnumFacing.EAST).withProperty(BlockVine.NORTH, enumfacing == EnumFacing.SOUTH).withProperty(BlockVine.EAST, enumfacing == EnumFacing.WEST);
						worldIn.setBlockState(position, iblockstate, 2);
						BlockPos down = position.down();
						while(worldIn.isAirBlock(down)) {
							worldIn.setBlockState(down, iblockstate, 2);
							down = down.down();
						}
						break;
					}
				}
			} else {
				position = position.add(MathHelper.getInt(rand, -3, 3), 0, MathHelper.getInt(rand, -3, 3));
			}
		}

		return true;
	}
	
	private boolean isOutsideBounds(int radius, BlockPos original, BlockPos pos) {
		boolean flag1 = Math.abs(original.getX() - pos.getX()) > radius;
		boolean flag2 = Math.abs(original.getZ() - pos.getZ()) > radius;
		return flag1 || flag2;
	}
}