package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.TwilightForestMod;

public class TFDimensionData {

	// Avoid at all costs. If you need SeaLevel info instead and are ServerSide, use WorldUtil.getGeneratorSeaLevel()
	// TODO How should we fix this for clients? Would need to sync serverlevel's sealevel to clients
	@Deprecated // FIXME Make private
	public static final int SEALEVEL = 0;

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

}
