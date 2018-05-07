package twilightforest.world;


import net.minecraft.block.BlockLeaves;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;

public class TFGenSmallRainboak extends TFGenSmallTwilightOak {

	public TFGenSmallRainboak() {
		this(false);
	}


	public TFGenSmallRainboak(boolean notify) {
		super(notify);
		this.leafState = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.RAINBOAK).withProperty(BlockLeaves.CHECK_DECAY, false);
	}

}
