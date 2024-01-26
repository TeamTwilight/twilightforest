package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.enums.HollowLogVariants;
import twilightforest.util.DirectionUtil;

public class HollowLogVertical extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape HOLLOW_SHAPE = Shapes.join(Shapes.block(), Block.box(2, 0, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
	private static final VoxelShape COLLISION_SHAPE = Shapes.join(Shapes.block(), Block.box(1, 0, 1, 15, 16, 15), BooleanOp.ONLY_FIRST);

	private final DeferredHolder<Block, HollowLogClimbable> climbable;

	public HollowLogVertical(Properties properties, DeferredHolder<Block, HollowLogClimbable> climbable) {
		super(properties);
		this.climbable = climbable;

		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!isInside(hit, pos)) return super.use(state, level, pos, player, hand, hit);

		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(Blocks.VINE.asItem())) {
			level.setBlock(pos, this.climbable.get().defaultBlockState().setValue(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.VINE).setValue(HollowLogClimbable.FACING, DirectionUtil.horizontalOrElse(hit.getDirection(), player.getDirection().getOpposite())), 3);
			level.playSound(null, pos, SoundEvents.VINE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) stack.shrink(1);

			return InteractionResult.sidedSuccess(level.isClientSide());

		} else if (stack.is(Blocks.LADDER.asItem())) {
			level.setBlock(pos, this.climbable.get().defaultBlockState().setValue(HollowLogClimbable.VARIANT, state.getValue(WATERLOGGED) ? HollowLogVariants.Climbable.LADDER_WATERLOGGED : HollowLogVariants.Climbable.LADDER).setValue(HollowLogClimbable.FACING, DirectionUtil.horizontalOrElse(hit.getDirection(), player.getDirection().getOpposite())), 3);
			level.playSound(null, pos, SoundEvents.LADDER_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) stack.shrink(1);

			return InteractionResult.sidedSuccess(level.isClientSide());
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getFluidState().getType() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, neighborState, accessor, pos, neighborPos);
	}

	private static boolean isInside(HitResult result, BlockPos pos) {
		Vec3 vec = result.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

		return (0.124 <= vec.x() && vec.x() <= 0.876) && (0.124 <= vec.z() && vec.z() <= 0.876);
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 5;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 5;
	}
}
