package twilightforest.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFDataComponents;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TFItemStackUtils {
	public static boolean consumeInventoryItem(final Player player, final ItemLike item, CompoundTag persistentTag, boolean saveItemToTag) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (consumeInventoryItem(stack, item, persistentTag, saveItemToTag, player.registryAccess())) {
				return true;
			}
		}
		return false;
	}

	public static boolean consumeInventoryItem(final ItemStack stack, final ItemLike item, CompoundTag persistentTag, boolean saveItemToTag, HolderLookup.Provider provider) {
		if (stack.is(item.asItem())) {
			if (saveItemToTag) {
				ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).resultOrPartial(TwilightForestMod.LOGGER::error).ifPresent(tag -> persistentTag.put(CharmEvents.CONSUMED_CHARM_TAG, tag));
			}
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

		return false;
	}

	public static NonNullList<@NotNull ItemStack> sortArmorForCasket(Player player) {
		NonNullList<@NotNull ItemStack> armor = NonNullList.create();
		for (int i = 36; i < 41; i++) {
			armor.add(player.getInventory().getItem(i));
		}
		Collections.reverse(armor);
		return armor;
	}

	public static NonNullList<@NotNull ItemStack> sortInvForCasket(Player player) {
		NonNullList<@NotNull ItemStack> inv = NonNullList.create();
		for (int i = 0; i < 36; i++) {
			inv.add(player.getInventory().getItem(i));
		}
		NonNullList<@NotNull ItemStack> sorted = NonNullList.create();
		//hotbar at the bottom
		sorted.addAll(inv.subList(9, 36));
		sorted.addAll(inv.subList(0, 9));

		return sorted;
	}

	public static NonNullList<@NotNull ItemStack> splitToSize(ItemStack stack) {

		NonNullList<@NotNull ItemStack> result = NonNullList.create();

		int size = stack.getMaxStackSize();

		while (!stack.isEmpty()) {
			result.add(stack.split(size));
		}

		return result;
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
	public static void loadNoClear(HolderLookup.Provider registryAccess, ListTag tag, Inventory inventory) {
		List<ItemStack> blockedItems = new ArrayList<>();

		for (int i = 0; i < tag.size(); ++i) {
			CompoundTag compoundtag = tag.getCompoundOrEmpty(i);
			int j = compoundtag.getByteOr("Slot", (byte) 0) & 255;
			ItemStack itemstack = ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, compoundtag).resultOrPartial(_ -> {}).orElse(ItemStack.EMPTY);

			if (!itemstack.isEmpty()) {
				int targetSlot = -1;

				if (j < 36) {
					targetSlot = j;
				}
				else if (j >= 100 && j < 104) {
					targetSlot = 36 + (j - 100);
				}
				else if (j == 150) {
					targetSlot = 40;
				}

				if (targetSlot >= 0) {
					if (inventory.getItem(targetSlot).isEmpty()) {
						inventory.setItem(targetSlot, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				} else {
					blockedItems.add(itemstack);
				}
			}
		}

		if (!blockedItems.isEmpty()) {
			blockedItems.forEach(inventory::add);
		}
	}


	public static void hurtButDontBreak(ItemStack stack, int amount, ServerLevel level, @Nullable LivingEntity entity) {
		if (stack.isDamageableItem()) {
			amount = stack.getItem().damageItem(stack, amount, entity, _ -> {});
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

	// [VanillaCopy] from GiveCommand.giveItem
	public static void giveOrDrop(ItemStack itemStack, Player player) {
		boolean flag = player.getInventory().add(itemStack);
		if (flag && itemStack.isEmpty()) {
			ItemEntity itementity1 = player.drop(itemStack.copy(), false);
			if (itementity1 != null) {
				itementity1.makeFakeItem();
			}

			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.containerMenu.broadcastChanges();
		} else {
			ItemEntity itementity = player.drop(itemStack, false);
			if (itementity != null) {
				itementity.setNoPickUpDelay();
				itementity.setTarget(player.getUUID());
			}
		}
	}
}
