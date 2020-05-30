package twilightforest.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class BlockTFStairs extends StairsBlock {

    protected BlockTFStairs(BlockState modelState) {
        super(() -> modelState, Properties.from(modelState.getBlock()));
    }
}
