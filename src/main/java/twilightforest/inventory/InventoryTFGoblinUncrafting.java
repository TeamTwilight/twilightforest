package twilightforest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import twilightforest.TFConfig;

public class InventoryTFGoblinUncrafting implements IInventory {

	private final NonNullList<ItemStack> contents = NonNullList.withSize(9, ItemStack.EMPTY);

	public int numberOfInputItems;
	public int uncraftingCost;
	public int recraftingCost;

	public InventoryTFGoblinUncrafting(ContainerTFUncrafting container) {}

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : contents) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return contents.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		ItemStack stack = this.contents.get(index);
		if (stack.isEmpty()) return ItemStack.EMPTY;
		if (stack.getCount() <= amount) {
			this.contents.set(index, ItemStack.EMPTY);
			//this.eventHandler.onCraftMatrixChanged(this);
			return stack;
		} else {
			return stack.splitStack(amount);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.contents.get(index);
		if (stack.isEmpty()) return ItemStack.EMPTY;
		this.contents.set(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.contents.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public String getName() {
		return "twilightforest.goblincrafting";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !TFConfig.disableUncrafting;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
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
		contents.clear();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

}
