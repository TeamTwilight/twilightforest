package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemTFTrophy extends WallOrFloorItem {

	public ItemTFTrophy(Block floorBlockIn, Block wallBlockIn, Item.Properties builder) {
		super(floorBlockIn, wallBlockIn, builder);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.HEAD;
	}

	@Override
	@Nullable
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.HEAD;
	}
}
