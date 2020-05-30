package twilightforest.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;

public class ItemTFSteeleafSword extends SwordItem {

	public ItemTFSteeleafSword(IItemTier material, Properties props) {
		super(material, 3, -2.4F, props);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.LOOTING, 2);
			list.add(istack);
		}
	}
}
