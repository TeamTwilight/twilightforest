package twilightforest.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.codehaus.plexus.util.StringUtils;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFDataComponents;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TFItemStackUtils {

	public static boolean consumeInventoryItem(final Player player, final Item item, CompoundTag persistentTag, boolean saveItemToTag) {
		return consumeInventoryItem(player.getInventory().armor, item, persistentTag, saveItemToTag, player.registryAccess())
			|| consumeInventoryItem(player.getInventory().items, item, persistentTag, saveItemToTag, player.registryAccess())
			|| consumeInventoryItem(player.getInventory().offhand, item, persistentTag, saveItemToTag, player.registryAccess());
	}

	public static boolean consumeInventoryItem(final NonNullList<ItemStack> stacks, final Item item, CompoundTag persistentTag, boolean saveItemToTag, HolderLookup.Provider provider) {
		for (ItemStack stack : stacks) {
			if (stack.is(item)) {
				if (saveItemToTag) persistentTag.put(CharmEvents.CONSUMED_CHARM_TAG, stack.save(provider));
				BlockItemStateProperties blockItemStateProperties = stack.get(DataComponents.BLOCK_STATE);
				if (blockItemStateProperties != null && blockItemStateProperties.properties().containsKey(KeepsakeCasketBlock.BREAKAGE.getName())) {
					String propertyValueString = blockItemStateProperties.properties().get(KeepsakeCasketBlock.BREAKAGE.getName());

					persistentTag.putInt(CharmEvents.CASKET_DAMAGE_TAG, StringUtils.isNumeric(propertyValueString) ? Integer.parseInt(propertyValueString) : 0);
				} else if (stack.has(TFDataComponents.CASKET_DAMAGE)) {
					persistentTag.putInt(CharmEvents.CASKET_DAMAGE_TAG, stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0));
				}
				stack.shrink(1);
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


	public static boolean hasInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		return customData != null && customData.contains(key);
	}

	public static void addInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		CompoundTag nbt = customData == null ? new CompoundTag() : customData.copyTag();
		nbt.putBoolean(key, true);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
	}

	public static void clearInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData != null) {
			CompoundTag nbt = customData.copyTag();
			nbt.remove(key);
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
		}
	}

	//[VanillaCopy] of Inventory.load, but removed clearing all slots
	//also add a handler to move items to the next available slot if the slot they want to go to isnt available
	public static void loadNoClear(RegistryAccess registryAccess, ListTag tag, Inventory inventory) {

		List<ItemStack> blockedItems = new ArrayList<>();

		for (int i = 0; i < tag.size(); ++i) {
			CompoundTag compoundtag = tag.getCompound(i);
			int j = compoundtag.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.parseOptional(registryAccess, compoundtag);
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

		if (!blockedItems.isEmpty()) blockedItems.forEach(inventory::add);
	}

	public static void hurtButDontBreak(ItemStack stack, int amount, ServerLevel level, @Nullable LivingEntity entity) {
		if (stack.isDamageableItem()) {
			amount = stack.getItem().damageItem(stack, amount, entity, item -> {});
			if (entity == null || !entity.hasInfiniteMaterials()) {
				if (amount > 0) {
					amount = EnchantmentHelper.processDurabilityChange(level, stack, amount);
					if (amount <= 0) {
						return;
					}
				}

				if (entity instanceof ServerPlayer sp && amount != 0) {
					CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(sp, stack, stack.getDamageValue() + amount);
				}

				int i = stack.getDamageValue() + amount;
				stack.setDamageValue(i);
			}
		}
	}
}
