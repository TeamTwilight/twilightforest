package twilightforest.world.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;

public class TFGenerationSettings {
	@Deprecated // Avoid at all costs. Used as a fallback for clients.
	public static final int SEALEVEL = 0;

	public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<LevelStem> WORLDGEN_KEY = ResourceKey.create(Registries.LEVEL_STEM, DIMENSION);
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	// Checks if the world is linked by the default Twilight Portal.
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level level) {
		return DIMENSION.equals(level.dimension().location());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	@OnlyIn(Dist.CLIENT)
	public static boolean isTwilightWorldOnClient(Level clientWorld) {
		return TwilightForestMod.ID.equals(clientWorld.dimension().location().getNamespace()) || isTwilightPortalDestination(clientWorld);
	}
}
