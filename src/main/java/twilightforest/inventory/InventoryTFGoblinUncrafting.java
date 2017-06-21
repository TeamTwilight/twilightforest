package twilightforest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import twilightforest.TFConfig;

import java.util.Arrays;


public class InventoryTFGoblinUncrafting implements IInventory {

	private ItemStack[] contents = new ItemStack[9];
	public int numberOfInputItems;
	public int uncraftingCost;
	public int recraftingCost;


	public InventoryTFGoblinUncrafting(ContainerTFUncrafting containerTFGoblinCrafting) {
		Arrays.fill(contents, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack s : contents) {
			if (!s.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return contents[var1];
	}

	@Override
	public ItemStack decrStackSize(int slotNum, int amount) {
		if (!this.contents[slotNum].isEmpty()) {
			ItemStack takenStack;

			if (this.contents[slotNum].getCount() <= amount) {
				takenStack = this.contents[slotNum];
				this.contents[slotNum] = ItemStack.EMPTY;

				//this.eventHandler.onCraftMatrixChanged(this);
				return takenStack;
			} else {
				takenStack = this.contents[slotNum].splitStack(amount);
				return takenStack;
			}
		} else {
			return ItemStack.EMPTY;
		}

	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if (!this.contents[par1].isEmpty()) {
			ItemStack var2 = this.contents[par1];
			this.contents[par1] = ItemStack.EMPTY;
			return var2;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.contents[par1] = par2ItemStack;

		if (!par2ItemStack.isEmpty() && par2ItemStack.getCount() > this.getInventoryStackLimit()) {
			par2ItemStack.setCount(this.getInventoryStackLimit());
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
	public boolean isUsableByPlayer(EntityPlayer var1) {
		return !TFConfig.disableUncrafting;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
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
		Arrays.fill(contents, ItemStack.EMPTY);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

}
