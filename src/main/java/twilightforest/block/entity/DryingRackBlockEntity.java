package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.DryingRackBlock;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.DryingRecipe;

public class DryingRackBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem, WorldlyContainer {

	public static final int DEFAULT_DRYING_TIME = 20 * 60 * 5; //5 Minutes
	private ItemStack stack = ItemStack.EMPTY;
	private final RecipeManager.CachedCheck<SingleRecipeInput, DryingRecipe> quickCheck = RecipeManager.createCheck(TFRecipes.DRYING_RECIPE.get());

	protected boolean drying;
	protected int dryTime;
	protected int totalDryTime;

	public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.DRYING_RACK.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
		if (!state.getValue(DryingRackBlock.WATERLOGGED) && !level.isClientSide()) {
			if (!entity.getTheItem().isEmpty()) {
				SingleRecipeInput input = new SingleRecipeInput(entity.getTheItem());
				RecipeHolder<DryingRecipe> recipeholder = entity.quickCheck.getRecipeFor(input, level).orElse(null);

				entity.updateDryingTime(recipeholder != null);
				if (recipeholder != null) {
					entity.dryTime++;

					if (entity.dryTime >= entity.totalDryTime) {
						entity.setTheItem(recipeholder.value().assemble(input, level.registryAccess()));
						setChanged(level, pos, state);
					}
				}
			} else {
				entity.updateDryingTime(false);
			}
		}
	}

	private void updateDryingTime(boolean drying) {
		boolean wasDrying = this.drying;
		this.drying = drying;
		if (wasDrying != drying) {
			this.setChanged();
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
		}
	}

	@Override
	public BlockEntity getContainerBlockEntity() {
		return this;
	}

	@Override
	public ItemStack getTheItem() {
		return this.stack;
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	@Override
	public void setTheItem(ItemStack newItem) {
		boolean flag = !newItem.isEmpty() && ItemStack.isSameItemSameComponents(this.stack, newItem);
		this.stack = newItem;
		newItem.limitSize(this.getMaxStackSize(newItem));
		if (!flag) {
			this.totalDryTime = getDryingTime(this.level, this);
			this.dryTime = 0;
			this.setChanged();
		}
	}

	private static int getDryingTime(Level level, DryingRackBlockEntity entity) {
		SingleRecipeInput singlerecipeinput = new SingleRecipeInput(entity.getTheItem());
		return entity.quickCheck.getRecipeFor(singlerecipeinput, level).map(holder -> holder.value().getDryingTime()).orElse(DEFAULT_DRYING_TIME);
	}

	public boolean isDrying() {
		return this.drying;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.stack.isEmpty()) {
			tag.put("item", this.stack.save(registries));
		}
		tag.putInt("dry_time", this.dryTime);
		tag.putInt("total_dry_time", this.totalDryTime);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("item", Tag.TAG_COMPOUND)) {
			this.stack = ItemStack.parse(registries, tag.getCompound("item")).orElse(ItemStack.EMPTY);
		} else {
			this.stack = ItemStack.EMPTY;
		}
		this.dryTime = tag.getInt("dry_time");
		this.totalDryTime = tag.getInt("total_dry_time");

		if (tag.contains("drying")) {
			this.drying = tag.getBoolean("drying");
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		tag.putBoolean("drying", this.drying);

		if (!this.stack.isEmpty()) {
			tag.put("item", this.stack.save(registries));
		}

		return tag;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[]{0};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
		return this.getTheItem().isEmpty();
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return !this.drying;
	}
}
