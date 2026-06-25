package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.enums.HollowLogVariants;
import twilightforest.util.DirectionUtil;

@Configurable
public class VerticalHollowLogBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape HOLLOW_SHAPE = Shapes.join(Shapes.block(), Block.box(2, 0, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
	private static final VoxelShape COLLISION_SHAPE = Shapes.join(Shapes.block(), Block.box(1, 0, 1, 15, 16, 15), BooleanOp.ONLY_FIRST);

	@Autowired
	private DirectionUtil directionUtil;

	private final Holder<Block> climbable;

	public VerticalHollowLogBlock(Holder<Block> climbable, Properties properties) {
		super(properties);
		this.climbable = climbable;

		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	private static boolean isInside(HitResult result, BlockPos pos) {
		Vec3 vec = result.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

		return (0.124 <= vec.x() && vec.x() <= 0.876) && (0.124 <= vec.z() && vec.z() <= 0.876);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return HOLLOW_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(WATERLOGGED));
	}

	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!isInside(hit, pos)) return super.useItemOn(stack, state, level, pos, player, hand, hit);

		if (stack.is(Blocks.VINE.asItem())) {
			level.setBlock(pos, this.climbable.value().defaultBlockState().setValue(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.VINE).setValue(ClimbableHollowLogBlock.FACING, directionUtil.horizontalOrElse(hit.getDirection(), player.getDirection().getOpposite())), 3);
			HorizontalHollowLogBlock.playPlaceSound(Blocks.VINE.defaultBlockState(), level, pos, player);
			stack.consume(1, player);

			return InteractionResult.SUCCESS;

		} else if (stack.is(Blocks.LADDER.asItem())) {
			level.setBlock(pos, this.climbable.value().defaultBlockState().setValue(ClimbableHollowLogBlock.VARIANT, state.getValue(WATERLOGGED) ? HollowLogVariants.Climbable.LADDER_WATERLOGGED : HollowLogVariants.Climbable.LADDER).setValue(ClimbableHollowLogBlock.FACING, directionUtil.horizontalOrElse(hit.getDirection(), player.getDirection().getOpposite())), 3);
			HorizontalHollowLogBlock.playPlaceSound(Blocks.LADDER.defaultBlockState(), level, pos, player);
			stack.consume(1, player);

			return InteractionResult.SUCCESS;
		}

		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getFluidState().getType() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}
}
