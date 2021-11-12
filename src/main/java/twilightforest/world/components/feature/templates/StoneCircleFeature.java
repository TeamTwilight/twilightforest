package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.CobbleVariants;

import javax.annotation.Nullable;
import java.util.Random;

public class StoneCircleFeature extends TemplateFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation STONE_CIRCLE = TwilightForestMod.prefix("feature/ruins/stone_circle");

    public StoneCircleFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
        return templateManager.getOrCreate(STONE_CIRCLE);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, Random random) {
        settings.addProcessor(CobbleVariants.INSTANCE);
    }
}
