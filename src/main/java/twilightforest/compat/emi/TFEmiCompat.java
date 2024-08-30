package twilightforest.compat.emi;

import com.mojang.datafixers.util.Pair;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import dev.emi.emi.recipe.EmiGrindstoneRecipe;
import dev.emi.emi.recipe.special.EmiAnvilEnchantRecipe;
import dev.emi.emi.recipe.special.EmiAnvilRepairItemRecipe;
import dev.emi.emi.recipe.special.EmiGrindstoneDisenchantingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import twilightforest.config.TFConfig;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.recipes.*;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@EmiEntrypoint
public class TFEmiCompat implements EmiPlugin {
	public static final TFEmiRecipeCategory UNCRAFTING = new TFEmiRecipeCategory("uncrafting", TFBlocks.UNCRAFTING_TABLE);
	public static final TFEmiRecipeCategory CRUMBLE_HORN = new TFEmiRecipeCategory("crumble_horn", TFItems.CRUMBLE_HORN);
	public static final TFEmiRecipeCategory TRANSFORMATION = new TFEmiRecipeCategory("transformation", TFItems.TRANSFORMATION_POWDER);
	public static final TFEmiRecipeCategory MOONWORM_QUEEN = new TFEmiRecipeCategory("moonworm_queen", TFItems.MOONWORM_QUEEN);

	private static final Function<List<EmiIngredient>, Boolean> CANT_USE_ENCHANTS = stack ->
		stack.contains(EmiStack.of(TFItems.MOONWORM_QUEEN)) || stack.contains(EmiStack.of(TFItems.LAMP_OF_CINDERS)) || stack.contains(EmiStack.of(TFItems.ORE_MAGNET)) ||
			stack.contains(EmiStack.of(TFItems.TWILIGHT_SCEPTER)) || stack.contains(EmiStack.of(TFItems.LIFEDRAIN_SCEPTER)) ||
			stack.contains(EmiStack.of(TFItems.ZOMBIE_SCEPTER)) || stack.contains(EmiStack.of(TFItems.FORTIFICATION_SCEPTER));

	private static final Function<List<EmiIngredient>, Boolean> NO_REPAIRING = stack ->
		stack.contains(EmiStack.of(TFItems.LAMP_OF_CINDERS)) || stack.contains(EmiStack.of(TFItems.GLASS_SWORD)) || stack.contains(EmiStack.of(TFItems.MAZEBREAKER_PICKAXE));

	@Override
	@SuppressWarnings("unchecked")
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
		if (!TFConfig.disableEntireTable) {
			List<RecipeHolder<? extends CraftingRecipe>> recipes = RecipeViewerConstants.getAllUncraftingRecipes(manager);
			recipes.forEach(recipe -> registry.addRecipe(new EmiUncraftingRecipe<>(recipe)));
		}
		for (RecipeViewerConstants.TransformationPowderInfo info : RecipeViewerConstants.getTransformationPowderRecipes()) {
			registry.addRecipe(new EmiTransformationPowderRecipe(info.input(), info.output(), info.reversible()));
		}

		for (Pair<Block, Block> info : RecipeViewerConstants.getCrumbleHornRecipes()) {
			registry.addRecipe(new EmiCrumbleHornRecipe(info.getFirst(), info.getSecond()));
		}
		registry.addRecipe(new EmiMoonwormQueenRecipe());
		registry.addRecipe(new EmiEmperorsClothRecipe());

		for (RecipeHolder<SmithingRecipe> holder : manager.getAllRecipesFor(RecipeType.SMITHING).stream().filter(holder -> holder.value() instanceof NoTemplateSmithingRecipe).toList()) {
			NoTemplateSmithingRecipe recipe = (NoTemplateSmithingRecipe) holder.value();
			registry.addRecipe(new EmiNoSmithingTemplateRecipe(EmiIngredient.of(recipe.getBase()), EmiIngredient.of(recipe.getAddition()), EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())), recipe));
		}

		for (RecipeHolder<ScepterRepairRecipe> holder : manager.getAllRecipesFor(RecipeType.CRAFTING).stream().filter(holder -> holder.value() instanceof ScepterRepairRecipe).map(RecipeHolder.class::cast).toList()) {
			registry.addRecipe(new EmiScepterRepairRecipe(holder.value().getIngredients().stream().map(EmiIngredient::of).toList(), EmiStack.of(holder.value().getScepter()), holder.id()));
		}

		//remove other recipes as they arent actually possible recipes to use
		//emi makes a few assumptions about damageable items that it honestly shouldnt
		registry.removeRecipes(recipe -> {
			if (recipe instanceof EmiPatternCraftingRecipe || recipe instanceof EmiGrindstoneRecipe) {
				return recipe.getInputs().contains(EmiStack.of(TFItems.MOONWORM_QUEEN)) || NO_REPAIRING.apply(recipe.getInputs());
			} else if (recipe instanceof EmiGrindstoneDisenchantingRecipe || recipe instanceof EmiAnvilEnchantRecipe) {
				return CANT_USE_ENCHANTS.apply(recipe.getInputs());
			} else if (recipe instanceof EmiAnvilRepairItemRecipe || recipe instanceof EmiAnvilRecipe) {
				return NO_REPAIRING.apply(recipe.getInputs());
			}
			return false;
		});
	}
}
