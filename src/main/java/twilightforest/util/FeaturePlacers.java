package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.block.TFBlocks;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Feature Utility methods that invoke placement. For non-placement see FeatureLogic
 */
public final class FeaturePlacers {
    public static final BiFunction<LevelSimulatedReader, BlockPos, Boolean> VALID_TREE_POS = TreeFeature::validTreePos;

    public static <T extends Mob> void placeEntity(EntityType<T> entityType, BlockPos pos, ServerLevelAccessor levelAccessor) {
        Mob mob = entityType.create(levelAccessor.getLevel());

        if (mob == null) return;

        mob.setPersistenceRequired();
        mob.moveTo(pos, 0.0F, 0.0F);
        mob.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null ,null);
        levelAccessor.addFreshEntityWithPassengers(mob);
        levelAccessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
    }

    /**
     * Draws a line from {x1, y1, z1} to {x2, y2, z2}
     * This takes all variables for setting Branch
     */
    public static void drawBresenhamBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, Random random, BlockPos start, BlockPos end, BlockStateProvider config) {
        for (BlockPos pixel : new VoxelBresenhamIterator(start, end)) {
            placeIfValidTreePos(world, trunkPlacer, random, pixel, config);
        }
    }

    /**
     * Build a root, but don't let it stick out too far into thin air because that's weird
     */
    public static void buildRoot(LevelAccessor world, BiConsumer<BlockPos, BlockState> placer, Random rand, BlockPos start, double offset, int b, BlockStateProvider config) {
        BlockPos dest = FeatureLogic.translate(start.below(b + 2), 5, 0.3 * b + offset, 0.8);

        // go through block by block and stop drawing when we head too far into open air
        for (BlockPos coord : new VoxelBresenhamIterator(start.below(), dest)) {
            if (!placeIfValidRootPos(world, placer, rand, coord, config)) return;
        }
    }

    /**
     * Draws a line from {x1, y1, z1} to {x2, y2, z2}
     * This just takes a BlockState, used to set Trunk
     */
    public static void drawBresenhamTree(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, BlockPos from, BlockPos to, BlockStateProvider config, Random random) {
        for (BlockPos pixel : new VoxelBresenhamIterator(from, to)) {
            placeProvidedBlock(world, placer, predicate, pixel, config, random);
        }
    }

    public static void placeProvidedBlock(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> worldPlacer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, BlockPos pos, BlockStateProvider config, Random random) {
        if (predicate.apply(world, pos)) worldPlacer.accept(pos, config.getState(random, pos));
    }

    // Use for trunks with Odd-count widths
    public static void placeCircleOdd(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float radius, BlockStateProvider config) {
        // Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
        // the pos to be immutable causing multiple same BlockPos instances to exist.
        float radiusSquared = radius * radius;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        // trace out a quadrant
        for (int x = 0; x <= radius; x++) {
            for (int z = 1; z <= radius; z++) {
                // if we're inside the blob, fill it
                if (x * x + z * z <= radiusSquared) {
                    // do four at a time for easiness!
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x, 0,  z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, 0, -z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z, 0,  x), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z, 0, -x), config, random);
                    // Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
                }
            }
        }
    }

    // Use for trunks with Even-count widths
    // TODO Verify that it works correctly, haven't gotten to a compiling state yet to test
    public static void placeCircleEven(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float radius, BlockStateProvider config) {
        // Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
        // the pos to be immutable causing multiple same BlockPos instances to exist.
        float radiusSquared = radius * radius;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        // trace out a quadrant
        for (int x = 0; x <= radius; x++) {
            for (int z = 0; z <= radius; z++) {
                // if we're inside the blob, fill it
                if (x * x + z * z <= radiusSquared) {
                    // do four at a time for easiness!
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 1+x, 0, 1+z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, 0, -z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, 0, 1+z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 1+x, 0, -z), config, random);
                    // Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
                }
            }
        }
    }

    public static void placeSpheroid(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float xzRadius, float yRadius, float verticalBias, BlockStateProvider config) {
        float xzRadiusSquared = xzRadius * xzRadius;
        float yRadiusSquared = yRadius * yRadius;
        float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        for (int y = 0; y <= yRadius; y++) {
            if (y > yRadius) continue;

            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 0,  y, 0), config, random);
            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 0, -y, 0), config, random);
        }

        for (int x = 0; x <= xzRadius; x++) {
            for (int z = 1; z <= xzRadius; z++) {
                if (x * x + z * z > xzRadiusSquared) continue;

                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x, 0,  z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, 0, -z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z, 0,  x), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z, 0, -x), config, random);

                for (int y = 1; y <= yRadius; y++) {
                    float xzSquare = ((x * x + z * z) * yRadiusSquared);

                    if (xzSquare + (((y - verticalBias) * (y - verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x,  y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x,  y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z,  y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z,  y, -x), config, random);
                    }

                    if (xzSquare + (((y + verticalBias) * (y + verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x, -y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, -y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z, -y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z, -y, -x), config, random);
                    }
                }
            }
        }
    }

    // Version without the `verticalBias` unlike above
    public static void placeSpheroid(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float xzRadius, float yRadius, BlockStateProvider config) {
        float xzRadiusSquared = xzRadius * xzRadius;
        float yRadiusSquared = yRadius * yRadius;
        float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        for (int y = 0; y <= yRadius; y++) {
            if (y > yRadius) continue;

            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 0,  y, 0), config, random);
            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( 0, -y, 0), config, random);
        }

        for (int x = 0; x <= xzRadius; x++) {
            for (int z = 1; z <= xzRadius; z++) {
                if (x * x + z * z > xzRadiusSquared) continue;

                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x, 0,  z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, 0, -z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z, 0,  x), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z, 0, -x), config, random);

                for (int y = 1; y <= yRadius; y++) {
                    float xzSquare = ((x * x + z * z) * yRadiusSquared);

                    if (xzSquare + (y * y) * xzRadiusSquared <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x,  y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x,  y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z,  y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z,  y, -x), config, random);

                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  x, -y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -x, -y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset( -z, -y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(  z, -y, -x), config, random);
                    }
                }
            }
        }
    }

    // [VanillaCopy] TrunkPlacer.placeLog - Swapped TreeConfiguration for BlockStateProvider
    // If possible, use TrunkPlacer.placeLog instead
    public static boolean placeIfValidTreePos(LevelAccessor world, BiConsumer<BlockPos, BlockState> placer, Random random, BlockPos pos, BlockStateProvider config) {
        if (TreeFeature.validTreePos(world, pos)) {
            placer.accept(pos, config.getState(random, pos));
            return true;
        } else {
            return false;
        }
    }

    public static boolean placeIfValidRootPos(LevelAccessor world, BiConsumer<BlockPos, BlockState> placer, Random random, BlockPos pos, BlockStateProvider config) {
        if (FeatureLogic.canRootGrowIn(world, pos)) {
            placer.accept(pos, config.getState(random, pos));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a firefly at the specified height and angle.
     *
     * @param height how far up the tree
     * @param angle  from 0 - 1 rotation around the tree
     */
    public static void addFirefly(LevelAccessor world, BlockPos pos, int height, double angle) {
        int iAngle = (int) (angle * 4.0);
        if (iAngle == 0) {
            setIfEmpty(world, pos.offset( 1, height,  0), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));
        } else if (iAngle == 1) {
            setIfEmpty(world, pos.offset(-1, height,  0), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));
        } else if (iAngle == 2) {
            setIfEmpty(world, pos.offset( 0, height,  1), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH));
        } else if (iAngle == 3) {
            setIfEmpty(world, pos.offset( 0, height, -1), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH));
        }
    }

    private static void setIfEmpty(LevelAccessor world, BlockPos pos, BlockState state) {
        if (world.isEmptyBlock(pos)) {
            world.setBlock(pos, state,3);
        }
    }

    public static BlockState transferAllStateKeys(BlockState stateIn, Block blockOut) {
        return transferAllStateKeys(stateIn, blockOut.defaultBlockState());
    }

    public static BlockState transferAllStateKeys(BlockState stateIn, BlockState stateOut) {
        for (Property<?> property : stateOut.getProperties()) {
            stateOut = transferStateKey(stateIn, stateOut, property);
        }
        return stateOut;
    }

    public static <T extends Comparable<T>> BlockState transferStateKey(BlockState stateIn, BlockState stateOut, Property<T> property) {
        if(!stateIn.hasProperty(property) || !stateOut.hasProperty(property)) return stateOut;
        return stateOut.setValue(property, stateIn.getValue(property));
    }
}
