package twilightforest.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import twilightforest.beans.Component;
import twilightforest.init.TFDataComponents;
import twilightforest.item.ArcticArmorItem;

import java.util.OptionalInt;

@Component
public class ArmorUtil {

	public float getShroudedArmorPercentage(LivingEntity entity) {
		int shroudedArmor = 0;
		int nonShroudedArmor = 0;

		for (ItemStack stack : entity.getArmorSlots()) {
			if (!stack.isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
				shroudedArmor++;
			}

			nonShroudedArmor++;
		}

		return nonShroudedArmor > 0 && shroudedArmor > 0 ? (float) shroudedArmor / (float) nonShroudedArmor : 0.0F;
	}

}
