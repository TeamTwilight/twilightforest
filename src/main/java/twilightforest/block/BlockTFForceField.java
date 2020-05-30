package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockTFForceField extends BlockTFConnectableRotatedPillar {

	BlockTFForceField(Block.Properties props) {
		super(props, 2);
	}

	@Override
	protected AxisAlignedBB getSidedAABBStraight(Direction facing, Direction.Axis axis) {
		return makeQuickAABB(
				facing == Direction.EAST  || axis == Direction.Axis.X ? 16 : this.boundingBoxWidthLower,
				facing == Direction.UP    || axis == Direction.Axis.Y ? 16 : this.boundingBoxWidthLower,
				facing == Direction.SOUTH || axis == Direction.Axis.Z ? 16 : this.boundingBoxWidthLower,
				facing == Direction.WEST  || axis == Direction.Axis.X ?  0 : this.boundingBoxWidthUpper,
				facing == Direction.DOWN  || axis == Direction.Axis.Y ?  0 : this.boundingBoxWidthUpper,
				facing == Direction.NORTH || axis == Direction.Axis.Z ?  0 : this.boundingBoxWidthUpper);
	}

//	@Override
//	protected boolean canConnectTo(BlockState state, BlockState otherState, IBlockAccess world, BlockPos pos, Direction connectTo) {
//		BlockFaceShape blockFaceShape = otherState.getBlockFaceShape(world, pos, connectTo);
//
//		return blockFaceShape == BlockFaceShape.SOLID
//				|| blockFaceShape == BlockFaceShape.MIDDLE_POLE_THIN
//				|| super.canConnectTo(state, otherState, world, pos, connectTo);
//	}

	//TODO: Check this
//	@Override
//	public boolean isSolid(BlockState state) {
//		return false;
//	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return false; // TODO: ???
	}

	//TODO: Move to client
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.TRANSLUCENT;
//	}

	//TODO: Removed. Check this
//	@Override
//	@Deprecated
//	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
//		return world.getBlockState(pos.offset(face)).getBlock() != this && Block.shouldSideBeRendered(state, world, pos, face);
//	}

}
