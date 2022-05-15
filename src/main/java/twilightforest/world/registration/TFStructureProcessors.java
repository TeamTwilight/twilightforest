package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;

import java.util.function.Supplier;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<StructureProcessorType<CobbleVariants>> COBBLE_VARIANTS = registerProcessor("cobble_variants", () -> () -> CobbleVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<SmoothStoneVariants>> SMOOTH_STONE_VARIANTS = registerProcessor("smooth_stone_variants", () -> () -> SmoothStoneVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<StoneBricksVariants>> STONE_BRICK_VARIANTS = registerProcessor("stone_brick_variants", () -> () -> StoneBricksVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<NagastoneVariants>> NAGASTONE_VARIANTS = registerProcessor("nagastone_variants", () -> () -> NagastoneVariants.CODEC);

	public static final RegistryObject<StructureProcessorType<CobblePlankSwizzler>> COBBLE_PLANK_SWIZZLER = registerProcessor("cobble_plank_swizzler", () -> () -> CobblePlankSwizzler.CODEC);
	public static final RegistryObject<StructureProcessorType<SmartGrassProcessor>> SMART_GRASS = registerProcessor("smart_grass", () -> () -> SmartGrassProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<BoxCuttingProcessor>> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", () -> () -> BoxCuttingProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<TargetedRotProcessor>> TARGETED_ROT = registerProcessor("targeted_rot", () -> () -> TargetedRotProcessor.CODEC);

	public static final RegistryObject<StructureProcessorType<GraveyardFeature.WebTemplateProcessor>> WEB = registerProcessor("web", () -> () -> GraveyardFeature.WebTemplateProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<CourtyardTerraceTemplateProcessor>> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", () -> () -> CourtyardTerraceTemplateProcessor.CODEC);

	//goofy but needed
	public static <P extends StructureProcessor> RegistryObject<StructureProcessorType<P>> registerProcessor(String name, Supplier<StructureProcessorType<P>> processor) {
		return STRUCTURE_PROCESSORS.register(name, processor);
	}
}
