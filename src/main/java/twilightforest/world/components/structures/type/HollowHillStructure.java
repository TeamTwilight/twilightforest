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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import twilightforest.TFRegistries;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.TFStructureTypes;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.HollowHillComponent;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HollowHillStructure extends LandmarkStructure implements ConfigurableSpawns, CustomDensitySource {
    public static final Codec<HollowHillStructure> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    // TODO Clean up findGenerationPoint() first before even thinking about increasing upper limit
                    Codec.intRange(1, 3).fieldOf("hill_size").forGetter(s -> s.size),
                    ControlledSpawningConfig.FLAT_CODEC.forGetter(s -> s.controlledSpawningConfig),
                    StructureSpeleothemConfigs.CODEC.fieldOf("speleothem_config").forGetter(s -> s.speleothemConfig)
            )
            .and(landmarkCodec(instance))
            .apply(instance, HollowHillStructure::new)
    );

    private final int size;
    private final ControlledSpawningConfig controlledSpawningConfig;
    private final Holder.Reference<StructureSpeleothemConfig> speleothemConfig;

    public HollowHillStructure(int size, ControlledSpawningConfig controlledSpawningConfig, Holder<StructureSpeleothemConfig> speleothemConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
        this.size = size;
        this.controlledSpawningConfig = controlledSpawningConfig;
        this.speleothemConfig = (Holder.Reference<StructureSpeleothemConfig>) speleothemConfig;
    }

    // "Cuts" the box into a half-dome
    public boolean canSpawnMob(BlockPos spawnPos, BoundingBox structureStartBox) {
        float hX = Mth.inverseLerp(spawnPos.getX(), structureStartBox.minX(), structureStartBox.maxX()) * 2 - 1;
        float hY = Mth.inverseLerp(spawnPos.getY(), structureStartBox.minY(), structureStartBox.maxY());
        float hZ = Mth.inverseLerp(spawnPos.getZ(), structureStartBox.minZ(), structureStartBox.maxZ()) * 2 - 1;

        return Mth.length(hX, hY, hZ) < 0.975f;
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return switch (this.size) { // TODO Clean up once TFLandmark params are no longer necessary
            case 1 -> new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, this.size, x - 3, y - 2, z - 3, this.speleothemConfig);
            case 2 -> new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, this.size, x - 7, y - 5, z - 7, this.speleothemConfig);
            default -> new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, this.size, x - 11, y - 5, z - 11, this.speleothemConfig);
        };
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.HOLLOW_HILL.get();
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }

    public static HollowHillStructure buildSmallHillConfig(BootstapContext<Structure> context) {
        return new HollowHillStructure(
                1,
                ControlledSpawningConfig.create(List.of(List.of(
                        new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 4, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 4, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP.get(), 10, 4, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.SWARM_SPIDER.get(), 10, 4, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 4, 8)
                )), List.of(), List.of()),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.SMALL_HILL),
                new DecorationConfig(1, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }

    public static HollowHillStructure buildMediumHillConfig(BootstapContext<Structure> context) {
        return new HollowHillStructure(
                2,
                ControlledSpawningConfig.create(List.of(List.of(
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP_SAPPER.get(), 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(TFEntities.SWARM_SPIDER.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 1, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 5, 1, 1),
                        new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 5, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
                )), List.of(), List.of()),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.MEDIUM_HILL),
                new DecorationConfig(2, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }

    public static HollowHillStructure buildLargeHillConfig(BootstapContext<Structure> context) {
        return new HollowHillStructure(
                3,
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP_SAPPER.get(), 2, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 1),
                        new MobSpawnSettings.SpawnerData(TFEntities.WRAITH.get(), 2, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
                ),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.LARGE_HILL),
                new DecorationConfig(3, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }

    @Override
    public DensityFunction getStructureTerraformer(ChunkPos chunkSliceAt, StructureStart structurePieceSource) {
        int hillSize = this.size;

        final float radius = (hillSize * 4 + 0.8f) * 8;
        final float radiusInner = radius - 8;

        final BoundingBox structureBox = structurePieceSource.getBoundingBox();
        final int width = Math.min(structureBox.getXSpan(), structureBox.getZSpan());
        final int yCeilingFocus = structureBox.minY();
        final BlockPos hillCenter = structureBox.getCenter();

        // Main mound density field. All values above mount surface are 0 while other values under the mound are 1.
        DensityFunction hillMound = HollowHillFunction.fromPos(hillCenter.atY(yCeilingFocus + 10), radius, 0.7f)
                .clamp(0, 1);

        // Similar field like above, but all per-position values multiplied by -1. Positive terrain field above (hill mound) and negative terrain field below (inner hill gap)
        DensityFunction innerCeiling = DensityFunctions.mul(
                DensityFunctions.constant(-1),
                HollowHillFunction.fromPos(hillCenter.atY(yCeilingFocus + 6), radiusInner, 0.675f)
        );

        // Field that domes upwards instead of downwards like above 2 DensityFunctions.
        // Negative terrain field above (inner hill gap) and positive terrain field below (stone underground)
        DensityFunction innerFloor = HollowHillFunction.fromPos(hillCenter.atY(yCeilingFocus + hillSize + hillSize / 2), 2 - radiusInner, 1/10f);

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

        return DensityFunctions.mul(maskingSphere, DensityFunctions.mul(DensityFunctions.constant(8), hollowHill));
    }
}
