package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.IMCHandler;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.util.FeatureLogic;
import twilightforest.world.components.feature.config.SpikeConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockSpikeFeature extends Feature<SpikeConfig> {
    public BlockSpikeFeature(Codec<SpikeConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpikeConfig> context) {
        Random random = context.random();
        return startSpike(context.level(), context.origin(), context.config(), random);
    }

    public static boolean startSpike(WorldGenLevel level, BlockPos startPos, SpikeConfig config, Random random) {
        return startSpike(level, startPos, config.blockState, config.lengthBounds.sample(random), config.lengthBounds.getMinValue(), config.tipClearance.sample(random), config.hang, random);
    }

    public static boolean startSpike(WorldGenLevel level, BlockPos startPos, BlockStateProvider blockState, int length, int lengthMinimum, int clearance, boolean hang, Random random) {
        BlockPos.MutableBlockPos movingPos = startPos.mutable();
        int clearedLength = 0;
        int dY = hang ? -1 : 1;

        // First find an air block
        for (int i = 0; i < length; i++) {
            clearedLength = i;

            if (FeatureLogic.worldGenReplaceable(level.getBlockState(movingPos))) break;

            movingPos.move(0, dY, 0);
        }

        // Since this gets skipped from the previous line, we invoke it manually
        movingPos.move(0, dY, 0);

        // Then find a solid block
        int remainingScanLength = length - clearedLength + clearance;
        int finalLength = clearedLength - clearance;
        for (int i = 0; i < remainingScanLength; i++) {
            finalLength = clearedLength + i;

            if (!FeatureLogic.worldGenReplaceable(level.getBlockState(movingPos))) break;

            movingPos.move(0, dY, 0);
        }

        finalLength = Math.min(length, finalLength);

        if (finalLength < lengthMinimum) return false;

        return makeSpike(level, startPos, blockState, finalLength, dY, random, hang);
    }

    private static boolean makeSpike(WorldGenLevel level, BlockPos startPos, BlockStateProvider blockState, int length, int dY, Random random, boolean hang) {
        int diameter = (int) (length / 4.5F); // diameter of the base

        //only place spikes on solid ground, not on the tops of trees
        if (!level.getBlockState(startPos.below()).getMaterial().isSolid() && !hang) return false;

        // let's see...
        for (int dx = -diameter; dx <= diameter; dx++) {
            for (int dz = -diameter; dz <= diameter; dz++) {
                // determine how long this spike will be.
                int absx = Math.abs(dx);
                int absz = Math.abs(dz);
                int dist = (int) (Math.max(absx, absz) + Math.min(absx, absz) * 0.5F);
                int spikeLength;

                if (dist <= 0) spikeLength = length;
                else spikeLength = random.nextInt((int) (length / (dist + 0.25F)));

                for (int i = 0; i < spikeLength; i++) {
                    BlockPos placement = startPos.offset(dx, i * dY, dz);

                    if (FeatureLogic.worldGenReplaceable(level.getBlockState(placement)) && (dY > 0 || placement.getY() < level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placement.getX(), placement.getZ()) - 1))
                        level.setBlock(placement, blockState.getState(random, placement), 3);
                }
            }
        }

        return true;
    }

    private static final List<StalactiteEntry> largeHillStalactites = new ArrayList<>();
    private static final List<StalactiteEntry> mediumHillStalactites = new ArrayList<>();
    private static final List<StalactiteEntry> smallHillStalactites = new ArrayList<>();

    /**
     * Makes a random stalactite appropriate to the cave size
     * <p>
     * All include iron, coal and glowstone.
     * <p>
     * Gold and redstone appears in size 2 and larger caves.
     * <p>
     * Diamonds and lapis only appear in size 3 and larger caves.
     */
    public static SpikeConfig makeRandomOreStalactite(Random rand, int hillSize) {
        if (hillSize >= 3 || hillSize >= 2 && rand.nextInt(5) == 0) {
            return WeightedRandom.getRandomItem(rand, largeHillStalactites).get().stalactite;
        }
        if (hillSize >= 2 || hillSize >= 1 && rand.nextInt(5) == 0) {
            return WeightedRandom.getRandomItem(rand, mediumHillStalactites).get().stalactite;
        }
        return WeightedRandom.getRandomItem(rand, smallHillStalactites).get().stalactite;
    }

    public static class StalactiteEntry extends WeightedEntry.IntrusiveBase {
        final SpikeConfig stalactite;

        StalactiteEntry(SpikeConfig stalactite, int itemWeight) {
            super(itemWeight);
            this.stalactite = stalactite;
        }

        public StalactiteEntry(BlockState blockState, float size, int maxLength, int itemWeight) {
            this(new SpikeConfig(BlockStateProvider.simple(blockState), UniformInt.of((int) (maxLength * size), maxLength), ConstantInt.of(4), true), itemWeight);
        }
    }

    public static void registerStalactite(int hillSize, BlockState blockState, float size, int maxLength, int minHeight, int itemWeight) {
        if (itemWeight > 0) registerStalactite(hillSize, new StalactiteEntry(blockState, size, maxLength, itemWeight));
    }

    private static void registerStalactite(int hillSize, BlockSpikeFeature.StalactiteEntry entry) {
        if (hillSize <= 1)
            smallHillStalactites.add(entry);

        if (hillSize <= 2)
            mediumHillStalactites.add(entry);

        largeHillStalactites.add(entry);
    }

    /*
     * Current default weights are as follows:
     *
     * Large (total 195 = 13*15):
     * 2/13 diamond
     * 2/13 lapis
     * 1/13 emerald
     * 8/13 [medium pool]
     *
     * Medium (total 120 = 6*20):
     * 1/6 gold
     * 1/6 redstone
     * 3/6 [small pool]
     *
     * Small (total 60 = 5*12):
     * 2/5 iron
     * 2/5 coal
     * 1/5 glowstone
     */
    private static void addDefaultStalactites() {
        registerStalactite(3, Blocks.DIAMOND_ORE.defaultBlockState(), 0.5F, 4, 16, 30);
        registerStalactite(3, Blocks.LAPIS_ORE.defaultBlockState(), 0.8F, 8, 1, 30);
        registerStalactite(3, Blocks.EMERALD_ORE.defaultBlockState(), 0.5F, 3, 12, 15);

        registerStalactite(2, Blocks.GOLD_ORE.defaultBlockState(), 0.6F, 6, 1, 20);
        registerStalactite(2, Blocks.REDSTONE_ORE.defaultBlockState(), 0.8F, 8, 1, 40);

        registerStalactite(1, Blocks.IRON_ORE.defaultBlockState(), 0.7F, 8, 1, 24);
        registerStalactite(1, Blocks.COAL_ORE.defaultBlockState(), 0.8F, 12, 1, 24);
        registerStalactite(1, Blocks.COPPER_ORE.defaultBlockState(), 0.6F, 12, 1, 12);
        registerStalactite(1, Blocks.GLOWSTONE.defaultBlockState(), 0.5F, 8, 1, 12);
    }

    public static void loadStalactites() {
        smallHillStalactites.clear();
        mediumHillStalactites.clear();
        largeHillStalactites.clear();

        TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.load();
        if (TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.useConfigOnly.get()) {
            if (smallHillStalactites.isEmpty()) {
                TwilightForestMod.LOGGER.info("Not all hollow hills are populated with the config, adding fallback");
                registerStalactite(1, Blocks.STONE.defaultBlockState(), 0.7F, 8, 1, 1);
            }
            return;
        }
        addDefaultStalactites();
        IMCHandler.getStalactites().forEach(BlockSpikeFeature::registerStalactite);
    }
}
