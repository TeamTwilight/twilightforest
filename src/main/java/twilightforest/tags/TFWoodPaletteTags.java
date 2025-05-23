package twilightforest.tags;

import net.minecraft.tags.TagKey;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.util.woods.WoodPalette;

public class TFWoodPaletteTags {

	public static final TagKey<WoodPalette> WELL_SWIZZLE_MASK = create("well_swizzle_mask");
	public static final TagKey<WoodPalette> DRUID_HUT_SWIZZLE_MASK = create("druid_hut_swizzle_mask");
	public static final TagKey<WoodPalette> COMMON_PALETTES = create("common");
	public static final TagKey<WoodPalette> UNCOMMON_PALETTES = create("uncommon");
	public static final TagKey<WoodPalette> RARE_PALETTES = create("rare");
	public static final TagKey<WoodPalette> TREASURE_PALETTES = create("treasure");

	private static TagKey<WoodPalette> create(String name) {
		return TagKey.create(TFRegistries.Keys.WOOD_PALETTES, TwilightForestMod.prefix(name));
	}
}
