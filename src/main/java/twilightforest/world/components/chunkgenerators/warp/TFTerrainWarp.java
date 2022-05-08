package twilightforest.world.components.chunkgenerators.warp;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import twilightforest.world.components.biomesources.TFBiomeProvider;

/*
 * This is where the magic of warping terrain with biomes happens.
 * This will require having the BiomeSource be specific or at least parent the specific BiomeSource otherwise the logic can't run
 */
public class TFTerrainWarp {

    private final int cellWidth;
    private final int cellHeight;
    private final int cellCountY;
    private final BiomeSource biomeSource;
    private final NoiseSettings noiseSettings;
    private final NoiseSlider topSlide;
    private final NoiseSlider bottomSlide;
    private final BlendedNoise blendedNoise;
    private final double dimensionDensityFactor;
    private final double dimensionDensityOffset;
    public final NoiseModifier caveNoiseModifier;
    protected static final float[] BIOME_WEIGHTS = Util.make(new float[25], (afloat) -> {
        for(int x = -2; x <= 2; ++x) {
            for(int z = -2; z <= 2; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                afloat[x + 2 + (z + 2) * 5] = weight;
            }
        }
    });

    public TFTerrainWarp(int width, int height, int yCount, BiomeSource source, NoiseSettings settings, BlendedNoise blend, NoiseModifier modifier) {
        this.cellWidth = width;
        this.cellHeight = height;
        this.cellCountY = yCount;
        this.biomeSource = source;
        this.noiseSettings = settings;
        this.topSlide = settings.topSlideSettings();
        this.bottomSlide = settings.bottomSlideSettings();
        this.blendedNoise = blend;
        //Fallbacks will never be met as this will crash to enforce correct source
        this.dimensionDensityFactor = source instanceof TFBiomeProvider tfsource ? tfsource.getBaseFactor() : 1.0F;
        this.dimensionDensityOffset = source instanceof TFBiomeProvider tfsource ? tfsource.getBaseOffset() : 0.0F;
        this.caveNoiseModifier = modifier;
    }

    public void fillNoiseColumn(ChunkGenerator generator, double[] adouble, int x, int z, NoiseSettings settings, int sealevel, int min, int max) {
        if (biomeSource instanceof TFBiomeProvider source) {
            double d0;
            double d1;
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float depth = source.getBiomeDepth(x, sealevel, z, generator.climateSampler());

            for (int offX = -2; offX <= 2; ++offX) {
                for (int offZ = -2; offZ <= 2; ++offZ) {
                    Biome biome = source.getNoiseBiome(x + offX, sealevel, z + offZ, generator.climateSampler()).value();
                    float offD = source.getBiomeDepth(biome);
                    float offS = source.getBiomeScale(biome);
                    float f6;
                    float f7;
                    f6 = offD;
                    f7 = offS;

                    float f8 = offD > depth ? 0.5F : 1.0F;
                    float f9 = f8 * BIOME_WEIGHTS[offX + 2 + (offZ + 2) * 5] / (f6 + 2.0F);
                    f += f7 * f9;
                    f1 += f6 * f9;
                    f2 += f9;
                }
            }

            float f10 = f1 / f2;
            float f11 = f / f2;
            double d6 = f10 * 0.5F - 0.125F;
            double d8 = f11 * 0.9F + 0.1F;
            d0 = d6 * 0.265625D;
            d1 = 96.0D / d8;

            double scaleXZ = 684.412D * settings.noiseSamplingSettings().xzScale();
            double scaleY = 684.412D * settings.noiseSamplingSettings().yScale();
            double factorXZ = scaleXZ / settings.noiseSamplingSettings().xzFactor();
            double factorY = scaleY / settings.noiseSamplingSettings().yFactor();
            double density = -0.46875;

            if (blendedNoise instanceof TFBlendedNoise blend) {
                for (int index = 0; index <= max; ++index) {
                    int y = index + min;
                    double noise = blend.sampleAndClampNoise(x, y, z, scaleXZ, scaleY, factorXZ, factorY);
                    double totaldensity = this.computeInitialDensity(y, d0, d1, density) + noise;
                    totaldensity = this.caveNoiseModifier.modifyNoise(totaldensity, y * this.cellHeight, z * this.cellWidth, x * this.cellWidth);
                    totaldensity = this.applySlide(totaldensity, y);
                    adouble[index] = totaldensity;
                }
            } else {
                throw new IllegalArgumentException("BlendedNoise is not an instance of TFBlendedNoise");
            }

        } else {
            throw new IllegalArgumentException("BiomeSource is not an instance of TFBiomeProvider");
        }
    }

    protected double computeInitialDensity(int y, double offset, double factor, double density) {
        double base = 1.0D - (double)y * 2.0D / 32.0D + density;
        double factored = base * this.dimensionDensityFactor + this.dimensionDensityOffset;
        double total = (factored + offset) * factor;
        return total * (double)(total > 0.0D ? 4 : 1);
    }

    protected double applySlide(double density, int height) {
        int i = Mth.intFloorDiv(this.noiseSettings.minY(), this.cellHeight);
        int j = height - i;
        density = this.topSlide.applySlide(density, this.cellCountY - j);
        density = this.bottomSlide.applySlide(density, j);
        return density;
    }
}