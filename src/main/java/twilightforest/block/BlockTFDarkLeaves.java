package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockTFDarkLeaves extends Block {

	protected BlockTFDarkLeaves(Properties props) {
		super(props);
	}

//	@Override
//	public int damageDropped(BlockState state) {
//		return 3;
//	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 1;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 0;
	}

//	@Override
//	public int quantityDropped(Random random) {
//		return random.nextInt(40) == 0 ? 1 : 0;
//	}
//
//	@Override
//	public Item getItemDropped(BlockState state, Random random, int fortune) {
//		return Item.getItemFromBlock(TFBlocks.twilight_sapling);
//	}
//
//	@Override
//	public ItemStack getItem(World world, BlockPos pos, BlockState state) {
//		return new ItemStack(this);
//	}
//
//	@Override
//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune) {
//		Random rand = world instanceof World ? ((World)world).rand : RANDOM;
//		if (rand.nextInt(40) == 0) {
//			Item item = this.getItemDropped(state, rand, fortune);
//			drops.add(new ItemStack(item, 1, this.damageDropped(state)));
//		}
//	}
}
