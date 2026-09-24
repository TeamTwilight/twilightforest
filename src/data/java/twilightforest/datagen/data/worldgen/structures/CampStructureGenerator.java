package twilightforest.datagen.data.worldgen.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.type.CampStructure;

import java.util.Map;

public class CampStructureGenerator {

    public static CampStructure buildStructureConfig(BootstrapContext<Structure> context) {
        return new CampStructure(new Structure.StructureSettings(
            context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_CAMP_BIOMES),
            Map.of(),
            GenerationStep.Decoration.SURFACE_STRUCTURES,
            TerrainAdjustment.BEARD_BOX
        ));
    }

}
