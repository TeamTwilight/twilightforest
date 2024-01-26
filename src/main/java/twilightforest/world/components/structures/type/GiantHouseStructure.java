package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.trollcave.CloudCastleComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiantHouseStructure extends ProgressionStructure implements ConfigurableSpawns {
    public static final Codec<GiantHouseStructure> CODEC = RecordCodecBuilder.create(instance ->
            progressionCodec(instance)
                    .and(ControlledSpawningConfig.FLAT_CODEC.forGetter(ConfigurableSpawns::getConfig))
                    .apply(instance, GiantHouseStructure::new)
    );

    private final ControlledSpawningConfig controlledSpawningConfig;

    public GiantHouseStructure(AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings, ControlledSpawningConfig controlledSpawningConfig) {
        super(advancementLockConfig, hintConfig, decorationConfig, structureSettings);

        this.controlledSpawningConfig = controlledSpawningConfig;
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		// add cloud castle
		return new CloudCastleComponent(1, x, y + 168, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.GIANT_HOUSE.get();
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }

    @Override
    protected boolean dontCenter() {
        return true;
    }

    public static GiantHouseStructure buildGiantHouseConfig(BootstapContext<Structure> context) {
        return new GiantHouseStructure(
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_merge"))),
                new HintConfig(HintConfig.book("trollcave", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(1, true, true, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_GIANT_HOUSE_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                        TerrainAdjustment.NONE
                ),
                ControlledSpawningConfig.create(List.of(List.of( // cloud monsters
                        new MobSpawnSettings.SpawnerData(TFEntities.GIANT_MINER.get(), 10, 1, 1),
                        new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT.get(), 10, 1, 1)
                )), List.of(), List.of())
        );
    }
}
