package twilightforest.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentTFChillAura extends Enchantment {
	// TODO implement
	public EnchantmentTFChillAura(Rarity rarity) {
		super(rarity, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[]{
				EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
		});
		this.setName("tfChillAura");
	}
}
