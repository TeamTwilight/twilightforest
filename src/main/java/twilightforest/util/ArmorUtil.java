package twilightforest.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import tamaized.beanification.Component;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEquipmentAssets;

import java.util.Optional;

@Component
public class ArmorUtil {
	public float getShroudedArmorPercentage(LivingEntity entity) {
		int shroudedArmor = 0;
		int armorSlots = 0;

		for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
			if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
				continue;
			}

			ItemStack stack = entity.getItemBySlot(slot);

			if (!stack.isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
				shroudedArmor++;
			}

			armorSlots++;
		}

		return armorSlots > 0 ? (float) shroudedArmor / (float) armorSlots : 0.0F;
	}

	public static void updateEmperorsClothEquippable(ItemStack stack) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);

		if (equippable == null)
			return;

		if (stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			if (equippable.assetId().filter(TFEquipmentAssets.HIDDEN::equals).isPresent())
				return;

			stack.set(DataComponents.EQUIPPABLE, new Equippable(
				equippable.slot(),
				equippable.equipSound(),
				Optional.of(TFEquipmentAssets.HIDDEN),
				equippable.cameraOverlay(),
				equippable.allowedEntities(),
				equippable.dispensable(),
				equippable.swappable(),
				equippable.damageOnHurt(),
				equippable.equipOnInteract(),
				equippable.canBeSheared(),
				equippable.shearingSound()
			));
		} else {
			stack.set(DataComponents.EQUIPPABLE, stack.getPrototype().get(DataComponents.EQUIPPABLE));
		}
	}
}
