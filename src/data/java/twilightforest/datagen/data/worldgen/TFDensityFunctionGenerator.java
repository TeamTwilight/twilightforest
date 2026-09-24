package twilightforest.datagen.data.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.NoiseDensityRouter;
import twilightforest.world.components.chunkgenerators.SqrtDensityFunction;
import twilightforest.world.components.chunkgenerators.TerrainDensityRouter;
import twilightforest.world.components.layer.BiomeDensitySource;

public class TFDensityFunctionGenerator {

    public static final ResourceKey<DensityFunction> BIOME_TERRAIN_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("raw_biome_terrain"));
    public static final ResourceKey<DensityFunction> BIOME_NOISE_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("raw_biome_noise"));
    public static final ResourceKey<DensityFunction> FORESTED_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("forested_terrain"));
    public static final ResourceKey<DensityFunction> SKYLIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("skylight_terrain"));

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        Holder.Reference<BiomeDensitySource> biomeGrid = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA).getOrThrow(BiomeLayerStack.BIOME_GRID);
        DensityFunction referencedBiomeDensity = makeBiomeDensityRaw(context, biomeGrid);
        DensityFunction ambientTerrainNoise = makeAmbientNoise2D(context);
        DensityFunction referencedNoiseDensity = makeStreamDensityRaw(context, biomeGrid);

        makeForestedTerrain(context, referencedBiomeDensity, ambientTerrainNoise, referencedNoiseDensity);
        makeSkylightTerrain(context, referencedBiomeDensity, ambientTerrainNoise);
    }

    private static DensityFunction makeBiomeDensityRaw(BootstrapContext<DensityFunction> context, Holder.Reference<BiomeDensitySource> biomeGrid) {
        DensityFunction rawBiomeDensityReferenced = new TerrainDensityRouter(
            biomeGrid,
            -31,
            64,
            1,
            DensityFunctions.constant(8),
            DensityFunctions.constant(-1.25)
        );

        // Debug: For a flat substitute of TerrainDensityRouter
        //if (false) rawBiomeDensityReferenced = DensityFunctions.yClampedGradient(-31, 32, 2, -2);

        return new DensityFunctions.HolderHolder(context.register(BIOME_TERRAIN_RAW, rawBiomeDensityReferenced));
    }

    private static DensityFunction makeAmbientNoise2D(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noiseLookup = context.lookup(Registries.NOISE);
        Holder.Reference<NormalNoise.NoiseParameters> surfaceParams = noiseLookup.getOrThrow(Noises.SURFACE);
        Holder.Reference<NormalNoise.NoiseParameters> ridgeParams = noiseLookup.getOrThrow(Noises.RIDGE);

        DensityFunction noiseInterpolator = mulAddHalf(DensityFunctions.noise(surfaceParams, 1, 0));
        DensityFunction wideNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 1, 0));
        DensityFunction thinNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 4, 0));

        DensityFunction jitteredNoise = DensityFunctions.lerp(
            noiseInterpolator.clamp(0, 1),
            wideNoise,
            thinNoise
        );

        return DensityFunctions.flatCache(jitteredNoise);
    }

    private static DensityFunction makeStreamDensityRaw(BootstrapContext<DensityFunction> context, Holder.Reference<BiomeDensitySource> biomeGrid) {
        DensityFunction rawStreamDensityReferenced = new NoiseDensityRouter(
            biomeGrid,
            -31,
            64,
            1
        );

        // Debug: For a flat substitute of TerrainDensityRouter
        //if (false) rawStreamDensityReferenced = DensityFunctions.yClampedGradient(-31, 32, 2, -2);

        return new DensityFunctions.HolderHolder(context.register(BIOME_NOISE_RAW, rawStreamDensityReferenced));
    }

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

    private static void makeForestedTerrain(BootstrapContext<DensityFunction> context, DensityFunction rawBiomeDensity, DensityFunction ambientTerrainNoise, DensityFunction rawNoiseDensity) {
        DensityFunction biomedLandscape = DensityFunctions.mul(
            DensityFunctions.constant(1 / 6f),
            DensityFunctions.add(
                rawBiomeDensity,
                DensityFunctions.yClampedGradient(-31, 256, 31, -256)
            )
        );

        DensityFunction finalDensity = DensityFunctions.add(
            biomedLandscape,
            DensityFunctions.mul(
                rawNoiseDensity,
                DensityFunctions.interpolated(
                    DensityFunctions.max(
                        DensityFunctions.zero(),
                        ambientTerrainNoise
                    )
                )
            )
        );

        context.register(FORESTED_TERRAIN, finalDensity.clamp(-0.1, 0.5));
    }

    // Heavy WIP
    private static void makeSkylightTerrain(BootstrapContext<DensityFunction> context, DensityFunction rawBiomeDensity, DensityFunction ambientTerrainNoise) {
        // FIXME Rapid terrain changes around Highlands are causing islands to stretch into walls when transitioning from the Stream biome

        DensityFunction skyIslandNoise = DensityFunctions.add(
            DensityFunctions.constant(-0.5),
            DensityFunctions.mul(
                DensityFunctions.add(
                    DensityFunctions.constant(-0.5),
                    ambientTerrainNoise
                ),
                DensityFunctions.constant(5)
            )
        );

        DensityFunction biomeDensity = DensityFunctions.mul(
            DensityFunctions.constant(-0.25),
            DensityFunctions.mul(DensityFunctions.add(
                rawBiomeDensity,
                DensityFunctions.yClampedGradient(-31, 256, 31, -256)
            ), DensityFunctions.constant(-1)).halfNegative().abs()
        );

        DensityFunction finalDensity = DensityFunctions.add(
            new SqrtDensityFunction(
                DensityFunctions.interpolated(skyIslandNoise).clamp(0, 2)
            ),
            biomeDensity
        );

        context.register(SKYLIGHT_TERRAIN, finalDensity.clamp(-0.1, 0.5));
    }

}
