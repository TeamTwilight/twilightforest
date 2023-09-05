package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.stronghold.StrongholdEntranceComponent;
import twilightforest.world.components.structures.util.ConquerableStructure;

import java.util.List;
import java.util.Map;

public class KnightStrongholdStructure extends ConquerableStructure {
    public static final Codec<KnightStrongholdStructure> CODEC = RecordCodecBuilder.create(instance ->
            conquerStatusCodec(instance).apply(instance, KnightStrongholdStructure::new)
    );

    public KnightStrongholdStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new StrongholdEntranceComponent(0, x, y + 5, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.KNIGHT_STRONGHOLD.get();
    }

    public static KnightStrongholdStructure buildKnightStrongholdConfig(BootstapContext<Structure> context) {
        return new KnightStrongholdStructure(
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(TFEntities.BLOCKCHAIN_GOBLIN.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.LOWER_GOBLIN_KNIGHT.get(), 5, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.HELMET_CRAB.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(TFEntities.REDCAP_SAPPER.get(), 2, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 5, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SLIME, 5, 4, 4)
                ),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_trophy_pedestal"))),
                new HintConfig(HintConfig.book("tfstronghold", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(3, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_KNIGHT_STRONGHOLD_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }
}
