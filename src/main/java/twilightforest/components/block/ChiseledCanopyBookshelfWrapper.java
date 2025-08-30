package twilightforest.components.block;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;

public class ChiseledCanopyBookshelfWrapper extends InvWrapper {
	public ChiseledCanopyBookshelfWrapper(ChiseledCanopyShelfBlockEntity inv) {
		super(inv);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (((ChiseledCanopyShelfBlockEntity)this.getInv()).getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) return ItemStack.EMPTY;
		return super.extractItem(slot, amount, simulate);
	}
}
