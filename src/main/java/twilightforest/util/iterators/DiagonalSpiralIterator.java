package twilightforest.util.iterators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import twilightforest.util.BinaryIntegerFunction;

import java.util.Iterator;

public class DiagonalSpiralIterator<T> implements Iterator<T>, Iterable<T> {

	private static final Direction[] DIRECTIONS = new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

	private final int xConstant;
	private final int zConstant;
	private final int radius;
	private final int spacing;
	private final BinaryIntegerFunction<T> converter;

	private int offset = 0; // How far are the rows from the center
	private int side = 0; // Which of the 4 sides is being marched over
	private int advance = 0; // Where on the side?

	public static DiagonalSpiralIterator<BlockPos> atElevationZero(int xConstant, int zConstant, boolean skipCenter, int radius, int spacing) {
		return new DiagonalSpiralIterator<>(xConstant, zConstant, skipCenter, radius, spacing, (x, z) -> new BlockPos(x, 0, z));
	}

	public DiagonalSpiralIterator(int xConstant, int zConstant, boolean skipCenter, int radius, int spacing, BinaryIntegerFunction<T> converter) {
		this.xConstant = xConstant;
		this.zConstant = zConstant;
		this.radius = radius / spacing;
		this.spacing = spacing;
		this.converter = converter;

		if (skipCenter) {
			this.offset = 1;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return this.offset <= this.radius * 2;
	}

	@Override
	public T next() {
		T newPos = this.nextBlockPos();
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

		int advanceLimit = Math.min(this.offset, this.radius + 1);
		if (this.advance >= advanceLimit) {
			this.side = (this.side + 1) % 4;
			if (this.side == 0) {
				this.offset++;
			}
			this.advance = 0;
			if (this.offset > this.radius) {
				this.advance = this.offset - this.radius;
			}
		}
	}

	private T nextBlockPos() {
		Direction direction = DIRECTIONS[this.side];
		Direction clockWise = direction.getClockWise();
		int u = this.offset - this.advance;
		int v = this.advance;
		return this.converter.apply(this.xConstant + (direction.getStepX() * u + clockWise.getStepX() * v) * this.spacing, this.zConstant + (direction.getStepZ() * u + clockWise.getStepZ() * v) * this.spacing);
	}
}
