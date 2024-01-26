package twilightforest.block;

import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

public class BeanstalkLeavesBlock extends TFAlternativeStemLeavesBlock {
    public BeanstalkLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isStem(BlockState state) {
        return state.is(TFBlocks.HUGE_STALK);
    }
}
