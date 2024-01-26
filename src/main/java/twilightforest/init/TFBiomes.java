package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.BiomeGrassColors;

import static twilightforest.world.registration.biomes.BiomeHelper.*;

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

	public static void bootstrap(BootstapContext<Biome> context) {
		HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);
		HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);
		context.register(FOREST, biomeWithDefaults(fireflyParticles(defaultAmbientBuilder()), defaultMobSpawning(), twilightForestGen(featureGetter, carverGetter)).build());
		context.register(DENSE_FOREST, biomeWithDefaults(fireflyParticles(defaultAmbientBuilder()).waterColor(0x005522), defaultMobSpawning(), denseForestGen(featureGetter, carverGetter)).temperature(0.7F).downfall(0.8F).build());
		context.register(FIREFLY_FOREST, biomeWithDefaults(fireflyForestParticles(defaultAmbientBuilder()), defaultMobSpawning(), fireflyForestGen(featureGetter, carverGetter)).temperature(0.5F).downfall(1).build());
		context.register(CLEARING, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), clearingGen(featureGetter, carverGetter)).temperature(0.8F).downfall(0.4F).build());
		context.register(OAK_SAVANNAH, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), oakSavannaGen(featureGetter, carverGetter)).temperature(0.9F).downfall(0).build());

		context.register(MUSHROOM_FOREST, biomeWithDefaults(fireflyParticles(defaultAmbientBuilder()), defaultMobSpawning(), mushroomForestGen(featureGetter, carverGetter)).temperature(0.8F).downfall(0.8F).build());
		context.register(DENSE_MUSHROOM_FOREST, biomeWithDefaults(fireflyParticles(defaultAmbientBuilder()), defaultMobSpawning(), denseMushroomForestGen(featureGetter, carverGetter)).temperature(0.8F).downfall(1).build());

		context.register(SPOOKY_FOREST, biomeWithDefaults(defaultAmbientBuilder().grassColorOverride(0xC45123).foliageColorOverride(0xFF8501).waterColor(0xBC8857).grassColorModifier(BiomeGrassColors.SPOOKY_FOREST), spookSpawning(), spookyForestGen(featureGetter, carverGetter)).temperature(0.5F).downfall(1).build());
		context.register(ENCHANTED_FOREST, biomeWithDefaults(fireflyParticles(defaultAmbientBuilder()).foliageColorOverride(0x00FFFF).grassColorOverride(0x00FFFF).grassColorModifier(BiomeGrassColors.ENCHANTED_FOREST), defaultMobSpawning(), enchantedForestGen(featureGetter, carverGetter)).hasPrecipitation(false).build());
		context.register(STREAM, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), streamsAndLakes(featureGetter, carverGetter, false)).temperature(0.5F).downfall(0.1F).build());
		context.register(LAKE, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), streamsAndLakes(featureGetter, carverGetter, true)).temperature(0.66F).downfall(1).build());

		context.register(SWAMP, biomeWithDefaults(defaultAmbientBuilder().skyColor(0x002112).fogColor(0x003F21).grassColorOverride(0x5C694E).foliageColorOverride(0x496137).waterColor(0x95B55F).grassColorModifier(BiomeGrassColors.SWAMP), swampSpawning(), swampGen(featureGetter, carverGetter)).temperature(0.8F).downfall(0.9F).build());
		context.register(FIRE_SWAMP, biomeWithDefaults(whiteAshParticles(defaultAmbientBuilder()).waterColor(0x2D0700).fogColor(0x380A00).grassColorOverride(0x572E23).foliageColorOverride(0x64260F).waterColor(0x6C2C2C), new MobSpawnSettings.Builder(), fireSwampGen(featureGetter, carverGetter)).hasPrecipitation(false).temperature(1).downfall(0.4F).build());

		context.register(DARK_FOREST, biomeWithDefaults(defaultAmbientBuilder().skyColor(0x000000).fogColor(0x000000).grassColorOverride(0x4B6754).foliageColorOverride(0x3B5E3F).grassColorModifier(BiomeGrassColors.DARK_FOREST), darkForestSpawning(), darkForestGen(featureGetter, carverGetter)).temperature(0.7F).downfall(0.8F).build());
		context.register(DARK_FOREST_CENTER, biomeWithDefaults(defaultAmbientBuilder().skyColor(0x000000).fogColor(0x493000).grassColorOverride(0x667540).foliageColorOverride(0xF9821E).grassColorModifier(BiomeGrassColors.DARK_FOREST_CENTER), new MobSpawnSettings.Builder(), darkForestCenterGen(featureGetter, carverGetter)).build());

		context.register(SNOWY_FOREST, biomeWithDefaults(defaultAmbientBuilder().skyColor(0x808080).fogColor(0xFFFFFF).foliageColorOverride(0xFFFFFF).grassColorOverride(0xFFFFFF), snowForestSpawning(), snowyForestGen(featureGetter, carverGetter)).hasPrecipitation(true).temperature(0.09F).downfall(0.9F).build());
		context.register(GLACIER, biomeWithDefaults(defaultAmbientBuilder().skyColor(0x130D28).fogColor(0x361F88), penguinSpawning(), glacierGen(featureGetter, carverGetter)).temperature(0.08F).downfall(0.1F).hasPrecipitation(true).build());

		context.register(HIGHLANDS, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), highlandsGen(featureGetter, carverGetter)).temperature(0.4F).downfall(0.7F).build());
		context.register(HIGHLANDS_UNDERGROUND, biomeWithDefaults(defaultAmbientBuilder(), undergroundMobSpawning(), highlandsUndergroundGen(featureGetter, carverGetter)).temperature(0.35F).downfall(0.0F).build());
		context.register(THORNLANDS, biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnSettings.Builder(), thornlandsGen(featureGetter, carverGetter)).temperature(0.3F).downfall(0.2F).build());
		context.register(FINAL_PLATEAU, biomeWithDefaults(defaultAmbientBuilder(), ravenSpawning(), new BiomeGenerationSettings.Builder(featureGetter, carverGetter)).temperature(1.0F).downfall(0.2F).build());

		context.register(UNDERGROUND, biomeWithDefaults(defaultAmbientBuilder(), undergroundMobSpawning(), undergroundGen(featureGetter, carverGetter)).temperature(0.7F).downfall(0.0F).build());
	}
}
