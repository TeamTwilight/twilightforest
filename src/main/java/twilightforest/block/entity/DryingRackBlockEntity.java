package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.block.DryingRackBlock;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.DryingRecipe;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity {

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
				boolean recipeHolderExists = recipeholder != null;
				entity.updateDryingTime(recipeHolderExists);
				if (recipeHolderExists) {
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

	public ItemStack getTheItem() {
		return this.stack;
	}

	public void setTheItem(ItemStack newItem) {
		boolean updateDryTime = newItem.isEmpty() || !ItemStack.isSameItemSameComponents(this.stack, newItem);
		this.stack = newItem;
		this.stack.limitSize(1);
		if (updateDryTime) {
			this.totalDryTime = getDryingTime();
			this.dryTime = 0;
			this.setChanged();

			if (this.level != null && !this.level.isClientSide) {
				if (newItem.isEmpty() || this.getBlockState().getValue(DryingRackBlock.WATERLOGGED)) {
					this.drying = false;
				} else {
					this.drying = this.quickCheck.getRecipeFor(new SingleRecipeInput(newItem), this.level).isPresent();
				}
			}
		}
	}

	public ItemStack takeTheItem() {
		ItemStack theItem = this.getTheItem();
		this.setTheItem(ItemStack.EMPTY);
		return theItem;
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel level) {
		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		return this.fillFromLootTable(lootTableKey, seed, level, currentServer.reloadableRegistries());
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel, ReloadableServerRegistries.Holder holder) {
		LootTable lootTable = holder.getLootTable(lootTableKey);

		if (lootTable == LootTable.EMPTY) return false;

		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getBlockPos())).create(LootContextParamSets.CHEST);

		lootTable.getRandomItemsRaw(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.location())), lootStack -> this.stack = lootStack);

		return true;
	}

	private int getDryingTime() {
		SingleRecipeInput singlerecipeinput = new SingleRecipeInput(this.getTheItem());
		Level level = getLevel();
		if (level == null)
			return DEFAULT_DRYING_TIME;
		return this.quickCheck.getRecipeFor(singlerecipeinput, level).map(holder -> holder.value().getDryingTime()).orElse(DEFAULT_DRYING_TIME);
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

	public record DryingRackHandler(DryingRackBlockEntity inventory) implements IItemHandlerModifiable {
		@Override
		public int getSlots() {
			return 1;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return this.inventory.getTheItem();
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!this.inventory.getTheItem().isEmpty()) {
				return stack;
			}

			ItemStack copyStack = stack.copy();
			ItemStack splitOut = copyStack.split(1);
			if (!simulate) {
				this.inventory.setTheItem(splitOut);
			}

			return copyStack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (this.inventory.drying) {
				return ItemStack.EMPTY;
			}

			return simulate ? this.inventory.getTheItem() : this.inventory.takeTheItem();
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return true;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			this.inventory.setTheItem(stack);
		}
	}
}
