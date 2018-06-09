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
                int consume = Math.min(count, stack.getCount());
                stack.shrink(consume);
                count -= consume;
                consumedSome = true;
            }
        }

        return consumedSome;
    }

    public static void keepBaubles(EntityPlayer player, NonNullList<ItemStack> items) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

        if (baubles.getSlots() != items.size()) {
            TwilightForestMod.LOGGER.warn("The list size doesn't equal amount of bauble slots, wtf did you do?");
        } else {
            for (int i = 0; i < baubles.getSlots() && i < items.size(); i++) {
                items.set(i, baubles.getStackInSlot(i));
                baubles.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public static void respawnBaubles(EntityPlayer player, NonNullList<ItemStack> items) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

        if (items.size() == baubles.getSlots()) {
            for (int i = 0; i < baubles.getSlots() && i < items.size(); i++) {
                baubles.setStackInSlot(i, items.get(i));
            }
        } else {
            TwilightForestMod.LOGGER.warn("The list size doesn't equal amount of bauble slots, wtf did you do?");
            dropTableItems(player, items);
        }
    }

    private static void dropTableItems(EntityPlayer player, @Nullable NonNullList<ItemStack> items) {
        if (items != null) {
            for (ItemStack itemStack : items) {
                if (!itemStack.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                }
            }
        }
    }

    public static int getSlotAmount(EntityPlayer player) {
        return BaublesApi.getBaublesHandler(player).getSlots();
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
