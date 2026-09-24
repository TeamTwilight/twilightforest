package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.init.custom.DwarfRabbitVariants;

public class DwarfRabbitVariantGenerator {

    public static void bootstrap(BootstrapContext<DwarfRabbitVariant> context) {
        context.register(DwarfRabbitVariants.BROWN, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnybrown.png")));
        context.register(DwarfRabbitVariants.DUTCH, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnydutch.png")));
        context.register(DwarfRabbitVariants.WHITE, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnywhite.png")));
    }

}
