package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.AxisUtil;

public class HorizontalHollowLogBlock extends Block implements WaterloggedBlock {
	public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	public static final EnumProperty<HollowLogVariants.Horizontal> VARIANT = EnumProperty.create("variant", HollowLogVariants.Horizontal.class);

	private static final VoxelShape HOLLOW_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 2, 2, 16, 14, 14), BooleanOp.ONLY_FIRST);
	private static final VoxelShape HOLLOW_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(2, 2, 0, 14, 14, 16), BooleanOp.ONLY_FIRST);
	private static final VoxelShape CARPET_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 3, 2, 16, 14, 14), BooleanOp.ONLY_FIRST);
	private static final VoxelShape CARPET_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(2, 3, 0, 14, 14, 16), BooleanOp.ONLY_FIRST);
	private static final VoxelShape COLLISION_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 1, 1, 16, 15, 15), BooleanOp.ONLY_FIRST);
	private static final VoxelShape COLLISION_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(1, 1, 0, 15, 15, 16), BooleanOp.ONLY_FIRST);

	public HorizontalHollowLogBlock(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_AXIS, Direction.Axis.X).setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY));
	}

	private static boolean canChangeVariant(HollowLogVariants.Horizontal variant, Level level, BlockPos pos, Direction.Axis axis) {
		// Empty Log, or the Log has water, and will be filled by water
		return variant == HollowLogVariants.Horizontal.EMPTY || (variant == HollowLogVariants.Horizontal.WATERLOGGED && level.getFluidState(pos.relative(AxisUtil.getAxisDirectionNegative(axis))).getType() != Fluids.WATER && level.getFluidState(pos.relative(AxisUtil.getAxisDirectionPositive(axis))).getType() != Fluids.WATER);
	}

	private static boolean isInside(HitResult result, Direction.Axis axis, BlockPos pos) {
		Vec3 vec = result.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

		if (0.124 > vec.y || vec.y > 0.876) return false;
		// 2vox/16vox < y < 14vox/16vox

		return switch (axis) {
			case X -> (0.124 <= vec.z && vec.z <= 0.876);
			case Z -> (0.124 <= vec.x && vec.x <= 0.876);
			default -> false;
		};
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(VARIANT)) {
			case EMPTY, WATERLOGGED -> switch (state.getValue(HORIZONTAL_AXIS)) {
				case X -> HOLLOW_SHAPE_X;
				case Z -> HOLLOW_SHAPE_Z;
				default -> Shapes.block(); // Should never be reached
			};
			case MOSS, MOSS_AND_GRASS, SNOW -> switch (state.getValue(HORIZONTAL_AXIS)) {
				case X -> CARPET_SHAPE_X;
				case Z -> CARPET_SHAPE_Z;
				default -> Shapes.block(); // Should never be reached
			};
		};
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		// The player is intentionally allowed to clip through the moss inside the log
		return switch (state.getValue(HORIZONTAL_AXIS)) {
			case X -> COLLISION_SHAPE_X;
			case Z -> COLLISION_SHAPE_Z;
			default -> Shapes.empty(); // Should never be reached
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_AXIS, VARIANT);
	}

	@Override
	public boolean isStateWaterlogged(BlockState state) {
		return state.getValue(VARIANT) == HollowLogVariants.Horizontal.WATERLOGGED;
	}

	@Override
	public BlockState setWaterlog(BlockState prior, boolean doWater) {
		return prior.setValue(VARIANT, doWater ? HollowLogVariants.Horizontal.WATERLOGGED : HollowLogVariants.Horizontal.EMPTY);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(HORIZONTAL_AXIS, context.getClickedFace().getAxis()).setValue(VARIANT, context.getLevel().getBlockState(context.getClickedPos()).getFluidState().getType() == Fluids.WATER ? HollowLogVariants.Horizontal.WATERLOGGED : HollowLogVariants.Horizontal.EMPTY);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(VARIANT) == HollowLogVariants.Horizontal.WATERLOGGED ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (this.isStateWaterlogged(state)) {
			access.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(reader));
		}

		return super.updateShape(state, reader, access, pos, direction, facingPos, facingState, random);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		Direction.Axis stateAxis = state.getValue(HORIZONTAL_AXIS);
		if (!isInside(hit, stateAxis, pos)) return super.useItemOn(stack, state, level, pos, player, hand, hit);

		HollowLogVariants.Horizontal variant = state.getValue(VARIANT);

		if (stack.is(TFBlocks.MOSS_PATCH.asItem())) {
			if (canChangeVariant(variant, level, pos, stateAxis)) {
				level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.MOSS), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);

				return InteractionResult.SUCCESS;
			}
		} else if (stack.is(Blocks.SHORT_GRASS.asItem())) {
			if (variant == HollowLogVariants.Horizontal.MOSS) {
				level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);

				return InteractionResult.SUCCESS;
			}
		} else if (stack.is(Items.SNOWBALL)) {
			if (canChangeVariant(variant, level, pos, stateAxis)) {
				level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.SNOW), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);

				return InteractionResult.SUCCESS;
			}
		} else if (stack.canPerformAction(ItemAbilities.SHOVEL_DIG)) {
			if (variant == HollowLogVariants.Horizontal.SNOW) {
				level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
				if (!player.isCreative()) {
					stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
					level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Items.SNOWBALL)));
				}

				return InteractionResult.SUCCESS;
			}
		} else if (stack.canPerformAction(ItemAbilities.SHEARS_HARVEST)) {
			if (variant == HollowLogVariants.Horizontal.MOSS || variant == HollowLogVariants.Horizontal.MOSS_AND_GRASS) {
				level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

				if (!player.isCreative()) {
					stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
					level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(TFBlocks.MOSS_PATCH.get())));
					if (variant == HollowLogVariants.Horizontal.MOSS_AND_GRASS)
						level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Blocks.SHORT_GRASS)));
				}

				return InteractionResult.SUCCESS;
			}
		}

		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		Direction.Axis stateAxis = state.getValue(HORIZONTAL_AXIS);
		if (!isInside(hit, stateAxis, pos)) return super.useWithoutItem(state, level, pos, player, hit);
		if (player.isSecondaryUseActive() && player.getDirection().getAxis().equals(stateAxis) &&
			player.blockPosition().getY() == pos.getY() && !player.isFallFlying() && player.position().distanceTo(Vec3.atBottomCenterOf(pos)) < 0.81D) {
			player.setPose(Pose.SWIMMING);
		}
		return super.useWithoutItem(state, level, pos, player, hit);
	}
}
