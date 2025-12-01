package twilightforest.util.iterators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DiagonalSpiralIterator implements Iterator<BlockPos>, Iterable<BlockPos> {

	private static final Direction[] DIRECTIONS = new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

	@Deprecated // FIXME remove
	private static final Logger LOGGER = LogManager.getLogger();

	private int offset = 0; // How far are the rows from the center
	private int side = 0; // Which of the 4 sides is being marched over
	private int advance = 0; // Where on the side?

	@Override
	public @NotNull Iterator<BlockPos> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public BlockPos next() {
		BlockPos newPos = this.nextBlockPos();
		this.advancePos();
		return newPos;
	}

	private void advancePos() {
		if (this.offset == 0) {
			this.offset = 1;
			this.advance = 0;
			return;
		}

		this.advance++; // Prepped for next iteration

		if (this.advance >= this.offset) {
			this.side = (this.side + 1) % 4;
			if (this.side == 0) {
				this.offset++;
			}
			this.advance = 0;
		}
	}

	private @NotNull BlockPos nextBlockPos() {
		Direction direction = DIRECTIONS[this.side];
		return BlockPos.ZERO.relative(direction, this.offset - this.advance).relative(direction.getClockWise(), this.advance);
	}
}
