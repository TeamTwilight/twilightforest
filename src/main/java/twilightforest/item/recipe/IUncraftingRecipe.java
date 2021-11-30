package twilightforest.item.recipe;

import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("NullableProblems")
public interface IUncraftingRecipe extends CraftingRecipe {
    @Override
    default RecipeType<?> getType() {
        return TFRecipes.UNCRAFTING_RECIPE;
    }
}
