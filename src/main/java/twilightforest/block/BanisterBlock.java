package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.enums.BanisterShape;

import java.util.List;

public class BanisterBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

	public static final MapCodec<BanisterBlock> CODEC = simpleCodec(BanisterBlock::new);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<BanisterShape> SHAPE = EnumProperty.create("shape", BanisterShape.class);
	public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");

	public BanisterBlock(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(SHAPE, BanisterShape.TALL).setValue(EXTENDED, false).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();

		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(SHAPE, context.getLevel().getBlockState(pos.above()).is(BlockTagGenerator.BANISTERS) ? BanisterShape.CONNECTED : BanisterShape.TALL)
				.setValue(WATERLOGGED, context.getLevel().getFluidState(pos).getType() == Fluids.WATER);
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		boolean extended = state.getValue(EXTENDED);

		return switch (state.getValue(SHAPE)) {
			case SHORT -> {
				switch (state.getValue(BanisterBlock.FACING)) {
					case NORTH -> {
						yield extended ? NORTH_SHORT_EXTENDED : NORTH_SHORT;
					}
					case WEST -> {
						yield extended ? WEST_SHORT_EXTENDED : WEST_SHORT;
					}
					case EAST -> {
						yield extended ? EAST_SHORT_EXTENDED : EAST_SHORT;
					}
					default -> {
						yield extended ? SOUTH_SHORT_EXTENDED : SOUTH_SHORT;
					}
				}
			}
			case CONNECTED -> {
				switch (state.getValue(BanisterBlock.FACING)) {
					case NORTH -> {
						yield extended ? NORTH_CONNECTED_EXTENDED : NORTH_SUPPORTS_TALL;
					}
					case WEST -> {
						yield extended ? WEST_CONNECTED_EXTENDED : WEST_SUPPORTS_TALL;
					}
					case EAST -> {
						yield extended ? EAST_CONNECTED_EXTENDED : EAST_SUPPORTS_TALL;
					}
					default -> {
						yield extended ? SOUTH_CONNECTED_EXTENDED : SOUTH_SUPPORTS_TALL;
					}
				}
			}
			case TALL -> {
				switch (state.getValue(BanisterBlock.FACING)) {
					case NORTH -> {
						yield extended ? NORTH_TALL_EXTENDED : NORTH_TALL;
					}
					case WEST -> {
						yield extended ? WEST_TALL_EXTENDED : WEST_TALL;
					}
					case EAST -> {
						yield extended ? EAST_TALL_EXTENDED : EAST_TALL;
					}
					default -> {
						yield extended ? SOUTH_TALL_EXTENDED : SOUTH_TALL;
					}
				}
			}
		};
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("block.twilightforest.banister.cycle").withStyle(ChatFormatting.GRAY));
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob mob) {
		return BlockPathTypes.BLOCKED;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(SHAPE, EXTENDED, FACING, WATERLOGGED);
	}

	@Override
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack held = player.getItemInHand(hand);

		if (held.canPerformAction(ToolActions.AXE_WAX_OFF)) {
			BlockState newState = state.cycle(SHAPE);

			// If we reach BanisterShape.TALL it means we went a full cycle, so we'll also cycle the extension
			level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, newState.getValue(SHAPE) == BanisterShape.TALL ? newState.cycle(EXTENDED) : newState, 3);

			return InteractionResult.SUCCESS;
		}

		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 20;
	}

	// These extend upwards to 16 instead of 12 because they're used on both the Tall and Connected shapes
	private static final VoxelShape NORTH_SUPPORTS_TALL = Shapes.or(
			Block.box(2.5D, 0.0D, 0.0D, 5.5D, 16.0D, 3.0D),
			Block.box(10.5D, 0.0D, 0.0D, 13.5D, 16.0D, 3.0D)
	);
	private static final VoxelShape EAST_SUPPORTS_TALL = Shapes.or(
			Block.box(13.0D, 0.0D, 2.5D, 16.0D, 16.0D, 5.5D),
			Block.box(13.0D, 0.0D, 10.5D, 16.0D, 16.0D, 13.5D)
	);
	private static final VoxelShape SOUTH_SUPPORTS_TALL = Shapes.or(
			Block.box(2.5D, 0.0D, 13.0D, 5.5D, 16.0D, 16.0D),
			Block.box(10.5D, 0.0D, 13.0D, 13.5D, 16.0D, 16.0D)
	);
	private static final VoxelShape WEST_SUPPORTS_TALL = Shapes.or(
			Block.box(0.0D, 0.0D, 2.5D, 3.0D, 16.0D, 5.5D),
			Block.box(0.0D, 0.0D, 10.5D, 3.0D, 16.0D, 13.5D)
	);

	private static final VoxelShape NORTH_SUPPORTS_SHORT = Shapes.or(
			Block.box(2.5D, 0.0D, 0.0D, 5.5D, 4.0D, 3.0D),
			Block.box(10.5D, 0.0D, 0.0D, 13.5D, 4.0D, 3.0D)
	);
	private static final VoxelShape EAST_SUPPORTS_SHORT = Shapes.or(
			Block.box(13.0D, 0.0D, 2.5D, 16.0D, 4.0D, 5.5D),
			Block.box(13.0D, 0.0D, 10.5D, 16.0D, 4.0D, 13.5D)
	);
	private static final VoxelShape SOUTH_SUPPORTS_SHORT = Shapes.or(
			Block.box(2.5D, 0.0D, 13.0D, 5.5D, 4.0D, 16.0D),
			Block.box(10.5D, 0.0D, 13.0D, 13.5D, 4.0D, 16.0D)
	);
	private static final VoxelShape WEST_SUPPORTS_SHORT = Shapes.or(
			Block.box(0.0D, 0.0D, 2.5D, 3.0D, 4.0D, 5.5D),
			Block.box(0.0D, 0.0D, 10.5D, 3.0D, 4.0D, 13.5D)
	);

	private static final VoxelShape NORTH_EXTENSION = Shapes.or(
			Block.box(2.5D, -8.0D, 0.0D, 5.5D, 0.0D, 3.0D),
			Block.box(10.5D, -8.0D, 0.0D, 13.5D, 0.0D, 3.0D)
	);
	private static final VoxelShape EAST_EXTENSION = Shapes.or(
			Block.box(13.0D, -8.0D, 2.5D, 16.0D, 0.0D, 5.5D),
			Block.box(13.0D, -8.0D, 10.5D, 16.0D, 0.0D, 13.5D)
	);
	private static final VoxelShape SOUTH_EXTENSION = Shapes.or(
			Block.box(2.5D, -8.0D, 13.0D, 5.5D, 0.0D, 16.0D),
			Block.box(10.5D, -8.0D, 13.0D, 13.5D, 0.0D, 16.0D)
	);
	private static final VoxelShape WEST_EXTENSION = Shapes.or(
			Block.box(0.0D, -8.0D, 2.5D, 3.0D, 0.0D, 5.5D),
			Block.box(0.0D, -8.0D, 10.5D, 3.0D, 0.0D, 13.5D)
	);

	private static final VoxelShape NORTH_TALL = Shapes.or(Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 4.0D), NORTH_SUPPORTS_TALL);
	private static final VoxelShape EAST_TALL = Shapes.or(Block.box(12.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D), EAST_SUPPORTS_TALL);
	private static final VoxelShape SOUTH_TALL = Shapes.or(Block.box(0.0D, 12.0D, 12.0D, 16.0D, 16.0D, 16.0D), SOUTH_SUPPORTS_TALL);
	private static final VoxelShape WEST_TALL = Shapes.or(Block.box(0.0D, 12.0D, 0.0D, 4.0D, 16.0D, 16.0D), WEST_SUPPORTS_TALL);

	private static final VoxelShape NORTH_SHORT = Shapes.or(Block.box(0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 4.0D), NORTH_SUPPORTS_SHORT);
	private static final VoxelShape EAST_SHORT = Shapes.or(Block.box(12.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D), EAST_SUPPORTS_SHORT);
	private static final VoxelShape SOUTH_SHORT = Shapes.or(Block.box(0.0D, 4.0D, 12.0D, 16.0D, 8.0D, 16.0D), SOUTH_SUPPORTS_SHORT);
	private static final VoxelShape WEST_SHORT = Shapes.or(Block.box(0.0D, 4.0D, 0.0D, 4.0D, 8.0D, 16.0D), WEST_SUPPORTS_SHORT);

	private static final VoxelShape NORTH_TALL_EXTENDED = Shapes.or(NORTH_TALL, NORTH_EXTENSION);
	private static final VoxelShape EAST_TALL_EXTENDED = Shapes.or(EAST_TALL, EAST_EXTENSION);
	private static final VoxelShape SOUTH_TALL_EXTENDED = Shapes.or(SOUTH_TALL, SOUTH_EXTENSION);
	private static final VoxelShape WEST_TALL_EXTENDED = Shapes.or(WEST_TALL, WEST_EXTENSION);

	private static final VoxelShape NORTH_SHORT_EXTENDED = Shapes.or(NORTH_SHORT, NORTH_EXTENSION);
	private static final VoxelShape EAST_SHORT_EXTENDED = Shapes.or(EAST_SHORT, EAST_EXTENSION);
	private static final VoxelShape SOUTH_SHORT_EXTENDED = Shapes.or(SOUTH_SHORT, SOUTH_EXTENSION);
	private static final VoxelShape WEST_SHORT_EXTENDED = Shapes.or(WEST_SHORT, WEST_EXTENSION);

	private static final VoxelShape NORTH_CONNECTED_EXTENDED = Shapes.or(NORTH_SUPPORTS_TALL, NORTH_EXTENSION);
	private static final VoxelShape EAST_CONNECTED_EXTENDED = Shapes.or(EAST_SUPPORTS_TALL, EAST_EXTENSION);
	private static final VoxelShape SOUTH_CONNECTED_EXTENDED = Shapes.or(SOUTH_SUPPORTS_TALL, SOUTH_EXTENSION);
	private static final VoxelShape WEST_CONNECTED_EXTENDED = Shapes.or(WEST_SUPPORTS_TALL, WEST_EXTENSION);
}
