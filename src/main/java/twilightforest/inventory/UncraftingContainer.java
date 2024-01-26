package twilightforest.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFConfig;

public class UncraftingContainer implements Container {

	private final NonNullList<ItemStack> contents = NonNullList.withSize(9, ItemStack.EMPTY);

	public int numberOfInputItems;
	public int uncraftingCost;
	public int recraftingCost;
	public UncraftingMenu menu;

	public UncraftingContainer(UncraftingMenu menu) {
		this.menu = menu;
	}

	@Override
	public int getContainerSize() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.contents) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return contents.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		ItemStack stack = this.contents.get(index);
		if (stack.isEmpty()) return ItemStack.EMPTY;
		if (stack.getCount() <= amount) {
			this.contents.set(index, ItemStack.EMPTY);
			return stack;
		} else {
			return stack.split(amount);
		}
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack stack = this.contents.get(index);
		if (stack.isEmpty()) return ItemStack.EMPTY;
		this.contents.set(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.contents.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

		this.setChanged();
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get();
	}

	@Override
	public void startOpen(Player player) {
	}

	@Override
	public void stopOpen(Player player) {
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return false;
	}

	@Override
	public void clearContent() {
		this.contents.clear();
	}
}
