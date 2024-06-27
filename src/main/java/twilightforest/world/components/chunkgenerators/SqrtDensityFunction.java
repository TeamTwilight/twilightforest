package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction.SimpleFunction {
	public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new));
	public static final KeyDispatchDataCodec<SqrtDensityFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

	@Override
	public double compute(FunctionContext context) {
		double sqrt = Math.sqrt(this.input.compute(context));
		return sqrt;
	}

	@Override
	public double minValue() {
		return this.input.minValue();
	}

	@Override
	public double maxValue() {
		return this.input.maxValue();
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KEY_CODEC;
	}

	@Override
	public DensityFunction mapAll(Visitor visitor) {
		return visitor.apply(new SqrtDensityFunction(this.input.mapAll(visitor)));
	}
}
