package twilightforest.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTFGiantSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFGiantSword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == TFItems.ironwood_ingot || super.getIsRepairable(stack, material);
	}

	@Override
	@Nonnull
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());

			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 10 + getDamageVsEntity(), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5, 0));
		}

		return multimap;
	}
}
