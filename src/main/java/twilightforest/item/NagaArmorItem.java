package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import static twilightforest.TFConstants.ARMOR_DIR;

public class NagaArmorItem extends ArmorItem {
	protected NagaArmorItem(ArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties props) {
		super(materialIn, equipmentSlotIn, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return ARMOR_DIR + "naga_scale_2.png";
		} else {
			return ARMOR_DIR + "naga_scale_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case CHEST -> istack.enchant(Enchantments.FIRE_PROTECTION, 3);
				case LEGS -> istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 3);
				default -> {
				}
			}
			list.add(istack);
		}
	}
}
