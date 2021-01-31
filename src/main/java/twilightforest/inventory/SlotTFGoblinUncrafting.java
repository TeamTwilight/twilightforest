package twilightforest.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;

public class SlotTFGoblinUncrafting extends Slot {

	protected final PlayerEntity player;
	protected final IInventory inputSlot;
	protected final InventoryTFGoblinUncrafting uncraftingMatrix;
	protected final IInventory assemblyMatrix;

	public SlotTFGoblinUncrafting(PlayerEntity player, IInventory inputSlot, InventoryTFGoblinUncrafting uncraftingMatrix, IInventory assemblyMatrix, int slotNum, int x, int y) {
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
	public boolean isItemValid(ItemStack stack) {
		// don't put things in this matrix
		return false;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(PlayerEntity player) {
		// if there is anything in the assembly matrix, then you cannot have these items
		if (!this.assemblyMatrix.isEmpty()) {
			return false;
		}

		// can't take a marked stack
		if (ContainerTFUncrafting.isMarked(this.getStack())) {
			return false;
		}

		// if uncrafting is disabled, no!
		if (TFConfig.COMMON_CONFIG.disableUncrafting.get()) {
			return false;
		}

		// if you don't have enough XP, no
		if (this.uncraftingMatrix.uncraftingCost > player.experienceLevel && !player.abilities.isCreativeMode) {
			return false;
		}

		return true;
	}

	/**
	 * Called when the player picks up an item from an inventory slot
	 */
	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		// charge the player for this
		if (this.uncraftingMatrix.uncraftingCost > 0) {
			this.player.addExperienceLevel(-this.uncraftingMatrix.uncraftingCost);
		}

		// move all remaining items from the uncrafting grid to the assembly grid
		// the assembly grid should be empty for this to even happen, so just plop the items right in
		for (int i = 0; i < 9; i++) {
			ItemStack transferStack = this.uncraftingMatrix.getStackInSlot(i);
			if (!transferStack.isEmpty() && !ContainerTFUncrafting.isMarked(transferStack)) {
				this.assemblyMatrix.setInventorySlotContents(i, transferStack.copy());
			}
		}

		// decrement the inputslot by 1
		// do this second so that it doesn't change the contents of the uncrafting grid
		ItemStack inputStack = this.inputSlot.getStackInSlot(0);
		if (!inputStack.isEmpty()) {
			this.inputSlot.decrStackSize(0, uncraftingMatrix.numberOfInputItems);
		}

		return super.onTake(player, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isEnabled() {
		return false;
	}
}
