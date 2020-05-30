package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockTFMiniatureStructure extends Block {
    public BlockTFMiniatureStructure() {
        super(Properties.create(Material.BARRIER).hardnessAndResistance(0.75F).nonOpaque());
    }
}
