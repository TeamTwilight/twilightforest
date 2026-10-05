package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.timeline.Timeline;
import twilightforest.TwilightForestMod;
import twilightforest.datagen.data.TFTimelineGenerator;
import twilightforest.tags.TFTimelineTags;

import java.util.concurrent.CompletableFuture;

public class TimelineTagGenerator extends KeyTagProvider<Timeline> {
	public TimelineTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Registries.TIMELINE, lookupProvider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		this.tag(TFTimelineTags.IN_TWILIGHT).add(TFTimelineGenerator.TWILIGHT);
	}
}
