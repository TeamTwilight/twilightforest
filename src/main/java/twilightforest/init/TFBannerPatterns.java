package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

public class TFBannerPatterns {
	public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, TwilightForestMod.ID);

	public static final DeferredHolder<BannerPattern, BannerPattern> NAGA = BANNER_PATTERNS.register("naga", () -> new BannerPattern("tfn"));
	public static final DeferredHolder<BannerPattern, BannerPattern> LICH = BANNER_PATTERNS.register("lich", () -> new BannerPattern("tfl"));
	public static final DeferredHolder<BannerPattern, BannerPattern> MINOSHROOM = BANNER_PATTERNS.register("minoshroom", () -> new BannerPattern("tfm"));
	public static final DeferredHolder<BannerPattern, BannerPattern> HYDRA = BANNER_PATTERNS.register("hydra", () -> new BannerPattern("tfh"));
	public static final DeferredHolder<BannerPattern, BannerPattern> KNIGHT_PHANTOM = BANNER_PATTERNS.register("knight_phantom", () -> new BannerPattern("tfp"));
	public static final DeferredHolder<BannerPattern, BannerPattern> UR_GHAST = BANNER_PATTERNS.register("ur_ghast", () -> new BannerPattern("tfg"));
	public static final DeferredHolder<BannerPattern, BannerPattern> ALPHA_YETI = BANNER_PATTERNS.register("alpha_yeti", () -> new BannerPattern("tfy"));
	public static final DeferredHolder<BannerPattern, BannerPattern> SNOW_QUEEN = BANNER_PATTERNS.register("snow_queen", () -> new BannerPattern("tfq"));
	public static final DeferredHolder<BannerPattern, BannerPattern> QUEST_RAM = BANNER_PATTERNS.register("quest_ram", () -> new BannerPattern("tfr"));

}
