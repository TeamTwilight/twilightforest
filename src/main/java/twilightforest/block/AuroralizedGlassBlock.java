package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

public class AuroralizedGlassBlock extends TransparentBlock {

	public AuroralizedGlassBlock(Properties properties) {
		super(properties);
	}

	// PORTAGE : crochet NeoForge sans equivalent Forge, a rebrancher par mixin.
	public Integer twilightforestBeaconColor(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		return ColorUtil.hsvToRGB(SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}
}
