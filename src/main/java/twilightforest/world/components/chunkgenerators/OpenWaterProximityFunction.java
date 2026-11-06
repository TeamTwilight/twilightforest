package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.QuartPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record OpenWaterProximityFunction(DensityFunction terrain, int fluidLevel, int radius) implements DensityFunction.SimpleFunction {

	public static final MapCodec<OpenWaterProximityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.HOLDER_HELPER_CODEC.fieldOf("terrain").forGetter(OpenWaterProximityFunction::terrain),
		Codec.INT.fieldOf("fluid_level").forGetter(OpenWaterProximityFunction::fluidLevel),
		Codec.intRange(0, 32).fieldOf("radius").forGetter(OpenWaterProximityFunction::radius)
	).apply(instance, OpenWaterProximityFunction::new));

	public static final KeyDispatchDataCodec<OpenWaterProximityFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

	@Override
	public double compute(FunctionContext context) {
		int belowFluidLevel = fluidLevel - 1;
		for (int dx = -radius; dx <= radius; dx += QuartPos.SIZE) {
			for (int dz = -radius; dz <= radius; dz += QuartPos.SIZE) {
				if (terrain.compute(new SinglePointContext(context.blockX() + dx, belowFluidLevel, context.blockZ() + dz)) <= 0.0)
					return 1.0;
			}
		}
		return -1.0;
	}

	@Override
	public double minValue() {
		return -1.0;
	}

	@Override
	public double maxValue() {
		return 1.0;
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KEY_CODEC;
	}

	@Override
	public DensityFunction mapAll(Visitor visitor) {
		return visitor.apply(new OpenWaterProximityFunction(terrain.mapAll(visitor), fluidLevel, radius));
	}
}
