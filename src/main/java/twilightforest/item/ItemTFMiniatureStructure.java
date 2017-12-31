package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import twilightforest.enums.StructureVariant;

import java.util.Locale;

public class ItemTFMiniatureStructure extends ItemBlockTFMeta {
    public ItemTFMiniatureStructure(Block block) {
        super(block);
        this.setAppend(false);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocalFormatted(this.getUnlocalizedName(stack), I18n.translateToLocal("structure." + StructureVariant.values()[stack.getMetadata() % StructureVariant.values().length].getName().toLowerCase(Locale.ROOT) + ".name"));
    }
}
