package twilightforest.util.iterators;

import org.joml.Vector2i;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class XZQuadrantIteratorTests {

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
	public void centerOnly() {
		int radius = 3;
		for (int y = -radius; y <= radius; y++) {
			for (int x = -radius; x <= radius; x++) {
				XZQuadrantIterator<Vector2i> xzQuadrantIterator = new XZQuadrantIterator<>(x, y, false, 0, 1, Vector2i::new);

				Vector2i next = xzQuadrantIterator.next();
				assertSame(x, next.x);
				assertSame(y, next.y);

				assertEmpty(xzQuadrantIterator);
			}
		}
	}

	@Test
	public void skippedCenter() {
		XZQuadrantIterator<Vector2i> xzQuadrantIterator = new XZQuadrantIterator<>(0, 0, true, 0, 1, Vector2i::new);

		assertEmpty(xzQuadrantIterator);
	}

	@Test
	public void parity() {
		List<Vector2i> result = StreamSupport.stream(new XZQuadrantIterator<>(0, 0, false, 3, 1, Vector2i::new).spliterator(), false).toList();

		assertEquals(parityList, result);
	}

	private void assertEmpty(XZQuadrantIterator<Vector2i> xzQuadrantIterator) {
		assertFalse(xzQuadrantIterator.hasNext());

		assertEquals(0, StreamSupport.stream(xzQuadrantIterator.spliterator(), false).count());
	}
}
