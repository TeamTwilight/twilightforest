package twilightforest.item.travellers_gear;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.Optional;

public class TravellersArmorBeltItem extends TravellersArmorItem {

	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots, int durability) {
		super(equipmentType, properties, insertableModifierSlots, durability);
	}

	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots) {
		super(equipmentType, properties, insertableModifierSlots);
	}

	public static Properties beltProperties(Properties properties) {
		return properties
			.component(TFDataComponents.SWAP_HOTBAR_ABILITY, Unit.INSTANCE)
			.component(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(9, ItemStack.EMPTY)))
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, Unit.INSTANCE);
	}

	@Override
	public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
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
		ItemStack legArmor = player.getInventory().getArmor(EquipmentSlot.LEGS.getIndex());
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!hasSwapHotbar(player.registryAccess(), legArmor) || containerContents == null)
			return;

		NonNullList<ItemStack> hotbarStacks = NonNullList.withSize(9, ItemStack.EMPTY);
		Inventory inventory = player.getInventory();
		boolean hasChanged = false;
		for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
			ItemStack inventoryStack = inventory.getItem(slotIndex);
			ItemStack beltStack = containerContents.getSlots() <= slotIndex ? ItemStack.EMPTY : containerContents.getStackInSlot(slotIndex);
			if (inventoryStack.getItem().canFitInsideContainerItems() && !inventoryStack.is(ItemTagGenerator.TRAVELLERS_BELT_BLACKLISTED)) {
				hotbarStacks.set(slotIndex, inventoryStack);
				inventory.setItem(slotIndex, beltStack);
				if (!beltStack.equals(inventoryStack))
					hasChanged = true;
			} else {
				hotbarStacks.set(slotIndex, beltStack);
			}
		}
		legArmor.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(hotbarStacks));
		if (hasChanged)
			player.level().playSound(null, player, TFSounds.SWAP_HOTBAR.get(), SoundSource.PLAYERS, 1F, 1F);
	}

	public static boolean hasSwapHotbar(HolderLookup.Provider registries, ItemStack stack) {
		return (TravellersModifiersManager.isModifierActive(registries, stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER) || TravellersModifiersManager.isModifierActive(registries, stack, TravellersModifiersManager.SWAP_HOTBAR_ABILITY))
			&& stack.has(DataComponents.CONTAINER);
	}

	public record Tooltip(ItemContainerContents contents) implements TooltipComponent {}
}
