package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFDataComponents;

public class TravellersArmorBeltItem extends TravellersArmorItem {
	@SuppressWarnings("unused")
	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties, int durability) {
		super(equipmentType, beltProperties(properties), durability);
	}

	public TravellersArmorBeltItem(ArmorItem.Type equipmentType, Properties properties) {
		super(equipmentType, beltProperties(properties), 4);
	}

	public static void travellersTrySwapHotbar(Player player) {
		ItemStack legArmor = player.getInventory().getArmor(EquipmentSlot.LEGS.getIndex());
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!legArmor.has(TFDataComponents.TRAVELLERS_HAS_BELT) || containerContents == null)
			return;

		NonNullList<ItemStack> hotbarStacks = NonNullList.withSize(9, ItemStack.EMPTY);
		Inventory inventory = player.getInventory();
		for (int i = 0; i < 9; i++) {
			ItemStack inventoryStack = inventory.getItem(i);
			ItemStack beltStack = containerContents.getSlots() <= i ? ItemStack.EMPTY : containerContents.getStackInSlot(i);
			if (inventoryStack.getItem().canFitInsideContainerItems() && !inventoryStack.is(ItemTagGenerator.TRAVELLERS_BELT_BLACKLISTED)) {
				hotbarStacks.set(i, inventoryStack);
				inventory.setItem(i, beltStack);
			} else {
				hotbarStacks.set(i, beltStack);
			}
		}
		legArmor.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(hotbarStacks));
	}

	public static Properties beltProperties(Properties properties) {
		return properties
			.component(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(9, ItemStack.EMPTY)))
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, true);
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
}
