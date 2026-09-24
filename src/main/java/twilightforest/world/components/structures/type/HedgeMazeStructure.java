package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.HedgeMazeComponent;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Optional;

public class HedgeMazeStructure extends LandmarkStructure implements CustomDensitySource {
	public static final MapCodec<HedgeMazeStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> landmarkCodec(instance).apply(instance, HedgeMazeStructure::new));

	public HedgeMazeStructure(Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new HedgeMazeComponent(0, x + 1, y + 4, z + 1);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.HEDGE_MAZE.get();
	}

	@Override
	public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
		return CustomDensitySource.getInvertedPyramidTerraformer(structurePieceSource, 0, 4);
	}

	@Override
	public int adjustForTerrain(GenerationContext context, int x, int z) {
		return WorldUtil.adjustForTerrain(context, x, z, 24, 4);
	}
}
