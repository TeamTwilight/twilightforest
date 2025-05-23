package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

// Negative radius values cause a bowling-up shaped zero-threshold over this DensityFunction's field, making it useful for the hollow hill's floor alongside as its regular mound shape
public record HollowHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale) implements DensityFunction.SimpleFunction {
	public static final MapCodec<HollowHillFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(HollowHillFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(HollowHillFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(HollowHillFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(HollowHillFunction::radius),
		Codec.FLOAT.fieldOf("height_scale").forGetter(HollowHillFunction::heightScale)
	).apply(instance, HollowHillFunction::new));
	public static final KeyDispatchDataCodec<HollowHillFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

	public static HollowHillFunction fromPos(BlockPos blockPos, float radius, float heightScale) {
		return new HollowHillFunction(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, radius, heightScale);
	}

	@Override
	public double compute(FunctionContext context) {
		float dX = context.blockX() - this.centerX;
		float dY = context.blockY() - this.bottomY;
		float dZ = context.blockZ() - this.centerZ;

		return compute(dX, dY, dZ);
	}

	public double compute(float dX, float dY, float dZ) {
		float dist = Mth.sqrt(dX * dX + dZ * dZ);
		// Because cosine is an even function, the radius multiplying cosine's result is the only variable that can affect this DensityFunction using a negative value.
		float height = Mth.cos(dist / this.radius * Mth.PI) * this.radius * 0.3333333334f;

		float normalizedDist = Mth.clamp(dist / Mth.abs(this.radius), 0, 1);

		if (normalizedDist >= 1) {
			return 0;
		}

		return Mth.clamp((height * this.heightScale - dY), -1, 1);
	}

	@Override
	public double minValue() {
		return -1;
	}

	@Override
	public double maxValue() {
		return 1;
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KEY_CODEC;
	}
}
