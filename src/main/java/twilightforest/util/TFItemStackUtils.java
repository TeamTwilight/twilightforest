package twilightforest.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import twilightforest.compat.Baubles;
import twilightforest.compat.TFCompat;

import java.util.function.Predicate;

public class TFItemStackUtils {
	public static boolean consumeInventoryItem(EntityLivingBase living, Predicate<ItemStack> matcher, int count) {
		boolean consumedSome = false;
		IItemHandler inv = living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		for (int i = 0; i < inv.getSlots() && count > 0; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (matcher.test(stack)) {
				int consume = Math.min(count, stack.getCount());
				stack.shrink(consume);
				count -= consume;
				consumedSome = true;
			}
		}

		if (TFCompat.BAUBLES.isActivated() && living instanceof EntityPlayer)
            consumedSome |= Baubles.consumeInventoryItem((EntityPlayer) living, matcher, count);

		return consumedSome;
	}
}
