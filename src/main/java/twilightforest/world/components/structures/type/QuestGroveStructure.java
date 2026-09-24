package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.QuestGrove;
import twilightforest.world.components.structures.util.ConquerableStructure;

import java.util.Optional;

public class QuestGroveStructure extends ConquerableStructure {
	public static final MapCodec<QuestGroveStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> landmarkCodec(instance).apply(instance, QuestGroveStructure::new));
	public static final int LENGTH = 27;

	public QuestGroveStructure(Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new QuestGrove(context.structureTemplateManager(), new BlockPos(x - LENGTH / 2 + 1, y + 2, z - LENGTH / 2 + 1)); // + 1 offsets to center the structure
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.QUEST_GROVE.get();
	}

}
