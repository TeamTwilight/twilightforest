package twilightforest.item.travellers_gear;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.tags.TFItemTags;

import java.util.Optional;

public class TravellersArmorBeltItem extends TravellersArmorItem {
	public static final ItemContainerContents DEFAULT_EMPTY_BELT_CONTAINER = ItemContainerContents.fromItems(NonNullList.withSize(9, ItemStack.EMPTY));

	public TravellersArmorBeltItem(int insertableModifierSlots, Properties properties) {
		super(insertableModifierSlots, properties);
	}

	public static Properties beltProperties(Properties properties) {
		return properties
			.component(TFDataComponents.SWAP_HOTBAR_ABILITY, Unit.INSTANCE)
			.component(DataComponents.CONTAINER, DEFAULT_EMPTY_BELT_CONTAINER)
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, Unit.INSTANCE);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		return display.shows(DataComponents.CONTAINER)
			? Optional.ofNullable(stack.get(DataComponents.CONTAINER)).map(Tooltip::new)
			: Optional.empty();
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}

	@Override
	public boolean canFitInsideContainerItems(ItemStack stack) {
		return !stack.has(DataComponents.CONTAINER);
	}

	public static void travellersTrySwapHotbar(Player player) {
		ItemStack legArmor = player.getInventory().getItem(EquipmentSlot.LEGS.getIndex(Inventory.INVENTORY_SIZE));
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!hasSwapHotbar(player, legArmor) || containerContents == null)
			return;

		NonNullList<ItemStack> hotbarStacks = NonNullList.withSize(9, ItemStack.EMPTY);
		Inventory inventory = player.getInventory();
		boolean isSwapHotbarActive = isSwapHotbarActive(player, legArmor);
		boolean hasChanged = false;
		for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
			ItemStack inventoryStack = inventory.getItem(slotIndex);
			ItemStack beltStack = containerContents.getSlots() <= slotIndex ? ItemStack.EMPTY : containerContents.getStackInSlot(slotIndex);
			if (inventoryStack.canFitInsideContainerItems() && !inventoryStack.is(TFItemTags.TRAVELLERS_BELT_BLACKLISTED) && (isSwapHotbarActive || inventoryStack.isEmpty())) {
				hotbarStacks.set(slotIndex, inventoryStack);
				inventory.setItem(slotIndex, beltStack);
				if (!beltStack.equals(inventoryStack))
					hasChanged = true;
			} else {
				hotbarStacks.set(slotIndex, beltStack);
			}
		}
		ItemStack modifiedBelt = legArmor.copy();
		modifiedBelt.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(hotbarStacks));
		player.getInventory().setItem(EquipmentSlot.LEGS.getIndex(Inventory.INVENTORY_SIZE), modifiedBelt);
		if (hasChanged)
			player.level().playSound(null, player, TFSounds.SWAP_HOTBAR.get(), SoundSource.PLAYERS, 1F, 1F);
	}

	public static boolean isSwapHotbarActive(Player player, ItemStack stack) {
		return (TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER) || TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.SWAP_HOTBAR_ABILITY))
			&& stack.has(DataComponents.CONTAINER);
	}

	public static boolean hasSwapHotbar(Player player, ItemStack stack) {
		return (TravellersModifiersManager.hasTravellersModifier(player.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER) || TravellersModifiersManager.hasTravellersModifier(player.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_ABILITY))
			&& stack.has(DataComponents.CONTAINER);
	}

	public record Tooltip(ItemContainerContents contents) implements TooltipComponent {}
}
