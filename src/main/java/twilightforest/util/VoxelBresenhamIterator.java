package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Short-able voxel line placement that lazily creates BlockPos
 */
public final class VoxelBresenhamIterator implements Iterator<BlockPos>, Iterable<BlockPos> {
	private final int x_inc, y_inc, z_inc, doubleAbsDx, doubleAbsDy, doubleAbsDz, length;
	private final BlockPos.MutableBlockPos voxel;
	private final Direction.Axis direction;

	private int i = 0, err_1, err_2;

	public VoxelBresenhamIterator(BlockPos voxel, BlockPos towards) {
		this(voxel, towards.getX(), towards.getY(), towards.getZ());
	}

	public VoxelBresenhamIterator(BlockPos voxel, final int x2, final int y2, final int z2) {
		this.voxel = voxel.mutable();

		final int x1 = this.voxel.getX();
		final int y1 = this.voxel.getY();
		final int z1 = this.voxel.getZ();

		final int xVec = x2 - x1;
		final int yVec = y2 - y1;
		final int zVec = z2 - z1;

		final int absDx = Math.abs(xVec);
		final int absDy = Math.abs(yVec);
		final int absDz = Math.abs(zVec);

		this.x_inc = (xVec < 0) ? -1 : 1;
		this.y_inc = (yVec < 0) ? -1 : 1;
		this.z_inc = (zVec < 0) ? -1 : 1;

		this.doubleAbsDx = absDx << 1;
		this.doubleAbsDy = absDy << 1;
		this.doubleAbsDz = absDz << 1;

		if (absDx >= absDy && absDx >= absDz) {
			this.err_1 = this.doubleAbsDy - absDx;
			this.err_2 = this.doubleAbsDz - absDx;

			this.direction = Direction.Axis.X;
			this.length = absDx + 1;
		} else if (absDy >= absDx && absDy >= absDz) {
			this.err_1 = this.doubleAbsDx - absDy;
			this.err_2 = this.doubleAbsDz - absDy;

			this.direction = Direction.Axis.Y;
			this.length = absDy + 1;
		} else {
			this.err_1 = this.doubleAbsDy - absDz;
			this.err_2 = this.doubleAbsDx - absDz;

			this.direction = Direction.Axis.Z;
			this.length = absDz + 1;
		}
	}

	@Override
	public boolean hasNext() {
		return this.i < this.length;
	}

	@Override
	public BlockPos next() {
		final BlockPos out = this.voxel.immutable();

		if (this.hasNext()) {
			this.primeNext();
			this.i++;
		}

		return out;
	}

	private void primeNext() {
		switch (this.direction) {
			case X -> {
				if (this.err_1 > 0) {
					this.voxel.move(0, this.y_inc, 0);
					this.err_1 -= this.doubleAbsDx;
				}
				if (this.err_2 > 0) {
					this.voxel.move(0, 0, this.z_inc);
					this.err_2 -= this.doubleAbsDx;
				}

				this.err_1 += this.doubleAbsDy;
				this.err_2 += this.doubleAbsDz;
				this.voxel.move(this.x_inc, 0, 0);
			}
			case Y -> {
				if (this.err_1 > 0) {
					this.voxel.move(this.x_inc, 0, 0);
					this.err_1 -= this.doubleAbsDy;
				}
				if (this.err_2 > 0) {
					this.voxel.move(0, 0, this.z_inc);
					this.err_2 -= this.doubleAbsDy;
				}

				this.err_1 += this.doubleAbsDx;
				this.err_2 += this.doubleAbsDz;
				this.voxel.move(0, this.y_inc, 0);
			}
			case Z -> {
				if (this.err_1 > 0) {
					this.voxel.move(0, this.y_inc, 0);
					this.err_1 -= this.doubleAbsDz;
				}
				if (this.err_2 > 0) {
					this.voxel.move(this.x_inc, 0, 0);
					this.err_2 -= this.doubleAbsDz;
				}

				this.err_1 += this.doubleAbsDy;
				this.err_2 += this.doubleAbsDx;
				this.voxel.move(0, 0, this.z_inc);
			}
		}
	}

	@Override
	public Iterator<BlockPos> iterator() {
		return this;
	}

	// ---------------- Only for parity testing ----------------------------------------------------------------

	public static void main(String... args) {
		boolean allMatching = true;

		for (Direction facing : Direction.values()) {
			allMatching &= parityTest(BlockPos.ZERO, BlockPos.ZERO.relative(facing));
			allMatching &= parityTest(BlockPos.ZERO.relative(facing.getOpposite()), BlockPos.ZERO.relative(facing, 2));
			allMatching &= parityTest(BlockPos.ZERO.relative(facing.getOpposite()), BlockPos.ZERO.relative(facing, 10).offset(5, 5, 5));
			allMatching &= parityTest(BlockPos.ZERO.relative(facing.getOpposite(), 4), BlockPos.ZERO.relative(facing, 15).offset(32, 16, 64));
		}

		allMatching &= parityTest(BlockPos.ZERO, new BlockPos(1, 1, 1));
		allMatching &= parityTest(new BlockPos(-1, -1, -1), new BlockPos(7, 12, 17));

		if (allMatching) {
			System.out.println("\nAll tests passed for line algorithms");
		} else {
			System.out.println("\nA line algorithm test failed!");
		}
	}

	private static boolean parityTest(BlockPos source, BlockPos destination) {
		List<BlockPos> iterator = new ArrayList<>();

		for (BlockPos pos : new VoxelBresenhamIterator(source, destination))
			iterator.add(pos);

		List<BlockPos> arrayed = Arrays.asList(getBresenhamArrays(source, destination));

		return diffScan(iterator, arrayed);
	}

	// True if no matches found
	private static boolean diffScan(List<BlockPos> first, List<BlockPos> second) {
		System.out.println("Scanning results " + first + " & " + second);

		int size1 = first.size();
		int size2 = second.size();
		boolean matching = size1 == size2;
		if (!matching)
			System.out.println("List size mismatch! List 1 has " + size1 + " but List 2 has " + size2);

		int scanLength = Math.min(size1, size2);
		for (int index = 0; index < scanLength; index++) {
			BlockPos pos1 = first.get(index);
			BlockPos pos2 = second.get(index);
			BlockPos delta = pos2.subtract(pos1);

			if (!delta.equals(BlockPos.ZERO)) {
				matching = false;
				System.out.println("Mismatch found! element[" + index + "], diff: " + delta);
			}
		}

		if (matching) {
			System.out.println("No mismatches found between common elements!");
			return true;
		}

		return false;
	}

	// The old code as it used to exist in older versions. Existing only as an accuracy benchmark for this class (100% parity)
	private static BlockPos[] getBresenhamArrays(BlockPos src, BlockPos dest) {
		return getBresenhamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
	}

	private static BlockPos[] getBresenhamArrays(int x1, int y1, int z1, int x2, int y2, int z2) {
		int i, dx, dy, dz, absDx, absDy, absDz, x_inc, y_inc, z_inc, err_1, err_2, doubleAbsDx, doubleAbsDy, doubleAbsDz;

		BlockPos pixel = new BlockPos(x1, y1, z1);
		BlockPos[] lineArray;

		dx = x2 - x1;
		dy = y2 - y1;
		dz = z2 - z1;
		x_inc = (dx < 0) ? -1 : 1;
		absDx = Math.abs(dx);
		y_inc = (dy < 0) ? -1 : 1;
		absDy = Math.abs(dy);
		z_inc = (dz < 0) ? -1 : 1;
		absDz = Math.abs(dz);
		doubleAbsDx = absDx << 1;
		doubleAbsDy = absDy << 1;
		doubleAbsDz = absDz << 1;

		if ((absDx >= absDy) && (absDx >= absDz)) {
			err_1 = doubleAbsDy - absDx;
			err_2 = doubleAbsDz - absDx;
			lineArray = new BlockPos[absDx + 1];
			for (i = 0; i < absDx; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.above(y_inc);
					err_1 -= doubleAbsDx;
				}
				if (err_2 > 0) {
					pixel = pixel.south(z_inc);
					err_2 -= doubleAbsDx;
				}
				err_1 += doubleAbsDy;
				err_2 += doubleAbsDz;
				pixel = pixel.east(x_inc);
			}
		} else if ((absDy >= absDx) && (absDy >= absDz)) {
			err_1 = doubleAbsDx - absDy;
			err_2 = doubleAbsDz - absDy;
			lineArray = new BlockPos[absDy + 1];
			for (i = 0; i < absDy; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.east(x_inc);
					err_1 -= doubleAbsDy;
				}
				if (err_2 > 0) {
					pixel = pixel.south(z_inc);
					err_2 -= doubleAbsDy;
				}
				err_1 += doubleAbsDx;
				err_2 += doubleAbsDz;
				pixel = pixel.above(y_inc);
			}
		} else {
			err_1 = doubleAbsDy - absDz;
			err_2 = doubleAbsDx - absDz;
			lineArray = new BlockPos[absDz + 1];
			for (i = 0; i < absDz; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.above(y_inc);
					err_1 -= doubleAbsDz;
				}
				if (err_2 > 0) {
					pixel = pixel.east(x_inc);
					err_2 -= doubleAbsDz;
				}
				err_1 += doubleAbsDy;
				err_2 += doubleAbsDx;
				pixel = pixel.south(z_inc);
			}
		}
		lineArray[lineArray.length - 1] = pixel;

		return lineArray;
	}
}
