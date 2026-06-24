package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.StoneBricksVariants;

public class GroveRuinsFeature extends TemplateFeature<NoneFeatureConfiguration> {
	private static final Identifier PILLAR = TwilightForestMod.prefix("feature/ruins/grove_pillar");
	private static final Identifier ARCH = TwilightForestMod.prefix("feature/ruins/grove_arch");

	public GroveRuinsFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Nullable
	@Override
	protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(random.nextBoolean() ? PILLAR : ARCH);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random, NoneFeatureConfiguration config) {
		settings.addProcessor(StoneBricksVariants.INSTANCE);
	}
}
