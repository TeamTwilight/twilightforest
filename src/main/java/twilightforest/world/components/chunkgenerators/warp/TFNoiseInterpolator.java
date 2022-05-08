package twilightforest.world.components.chunkgenerators.warp;

import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;

//VanillaCopy of NoiseInterpolator from 1.17.1. Just easier to control than the existing NoiseInterpolator
public class TFNoiseInterpolator {

    private final int cellCountY;
    private final int cellCountZ;
    private final int cellMinY;
    private final NoiseFiller noiseFiller;
    private double[][] slice0;
    private double[][] slice1;
    private final int firstX;
    private final int firstZ;

    private double noise000;
    private double noise001;
    private double noise100;
    private double noise101;
    private double noise010;
    private double noise011;
    private double noise110;
    private double noise111;

    private double value;
    private double valueZ0;
    private double valueZ1;
    private double valueXZ00;
    private double valueXZ10;
    private double valueXZ01;
    private double valueXZ11;

    public TFNoiseInterpolator(int x, int max, int z, ChunkPos pos, int min, NoiseFiller filler) {
        this.cellCountY = max;
        this.cellCountZ = z;
        this.cellMinY = min;
        this.noiseFiller = filler;
        this.slice0 = allocateSlice(max, z);
        this.slice1 = allocateSlice(max, z);
        this.firstX = pos.x * x;
        this.firstZ = pos.z * z;
    }

    private static double[][] allocateSlice(int y, int z) {
        int height = y + 1;
        int width = z + 1;
        double[][] cells = new double[width][height];

        for (int cell = 0; cell < width; cell++) {
            cells[cell] = new double[height];
        }

        return cells;
    }

    public void initialiseFirstX() {
        this.fillSlice(this.slice0, this.firstX);
    }

    public void advanceX(int advance) {
        this.fillSlice(this.slice1, this.firstX + advance + 1);
    }

    private void fillSlice(double[][] slices, int cell) {
        for (int width = 0; width < this.cellCountZ + 1; width++) {
            int cellWidth = this.firstZ + width;
            this.noiseFiller.fillNoiseColumn(slices[width], cell, cellWidth, this.cellMinY, this.cellCountY);
        }
    }

    public void selectYZ(int y, int z) {
        this.noise000 = this.slice0[  z  ][  y  ];
        this.noise001 = this.slice0[z + 1][  y  ];
        this.noise100 = this.slice1[  z  ][  y  ];
        this.noise101 = this.slice1[z + 1][  y  ];
        this.noise010 = this.slice0[  z  ][y + 1];
        this.noise011 = this.slice0[z + 1][y + 1];
        this.noise110 = this.slice1[  z  ][y + 1];
        this.noise111 = this.slice1[z + 1][y + 1];
    }

    public void updateX(double x) {
        this.valueZ0 = Mth.lerp(x, this.valueXZ00, this.valueXZ10);
        this.valueZ1 = Mth.lerp(x, this.valueXZ01, this.valueXZ11);
    }

    public void updateY(double y) {
        this.valueXZ00 = Mth.lerp(y, this.noise000, this.noise010);
        this.valueXZ10 = Mth.lerp(y, this.noise100, this.noise110);
        this.valueXZ01 = Mth.lerp(y, this.noise001, this.noise011);
        this.valueXZ11 = Mth.lerp(y, this.noise101, this.noise111);
    }

    public double updateZ(double z) {
        this.value = Mth.lerp(z, this.valueZ0, this.valueZ1);
        return this.value;
    }

    public void swapSlices() {
        double[][] swap = this.slice0;
        this.slice0 = this.slice1;
        this.slice1 = swap;
    }

    public interface NoiseFiller {
        void fillNoiseColumn(double[] columns, int x, int z, int min, int max);
    }
}
