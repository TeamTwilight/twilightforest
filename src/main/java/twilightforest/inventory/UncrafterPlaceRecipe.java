package twilightforest.inventory;

import com.google.common.collect.Lists;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public abstract class UncrafterPlaceRecipe implements UncraftingPlaceRecipe<Integer> {
	// Slots 0 & 1 are Uncrafting input & crafting output
	// Slots 2 to 10 are Uncrafting matrix
	// Slots 11 to 19 are Crafting matrix
	protected static final int matrixOffset = 11;

	protected final RecipeBookMenu menu;
	protected net.minecraft.world.entity.player.Inventory inventory;
	protected StackedItemContents stackedContents = new StackedItemContents();

	public UncrafterPlaceRecipe(RecipeBookMenu menu) {
		this.menu = menu;
	}

	@Override
	public void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<?> recipe, boolean placeAll) {
		if (recipe != null && player.getRecipeBook().contains(recipe.id())) {
			this.inventory = player.getInventory();
			if (this.tryClearGrid() || player.isCreative()) {
				this.stackedContents.clear();
				player.getInventory().fillStackedContents(this.stackedContents);
				this.menu.fillCraftSlotsStackedContents(this.stackedContents);
				if (this.stackedContents.canCraft(recipe.value(), null)) {
					this.handleRecipeClicked(recipe, placeAll);
				} else {
					this.clearGrid();
				}

				player.getInventory().setChanged();
			}
		}
	}

	protected void handleRecipeClicked(RecipeHolder<?> recipeHolder, boolean placeAll) {
		// Placeholder - actual implementation is handled by ServerPlaceRecipe.placeRecipe() in the menu
	}

	@Override
	public void addItemToSlot(Integer ingredient, int slot, int maxAmount, int gridY, int gridX) {

	}

	@Override
	public void placeRecipe(int width, int height, int outputSlot, RecipeHolder<?> recipe, Iterator<Integer> ingredients, int maxAmount) {
		UncraftingPlaceRecipe.super.placeRecipe(width, height, outputSlot, recipe, ingredients, maxAmount);
	}

	protected int getAmountOfFreeSlotsInInventory() {
		int i = 0;

		for (ItemStack itemstack : this.inventory.getNonEquipmentItems()) {
			if (itemstack.isEmpty()) {
				++i;
			}
		}

		return i;
	}

	protected boolean tryClearGrid() {
		List<ItemStack> list = Lists.newArrayList();
		int i = this.getAmountOfFreeSlotsInInventory();

		if (i > 0) {
			// Uncrafting input slot
			ItemStack itemstack = this.menu.getSlot(0).getItem().copy();
			if (!itemstack.isEmpty()) list.add(itemstack);
		}

		for (int slotIndex = 0; slotIndex < 3 * 3; ++slotIndex) {
			ItemStack itemstack = this.menu.getSlot(slotIndex + matrixOffset).getItem().copy();
			if (!itemstack.isEmpty()) {
				int k = this.inventory.getSlotWithRemainingSpace(itemstack);
				if (k == -1 && list.size() <= i) {
					for (ItemStack itemstack1 : list) {
						if (ItemStack.isSameItem(itemstack1, itemstack)
							&& itemstack1.getCount() != itemstack1.getMaxStackSize()
							&& itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
							itemstack1.grow(itemstack.getCount());
							itemstack.setCount(0);
							break;
						}
					}

					if (!itemstack.isEmpty()) {
						if (list.size() >= i) {
							return false;
						}

						list.add(itemstack);
					}
				} else if (k == -1) {
					return false;
				}
			}
		}

		return true;
	}

	protected void clearGrid() {
		for (int i = 0; i < 9; i++) {
			this.menu.getSlot(i + matrixOffset).set(ItemStack.EMPTY);
		}
	}
}
