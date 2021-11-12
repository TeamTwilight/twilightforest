package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Iterator;

/** Short-able voxel line placement that lazily creates BlockPos */
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
}
