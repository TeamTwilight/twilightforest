package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final DeferredRegister<MapCodec<? extends StructureProcessor>> STRUCTURE_PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<CobbleVariants>> COBBLE_VARIANTS = STRUCTURE_PROCESSORS.register("cobble_variants", () -> CobbleVariants.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<SmoothStoneVariants>> SMOOTH_STONE_VARIANTS = STRUCTURE_PROCESSORS.register("smooth_stone_variants", () -> SmoothStoneVariants.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<StoneBricksVariants>> STONE_BRICK_VARIANTS = STRUCTURE_PROCESSORS.register("stone_brick_variants", () -> StoneBricksVariants.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<InfestBlocksProcessor>> INFEST_BLOCKS = STRUCTURE_PROCESSORS.register("infest_blocks", () -> InfestBlocksProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<NagastoneVariants>> NAGASTONE_VARIANTS = STRUCTURE_PROCESSORS.register("nagastone_variants", () -> NagastoneVariants.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<StateTransfiguringProcessor>> STATE_TRANSFIGURING = STRUCTURE_PROCESSORS.register("state_transfiguring", () -> StateTransfiguringProcessor.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<WoodPaletteSwizzle>> PLANK_SWIZZLE = STRUCTURE_PROCESSORS.register("wood_swizzle", () -> WoodPaletteSwizzle.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<SmartGrassProcessor>> SMART_GRASS = STRUCTURE_PROCESSORS.register("smart_grass", () -> SmartGrassProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<BoxCuttingProcessor>> BOX_CUTTING_PROCESSOR = STRUCTURE_PROCESSORS.register("box_cutting", () -> BoxCuttingProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<TargetedRotProcessor>> TARGETED_ROT = STRUCTURE_PROCESSORS.register("targeted_rot", () -> TargetedRotProcessor.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<GraveyardFeature.WebTemplateProcessor>> WEB = STRUCTURE_PROCESSORS.register("web", () -> GraveyardFeature.WebTemplateProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<CourtyardTerraceTemplateProcessor>> COURTYARD_TERRACE = STRUCTURE_PROCESSORS.register("courtyard_terrace", () -> CourtyardTerraceTemplateProcessor.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<SoftReplaceProcessor>> SOFT_REPLACE = STRUCTURE_PROCESSORS.register("soft_replace", () -> SoftReplaceProcessor.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<SpawnerProcessor>> SPAWNER_PROCESSOR = STRUCTURE_PROCESSORS.register("spawner_processor", () -> SpawnerProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<UpdateMarkingProcessor>> UPDATE_MARKING_PROCESSOR = STRUCTURE_PROCESSORS.register("update_marking", () -> UpdateMarkingProcessor.CODEC);

	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<VerticalDecayProcessor>> VERTICAL_DECAY = STRUCTURE_PROCESSORS.register("vertical_decay", () -> VerticalDecayProcessor.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<WoodMultiPaletteSwizzle>> PLANK_MULTISWIZZLE = STRUCTURE_PROCESSORS.register("wood_multiswizzle", () -> WoodMultiPaletteSwizzle.CODEC);
}
