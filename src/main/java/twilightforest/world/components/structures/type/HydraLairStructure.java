package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.HydraLairComponent;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HydraLairStructure extends ProgressionStructure implements CustomDensitySource {
    public static final Codec<HydraLairStructure> CODEC = RecordCodecBuilder.create(instance ->
            progressionCodec(instance)
                    .and(StructureSpeleothemConfigs.CODEC.fieldOf("speleothem_config").forGetter(s -> s.speleothemConfig))
                    .apply(instance, HydraLairStructure::new)
    );

    private final Holder.Reference<StructureSpeleothemConfig> speleothemConfig;

    public HydraLairStructure(AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings, Holder<StructureSpeleothemConfig> speleothemConfig) {
        super(advancementLockConfig, hintConfig, decorationConfig, structureSettings);

        this.speleothemConfig = (Holder.Reference<StructureSpeleothemConfig>) speleothemConfig;
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new HydraLairComponent(0, x - 7, y, z - 7, this.speleothemConfig);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.HYDRA_LAIR.get();
    }

    public static HydraLairStructure buildHydraLairConfig(BootstapContext<Structure> context) {
        return new HydraLairStructure(
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_labyrinth"))),
                new HintConfig(HintConfig.book("hydralair", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(2, false, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HYDRA_LAIR_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.HYDRA_LAIR)
        );
    }

    @Override
    public DensityFunction getStructureTerraformer(ChunkPos chunkSliceAt, StructureStart structurePieceSource) {
        final int hillSize = 2;

        final float radius = (hillSize * 4 + 0.8f) * 8;
        final float radiusInner = radius - 8;

        final BoundingBox structureBox = structurePieceSource.getBoundingBox();
        final int width = Math.min(structureBox.getXSpan(), structureBox.getZSpan());
        final int yCeilingFocus = structureBox.minY();
        final BlockPos hillCenter = structureBox.getCenter();

        // Main mound density field. All values above mount surface are 0 while other values under the mound are 1.
        DensityFunction hillMound = HollowHillFunction.fromPos(hillCenter.atY(yCeilingFocus + 8), radius, 0.7f)
                .clamp(0, 1);

        // Field that domes upwards instead of downwards like HollowHillFunction
        // Negative terrain field above (inner hill gap) and positive terrain field below (stone underground)
        DensityFunction innerFloor = DensityFunctions.yClampedGradient(-1, 0, 1, -4);

        // Similar field like above, but all per-position values multiplied by -1. Positive terrain field above (hill mound) and negative terrain field below (inner hill gap)
        DensityFunction innerCeiling = DensityFunctions.mul(
                DensityFunctions.constant(-1),
                HollowHillFunction.fromPos(hillCenter.atY(yCeilingFocus + 6), radiusInner, 0.675f)
        );

        // Merge the inner ceiling & inner floor density functions, and obtain the maximum value.
        // Resulting terrain field will "carve" out the interior space, using negative field values past 0.
        DensityFunction interior = DensityFunctions.max(innerCeiling, innerFloor);

        DensityFunction interiorMask = FocusedDensityFunction.fromPos(hillCenter.atY(yCeilingFocus), radiusInner * 0.52f, -radiusInner, 1);

        DensityFunction interiorMasked = DensityFunctions.max(interiorMask, interior);

        // Finally combine the hill mound & interior fields, resulting field containing per-position minimums from both.
        // Everything above the hill mound surface are zeros. The interior's field has negative values where "inside" is and positive values where "not inside" is, with zeros forming the interior surfaces.
        // This min() function combines these two surfaces formed by said zeros
        DensityFunction hollowHill = DensityFunctions.min(hillMound, interiorMasked);

        DensityFunction maskingSphere = FocusedDensityFunction.fromPos(hillCenter.below(Mth.ceil(radius * 0.1)), width * 0.5f + 5, width * 0.25f, 0).clamp(0, 1);

        DensityFunction cutout = FocusedDensityFunction.fromPos(hillCenter.offset(-16, 0 ,-16), 23, -23, 0).clamp(-4, 0);

        DensityFunction hillMasked = DensityFunctions.mul(maskingSphere, hollowHill);

        DensityFunction lair = DensityFunctions.add(hillMasked, DensityFunctions.max(cutout, innerFloor).clamp(-2, 0));

        return DensityFunctions.mul(DensityFunctions.constant(8), lair);
    }
}
