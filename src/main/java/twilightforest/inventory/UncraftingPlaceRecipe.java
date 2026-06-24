package twilightforest.inventory;

import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Iterator;

//modified version of PlaceRecipe that uses the correct slots for the uncrafting table
public interface UncraftingPlaceRecipe<C> {

	int matrixOffset = 11;

	void addItemToSlot(C ingredient, int slotIndex, int maxAmount, int gridY, int gridX);

	default void placeRecipe(int gridWidth, int gridHeight, int outputSlot, Recipe<?> recipe, Iterator<C> ingredients, int maxAmount) {
		int recipeWidth = gridWidth;
		int recipeHeight = gridHeight;

		if (recipe instanceof ShapedRecipe shapedRecipe) {
			recipeWidth = shapedRecipe.getWidth();
			recipeHeight = shapedRecipe.getHeight();
		}

		int slotIndex = matrixOffset;

		for (int gridY = 0; gridY < gridHeight; ++gridY) {
			boolean yOverfitted = (float) recipeHeight < (float) gridHeight / 2.0F;
			int rad = Mth.floor((float) gridHeight / 2.0F - (float) recipeHeight / 2.0F);
			if (yOverfitted && rad > gridY) {
				slotIndex += gridWidth;
				++gridY;
			}

			for (int gridX = 0; gridX < gridWidth; ++gridX) {
				if (!ingredients.hasNext()) {
					return;
				}

				yOverfitted = (float) recipeWidth < (float) gridWidth / 2.0F;
				rad = Mth.floor((float) gridWidth / 2.0F - (float) recipeWidth / 2.0F);
				int o = recipeWidth;
				boolean xOverfitted = gridX < recipeWidth;
				if (yOverfitted) {
					o = rad + recipeWidth;
					xOverfitted = rad <= gridX && gridX < rad + recipeWidth;
				}

				if (xOverfitted) {
					this.addItemToSlot(ingredients.next(), slotIndex, maxAmount, gridY, gridX);
				} else if (o == gridX) {
					slotIndex += gridWidth - gridX;
					break;
				}

				++slotIndex;
			}
		}
	}
}
