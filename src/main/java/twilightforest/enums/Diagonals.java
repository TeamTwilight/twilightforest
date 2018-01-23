package twilightforest.enums;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import java.util.Locale;

public enum Diagonals implements IStringSerializable {
    TOP_RIGHT   ((x, rX) -> rX - x, (y, rY) -> y     , true , false),
    BOTTOM_RIGHT((x, rX) -> rX - x, (y, rY) -> rY - y, false, false),
    BOTTOM_LEFT ((x, rX) -> x     , (y, rY) -> rY - y, false, true ),
    TOP_LEFT    ((x, rX) -> x     , (y, rY) -> y     , true , true );

    public final Inversion operationX;
    public final Inversion operationY;
    private final boolean isTop;
    private final boolean isLeft;

    Diagonals(Inversion operationX, Inversion operationY, boolean isTop, boolean isLeft) {
        this.operationX = operationX;
        this.operationY = operationY;
        this.isTop = isTop;
        this.isLeft = isLeft;
    }

    public static Diagonals rotate(Diagonals diagonal, Rotation rotation) {
        return Diagonals.values()[(diagonal.ordinal() + rotation.ordinal()) % 4];
    }

    public static Diagonals mirrorOn(EnumFacing.Axis axis, Diagonals diagonal, Mirror mirror) {
        switch (axis) {
            case X:
                return mirrorOnX(diagonal, mirror);
            case Z:
                return mirrorOnZ(diagonal, mirror);
            default:
                return mirrorDefault(diagonal, mirror);
        }
    }

    public static Diagonals mirrorOnX(Diagonals diagonal, Mirror mirror) {
        switch (mirror) {
            case FRONT_BACK:
                switch (diagonal) {
                    case TOP_RIGHT:
                        return BOTTOM_RIGHT;
                    case BOTTOM_RIGHT:
                        return TOP_RIGHT;
                    case BOTTOM_LEFT:
                        return TOP_LEFT;
                    case TOP_LEFT:
                        return BOTTOM_LEFT;
                }
            default:
                return diagonal;
        }
    }

    public static Diagonals mirrorLeftRight(Diagonals diagonal) {
        switch (diagonal) {
            case TOP_RIGHT:
                return TOP_LEFT;
            case BOTTOM_RIGHT:
                return BOTTOM_LEFT;
            case BOTTOM_LEFT:
                return BOTTOM_RIGHT;
            case TOP_LEFT:
                return TOP_RIGHT;
        }

        return diagonal;
    }

    public static Diagonals mirrorUpDown(Diagonals diagonal) {
        switch (diagonal) {
            case TOP_RIGHT:
                return BOTTOM_RIGHT;
            case BOTTOM_RIGHT:
                return TOP_RIGHT;
            case BOTTOM_LEFT:
                return TOP_LEFT;
            case TOP_LEFT:
                return BOTTOM_LEFT;
        }

        return diagonal;
    }

    public static Diagonals mirrorDefault(Diagonals diagonal, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return mirrorLeftRight(diagonal);
            case FRONT_BACK:
                return mirrorUpDown(diagonal);
            default:
                return diagonal;
        }
    }

    public static Diagonals mirrorOnZ(Diagonals diagonal, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                switch (diagonal) {
                    case TOP_RIGHT:
                        return TOP_LEFT;
                    case BOTTOM_RIGHT:
                        return BOTTOM_LEFT;
                    case BOTTOM_LEFT:
                        return BOTTOM_RIGHT;
                    case TOP_LEFT:
                        return TOP_RIGHT;
                }
            default:
                return diagonal;
        }
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public interface Inversion {
        int convert(int numberIn, int range);
    }
}