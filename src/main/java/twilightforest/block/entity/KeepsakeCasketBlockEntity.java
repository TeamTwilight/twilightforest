package twilightforest.block.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.UUID;

//used a fair bit of chest logic in this for the lid
@OnlyIn(value = Dist.CLIENT, _interface = LidBlockEntity.class)
public class KeepsakeCasketBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
    private static final int limit = 9 * 5;
    public NonNullList<ItemStack> contents = NonNullList.withSize(limit, ItemStack.EMPTY);
    @Nullable
    public String name;
    @Nullable
    public String casketname;
    @Nullable
    public UUID playeruuid;
    protected float lidAngle;
    protected float prevLidAngle;
    protected int numPlayersUsing;
    private int ticksSinceSync;

    public KeepsakeCasketBlockEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.KEEPSAKE_CASKET.get(), pos, state);
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.contents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        // Because NonNullList is a very incomplete List as a whole, not even proxying half of the conveniences of List to its delegate.
        // We hereby curse it to merge stacks instead.

        // Due to some outside usages we will be doing a pull-overwrite operation here instead.
        int limit = Math.min(contents.size(), itemsIn.size());

        for (int i = 0; i < limit; i++) {
            ItemStack stack = itemsIn.get(i);
            //noinspection ConstantConditions
            if (stack != null) { // No, it is very easily possible for NonNullList to have null.
                contents.set(i, itemsIn.get(i));
                itemsIn.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.twilightforest.keepsake_casket");
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
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.contents);
        }
        if(playeruuid != null) compound.putUUID("deadPlayer", playeruuid);
        if(casketname != null) compound.putString("playerName", casketname);
        return compound;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.contents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.contents);
        }
        if(nbt.hasUUID("deadPlayer")) playeruuid = nbt.getUUID("deadPlayer");
        if(nbt.hasUUID("playerName")) casketname = nbt.getString("playerName");
    }

    //[VanillaCopy] of EnderChestTileEntity, with some small adaptations
    public static void tick(Level level, BlockPos pos, BlockState state, KeepsakeCasketBlockEntity te) {
        if (++te.ticksSinceSync % 20 * 4 == 0) {
            level.blockEvent(pos, TFBlocks.KEEPSAKE_CASKET.get(), 1, te.numPlayersUsing);
        }
        te.prevLidAngle = te.lidAngle;
        if (te.numPlayersUsing > 0 && te.lidAngle == 0.0F) {
            level.playSound(null, pos, TFSounds.CASKET_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
        if (te.numPlayersUsing == 0 && te.lidAngle > 0.0F || te.numPlayersUsing > 0 && te.lidAngle < 1.0F) {
            float f2 = te.lidAngle;

            if (te.numPlayersUsing > 0) te.lidAngle += 0.025F;
            else te.lidAngle -= 0.075F;

            if (te.lidAngle > 1.0F) te.lidAngle = 1.0F;

            if (te.lidAngle < 0.4F && f2 >= 0.4F) {
                level.playSound(null, pos, TFSounds.CASKET_CLOSE, SoundSource.BLOCKS, 0.75F, level.random.nextFloat() * 0.1F + 0.9F);
            }
            if (te.lidAngle < 0.0F) te.lidAngle = 0.0F;
        }

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
        if(playeruuid != null) {
            if(user.hasPermissions(3) || user.getGameProfile().getId().equals(playeruuid)) {
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
        if(playeruuid != null) {
            if(user.hasPermissions(3) || user.getGameProfile().getId().equals(playeruuid)) {
                return super.canOpen(user);
            } else {
                user.playNotifySound(TFSounds.CASKET_LOCKED, SoundSource.BLOCKS, 0.5F, 0.5F);
                user.displayClientMessage(new TranslatableComponent("block.twilightforest.casket.locked", name).withStyle(ChatFormatting.RED), true);
                return false;
            }
        } else {
            return super.canOpen(user);
        }
    }

    @Override
    protected IItemHandler createUnSidedHandler() {
        return new EmptyHandler();
    }

    //remove stored player when chest is broken
    @Override
    public void setRemoved() {
        playeruuid = null;
        this.invalidateCaps();
        super.setRemoved();
    }

    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.level.blockEvent(this.worldPosition, TFBlocks.KEEPSAKE_CASKET.get(), 1, this.numPlayersUsing);
        }

    }

    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.level.blockEvent(this.worldPosition, TFBlocks.KEEPSAKE_CASKET.get(), 1, this.numPlayersUsing);
        }

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getOpenNess(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}
