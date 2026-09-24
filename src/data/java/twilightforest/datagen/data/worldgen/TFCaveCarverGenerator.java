package twilightforest.datagen.data.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import twilightforest.init.TFCaveCarvers;
import twilightforest.tags.TFBlockTags;

public class TFCaveCarverGenerator {

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        context.register(TFCaveCarvers.TFCAVES_CONFIGURED, TFCaveCarvers.TF_CAVES.value().configured(new CaveCarverConfiguration(
            0.1F,
            UniformHeight.of(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(-8)),
            ConstantFloat.of(0.6F),
            VerticalAnchor.bottom(),
            blocks.getOrThrow(TFBlockTags.CARVER_REPLACEABLES),
            ConstantFloat.of(1.05F),
            ConstantFloat.of(1.05F),
            ConstantFloat.of(-0.7F)
        )));

        context.register(TFCaveCarvers.HIGHLANDCAVES_CONFIGURED, TFCaveCarvers.HIGHLAND_CAVES.value().configured(new CaveCarverConfiguration(
            1f,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(8), VerticalAnchor.absolute(32), 16),
            ConstantFloat.of(0.6f),
            VerticalAnchor.bottom(),
            blocks.getOrThrow(TFBlockTags.CARVER_REPLACEABLES),
            UniformFloat.of(1.1f, 1.3f),
            ConstantFloat.of(1.1f),
            UniformFloat.of(-0.9F, -0.65F)
        )));
    }

}
