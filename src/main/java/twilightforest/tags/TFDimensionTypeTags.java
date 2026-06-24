package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.TwilightForestMod;

public class TFDimensionTypeTags {

	public static final TagKey<DimensionType> ALLOWS_MAGIC_MAP_CHARTING = create("allows_magic_map_charting");

	private static TagKey<DimensionType> create(String tagName) {
		return TagKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix(tagName));
	}
}
