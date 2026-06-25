package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.TriState;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.*;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.timeline.Timeline;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.registration.surface_rules.TFSurfaceRules;

import java.util.List;
import java.util.Optional;

public class TFDimensionData {

	// Avoid at all costs. If you need SeaLevel info instead and are ServerSide, use WorldUtil.getGeneratorSeaLevel()
	// TODO How should we fix this for clients? Would need to sync serverlevel's sealevel to clients
	@Deprecated // FIXME Make private
	public static final int SEALEVEL = 0;

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("skylight_noise_gen"));

	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, TFDimension.DIMENSION);

	public static final TagKey<Timeline> TWILIGHT_TIMELINES = TagKey.create(Registries.TIMELINE, TwilightForestMod.prefix("twilight"));

	public static void bootstrapType(BootstrapContext<DimensionType> context) {
		HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
		Optional<Holder<WorldClock>> overworldClock = context.lookup(Registries.WORLD_CLOCK)
			.get(WorldClocks.OVERWORLD)
			.map(h -> (Holder<WorldClock>) h);
		context.register(TWILIGHT_DIM_TYPE, new DimensionType(
			true, //fixed time
			true, //skylight
			false, //ceiling
			false, //ender dragon fight
			1 / 8.0D, //coordinate scale
			-32, // Minimum Y Level
			32 + 256, // Height + Min Y = Max Y
			32 + 256, // Logical Height
			BlockTags.INFINIBURN_OVERWORLD, //infiburn tag
			0.01F, //ambient light
			new DimensionType.MonsterSettings(UniformInt.of(0, 7), 7), //monster settings
			DimensionType.Skybox.OVERWORLD, //skybox
			CardinalLighting.Type.DEFAULT, //cardinal lighting
			EnvironmentAttributeMap.builder() //attributes
				.set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(new Music(TFSounds.MUSIC, 1200, 12000, true)))
				.set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
				.set(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false)
				.set(EnvironmentAttributes.BED_RULE, new BedRule(BedRule.Rule.NEVER, BedRule.Rule.ALWAYS, false, Optional.empty()))
				.set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
				.set(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false)
				.set(EnvironmentAttributes.CAN_START_RAID, false)
				.set(EnvironmentAttributes.CREAKING_ACTIVE, true)
				.set(EnvironmentAttributes.EYEBLOSSOM_OPEN, TriState.TRUE)
				.set(EnvironmentAttributes.SUN_ANGLE, 93.2F)
				.set(EnvironmentAttributes.MOON_ANGLE, 273.2F)
				.set(EnvironmentAttributes.STAR_BRIGHTNESS, 1.0F)
				.set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 0.62F)
				.set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.38F)
				.set(EnvironmentAttributes.SKY_LIGHT_COLOR, 0x9292FF)
				.set(EnvironmentAttributes.SKY_COLOR, 0x7B7B7B)
				.set(EnvironmentAttributes.FOG_COLOR, 0x838387)
				.build(),
			timelines.getOrThrow(TWILIGHT_TIMELINES), //timelines
			overworldClock //clock - overworld clock so time flows for moon phase and clock item
		));
	}

	public static NoiseGeneratorSettings makeNoiseSettings(BootstrapContext<NoiseGeneratorSettings> context, boolean skylight) {
		HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
		DensityFunction finalDensity = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(skylight ? TFDensityFunctions.SKYLIGHT_TERRAIN : TFDensityFunctions.FORESTED_TERRAIN));

		NoiseSettings tfNoise = NoiseSettings.create(
			-32, //TODO Deliberate over this. For now it'll be -32
			256,
			2,
			2
		);

		return new NoiseGeneratorSettings(
			tfNoise,
			Blocks.STONE.defaultBlockState(),
			skylight ? Blocks.AIR.defaultBlockState() : Blocks.WATER.defaultBlockState(),
			new NoiseRouter(
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				finalDensity,
				finalDensity,
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero()
			),
			TFSurfaceRules.tfSurface(),
			List.of(),
			TFDimensionData.SEALEVEL,
			false,
			false,
			false,
			false
		);
	}

	public static void bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> context) {
		context.register(TWILIGHT_NOISE_GEN, makeNoiseSettings(context, false));
		context.register(SKYLIGHT_NOISE_GEN, makeNoiseSettings(context, true));
	}

	public static void bootstrapStem(BootstrapContext<LevelStem> context) {
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

		HolderGetter<BiomeDensitySource> biomeDataRegistry = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA);

		NoiseBasedChunkGenerator twilightChunkGenerator = new NoiseBasedChunkGenerator(
			new TFBiomeProvider(biomeDataRegistry.getOrThrow(BiomeLayerStack.BIOME_GRID)),
			noiseGenSettings.getOrThrow(TWILIGHT_NOISE_GEN)
		);

		LevelStem stem = new LevelStem(
			dimTypes.getOrThrow(TWILIGHT_DIM_TYPE),
			twilightChunkGenerator
		);

		context.register(TWILIGHT_LEVEL_STEM, stem);
	}
}
