package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import twilightforest.block.DryingRackBlock;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.DryingRecipe;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity {

	public static final int DEFAULT_DRYING_TIME = 20 * 60 * 5; //5 Minutes
	private ItemStack stack = ItemStack.EMPTY;
	private final RecipeManager.CachedCheck<RecipeInput, DryingRecipe> quickCheck = RecipeManager.createCheck(TFRecipes.DRYING_RECIPE.get());

	protected boolean drying;
	protected int dryTime;
	protected int totalDryTime;

	public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.DRYING_RACK.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
		if (!state.getValue(DryingRackBlock.WATERLOGGED) && level instanceof ServerLevel serverLevel) {
			if (!entity.getTheItem().isEmpty()) {
				SingleRecipeInput input = new SingleRecipeInput(entity.getTheItem());
				RecipeHolder<DryingRecipe> recipeholder = entity.quickCheck.getRecipeFor(input, serverLevel).orElse(null);
				boolean recipeHolderExists = recipeholder != null;
				entity.updateDryingTime(recipeHolderExists);
				if (recipeHolderExists) {
					entity.dryTime++;

					if (entity.dryTime >= entity.totalDryTime) {
						entity.setTheItem(recipeholder.value().assemble(input));
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
			this.totalDryTime = this.getDryingTime();
			this.dryTime = 0;
			this.setChanged();

			if (this.level instanceof ServerLevel serverLevel) {
				if (newItem.isEmpty() || this.getBlockState().getValue(DryingRackBlock.WATERLOGGED)) {
					this.drying = false;
				} else {
					this.drying = this.quickCheck.getRecipeFor(new SingleRecipeInput(newItem), serverLevel).isPresent();
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

		lootTable.getRandomItems(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.identifier())), lootStack -> this.stack = lootStack);

		return true;
	}

	private int getDryingTime() {
		SingleRecipeInput singlerecipeinput = new SingleRecipeInput(this.getTheItem());
		if (this.level instanceof ServerLevel serverLevel) {
			return this.quickCheck.getRecipeFor(singlerecipeinput, serverLevel).map(holder -> holder.value().getDryingTime()).orElse(DEFAULT_DRYING_TIME);
		}
		return DEFAULT_DRYING_TIME;
	}

	public boolean isDrying() {
		return this.drying;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.storeNullable("item", ItemStack.CODEC, this.stack);
		output.putInt("dry_time", this.dryTime);
		output.putInt("total_dry_time", this.totalDryTime);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.stack = input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		this.dryTime = input.getIntOr("dry_time", 0);
		this.totalDryTime = input.getIntOr("total_dry_time", DEFAULT_DRYING_TIME);
		this.drying = input.getBooleanOr("drying", false);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}

	//Based From MasonJarBlockEntity class's MasonJarItemStackHandler
	public static class DryingRackHandler extends ItemStacksResourceHandler {
		protected final DryingRackBlockEntity inventory;

		public DryingRackHandler(DryingRackBlockEntity blockEntity) {
			super(1);
			this.inventory = blockEntity;
		}

		// Used for simple checks of what the one item is, without going through all the hoops. Used by the renderer and when saving contents to item
		public ItemStack getItem() {
			return this.stacks.getFirst().copy();
		}

		// Peeks at the stored item, without cloning it
		private ItemStack peekItem() {
			return this.stacks.getFirst();
		}

		// Used when syncing to client and when placing a jar that already has stored items
		public void setItem(ItemStack itemStack) {
			this.stacks.set(0, itemStack);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			return resource.toStack().canFitInsideContainerItems();
		}

		@Override
		public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
			if (this.inventory.drying) {
				return 0;
			}
			int extracted = super.extract(index, resource, amount, transaction);
			if (extracted > 0) {
				this.inventory.setChanged();
			}
			return extracted;
		}

		@Override
		public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
			int inserted = super.insert(index, resource, amount, transaction);
			if (inserted > 0) {
				this.inventory.setChanged();
			}
			return inserted;
		}

		public boolean isEmpty() {
			return this.stacks.getFirst().isEmpty();
		}
	}
}
