package twilightforest.inventory.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingMenu;

import java.util.HashMap;
import java.util.Map;

public class UncraftingResultSlot extends ResultSlot {

	private final Player player;
	private final Container inputSlot;
	private final UncraftingContainer uncraftingMatrix;
	private final CraftingContainer assemblyMatrix;
	private final Map<Integer, ItemStack> tempRemainderMap;

	public UncraftingResultSlot(Player player, Container input, Container uncraftingMatrix, Container assemblyMatrix, Container result, int slotIndex, int x, int y) {
		super(player, (CraftingContainer) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (UncraftingContainer) uncraftingMatrix;
		this.assemblyMatrix = (CraftingContainer) assemblyMatrix;
		this.tempRemainderMap = new HashMap<>();
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		// let's see, if the assembly matrix can produce this item, then it's a normal recipe, if not, it's combined.  Will that work?
		boolean combined = true;

		//clear the temp map, just in case
		this.tempRemainderMap.clear();

		for (RecipeHolder<CraftingRecipe> recipe : player.level().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, this.assemblyMatrix, this.player.level())) {
			if (ItemStack.isSameItemSameTags(recipe.value().getResultItem(player.level().registryAccess()), stack)) {
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
			for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
				if (this.assemblyMatrix.getItem(i).isEmpty()) {
					if (!UncraftingMenu.isMarked(this.uncraftingMatrix.getItem(i))) {
						this.uncraftingMatrix.setItem(i, ItemStack.EMPTY);
					} else {
						//if we have an ingredient in the grid and one in the uncrafting matrix, copy the uncrafting matrix item for later
						this.tempRemainderMap.put(i, this.uncraftingMatrix.getItem(i));
					}
				}
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		//VanillaCopy of the super method, but altered to work with the assembly matrix
		this.checkTakeAchievements(stack);

		CommonHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> remainingItems = player.level().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.assemblyMatrix, player.level());
		CommonHooks.setCraftingPlayer(null);

		for(int i = 0; i < remainingItems.size(); ++i) {
			ItemStack currentStack = this.assemblyMatrix.getItem(i);
			ItemStack remainingStack = remainingItems.get(i);
			if (!currentStack.isEmpty()) {
				this.assemblyMatrix.removeItem(i, 1);
				currentStack = this.assemblyMatrix.getItem(i);
			}

			if (!remainingStack.isEmpty()) {
				if (currentStack.isEmpty()) {
					this.assemblyMatrix.setItem(i, remainingStack);
				} else if (!ItemStack.isSameItemSameTags(currentStack, remainingStack) && !this.player.getInventory().add(remainingStack)) {
					this.player.drop(remainingStack, false);
				}
			}
		}
		//add all remainders to the crafting grid. This prevents any extra items from being deleted during the recrafting process.
		if (!this.tempRemainderMap.isEmpty()) {
			this.tempRemainderMap.forEach(this.assemblyMatrix::setItem);
		}
	}
}
