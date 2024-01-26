package twilightforest.block;

import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

public class ThornLeavesBlock extends TFAlternativeStemLeavesBlock {
    public ThornLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isStem(BlockState state) {
        return state.is(TFBlocks.BROWN_THORNS) || state.is(TFBlocks.GREEN_THORNS);
    }
}
