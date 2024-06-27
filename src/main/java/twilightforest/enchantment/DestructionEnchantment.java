package twilightforest.enchantment;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.ChainBlockItem;

public class DestructionEnchantment extends LootOnlyEnchantment {
	public DestructionEnchantment(int weight) {
		super(ItemTagGenerator.BLOCK_AND_CHAIN_ENCHANTABLE, weight, 3, Enchantment.dynamicCost(5, 9), Enchantment.dynamicCost(20, 9), 2, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ChainBlockItem;
	}

	@Override
	public float getDamageBonus(int level, EntityType<?> entityType, ItemStack enchantedItem) {
		return -level * 1.5F;
	}
}
