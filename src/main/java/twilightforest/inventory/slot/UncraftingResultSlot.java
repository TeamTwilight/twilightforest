package twilightforest.inventory.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.inventory.UncraftingInventory;

public class UncraftingResultSlot extends ResultSlot {

	private final Player player;
	private final Container inputSlot;
	private final UncraftingInventory uncraftingMatrix;
	private final CraftingContainer assemblyMatrix;

	public UncraftingResultSlot(Player player, Container input, Container uncraftingMatrix, Container assemblyMatrix, Container result, int slotIndex, int x, int y) {
		super(player, (CraftingContainer) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (UncraftingInventory) uncraftingMatrix;
		this.assemblyMatrix = (CraftingContainer) assemblyMatrix;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		// let's see, if the assembly matrix can produce this item, then it's a normal recipe, if not, it's combined.  Will that work?
		boolean combined = true;

		for (Recipe<CraftingContainer> recipe : player.level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, this.assemblyMatrix, this.player.level)) {
			if (ItemStack.isSameItemSameTags(recipe.getResultItem(), stack)) {
				combined = false;
				break;
			}
		}

		if (combined) {
			// charge the player before the stacks empty
			if (this.uncraftingMatrix.recraftingCost > 0) {
				this.player.giveExperienceLevels(-this.uncraftingMatrix.recraftingCost);
			}

			// if we are using a combined recipe, wipe the uncrafting matrix and decrement the input appropriately
			for (int i = 0; i < uncraftingMatrix.getContainerSize(); i++) {
				this.uncraftingMatrix.setItem(i, ItemStack.EMPTY);
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		this.checkTakeAchievements(stack);
		for (int i = 0; i < assemblyMatrix.getContainerSize(); i++) {
			assemblyMatrix.removeItem(i, 1);
		}
	}
}
