package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

// Negative radius values cause a bowling-up shaped zero-threshold over this DensityFunction's field, making it useful for the hollow hill's floor alongside as its regular mound shape
public record TanhHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale, float cosAngleBiasDirection, float sinAngleBiasDirection, boolean isXOriented, boolean isOnRightSide) implements DensityFunction.SimpleFunction {
	public static final MapCodec<TanhHillFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(TanhHillFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(TanhHillFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(TanhHillFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(TanhHillFunction::radius),
		Codec.FLOAT.fieldOf("height_scale").forGetter(TanhHillFunction::heightScale),
		Codec.FLOAT.fieldOf("cos_angle_bias_direction").forGetter(TanhHillFunction::cosAngleBiasDirection),
		Codec.FLOAT.fieldOf("sin_angle_bias_direction").forGetter(TanhHillFunction::sinAngleBiasDirection),
		Codec.BOOL.fieldOf("is_x_oriented").forGetter(TanhHillFunction::isXOriented),
		Codec.BOOL.fieldOf("is_on_right_side").forGetter(TanhHillFunction::isOnRightSide)
	).apply(instance, TanhHillFunction::new));
	public static final KeyDispatchDataCodec<TanhHillFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);
	private static final float PERPENDICULAR_BIAS = 1.3F;

	public TanhHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale, float angleBiasDirection, boolean isXOriented, boolean isOnRightSide) {
		this(centerX, bottomY, centerZ, radius, heightScale, Mth.cos(angleBiasDirection), Mth.sin(angleBiasDirection), isXOriented, isOnRightSide);
	}


		@Override
	public double compute(FunctionContext context) {
		float dX = context.blockX() - this.centerX;
		float dY = context.blockY() - this.bottomY;
		float dZ = context.blockZ() - this.centerZ;
		if (isXOriented)  // Make mounds more parallel to the trunk axis in shape
			dZ *= PERPENDICULAR_BIAS;
		else
			dX *= PERPENDICULAR_BIAS;
		return compute(dX, dY, dZ);
	}

	public double compute(float dX, float dY, float dZ) {
		if (dY > 10 || dY < -10)
			return -1;
		float perpendicularDist = isXOriented ? dZ : dX;
		if (!isOnRightSide)
			perpendicularDist = -perpendicularDist;
		if (perpendicularDist < 0)
			return -1;

		double distSquared = dX * dX + dZ * dZ;
		if (distSquared > 9 * radius * radius)
			return -1;
		double dist = Math.sqrt(distSquared);
		// phi = atan2(dZ, dX); atan2 is expensive, don't use it.
		double cosPhi = dX / dist;
		double sinPhi = dZ / dist;
		double cos2Phi = cosPhi * cosPhi - sinPhi * sinPhi;
		double sin2Phi = 2 * cosPhi * sinPhi;
		dist /= (1 + (sin2Phi * cosAngleBiasDirection - cos2Phi * sinAngleBiasDirection) / 3);  // = (1 + Math.sin(phi * 2 - angleBiasDirection) / 3)
		double height = getHeight(dist);
		if (this.heightScale * (height + 1) > dY)
			return 1;
		return -1;
	}

	private double getHeight(double dist) {
		double tanhExp = Math.exp(2 * (2 - 2 * dist / radius));  // use tanh(x) = (exp(2x) - 1) / (exp(2x) + 1) because exp is more likely be jit optimized
		double height =  // simple example without biases https://www.wolframalpha.com/input?i=tanh%282+-+2+*+d+%2F+3%29+-+d+*+%281+-+exp%28-d+*+3+*+0.01%29%29+plot+d+%3D+0..10
			((tanhExp - 1) / (tanhExp + 1))  // Make tanh shaped mound with angle and axis biases
			- dist * (1 - Math.exp(-dist * radius * 0.01));  // Make beardifier weaker and weaker with distance (slant asymptote k ≈ -1)
		return height;
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
