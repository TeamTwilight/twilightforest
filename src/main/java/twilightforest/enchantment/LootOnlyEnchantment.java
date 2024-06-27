package twilightforest.enchantment;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public class LootOnlyEnchantment extends Enchantment {

	protected LootOnlyEnchantment(TagKey<Item> supportedItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, EquipmentSlot... slots) {
		this(supportedItems, Optional.empty(), weight, maxLevel, minCost, maxCost, anvilCost, slots);
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	protected LootOnlyEnchantment(TagKey<Item> supportedItems, Optional<TagKey<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, EquipmentSlot... slots) {
		super(new Enchantment.EnchantmentDefinition(supportedItems, primaryItems, weight, maxLevel, minCost, maxCost, anvilCost, FeatureFlags.DEFAULT_FLAGS, slots));
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}
}
