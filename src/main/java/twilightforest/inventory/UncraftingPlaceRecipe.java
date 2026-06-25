package twilightforest.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

//modified version of PlaceRecipe that uses the correct slots for the uncrafting table
public interface UncraftingPlaceRecipe<C> {

	// Slots 0 & 1 are Uncrafting input & crafting output
	// Slots 2 to 10 are Uncrafting matrix
	// Slots 11 to 19 are Crafting matrix
	int matrixOffset = 11;

	void setupGhostRecipe(RecipeHolder<?> recipe, List<Slot> slots);

	void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<?> recipe, boolean placeAll);

    void addItemToSlot(C ingredient, int slot, int maxAmount, int gridY, int gridX);

	default void placeRecipe(int width, int height, int outputSlot, RecipeHolder<?> recipe, Iterator<C> ingredients, int maxAmount) {
		int widthModified = width;
		int heightModified = height;
		if (recipe.value() instanceof ShapedRecipe shapedRecipe) {
			widthModified = shapedRecipe.getWidth();
			heightModified = shapedRecipe.getHeight();
		}

		int slotIndex = matrixOffset;

		for (int gridY = 0; gridY < height; ++gridY) {
			boolean yOverfitted = (float) heightModified < (float) height / 2.0F;
			int rad = Mth.floor((float) height / 2.0F - (float) heightModified / 2.0F);
			if (yOverfitted && rad > gridY) {
				slotIndex += width;
				++gridY;
			}

			for (int gridX = 0; gridX < width; ++gridX) {
				if (!ingredients.hasNext()) {
					return;
				}

				yOverfitted = (float) widthModified < (float) width / 2.0F;
				rad = Mth.floor((float) width / 2.0F - (float) widthModified / 2.0F);
				int o = widthModified;
				boolean xOverfitted = gridX < widthModified;
				if (yOverfitted) {
					o = rad + widthModified;
					xOverfitted = rad <= gridX && gridX < rad + widthModified;
				}

				if (xOverfitted) {
					this.addItemToSlot(ingredients.next(), slotIndex, maxAmount, gridY, gridX);
				} else if (o == gridX) {
					slotIndex += width - gridX;
					break;
				}

				++slotIndex;
			}
		}
	}
}
