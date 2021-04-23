package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockTFMiniatureStructure extends Block {
    public BlockTFMiniatureStructure() {
        super(Properties.create(Material.BARRIER).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(0.75F).notSolid());
    }
}
