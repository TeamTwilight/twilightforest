package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootGenerator extends LootTableProvider {
	public LootGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, TFLootTables.allBuiltin(), List.of(
			new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK),
			new LootTableProvider.SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST),
			new LootTableProvider.SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY),
			new LootTableProvider.SubProviderEntry(SpecialLootTables::new, LootContextParamSets.EMPTY)
		), provider);
	}

	@Override
	protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {

	}
}
