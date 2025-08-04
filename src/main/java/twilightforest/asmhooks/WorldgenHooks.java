package twilightforest.asmhooks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

@SuppressWarnings({"JavadocReference", "unused"})
public class WorldgenHooks {

	/**
	 * {@link twilightforest.asm.transformers.beardifier.InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator#createNoiseChunk(ChunkAccess, StructureManager, Blender, RandomState)}
	 */
	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.iterator();
	}

	/**
	 * {@link twilightforest.asm.transformers.beardifier.BeardifierComputeTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link net.minecraft.world.level.levelgen.Beardifier#compute(DensityFunction.FunctionContext)}
	 */
	public static double getCustomDensity(double o, DensityFunction.FunctionContext context, @Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null)
			return o;

		double newDensity = 0;

		while (customDensities.hasNext()) {
			newDensity += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);

		return o + newDensity;
	}

	/**
	 * {@link twilightforest.asm.transformers.chunk.ChunkStatusTaskTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.chunk.status.ChunkStatusTasks#generateSurface(WorldGenContext, ChunkStep, StaticCache2D, ChunkAccess)}
	 */
	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	/**
	 * {@link twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.levelgen.structure.StructureStart#loadStaticStart(StructurePieceSerializationContext, CompoundTag, long)}<br/>
	 * Targets: {@link net.minecraft.world.level.levelgen.structure.StructureStart#StructureStart(Structure, ChunkPos, int, PiecesContainer)}
	 */
	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}
