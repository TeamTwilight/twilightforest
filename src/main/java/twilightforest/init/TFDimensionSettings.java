package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.TerrainDensityRouter;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;
import twilightforest.world.registration.surface_rules.TFSurfaceRules;

import java.util.List;
import java.util.OptionalLong;

public class TFDimensionSettings {

	public static long seed; //Minecraft Overworld seed - used for seed ASM

	public static final ResourceKey<DensityFunction> TWILIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("twilight_terrain"));
	public static final ResourceKey<DensityFunction> SKYLIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("skylight_terrain"));

	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("skylight_noise_gen"));

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM =  ResourceKey.create(Registries.LEVEL_STEM, TwilightForestMod.prefix("twilight_forest"));

	private static DimensionType twilightDimType() {
		return new DimensionType(
				OptionalLong.of(13000L), //fixed time
				true, //skylight
				false, //ceiling
				false, //ultrawarm
				true, //natural
				0.125D, //coordinate scale
				true, //bed works
				true, //respawn anchor works
				-32, // Minimum Y Level
				32 + 256, // Height + Min Y = Max Y
				32 + 256, // Logical Height
				BlockTags.INFINIBURN_OVERWORLD, //infiburn
				TwilightForestMod.prefix("renderer"), // DimensionRenderInfo
				0f, // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
				new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)
		);
	}

	public static void bootstrapDensityFunctions(BootstapContext<DensityFunction> context) {
		Holder.Reference<BiomeDensitySource> biomeGrid = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA).getOrThrow(BiomeLayerStack.BIOME_GRID);
		Holder.Reference<NormalNoise.NoiseParameters> surfaceParams = context.lookup(Registries.NOISE).getOrThrow(Noises.SURFACE);
		Holder.Reference<NormalNoise.NoiseParameters> ridgeParams = context.lookup(Registries.NOISE).getOrThrow(Noises.RIDGE);

        DensityFunction routedBiomeWarp = DensityFunctions.mul(
				DensityFunctions.constant(1/6f),
				DensityFunctions.add(
						new TerrainDensityRouter(
								biomeGrid,
								new DensityFunction.NoiseHolder(surfaceParams),
								-31,
								64,
								1,
								DensityFunctions.constant(8),
								DensityFunctions.constant(-1.25)
						),
						DensityFunctions.yClampedGradient(-31, 256, 31, -256)
				)
		);

		// Debug: For a flat substitute of BiomeTerrainWarpRouter
		// routedBiomeWarp = DensityFunctions.yClampedGradient(-31, 32, 2, -2);

		DensityFunction wideNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 1, 0));

		DensityFunction thinNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 4, 0));

		DensityFunction noiseInterpolator = mulAddHalf(DensityFunctions.noise(surfaceParams, 1, 1.0/16.0));

		DensityFunction jitteredNoise = DensityFunctions.lerp(
				noiseInterpolator.clamp(0, 1),
				wideNoise,
				thinNoise
		);

		DensityFunction finalDensity = DensityFunctions.add(
				routedBiomeWarp,
				DensityFunctions.cache2d(DensityFunctions.interpolated(DensityFunctions.max(
						DensityFunctions.zero(),
						jitteredNoise
				)))
		);

		context.register(TWILIGHT_TERRAIN, finalDensity.clamp(-0.1, 1));

		context.register(SKYLIGHT_TERRAIN, DensityFunctions.mul(
				DensityFunctions.constant(0.1),
				DensityFunctions.add(
						DensityFunctions.constant(-1),
						noiseInterpolator
				).clamp(-1, 0)
		).clamp(-0.1, 1));
	}

	@NotNull
	private static DensityFunction mulAddHalf(DensityFunction input) {
		// mulAddHalf(x) = x * 0.5 + 0.5
		// Useful for squeezing function range [-1,1] into [0,1]
		return DensityFunctions.add(
				DensityFunctions.constant(0.5),
				DensityFunctions.mul(
						DensityFunctions.constant(0.5),
						input
				)
		);
	}

	public static NoiseGeneratorSettings tfDefault(BootstapContext<NoiseGeneratorSettings> context) {
		HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
		DensityFunction finalDensity = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(TWILIGHT_TERRAIN));

		NoiseSettings tfNoise = NoiseSettings.create(
				-32, //TODO Deliberate over this. For now it'll be -32
				256,
				2,
				2
		);

		return new NoiseGeneratorSettings(
				tfNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
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
				0,
				false,
				false,
				false,
				false
		);
	}

	// completely untested
	public static NoiseGeneratorSettings skylight(BootstapContext<NoiseGeneratorSettings> context) {
		HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
		DensityFunction finalDensity = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(SKYLIGHT_TERRAIN));

		NoiseSettings skylightNoise = NoiseSettings.create(
				-32, //min height
				256, // height
				2, // size_horizontal
				2 // size_vertical
		);

		return new NoiseGeneratorSettings(
				skylightNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.AIR.defaultBlockState(),
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
				0,
				false,
				false,
				false,
				false
		);
	}

	public static void bootstrapNoise(BootstapContext<NoiseGeneratorSettings> context) {
		context.register(TWILIGHT_NOISE_GEN, tfDefault(context));
		context.register(SKYLIGHT_NOISE_GEN, skylight(context));
	}

	public static void bootstrapType(BootstapContext<DimensionType> context) {
		context.register(TWILIGHT_DIM_TYPE, twilightDimType());
	}

	public static void bootstrapStem(BootstapContext<LevelStem> context) {
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

		HolderGetter<BiomeDensitySource> biomeDataRegistry = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA);

		NoiseBasedChunkGenerator twilightChunkGenerator = new NoiseBasedChunkGenerator(
				new TFBiomeProvider(biomeDataRegistry.getOrThrow(BiomeLayerStack.BIOME_GRID)),
				noiseGenSettings.getOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN)
		);

		LevelStem stem = new LevelStem(
				dimTypes.getOrThrow(TFDimensionSettings.TWILIGHT_DIM_TYPE),
				twilightChunkGenerator
		);

		context.register(TWILIGHT_LEVEL_STEM, stem);
	}
}
