package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TwilightForestMod;

public class TFBannerPatternTags {

	public static final TagKey<BannerPattern> NAGA_BANNER_PATTERN = create("pattern_item/naga");
	public static final TagKey<BannerPattern> LICH_BANNER_PATTERN = create("pattern_item/lich");
	public static final TagKey<BannerPattern> MINOSHROOM_BANNER_PATTERN = create("pattern_item/minoshroom");
	public static final TagKey<BannerPattern> HYDRA_BANNER_PATTERN = create("pattern_item/hydra");
	public static final TagKey<BannerPattern> KNIGHT_PHANTOM_BANNER_PATTERN = create("pattern_item/knight_phantom");
	public static final TagKey<BannerPattern> UR_GHAST_BANNER_PATTERN = create("pattern_item/ur_ghast");
	public static final TagKey<BannerPattern> ALPHA_YETI_BANNER_PATTERN = create("pattern_item/alpha_yeti");
	public static final TagKey<BannerPattern> SNOW_QUEEN_BANNER_PATTERN = create("pattern_item/snow_queen");
	public static final TagKey<BannerPattern> QUESTING_RAM_BANNER_PATTERN = create("pattern_item/questing_ram");

	private static TagKey<BannerPattern> create(String name) {
		return TagKey.create(Registries.BANNER_PATTERN, TwilightForestMod.prefix(name));
	}
}
