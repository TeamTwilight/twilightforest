package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.minotaurmaze.MazeRuinsComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LabyrinthStructure extends ControlledSpawningStructure implements ConfigurableSpawns, CustomDensitySource {
    public static final Codec<LabyrinthStructure> CODEC = RecordCodecBuilder.create(instance ->
            controlledSpawningCodec(instance).apply(instance, LabyrinthStructure::new)
    );

    public LabyrinthStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new MazeRuinsComponent(0, x + 5, y, z + 5); // Offset centers labyrinth mound on intersection of 4 chunk boundaries
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LABYRINTH.get();
    }

    public static LabyrinthStructure buildLabyrinthConfig(BootstapContext<Structure> context) {
        return new LabyrinthStructure(
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(TFEntities.MINOTAUR.get(), 20, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.MAZE_SLIME.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 10, 1, 1)
                ),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(HintConfig.book("labyrinth", 5), TFEntities.KOBOLD.get()),
                new DecorationConfig(3, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LABYRINTH_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }

    @Override
    public DensityFunction getStructureTerraformer(ChunkPos chunkSliceAt, StructureStart structurePieceSource) {
        final float radius = 35;

        final BoundingBox structureBox = structurePieceSource.getBoundingBox();
        final BlockPos hillCenter = structureBox.getCenter();
        final int yCeilingFocus = hillCenter.getY();

        // Main mound density field. All values above mount surface are 0 while other values under the mound are 1.
        DensityFunction hillMound = new HollowHillFunction(hillCenter.getX() + 1, hillCenter.getY() + 7, hillCenter.getZ() + 2f, radius, 0.8f)
                .clamp(0, 2);

        //if (true) return hillMound;

        // Similar field like above, but all per-position values multiplied by -1. Positive terrain field above (hill mound)
        DensityFunction ceilingCapped = DensityFunctions.yClampedGradient(5, 6, -1, 1);
        //if (true) return innerCeiling;

        // Floor leading up inwards, offering chance for terrain to slop upwards if otherwise submerged
        BlockPos pos = hillCenter.offset(1, 0, 1);
        DensityFunction innerFloor = DensityFunctions.add(
                DensityFunctions.yClampedGradient(-4, 3, 26, -1),
                DensityFunctions.mul(
                        DensityFunctions.constant(-1),
                        new AbsoluteDifferenceFunction.Max(32, pos.getX() + 0.5f, pos.getZ() + 0.5f)
                )
        );

        // Interior entrances
        DensityFunction entrances = DensityFunctions.max(
                ceilingCapped,
                DensityFunctions.add(
                        DensityFunctions.constant(-2),
                        new AbsoluteDifferenceFunction.Min(32, pos.getX() + 0.5f, pos.getZ() + 0.5f)
                )
        );
        //if (true) return entrances;

        // Merge the inner ceiling & inner floor density functions, and obtain the maximum value.
        // Resulting terrain field will "carve" out the interior space, using negative field values past 0.
        DensityFunction interior = DensityFunctions.max(entrances, innerFloor).clamp(0, 1);
        //if (true) return interior;

        DensityFunction interiorMask = FocusedDensityFunction.fromPos(hillCenter.atY(yCeilingFocus), radius * 0.7f, radius, 0);

        DensityFunction interiorMasked = DensityFunctions.lerp(interiorMask.clamp(0, 1), DensityFunctions.zero(), interior);

        // Finally combine the hill mound & interior fields, resulting field containing per-position minimums from both.
        // Everything above the hill mound surface are zeros. The interior's field has negative values where "inside" is and positive values where "not inside" is, with zeros forming the interior surfaces.
        // This min() function combines these two surfaces formed by said zeros
        DensityFunction hollowHill = DensityFunctions.min(hillMound, interiorMasked);

        return hollowHill;
    }
}
