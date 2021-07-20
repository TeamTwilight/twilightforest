package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFUnfinished extends Block {
    private boolean nyi;

    public BlockTFUnfinished(Properties properties, boolean nyi) {
        super(properties);
        this.nyi = nyi;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(nyi) {
            tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
        } else {
            tooltip.add(new TranslationTextComponent("twilightforest.misc.wip0"));
            tooltip.add(new TranslationTextComponent("twilightforest.misc.wip1"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
