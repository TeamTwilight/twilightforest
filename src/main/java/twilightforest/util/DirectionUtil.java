package twilightforest.util;

import net.minecraft.core.Direction;
import twilightforest.beans.Component;

@Component
public class DirectionUtil {

	public Direction horizontalOrElse(Direction horizontal, Direction orElse) {
		return horizontal.getAxis().isHorizontal() ? horizontal : horizontalOrElse(orElse, Direction.NORTH);
	}

	public static Direction fromStringOrElse(String label, Direction orElse) {
		return switch (label) {
			case "up" -> Direction.UP;
			case "down" -> Direction.DOWN;
			case "north" -> Direction.NORTH;
			case "south" -> Direction.SOUTH;
			case "west" -> Direction.WEST;
			case "east" -> Direction.EAST;
			default -> orElse;
		};
	}
}
