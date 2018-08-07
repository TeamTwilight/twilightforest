package twilightforest.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
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
			AttributeModifier damageModifier = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 10 + getAttackDamage(), 0);
			AttributeModifier speedModifier = new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5, 0);

			// remove the entries added by superclass (to allow 'overwriting')
			multimap.remove(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), damageModifier);
			multimap.remove(SharedMonsterAttributes.ATTACK_SPEED.getName(), speedModifier);

			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), damageModifier);
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), speedModifier);
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Weapon modifier", 2.5, 0));
		}

		return multimap;
	}
}
