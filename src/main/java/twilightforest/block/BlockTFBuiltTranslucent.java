package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFSounds;

import java.util.Random;

public class BlockTFBuiltTranslucent extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFBuiltTranslucent(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		super.fillStateContainer(container);
		container.add(ACTIVE);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(ACTIVE)) {
			world.removeBlock(pos, false);
			world.playSound(null, pos, TFSounds.BUILDER_REPLACE, SoundCategory.BLOCKS, 0.3F, 0.5F);

			for (Direction e : Direction.values()) {
				BlockTFBuilder.activateBuiltBlocks(world, pos.offset(e));
			}
		}
	}
}
