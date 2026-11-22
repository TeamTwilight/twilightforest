package twilightforest.world.components.feature.templates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.processors.SmartGrassProcessor;

public class FancyWellFeature extends TemplateFeature {

	private static final Identifier WELL_TOP = TwilightForestMod.prefix("feature/well/fancy_well_top");
	private static final Identifier WELL_BOTTOM = TwilightForestMod.prefix("feature/well/fancy_well_bottom");

	public static final MapCodec<FancyWellFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		SwizzleConfig.MAP_CODEC.forGetter(FancyWellFeature::getConfig)
	).apply(instance, FancyWellFeature::new));

	private final SwizzleConfig config;

	public FancyWellFeature(SwizzleConfig config) {
		this.config = config;
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	protected @Nullable StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(WELL_TOP);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random) {
		config.buildAddProcessors(settings, random);
	}

	@Override
	protected void postPlacement(WorldGenLevel world, RandomSource random, StructureTemplateManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos) {
		StructureTemplate template = templateManager.getOrCreate(WELL_BOTTOM);

		if (template == null)
			return;

		placementPos = placementPos.below(template.getSize().getY());//.relative(rotation.rotate(mirror.mirror(Direction.SOUTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);
		placementSettings.addProcessor(SmartGrassProcessor.INSTANCE);

		template.placeInWorld(world, placementPos, placementPos, placementSettings, random, Block.UPDATE_CLIENTS);

		for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK)) {
			StructureMode mode = StructureMode.valueOf(info.nbt().getString("mode").orElseThrow());
			if (info.nbt() != null && mode == StructureMode.DATA)
				this.processMarkers(info, world, rotation, mirror, random);
		}
	}

	@Override
	protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, RandomSource random) {
		String s = info.nbt().getStringOr("metadata", "");

		if (!s.startsWith("loot"))
			return;

		world.removeBlock(info.pos(), false);

		TFLootTables.generateLootContainer(world, info.pos(), Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, TFLootTables.FANCY_WELL);
	}

	public SwizzleConfig getConfig() {
		return config;
	}
}
