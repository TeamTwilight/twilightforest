package twilightforest.datagen.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

public class EntityInteractLootTables implements LootTableSubProvider {
	protected final LootTableSubProvider.Context output;

	public EntityInteractLootTables(LootTableSubProvider.Context output) {
		this.output = output;
	}

	@Override
	public void run() {
		output.accept(TFLootTables.RAVEN_INTERACTION, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(TFItems.RAVEN_FEATHER))));
	}
}
