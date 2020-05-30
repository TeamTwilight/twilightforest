package twilightforest.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class TFItemStackUtils {

	public static boolean consumeInventoryItem(LivingEntity living, Predicate<ItemStack> matcher, int count) {

//		IItemHandler inv = living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//		if (inv == null) return false;

		boolean consumedSome = false;

//		for (int i = 0; i < inv.getSlots() && count > 0; i++) {
//			ItemStack stack = inv.getStackInSlot(i);
//			if (matcher.test(stack)) {
//				ItemStack consumed = inv.extractItem(i, count, false);
//				count -= consumed.getCount();
//				consumedSome = true;
//			}
//		}

		//TODO: Baubles is dead (I think)
		/*if (TFCompat.BAUBLES.isActivated() && living instanceof EntityPlayer) {
			consumedSome |= Baubles.consumeInventoryItem((EntityPlayer) living, matcher, count);
		}*/

		return consumedSome;
	}

	public static NonNullList<ItemStack> splitToSize(ItemStack stack) {

		NonNullList<ItemStack> result = NonNullList.create();

		int size = stack.getMaxStackSize();

		while (!stack.isEmpty()) {
			result.add(stack.split(size));
		}

		return result;
	}

	//TODO: IE Compat
//	public static boolean hasToolMaterial(ItemStack stack, IItemTier material) {
//
//		Item item = stack.getItem();
//
//		// see TileEntityFurnace.getItemBurnTime
//		if (item instanceof ToolItem && material.toString().equals(((ToolItem)item).getToolMaterialName())) {
//			return true;
//		}
//		if (item instanceof SwordItem && material.toString().equals(((SwordItem)item).getToolMaterialName())) {
//			return true;
//		}
//		if (item instanceof HoeItem && material.toString().equals(((HoeItem)item).getMaterialName())) {
//			return true;
//		}
//
//		return false;
//	}

	public static void clearInfoTag(ItemStack stack, String key) {
		CompoundNBT nbt = stack.getTag();
		if (nbt != null) {
			nbt.remove(key);
			if (nbt.isEmpty()) {
				stack.setTag(null);
			}
		}
	}
}
