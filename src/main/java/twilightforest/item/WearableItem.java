package twilightforest.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import twilightforest.block.CritterBlock;
import twilightforest.compat.CuriosCompat;
import twilightforest.compat.TFCompat;

import javax.annotation.Nullable;

public class WearableItem extends BlockItem {
    public WearableItem(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.HEAD;
    }

    @Override
    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(ModList.get().isLoaded(TFCompat.CURIOS_ID) && this.getBlock() instanceof CritterBlock) {
            CuriosCompat.setupCuriosCapability(stack);
        }
        return super.initCapabilities(stack, nbt);
    }
}
