package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.CobbleVariants;

public class StoneCircleFeature extends TemplateFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation STONE_CIRCLE = TwilightForestMod.prefix("feature/ruins/stone_circle");

    public StoneCircleFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
        return templateManager.getOrCreate(STONE_CIRCLE);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, RandomSource random, NoneFeatureConfiguration config) {
        settings.addProcessor(CobbleVariants.INSTANCE);
    }
}
