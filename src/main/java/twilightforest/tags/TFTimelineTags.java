package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.timeline.Timeline;
import twilightforest.TwilightForestMod;

public class TFTimelineTags {

	public static final TagKey<Timeline> IN_TWILIGHT = TagKey.create(Registries.TIMELINE, TwilightForestMod.prefix("in_twilight"));
}
