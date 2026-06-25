package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
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
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFDataComponents;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TFItemStackUtils {

	public static boolean consumeInventoryItem(final Player player, final ItemLike item, CompoundTag persistentTag, boolean saveItemToTag) {
		return consumeInventoryItem(getArmorItems(player), item, persistentTag, saveItemToTag, player.registryAccess())
			|| consumeInventoryItem(player.getInventory().getNonEquipmentItems(), item, persistentTag, saveItemToTag, player.registryAccess())
			|| consumeInventoryItem(getOffhandItems(player), item, persistentTag, saveItemToTag, player.registryAccess());
	}

	private static NonNullList<ItemStack> getArmorItems(Player player) {
		NonNullList<ItemStack> armor = NonNullList.create();
		armor.add(player.getItemBySlot(EquipmentSlot.FEET));
		armor.add(player.getItemBySlot(EquipmentSlot.LEGS));
		armor.add(player.getItemBySlot(EquipmentSlot.CHEST));
		armor.add(player.getItemBySlot(EquipmentSlot.HEAD));
		return armor;
	}

	private static NonNullList<ItemStack> getOffhandItems(Player player) {
		NonNullList<ItemStack> offhand = NonNullList.create();
		offhand.add(player.getItemBySlot(EquipmentSlot.OFFHAND));
		return offhand;
	}

	public static boolean consumeInventoryItem(final NonNullList<ItemStack> stacks, final ItemLike item, CompoundTag persistentTag, boolean saveItemToTag, HolderLookup.Provider provider) {
		for (ItemStack stack : stacks) {
			if (stack.is(item.asItem())) {
				if (saveItemToTag) {
					DataResult<net.minecraft.nbt.Tag> result = ItemStack.OPTIONAL_CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), stack);
					result.result().ifPresent(tag -> persistentTag.put(CharmEvents.CONSUMED_CHARM_TAG, tag));
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
		}

		return false;
	}

	public static NonNullList<ItemStack> sortArmorForCasket(Player player) {
		NonNullList<ItemStack> armor = NonNullList.create();
		armor.add(player.getItemBySlot(EquipmentSlot.FEET));
		armor.add(player.getItemBySlot(EquipmentSlot.LEGS));
		armor.add(player.getItemBySlot(EquipmentSlot.CHEST));
		armor.add(player.getItemBySlot(EquipmentSlot.HEAD));
		Collections.reverse(armor);
		return armor;
	}

	public static NonNullList<ItemStack> sortInvForCasket(Player player) {
		NonNullList<ItemStack> inv = player.getInventory().getNonEquipmentItems();
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

	public static boolean hasToolMaterial(ItemStack stack) {
		return stack.has(DataComponents.TOOL);
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
			CompoundTag compoundtag = tag.getCompound(i).orElse(new CompoundTag());
			int j = compoundtag.getByte("Slot").orElse((byte)0) & 255;
			ItemStack itemstack = ItemStack.OPTIONAL_CODEC.decode(registryAccess.createSerializationContext(NbtOps.INSTANCE), compoundtag).result().map(Pair::getFirst).orElse(ItemStack.EMPTY);
			if (!itemstack.isEmpty()) {
				if (j < inventory.getNonEquipmentItems().size()) {
					if (inventory.getNonEquipmentItems().get(j).isEmpty()) {
						inventory.getNonEquipmentItems().set(j, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				} else if (j >= 100 && j < 104) {
					// Map old armor slot numbers (100-103) to equipment slots
					EquipmentSlot slot = switch (j - 100) {
						case 0 -> EquipmentSlot.FEET;
						case 1 -> EquipmentSlot.LEGS;
						case 2 -> EquipmentSlot.CHEST;
						case 3 -> EquipmentSlot.HEAD;
						default -> null;
					};
					if (slot != null && inventory.getItem(j).isEmpty()) {
						inventory.setItem(j, itemstack);
					} else {
						blockedItems.add(itemstack);
					}
				} else if (j >= 150 && j < 151) {
					if (inventory.getItem(40).isEmpty()) {
						inventory.setItem(40, itemstack);
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
