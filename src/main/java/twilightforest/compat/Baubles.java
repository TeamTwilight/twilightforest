package twilightforest.compat;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.TwilightForestMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class Baubles {

    public static boolean consumeInventoryItem(EntityPlayer player, Predicate<ItemStack> matcher, int count) {

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        boolean consumedSome = false;

        int slots = baubles.getSlots();
        for (int i = 0; i < slots && count > 0; i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (matcher.test(stack)) {
                ItemStack consumed = baubles.extractItem(i, count, false);
                count -= consumed.getCount();
                consumedSome = true;
            }
        }

        return consumedSome;
    }

    public static NonNullList<ItemStack> keepBaubles(EntityPlayer player) {

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        NonNullList<ItemStack> kept = NonNullList.withSize(baubles.getSlots(), ItemStack.EMPTY);

        for (int i = 0; i < baubles.getSlots(); i++) {
            kept.set(i, baubles.getStackInSlot(i).copy());
            baubles.setStackInSlot(i, ItemStack.EMPTY);
        }

        return kept;
    }

    public static void returnBaubles(EntityPlayer player, NonNullList<ItemStack> items) {

        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

        if (items.size() != baubles.getSlots()) {
            TwilightForestMod.LOGGER.warn("The list size doesn't equal amount of bauble slots, wtf did you do?");
            giveItems(player, items);
            return;
        }

        NonNullList<ItemStack> displaced = NonNullList.create();

        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack kept = items.get(i);
            if (!kept.isEmpty()) {
                ItemStack existing = baubles.getStackInSlot(i);
                baubles.setStackInSlot(i, kept);
                if (!existing.isEmpty()) {
                    displaced.add(existing);
                }
            }
        }

        giveItems(player, displaced);
    }

    private static void giveItems(EntityPlayer player, NonNullList<ItemStack> items) {
        for (ItemStack stack : items) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }
    }

    @SuppressWarnings("unchecked")
    public static final class BasicBaubleProvider implements ICapabilityProvider {
        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE ? (T) (IBauble) itemStack -> BaubleType.TRINKET : null;
        }
    }
}
