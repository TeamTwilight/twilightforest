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
import twilightforest.world.components.structures.HedgeMazeComponent;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HedgeMazeStructure extends LandmarkStructure {
    public static final Codec<HedgeMazeStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, HedgeMazeStructure::new));

    public HedgeMazeStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new HedgeMazeComponent(0, x + 1, y + 4, z + 1);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.HEDGE_MAZE.get();
    }

    public static HedgeMazeStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new HedgeMazeStructure(
                new DecorationClearance.DecorationConfig(2, false, true, true),
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HEDGE_MAZE_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_BOX
                )
        );
    }
}
