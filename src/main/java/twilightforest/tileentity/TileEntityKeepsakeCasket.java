package twilightforest.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityKeepsakeCasket extends LockableLootTileEntity {
    private static final int limit = 9 * 5;
    private NonNullList<ItemStack> contents = NonNullList.withSize(limit, ItemStack.EMPTY);

    public TileEntityKeepsakeCasket() {
        super(TFTileEntities.KEEPSAKE_CASKET.get());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
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

        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.contents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.contents);
        }
    }
}
