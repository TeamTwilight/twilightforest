package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerMainComponent;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Optional;

public class MushroomTowerStructure extends LandmarkStructure {
	public static final MapCodec<MushroomTowerStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> landmarkCodec(instance).apply(instance, MushroomTowerStructure::new));

	public MushroomTowerStructure(Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new MushroomTowerMainComponent(random, 0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.MUSHROOM_TOWER.get();
	}

}
