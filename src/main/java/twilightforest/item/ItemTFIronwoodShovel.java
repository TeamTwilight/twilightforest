package twilightforest.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;

public class ItemTFIronwoodShovel extends ShovelItem {

	public ItemTFIronwoodShovel(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 1.5F, -3.0F, props);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.UNBREAKING, 1);
			list.add(istack);
		}
	}
}
