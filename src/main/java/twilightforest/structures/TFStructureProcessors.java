package twilightforest.structures;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import twilightforest.TwilightForestMod;
import twilightforest.worldgen.structures.GenDruidHut;
import twilightforest.worldgen.structures.TFGenGraveyard;
import twilightforest.structures.courtyard.CourtyardStairsTemplateProcessor;
import twilightforest.structures.courtyard.CourtyardTerraceTemplateProcessor;
import twilightforest.structures.courtyard.CourtyardWallTemplateProcessor;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final IStructureProcessorType<?> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", CourtyardTerraceTemplateProcessor.codecTerraceProcessor);
	public static final IStructureProcessorType<?> COURTYARD_STAIRS = registerProcessor("courtyard_stairs", CourtyardStairsTemplateProcessor.codecStairsProcessor);
	public static final IStructureProcessorType<?> COURTYARD_WALL = registerProcessor("courtyard_wall", CourtyardWallTemplateProcessor.codecWallProcessor);
	public static final IStructureProcessorType<?> MOSSY_COBBLE = registerProcessor("mossy_cobble", MossyCobbleTemplateProcessor.codecMossyProcessor);
	public static final IStructureProcessorType<?> HUT = registerProcessor("hut", GenDruidHut.HutTemplateProcessor.codecHutProcessor);
	public static final IStructureProcessorType<?> WEB = registerProcessor("hut", TFGenGraveyard.WebTemplateProcessor.codecWebProcessor);

	public static <P extends StructureProcessor> IStructureProcessorType<P> registerProcessor(String name, Codec<P> processor) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, TwilightForestMod.prefix(name), () -> processor);
	}
}
