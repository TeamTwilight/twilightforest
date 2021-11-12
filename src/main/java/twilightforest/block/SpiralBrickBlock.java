package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import twilightforest.enums.Diagonals;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SpiralBrickBlock extends Block {

    public static final EnumProperty<Diagonals> DIAGONAL = EnumProperty.create("diagonal", Diagonals.class);
    public static final EnumProperty<Direction.Axis> AXIS_FACING = EnumProperty.create("axis", Direction.Axis.class);

    public SpiralBrickBlock() {
        super(Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(DIAGONAL, Diagonals.TOP_RIGHT).setValue(AXIS_FACING, Direction.Axis.X));
    }

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS_FACING, DIAGONAL);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));

		if (!context.getPlayer().isShiftKeyDown() && context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite())).getBlock() instanceof SpiralBrickBlock) {
			Direction.Axis axis = state.getValue(AXIS_FACING);

			return super.getStateForPlacement(context)
					.setValue(AXIS_FACING, axis)
					.setValue(DIAGONAL, Diagonals.mirror(state.getValue(DIAGONAL), context.getClickedFace().getAxis() == Direction.Axis.X ? Mirror.LEFT_RIGHT : Mirror.FRONT_BACK));
		}

		//Direction playerFacing = Direction.getDirectionFromEntityLiving(pos, placer);
		Direction playerFacing = context.getNearestLookingDirection().getOpposite();

		return super.getStateForPlacement(context)
				.setValue(AXIS_FACING, playerFacing.getAxis())
				.setValue(DIAGONAL, getDiagonalFromPlayerPlacement(context.getPlayer(), context.getClickedFace()));
	}

    private static Diagonals getDiagonalFromPlayerPlacement(LivingEntity placer, Direction facing) {
        int angleX = (int) ((placer.getXRot() + 180f) / 180f) & 1;
        int angleY = (int) ((placer.getYRot() + 180f) / 90f) & 3;

        switch (facing) {
            case DOWN:
            case UP:
                switch (angleY) {
                    default: return Diagonals.TOP_RIGHT; // NORTH EAST
                    case 1: return Diagonals.BOTTOM_RIGHT; // SOUTH EAST
                    case 2: return Diagonals.BOTTOM_LEFT; // SOUTH WEST
                    case 3: return Diagonals.TOP_LEFT; // NORTH WEST
                }

                /*
                NORTH
                3   0

                2   1
                */

            case NORTH:
                return Diagonals.getDiagonalFromUpDownLeftRight(  isEast(angleY), angleX < 1);
            case SOUTH:
                return Diagonals.getDiagonalFromUpDownLeftRight( !isEast(angleY), angleX < 1);
            case EAST:
                return Diagonals.getDiagonalFromUpDownLeftRight( isNorth(angleY), angleX < 1);
            case WEST:
                return Diagonals.getDiagonalFromUpDownLeftRight(!isNorth(angleY), angleX < 1);
        }

        return Diagonals.TOP_RIGHT;
    }

    private static boolean isNorth(int intIn) {
        return intIn == 0 || intIn == 3;
    }

    private static boolean isEast(int intIn) {
        return intIn == 0 || intIn == 1;
    }

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (rot == Rotation.NONE) return state;

		Direction.Axis axis = state.getValue(AXIS_FACING);

		if (axis == Direction.Axis.Y) {
			return state.setValue(DIAGONAL, Diagonals.rotate(state.getValue(DIAGONAL), rot));
		} else {
			if (rot == Rotation.CLOCKWISE_180 || (axis == Direction.Axis.X && rot == Rotation.COUNTERCLOCKWISE_90) || (axis == Direction.Axis.Z && rot == Rotation.CLOCKWISE_90))
				state = state.setValue(DIAGONAL, Diagonals.mirror(state.getValue(DIAGONAL), Mirror.LEFT_RIGHT));

			return rot.ordinal() % 2 == 0 ? state : state.setValue(AXIS_FACING, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		}
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.setValue(DIAGONAL, Diagonals.mirrorOn(state.getValue(AXIS_FACING), state.getValue(DIAGONAL), mirrorIn));
	}

}
