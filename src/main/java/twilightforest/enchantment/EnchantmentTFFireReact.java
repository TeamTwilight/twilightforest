package twilightforest.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentTFFireReact extends Enchantment {
	// TODO implement
	public EnchantmentTFFireReact(Rarity rarity) {
		super(rarity, EnchantmentType.ARMOR, new EquipmentSlotType[]{
				EquipmentSlotType.HEAD, EquipmentSlotType.CHEST,
				EquipmentSlotType.LEGS, EquipmentSlotType.FEET
		});
	}
	
	@Override
	public boolean canGenerateInLoot() {
		return false;
	}
	
	@Override
	public boolean canVillagerTrade() {
	      return false;
	}
}
