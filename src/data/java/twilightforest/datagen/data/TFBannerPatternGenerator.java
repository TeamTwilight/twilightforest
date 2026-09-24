package twilightforest.datagen.data;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;

public class TFBannerPatternGenerator {

    public static void bootstrap(BootstrapContext<BannerPattern> context) {
        context.register(TFBannerPatterns.NAGA, new BannerPattern(TwilightForestMod.prefix("naga"), "block.minecraft.banner.twilightforest.naga"));
        context.register(TFBannerPatterns.LICH, new BannerPattern(TwilightForestMod.prefix("lich"), "block.minecraft.banner.twilightforest.lich"));
        context.register(TFBannerPatterns.MINOSHROOM, new BannerPattern(TwilightForestMod.prefix("minoshroom"), "block.minecraft.banner.twilightforest.minoshroom"));
        context.register(TFBannerPatterns.HYDRA, new BannerPattern(TwilightForestMod.prefix("hydra"), "block.minecraft.banner.twilightforest.hydra"));
        context.register(TFBannerPatterns.KNIGHT_PHANTOM, new BannerPattern(TwilightForestMod.prefix("knight_phantom"), "block.minecraft.banner.twilightforest.knight_phantom"));
        context.register(TFBannerPatterns.UR_GHAST, new BannerPattern(TwilightForestMod.prefix("ur_ghast"), "block.minecraft.banner.twilightforest.ur_ghast"));
        context.register(TFBannerPatterns.ALPHA_YETI, new BannerPattern(TwilightForestMod.prefix("alpha_yeti"), "block.minecraft.banner.twilightforest.alpha_yeti"));
        context.register(TFBannerPatterns.SNOW_QUEEN, new BannerPattern(TwilightForestMod.prefix("snow_queen"), "block.minecraft.banner.twilightforest.snow_queen"));
        context.register(TFBannerPatterns.QUESTING_RAM, new BannerPattern(TwilightForestMod.prefix("quest_ram"), "block.minecraft.banner.twilightforest.quest_ram"));
    }

}
