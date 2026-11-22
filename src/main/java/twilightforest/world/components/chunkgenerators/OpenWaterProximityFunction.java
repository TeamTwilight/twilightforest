package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.QuartPos;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.*;

public record OpenWaterProximityFunction(DensityFunction terrain, int fluidLevel, int radius) implements DensityFunction {

	public static final MapCodec<OpenWaterProximityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.CODEC.fieldOf("terrain").forGetter(OpenWaterProximityFunction::terrain),
		Codec.INT.fieldOf("fluid_level").forGetter(OpenWaterProximityFunction::fluidLevel),
		Codec.intRange(0, 32).fieldOf("radius").forGetter(OpenWaterProximityFunction::radius)
	).apply(instance, OpenWaterProximityFunction::new));

	@Override
	public DensitySampler compileSampler(CompileContext context) {
		return new OpenWaterProximityFunction.Sampler(terrain.compileSampler(context), fluidLevel, radius);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction rewrite = rule.rewrite(terrain);
		return rewrite == terrain ? this : new OpenWaterProximityFunction(rewrite, fluidLevel, radius);
	}

	@Override
	public Interval range() {
		return Interval.NEGATIVE_ONE_TO_ONE;
	}

	@Override
	public @Axes int domainAxes() {
		return AXIS_X | AXIS_Z;
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public record Sampler(DensitySampler terrain, int fluidLevel, int radius) implements DensitySampler {

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			int belowFluidLevel = fluidLevel - 1;
			for (int dx = -radius; dx <= radius; dx += QuartPos.SIZE) {
				for (int dz = -radius; dz <= radius; dz += QuartPos.SIZE) {
					if (terrain.sampleValue(context, blockX + dx, belowFluidLevel, blockZ + dz) <= 0.0)
						return 1.0f;
				}
			}
			return -1.0f;
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}
}
