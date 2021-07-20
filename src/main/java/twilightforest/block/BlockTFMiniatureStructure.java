package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFMiniatureStructure extends Block {
    public BlockTFMiniatureStructure() {
        super(Properties.create(Material.BARRIER).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(0.75F).notSolid());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
