package twilightforest.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class XZQuadrantIterator<T> implements Iterator<T>, Iterable<T> {
	private final BinaryIntegerFunction<T> converter;

	private final int xConstant;
	private final int zConstant;
	private final int radius;
	private final int spacing;

	private int dX = 0;
	private int dZ = 1;
	private int cardinal = 0;

	public XZQuadrantIterator(int xConstant, int zConstant, boolean skipCenter, int radius, int spacing, BinaryIntegerFunction<T> converter) {
		this.converter = converter;

		this.xConstant = xConstant;
		this.zConstant = zConstant;
		this.radius = radius;
		this.spacing = spacing;

		if (!skipCenter) {
			this.dZ = 0;
			this.cardinal = -1;
		}
	}

	@Override
	public boolean hasNext() {
		return this.dX + 1 <= this.radius;
	}

	@Override
	public T next() {
		if (!this.hasNext()) throw new IllegalStateException("Cannot iterate further on XZ quadrants! [" + this + "]");

		return switch (this.cardinal) {
			case 0 -> {
				this.cardinal++;
				yield this.generate(this.dZ * this.spacing, -this.dX * this.spacing);
			}
			case 1 -> {
				this.cardinal++;
				yield this.generate(-this.dX * this.spacing, -this.dZ * this.spacing);
			}
			case 2 -> {
				this.cardinal++;
				yield this.generate(-this.dZ * this.spacing, this.dX * this.spacing);
			}
			default -> { // Basically -1 or 3
				this.cardinal = 0;
				T ret = this.generate(this.dX * this.spacing, this.dZ * this.spacing);
				this.tickXZ();
				yield ret;
			}
		};
	}

	private T generate(int dX, int dZ) {
		return this.converter.apply(this.xConstant + dX, this.zConstant + dZ);
	}

	// Primes this iterator for next, or deadlocks its completion
	private void tickXZ() {
		if (!this.hasNext()) return;

		if (this.dZ < this.radius) {
			this.dZ++;
		} else {
			this.dZ = 1;
			this.dX++;
		}
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@FunctionalInterface
	public interface BinaryIntegerFunction<T> {
		T apply(int x, int z);
	}

	@Override
	public String toString() {
		return "XZQuadrantIterator{" +
			"converter=" + converter +
			", xConstant=" + xConstant +
			", zConstant=" + zConstant +
			", radius=" + radius +
			", spacing=" + spacing +
			", dX=" + dX +
			", dZ=" + dZ +
			", cardinal=" + cardinal +
			'}';
	}
}
