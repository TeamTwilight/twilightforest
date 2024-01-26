package twilightforest.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TFItemStackUtils {

	public static int damage = 0;

	@Deprecated
	public static boolean consumeInventoryItem(LivingEntity living, final Predicate<ItemStack> matcher, final int count) {
		TwilightForestMod.LOGGER.warn("consumeInventoryItem accessed! Forge requires the player to be alive before we can access this cap. This cap is most likely being accessed for an Afterdeath Charm!");

		IItemHandler handler = living.getCapability(Capabilities.ItemHandler.ENTITY);
		if (handler != null) {
			int innerCount = count;
			boolean consumedSome = false;

			for (int i = 0; i < handler.getSlots() && innerCount > 0; i++) {
				ItemStack stack = handler.getStackInSlot(i);
				if (matcher.test(stack)) {
					ItemStack consumed = handler.extractItem(i, innerCount, false);
					innerCount -= consumed.getCount();
					consumedSome = true;
				}
			}

			return consumedSome;
		}

		return false;
	}

	public static boolean consumeInventoryItem(final Player player, final Item item) {
		return consumeInventoryItem(player.getInventory().armor, item) || consumeInventoryItem(player.getInventory().items, item) || consumeInventoryItem(player.getInventory().offhand, item);
	}

	public static boolean consumeInventoryItem(final NonNullList<ItemStack> stacks, final Item item) {
		for (ItemStack stack : stacks) {
			if (stack.getItem() == item) {
				stack.shrink(1);
				CompoundTag nbt = stack.getOrCreateTag();
				if (nbt.contains("BlockStateTag")) {
					CompoundTag damageNbt = nbt.getCompound("BlockStateTag");
					if (damageNbt.contains("damage")) {
						damage = damageNbt.getInt("damage");
					}
				}
				return true;
			}
		}

		return false;
	}

	public static NonNullList<ItemStack> sortArmorForCasket(Player player) {
		NonNullList<ItemStack> armor = player.getInventory().armor;
		Collections.reverse(armor);
		return armor;
	}

	public static NonNullList<ItemStack> sortInvForCasket(Player player) {
		NonNullList<ItemStack> inv = player.getInventory().items;
		NonNullList<ItemStack> sorted = NonNullList.create();
		//hotbar at the bottom
		sorted.addAll(inv.subList(9, 36));
		sorted.addAll(inv.subList(0, 9));

		return sorted;
	}

	public static NonNullList<ItemStack> splitToSize(ItemStack stack) {

		NonNullList<ItemStack> result = NonNullList.create();

		int size = stack.getMaxStackSize();

		while (!stack.isEmpty()) {
			result.add(stack.split(size));
		}

		return result;
	}

	public static boolean hasToolMaterial(ItemStack stack, Tier tier) {

		Item item = stack.getItem();

		// see TileEntityFurnace.getItemBurnTime
		if (item instanceof TieredItem tieredItem && tier.equals(tieredItem.getTier())) {
			return true;
		}
		if (item instanceof SwordItem sword && tier.equals(sword.getTier())) {
			return true;
		}
		return item instanceof HoeItem hoe && tier.equals(hoe.getTier());
	}

	public static void clearInfoTag(ItemStack stack, String key) {
		CompoundTag nbt = stack.getTag();
		if (nbt != null) {
			nbt.remove(key);
			if (nbt.isEmpty()) {
				stack.setTag(null);
			}
		}
	}

	//[VanillaCopy] of Inventory.load, but removed clearing all slots
	//also add a handler to move items to the next available slot if the slot they want to go to isnt available
	public static void loadNoClear(ListTag tag, Inventory inventory) {

		List<ItemStack> blockedItems = new ArrayList<>();

		for (int i = 0; i < tag.size(); ++i) {
			CompoundTag compoundtag = tag.getCompound(i);
			int j = compoundtag.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.of(compoundtag);
			if (!itemstack.isEmpty()) {
				if (j < inventory.items.size()) {
					if (inventory.items.get(j).isEmpty()) {
						inventory.items.set(j, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				} else if (j >= 100 && j < inventory.armor.size() + 100) {
					if (inventory.armor.get(j - 100).isEmpty()) {
						inventory.armor.set(j - 100, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				} else if (j >= 150 && j < inventory.offhand.size() + 150) {
					if (inventory.offhand.get(j - 150).isEmpty()) {
						inventory.offhand.set(j - 150, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				}
			}
		}

		if(!blockedItems.isEmpty()) blockedItems.forEach(inventory::add);
	}
}
