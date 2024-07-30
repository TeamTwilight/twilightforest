package twilightforest.util.iterators;

import org.joml.Vector2i;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XZQuadrantIteratorTests {
	@Test
	public void testCenterOnly() {
		int radius = 3;
		for (int y = -radius; y <= radius; y++) {
			for (int x = -radius; x <= radius; x++) {
				XZQuadrantIterator<Vector2i> xzQuadrantIterator = new XZQuadrantIterator<>(x, y, false, 0, 1, Vector2i::new);

				Vector2i next = xzQuadrantIterator.next();
				assertSame(x, next.x);
				assertSame(y, next.y);

				this.assertEOI(xzQuadrantIterator);
			}
		}
	}

	@Test
	public void testSkippedCenter() {
		XZQuadrantIterator<Vector2i> xzQuadrantIterator = new XZQuadrantIterator<>(0, 0, true, 0, 1, Vector2i::new);

		this.assertEOI(xzQuadrantIterator);
	}

	private final List<Vector2i> parityList = List.of(
		new Vector2i(0, 0),
		new Vector2i(1, 0), new Vector2i(0, -1), new Vector2i(-1, 0), new Vector2i(0, 1),
		new Vector2i(2, 0), new Vector2i(0, -2), new Vector2i(-2, 0), new Vector2i(0, 2),
		new Vector2i(3, 0), new Vector2i(0, -3), new Vector2i(-3, 0), new Vector2i(0, 3),
		new Vector2i(1, -1), new Vector2i(-1, -1), new Vector2i(-1, 1), new Vector2i(1, 1),
		new Vector2i(2, -1), new Vector2i(-1, -2), new Vector2i(-2, 1), new Vector2i(1, 2),
		new Vector2i(3, -1), new Vector2i(-1, -3), new Vector2i(-3, 1), new Vector2i(1, 3),
		new Vector2i(1, -2), new Vector2i(-2, -1), new Vector2i(-1, 2), new Vector2i(2, 1),
		new Vector2i(2, -2), new Vector2i(-2, -2), new Vector2i(-2, 2), new Vector2i(2, 2),
		new Vector2i(3, -2), new Vector2i(-2, -3), new Vector2i(-3, 2), new Vector2i(2, 3)
	);
	@Test
	public void testParity() {
		XZQuadrantIterator<Vector2i> xzQuadrantIterator = new XZQuadrantIterator<>(0, 0, false, 3, 1, Vector2i::new);

		List<Vector2i> positions = new ArrayList<>();
		for (Vector2i position : xzQuadrantIterator) {
			positions.add(position);
			//System.out.println("new Vector2i(" + position.x + ", " + position.y + ")");
		}

		assertArrayEquals(this.parityList.toArray(Vector2i[]::new), positions.toArray(Vector2i[]::new));
	}

	private void assertEOI(XZQuadrantIterator<Vector2i> xzQuadrantIterator) {
		assertFalse(xzQuadrantIterator.hasNext());

		List<Vector2i> overflow = new ArrayList<>();
		for (Vector2i excess : xzQuadrantIterator) {
			overflow.add(excess);
		}
		assertEquals(0, overflow.size()); // The above loop should never run
	}
}
