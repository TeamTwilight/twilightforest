package twilightforest.world.components.structures.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.Optional;

/**
 * Filters possible placements to only chunks actually demarcated to generate a Twilight Forest landmark structure
 * Does not filter for biome. That's for the structure's config to handle.
 */
public class LandmarkGridPlacement extends StructurePlacement {
	private static final Logger LOGGER = LoggerFactory.getLogger(LandmarkGridPlacement.class);

	public static final MapCodec<LandmarkGridPlacement> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure_grid_lock").forGetter(p -> p.landmark)
	).apply(inst, LandmarkGridPlacement::new));

	private final Optional<ResourceKey<Structure>> landmark;

	// Using this will mean this structure will spawn at every center, unless its generation stub is actually blocked by the structure
	public static LandmarkGridPlacement forceStructureForCenters() {
		return new LandmarkGridPlacement(Optional.empty());
	}

	public LandmarkGridPlacement(Optional<ResourceKey<Structure>> landmark) {
		super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1f, 0, Optional.empty()); // None of these params matter except for possibly flat-world or whatever

		this.landmark = landmark;
		LOGGER.info("[TF-PLACEMENT] LandmarkGridPlacement created with landmark: {}", landmark.map(k -> k.identifier().toString()).orElse("empty (forced centers)"));
	}

	@Override
	protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
		boolean hasCenter = LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ);
		if (!hasCenter)
			return false;

		boolean result = this.landmark.isEmpty() || LegacyLandmarkPlacements.pickVarietyLandmark(chunkX, chunkZ).equals(this.landmark.get());
		if (result && this.landmark.isPresent()) {
			LOGGER.debug("[TF-PLACEMENT] Chunk [{}, {}] matched for landmark {}", chunkX, chunkZ, this.landmark.get().identifier().toString());
		} else if (result) {
			LOGGER.debug("[TF-PLACEMENT] Chunk [{}, {}] matched for forced center", chunkX, chunkZ);
		}
		return result;
	}

	@Override
	public StructurePlacementType<?> type() {
		return TFStructurePlacementTypes.GRID_LANDMARK_PLACEMENT_TYPE.get();
	}
}
