package twilightforest.world;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * This class fixes the vanilla WorldGenVines, which appears to be nonfunctional in 1.11
 */
public class TFGenVines extends WorldGenerator {
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		for (; position.getY() > TFWorld.SEALEVEL; position = position.down()) {
			if (worldIn.isAirBlock(position)) {
				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
					if( tryPlaceVine( worldIn, position, enumfacing ) ) {
						int length = rand.nextInt(7) + 1;
						for( int i = 1; i < length; i ++ ) {
							BlockPos offset = position.down(i);
							if( !worldIn.isAirBlock(offset) )
								break;
							placeVine(worldIn, offset, enumfacing );
						}
						break;
					}
				}
			} else {
				position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
			}
		}

		return true;
	}
	
	private void placeVine(World worldIn, BlockPos position, EnumFacing enumfacing) {
		IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, enumfacing == EnumFacing.NORTH).withProperty(BlockVine.WEST, Boolean.valueOf(enumfacing == EnumFacing.EAST)).withProperty(BlockVine.NORTH, Boolean.valueOf(enumfacing == EnumFacing.SOUTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumfacing == EnumFacing.WEST));
		worldIn.setBlockState(position, iblockstate, 2);
	}
	
	private boolean tryPlaceVine(World worldIn, BlockPos position, EnumFacing enumfacing) {
		if (!Blocks.VINE.canPlaceBlockOnSide(worldIn, position, enumfacing))
			return false;
		
		placeVine(worldIn, position, enumfacing);
		return true;
	}
}