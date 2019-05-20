package twilightforest.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTF extends Item implements ModelRegisterCallback {
	private final EnumRarity RARITY;

	protected ItemTF() {
		this.setCreativeTab(TFItems.creativeTab);
		this.RARITY = EnumRarity.COMMON;
	}

	protected ItemTF(EnumRarity rarity) {
		this.setCreativeTab(TFItems.creativeTab);
		this.RARITY = rarity;
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.isItemEnchanted() ? EnumRarity.RARE.compareTo(RARITY) > 0 ? EnumRarity.RARE : RARITY : RARITY;
	}
}
