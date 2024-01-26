package twilightforest.data.custom;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class UncraftingGenerator {

	public static void buildRecipes(RecipeOutput output) {
		UncraftingRecipeBuilder.uncrafting(Items.TIPPED_ARROW, 8).setCost(4)
				.pattern("AAA")
				.pattern("A A")
				.pattern("AAA")
				.define('A', Ingredient.of(Items.ARROW)).save(output);

		UncraftingRecipeBuilder.uncrafting(Items.WRITTEN_BOOK).setCost(0)
				.pattern("B")
				.define('B', Items.BOOK).save(output);
	}
}
