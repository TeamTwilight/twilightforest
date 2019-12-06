package twilightforest.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;

public class SlotTFGoblinCraftResult extends CraftingResultSlot {

	private final PlayerEntity player;
	private final IInventory inputSlot;
	private final InventoryTFGoblinUncrafting uncraftingMatrix;
	private final CraftingInventory assemblyMatrix;

	public SlotTFGoblinCraftResult(PlayerEntity player, IInventory input, IInventory uncraftingMatrix, IInventory assemblyMatrix, IInventory result, int slotIndex, int x, int y) {
		super(player, (CraftingInventory) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (InventoryTFGoblinUncrafting) uncraftingMatrix;
		this.assemblyMatrix = (CraftingInventory) assemblyMatrix;
	}

	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		// let's see, if the assembly matrix can produce this item, then it's a normal recipe, if not, it's combined.  Will that work?
		boolean combined = true;

		if (ItemStack.areItemStacksEqual(CraftingManager.findMatchingResult(this.assemblyMatrix, this.player.world), stack)) {
			combined = false;
		}

		if (combined) {
			// charge the player before the stacks empty
			if (this.uncraftingMatrix.recraftingCost > 0) {
				this.player.addExperienceLevel(-this.uncraftingMatrix.recraftingCost);
			}

			// if we are using a combined recipe, wipe the uncrafting matrix and decrement the input appropriately
			for (int i = 0; i < uncraftingMatrix.getSizeInventory(); i++) {
				this.uncraftingMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
				this.inputSlot.decrStackSize(0, this.uncraftingMatrix.numberOfInputItems);
			}
		}

		return super.onTake(player, stack);
	}
}
