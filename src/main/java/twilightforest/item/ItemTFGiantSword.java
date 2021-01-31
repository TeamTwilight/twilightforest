package twilightforest.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ItemTFGiantSword extends SwordItem {

	public ItemTFGiantSword(IItemTier material, Properties props) {
		super(material, 10 + (int)material.getAttackDamage(), -3.5F, props);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == TFItems.ironwood_ingot.get() || super.getIsRepairable(stack, material);
	}

	//TODO: ImmutableMap. This is unviable
//	@Override
//	@Nonnull
//	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
//		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
//
//		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
//			multimap.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Weapon modifier", 2.5, AttributeModifier.Operation.ADDITION));
//		}
//
//		return multimap;
//	}
}
