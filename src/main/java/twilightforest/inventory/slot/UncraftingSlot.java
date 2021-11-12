package twilightforest.inventory.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingInventory;

public class UncraftingSlot extends Slot {

	protected final Player player;
	protected final Container inputSlot;
	protected final UncraftingInventory uncraftingMatrix;
	protected final Container assemblyMatrix;

	public UncraftingSlot(Player player, Container inputSlot, UncraftingInventory uncraftingMatrix, Container assemblyMatrix, int slotNum, int x, int y) {
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
		if (UncraftingContainer.isMarked(this.getItem())) {
			return false;
		}

		// if uncrafting is disabled, no!
		if (TFConfig.COMMON_CONFIG.disableUncrafting.get()) {
			return false;
		}

		// if you don't have enough XP, no
		if (this.uncraftingMatrix.uncraftingCost > player.experienceLevel && !player.getAbilities().instabuild) {
			return false;
		}

		return true;
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
			if (!transferStack.isEmpty() && !UncraftingContainer.isMarked(transferStack)) {
				this.assemblyMatrix.setItem(i, transferStack.copy());
			}
		}

		// decrement the inputslot by 1
		// do this second so that it doesn't change the contents of the uncrafting grid
		ItemStack inputStack = this.inputSlot.getItem(0);
		if (!inputStack.isEmpty()) {
			this.inputSlot.removeItem(0, uncraftingMatrix.numberOfInputItems);
		}

		super.onTake(player, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isActive() {
		return false;
	}
}
