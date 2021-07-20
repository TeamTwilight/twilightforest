package twilightforest.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import twilightforest.TwilightForestMod;

public class ItemTFIronwoodArmor extends ArmorItem {

	public ItemTFIronwoodArmor(IArmorMaterial armorMaterial, EquipmentSlotType armorType, Properties props) {
		super(armorMaterial, armorType, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
		if (slot == EquipmentSlotType.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "ironwood_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "ironwood_1.png";
		}
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.getEquipmentSlot()) {
				case HEAD:
					istack.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
					break;
				case CHEST:
				case LEGS:
					istack.addEnchantment(Enchantments.PROTECTION, 1);
					break;
				case FEET:
					istack.addEnchantment(Enchantments.FEATHER_FALLING, 1);
					break;
				default:
					break;
			}
			list.add(istack);
		}
	}
}
