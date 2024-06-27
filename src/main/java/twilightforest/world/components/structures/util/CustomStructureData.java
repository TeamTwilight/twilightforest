package twilightforest.world.components.structures.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import twilightforest.world.components.structures.start.TFStructureStart;

public interface CustomStructureData {
	default TFStructureStart forDeserialization(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieces, CompoundTag nbt) {
		TFStructureStart start = new TFStructureStart(structure, chunkPos, references, pieces);
		start.load(nbt);
		return start;
	}
}
