package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafHoe extends ItemHoe implements ModelRegisterCallback {

	public ItemTFSteeleafHoe(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
