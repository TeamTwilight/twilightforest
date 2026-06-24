package twilightforest.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tamaized.beanification.Component;
import twilightforest.init.TFDataComponents;

@Component
public class ArmorUtil {
	public float getShroudedArmorPercentage(LivingEntity entity) {
		int shroudedArmor = 0;
		int armorSlots = 0;

		for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
			ItemStack stack = entity.getItemBySlot(slot);

			if (!stack.isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
				shroudedArmor++;
			}

			armorSlots++;
		}

		return armorSlots > 0 ? (float) shroudedArmor / (float) armorSlots : 0.0F;
	}
}
