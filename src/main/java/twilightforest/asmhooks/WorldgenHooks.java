package twilightforest.asmhooks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

@SuppressWarnings({"JavadocReference", "unused"})
public class WorldgenHooks {

	public static ObjectList<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		TwilightForestMod.LOGGER.debug("TF-WorldgenHooks: Gathered {} custom densities for chunk ({},{})", customStructureTerraforms.size(), chunkPos.x(), chunkPos.z());
		return customStructureTerraforms;
	}

	public static double getCustomDensity(double original, DensityFunction.FunctionContext context, ObjectList<DensityFunction> customDensities) {
		if (customDensities == null || customDensities.isEmpty())
			return original;

		double addedDensity = 0;

		for (int i = 0; i < customDensities.size(); i++) {
			addedDensity += customDensities.get(i).compute(context);
		}

		return original + addedDensity;
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}
