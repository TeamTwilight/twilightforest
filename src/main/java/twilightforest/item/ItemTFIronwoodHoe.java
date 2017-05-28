package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodHoe extends ItemHoe implements ModelRegisterCallback {

	public ItemTFIronwoodHoe(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
