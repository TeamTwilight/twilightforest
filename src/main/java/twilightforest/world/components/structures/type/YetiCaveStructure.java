package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.YetiCaveComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YetiCaveStructure extends ControlledSpawningStructure {
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
        return new YetiCaveComponent(0, x, y, z, this.speleothemConfig);
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
                        TerrainAdjustment.BURY
                ),
                context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.YETI_CAVE)
        );
    }
}
