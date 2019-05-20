package twilightforest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryTFGoblinInput implements IInventory {

	private ItemStack stackInput = ItemStack.EMPTY;
	private final ContainerTFUncrafting craftingContainer;

	public InventoryTFGoblinInput(ContainerTFUncrafting containerTFGoblinCrafting) {
		this.craftingContainer = containerTFGoblinCrafting;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return stackInput.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return index == 0 ? stackInput : ItemStack.EMPTY;
	}

	@Override
	public String getName() {
		return "Input";
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		if (index == 0 && !this.stackInput.isEmpty()) {
			ItemStack takenStack;

			if (this.stackInput.getCount() <= amount) {
				takenStack = this.stackInput;
				this.stackInput = ItemStack.EMPTY;
				this.craftingContainer.onCraftMatrixChanged(this);
				return takenStack;
			} else {
				takenStack = this.stackInput.splitStack(amount);
				this.craftingContainer.onCraftMatrixChanged(this);
				return takenStack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index == 0 && !this.stackInput.isEmpty()) {
			ItemStack stack = this.stackInput;
			this.stackInput = ItemStack.EMPTY;
			return stack;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			this.stackInput = stack;
			this.craftingContainer.onCraftMatrixChanged(this);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		this.craftingContainer.onCraftMatrixChanged(this);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		stackInput = ItemStack.EMPTY;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}
}
