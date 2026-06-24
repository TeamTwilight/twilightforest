package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.biomes.BiomeHelper;

public class TFBiomes {

	public static final ResourceKey<Biome> FOREST = makeKey("forest");
	public static final ResourceKey<Biome> DENSE_FOREST = makeKey("dense_forest");
	public static final ResourceKey<Biome> FIREFLY_FOREST = makeKey("firefly_forest");
	public static final ResourceKey<Biome> CLEARING = makeKey("clearing");
	public static final ResourceKey<Biome> OAK_SAVANNAH = makeKey("oak_savannah");
	public static final ResourceKey<Biome> STREAM = makeKey("stream");
	public static final ResourceKey<Biome> LAKE = makeKey("lake");

	public static final ResourceKey<Biome> MUSHROOM_FOREST = makeKey("mushroom_forest");
	public static final ResourceKey<Biome> DENSE_MUSHROOM_FOREST = makeKey("dense_mushroom_forest");

	public static final ResourceKey<Biome> ENCHANTED_FOREST = makeKey("enchanted_forest");
	public static final ResourceKey<Biome> SPOOKY_FOREST = makeKey("spooky_forest");

	public static final ResourceKey<Biome> SWAMP = makeKey("swamp");
	public static final ResourceKey<Biome> FIRE_SWAMP = makeKey("fire_swamp");

	public static final ResourceKey<Biome> DARK_FOREST = makeKey("dark_forest");
	public static final ResourceKey<Biome> DARK_FOREST_CENTER = makeKey("dark_forest_center");

	public static final ResourceKey<Biome> SNOWY_FOREST = makeKey("snowy_forest");
	public static final ResourceKey<Biome> GLACIER = makeKey("glacier");

	public static final ResourceKey<Biome> HIGHLANDS = makeKey("highlands");
	public static final ResourceKey<Biome> HIGHLANDS_UNDERGROUND = makeKey("highlands_underground");

	public static final ResourceKey<Biome> THORNLANDS = makeKey("thornlands");
	public static final ResourceKey<Biome> FINAL_PLATEAU = makeKey("final_plateau");

	public static final ResourceKey<Biome> UNDERGROUND = makeKey("underground");

	private static ResourceKey<Biome> makeKey(String name) {
		return ResourceKey.create(Registries.BIOME, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<Biome> context) {
		HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);
		HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);
		context.register(FOREST, BiomeHelper.twilightForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(DENSE_FOREST, BiomeHelper.denseForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(FIREFLY_FOREST, BiomeHelper.fireflyForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(CLEARING, BiomeHelper.clearing(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(OAK_SAVANNAH, BiomeHelper.oakSavanna(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(MUSHROOM_FOREST, BiomeHelper.mushroomForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(DENSE_MUSHROOM_FOREST, BiomeHelper.denseMushroomForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(SPOOKY_FOREST, BiomeHelper.spookyForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(ENCHANTED_FOREST, BiomeHelper.enchantedForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(STREAM, BiomeHelper.stream(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(LAKE, BiomeHelper.lake(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(SWAMP, BiomeHelper.swamp(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(FIRE_SWAMP, BiomeHelper.fireSwamp(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(DARK_FOREST, BiomeHelper.darkForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(DARK_FOREST_CENTER, BiomeHelper.darkForestCenter(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(SNOWY_FOREST, BiomeHelper.snowyForest(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(GLACIER, BiomeHelper.glacier(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(HIGHLANDS, BiomeHelper.highlands(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(HIGHLANDS_UNDERGROUND, BiomeHelper.highlandsUnderground(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(THORNLANDS, BiomeHelper.thornlands(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
		context.register(FINAL_PLATEAU, BiomeHelper.finalPlateau(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());

		context.register(UNDERGROUND, BiomeHelper.underground(featureGetter, carverGetter).setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false).build());
	}
}
