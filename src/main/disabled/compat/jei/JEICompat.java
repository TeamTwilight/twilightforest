package twilightforest.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.fml.ModList;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.categories.*;
import twilightforest.compat.jei.extension.*;
import twilightforest.compat.jei.renderers.EntityHelper;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.renderers.FakeItemEntityHelper;
import twilightforest.compat.jei.renderers.FakeItemEntityRenderer;
import twilightforest.compat.jei.subtype.CasketSubtypeInterpreter;
import twilightforest.compat.jei.util.CrumbleRecipe;
import twilightforest.compat.jei.util.GrindstoneTravellersRecipesGetter;
import twilightforest.compat.jei.util.OminousFireRecipe;
import twilightforest.compat.jei.util.TransformationRecipe;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFBlockTags;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFMenuTypes;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

	public static final IIngredientType<FakeEntityType> ENTITY_TYPE = () -> FakeEntityType.class;
	public static final IIngredientType<FakeItemEntity> FAKE_ITEM_ENTITY = () -> FakeItemEntity.class;


	public static boolean isEmiInstalled() {
		//Skip handling if both EMI and JEI are loaded as otherwise some things behave strangely
		return ModList.get().isLoaded("emi");
	}

	@Override
	public Identifier getPluginUid() {
		return Identifier.fromNamespaceAndPath(TwilightForestMod.ID, "jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if (isEmiInstalled()) return;
		if (!TFConfig.disableEntireTable) {
			registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), JEIUncraftingCategory.UNCRAFTING);
		}
		registration.addRecipeCatalyst(new ItemStack(TFItems.TRANSFORMATION_POWDER.get()), TransformationPowderCategory.TRANSFORMATION);
		registration.addRecipeCatalyst(new ItemStack(TFItems.EXANIMATE_ESSENCE.get()), OminousFireCategory.OMINOUS_FIRE);
		registration.addRecipeCatalyst(new ItemStack(TFItems.CRUMBLE_HORN.get()), CrumbleHornCategory.CRUMBLE_HORN);

		for (var block : BuiltInRegistries.BLOCK.getTagOrEmpty(TFBlockTags.DRYING_RACKS)) {
			registration.addRecipeCatalyst(new ItemStack(block.value()), DryingCategory.DRYING);
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		if (isEmiInstalled()) return;
		registration.registerSubtypeInterpreter(TFItems.KEEPSAKE_CASKET.asItem(), CasketSubtypeInterpreter.INSTANCE);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		if (isEmiInstalled()) return;
		registration.addRecipeTransferHandler(UncraftingMenu.class, TFMenuTypes.UNCRAFTING.get(), RecipeTypes.CRAFTING, 11, 9, 20, 36);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		if (isEmiInstalled()) return;
		registration.register(ENTITY_TYPE, Collections.emptyList(), new EntityHelper(), new EntityRenderer(16), FakeEntityType.CODEC);
		registration.register(FAKE_ITEM_ENTITY, Collections.emptyList(), new FakeItemEntityHelper(), new FakeItemEntityRenderer(16), FakeItemEntity.CODEC);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		if (isEmiInstalled()) return;
		registration.addRecipeCategories(new JEIUncraftingCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new TransformationPowderCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new OminousFireCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CrumbleHornCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new DryingCategory(registration.getJeiHelpers().getGuiHelper()));
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		if (isEmiInstalled()) return;
		registration.getSmithingCategory().addExtension(NoTemplateSmithingRecipe.class, new NoTemplateSmithingExtension());
		registration.getCraftingCategory().addExtension(ScepterRepairRecipe.class, new ScepterRepairExtension());
		registration.getCraftingCategory().addExtension(TravellersGearModifierRecipe.class, new TravellersGearModifierExtension());
		registration.getCraftingCategory().addExtension(MoonwormQueenRepairRecipe.class, new MoonwormQueenExtension());
		registration.getCraftingCategory().addExtension(EssenceRepairRecipe.class, new ExanimateEssenceRepairExtension());
		registration.getCraftingCategory().addExtension(CasketRepairRecipe.class, new CasketRepairExtension());
		registration.getCraftingCategory().addExtension(TravellersVestGlovesMergeRecipe.class, new TravellersVestGlovesMergeExtension());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerRecipes(IRecipeRegistration registration) {
		if (isEmiInstalled()) return;
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!TFConfig.disableEntireTable) {
			List<RecipeHolder<? extends CraftingRecipe>> recipes = RecipeViewerConstants.getAllUncraftingRecipes(manager);
			registration.addRecipes(JEIUncraftingCategory.UNCRAFTING, (List<CraftingRecipe>) recipes.stream().map(RecipeHolder::value).toList());
		}
		registration.addRecipes(TransformationPowderCategory.TRANSFORMATION, RecipeViewerConstants.getTransformationPowderRecipes().stream().map(info -> new TransformationRecipe(new FakeEntityType(info.input()), new FakeEntityType(info.output()), info.reversible())).toList());
		registration.addRecipes(OminousFireCategory.OMINOUS_FIRE, RecipeViewerConstants.getOminousFireRecipes().stream().map(info -> new OminousFireRecipe(new FakeEntityType(info.input()), new FakeEntityType(info.output()))).toList());
		registration.addRecipes(CrumbleHornCategory.CRUMBLE_HORN, RecipeViewerConstants.getCrumbleHornRecipes().stream().map(info -> new CrumbleRecipe(info.getFirst(), info.getSecond())).toList());
		registration.addRecipes(DryingCategory.DRYING, manager.getAllRecipesFor(TFRecipes.DRYING_RECIPE.get()).stream().filter(holder -> !holder.value().getResult().is(TFItems.STALE_BREAD)).map(RecipeHolder::value).toList());
		registration.addRecipes(RecipeTypes.GRINDSTONE, GrindstoneTravellersRecipesGetter.getRecipes());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if (isEmiInstalled()) return;
		registration.addRecipeClickArea(UncraftingScreen.class, 34, 33, 27, 20, JEIUncraftingCategory.UNCRAFTING);
		registration.addRecipeClickArea(UncraftingScreen.class, 115, 33, 27, 20, RecipeTypes.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(TFItems.MAGIC_PAINTING.toStack()));
	}
}
