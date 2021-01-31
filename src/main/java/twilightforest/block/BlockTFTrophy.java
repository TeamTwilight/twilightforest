package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import twilightforest.enums.BossVariant;

//[VanillaCopy] of SkullBlock but we add a shape for the ur-ghast and we use BossVariant instead of ISkullType
public class BlockTFTrophy extends BlockTFAbstractTrophy {

	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_0_15;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	protected static final VoxelShape GHAST_SHAPE = Block.makeCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);

	private final BossVariant variant;

	public BlockTFTrophy(BossVariant variant) {
		super(variant, Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F)); // TODO: Placeholder variable
		this.variant = variant;
		setDefaultState(stateContainer.getBaseState().with(BlockTFTrophy.ROTATION, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (((BlockTFAbstractTrophy) state.getBlock()).getVariant() == BossVariant.UR_GHAST) {
			return GHAST_SHAPE;
		}
			return SHAPE;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor(context.getPlacementYaw() * 16.0F / 360.0F + 0.5D) & 15));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(ROTATION, Integer.valueOf(rot.rotate(state.get(ROTATION), 16)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.with(ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(state.get(ROTATION), 16)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ROTATION);
	}
}
