package twilightforest.datagen.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.RecipePrioritiesProvider;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class RecipePriorityGenerator extends RecipePrioritiesProvider {

	public RecipePriorityGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, TwilightForestMod.ID);
	}

	@Override
	protected void start() {
		this.add("chiseled_canopy_bookshelf", 1);

		this.add("wood/twilight_oak_chest", 1);
		this.add("wood/canopy_chest", 1);
		this.add("wood/mangrove_chest", 1);
		this.add("wood/dark_chest", 1);
		this.add("wood/time_chest", 1);
		this.add("wood/transformation_chest", 1);
		this.add("wood/mining_chest", 1);
		this.add("wood/sorting_chest", 1);

		this.add("wood/twilight_oak_trapped_chest", 1);
		this.add("wood/canopy_trapped_chest", 1);
		this.add("wood/mangrove_trapped_chest", 1);
		this.add("wood/dark_trapped_chest", 1);
		this.add("wood/time_trapped_chest", 1);
		this.add("wood/transformation_trapped_chest", 1);
		this.add("wood/mining_trapped_chest", 1);
		this.add("wood/sorting_trapped_chest", 1);
	}
}
