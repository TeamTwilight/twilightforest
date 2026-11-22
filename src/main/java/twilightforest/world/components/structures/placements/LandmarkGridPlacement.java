package twilightforest.world.components.structures.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.Optional;

/**
 * Filters possible placements to only chunks actually demarcated to generate a Twilight Forest landmark structure
 * Does not filter for biome. That's for the structure's config to handle.
 */
public class LandmarkGridPlacement implements StructurePlacement {
	public static final MapCodec<LandmarkGridPlacement> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure_grid_lock").forGetter(p -> p.landmark)
	).apply(inst, LandmarkGridPlacement::new));

	private final Optional<ResourceKey<Structure>> landmark;

	// Using this will mean this structure will spawn at every center, unless its generation stub is actually blocked by the structure
	public static LandmarkGridPlacement forceStructureForCenters() {
		return new LandmarkGridPlacement(Optional.empty());
	}

	public LandmarkGridPlacement(Optional<ResourceKey<Structure>> landmark) {
		this.landmark = landmark;
	}


	@Override
	public boolean isStructureChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int i, int i1) {
		if (!LegacyLandmarkPlacements.chunkHasLandmarkCenter(i, i1))
			return false;

		return this.landmark.isEmpty() || LegacyLandmarkPlacements.pickVarietyLandmark(i, i1) == this.landmark.get();
	}

	@Override
	public MapCodec<? extends StructurePlacement> codec() {
		return CODEC;
	}
}
