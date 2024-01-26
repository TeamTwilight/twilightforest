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
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerMainComponent;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MushroomTowerStructure extends LandmarkStructure {
    public static final Codec<MushroomTowerStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, MushroomTowerStructure::new));

    public MushroomTowerStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new MushroomTowerMainComponent(random, 0, x, y, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.MUSHROOM_TOWER.get();
    }

    public static MushroomTowerStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new MushroomTowerStructure(
                new DecorationConfig(2, true, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_MUSHROOM_TOWER_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }
}
