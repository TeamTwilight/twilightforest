package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
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
import twilightforest.world.components.structures.QuestGrove;
import twilightforest.world.components.structures.util.ConquerableStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QuestGroveStructure extends ConquerableStructure {
    public static final Codec<QuestGroveStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, QuestGroveStructure::new));

    public QuestGroveStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new QuestGrove(context.structureTemplateManager(), new BlockPos(x - 12, y + 2, z - 12));
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.QUEST_GROVE.get();
    }

    public static QuestGroveStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new QuestGroveStructure(
                new DecorationConfig(1, false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
