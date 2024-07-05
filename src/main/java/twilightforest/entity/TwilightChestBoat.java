package twilightforest.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

public class TwilightChestBoat extends TwilightBoat implements HasCustomInventoryScreen, ContainerEntity {

	private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
	@javax.annotation.Nullable
	private ResourceKey<LootTable> lootTable;
	private long lootTableSeed;

	public TwilightChestBoat(EntityType<? extends TwilightBoat> type, Level level) {
		super(type, level);
	}

	@SuppressWarnings("this-escape")
	public TwilightChestBoat(Level level, double x, double y, double z) {
		this(TFEntities.CHEST_BOAT.get(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected float getSinglePassengerXOffset() {
		return 0.15F;
	}

	@Override
	protected int getMaxPassengers() {
		return 1;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		this.addChestVehicleSaveData(tag, this.registryAccess());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.readChestVehicleSaveData(tag, this.registryAccess());
	}

	@Override
	public void destroy(DamageSource damageSource) {
		super.destroy(damageSource);
		this.chestVehicleDestroyed(damageSource, this.level(), this);
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		if (!this.level().isClientSide() && reason.shouldDestroy()) {
			Containers.dropContents(this.level(), this, this);
		}

		super.remove(reason);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.canAddPassenger(player) && !player.isSecondaryUseActive()) {
			return super.interact(player, hand);
		} else {
			InteractionResult interactionresult = this.interactWithContainerVehicle(player);
			if (interactionresult.consumesAction()) {
				this.gameEvent(GameEvent.CONTAINER_OPEN, player);
				PiglinAi.angerNearbyPiglins(player, true);
			}

			return interactionresult;
		}
	}

	@Override
	public void openCustomInventoryScreen(Player player) {
		player.openMenu(this);
		if (!player.level().isClientSide()) {
			this.gameEvent(GameEvent.CONTAINER_OPEN, player);
			PiglinAi.angerNearbyPiglins(player, true);
		}

	}

	@Override
	public Item getDropItem() {
		return switch (this.getTwilightBoatType()) {
			case TWILIGHT_OAK -> TFItems.TWILIGHT_OAK_CHEST_BOAT.get();
			case CANOPY -> TFItems.CANOPY_CHEST_BOAT.get();
			case MANGROVE -> TFItems.MANGROVE_CHEST_BOAT.get();
			case DARKWOOD -> TFItems.DARK_CHEST_BOAT.get();
			case TIME -> TFItems.TIME_CHEST_BOAT.get();
			case TRANSFORMATION -> TFItems.TRANSFORMATION_CHEST_BOAT.get();
			case MINING -> TFItems.MINING_CHEST_BOAT.get();
			case SORTING -> TFItems.SORTING_CHEST_BOAT.get();
		};
	}

	@Override
	public void clearContent() {
		this.clearChestVehicleContent();
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.getChestVehicleItem(index);
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		return this.removeChestVehicleItem(index, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return this.removeChestVehicleItemNoUpdate(index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.setChestVehicleItem(index, stack);
	}

	@Override
	public SlotAccess getSlot(int index) {
		return this.getChestVehicleSlot(index);
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return this.isChestVehicleStillValid(player);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		if (this.lootTable != null && player.isSpectator()) {
			return null;
		} else {
			this.unpackLootTable(inventory.player);
			return ChestMenu.threeRows(id, inventory, this);
		}
	}

	public void unpackLootTable(@Nullable Player player) {
		this.unpackChestVehicleLootTable(player);
	}

	@Override
	public ResourceKey<LootTable> getLootTable() {
		return this.lootTable;
	}

	@Override
	public void setLootTable(@Nullable ResourceKey<LootTable> location) {
		this.lootTable = location;
	}

	@Override
	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long seed) {
		this.lootTableSeed = seed;
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.itemStacks;
	}

	@Override
	public void clearItemStacks() {
		this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	}
}
