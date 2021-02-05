package twilightforest.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.biome.IBiomeMagnifier;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import twilightforest.TwilightForestMod;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFBiomeProvider;
import twilightforest.world.TFDimensions;
import twilightforest.worldgen.biomes.BiomeMaker;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class TwilightWorldDataCompiler extends WorldDataCompilerAndOps<JsonElement> {
	public TwilightWorldDataCompiler(DataGenerator generator) {
		super(generator, JsonOps.INSTANCE, GSON::toJson, new DynamicRegistries.Impl());
	}

	@Override
	public void generate(DirectoryCache directoryCache) {
		getDimensions().forEach((rl, dimension) -> serialize(Registry.DIMENSION_KEY, rl, dimension, Dimension.CODEC));
		getBiomes().forEach((rl, biome) -> serialize(Registry.BIOME_KEY, rl, biome, Biome.CODEC));
	}

	private Map<ResourceLocation, Dimension> getDimensions() {
		return ImmutableMap.of(
				new ResourceLocation(TwilightForestMod.ID, "twilightforest"), makeTwilightForest()
		);
	}

	private Map<ResourceLocation, Biome> getBiomes() {
		return BiomeMaker
				.generateBiomes()
				.entrySet()
				.stream()
				.peek(registryKeyBiomeEntry -> registryKeyBiomeEntry.getValue().setRegistryName(registryKeyBiomeEntry.getKey().getLocation()))
				.collect(Collectors.toMap(entry -> entry.getKey().getLocation(), Map.Entry::getValue));
	}

	private Dimension makeTwilightForest() {
		Optional<DimensionType> twilightType = makeDimensionType(
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
				256,
				FuzzedBiomeMagnifier.INSTANCE,
				new ResourceLocation("infiniburn_overworld"),
				new ResourceLocation(TwilightForestMod.ID, "renderer"), // DimensionRenderInfo
				0f // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
		);

		// Register the type in the local datagen registry. Hacky.
		getOrCreateInRegistry(dynamicRegistries.getRegistry(Registry.DIMENSION_TYPE_KEY), RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation(TwilightForestMod.ID, "forest_type")), twilightType::get);

		NoiseSettings noiseSettings = new NoiseSettings(
				128, // Noise Height - This allows us to shorten the world so we can cram more stuff upwards
				new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D),
				new SlideSettings(-10, 3, 0),
				new SlideSettings(-30, 0, 0),
				1,
				2,
				1.0D,
				-0.46875D,
				false,
				true,
				false,
				false
		);

		Optional<DimensionSettings> dimensionSettings = makeDimensionSettings(
				new DimensionStructuresSettings(Optional.empty(), ImmutableMap.of()),
				noiseSettings,
				Blocks.STONE.getDefaultState(),
				Blocks.WATER.getDefaultState(),
				-1,
				0,
				31,
				false
		);

		// Register the dimension noise settings in the local datagen registry. Hacky.
		getOrCreateInRegistry(dynamicRegistries.getRegistry(Registry.NOISE_SETTINGS_KEY), RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation(TwilightForestMod.ID, "forest_noise_config")), dimensionSettings::get);

		TFDimensions.init();
		ChunkGeneratorTwilightForest chunkGenerator = new ChunkGeneratorTwilightForest(new TFBiomeProvider(0L, new SimpleRegistry<>(Registry.BIOME_KEY, Lifecycle.experimental())), 0L, dimensionSettings::get);

		return new Dimension(twilightType::get, chunkGenerator);
	}

	// Otherwise an AT increases runtime overhead, so we use reflection here instead since dataGen won't run on regular minecraft runtime, so we instead have faux-constructors here

	@SuppressWarnings("SameParameterValue")
	private static Optional<DimensionSettings> makeDimensionSettings(DimensionStructuresSettings structures, NoiseSettings noise, BlockState defaultBlock, BlockState defaultFluid, int bedrockRoofPosition, int bedrockFloorPosition, int seaLevel, boolean disableMobGeneration) {
		try {
			Constructor<DimensionSettings> ctor = DimensionSettings.class.getDeclaredConstructor(
					DimensionStructuresSettings.class,
					NoiseSettings.class,
					BlockState.class,
					BlockState.class,
					int.class,
					int.class,
					int.class,
					boolean.class
			);

			ctor.setAccessible(true);

			return Optional.of(ctor.newInstance(structures, noise, defaultBlock, defaultFluid, bedrockRoofPosition, bedrockFloorPosition, seaLevel, disableMobGeneration));
		} catch (Exception e) {
			LOGGER.error("Error constructing `DimensionSettings`!", e);

			return Optional.empty();
		}
	}

	@SuppressWarnings("SameParameterValue")
	private static Optional<DimensionType> makeDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, IBiomeMagnifier magnifier, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
		try {
			Constructor<DimensionType> ctor = DimensionType.class.getDeclaredConstructor(
					OptionalLong.class,
					boolean.class,
					boolean.class,
					boolean.class,
					boolean.class,
					double.class,
					boolean.class,
					boolean.class,
					boolean.class,
					boolean.class,
					boolean.class,
					int.class,
					IBiomeMagnifier.class,
					ResourceLocation.class,
					ResourceLocation.class,
					float.class
			);

			ctor.setAccessible(true);

			return Optional.of(ctor.newInstance(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, hasDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, magnifier, infiniburn, effects, ambientLight));
		} catch (Exception e) {
			LOGGER.error("Error constructing `DimensionType`!", e);

			return Optional.empty();
		}
	}
}
