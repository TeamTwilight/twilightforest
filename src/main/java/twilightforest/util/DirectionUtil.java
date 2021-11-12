package twilightforest.util;

import net.minecraft.core.Direction;

public final class DirectionUtil {
    public static Direction horizontalOrElse(Direction horizontal, Direction orElse) {
        return horizontal.getAxis().isHorizontal() ? horizontal : horizontalOrElse(orElse, Direction.NORTH);
    }

    private DirectionUtil() {
    }
}
