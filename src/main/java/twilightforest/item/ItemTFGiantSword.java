package twilightforest.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ItemTFGiantSword extends SwordItem {

	public ItemTFGiantSword(IItemTier material, Properties props) {
		super(material, 10, -3.5F, props);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == TFItems.ironwood_ingot.get() || super.getIsRepairable(stack, material);
	}
}
