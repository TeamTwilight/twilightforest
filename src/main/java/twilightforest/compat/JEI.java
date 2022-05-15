package twilightforest.compat;

import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.UncraftingGui;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.item.recipe.TFRecipes;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEI implements IModPlugin {
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), JEIUncraftingCategory.UNCRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(UncraftingContainer.class, RecipeTypes.CRAFTING, 11, 9, 20, 36);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
        ShaderRegistry.rarityWeightMap.keySet().forEach((rarity) ->
                ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(
                        ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_" + rarity)).getDefaultInstance()
                )));
    }

    @Override
    public ResourceLocation getPluginUid() {
        return TwilightForestMod.prefix("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIUncraftingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CraftingRecipe> recipes = manager.getAllRecipesFor(RecipeType.CRAFTING);
        recipes.removeIf(recipe -> recipe.getResultItem().isEmpty() ||
                recipe.getResultItem().is(ItemTagGenerator.BANNED_UNCRAFTABLES) ||
                TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.getId().toString()) ||
                TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() != TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.getId().getNamespace()));//Prevents things that are tagged as banned from showing up
        recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
        registration.addRecipes(JEIUncraftingCategory.UNCRAFTING, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(UncraftingGui.class, 34, 33, 27, 20, JEIUncraftingCategory.UNCRAFTING);
        registration.addRecipeClickArea(UncraftingGui.class, 115, 33, 27, 20, RecipeTypes.CRAFTING);
    }
}
