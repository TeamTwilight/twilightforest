package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

// For making spheres
public record FocusedDensityFunction(float centerX, float bottomY, float centerZ, float radius, float nearValue, float farValue) implements DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<FocusedDensityFunction> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("x_center").forGetter(FocusedDensityFunction::centerX),
            Codec.FLOAT.fieldOf("y_bottom").forGetter(FocusedDensityFunction::bottomY),
            Codec.FLOAT.fieldOf("z_center").forGetter(FocusedDensityFunction::centerZ),
            Codec.FLOAT.fieldOf("radius").forGetter(FocusedDensityFunction::radius),
            Codec.FLOAT.fieldOf("near_value").forGetter(FocusedDensityFunction::nearValue),
            Codec.FLOAT.fieldOf("far_value").forGetter(FocusedDensityFunction::farValue)
    ).apply(instance, FocusedDensityFunction::new)));

    public static FocusedDensityFunction fromPos(BlockPos blockPos, float radius, float nearValue, float farValue) {
        return new FocusedDensityFunction(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, radius, nearValue, farValue);
    }

    @Override
    public double compute(FunctionContext context) {
        float dX = this.centerX - context.blockX();
        float dY = this.bottomY - context.blockY();
        float dZ = this.centerZ - context.blockZ();

        float dist = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);

        return Mth.clampedMap(dist, 0, this.radius, this.nearValue, this.farValue);
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return this.radius;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
