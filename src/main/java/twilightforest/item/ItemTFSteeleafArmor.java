package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafArmor extends ItemTFArmor implements ModelRegisterCallback {

	public ItemTFSteeleafArmor(ItemArmor.ArmorMaterial material, EntityEquipmentSlot slot, EnumRarity rarity) {
		super(material, slot, rarity);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.armorType) {
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
