package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class MiniatureStructureBlock extends Block {
    public MiniatureStructureBlock() {
        super(Properties.of(Material.BARRIER).requiresCorrectToolForDrops().strength(0.75F).noOcclusion());
    }
}
