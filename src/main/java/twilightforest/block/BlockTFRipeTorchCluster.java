package twilightforest.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFRipeTorchCluster extends BlockTFTrollRoot {

	protected BlockTFRipeTorchCluster() {
		this.setLightLevel(1.0F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return TFItems.torchberries;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return quantityDroppedWithBonus(fortune, random);
	}

	@Override
	public int quantityDropped(Random rand) {
		return 4 + rand.nextInt(5);
	}

	@Override
	public int quantityDroppedWithBonus(int bonus, Random rand) {
		if (bonus > 0 && Item.getItemFromBlock(this) != this.getItemDropped(getDefaultState(), rand, bonus)) {
			int j = rand.nextInt(bonus + 2) - 1;

			if (j < 0) {
				j = 0;
			}

			return this.quantityDropped(rand) * (j + 1);
		} else {
			return this.quantityDropped(rand);
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		// do not call normal harvest if the player is shearing
		if (world.isRemote || stack.isEmpty() || stack.getItem() != Items.SHEARS) {
			super.harvestBlock(world, player, pos, state, te, stack);
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
