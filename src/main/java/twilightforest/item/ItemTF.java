package twilightforest.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTF extends Item implements ModelRegisterCallback {

	private boolean isRare = false;

	protected ItemTF() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return isRare ? EnumRarity.RARE : EnumRarity.UNCOMMON;
	}

	public ItemTF makeRare() {
		this.isRare = true;
		return this;
	}
}
