package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafHoe extends ItemHoe implements ModelRegisterCallback {

	public ItemTFSteeleafHoe(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
