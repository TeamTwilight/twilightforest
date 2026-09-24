package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TwilightForestMod;

public class TFBannerPatterns {
	public static final ResourceKey<BannerPattern> NAGA = register("naga");
	public static final ResourceKey<BannerPattern> LICH = register("lich");
	public static final ResourceKey<BannerPattern> MINOSHROOM = register("minoshroom");
	public static final ResourceKey<BannerPattern> HYDRA = register("hydra");
	public static final ResourceKey<BannerPattern> KNIGHT_PHANTOM = register("knight_phantom");
	public static final ResourceKey<BannerPattern> UR_GHAST = register("ur_ghast");
	public static final ResourceKey<BannerPattern> ALPHA_YETI = register("alpha_yeti");
	public static final ResourceKey<BannerPattern> SNOW_QUEEN = register("snow_queen");
	public static final ResourceKey<BannerPattern> QUESTING_RAM = register("questing_ram");

	private static ResourceKey<BannerPattern> register(String name) {
		return ResourceKey.create(Registries.BANNER_PATTERN, TwilightForestMod.prefix(name));
	}

}
