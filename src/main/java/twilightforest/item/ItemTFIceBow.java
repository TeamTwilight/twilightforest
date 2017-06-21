package twilightforest.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import twilightforest.entity.EntityIceArrow;

public class ItemTFIceBow extends ItemTFBowBase {
	public ItemTFIceBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	protected EntityArrow getArrow(World world, ItemStack stack, EntityPlayer entityPlayer) {
		return new EntityIceArrow(world, entityPlayer);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == Item.getItemFromBlock(Blocks.ICE) || super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
}
