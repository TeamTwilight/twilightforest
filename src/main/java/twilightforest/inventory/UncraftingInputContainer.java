package twilightforest.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class UncraftingInputContainer implements Container {

	private ItemStack stack = ItemStack.EMPTY;
	private final UncraftingMenu container;

	public UncraftingInputContainer(UncraftingMenu containerTFGoblinCrafting) {
		this.container = containerTFGoblinCrafting;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return index == 0 ? this.stack : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		if (index == 0 && !this.stack.isEmpty()) {
			ItemStack takenStack;

			if (this.stack.getCount() <= amount) {
				takenStack = this.stack;
				this.stack = ItemStack.EMPTY;
				this.container.slotsChanged(this);
				return takenStack;
			} else {
				takenStack = this.stack.split(amount);
				this.container.slotsChanged(this);
				return takenStack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		if (index == 0 && !this.stack.isEmpty()) {
			ItemStack stack = this.stack;
			this.stack = ItemStack.EMPTY;
			return stack;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
			this.container.slotsChanged(this);
		}
	}

	@Override
	public void setChanged() {
		this.container.slotsChanged(this);
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		this.stack = ItemStack.EMPTY;
	}
}
