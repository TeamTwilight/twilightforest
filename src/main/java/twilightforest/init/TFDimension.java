package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;

public class TFDimension {

	public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	// Referenced by our DimensionType 'twilight_forest_type'.
	public static final ResourceLocation DIMENSION_RENDERER = TwilightForestMod.prefix("renderer");

	// Checks if the world is linked by the default Twilight Portal.
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level level) {
		return DIMENSION.equals(level.dimension().location());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	public static boolean isTwilightWorldOnClient(Level clientWorld) {
		return TwilightForestMod.ID.equals(clientWorld.dimension().location().getNamespace()) || isTwilightPortalDestination(clientWorld);
	}
}
