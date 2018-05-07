package twilightforest.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import twilightforest.compat.Baubles;
import twilightforest.compat.TFCompat;

import javax.annotation.Nullable;

public class ItemCharmBaubleable extends ItemTF {
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return TFCompat.BAUBLES.isActivated() ? new Baubles.BasicBaubleProvider() : null;
    }
}
