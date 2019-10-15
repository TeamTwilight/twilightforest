package twilightforest.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import twilightforest.compat.Baubles;
import twilightforest.compat.TFCompat;

import java.util.function.Predicate;

public class TFItemStackUtils {

	public static boolean consumeInventoryItem(EntityLivingBase living, Predicate<ItemStack> matcher, int count) {

		IItemHandler inv = living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (inv == null) return false;

		boolean consumedSome = false;

		for (int i = 0; i < inv.getSlots() && count > 0; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (matcher.test(stack)) {
				ItemStack consumed = inv.extractItem(i, count, false);
				count -= consumed.getCount();
				consumedSome = true;
			}
		}

		if (TFCompat.BAUBLES.isActivated() && living instanceof EntityPlayer) {
			consumedSome |= Baubles.consumeInventoryItem((EntityPlayer) living, matcher, count);
		}

		return consumedSome;
	}

	public static NonNullList<ItemStack> splitToSize(ItemStack stack) {

		NonNullList<ItemStack> result = NonNullList.create();

		int size = stack.getMaxStackSize();

		while (!stack.isEmpty()) {
			result.add(stack.splitStack(size));
		}

		return result;
	}

	public static boolean hasToolMaterial(ItemStack stack, Item.ToolMaterial material) {

		Item item = stack.getItem();

		// see TileEntityFurnace.getItemBurnTime
		if (item instanceof ItemTool && material.toString().equals(((ItemTool)item).getToolMaterialName())) {
			return true;
		}
		if (item instanceof ItemSword && material.toString().equals(((ItemSword)item).getToolMaterialName())) {
			return true;
		}
		if (item instanceof ItemHoe && material.toString().equals(((ItemHoe)item).getMaterialName())) {
			return true;
		}

		return false;
	}

	public static void clearInfoTag(ItemStack stack, String key) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			nbt.removeTag(key);
			if (nbt.isEmpty()) {
				stack.setTagCompound(null);
			}
		}
	}
}
