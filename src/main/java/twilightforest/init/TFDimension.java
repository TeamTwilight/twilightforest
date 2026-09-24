package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.TwilightForestMod;

public class TFDimension {

	// Avoid at all costs. If you need SeaLevel info instead and are ServerSide, use WorldUtil.getGeneratorSeaLevel()
	// TODO How should we fix this for clients? Would need to sync serverlevel's sealevel to clients
	@Deprecated // FIXME Make private
	public static final int SEALEVEL = 0;

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

	public static final Identifier DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	// Referenced by our DimensionType 'twilight_forest_type'.
	public static final Identifier DIMENSION_RENDERER = TwilightForestMod.prefix("renderer");

	// Checks if the world is linked by the default Twilight Portal.
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level level) {
		return DIMENSION.equals(level.dimension().identifier());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	public static boolean isTwilightWorldOnClient(Level clientWorld) {
		return TwilightForestMod.ID.equals(clientWorld.dimension().identifier().getNamespace()) || isTwilightPortalDestination(clientWorld);
	}
}
