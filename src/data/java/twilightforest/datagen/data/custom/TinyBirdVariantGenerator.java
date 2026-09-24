package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.init.custom.TinyBirdVariants;

public class TinyBirdVariantGenerator {

    public static void bootstrap(BootstrapContext<TinyBirdVariant> context) {
        context.register(TinyBirdVariants.BLUE, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdblue.png")));
        context.register(TinyBirdVariants.BROWN, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdbrown.png")));
        context.register(TinyBirdVariants.GOLD, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdgold.png")));
        context.register(TinyBirdVariants.RED, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdred.png")));
    }

}
