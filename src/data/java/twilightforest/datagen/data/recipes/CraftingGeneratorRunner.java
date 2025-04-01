package twilightforest.datagen.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class CraftingGeneratorRunner extends RecipeProvider.Runner {

	public CraftingGeneratorRunner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
		return new CraftingGenerator(output, provider);
	}

	@Override
	public String getName() {
		return "Twilight Forest Recipes";
	}
}
