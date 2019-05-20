package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFDarkLeaves extends Block implements ModelRegisterCallback {

	protected BlockTFDarkLeaves() {
		super(Material.LEAVES);
		this.setHardness(2F);
		this.setResistance(10F);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 3;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return 1;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(40) == 0 ? 1 : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		return Item.getItemFromBlock(TFBlocks.twilight_sapling);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		Random rand = world instanceof World ? ((World)world).rand : RANDOM;
		if (rand.nextInt(40) == 0) {
			Item item = this.getItemDropped(state, rand, fortune);
			drops.add(new ItemStack(item, 1, this.damageDropped(state)));
		}
	}
}
