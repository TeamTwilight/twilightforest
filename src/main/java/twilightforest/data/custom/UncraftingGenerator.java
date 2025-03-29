package twilightforest.data.custom;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

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

		UncraftingRecipeBuilder.uncrafting(TFBlocks.KEEPSAKE_CASKET.value()).setCost(0)
			.pattern("C")
			.define('C', TFBlocks.SKULL_CHEST.value()).save(output, TwilightForestMod.prefix("relinquish_keepsakes"));
	}
}
