package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import twilightforest.TwilightForestMod;

public class ItemTFSteeleafArmor extends ArmorItem {

	public ItemTFSteeleafArmor(IArmorMaterial material, EquipmentSlotType slot, Properties props) {
		super(material, slot, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
		if (slot == EquipmentSlotType.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
		}
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case HEAD:
					istack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 2);
					break;
				case CHEST:
					istack.addEnchantment(Enchantments.BLAST_PROTECTION, 2);
					break;
				case LEGS:
					istack.addEnchantment(Enchantments.FIRE_PROTECTION, 2);
					break;
				case FEET:
					istack.addEnchantment(Enchantments.FEATHER_FALLING, 2);
					break;
				default:
					break;
			}
			list.add(istack);
		}
	}
}
