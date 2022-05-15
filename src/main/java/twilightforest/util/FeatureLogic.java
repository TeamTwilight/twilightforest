package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.BlockTagGenerator;

import java.util.Random;
import java.util.function.Predicate;

/**
 * Feature Utility methods that don't invoke placement. For placement see FeaturePlacers
 */
public final class FeatureLogic {
    public static final Predicate<BlockState> IS_REPLACEABLE_AIR = state -> state.getMaterial().isReplaceable() || state.isAir();
    public static final Predicate<BlockState> SHOULD_SKIP = state -> state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    public static boolean hasEmptyNeighbor(LevelSimulatedReader worldReader, BlockPos pos) {
        return worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR)
                || worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR)
                || worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR)
                || worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR)
                || worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR)
                || worldReader.isStateAtPosition(pos.below(), IS_REPLACEABLE_AIR);
    }

    // Slight stretch of logic: We check if the block is completely surrounded by air.
    // If it's not completely surrounded by air, then there's a solid
    public static boolean hasSolidNeighbor(LevelSimulatedReader worldReader, BlockPos pos) {
        return !(worldReader.isStateAtPosition(pos.below(), IS_REPLACEABLE_AIR)
                && worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR)
                && worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR)
                && worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR)
                && worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR)
                && worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR));
    }

    public static boolean canRootGrowIn(LevelSimulatedReader worldReader, BlockPos pos) {
        if (worldReader.isStateAtPosition(pos, IS_REPLACEABLE_AIR)) {
            // roots can grow through air if they are near a solid block
            return hasSolidNeighbor(worldReader, pos);
        } else {
            return worldReader.isStateAtPosition(pos, FeatureLogic::worldGenReplaceable);
        }
    }

    public static boolean worldGenReplaceable(BlockState state) {
        return (state.getMaterial().isReplaceable() || state.is(BlockTagGenerator.WORLDGEN_REPLACEABLES)) && !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    /**
     * Moves distance along the vector.
     * <p>
     * This goofy function takes a float between 0 and 1 for the angle, where 0 is 0 degrees, .5 is 180 degrees and 1 and 360 degrees.
     * For the tilt, it takes a float between 0 and 1 where 0 is straight up, 0.5 is straight out and 1 is straight down.
     */
    public static BlockPos translate(BlockPos pos, double distance, double angle, double tilt) {
        double rangle = angle * 2.0D * Math.PI;
        double rtilt = tilt * Math.PI;

        return pos.offset(
                Math.round(Math.sin(rangle) * Math.sin(rtilt) * distance),
                Math.round(Math.cos(rtilt) * distance),
                Math.round(Math.cos(rangle) * Math.sin(rtilt) * distance)
        );
    }

    /**
     * Get an array of values that represent a line from point A to point B
     */
    @Deprecated // Use VoxelBresenhamIterator directly instead
    public static BlockPos[] getBresenhamArrays(BlockPos src, BlockPos dest) {
        return getBresenhamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
    }

    /**
     * Get an array of values that represent a line from point A to point B
     */
    @Deprecated // Use VoxelBresenhamIterator directly instead
    public static BlockPos[] getBresenhamArrays(int x1, int y1, int z1, int x2, int y2, int z2) {
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

    /**
     * Gets either cobblestone or mossy cobblestone, randomly.  Used for ruins.
     */
    @Deprecated // Determine if we can actually remove this one and delegate to StructureProcessor
    public static BlockState randStone(Random rand, int howMuch) {
        return rand.nextInt(howMuch) >= 1 ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.MOSSY_COBBLESTONE.defaultBlockState();
    }
}
