package twilightforest.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tamaized.beanification.Component;
import twilightforest.init.TFDataComponents;

@Component
public class ArmorUtil {
	public float getShroudedArmorPercentage(LivingEntity entity) {
		int shroudedArmor = 0;
		int nonShroudedArmor = 0;

		EquipmentSlot[] armorSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

		for (EquipmentSlot slot : armorSlots) {
			ItemStack stack = entity.getItemBySlot(slot);

			if (!stack.isEmpty()) {
				if (stack.has(TFDataComponents.EMPERORS_CLOTH)) {
					shroudedArmor++;
				}
			}

			nonShroudedArmor++;
		}

		return nonShroudedArmor > 0 && shroudedArmor > 0 ? (float) shroudedArmor / (float) nonShroudedArmor : 0.0F;
	}
}

