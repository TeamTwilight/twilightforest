package twilightforest.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import twilightforest.config.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.categories.CrumbleHornCategory;
import twilightforest.compat.jei.categories.JEIUncraftingCategory;
import twilightforest.compat.jei.categories.MoonwormQueenCategory;
import twilightforest.compat.jei.categories.TransformationPowderCategory;
import twilightforest.compat.jei.renderers.EntityHelper;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.renderers.FakeItemEntityHelper;
import twilightforest.compat.jei.renderers.FakeItemEntityRenderer;
import twilightforest.compat.jei.util.CrumbleRecipe;
import twilightforest.compat.jei.util.TransformationRecipe;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFMenuTypes;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

	public static final IIngredientType<FakeEntityType> ENTITY_TYPE = () -> FakeEntityType.class;
	public static final IIngredientType<FakeItemEntity> FAKE_ITEM_ENTITY = () -> FakeItemEntity.class;

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if (!TFConfig.disableEntireTable) {
			registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), JEIUncraftingCategory.UNCRAFTING);
		}
		registration.addRecipeCatalyst(new ItemStack(TFItems.TRANSFORMATION_POWDER.get()), TransformationPowderCategory.TRANSFORMATION);
		registration.addRecipeCatalyst(new ItemStack(TFItems.CRUMBLE_HORN.get()), CrumbleHornCategory.CRUMBLE_HORN);
		registration.addRecipeCatalyst(new ItemStack(TFItems.MOONWORM_QUEEN.get()), MoonwormQueenCategory.MOONWORM_QUEEN);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(UncraftingMenu.class, TFMenuTypes.UNCRAFTING.get(), RecipeTypes.CRAFTING, 11, 9, 20, 36);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ENTITY_TYPE, Collections.emptyList(), new EntityHelper(), new EntityRenderer(16));
		registration.register(FAKE_ITEM_ENTITY, Collections.emptyList(), new FakeItemEntityHelper(), new FakeItemEntityRenderer(16));
	}

	@Override
	public ResourceLocation getPluginUid() {
		return TwilightForestMod.prefix("jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new JEIUncraftingCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new TransformationPowderCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CrumbleHornCategory(registration.getJeiHelpers().getGuiHelper()));
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!manager.getAllRecipesFor(RecipeType.CRAFTING).stream().filter(holder -> holder.value() instanceof MoonwormQueenRepairRecipe).toList().isEmpty()) {
			registration.addRecipeCategories(new MoonwormQueenCategory(registration.getJeiHelpers().getGuiHelper()));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!TFConfig.disableEntireTable) {
			List<RecipeHolder<? extends CraftingRecipe>> recipes = RecipeViewerConstants.getAllUncraftingRecipes(manager);
			registration.addRecipes(JEIUncraftingCategory.UNCRAFTING, (List<CraftingRecipe>) recipes.stream().map(RecipeHolder::value).toList());
		}
		registration.addRecipes(TransformationPowderCategory.TRANSFORMATION, RecipeViewerConstants.getTransformationPowderRecipes().stream().map(info -> new TransformationRecipe(new FakeEntityType(info.input()), new FakeEntityType(info.output()), info.reversible())).toList());
		registration.addRecipes(CrumbleHornCategory.CRUMBLE_HORN, RecipeViewerConstants.getCrumbleHornRecipes().stream().map(info -> new CrumbleRecipe(info.getFirst(), info.getSecond())).toList());
		registration.addRecipes(MoonwormQueenCategory.MOONWORM_QUEEN, List.of(new MoonwormQueenRepairRecipe(CraftingBookCategory.MISC)));
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(UncraftingScreen.class, 34, 33, 27, 20, JEIUncraftingCategory.UNCRAFTING);
		registration.addRecipeClickArea(UncraftingScreen.class, 115, 33, 27, 20, RecipeTypes.CRAFTING);
	}
}
