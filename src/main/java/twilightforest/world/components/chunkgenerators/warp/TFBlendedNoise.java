package twilightforest.world.components.chunkgenerators.warp;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

/*
 * Honestly, this just makes absolutely certain that things go the way we want it. Might not even need this? Who knows.
 */
public class TFBlendedNoise extends BlendedNoise {

    public TFBlendedNoise(RandomSource random, NoiseSamplingSettings settings, int x, int z) {
        super(random, settings, x, z);
    }

    public double sampleAndClampNoise(int x, int y, int z, double scaleXZ, double scaleY, double factorXZ, double factorY) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double scale = 1.0D;

        for(int oct = 0; oct < 8; ++oct) {
            ImprovedNoise improvednoise = this.mainNoise.getOctaveNoise(oct);
            if (improvednoise != null) {
                d2 += improvednoise.noise(
                        PerlinNoise.wrap((double)x * factorXZ * scale),
                        PerlinNoise.wrap((double)y * factorY * scale),
                        PerlinNoise.wrap((double)z * factorXZ * scale),
                        factorY * scale,
                        (double)y * factorY * scale) / scale;
            }

            scale /= 2.0D;
        }

        double d8 = (d2 / 10.0D + 1.0D) / 2.0D;
        boolean flag1 = d8 >= 1.0D;
        boolean flag2 = d8 <= 0.0D;
        scale = 1.0D;

        for(int j = 0; j < 16; ++j) {
            double d4 = PerlinNoise.wrap((double)x * scaleXZ * scale);
            double d5 = PerlinNoise.wrap((double)y * scaleY * scale);
            double d6 = PerlinNoise.wrap((double)z * scaleXZ * scale);
            double d7 = scaleY * scale;
            if (!flag1) {
                ImprovedNoise minimumOct = this.minLimitNoise.getOctaveNoise(j);
                if (minimumOct != null) {
                    d0 += minimumOct.noise(d4, d5, d6, d7, (double)y * d7) / scale;
                }
            }

            if (!flag2) {
                ImprovedNoise maximumOct = this.maxLimitNoise.getOctaveNoise(j);
                if (maximumOct != null) {
                    d1 += maximumOct.noise(d4, d5, d6, d7, (double)y * d7) / scale;
                }
            }

            scale /= 2.0D;
        }

        return Mth.clampedLerp(d0 / 512.0D, d1 / 512.0D, d8);
    }
}
