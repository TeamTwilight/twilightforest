package twilightforest.item.travellers_gear;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFDataComponents;

import java.util.Optional;

public class TravellersArmorBeltItem extends TravellersArmorItem {
	@SuppressWarnings("unused")
	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties, int durability) {
		super(equipmentType, beltProperties(properties), durability);
	}

	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties) {
		super(equipmentType, beltProperties(properties), 4);
	}

	public static Properties beltProperties(Properties properties) {
		return properties
			.component(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(9, ItemStack.EMPTY)))
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, true);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
			? Optional.ofNullable(stack.get(DataComponents.CONTAINER)).map(Tooltip::new)
			: Optional.empty();
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}

	public static void travellersTrySwapHotbar(Player player) {
		ItemStack legArmor = player.getInventory().getArmor(EquipmentSlot.LEGS.getIndex());
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!legArmor.has(TFDataComponents.TRAVELLERS_HAS_BELT) || containerContents == null)
			return;

		NonNullList<ItemStack> hotbarStacks = NonNullList.withSize(9, ItemStack.EMPTY);
		Inventory inventory = player.getInventory();
		for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
			ItemStack inventoryStack = inventory.getItem(slotIndex);
			ItemStack beltStack = containerContents.getSlots() <= slotIndex ? ItemStack.EMPTY : containerContents.getStackInSlot(slotIndex);
			if (inventoryStack.getItem().canFitInsideContainerItems() && !inventoryStack.is(ItemTagGenerator.TRAVELLERS_BELT_BLACKLISTED)) {
				hotbarStacks.set(slotIndex, inventoryStack);
				inventory.setItem(slotIndex, beltStack);
			} else {
				hotbarStacks.set(slotIndex, beltStack);
			}
		}
		legArmor.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(hotbarStacks));
	}

	public record Tooltip(ItemContainerContents contents) implements TooltipComponent {}
}
