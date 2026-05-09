package twilightforest.components.block;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;

public class ChiseledCanopyBookshelfWrapper implements ResourceHandler<ItemResource> {
	private final ChiseledCanopyShelfBlockEntity container;
	private final ResourceHandler<ItemResource> wrapped;

	public ChiseledCanopyBookshelfWrapper(ChiseledCanopyShelfBlockEntity container) {
		this.container = container;
		this.wrapped = VanillaContainerWrapper.of(container);
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	public @NonNull ItemResource getResource(int index) {
		return wrapped.getResource(index);
	}

	@Override
	public long getAmountAsLong(int index) {
		return wrapped.getAmountAsLong(index);
	}

	@Override
	public long getCapacityAsLong(int index, @NonNull ItemResource resource) {
		return wrapped.getCapacityAsLong(index, resource);
	}

	@Override
	public boolean isValid(int index, @NonNull ItemResource resource) {
		return wrapped.isValid(index, resource);
	}

	@Override
	public int insert(int index, @NonNull ItemResource resource, int amount, @NonNull TransactionContext transaction) {
		return wrapped.insert(index, resource, amount, transaction);
	}

	@Override
	public int extract(int index, @NonNull ItemResource resource, int amount, @NonNull TransactionContext transaction) {
		if(container.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) return 0;
		return wrapped.extract(index, resource, amount, transaction);
	}
}
