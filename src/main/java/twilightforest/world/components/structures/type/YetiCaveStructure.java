package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
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
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.YetiCaveComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YetiCaveStructure extends ControlledSpawningStructure implements CustomDensitySource {
    public static final Codec<YetiCaveStructure> CODEC = RecordCodecBuilder.create(instance ->
            controlledSpawningCodec(instance)
                    .and(StructureSpeleothemConfigs.CODEC.fieldOf("speleothem_config").forGetter(s -> s.speleothemConfig))
                    .apply(instance, YetiCaveStructure::new)
    );

    private final Holder.Reference<StructureSpeleothemConfig> speleothemConfig;

    public YetiCaveStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings, Holder<StructureSpeleothemConfig> speleothemConfig) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);

        this.speleothemConfig = (Holder.Reference<StructureSpeleothemConfig>) speleothemConfig;
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new YetiCaveComponent(0, x, y + 4, z, this.speleothemConfig);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.YETI_CAVE.get();
    }

    @Override
    protected boolean dontCenter() {
        return true;
    }

    public static YetiCaveStructure buildYetiCaveConfig(BootstapContext<Structure> context) {
        return new YetiCaveStructure(
                ControlledSpawningConfig.firstIndexMonsters(new MobSpawnSettings.SpawnerData(TFEntities.YETI.get(), 5, 1, 2)),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(HintConfig.book("yeticave", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(2, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_YETI_CAVE_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.YETI_CAVE)
        );
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
        int riser = 5;

        BlockPos centerPos = structurePieceSource.getBoundingBox().getCenter().above(riser);

        // Makes exterior "cut pyramid", trapezoid prism
        DensityFunction exteriorTrapezoidSq = DensityFunctions.add(
                DensityFunctions.mul(
                        DensityFunctions.constant(-1),
                        AbsoluteDifferenceFunction.max(40, centerPos)
                ),
                DensityFunctions.add(
                        DensityFunctions.yClampedGradient(0, 24 + riser, 40, 32), // Outside sloping walls
                        DensityFunctions.yClampedGradient(21 + riser, 22 + riser, 0, -100000) // Roof
                )
        );
        // if (true) return exteriorTrapezoidSq;

        // Makes floor and ceiling
        int deltaToGround = 8;
        int deltaToAir = 4;
        int yPosTop = 14 + riser;
        int yPosBottom = -3 + riser;
        DensityFunction floorCeiling = DensityFunctions.add(
                DensityFunctions.mul(
                        DensityFunctions.constant(32),
                        DensityFunctions.max(
                            DensityFunctions.yClampedGradient(yPosTop - deltaToAir, yPosTop + deltaToGround, -1, 1), // Ceiling
                            DensityFunctions.yClampedGradient(yPosBottom - deltaToGround, yPosBottom + deltaToAir, 1, -1) // Floor
                        )
                ),
                DensityFunctions.add(
                        DensityFunctions.constant(0),
                        AbsoluteDifferenceFunction.min(64, centerPos)
                ).clamp(1, 64)
        );
        //if (true) return floorCeiling;

        DensityFunction walls = DensityFunctions.yClampedGradient(-3 + riser, 21 + riser, -9.5, -2.5);

        // Entrances & actual square interior, as a negative
        DensityFunction interiorCarver = DensityFunctions.min(
                // Entrances (+ shaped)
                AbsoluteDifferenceFunction.min(64, centerPos),
                // Square interior "room" box
                DensityFunctions.add(
                        DensityFunctions.constant(-20),
                        AbsoluteDifferenceFunction.max(64, centerPos)
                )
        );

        // Adding these two gives the interior sloping walls
        DensityFunction shapedInterior = DensityFunctions.add(
                walls,
                interiorCarver
        );
        //if (true) return shapedInterior;

        // Fully-enclosed interior, with walls and ceiling
        DensityFunction interior = DensityFunctions.max(
                floorCeiling,
                shapedInterior
        );
        //if (true) return interior;

        // Entrance sloping
        DensityFunction entranceSlope = DensityFunctions.add(
                DensityFunctions.yClampedGradient(-8, 16 + riser, -46, -48),
                AbsoluteDifferenceFunction.max(64, centerPos)
        );

        var interiorWithSmoothedEntrances = DensityFunctions.max(
                interior,
                DensityFunctions.mul(
                        DensityFunctions.constant(1/64f),
                        DensityFunctions.add(
                                DensityFunctions.constant(15f),
                                entranceSlope
                        )
                )
        );

        DensityFunction yetiCave = DensityFunctions.lerp(
                DensityFunctions.mul(
                        DensityFunctions.constant(0.020833334f),
                        exteriorTrapezoidSq
                ).clamp(0, 1),
                DensityFunctions.zero(),
                DensityFunctions.mul(DensityFunctions.constant(16), interiorWithSmoothedEntrances)
        );

        return yetiCave;
    }
}
