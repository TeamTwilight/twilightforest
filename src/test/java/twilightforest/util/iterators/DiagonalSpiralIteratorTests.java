package twilightforest.util.iterators;

import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiagonalSpiralIteratorTests {

	@Test
	public void diagonalSpiralFromCenter() {
		DiagonalSpiralIterator spiralIterator = new DiagonalSpiralIterator();

		assertEquals(BlockPos.ZERO, spiralIterator.next());

		// Row 1
		assertEquals(BlockPos.ZERO.north(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(), spiralIterator.next());

		// Row 2
		assertEquals(BlockPos.ZERO.north(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.north().east(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.east().south(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.south().west(), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west(2), spiralIterator.next());
		assertEquals(BlockPos.ZERO.west().north(), spiralIterator.next());

		// Row 3
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
		assertEquals(BlockPos.ZERO.north(4), spiralIterator.next());
	}

}
