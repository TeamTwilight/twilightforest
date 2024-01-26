package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.darktower.DarkTowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DarkTowerStructure extends ControlledSpawningStructure {
    public static final Codec<DarkTowerStructure> CODEC = RecordCodecBuilder.create(instance ->
            controlledSpawningCodec(instance).apply(instance, DarkTowerStructure::new)
    );

    public DarkTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new DarkTowerMainComponent(random, 0, x, y, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.DARK_TOWER.get();
    }

    public static DarkTowerStructure buildDarkTowerConfig(BootstapContext<Structure> context) {
        return new DarkTowerStructure(
                ControlledSpawningConfig.create(List.of(List.of(
                        new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GOLEM.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 5, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 2, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1),
                        new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GHASTLING.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_BROODLING.get(), 10, 4, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 10, 1, 1)
                ), List.of(
                        // roof ghasts
                        new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GHASTGUARD.get(), 10, 1, 2)
                )), List.of(), List.of(
                        // aquarium squids (only in aquariums between y = 35 and y = 64. :/
                        new MobSpawnSettings.SpawnerData(EntityType.SQUID, 10, 4, 4)
                )),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_knights"))),
                new HintConfig(HintConfig.book("darktower", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(1, false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_DARK_TOWER_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
