package twilightforest.world.registration;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;
import twilightforest.world.components.processors.NagastoneVariants;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {
	public static final StructureProcessorType<CobbleVariants> COBBLE_VARIANTS = registerProcessor("cobble_variants", CobbleVariants.CODEC);
	public static final StructureProcessorType<SmoothStoneVariants> SMOOTH_STONE_VARIANTS = registerProcessor("smooth_stone_variants", SmoothStoneVariants.CODEC);
	public static final StructureProcessorType<StoneBricksVariants> STONE_BRICK_VARIANTS = registerProcessor("stone_brick_variants", StoneBricksVariants.CODEC);
	public static final StructureProcessorType<NagastoneVariants> NAGASTONE_VARIANTS = registerProcessor("nagastone_variants", NagastoneVariants.CODEC);

	public static final StructureProcessorType<CobblePlankSwizzler> COBBLE_PLANK_SWIZZLER = registerProcessor("cobble_plank_swizzler", CobblePlankSwizzler.CODEC);
	public static final StructureProcessorType<SmartGrassProcessor> SMART_GRASS = registerProcessor("smart_grass", SmartGrassProcessor.CODEC);
	public static final StructureProcessorType<BoxCuttingProcessor> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", BoxCuttingProcessor.CODEC);
	public static final StructureProcessorType<TargetedRotProcessor> TARGETED_ROT = registerProcessor("targeted_rot", TargetedRotProcessor.CODEC);

	public static final StructureProcessorType<GraveyardFeature.WebTemplateProcessor> WEB = registerProcessor("web", GraveyardFeature.WebTemplateProcessor.CODEC);
	public static final StructureProcessorType<CourtyardTerraceTemplateProcessor> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", CourtyardTerraceTemplateProcessor.CODEC);

	public static <P extends StructureProcessor> StructureProcessorType<P> registerProcessor(String name, Codec<P> processor) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, TwilightForestMod.prefix(name), () -> processor);
	}
}
