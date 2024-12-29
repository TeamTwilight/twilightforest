package twilightforest.block.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import java.util.UUID;

//used a fair bit of chest logic in this for the lid
public class KeepsakeCasketBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
	private static final int limit = 9 * 5;
	public NonNullList<ItemStack> contents = NonNullList.withSize(limit, ItemStack.EMPTY);
	@Nullable
	public String playerName;
	@Nullable
	public String casketname;
	@Nullable
	public UUID playeruuid;
	protected float lidAngle;
	protected float prevLidAngle;
	protected int numPlayersUsing;
	private int ticksSinceSync;

	public static KeepsakeCasketBlockEntity createSkullChestBE(BlockPos pos, BlockState state) {
		return new KeepsakeCasketBlockEntity(TFBlockEntities.SKULL_CHEST.get(), pos, state);
	}

	public static KeepsakeCasketBlockEntity createKeepsakeCasketBE(BlockPos pos, BlockState state) {
		return new KeepsakeCasketBlockEntity(TFBlockEntities.KEEPSAKE_CASKET.get(), pos, state);
	}

	private KeepsakeCasketBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	//[VanillaCopy] of EnderChestBlockEntity, with some small adaptations
	public static void tick(Level level, BlockPos pos, BlockState state, KeepsakeCasketBlockEntity te) {
		if (++te.ticksSinceSync % 20 * 4 == 0) {
			level.blockEvent(pos, state.getBlock(), 1, te.numPlayersUsing);
		}
		te.prevLidAngle = te.lidAngle;
		if (te.numPlayersUsing > 0 && te.lidAngle == 0.0F) {
			level.playSound(null, pos, TFSounds.CASKET_OPEN.get(), SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
		}
		if (te.numPlayersUsing == 0 && te.lidAngle > 0.0F || te.numPlayersUsing > 0 && te.lidAngle < 1.0F) {
			float f2 = te.lidAngle;

			if (te.numPlayersUsing > 0) te.lidAngle += 0.025F;
			else te.lidAngle -= 0.075F;

			if (te.lidAngle > 1.0F) te.lidAngle = 1.0F;

			if (te.lidAngle < 0.4F && f2 >= 0.4F) {
				level.playSound(null, pos, TFSounds.CASKET_CLOSE.get(), SoundSource.BLOCKS, 0.75F, level.getRandom().nextFloat() * 0.1F + 0.9F);
			}
			if (te.lidAngle < 0.0F) te.lidAngle = 0.0F;
		}

	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.contents) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.contents;
	}

	@Override
	public void setItems(NonNullList<ItemStack> items) {
		// Because NonNullList is a very incomplete List as a whole, not even proxying half of the conveniences of List to its delegate.
		// We hereby curse it to merge stacks instead.

		// Due to some outside usages we will be doing a pull-overwrite operation here instead.
		int limit = Math.min(contents.size(), items.size());

		for (int i = 0; i < limit; i++) {
			ItemStack stack = items.get(i);
			//noinspection ConstantConditions
			if (stack != null) { // No, it is very easily possible for NonNullList to have null.
				this.contents.set(i, items.get(i));
				items.set(i, ItemStack.EMPTY);
			}
		}
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
		return limit;
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		if (!this.trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, this.contents, provider);
		}
		if (this.playeruuid != null) compound.putUUID("deadPlayer", this.playeruuid);
		if (this.casketname != null) compound.putString("playerName", this.casketname);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.loadAdditional(nbt, provider);
		this.contents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.contents, provider);
		}
		if (nbt.hasUUID("deadPlayer")) this.playeruuid = nbt.getUUID("deadPlayer");
		if (nbt.hasUUID("playerName")) this.casketname = nbt.getString("playerName");
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	//if we have a dead player UUID set, then only that player can open the casket
	@Override
	public boolean stillValid(Player user) {
		if (this.playeruuid != null) {
			if (user.hasPermissions(3) || user.getGameProfile().getId().equals(this.playeruuid)) {
				return super.stillValid(user);
			} else {
				return false;
			}
		} else {
			return super.stillValid(user);
		}
	}

	@Override
	public boolean canOpen(Player user) {
		if (this.playeruuid != null) {
			if (user.hasPermissions(3) || user.getGameProfile().getId().equals(this.playeruuid)) {
				return super.canOpen(user);
			} else {
				user.playNotifySound(TFSounds.CASKET_LOCKED.get(), SoundSource.BLOCKS, 0.5F, 0.5F);
				user.displayClientMessage(Component.translatable("block.twilightforest.casket.locked", playerName).withStyle(ChatFormatting.RED), true);
				return false;
			}
		} else {
			return super.canOpen(user);
		}
	}

	//remove stored player when chest is broken
	@Override
	public void setRemoved() {
		this.playeruuid = null;
		this.invalidateCapabilities();
		super.setRemoved();
	}

	@Override
	public void startOpen(Player player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			this.getLevel().blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, this.numPlayersUsing);
		}

	}

	@Override
	public void stopOpen(Player player) {
		if (!player.isSpectator()) {
			--this.numPlayersUsing;
			this.getLevel().blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, this.numPlayersUsing);
		}
	}

	@Override
	public float getOpenNess(float partialTicks) {
		return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
	}
}
