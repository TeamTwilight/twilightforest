package twilightforest.tileentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.UUID;

//used a fair bit of chest logic in this for the lid
@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class TileEntityKeepsakeCasket extends LockableLootTileEntity implements IChestLid, ITickableTileEntity {
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

    public TileEntityKeepsakeCasket() {
        super(TFTileEntities.KEEPSAKE_CASKET.get());
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
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.twilightforest.keepsake_casket");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChestContainer(ContainerType.GENERIC_9X5, id, player, this, 5);
    }

    @Override
    public int getSizeInventory() {
        return limit;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.contents);
        }
        if(playeruuid != null) compound.putUniqueId("deadPlayer", playeruuid);
        if(casketname != null) compound.putString("playerName", casketname);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.contents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.contents);
        }
        if(nbt.hasUniqueId("deadPlayer")) playeruuid = nbt.getUniqueId("deadPlayer");
        if(nbt.hasUniqueId("playerName")) casketname = nbt.getString("playerName");
    }

    //[VanillaCopy] of EnderChestTileEntity, with some small adaptations
    @Override
    public void tick() {
        if (++this.ticksSinceSync % 20 * 4 == 0) {
            this.world.addBlockEvent(this.pos, TFBlocks.keepsake_casket.get(), 1, this.numPlayersUsing);
        }
        this.prevLidAngle = this.lidAngle;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.world.playSound(null, this.pos, TFSounds.CASKET_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0) this.lidAngle += 0.025F;
            else this.lidAngle -= 0.075F;

            if (this.lidAngle > 1.0F) this.lidAngle = 1.0F;

            if (this.lidAngle < 0.4F && f2 >= 0.4F) {
                this.world.playSound(null, this.pos, TFSounds.CASKET_CLOSE, SoundCategory.BLOCKS, 0.75F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (this.lidAngle < 0.0F) this.lidAngle = 0.0F;
        }

    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    //if we have a dead player UUID set, then only that player can open the casket
    @Override
    public boolean isUsableByPlayer(PlayerEntity user) {
        if(playeruuid != null) {
            if(user.hasPermissionLevel(3) || user.getGameProfile().getId().equals(playeruuid)) {
                return super.isUsableByPlayer(user);
            } else {
                return false;
            }
        } else {
            return super.isUsableByPlayer(user);
        }
    }

    @Override
    public boolean canOpen(PlayerEntity user) {
        if(playeruuid != null) {
            if(user.hasPermissionLevel(3) || user.getGameProfile().getId().equals(playeruuid)) {
                return super.canOpen(user);
            } else {
                user.playSound(TFSounds.CASKET_LOCKED, SoundCategory.BLOCKS, 0.5F, 0.5F);
                user.sendStatusMessage(new TranslationTextComponent("block.twilightforest.casket.locked", name).mergeStyle(TextFormatting.RED), true);
                return false;
            }
        } else {
            return super.canOpen(user);
        }
    }

    //remove stored player when chest is broken
    @Override
    public void remove() {
        playeruuid = null;
        this.updateContainingBlockInfo();
        super.remove();
    }

    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, TFBlocks.keepsake_casket.get(), 1, this.numPlayersUsing);
        }

    }

    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, TFBlocks.keepsake_casket.get(), 1, this.numPlayersUsing);
        }

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}
