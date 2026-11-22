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
import twilightforest.world.components.processors.StoneBricksVariants;

public class GroveRuinsFeature extends TemplateFeature {

	private static final Identifier PILLAR = TwilightForestMod.prefix("feature/ruins/grove_pillar");
	private static final Identifier ARCH = TwilightForestMod.prefix("feature/ruins/grove_arch");

	public static final MapCodec<GroveRuinsFeature> MAP_CODEC = MapCodec.unit(GroveRuinsFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	protected @Nullable StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(random.nextBoolean() ? PILLAR : ARCH);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random) {
		settings.addProcessor(StoneBricksVariants.INSTANCE);
	}
}
