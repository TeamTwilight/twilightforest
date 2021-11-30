package twilightforest.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.UncraftingGui;
import twilightforest.data.ItemTagGenerator;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.item.recipe.TFRecipes;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEI implements IModPlugin {
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), VanillaRecipeCategoryUid.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()), JEIUncraftingCategory.UNCRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(UncraftingContainer.class, VanillaRecipeCategoryUid.CRAFTING, 11, 9, 20, 36);
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
        recipes.removeIf(recipe -> recipe.getResultItem().isEmpty() | recipe.getResultItem().is(ItemTagGenerator.BANNED_UNCRAFTABLES));//Prevents things that are tagged as banned from showing up
        recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE));
        registration.addRecipes(recipes, JEIUncraftingCategory.UNCRAFTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(UncraftingGui.class, 34, 33, 27, 20, JEIUncraftingCategory.UNCRAFTING);
        registration.addRecipeClickArea(UncraftingGui.class, 115, 33, 27, 20, VanillaRecipeCategoryUid.CRAFTING);
    }
}
