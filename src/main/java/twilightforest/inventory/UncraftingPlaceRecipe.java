package twilightforest.inventory;

import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Iterator;

//modified version of PlaceRecipe that uses the correct slots for the uncrafting table
public interface UncraftingPlaceRecipe<C> extends PlaceRecipeHelper {

	// Slots 0 & 1 are Uncrafting input & crafting output
	// Slots 2 to 10 are Uncrafting matrix
	// Slots 11 to 19 are Crafting matrix
	int matrixOffset = 11;

	void addItemToSlot(C ingredient, int slotIndex, int gridY, int gridX);

	default void placeRecipe(int width, int height, Recipe<?> recipe, Iterable<C> entries, Output<?> output) {
		int recipeWidth = width;
		int recipeHeight = height;
		Iterator<C> ingredients = entries.iterator();

		if (recipe instanceof ShapedRecipe shapedRecipe) {
			recipeWidth = shapedRecipe.getWidth();
			recipeHeight = shapedRecipe.getHeight();
		}

		int slotIndex = matrixOffset;

		for (int gridY = 0; gridY < height; ++gridY) {
			boolean yOverfitted = (float) recipeHeight < (float) height / 2.0F;
			int rad = Mth.floor((float) height / 2.0F - (float) recipeHeight / 2.0F);
			if (yOverfitted && rad > gridY) {
				slotIndex += width;
				++gridY;
			}

			for (int gridX = 0; gridX < width; ++gridX) {
				if (!ingredients.hasNext()) {
					return;
				}

				yOverfitted = (float) recipeWidth < (float) width / 2.0F;
				rad = Mth.floor((float) width / 2.0F - (float) recipeWidth / 2.0F);
				int o = recipeWidth;
				boolean xOverfitted = gridX < recipeWidth;
				if (yOverfitted) {
					o = rad + recipeWidth;
					xOverfitted = rad <= gridX && gridX < rad + recipeWidth;
				}

				if (xOverfitted) {
					this.addItemToSlot(ingredients.next(), slotIndex, gridY, gridX);
				} else if (o == gridX) {
					slotIndex += width - gridX;
					break;
				}

				++slotIndex;
			}
		}
	}
}
