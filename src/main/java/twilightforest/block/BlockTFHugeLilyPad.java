package twilightforest.block;

import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.enums.HugeLilypadPiece;

import java.util.List;

public class BlockTFHugeLilyPad extends BushBlock {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<HugeLilypadPiece> PIECE = EnumProperty.create("piece", HugeLilypadPiece.class);
	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 0.015625, 1));

	private boolean isSelfDestructing = false;

	protected BlockTFHugeLilyPad() {
		super(Properties.create(Material.PLANTS).sound(SoundType.PLANT));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(PIECE, HugeLilypadPiece.NW));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, PIECE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		IFluidState ifluidstate = worldIn.getFluidState(pos);
		return ifluidstate.getFluid() == Fluids.WATER;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		//TwilightForestMod.LOGGER.info("Destroying giant lilypad at {}, state {}", pos, state);

		if (!this.isSelfDestructing) {
			this.setGiantBlockToAir(world, pos, state);
		}
	}

	private void setGiantBlockToAir(World world, BlockPos pos, BlockState state) {
		// this flag is not threadsafe
		this.isSelfDestructing = true;

		for (BlockPos check : this.getAllMyBlocks(pos, state)) {
			BlockState stateThere = world.getBlockState(check);
			if (stateThere.getBlock() == this) {
				world.destroyBlock(check, false);
			}
		}

		this.isSelfDestructing = false;
	}

//	@Override
//	public boolean canBlockStay(World world, BlockPos pos, BlockState state) {
//		for (BlockPos check : this.getAllMyBlocks(pos, state)) {
//			BlockState dStateBelow = world.getBlockState(check.down());
//
//			if (!(dStateBelow.getBlock() == Blocks.WATER || dStateBelow.getBlock() == Blocks.FLOWING_WATER)
//					|| dStateBelow.get(BlockLiquid.LEVEL) != 0) {
//				return false;
//			}
//
//			if (world.getBlockState(check).getBlock() != this) {
//				//TwilightForestMod.LOGGER.info("giant lilypad cannot stay because we can't find all 4 pieces");
//				return false;
//			}
//		}
//
//		return true;
//	}

	/**
	 * Get all 4 coordinates for all parts of this lily pad.
	 */
	public List<BlockPos> getAllMyBlocks(BlockPos pos, BlockState state) {
		List<BlockPos> pieces = Lists.newArrayListWithCapacity(4);
		if (state.getBlock() == this) {
			// find NW corner
			BlockPos nwPos = pos;
			switch (state.get(PIECE)) {
				case NE:
					nwPos = nwPos.west();
					break;
				case SE:
					nwPos = nwPos.north().west();
					break;
				case SW:
					nwPos = nwPos.north();
					break;
				default:
					break;
			}

			pieces.add(nwPos);
			pieces.add(nwPos.south());
			pieces.add(nwPos.east());
			pieces.add(nwPos.south().east());
		}

		return pieces;
	}

	// [VanillaCopy] of super without dropping
	//TODO: We did move to loot tables, but evaluate on testing
//	@Override
//	protected void checkAndDropBlock(World worldIn, BlockPos pos, BlockState state) {
//		if (!this.canBlockStay(worldIn, pos, state)) {
//			// this.dropBlockAsItem(worldIn, pos, state, 0); TF - nodrop
//			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
//		}
//	}

	@Override
	@Deprecated
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

//	@Override
//	@Deprecated
//	public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
//		if (!(entityIn instanceof EntityBoat)) {
//			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
//		}
//	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		super.onEntityCollision(state, worldIn, pos, entityIn);

		if (entityIn instanceof BoatEntity) {
			worldIn.destroyBlock(new BlockPos(pos), true);
		}
	}

	//TODO: Move to client
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
