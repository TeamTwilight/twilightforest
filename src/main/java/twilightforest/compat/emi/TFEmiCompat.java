package twilightforest.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import twilightforest.TFConfig;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.recipes.EmiCrumbleHornRecipe;
import twilightforest.compat.emi.recipes.EmiMoonwormQueenRecipe;
import twilightforest.compat.emi.recipes.EmiTransformationPowderRecipe;
import twilightforest.compat.emi.recipes.EmiUncraftingRecipe;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.CrumbleRecipe;
import twilightforest.item.recipe.TransformPowderRecipe;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EmiEntrypoint
public class TFEmiCompat implements EmiPlugin {
	public static final TFEmiRecipeCategory UNCRAFTING = new TFEmiRecipeCategory("uncrafting", TFBlocks.UNCRAFTING_TABLE);
	public static final TFEmiRecipeCategory CRUMBLE_HORN = new TFEmiRecipeCategory("crumble_horn", TFItems.CRUMBLE_HORN);
	public static final TFEmiRecipeCategory TRANSFORMATION = new TFEmiRecipeCategory("transformation", TFItems.TRANSFORMATION_POWDER);
	public static final TFEmiRecipeCategory MOONWORM_QUEEN = new TFEmiRecipeCategory("moonworm_queen", TFItems.MOONWORM_QUEEN);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(UNCRAFTING);
		registry.addCategory(CRUMBLE_HORN);
		registry.addCategory(TRANSFORMATION);
		registry.addCategory(MOONWORM_QUEEN);

		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE));
		registry.addWorkstation(UNCRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE));
		registry.addWorkstation(CRUMBLE_HORN, EmiStack.of(TFItems.CRUMBLE_HORN));
		registry.addWorkstation(TRANSFORMATION, EmiStack.of(TFItems.TRANSFORMATION_POWDER));
		registry.addWorkstation(MOONWORM_QUEEN, EmiStack.of(TFItems.MOONWORM_QUEEN));

		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get()) {
			List<RecipeHolder<? extends CraftingRecipe>> recipes = RecipeViewerConstants.getAllUncraftingRecipes(manager);
			recipes.forEach(recipe -> registry.addRecipe(new EmiUncraftingRecipe<>(recipe)));
		}
		for (RecipeHolder<CrumbleRecipe> recipe : manager.getAllRecipesFor(TFRecipes.CRUMBLE_RECIPE.get())) {
			registry.addRecipe(new EmiCrumbleHornRecipe(recipe));
		}
		for (RecipeHolder<TransformPowderRecipe> recipe : manager.getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get())) {
			registry.addRecipe(new EmiTransformationPowderRecipe(recipe));
		}
		registry.addRecipe(new EmiMoonwormQueenRecipe());
	}
}
