package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;

public class BlockTFEncasedSmoker extends BlockTFSmoker {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected BlockTFEncasedSmoker(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		boolean powered = world.isBlockPowered(pos);

		if (!state.get(ACTIVE) && powered) {
			world.setBlockState(pos, state.with(ACTIVE, true), 3);
			world.playSound(null, pos, TFSounds.SMOKER_START, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}

		if (state.get(ACTIVE) && !powered) {
			world.setBlockState(pos, state.with(ACTIVE, false), 3);
			world.playSound(null, pos, TFSounds.SMOKER_START, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(ACTIVE);
	}

}
