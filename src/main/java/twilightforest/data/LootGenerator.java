package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootGenerator extends LootTableProvider {
	public LootGenerator(DataGenerator generator) {
		super(generator);
	}

	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList.of(
					Pair.of(BlockLootTables::new, LootParameterSets.BLOCK)
	);

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
		// [VanillaCopy] super, but remove call that checks that all vanilla tables are accounted for, because we aren't vanilla.
		map.forEach((id, builder) -> LootTableManager.check(validationtracker, id, builder));
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
		return tables;
	}

	@Override
	public String getName() {
		return "TwilightForest loot tables";
	}
}
