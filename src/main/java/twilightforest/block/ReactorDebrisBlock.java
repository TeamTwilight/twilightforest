package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorDebrisBlock extends Block {

	public ReactorDebrisBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean mioving) {
		//schedule this block to be removed 3 seconds after placement if not removed before then
		level.scheduleTick(pos, this, 60);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getBlock() == this) {
			level.destroyBlock(pos, false);
		}
	}
}
