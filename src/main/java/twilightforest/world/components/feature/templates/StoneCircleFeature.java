package twilightforest.world.components.feature.templates;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.CobbleVariants;

public class StoneCircleFeature extends TemplateFeature {

	private static final Identifier STONE_CIRCLE = TwilightForestMod.prefix("feature/ruins/stone_circle");

	public static final MapCodec<StoneCircleFeature> MAP_CODEC = MapCodec.unit(StoneCircleFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	protected @Nullable StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(STONE_CIRCLE);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random) {
		settings.addProcessor(CobbleVariants.INSTANCE);
	}

	@Override
	protected int yLevelOffset() {
		return -1;
	}
}
