package twilightforest.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import twilightforest.TwilightForestMod;

import java.util.function.Predicate;

public class TFItemStackUtils {
	@Deprecated
	public static boolean consumeInventoryItem(LivingEntity living, final Predicate<ItemStack> matcher, final int count) {
		TwilightForestMod.LOGGER.warn("consumeInventoryItem accessed! Forge requires the player to be alive before we can access this cap. This cap is most likely being accessed for an Afterdeath Charm!");

		return living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(inv -> {
			int innerCount = count;
			boolean consumedSome = false;

			for (int i = 0; i < inv.getSlots() && innerCount > 0; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (matcher.test(stack)) {
					ItemStack consumed = inv.extractItem(i, innerCount, false);
					innerCount -= consumed.getCount();
					consumedSome = true;
				}
			}

			//TODO: Baubles is dead (I think)
			/*if (TFCompat.BAUBLES.isActivated() && living instanceof EntityPlayer) {
				consumedSome |= Baubles.consumeInventoryItem((EntityPlayer) living, matcher, count);
			}*/

			return consumedSome;
		}).orElse(false);
	}

	public static boolean consumeInventoryItem(final PlayerEntity player, final Item item) {
		return consumeInventoryItem(player.inventory.armorInventory, item) || consumeInventoryItem(player.inventory.mainInventory, item) || consumeInventoryItem(player.inventory.offHandInventory, item);
	}

	public static boolean consumeInventoryItem(final NonNullList<ItemStack> stacks, final Item item) {
		for (int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if (stack.getItem() == item) {
				stack.grow(-1);

				return true;
			}
		}

		return false;
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
