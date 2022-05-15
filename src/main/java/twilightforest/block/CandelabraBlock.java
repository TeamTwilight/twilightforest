package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

// The code is flexible to allow colors but I'm not sure if they'd look good on Candelabra
public class CandelabraBlock extends AbstractLightableBlock {
    public static final BooleanProperty ON_WALL = BooleanProperty.create("on_wall");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape CANDLES_NORTH = Shapes.or(Block.box(1, 7, 2, 15, 15, 6), Block.box(1, 1, 3.5, 15, 7,4.5), Block.box(7.5, 1, 1, 8.5, 7,7), Block.box(6, 2, 0, 10, 6, 1));
    public static final VoxelShape CANDLES_SOUTH = Shapes.or(Block.box(1, 7, 10, 15, 15, 14), Block.box(1, 1, 11.5, 15, 7,12.5), Block.box(7.5, 1, 9, 8.5, 7,15), Block.box(6, 2, 15, 10, 6, 16));

    public static final VoxelShape CANDLES_WEST = Shapes.or(Block.box(2, 7, 1, 6, 15, 15), Block.box(3.5, 1, 1, 4.5, 7,15), Block.box(1, 1, 7.5, 7, 7,8.5), Block.box(0, 2, 6, 1, 6, 10));
    public static final VoxelShape CANDLES_EAST = Shapes.or(Block.box(10, 7, 1, 14, 15, 15), Block.box(11.5, 1, 1, 12.5, 7,15), Block.box(9, 1, 7.5, 15, 7,8.5), Block.box(15, 2, 6, 16, 6, 10));

    public static final VoxelShape CANDLES_X = Shapes.or(Block.box(6, 7, 1, 10, 15, 15), Block.box(7.5, 1, 1, 8.5, 7,15), Block.box(5, 1, 7.5, 11, 7,8.5), Block.box(6, 0, 6, 10, 1, 10));
    public static final VoxelShape CANDLES_Z = Shapes.or(Block.box(1, 7, 6, 15, 15, 10), Block.box(1, 1, 7.5, 15, 7,8.5), Block.box(7.5, 1, 5, 8.5, 7,11), Block.box(6, 0, 6, 10, 1, 10));

    public static final List<Vec3> NORTH_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.25D), new Vec3(0.5D, 0.875D, 0.25D), new Vec3(0.8125D, 0.875D, 0.25D));
    public static final List<Vec3> SOUTH_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.75D), new Vec3(0.5D, 0.875D, 0.75D), new Vec3(0.8125D, 0.875D, 0.75D));

    public static final List<Vec3> WEST_OFFSETS = List.of(new Vec3(0.25D, 0.875D, 0.1875D), new Vec3(0.25D, 0.875D, 0.5D), new Vec3(0.25D, 0.875D, 0.8125D));
    public static final List<Vec3> EAST_OFFSETS = List.of(new Vec3(0.75D, 0.875D, 0.1875D), new Vec3(0.75D, 0.875D, 0.5D), new Vec3(0.75D, 0.875D, 0.8125D));

    public static final List<Vec3> X_OFFSETS = List.of(new Vec3(0.5D, 0.875D, 0.1875D), new Vec3(0.5D, 0.875D, 0.5D), new Vec3(0.5D, 0.875D, 0.8125D));
    public static final List<Vec3> Z_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.5D), new Vec3(0.5D, 0.875D, 0.5D), new Vec3(0.8125D, 0.875D, 0.5D));

    protected CandelabraBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ON_WALL, false).setValue(LIGHTING, Lighting.NONE));
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(ON_WALL)) {
            return switch (state.getValue(FACING)) {
                case SOUTH -> SOUTH_OFFSETS;
                case WEST -> WEST_OFFSETS;
                case EAST -> EAST_OFFSETS;
                default -> NORTH_OFFSETS;
            };
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_OFFSETS : Z_OFFSETS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(ON_WALL)) {
            return switch (state.getValue(FACING)) {
                case SOUTH -> CANDLES_SOUTH;
                case WEST -> CANDLES_WEST;
                case EAST -> CANDLES_EAST;
                default -> CANDLES_NORTH;
            };
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? CANDLES_X : CANDLES_Z;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickDirection = context.getClickedFace();
        boolean onBottomBlock = clickDirection == Direction.UP;
        Direction[] placements = context.getNearestLookingDirections();
        BlockPos placePos = context.getClickedPos();
        Level level = context.getLevel();

        // If placer is clicking the bottom block, then we want to test for the bottom block first
        //  before we cycle the walls for possible placements
        // Otherwise we test wall placements before testing the bottom block
        if (onBottomBlock) {
            if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false);

            for (Direction nextSide : placements)
                if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
                    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true);
        } else {
            for (Direction nextSide : placements)
                if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
                    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true);

            if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false);
        }

        // Fail
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, ON_WALL, LIGHTING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return canSurvive(levelReader, pos, state.getValue(ON_WALL), state.getValue(FACING));
    }

    public static boolean canSurvive(LevelReader levelReader, BlockPos pos, boolean onWall, Direction facing) {
        return canSupportCenter(levelReader, onWall ? pos.relative(facing) : pos.below(), Direction.UP);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        boolean ominous = state.getValue(LIGHTING) == Lighting.OMINOUS;
        if (state.getValue(LIGHTING) != Lighting.NONE) {
            this.getParticleOffsets(state, level, pos).forEach(vec3 -> addParticlesAndSound(level, pos, vec3.x, vec3.y, vec3.z, rand, ominous));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }
}
