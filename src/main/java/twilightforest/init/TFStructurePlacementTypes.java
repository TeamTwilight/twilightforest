package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

public class TFStructurePlacementTypes {

	public static final DeferredRegister<MapCodec<? extends StructurePlacement>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<LandmarkGridPlacement>> GRID_LANDMARK_PLACEMENT_TYPE = STRUCTURE_PLACEMENT_TYPES.register("landmark_grid", () -> LandmarkGridPlacement.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<AvoidLandmarkGridPlacement>> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = STRUCTURE_PLACEMENT_TYPES.register("avoid_landmark_grid", () -> AvoidLandmarkGridPlacement.CODEC);
}
