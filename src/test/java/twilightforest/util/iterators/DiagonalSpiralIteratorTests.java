package twilightforest.util.iterators;

import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiagonalSpiralIteratorTests {

	@Test
	public void single() {
		DiagonalSpiralIterator spiralIterator = new DiagonalSpiralIterator(0);

		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO, spiralIterator.next());
		assertFalse(spiralIterator.hasNext());
	}

	@Test
	public void diagonalSpiralFromCenter() {
		DiagonalSpiralIterator spiralIterator = new DiagonalSpiralIterator(5);

		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO, spiralIterator.next());

		// Row 1
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(), spiralIterator.next());

		// Row 2
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(), spiralIterator.next());

		// Row 3
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(2), spiralIterator.next());

		// Row 4
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(3).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(3), spiralIterator.next());

		// Row 5
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(5), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(4).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(3).east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(5), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(4).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3).south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(5), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(4).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3).west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(4), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(5), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(4).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3).north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(4), spiralIterator.next());

		assertTrue(spiralIterator.hasNext()); // And so on
	}

	@Test
	public void diagonalSpiralFromCenterBounded5x5() {
		DiagonalSpiralIterator spiralIterator = new DiagonalSpiralIterator(2);

		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO, spiralIterator.next());

		// Row 1
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(), spiralIterator.next());

		// Row 2
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(), spiralIterator.next());

		// Corners of this diagonal square begin getting clipped by the given square radius
		// Row 3
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(2).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(2), spiralIterator.next());

		// Row 4
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(2).east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(2), spiralIterator.next());
		assertTrue(spiralIterator.hasNext()); // Last element
		assertEquals(BlockPos.ZERO.west(2).north(2), spiralIterator.next());

		// Diagonal row 4 completely exits the square zone
		assertFalse(spiralIterator.hasNext());
	}

	@Test
	public void diagonalSpiralFromCenterBoundedAdvanced7x7() {
		DiagonalSpiralIterator spiralIterator = new DiagonalSpiralIterator(3);

		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO, spiralIterator.next());

		// Row 1
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(), spiralIterator.next());

		// Row 2
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(), spiralIterator.next());

		// Row 3
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(2), spiralIterator.next());

		// Row 4
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(3).east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3).south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3).west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3).north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(3), spiralIterator.next());

		// Row 5
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(3).east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north(2).east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3).south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2).south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3).west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2).west(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(3).north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2).north(3), spiralIterator.next());

		// Row 6
		assertTrue(spiralIterator.hasNext());
		assertEquals(BlockPos.ZERO.north(3).east(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(3).south(3), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(3).west(3), spiralIterator.next());
		assertTrue(spiralIterator.hasNext()); // Last element
		assertEquals(BlockPos.ZERO.west(3).north(3), spiralIterator.next());

		// Diagonal row 7 completely exits the square zone
		assertFalse(spiralIterator.hasNext());
	}

}
