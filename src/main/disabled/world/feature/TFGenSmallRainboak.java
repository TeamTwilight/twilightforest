package twilightforest.world.feature;

import net.minecraft.block.LeavesBlock;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;

//TODO: Turn this into a simple tree Builder
public class TFGenSmallRainboak extends TFGenSmallTwilightOak {

	public TFGenSmallRainboak() {
		this(false);
	}

	public TFGenSmallRainboak(boolean notify) {
		super(notify, 4,
				TFBlocks.twilight_log.getDefaultState(),
				TFBlocks.twilight_leaves.getDefaultState().with(BlockTFLeaves.VARIANT, LeavesVariant.RAINBOAK).with(LeavesBlock.CHECK_DECAY, false)
		);
	}
}
