package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.*;

// For making spheres
public record FocusedDensityFunction(float centerX, float bottomY, float centerZ, float radius, float nearValue, float farValue) implements DensityFunction {
	public static final MapCodec<FocusedDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(FocusedDensityFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(FocusedDensityFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(FocusedDensityFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(FocusedDensityFunction::radius),
		Codec.FLOAT.fieldOf("near_value").forGetter(FocusedDensityFunction::nearValue),
		Codec.FLOAT.fieldOf("far_value").forGetter(FocusedDensityFunction::farValue)
	).apply(instance, FocusedDensityFunction::new));

	public static FocusedDensityFunction fromPos(BlockPos blockPos, float radius, float nearValue, float farValue) {
		return new FocusedDensityFunction(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, radius, nearValue, farValue);
	}

	@Override
	public DensitySampler compileSampler(CompileContext compileContext) {
		return new Sampler(this);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule dfRewriteRule) {
		return this;
	}

	@Override
	public @Axes int domainAxes() {
		return ALL_AXES;
	}

	@Override
	public Interval range() {
		return Interval.of(0.0F, radius);
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public record Sampler(FocusedDensityFunction function) implements DensitySampler {

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			float dX = function.centerX - blockX;
			float dY = function.bottomY - blockY;
			float dZ = function.centerZ - blockZ;
			float dist = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);
			return Mth.clampedMap(dist, 0, function.radius, function.nearValue, function.farValue);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}
}
