package twilightforest.datagen.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.ColorModifier;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.timeline.Timeline;
import twilightforest.TwilightForestMod;

public class TFTimelineGenerator {

	public static final ResourceKey<Timeline> TWILIGHT = ResourceKey.create(Registries.TIMELINE, TwilightForestMod.prefix("twilight"));

	/*
	 * Mostly gives us control to darken the sky and fog colour inherently in the dimension.
	 */
	public static void bootstrap(BootstrapContext<Timeline> context) {
		HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);
		Holder.Reference<WorldClock> tfclock = clocks.getOrThrow(TFWorldClockGenerator.TWILIGHT_FOREST);
		context.register(
			TWILIGHT,
			Timeline.builder(tfclock)
				.addModifierTrack(
					EnvironmentAttributes.FOG_COLOR,
					ColorModifier.MULTIPLY_RGB,
					track -> track.addKeyframe(0, ARGB.colorFromFloat(1.0F, 0.42F, 0.42F, 0.445F)))
				.addModifierTrack(
					EnvironmentAttributes.SKY_COLOR,
					ColorModifier.MULTIPLY_RGB,
					track -> track.addKeyframe(0, ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.375F)))
				.build()
		);
	}
}
