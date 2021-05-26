package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.tileentity.*;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockTFReactor extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFReactor(Properties props) {
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

		if (!state.get(ACTIVE) && isReactorReady(world, pos)) {
			// check if we should fire up the reactor
			world.setBlockState(pos, state.with(ACTIVE, true));
		}
	}

	/**
	 * Check if the reactor has all the specified things around it
	 */
	private boolean isReactorReady(World world, BlockPos pos) {
		return Arrays.stream(Direction.values())
				.allMatch(e -> world.getBlockState(pos.offset(e)).getBlock() == Blocks.REDSTONE_BLOCK);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(ACTIVE);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return hasTileEntity(state) ? new TileEntityTFCReactorActive() : null;
	}
}
