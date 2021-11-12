package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import twilightforest.TFSounds;

public class EncasedSmokerBlock extends TFSmokerBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected EncasedSmokerBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isClientSide) return;

		boolean powered = world.hasNeighborSignal(pos);

		if (!state.getValue(ACTIVE) && powered) {
			world.setBlock(pos, state.setValue(ACTIVE, true), 3);
			world.playSound(null, pos, TFSounds.SMOKER_START, SoundSource.BLOCKS, 0.3F, 0.6F);
		}

		if (state.getValue(ACTIVE) && !powered) {
			world.setBlock(pos, state.setValue(ACTIVE, false), 3);
			world.playSound(null, pos, TFSounds.SMOKER_START, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}
}
