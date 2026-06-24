package twilightforest.block.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

public class SkullChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
	private static final int SIZE = 9 * 5;
	public NonNullList<ItemStack> contents = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			level.playSound(null, pos, TFSounds.SKULL_CHEST_OPEN.get(), SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		}

		@Override
		protected void onClose(Level level, BlockPos pos, BlockState state) {
			level.playSound(null, pos, TFSounds.SKULL_CHEST_CLOSE.get(), SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		}

		@Override
		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int id, int param) {
			Block block = state.getBlock();
			level.blockEvent(pos, block, 1, param);
		}

		@Override
		public boolean isOwnContainer(Player player) {
			if (player.containerMenu instanceof ChestMenu chest) {
				return chest.getContainer() == SkullChestBlockEntity.this;
			} else {
				return false;
			}
		}
	};
	private final ChestLidController chestLidController = new ChestLidController();
	@Nullable
	public ResolvableProfile owner;

	public SkullChestBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.SKULL_CHEST.get(), pos, state);
	}

	public SkullChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SkullChestBlockEntity te) {
		te.chestLidController.tickLid();
	}

	public ContainerOpenersCounter getOpenersCounter() {
		return this.openersCounter;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.contents;
	}

	@Override
	public void setItems(NonNullList<ItemStack> items) {
		this.contents = items;
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new ChestMenu(MenuType.GENERIC_9x5, id, player, this, 5);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (!this.trySaveLootTable(output)) {
			ContainerHelper.saveAllItems(output, this.contents);
		}
		output.storeNullable("owner", ResolvableProfile.CODEC, this.owner);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.contents = NonNullList.withSize(SIZE, ItemStack.EMPTY);
		if (!this.tryLoadLootTable(input)) {
			ContainerHelper.loadAllItems(input, this.contents);
		}
		this.owner = input.read("owner", ResolvableProfile.CODEC).orElse(null);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.chestLidController.shouldBeOpen(type > 0);
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	@Override
	public void startOpen(ContainerUser containerUser) {
		if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
			this.getOpenersCounter().incrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), containerUser.getContainerInteractionRange());
		}
	}

	@Override
	public void stopOpen(ContainerUser containerUser) {
		if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
			this.getOpenersCounter().decrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void recheckOpen() {
		if (!this.isRemoved()) {
			this.getOpenersCounter().recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public float getOpenNess(float partialTicks) {
		return this.chestLidController.getOpenness(partialTicks);
	}

	//if we have a dead player UUID set, then only that player can open the casket
	@Override
	public boolean stillValid(Player player) {
		if (this.owner != null) {
			if (player.permissions().hasPermission(Permissions.COMMANDS_ADMIN) || player.getGameProfile().equals(this.owner.partialProfile())) {
				return super.stillValid(player);
			} else {
				return false;
			}
		} else {
			return super.stillValid(player);
		}
	}

	@Override
	public boolean canOpen(Player player) {
		if (this.owner != null) {
			if (player.permissions().hasPermission(Permissions.COMMANDS_ADMIN) || player.getGameProfile().equals(this.owner.partialProfile())) {
				return super.canOpen(player);
			} else {
				this.displayLockedInfo(player);
				return false;
			}
		} else {
			return super.canOpen(player);
		}
	}

	public void displayLockedInfo(Player player) {
		player.level().playLocalSound(this.getBlockPos(), TFSounds.SKULL_CHEST_LOCKED.get(), SoundSource.BLOCKS, 0.5F, 0.5F, false);
		player.sendOverlayMessage(Component.translatable("block.twilightforest.skull_chest.locked", this.owner.name().orElse("???")).withStyle(ChatFormatting.RED));
	}

	//remove stored player when chest is broken
	@Override
	public void setRemoved() {
		this.owner = null;
		super.setRemoved();
	}
}
