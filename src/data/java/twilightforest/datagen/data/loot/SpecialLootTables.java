package twilightforest.datagen.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

public class SpecialLootTables implements LootTableSubProvider {
	protected final LootTableSubProvider.Context output;

	public SpecialLootTables(LootTableSubProvider.Context output) {
		this.output = output;
	}

	@Override
	public void run() {
		output.accept(TFLootTables.CICADA_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(Items.DYE.gray()))));
		output.accept(TFLootTables.FIREFLY_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(Items.GLOWSTONE_DUST))));
		output.accept(TFLootTables.MOONWORM_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(Items.DYE.lime()))));

		output.accept(TFLootTables.MOONWORM_FAILED_TO_PLACE_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFBlocks.MOONWORM.asItem()))));

		output.accept(TFLootTables.LIFEDRAIN_SCEPTER_KILL_BONUS, LootTable.lootTable().withPool(
			LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)).apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
		));

		output.accept(TFLootTables.KNIGHT_PHANTOM_DEFEATED, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.KNIGHT_PHANTOM_TROPHY))));

		output.accept(TFLootTables.OMINOUS_SPAWNER_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.EXANIMATE_ESSENCE))));
	}
}
