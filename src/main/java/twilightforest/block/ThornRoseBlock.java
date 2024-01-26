package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.init.TFBlocks;

public class ThornRoseBlock extends BushBlock {

	public static final MapCodec<ThornRoseBlock> CODEC = simpleCodec(ThornRoseBlock::new);
	private static final float RADIUS = 0.4F;
	private static final VoxelShape AABB = Shapes.create(new AABB(0.5F - RADIUS, 0.5F - RADIUS, 0.5F - RADIUS, 0.5F + RADIUS, .5F + RADIUS, 0.5F + RADIUS));

	public ThornRoseBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(DirectionalBlock.FACING, Direction.UP));
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos.relative(state.getValue(DirectionalBlock.FACING).getOpposite()));
		return blockstate.is(TFBlocks.BROWN_THORNS) || blockstate.is(TFBlocks.GREEN_THORNS) || blockstate.isFaceSturdy(level, pos, state.getValue(DirectionalBlock.FACING));
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DirectionalBlock.FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(DirectionalBlock.FACING, context.getClickedFace());
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(DirectionalBlock.FACING, rotation.rotate(state.getValue(DirectionalBlock.FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(DirectionalBlock.FACING)));
	}
}
