package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBlockEntities;

import java.util.Optional;

public class TFChestBlockEntity extends ChestBlockEntity {

	private final ChestItemHandler itemHandler = new ChestItemHandler();

	public TFChestBlockEntity(BlockPos pos, BlockState state) {
		this(TFBlockEntities.TF_CHEST.get(), pos, state);
	}

	protected TFChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		itemHandler.bindToChest();
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		super.setItems(items);
		itemHandler.bindToChest();
	}

	private class ChestItemHandler extends ItemStacksResourceHandler {

		private ChestItemHandler() {
			super(getContainerSize());
			bindToChest();
		}

		private void bindToChest() {
			stacks = getItems();
		}

		@Override
		public ItemResource getResource(int index) {
			unpackLootTable(null);
			return super.getResource(index);
		}

		@Override
		public long getAmountAsLong(int index) {
			unpackLootTable(null);
			return super.getAmountAsLong(index);
		}

		@Override
		public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
			unpackLootTable(null);
			return super.insert(index, resource, amount, transaction);
		}

		@Override
		public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
			unpackLootTable(null);
			return super.extract(index, resource, amount, transaction);
		}

		@Override
		protected void onContentsChanged(int index, ItemStack previousContents) {
			setChanged();
			BlockState state = getBlockState();
			// instanceof here is just a glorified null check
			if (state.getValue(ChestBlock.TYPE) == ChestType.SINGLE || !(getLevel() instanceof Level chestLevel))
				return;
			BlockPos otherHalfPos = getBlockPos().relative(ChestBlock.getConnectedDirection(state));
			if (chestLevel.getBlockEntity(otherHalfPos) instanceof ChestBlockEntity otherHalf)
				otherHalf.setChanged();
		}
	}

	public static class ItemHandlerProvider implements IBlockCapabilityProvider<ResourceHandler<ItemResource>, @Nullable Direction>, DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<ResourceHandler<ItemResource>>> {

		@Override
		@Nullable
		public ResourceHandler<ItemResource> getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction context) {
			return state.getBlock() instanceof ChestBlock chest ? chest.combine(state, level, pos, true).apply(this).orElse(null) : null;
		}

		@Override
		public Optional<ResourceHandler<ItemResource>> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
			if (first instanceof TFChestBlockEntity tfFirst && second instanceof TFChestBlockEntity tfSecond)
				return Optional.of(new CombinedResourceHandler<>(tfFirst.itemHandler, tfSecond.itemHandler));
			return Optional.empty();
		}

		@Override
		public Optional<ResourceHandler<ItemResource>> acceptSingle(ChestBlockEntity single) {
			return single instanceof TFChestBlockEntity tfSingle ? Optional.of(tfSingle.itemHandler) : Optional.empty();
		}

		@Override
		public Optional<ResourceHandler<ItemResource>> acceptNone() {
			return Optional.empty();
		}
	}
}
