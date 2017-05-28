package twilightforest.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public class ItemTF extends Item implements ModelRegisterCallback {
	
	private boolean isRare = false;

	protected ItemTF() {
		this.setCreativeTab(TFItems.creativeTab);
	}
	
    @Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
    	return isRare ? EnumRarity.RARE : EnumRarity.UNCOMMON;
	}

	public ItemTF makeRare()
	{
		this.isRare = true;
		return this;
	}
}
