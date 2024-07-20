package twilightforest.util;

import net.minecraft.core.Direction;
import twilightforest.beans.Component;

@Component
public class DirectionUtil {

	public Direction horizontalOrElse(Direction horizontal, Direction orElse) {
		return horizontal.getAxis().isHorizontal() ? horizontal : horizontalOrElse(orElse, Direction.NORTH);
	}

}
