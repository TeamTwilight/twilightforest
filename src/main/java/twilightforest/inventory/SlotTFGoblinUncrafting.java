package twilightforest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;


public class SlotTFGoblinUncrafting extends Slot {
	protected EntityPlayer thePlayer;
	protected IInventory inputSlot;
	protected InventoryTFGoblinUncrafting uncraftingMatrix;
	protected IInventory assemblyMatrix;

	public SlotTFGoblinUncrafting(EntityPlayer par1EntityPlayer, IInventory inputSlot, InventoryTFGoblinUncrafting uncraftingMatrix, IInventory assemblyMatrix, int slotNum, int x, int y) {
		super(uncraftingMatrix, slotNum, x, y);
		this.thePlayer = par1EntityPlayer;
		this.inputSlot = inputSlot;
		this.uncraftingMatrix = uncraftingMatrix;
		this.assemblyMatrix = assemblyMatrix;
	}


	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		// don't put things in this matrix
		return false;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		// if there is anything in the assembly matrix, then you cannot have these items
		for (int i = 0; i < this.assemblyMatrix.getSizeInventory(); i++) {
			if (!this.assemblyMatrix.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		// if uncrafting is disabled, no!
		if (TFConfig.disableUncrafting) {
			return false;
		}

		// if you don't have enough XP, no
		if (this.uncraftingMatrix.uncraftingCost > par1EntityPlayer.experienceLevel && !par1EntityPlayer.capabilities.isCreativeMode) {
			return false;
		}

		return true;
	}

	/**
	 * Called when the player picks up an item from an inventory slot
	 */
	@Override
	public ItemStack onTake(EntityPlayer par1EntityPlayer, ItemStack par1ItemStack) {
		// charge the player for this
		if (this.uncraftingMatrix.uncraftingCost > 0) {
			this.thePlayer.addExperienceLevel(-this.uncraftingMatrix.uncraftingCost);
		}

		// move all remaining items from the uncrafting grid to the assembly grid
		// the assembly grid should be empty for this to even happen, so just plop the items right in
		for (int i = 0; i < 9; i++) {
			ItemStack transferStack = this.uncraftingMatrix.getStackInSlot(i);
			if (!transferStack.isEmpty() && transferStack.getCount() > 0) {
				this.assemblyMatrix.setInventorySlotContents(i, transferStack.copy());
			}
		}

		// decrement the inputslot by 1
		// do this second so that it doesn't change the contents of the uncrafting grid
		ItemStack inputStack = this.inputSlot.getStackInSlot(0);
		if (!inputStack.isEmpty()) {
			this.inputSlot.decrStackSize(0, uncraftingMatrix.numberOfInputItems);
		}


		return super.onTake(par1EntityPlayer, par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean isHere(IInventory inv, int slotIn) {
		return false;
	}
}
