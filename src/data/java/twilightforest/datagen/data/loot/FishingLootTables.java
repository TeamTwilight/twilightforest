package twilightforest.datagen.data.loot;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

import java.util.function.BiConsumer;

public record FishingLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		consumer.accept(TFLootTables.SPOOKY_FOREST_FISHING, LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_JUNK)
					.setWeight(10)
					.setQuality(-2))
				.add(NestedLootTable.lootTableReference(TFLootTables.SPOOKY_FOREST_FISHING_TREASURE)
					.setWeight(5)
					.setQuality(2)
					.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true)))))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH)
					.setWeight(85)
					.setQuality(-1))));

		consumer.accept(TFLootTables.SPOOKY_FOREST_FISHING_TREASURE, LootTable.lootTable()
			.withPool(
				LootPool.lootPool()
					.add(LootItem.lootTableItem(TFBlocks.VEILWOOD_SAPLING))
					.add(LootItem.lootTableItem(TFBlocks.WEBWORM))
					.add(
						LootItem.lootTableItem(TFItems.IRONWOOD_AXE)
							.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
							.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(35.0F)))
					)
					.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH))
					.add(LootItem.lootTableItem(TFItems.LIVEROOT))
					.add(LootItem.lootTableItem(TFBlocks.MASON_JAR))
			));
	}
}