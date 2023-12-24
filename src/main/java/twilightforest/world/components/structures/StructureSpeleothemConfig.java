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
import twilightforest.data.custom.stalactites.entry.SpeleothemVarietyConfig;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
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
        List<Stalactite> stalactites = StalactiteReloadListener.STALAGMITES_PER_HILL.get(varietyConfigSupplier.get().type());

        return compileSpeleothems(stalactites);
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileStalactites(Supplier<SpeleothemVarietyConfig> varietyConfigSupplier) {
        SpeleothemVarietyConfig varietyConfig = varietyConfigSupplier.get();

        // Ore Chance represents an interpolation between two weighted lists of A (stones) and B (ores)
        float weightedListInterpolation = varietyConfig.oreChance();

        final int quantizationFactor = 10000; // Since the weights are integers, use 1000 for ensuring interpolation precision up to ten-thousandths.
        final int oreChance = (int) (quantizationFactor * Mth.clamp(weightedListInterpolation, 0, 1));
        final int stoneChance = quantizationFactor - oreChance;

        List<Stalactite> stalactites = StalactiteReloadListener.STALACTITES_PER_HILL.get(varietyConfig.type());
        List<Stalactite> oreStalactites = StalactiteReloadListener.ORE_STALACTITES_PER_HILL.get(varietyConfig.type());

        // Simplify underlying data structures for returned lambdas by only "compiling" the list if the chance for the alternative is zero
        // or if the alternate's list is empty
        if (stalactites.isEmpty() && oreStalactites.isEmpty()) {
            return BlockSpikeFeature::defaultRandom;
        } else if (stoneChance == 0 || stalactites.isEmpty()) {
            return compileSpeleothems(oreStalactites);
        } else if (oreChance == 0 || oreStalactites.isEmpty()) {
            return compileSpeleothems(stalactites);
        }

        // Rebuild individual weighted lists, applying appropriate weights for stone speleothems and ore speleothems
        ArrayList<WeightedEntry.Wrapper<Stalactite>> unbakedRandomList = stalactites.stream().map(stalactite -> WeightedEntry.wrap(stalactite, stalactite.weight() * stoneChance)).collect(Collectors.toCollection(ArrayList::new));
        oreStalactites.stream().map(stalactite -> WeightedEntry.wrap(stalactite, stalactite.weight() * oreChance)).forEachOrdered(unbakedRandomList::add);

        // Construct this once. Constructing it inside the lambda means it'll be constructed each time the lambda is invoked
        WeightedRandomList<WeightedEntry.Wrapper<Stalactite>> randomList = WeightedRandomList.create(unbakedRandomList);

        // Return a function representing anonymous access to the randomList, by passing a RandomSource in which a Speleothem is returned.
        // This ensures the randomList is constructed only once.
        return random -> randomList.getRandom(random).map(WeightedEntry.Wrapper::getData).orElse(BlockSpikeFeature.STONE_STALACTITE);
    }

    @NotNull
    private static Function<RandomSource, Stalactite> compileSpeleothems(List<Stalactite> stalactites) {
        WeightedRandomList<WeightedEntry.Wrapper<Stalactite>> randomList = WeightedRandomList.create(stalactites.stream().map(stalactite -> WeightedEntry.wrap(stalactite, stalactite.weight())).toList());

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
