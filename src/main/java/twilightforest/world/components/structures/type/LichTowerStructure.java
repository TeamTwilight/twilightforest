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
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LichTowerStructure extends ControlledSpawningStructure {
    public static final Codec<LichTowerStructure> CODEC = RecordCodecBuilder.create(instance ->
            controlledSpawningCodec(instance).apply(instance, LichTowerStructure::new)
    );

    public LichTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new TowerMainComponent(random, 0, x, y, z);
        //return new TowerFoyer(context.structureTemplateManager(), new BlockPos(x, y + 1, z));
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LICH_TOWER.get();
    }

    @Override
    protected boolean dontCenter() {
        return true;
    }

    public static LichTowerStructure buildLichTowerConfig(BootstapContext<Structure> context) {
        return new LichTowerStructure(
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.get(), 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
                ),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
                new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(1, false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
