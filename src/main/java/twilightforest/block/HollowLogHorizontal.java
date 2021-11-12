package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraftforge.common.ToolActions;
import twilightforest.enums.HollowLogVariants;
import twilightforest.util.AxisUtil;

import javax.annotation.Nullable;

public class HollowLogHorizontal extends Block implements WaterloggedBlock {
    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<HollowLogVariants.Horizontal> VARIANT = EnumProperty.create("variant", HollowLogVariants.Horizontal.class);

    private static final VoxelShape HOLLOW_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 2, 2, 16, 14, 14), BooleanOp.ONLY_FIRST);
    private static final VoxelShape HOLLOW_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(2, 2, 0, 14, 14, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape CARPET_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 3, 2, 16, 14, 14), BooleanOp.ONLY_FIRST);
    private static final VoxelShape CARPET_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(2, 3, 0, 14, 14, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape COLLISION_SHAPE_X = Shapes.join(Shapes.block(), Block.box(0, 1, 1, 16, 15, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape COLLISION_SHAPE_Z = Shapes.join(Shapes.block(), Block.box(1, 1, 0, 15, 15, 16), BooleanOp.ONLY_FIRST);

    public HollowLogHorizontal(Properties props) {
        super(props);

        this.registerDefaultState(this.stateDefinition.any().setValue(HORIZONTAL_AXIS, Direction.Axis.X).setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // The player is intentionally allowed to clip through the moss inside the log
        return switch (state.getValue(HORIZONTAL_AXIS)) {
            case X -> COLLISION_SHAPE_X;
            case Z -> COLLISION_SHAPE_Z;
            default -> Shapes.empty(); // Should never be reached
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HORIZONTAL_AXIS, VARIANT));
    }

    @Override
    public boolean isStateWaterlogged(BlockState state) {
        return state.getValue(VARIANT) == HollowLogVariants.Horizontal.WATERLOGGED;
    }

    @Override
    public BlockState setWaterlog(BlockState prior, boolean doWater) {
        return prior.setValue(VARIANT, doWater ? HollowLogVariants.Horizontal.WATERLOGGED : HollowLogVariants.Horizontal.EMPTY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(HORIZONTAL_AXIS, context.getClickedFace().getAxis()).setValue(VARIANT, context.getLevel().getBlockState(context.getClickedPos()).getFluidState().getType() == Fluids.WATER ? HollowLogVariants.Horizontal.WATERLOGGED : HollowLogVariants.Horizontal.EMPTY);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(VARIANT) == HollowLogVariants.Horizontal.WATERLOGGED ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (this.isStateWaterlogged(state)) {
            level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction.Axis stateAxis = state.getValue(HORIZONTAL_AXIS);
        if (!isInside(hit, stateAxis, pos)) return super.use(state, level, pos, player, hand, hit);

        ItemStack stack = player.getItemInHand(hand);

        HollowLogVariants.Horizontal variant = state.getValue(VARIANT);

        if (stack.is(TFBlocks.MOSS_PATCH.get().asItem())) {
            if (canChangeVariant(variant, level, pos, stateAxis)) {
                level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.MOSS), 3);
                level.playSound(null, pos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) stack.shrink(1);

                player.swing(hand);

                return InteractionResult.CONSUME;
            }
        } else if (stack.is(Blocks.GRASS.asItem())) {
            if (variant == HollowLogVariants.Horizontal.MOSS) {
                level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS), 3);
                level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) stack.shrink(1);

                player.swing(hand);

                return InteractionResult.CONSUME;
            }
        } else if (stack.is(Items.SNOWBALL)) {
            if (canChangeVariant(variant, level, pos, stateAxis)) {
                level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.SNOW), 3);
                level.playSound(null, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) stack.shrink(1);

                player.swing(hand);

                return InteractionResult.CONSUME;
            }
        } else if (stack.canPerformAction(ToolActions.SHOVEL_DIG)) {
            if (variant == HollowLogVariants.Horizontal.SNOW) {
                level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY), 3);
                level.playSound(null, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Items.SNOWBALL)));
                }

                player.swing(hand);

                return InteractionResult.CONSUME;
            }
        } else if (stack.canPerformAction(ToolActions.SHEARS_HARVEST)) {
            if (variant == HollowLogVariants.Horizontal.MOSS || variant == HollowLogVariants.Horizontal.MOSS_AND_GRASS) {
                level.setBlock(pos, state.setValue(VARIANT, HollowLogVariants.Horizontal.EMPTY), 3);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(TFBlocks.MOSS_PATCH.get())));
                    if (variant == HollowLogVariants.Horizontal.MOSS_AND_GRASS)
                        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Blocks.GRASS)));
                }

                player.swing(hand);

                return InteractionResult.CONSUME;
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }

    private static boolean canChangeVariant(HollowLogVariants.Horizontal variant, Level level, BlockPos pos, Direction.Axis axis) {
        // Empty Log, or the Log has water, and will be filled by water
        return variant == HollowLogVariants.Horizontal.EMPTY || (variant == HollowLogVariants.Horizontal.WATERLOGGED && level.getFluidState(pos.relative(AxisUtil.getAxisDirectionNegative(axis))).getType() != Fluids.WATER && level.getFluidState(pos.relative(AxisUtil.getAxisDirectionPositive(axis))).getType() != Fluids.WATER);
    }

    private static boolean isInside(HitResult result, Direction.Axis axis, BlockPos pos) {
        //if (!result.isInside()) return false;

        Vec3 vec = result.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

        if (0.124 > vec.y || vec.y > 0.876) return false;
        // 2vox/16vox < y < 14vox/16vox

        return switch (axis) {
            case X -> (0.124 <= vec.z && vec.z <= 0.876);
            case Z -> (0.124 <= vec.x && vec.x <= 0.876);
            default -> false;
        };
    }
}
