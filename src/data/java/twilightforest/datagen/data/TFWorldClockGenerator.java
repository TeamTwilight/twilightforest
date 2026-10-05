package twilightforest.datagen.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.WorldClock;
import twilightforest.TwilightForestMod;

public class TFWorldClockGenerator {

	public static final ResourceKey<WorldClock> TWILIGHT_FOREST = ResourceKey.create(Registries.WORLD_CLOCK, TwilightForestMod.prefix("twilight_forest"));

	public static void bootstrap(BootstrapContext<WorldClock> context) {
		context.register(TWILIGHT_FOREST, new WorldClock());
	}
}
