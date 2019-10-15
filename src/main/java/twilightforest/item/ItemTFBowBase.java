package twilightforest.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

public abstract class ItemTFBowBase extends ItemBow implements ModelRegisterCallback {

	private static final EnumRarity RARITY = EnumRarity.UNCOMMON;

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.isItemEnchanted() ? EnumRarity.RARE.compareTo(RARITY) > 0 ? EnumRarity.RARE : RARITY : RARITY;
	}
}
