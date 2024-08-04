package twilightforest.util.iterators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class VoxelBresenhamIteratorTest {

	@Test
	public void parity() {
		assertParity(BlockPos.ZERO, new BlockPos(1, 1, 1));
		assertParity(new BlockPos(-1, -1, -1), new BlockPos(7, 12, 17));
	}

	@Test
	public void parityDirections() {
		for (Direction facing : Direction.values()) {
			assertParity(BlockPos.ZERO, BlockPos.ZERO.relative(facing));
			assertParity(BlockPos.ZERO.relative(facing.getOpposite()), BlockPos.ZERO.relative(facing, 2));
			assertParity(BlockPos.ZERO.relative(facing.getOpposite()), BlockPos.ZERO.relative(facing, 10).offset(5, 5, 5));
			assertParity(BlockPos.ZERO.relative(facing.getOpposite(), 4), BlockPos.ZERO.relative(facing, 15).offset(32, 16, 64));
		}
	}

	private void assertParity(BlockPos source, BlockPos destination) {
		assertArrayEquals(getBresenhamArrays(source, destination), StreamSupport.stream(new VoxelBresenhamIterator(source, destination).spliterator(), false).toArray(BlockPos[]::new));
	}

	// The old code as it used to exist in older versions. Existing only as an accuracy benchmark (100% parity)
	private BlockPos[] getBresenhamArrays(BlockPos src, BlockPos dest) {
		return getBresenhamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
	}

	private BlockPos[] getBresenhamArrays(int x1, int y1, int z1, int x2, int y2, int z2) {
		int i, dx, dy, dz, absDx, absDy, absDz, x_inc, y_inc, z_inc, err_1, err_2, doubleAbsDx, doubleAbsDy, doubleAbsDz;

		BlockPos pixel = new BlockPos(x1, y1, z1);
		BlockPos[] lineArray;

		dx = x2 - x1;
		dy = y2 - y1;
		dz = z2 - z1;
		x_inc = dx < 0 ? -1 : 1;
		absDx = Math.abs(dx);
		y_inc = dy < 0 ? -1 : 1;
		absDy = Math.abs(dy);
		z_inc = dz < 0 ? -1 : 1;
		absDz = Math.abs(dz);
		doubleAbsDx = absDx << 1;
		doubleAbsDy = absDy << 1;
		doubleAbsDz = absDz << 1;

		if (absDx >= absDy && absDx >= absDz) {
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
		} else if (absDy >= absDx && absDy >= absDz) {
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
