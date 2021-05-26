package twilightforest.block;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFAuroralizedGlass extends AbstractGlassBlock {

    public BlockTFAuroralizedGlass(Properties props) {
        super(props);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
