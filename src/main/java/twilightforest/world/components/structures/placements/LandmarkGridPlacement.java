package twilightforest.world.components.structures.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.Optional;

/**
 * Filters possible placements to only chunks actually demarcated to generate a Twilight Forest landmark structure
 * Does not filter for biome. That's for the structure's config to handle.
 */
public class LandmarkGridPlacement extends StructurePlacement {
    public static final Codec<LandmarkGridPlacement> CODEC = RecordCodecBuilder.create(inst -> inst.group(
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
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        if (!LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ))
            return false;

        return this.landmark.isEmpty() || LegacyLandmarkPlacements.pickVarietyLandmark(chunkX, chunkZ) == this.landmark.get();
    }

    @Override
    public StructurePlacementType<?> type() {
        return TFStructurePlacementTypes.GRID_LANDMARK_PLACEMENT_TYPE.get();
    }
}
