package twilightforest.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;

public class ItemTFSteeleafPick extends PickaxeItem {

	protected ItemTFSteeleafPick(IItemTier material, Properties props) {
		super(material, 1, -2.8F, props);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.FORTUNE, 2);
			list.add(istack);
		}
	}
}
