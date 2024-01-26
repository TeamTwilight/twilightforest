package twilightforest.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.NeoForgeMod;
import twilightforest.init.TFItems;

public class GiantSwordItem extends SwordItem implements GiantItem {

	public GiantSwordItem(Tier material, Properties properties) {
		super(material, 10, -3.5F, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.putAll(super.getDefaultAttributeModifiers(slot));
		attributeBuilder.put(NeoForgeMod.BLOCK_REACH.value(), new AttributeModifier(GIANT_REACH_MODIFIER, "Reach modifier", 2.5, AttributeModifier.Operation.ADDITION));
		attributeBuilder.put(NeoForgeMod.ENTITY_REACH.value(), new AttributeModifier(GIANT_RANGE_MODIFIER, "Range modifier", 2.5, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? attributeBuilder.build() : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
		return material.is(TFItems.IRONWOOD_INGOT) || super.isValidRepairItem(stack, material);
	}
}