package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import twilightforest.enums.NagastoneVariant;

import java.util.Random;

public class BlockTFNagastone extends Block {

	public static final EnumProperty<NagastoneVariant> VARIANT = EnumProperty.create("variant", NagastoneVariant.class);

	public BlockTFNagastone() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE).tickRandomly());
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14

		this.setDefaultState(stateContainer.getBaseState().with(VARIANT, NagastoneVariant.SOLID));
	}

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
	// TODO: Actually, move this to loot table
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

	@Override
	@Deprecated
	public void scheduledTick(BlockState stateIn, ServerWorld world, BlockPos pos, Random random) {
		if (NagastoneVariant.isHead(stateIn.get(VARIANT))) //TODO: Remove Head states
			return;

		// If state is not a head then you may go ahead and update
		int connectionCount = 0;
		BlockState stateOut;
		Direction[] facings = new Direction[2];

		// get sides
		for (Direction side : Direction.values())
			if (world.getBlockState(pos.offset(side)).getBlock() == TFBlocks.naga_stone.get())
				if (++connectionCount > 2) break;
				else facings[connectionCount - 1] = side;


		// if there are 2 sides that don't line on same axis, use an elbow part, else use axis part
		// if there is 1 side, then use an axis part
		// if there are 0 or greater than 2 sides, use solid
		// use default if there are more than 3 connections or 0
		switch (connectionCount) {
			case 1:
				facings[1] = facings[0]; // No null, for next statement
			case 2:
				stateOut = stateIn.with(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
				break;
			default:
				stateOut = this.getDefaultState();
				break;
		}

		// if result matches state in world, no need to add more effort
		if (stateIn != stateOut) world.setBlockState(pos, stateOut);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(VARIANT);
	}


	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
		return state.with(VARIANT, NagastoneVariant.rotate(state.get(VARIANT), rotation));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.with(VARIANT, NagastoneVariant.mirror(state.get(VARIANT), mirror));
	}

//	@Override
//	protected boolean canSilkHarvest() {
//		return false;
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//		return false;
//	}
}
