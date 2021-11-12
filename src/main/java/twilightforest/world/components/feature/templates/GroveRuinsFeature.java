package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.StoneBricksVariants;

import javax.annotation.Nullable;
import java.util.Random;

public class GroveRuinsFeature extends TemplateFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation PILLAR = TwilightForestMod.prefix("feature/ruins/grove_pillar");
    private static final ResourceLocation ARCH = TwilightForestMod.prefix("feature/ruins/grove_arch");

    public GroveRuinsFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
        return templateManager.getOrCreate(random.nextBoolean() ? PILLAR : ARCH);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, Random random) {
        settings.addProcessor(StoneBricksVariants.INSTANCE);
    }
}
