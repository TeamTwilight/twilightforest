package twilightforest.world.components.structures.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.type.KnightStrongholdStructure;

public interface CustomStructureData {
	default TFStructureStart forDeserialization(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieces, CompoundTag nbt) {
		TFStructureStart start;
		if (!nbt.contains("knight_y")) start = new TFStructureStart(structure, chunkPos, references, pieces);
		else start = new KnightStrongholdStructure.KnightStructureStart(structure, chunkPos, references, pieces);
		start.loadFromTag(nbt);
		return start;
	}
}
