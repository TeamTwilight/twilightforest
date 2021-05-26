package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import twilightforest.enums.Diagonals;

import javax.annotation.Nullable;

public class BlockTFSpiralBrick extends Block {

    public static final EnumProperty<Diagonals> DIAGONAL = EnumProperty.create("diagonal", Diagonals.class);
    public static final EnumProperty<Direction.Axis> AXIS_FACING = EnumProperty.create("axis", Direction.Axis.class);

    public BlockTFSpiralBrick() {
        super(Properties.create(Material.ROCK, MaterialColor.STONE).setRequiresTool().hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(DIAGONAL, Diagonals.TOP_RIGHT).with(AXIS_FACING, Direction.Axis.X));
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AXIS_FACING, DIAGONAL);
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return super.getLightValue(state, world, pos);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite()));

		if (!context.getPlayer().isSneaking() && context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite())).getBlock() instanceof BlockTFSpiralBrick) {
			Direction.Axis axis = state.get(AXIS_FACING);

			return super.getStateForPlacement(context)
					.with(AXIS_FACING, axis)
					.with(DIAGONAL, Diagonals.mirror(state.get(DIAGONAL), context.getFace().getAxis() == Direction.Axis.X ? Mirror.LEFT_RIGHT : Mirror.FRONT_BACK));
		}

		//Direction playerFacing = Direction.getDirectionFromEntityLiving(pos, placer);
		Direction playerFacing = context.getNearestLookingDirection().getOpposite();

		return super.getStateForPlacement(context)
				.with(AXIS_FACING, playerFacing.getAxis())
				.with(DIAGONAL, getDiagonalFromPlayerPlacement(context.getPlayer(), context.getFace()));
	}

    private static Diagonals getDiagonalFromPlayerPlacement(LivingEntity placer, Direction facing) {
        int angleX = (int) ((placer.rotationPitch + 180f) / 180f) & 1;
        int angleY = (int) ((placer.rotationYaw + 180f) / 90f) & 3;

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
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rot) {
		if (rot == Rotation.NONE) return state;

		Direction.Axis axis = state.get(AXIS_FACING);

		if (axis == Direction.Axis.Y) {
			return state.with(DIAGONAL, Diagonals.rotate(state.get(DIAGONAL), rot));
		} else {
			if (rot == Rotation.CLOCKWISE_180 || (axis == Direction.Axis.X && rot == Rotation.COUNTERCLOCKWISE_90) || (axis == Direction.Axis.Z && rot == Rotation.CLOCKWISE_90))
				state = state.with(DIAGONAL, Diagonals.mirror(state.get(DIAGONAL), Mirror.LEFT_RIGHT));

			return rot.ordinal() % 2 == 0 ? state : state.with(AXIS_FACING, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		}
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.with(DIAGONAL, Diagonals.mirrorOn(state.get(AXIS_FACING), state.get(DIAGONAL), mirrorIn));
	}

}
