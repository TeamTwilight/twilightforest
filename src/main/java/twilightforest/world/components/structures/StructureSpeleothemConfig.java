package twilightforest.world.components.structures;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.stalactites.entry.SpeleothemVarietyConfig;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record StructureSpeleothemConfig(
        RectangleLatticeIterator.TriangularLatticeConfig latticeConfig,
        String speleothemVarietyType,
        Supplier<SpeleothemVarietyConfig> speleothemVarietyConfig,
        // It hasn't been determined if StructureSpeleothemConfig objects are guaranteed to be initialized only after SpeleothemVarietyConfig objects
        // Use Suppliers.memoize to cut duplication while maintaining lazy initialization
        Supplier<Function<RandomSource, Stalactite>> stalactiteVariety,
        Supplier<Function<RandomSource, Stalactite>> stalagmiteVariety
) {
    public static final Codec<StructureSpeleothemConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RectangleLatticeIterator.TriangularLatticeConfig.CODEC.fieldOf("lattice").forGetter(StructureSpeleothemConfig::latticeConfig),
            Codec.STRING.xmap(String::toLowerCase, String::toLowerCase).fieldOf("type").forGetter(StructureSpeleothemConfig::speleothemVarietyType)
    ).apply(inst, StructureSpeleothemConfig::fromLocation));

    @NotNull
    public static StructureSpeleothemConfig fromLocation(RectangleLatticeIterator.TriangularLatticeConfig latticeConfig, final String type) {
        Supplier<SpeleothemVarietyConfig> lazyConfigSupplier = Suppliers.memoize(() -> StalactiteReloadListener.HILL_CONFIGS.get(type));

        Supplier<Function<RandomSource, Stalactite>> lazyStalactiteGetter = Suppliers.memoize(() -> compileStalactites(lazyConfigSupplier));
        Supplier<Function<RandomSource, Stalactite>> lazyStalagmiteGetter = Suppliers.memoize(() -> compileStalagmites(lazyConfigSupplier));

        return new StructureSpeleothemConfig(
                latticeConfig,
                type,
                lazyConfigSupplier,
                lazyStalactiteGetter,
                lazyStalagmiteGetter
        );
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileStalagmites(Supplier<SpeleothemVarietyConfig> varietyConfigSupplier) {
        SpeleothemVarietyConfig varietyConfig = varietyConfigSupplier.get();

        TwilightForestMod.LOGGER.debug("Compiling Stalagmite configs for " + varietyConfig.type() + " type");

        List<Stalactite> stalactites = StalactiteReloadListener.STALAGMITES_PER_HILL.get(varietyConfig.type());

        return compileSpeleothemsSimple(stalactites);
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileStalactites(Supplier<SpeleothemVarietyConfig> varietyConfigSupplier) {
        SpeleothemVarietyConfig varietyConfig = varietyConfigSupplier.get();

        TwilightForestMod.LOGGER.debug("Compiling Stalactite configs for " + varietyConfig.type() + " type");

        // Ore Chance represents an interpolation between two weighted lists of A (stones) and B (ores)
        float weightedListInterpolation = Mth.clamp(varietyConfig.oreChance(), 0, 1);

        List<Stalactite> stalactites = StalactiteReloadListener.STALACTITES_PER_HILL.get(varietyConfig.type());
        List<Stalactite> oreStalactites = StalactiteReloadListener.ORE_STALACTITES_PER_HILL.get(varietyConfig.type());

        int stoneWeightSum = stalactites.stream().mapToInt(Stalactite::weight).sum();
        int oreWeightSum = oreStalactites.stream().mapToInt(Stalactite::weight).sum();
        float totalWeight = stoneWeightSum + oreWeightSum;

        // Simplify underlying data structures for returned lambdas by only "compiling" the list if the chance for the alternative is zero
        // or if the alternate's list is empty
        if (totalWeight <= 0) {
            return BlockSpikeFeature::defaultRandom;
        } else if (stalactites.isEmpty() || stoneWeightSum <= 0) {
            return compileSpeleothemsSimple(oreStalactites);
        } else if (oreStalactites.isEmpty() || oreWeightSum <= 0) {
            return compileSpeleothemsSimple(stalactites);
        }

        // Since the weights are integers, this ensures some interpolation precision through float->integer casting
        // Stupid but simple way of transporting fractional precision up to ten-thousandths
        // Loss of precision is expected regardless, but insignificant
        // Yes, digits of precision are counted through String Length.
        // But this happens once per StructureSpeleothemConfig after loading, thanks to memoization.
        final double quantizationFactor = Math.ceil(Math.pow(10, Mth.clamp((weightedListInterpolation + "" + totalWeight).length() - 4, 2, 6)));

        final double stoneCounterweight = quantizationFactor * (1 - weightedListInterpolation) / stoneWeightSum;
        final double oreCounterweight = quantizationFactor * weightedListInterpolation / oreWeightSum;

        // Simplify underlying data structures for returned lambdas by only "compiling" the list if
        // the chance for the alternative is zero or if the alternate list is empty
        if (stoneCounterweight <= 0) {
            return compileSpeleothemsSimple(oreStalactites);
        } else if (oreCounterweight <= 0) {
            return compileSpeleothemsSimple(stalactites);
        }

        // Rebuild individual weighted lists, multiply appropriate weights for interpolating between stones vs ores speleothem
        ArrayList<WeightedEntry.Wrapper<Stalactite>> unbakedRandomList = stalactites.stream().map(s -> WeightedEntry.wrap(s, Mth.ceil(s.weight() * stoneCounterweight))).collect(Collectors.toCollection(ArrayList::new));
        // Add oreStalactites to the unbaked list
        oreStalactites.stream().map(s -> WeightedEntry.wrap(s, Mth.ceil(s.weight() * oreCounterweight))).forEachOrdered(unbakedRandomList::add);

        {
            StringJoiner joiner = new StringJoiner("\n");

            joiner.add("")
                    .add("Ore interpolation factor: " + weightedListInterpolation)
                    .add("Stone Counterweight: " + stoneCounterweight)
                    .add("Ore Counterweight: " + oreCounterweight);

            for (WeightedEntry.Wrapper<Stalactite> e : unbakedRandomList)
                joiner.add(e.getData() + " - After counterweight: " + e.getWeight().asInt());

            joiner.add("Total weight after counterweights: " + unbakedRandomList.stream().mapToInt(e -> e.getWeight().asInt()).sum());

            TwilightForestMod.LOGGER.debug(joiner);
        }

        return compileSpeleothems(unbakedRandomList);
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileSpeleothemsSimple(List<Stalactite> stalactites) {
        return compileSpeleothems(stalactites.stream().map(stalactite -> WeightedEntry.wrap(stalactite, stalactite.weight())).toList());
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileSpeleothems(List<WeightedEntry.Wrapper<Stalactite>> unbakedRandomList) {
        // Construct this once. Constructing it inside the lambda means it'll be constructed each time the lambda is invoked
        WeightedRandomList<WeightedEntry.Wrapper<Stalactite>> randomList = WeightedRandomList.create(unbakedRandomList);

        // Simplify underlying data structure for returned lambdas by simply returning a
        // Stone Stalactite getter whenever there's no random elements to pass.
        if (randomList.isEmpty() || randomList.unwrap().stream().mapToInt(w -> w.getWeight().asInt()).sum() <= 0) {
            return BlockSpikeFeature::defaultRandom;
        }

        // Return a function representing anonymous access to the randomList, by passing a RandomSource in which a Speleothem is returned.
        // This ensures the randomList is constructed only once.
        return random -> randomList.getRandom(random).map(WeightedEntry.Wrapper::getData).orElse(BlockSpikeFeature.STONE_STALACTITE);
    }

    public SpeleothemVarietyConfig getVarietyConfig() {
        return this.speleothemVarietyConfig.get();
    }

    public boolean shouldDoAStalactite(RandomSource rand) {
        return this.getVarietyConfig().shouldDoAStalactite(rand);
    }

    public boolean shouldDoAStalagmite(RandomSource rand) {
        return this.getVarietyConfig().shouldDoAStalagmite(rand);
    }

    @NotNull
    public Stalactite getStalactite(RandomSource rand) {
        return this.stalactiteVariety.get().apply(rand);
    }

    @NotNull
    public Stalactite getStalagmite(RandomSource rand) {
        return this.stalagmiteVariety.get().apply(rand);
    }

    @NotNull
    public Stalactite getSpeleothem(boolean hanging, RandomSource rand) {
        return hanging ? this.getStalactite(rand) : this.getStalagmite(rand);
    }

    @NotNull
    public Iterable<BlockPos.MutableBlockPos> latticeIterator(@Nullable BoundingBox bounds, int yLevel) {
        if (bounds == null)
            return List.of();

        return this.latticeConfig.boundedGrid(bounds, yLevel);
    }
}
