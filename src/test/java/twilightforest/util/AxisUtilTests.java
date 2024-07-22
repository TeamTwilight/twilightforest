package twilightforest.util;

import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AxisUtilTests {

	@Test
	public void getAxisDirectionPositiveX() {
		assertEquals(Direction.EAST, AxisUtil.getAxisDirectionPositive(Direction.Axis.X));
	}

	@Test
	public void getAxisDirectionPositiveY() {
		assertEquals(Direction.UP, AxisUtil.getAxisDirectionPositive(Direction.Axis.Y));
	}

	@Test
	public void getAxisDirectionPositiveZ() {
		assertEquals(Direction.SOUTH, AxisUtil.getAxisDirectionPositive(Direction.Axis.Z));
	}

	@Test
	public void getAxisDirectionNegativeX() {
		assertEquals(Direction.WEST, AxisUtil.getAxisDirectionNegative(Direction.Axis.X));
	}

	@Test
	public void getAxisDirectionNegativeY() {
		assertEquals(Direction.DOWN, AxisUtil.getAxisDirectionNegative(Direction.Axis.Y));
	}

	@Test
	public void getAxisDirectionNegativeZ() {
		assertEquals(Direction.NORTH, AxisUtil.getAxisDirectionNegative(Direction.Axis.Z));
	}

}
