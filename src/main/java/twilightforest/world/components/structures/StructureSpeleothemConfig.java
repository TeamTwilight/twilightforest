package twilightforest.world.components.structures;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.data.custom.stalactites.entry.SpeleothemVarietyConfig;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public record StructureSpeleothemConfig(RectangleLatticeIterator.TriangularLatticeConfig latticeConfig, String speleothemVarietyType, Supplier<SpeleothemVarietyConfig> speleothemVarietyConfig, Supplier<List<Stalactite>> stalactiteVariety, Supplier<List<Stalactite>> oreStalactiteVariety, Supplier<List<Stalactite>> stalagmiteVariety) {
    public static final Codec<StructureSpeleothemConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RectangleLatticeIterator.TriangularLatticeConfig.CODEC.fieldOf("lattice").forGetter(StructureSpeleothemConfig::latticeConfig),
            Codec.STRING.xmap(String::toLowerCase, String::toLowerCase).fieldOf("type").forGetter(StructureSpeleothemConfig::speleothemVarietyType)
    ).apply(inst, StructureSpeleothemConfig::fromLocation));

    public static StructureSpeleothemConfig fromLocation(RectangleLatticeIterator.TriangularLatticeConfig latticeConfig, final String speleothemVarietyConfig) {
        return new StructureSpeleothemConfig(latticeConfig, speleothemVarietyConfig, Suppliers.memoize(() -> StalactiteReloadListener.HILL_CONFIGS.get(speleothemVarietyConfig)), Suppliers.memoize(() -> StalactiteReloadListener.STALACTITES_PER_HILL.get(speleothemVarietyConfig)), Suppliers.memoize(() -> StalactiteReloadListener.ORE_STALACTITES_PER_HILL.get(speleothemVarietyConfig)), Suppliers.memoize(() -> StalactiteReloadListener.STALAGMITES_PER_HILL.get(speleothemVarietyConfig)));
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

    public List<Stalactite> getStalactiteVariety() {
        return this.stalactiteVariety.get();
    }

    public List<Stalactite> getOreStalactiteVariety() {
        return this.oreStalactiteVariety.get();
    }

    public List<Stalactite> getStalagmiteVariety() {
        return this.stalagmiteVariety.get();
    }

    public Iterable<BlockPos.MutableBlockPos> latticeIterator(@Nullable BoundingBox bounds, int yLevel) {
        if (bounds == null)
            return List.of();

        return this.latticeConfig.boundedGrid(bounds, yLevel);
    }

    public Stalactite getRandomStalactiteFromList(RandomSource random, boolean hanging) {
        List<Stalactite> stalactites = hanging ? ((random.nextFloat() < this.getVarietyConfig().oreChance()) ? this.getOreStalactiteVariety() : this.getStalactiteVariety()) : this.getStalagmiteVariety();

        // FIXME Not a fan of constructing this list each & every time this method is called during each Speleothem, this should be memoized
        WeightedRandomList<WeightedEntry.Wrapper<Stalactite>> list = WeightedRandomList.create(stalactites.stream().map(stalactite -> WeightedEntry.wrap(stalactite, stalactite.weight())).toList());

        return list.getRandom(random).orElse(WeightedEntry.wrap(BlockSpikeFeature.STONE_STALACTITE, 1)).getData();
    }
}
