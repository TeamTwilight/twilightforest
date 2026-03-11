package twilightforest.util;


import net.minecraft.core.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class DirectionUtilTests {

	private DirectionUtil instance;

	@BeforeEach
	public void setup() {
		instance = new DirectionUtil();
	}

	@Test
	public void horizontalOrElse() {
		assertSame(Direction.EAST, instance.horizontalOrElse(Direction.EAST, Direction.NORTH));
		assertSame(Direction.WEST, instance.horizontalOrElse(Direction.WEST, Direction.NORTH));
		assertSame(Direction.NORTH, instance.horizontalOrElse(Direction.NORTH, Direction.SOUTH));
		assertSame(Direction.SOUTH, instance.horizontalOrElse(Direction.SOUTH, Direction.NORTH));

		assertSame(Direction.EAST, instance.horizontalOrElse(Direction.UP, Direction.EAST));
		assertSame(Direction.EAST, instance.horizontalOrElse(Direction.DOWN, Direction.EAST));

		assertSame(Direction.NORTH, instance.horizontalOrElse(Direction.UP, Direction.DOWN));
		assertSame(Direction.NORTH, instance.horizontalOrElse(Direction.DOWN, Direction.UP));
	}

}
