package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.finalcastle.FinalCastleMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FinalCastleStructure extends ControlledSpawningStructure {
    public static final Codec<FinalCastleStructure> CODEC = RecordCodecBuilder.create(instance ->
            controlledSpawningCodec(instance).apply(instance, FinalCastleStructure::new)
    );

    public FinalCastleStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new FinalCastleMainComponent(0, x, y, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.FINAL_CASTLE.get();
    }

    public static FinalCastleStructure buildFinalCastleConfig(BootstapContext<Structure> context) {
        return new FinalCastleStructure( // TODO Re-enable mob spawns when proper castle mobs are created
                ControlledSpawningConfig.create(List.of(List.of(
                        // plain parts of the castle, like the tower maze
                        //new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 1, 2),
                        //new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 10, 1, 1),
                        //new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 10, 1, 1),
                        //new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 1)
                ), List.of(
                        // internal castle
                        //new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 1, 2),
                        //new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 10, 1, 1),
                        //new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 10, 1, 1),
                        //new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT.get(), 10, 1, 1)
                ), List.of(
                        // dungeons
                        //new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 10, 1, 1)
                ), List.of(
                        // forge
                        //new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 10, 1, 1)
                )), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_troll"))),
                // TODO: change this when we make a book for the castle
                new HintConfig(HintConfig.defaultBook(), TFEntities.KOBOLD.get()),
                new DecorationConfig(4, false, true, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_FINAL_CASTLE_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_BOX
                )
        );
    }
}
