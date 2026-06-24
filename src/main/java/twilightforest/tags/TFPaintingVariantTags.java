package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import twilightforest.TwilightForestMod;

public class TFPaintingVariantTags {

	public static final TagKey<PaintingVariant> LICH_TOWER_PAINTINGS = create("tower_paintings");
	public static final TagKey<PaintingVariant> LICH_BOSS_PAINTINGS = create("tower_boss_paintings");

	private static TagKey<PaintingVariant> create(String tagName) {
		return TagKey.create(Registries.PAINTING_VARIANT, TwilightForestMod.prefix(tagName));
	}
}
