package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFHugeGloomBlock;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenBigMushgloom extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int height = 3 + rand.nextInt(2) + rand.nextInt(2);

		if (!isAreaSuitable(world, rand, pos.add(-1, 0, -1), 3, height, 3)) {
			return false;
		}

		Block blockUnder = world.getBlockState(pos.down()).getBlock();
		if (blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS && blockUnder != Blocks.MYCELIUM) {
			return false;
		}

		// generate!
		for (int dy = 0; dy < height - 2; dy++) {
			this.setBlockAndNotifyAdequately(world, pos.up(dy), TFBlocks.huge_mushgloom.getDefaultState().withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.STEM));
		}

		makeMushroomCap(world, pos.up(height - 2));
		if (rand.nextBoolean()) {
			makeMushroomCap(world, pos.up(height - 1));
		}

		return true;
	}

	private void makeMushroomCap(World world, BlockPos pos) {
		IBlockState defState = TFBlocks.huge_mushgloom.getDefaultState();
		this.setBlockAndNotifyAdequately(world, pos.add(-1, 0, -1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST));
		this.setBlockAndNotifyAdequately(world, pos.add(0, 0, -1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.NORTH));
		this.setBlockAndNotifyAdequately(world, pos.add(1, 0, -1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST));
		this.setBlockAndNotifyAdequately(world, pos.add(-1, 0, 0), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.WEST));
		this.setBlockAndNotifyAdequately(world, pos, defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.CENTER));
		this.setBlockAndNotifyAdequately(world, pos.add(1, 0, 0), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.EAST));
		this.setBlockAndNotifyAdequately(world, pos.add(-1, 0, 1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST));
		this.setBlockAndNotifyAdequately(world, pos.add(0, 0, 1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.SOUTH));
		this.setBlockAndNotifyAdequately(world, pos.add(1, 0, 1), defState.withProperty(BlockTFHugeGloomBlock.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST));
	}

}
