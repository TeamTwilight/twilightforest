package twilightforest.worldgen;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.surfacebuilders.TFSurfaceBuilders;

public class ConfiguredSurfaceBuilders {
	public static final SurfaceBuilderConfig DEADROCK_CONFIG = new SurfaceBuilderConfig(BlockConstants.WEATHERED_DEADROCK, BlockConstants.CRACKED_DEADROCK, BlockConstants.DEADROCK);
	public static final SurfaceBuilderConfig HIGHLANDS_CONFIG = new SurfaceBuilderConfig(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.GRASS_BLOCK);
	public static final SurfaceBuilderConfig SNOW_CONFIG = new SurfaceBuilderConfig(BlockConstants.SNOW, BlockConstants.SNOW, BlockConstants.PACKED_ICE);

	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_TF_DEFAULT = TFSurfaceBuilders.DEFAULT_TF.func_242929_a(SurfaceBuilder.GRASS_DIRT_SAND_CONFIG);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_HIGHLANDS = TFSurfaceBuilders.HIGHLANDS.func_242929_a(HIGHLANDS_CONFIG);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_PLATEAU = TFSurfaceBuilders.PLATEAU.func_242929_a(DEADROCK_CONFIG);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_SNOW = SurfaceBuilder.DEFAULT.func_242929_a(SNOW_CONFIG);

	public static void registerConfigurations(Registry<ConfiguredSurfaceBuilder<?>> registry) {
		Registry.register(registry, TwilightForestMod.prefix("coarse_podzol_highlands"), ConfiguredSurfaceBuilders.CONFIGURED_HIGHLANDS);
		Registry.register(registry, TwilightForestMod.prefix("deadrock_plateau"), ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU);
	}
}