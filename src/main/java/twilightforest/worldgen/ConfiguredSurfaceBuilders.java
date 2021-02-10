package twilightforest.worldgen;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.surfacebuilders.TFSurfaceBuilders;

public class ConfiguredSurfaceBuilders {
	public static final SurfaceBuilderConfig DEADROCK_CONFIG = new SurfaceBuilderConfig(BlockConstants.WEATHERED_DEADROCK, BlockConstants.DEADROCK, BlockConstants.DEADROCK);
	public static final SurfaceBuilderConfig HIGHLANDS_CONFIG = new SurfaceBuilderConfig(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.GRASS_BLOCK);

	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_HIGHLANDS = TFSurfaceBuilders.HIGHLANDS.func_242929_a(ConfiguredSurfaceBuilders.HIGHLANDS_CONFIG);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_PLATEAU = TFSurfaceBuilders.PLATEAU.func_242929_a(ConfiguredSurfaceBuilders.DEADROCK_CONFIG);

	public static void registerConfigurations(Registry<ConfiguredSurfaceBuilder<?>> registry) {
		Registry.register(registry, TwilightForestMod.prefix("coarse_podzol_highlands"), ConfiguredSurfaceBuilders.CONFIGURED_HIGHLANDS);
		Registry.register(registry, TwilightForestMod.prefix("deadrock_plateau"), ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU);
	}
}