package twilightforest.datagen.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

import java.util.function.BiConsumer;

public record BlockInteractLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		consumer.accept(TFLootTables.RASPBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.RASPBERRY))));
		consumer.accept(TFLootTables.BLUEBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLUEBERRY))));
		consumer.accept(TFLootTables.BLACKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLACKBERRY))));
		consumer.accept(TFLootTables.MALOBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.MALOBERRY))));
		consumer.accept(TFLootTables.BLIGHTBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLIGHTBERRY))));
		consumer.accept(TFLootTables.DUSKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.DUSKBERRY))));
		consumer.accept(TFLootTables.SKYBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.SKYBERRY))));
		consumer.accept(TFLootTables.STINGBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.STINGBERRY))));

		consumer.accept(TFLootTables.COPPER_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.COPPER_BERRY))));
		consumer.accept(TFLootTables.IRON_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.IRON_BERRY))));
		consumer.accept(TFLootTables.GOLD_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.GOLD_BERRY))));
		consumer.accept(TFLootTables.ESSENCE_BERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.ESSENCE_BERRY))));

		consumer.accept(TFLootTables.TORCHBERRY_PLANT_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.TORCHBERRIES))));
		consumer.accept(TFLootTables.TROLLBER_PLANT_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.TORCHBERRIES))));
		consumer.accept(
			TFLootTables.LIVEROOT_STRIP_DROPS,
			LootTable.lootTable().withPool(
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.LIVEROOT).apply(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
					.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.AXE_STRIP))
			)
		);
	}
}
