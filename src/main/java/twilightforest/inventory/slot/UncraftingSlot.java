package twilightforest.inventory.slot;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.recipe.UncraftingRecipe;

public class UncraftingSlot extends Slot {

	protected final Player player;
	protected final Container inputSlot;
	protected final UncraftingContainer uncraftingMatrix;
	protected final Container assemblyMatrix;

	public UncraftingSlot(Player player, Container inputSlot, UncraftingContainer uncraftingMatrix, Container assemblyMatrix, int slotNum, int x, int y) {
		super(uncraftingMatrix, slotNum, x, y);
		this.player = player;
		this.inputSlot = inputSlot;
		this.uncraftingMatrix = uncraftingMatrix;
		this.assemblyMatrix = assemblyMatrix;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean mayPlace(ItemStack stack) {
		// don't put things in this matrix
		return false;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean mayPickup(Player player) {
		// if there is anything in the assembly matrix, then you cannot have these items
		if (!this.assemblyMatrix.isEmpty()) {
			return false;
		}

		// can't take a marked stack
		if (UncraftingMenu.isMarked(this.getItem())) {
			return false;
		}

		// if uncrafting is disabled, no!
		if (TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.get() && !(this.uncraftingMatrix.menu.storedGhostRecipe instanceof UncraftingRecipe)) {
			return false;
		}

		// if you don't have enough XP, no
		return this.uncraftingMatrix.uncraftingCost <= player.experienceLevel || player.getAbilities().instabuild;
	}

	/**
	 * Called when the player picks up an item from an inventory slot
	 */
	@Override
	public void onTake(Player player, ItemStack stack) {
		// charge the player for this
		if (this.uncraftingMatrix.uncraftingCost > 0) {
			this.player.giveExperienceLevels(-this.uncraftingMatrix.uncraftingCost);
		}

		// move all remaining items from the uncrafting grid to the assembly grid
		// the assembly grid should be empty for this to even happen, so just plop the items right in
		for (int i = 0; i < 9; i++) {
			ItemStack transferStack = this.uncraftingMatrix.getItem(i);
			if (!transferStack.isEmpty() && !UncraftingMenu.isMarked(transferStack)) {
				this.assemblyMatrix.setItem(i, transferStack.copy());
			}
		}

		// decrement the inputslot by 1
		// do this second so that it doesn't change the contents of the uncrafting grid
		ItemStack inputStack = this.inputSlot.getItem(0);
		if (!inputStack.isEmpty()) {
			if (player instanceof ServerPlayer server) {
				TFAdvancements.UNCRAFT_ITEM.get().trigger(server, inputStack);
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		super.onTake(player, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isActive() {
		return false;
	}
}
