package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

import java.util.function.Supplier;

public class TFStructurePlacementTypes {
	public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, TwilightForestMod.ID);

	public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<LandmarkGridPlacement>> GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("landmark_grid", () -> () -> LandmarkGridPlacement.CODEC);

    private static <P extends StructurePlacement> DeferredHolder<StructurePlacementType<?>, StructurePlacementType<P>> registerPlacer(String name, Supplier<StructurePlacementType<P>> factory) {
        return STRUCTURE_PLACEMENT_TYPES.register(name, factory);
    }
}
