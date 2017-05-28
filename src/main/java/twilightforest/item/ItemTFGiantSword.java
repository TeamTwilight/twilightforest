package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFGiantSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFGiantSword(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

    @Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return par2ItemStack.getItem() == TFItems.ironwoodIngot || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
}
