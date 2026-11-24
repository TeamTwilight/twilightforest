package twilightforest.datagen.data.reloadable.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

public class TFBlockInteractLoot implements LootTableSubProvider {

	protected final LootTableSubProvider.Context output;

	public TFBlockInteractLoot(LootTableSubProvider.Context output) {
		this.output = output;
	}

	@Override
	public void run() {
		output.accept(TFLootTables.RASPBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.RASPBERRY))));
		output.accept(TFLootTables.BLUEBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.BLUEBERRY))));
		output.accept(TFLootTables.BLACKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.BLACKBERRY))));
		output.accept(TFLootTables.MALOBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.MALOBERRY))));
		output.accept(TFLootTables.BLIGHTBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.BLIGHTBERRY))));
		output.accept(TFLootTables.DUSKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.DUSKBERRY))));
		output.accept(TFLootTables.SKYBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.SKYBERRY))));
		output.accept(TFLootTables.STINGBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.STINGBERRY))));

		output.accept(TFLootTables.COPPER_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.COPPER_BERRY))));
		output.accept(TFLootTables.IRON_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.IRON_BERRY))));
		output.accept(TFLootTables.GOLD_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.GOLD_BERRY))));
		output.accept(TFLootTables.ESSENCE_BERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.ESSENCE_BERRY))));

		output.accept(TFLootTables.TORCHBERRY_PLANT_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.TORCHBERRIES))));
		output.accept(TFLootTables.TROLLBER_PLANT_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.TORCHBERRIES))));
		output.accept(
			TFLootTables.LIVEROOT_STRIP_DROPS,
			LootTable.lootTable().withPool(
				LootPool.lootPool()
					.setRolls(ContextIntProviders.exactly(1))
					.add(LootItem.lootTableItem(TFItems.LIVEROOT).apply(ApplyBonusCount.addOreBonusCount(output.holderLookup(Registries.ENCHANTMENT).orElseThrow().getOrThrow(Enchantments.FORTUNE))))
					.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.AXE_STRIP))
			)
		);
	}
}
