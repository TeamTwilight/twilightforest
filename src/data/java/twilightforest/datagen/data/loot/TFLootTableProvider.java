package twilightforest.datagen.data.loot;

import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import twilightforest.loot.TFLootTables;

import java.util.List;

public class TFLootTableProvider {
	
	public static SingleRegistryBootstrap<LootTable> create() {
		return new LootTableProvider(
			TFLootTables.allBuiltin(),
			List.of(
				new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(BlockInteractLootTables::new, LootContextParamSets.BLOCK_INTERACT),
				new LootTableProvider.SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY),
				new LootTableProvider.SubProviderEntry(EntityInteractLootTables::new, LootContextParamSets.ENTITY_INTERACT),
				new LootTableProvider.SubProviderEntry(SpecialLootTables::new, LootContextParamSets.EMPTY)
			)
		);
	}
}
