package twilightforest.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FuzzyOffsetBiomeZoomer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlideSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.ConfiguredSurfaceBuilders;
import twilightforest.world.registration.ConfiguredWorldCarvers;
import twilightforest.world.registration.TFDimensions;
import twilightforest.world.registration.biomes.BiomeMaker;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class TwilightWorldDataCompiler extends WorldDataCompilerAndOps<JsonElement> {
	public TwilightWorldDataCompiler(DataGenerator generator) {
		super(generator, JsonOps.INSTANCE, GSON::toJson, new RegistryAccess.RegistryHolder());
	}

	@Override
	public void generate(HashCache directoryCache) {
		ConfiguredSurfaceBuilders.registerConfigurations(this.dynamicRegistries.registryOrThrow(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY));
		ConfiguredWorldCarvers.registerConfigurations(this.dynamicRegistries.registryOrThrow(Registry.CONFIGURED_CARVER_REGISTRY));

		Map<ResourceLocation, Biome> biomes = this.getBiomes();
		biomes.forEach((rl, biome) -> this.dynamicRegistries.registry(Registry.BIOME_REGISTRY).ifPresent(reg -> Registry.register(reg, rl, biome)));
		biomes.forEach((rl, biome) -> this.serialize(Registry.BIOME_REGISTRY, rl, biome, Biome.DIRECT_CODEC));

		this.getDimensions().forEach((rl, dimension) -> this.serialize(Registry.LEVEL_STEM_REGISTRY, rl, dimension, LevelStem.CODEC));
	}

	@Override
	protected JsonElement intercept(ResourceKey<?> key, JsonElement json) {
		if (key == Registry.LEVEL_STEM_REGISTRY)
			json.getAsJsonObject().
					get("generator").getAsJsonObject().
					get("wrapped_generator").getAsJsonObject().
					get("biome_source").getAsJsonObject().
					remove("seed");
		return super.intercept(key, json);
	}

	private Map<ResourceLocation, LevelStem> getDimensions() {
		NoiseGeneratorSettings forestDimensionSettings = makeDimensionSettings(
				new StructureSettings(Optional.empty(), ImmutableMap.of()),
				NoiseSettings.create(
						-32, // TODO Deliberate over this. For now it'll be -32
						128,
						new NoiseSamplingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D),
						new NoiseSlideSettings(-10, 3, 0),
						new NoiseSlideSettings(15, 3, 0),
						1,
						2,
						1.0D,
						-0.95,
						true,
						true,
						false,
						false
				),
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				Integer.MIN_VALUE,
				0,
				0,
				-32,
				false,
				false,
				false,
				false,
				false,
				false
		);

		// Problem island at /tp 9389.60 90.00 11041.66
		NoiseGeneratorSettings skyDimensionSettings = makeDimensionSettings(
				new StructureSettings(Optional.empty(), ImmutableMap.of()),
				// https://misode.github.io/worldgen/noise-settings/
				// So far this looks great! We just need to raise the island levels to sea level. Otherwise is generates flat-flakey islands that really show the roots on dirt bottoms from trees
				NoiseSettings.create(
						-16, //min height
						16, // height
						new NoiseSamplingSettings(3.0D, 1.0D, 256.0D, 16.0D), // sampling
						new NoiseSlideSettings(-3000, 92, -66), // top_slide
						new NoiseSlideSettings(-30, 7, 1), // bottom_slide
						2, // size_horizontal
						1, // size_vertical
						0.3D, // density_factor
						-0.2D, // density_offset
						true, // simplex_surface_noise
						false, // random_density_offset
						false, // island_noise_override
						false  // amplified
				),
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				Integer.MIN_VALUE,
				Integer.MAX_VALUE,
				0,
				-32,
				false,
				false,
				false,
				false,
				false,
				false
		);

		// Register the dimension noise settings in the local datagen registry.
		this.getOrCreateInRegistry(this.dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, TwilightForestMod.prefix("forest_noise_config")), () -> forestDimensionSettings);
		this.getOrCreateInRegistry(this.dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, TwilightForestMod.prefix("sky_noise_config")), () -> skyDimensionSettings);

		TFDimensions.init();

		//FIXME: The issue with generated files using 0 as the seed is here. We need to somehow just...not have it here?
		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(new TFBiomeProvider(0L, new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental())), 0L, () -> forestDimensionSettings);
				//new ChunkGeneratorTwilightForest(new FixedBiomeSource(getFromDynRegistry(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("enchanted_forest"))), 0L, () -> forestDimensionSettings);

		NoiseBasedChunkGenerator skyChunkGen = new NoiseBasedChunkGenerator(new TFBiomeProvider(0L, new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental())), 0L, () -> skyDimensionSettings);
		//NoiseChunkGenerator skyChunkGen = new NoiseChunkGenerator(new TFBiomeProvider(0L, new SimpleRegistry<>(Registry.BIOME_KEY, Lifecycle.experimental())), 4L, () -> WorldGenRegistries.NOISE_SETTINGS.getValueForKey(RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("floating_islands"))));
		//NoiseChunkGenerator skyChunkGen = new NoiseChunkGenerator(new CheckerboardBiomeProvider(BiomeMaker.BIOMES.values().stream().sorted((o1, o2) -> Float.compare(o1.getDepth(), o2.getDepth())).map(b -> (Supplier<Biome>) () -> b).collect(Collectors.toList()), 2), 4L, skyDimensionSettings::get);

		final DimensionType twilightType = DimensionType.create(
				OptionalLong.of(13000L), // TODO Kill the celestial bodies
				true,
				false,
				false,
				true,
				0.125D,
				false,
				false,
				true,
				true,
				false,
				-32, // Minimum Y Level
				32+256, // Height + Min Y = Max Y
				32+256, // Logical Height
				FuzzyOffsetBiomeZoomer.INSTANCE,
				new ResourceLocation("infiniburn_overworld"),
				TwilightForestMod.prefix("renderer"), // DimensionRenderInfo
				0f // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
		);

		// Register the type in the local datagen registry. Hacky.
		this.getOrCreateInRegistry(this.dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY), ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(TwilightForestMod.ID, "forest_type")), () -> twilightType);

		return ImmutableMap.of(
				TwilightForestMod.prefix("twilight_forest"), new LevelStem(() -> twilightType, new ChunkGeneratorTwilight(forestChunkGen, true, true, Optional.of(12), true))//,
				//TwilightForestMod.prefix("skylight_forest"), new LevelStem(() -> twilightType, skyChunkGen)
				// TODO add *actual* twilightforest:void world without islands
		);
	}

	private Map<ResourceLocation, Biome> getBiomes() {
		return BiomeMaker
				.BIOMES
				.entrySet()
				.stream()
				.peek(registryKeyBiomeEntry -> registryKeyBiomeEntry.getValue().setRegistryName(registryKeyBiomeEntry.getKey().location()))
				.collect(Collectors.toMap(entry -> entry.getKey().location(), Map.Entry::getValue));
	}
}
