package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.*;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class AbsoluteDifferenceFunction implements DensityFunction {

	protected final float max, centerX, centerZ;

	public AbsoluteDifferenceFunction(float max, float centerX, float centerZ) {
		this.max = max;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}

	public static Min min(float max, BlockPos pos) {
		return new Min(max, pos.getX(), pos.getZ());
	}

	public static Max max(float max, BlockPos pos) {
		return new Max(max, pos.getX(), pos.getZ());
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
		return AXIS_X | AXIS_Z;
	}

	@Override
	public Interval range() {
		return Interval.of(0.0F, max);
	}

	protected abstract float calculate(int blockX, int blockZ);

	// The sampler compiler keys its cache on value equality
	@Override
	public boolean equals(@Nullable Object o) {
		if (o == null || this.getClass() != o.getClass()) return false;
		AbsoluteDifferenceFunction that = (AbsoluteDifferenceFunction) o;
		return Float.compare(this.max, that.max) == 0 && Float.compare(this.centerX, that.centerX) == 0 && Float.compare(this.centerZ, that.centerZ) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.max, this.centerX, this.centerZ);
	}

	public record Sampler(AbsoluteDifferenceFunction function) implements DensitySampler {

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			return function.calculate(blockX, blockZ);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}

	public static class Min extends AbsoluteDifferenceFunction {
		public static final MapCodec<Min> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("max").forGetter(f -> f.max),
			Codec.FLOAT.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.FLOAT.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Min::new));

		public Min(float max, float xCenter, float zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		protected float calculate(int blockX, int blockZ) {
			return Math.min(Math.min(Math.abs(blockX - centerX), Math.abs(blockZ - centerZ)), max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}

	public static class Max extends AbsoluteDifferenceFunction {
		public static final MapCodec<Max> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("max").forGetter(f -> f.max),
			Codec.FLOAT.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.FLOAT.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Max::new));

		public Max(float max, float xCenter, float zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		protected float calculate(int blockX, int blockZ) {
			return Math.min(Math.max(Math.abs(blockX - centerX), Math.abs(blockZ - centerZ)), max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}
}
