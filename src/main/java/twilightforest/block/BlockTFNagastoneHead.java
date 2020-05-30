package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class BlockTFNagastoneHead extends HorizontalBlock {

	public BlockTFNagastoneHead() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14

		this.setDefaultState(getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		super.fillStateContainer(container);
		container.add(HORIZONTAL_FACING);
	}

	//We don't tick
//	@Override
//	@Deprecated
//	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
//		world.scheduleUpdate(pos, this, this.tickRate(world));
//	}
//
//	@Override
//	public void onBlockAdded(World world, BlockPos pos, BlockState state) {
//		world.scheduleUpdate(pos, this, this.tickRate(world));
//	}

	//      Blockstate to meta 0-3  will be heads
	//      Blockstate to meta 4-15 will be body components
	// BUT:
	//      Item meta 0 will be head
	//      Item meta 1 will be body
	// todo fix getStateForPlacement to respect this
	//TODO 1.14: Actually, move it to loot table
//	@Override
//	public int damageDropped(BlockState state) {
//		return NagastoneVariant.isHead(state.getValue(VARIANT)) ? 0 : 1;
//	}

	// Heads are manually placed, bodys are automatically connected
	// If player places head on horz side of block, use that block face. Else, defer to player rotation.
//	@Nonnull
//	@Override
//	@Deprecated
//	public BlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction facing, float hitX, float hitY, float hitZ, int meta, @Nonnull LivingEntity placer) {
//		return meta == 0
//				? this.getDefaultState().with(VARIANT, NagastoneVariant.getHeadFromFacing(facing.getAxis().isHorizontal() ? facing : placer.getHorizontalFacing().getOpposite()))
//				: this.getDefaultState();
//	}

	//Head doesn't need this
//	@Override
//	public void updateTick(World world, BlockPos pos, BlockState stateIn, Random rand) {
//		if (NagastoneVariant.isHead(stateIn.getValue(VARIANT)))
//			return;
//
//		// If state is not a head then you may go ahead and update
//		int connectionCount = 0;
//		BlockState stateOut;
//		Direction[] facings = new Direction[2];
//
//		// get sides
//		for (Direction side : Direction.VALUES)
//			if (world.getBlockState(pos.offset(side)).getBlock() == TFBlocks.naga_stone)
//				if (++connectionCount > 2) break;
//				else facings[connectionCount - 1] = side;
//
//
//		// if there are 2 sides that don't line on same axis, use an elbow part, else use axis part
//		// if there is 1 side, then use an axis part
//		// if there are 0 or greater than 2 sides, use solid
//		// use default if there are more than 3 connections or 0
//		switch (connectionCount) {
//			case 1:
//				facings[1] = facings[0]; // No null, for next statement
//			case 2:
//				stateOut = stateIn.with(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
//				break;
//			default:
//				stateOut = this.getDefaultState();
//				break;
//		}
//
//		// if result matches state in world, no need to add more effort
//		if (stateIn != stateOut) world.setBlockState(pos, stateOut);
//	}
}
