package twilightforest.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.item.Item.Properties;

public class GiantSwordItem extends SwordItem {

	public GiantSwordItem(Tier material, Properties props) {
		super(material, 10, -3.5F, props);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
		return material.getItem() == TFItems.IRONWOOD_INGOT.get() || super.isValidRepairItem(stack, material);
	}
}
