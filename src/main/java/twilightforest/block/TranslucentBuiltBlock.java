package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import twilightforest.init.TFSounds;

public class TranslucentBuiltBlock extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public TranslucentBuiltBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(ACTIVE)) {
			level.removeBlock(pos, false);
			level.playSound(null, pos, TFSounds.BUILDER_REPLACE.get(), SoundSource.BLOCKS, 0.3F, 0.5F);

			for (Direction e : Direction.values()) {
				BuilderBlock.activateBuiltBlocks(level, pos.relative(e));
			}
		}
	}
}
