package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.List;

public class BoxDensityFunction implements DensityFunction.SimpleFunction {
	public static final MapCodec<BoxDensityFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("minX").forGetter(f -> f.minX),
		Codec.INT.fieldOf("minY").forGetter(f -> f.minY),
		Codec.INT.fieldOf("minZ").forGetter(f -> f.minZ),
		Codec.INT.fieldOf("maxX").forGetter(f -> f.maxX),
		Codec.INT.fieldOf("maxY").forGetter(f -> f.maxY),
		Codec.INT.fieldOf("maxZ").forGetter(f -> f.maxZ),
		Codec.DOUBLE.fieldOf("minValue").forGetter(f -> f.minValue),
		Codec.DOUBLE.fieldOf("maxValue").forGetter(f -> f.maxValue),
		TerrainAdjustment.CODEC.fieldOf("beardifier").forGetter(f -> f.terrainAdjustment)
	).apply(inst, BoxDensityFunction::new));
	public static final KeyDispatchDataCodec<BoxDensityFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

	private final int minX, minY, minZ, maxX, maxY, maxZ;
	private final double minValue, maxValue;
	private final TerrainAdjustment terrainAdjustment;

	public static DensityFunction combine(List<BoundingBox> boxes, int dY, TerrainAdjustment terrainAdjustment) {
		if (boxes.isEmpty()) return DensityFunctions.constant(0);

		DensityFunction densityFunction = make(boxes.getFirst(), dY, terrainAdjustment);

		for (int idx = 1; idx < boxes.size(); idx++) {
			densityFunction = DensityFunctions.add(make(boxes.get(idx), dY, terrainAdjustment), densityFunction);
		}

		return densityFunction;
	}

	public static BoxDensityFunction make(BoundingBox box, int dY, TerrainAdjustment terrainAdjustment) {
		return new BoxDensityFunction(box.minX(), box.minY() + dY, box.minZ(), box.maxX(), box.maxY() + dY, box.maxZ(), -4.0, 4.0, terrainAdjustment);
	}

	public BoxDensityFunction(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, double minValue, double maxValue, TerrainAdjustment terrainAdjustment) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.terrainAdjustment = terrainAdjustment;
	}

	@Override // Parallels vanilla Beardifier behavior
	public double compute(FunctionContext context) {
		int blockX = context.blockX();
		int blockY = context.blockY();
		int blockZ = context.blockZ();

		// Dist is zero if inside the box
		int xDist = Math.max(0, Math.max(this.minX - blockX, blockX - this.maxX));
		int zDist = Math.max(0, Math.max(this.minZ - blockZ, blockZ - this.maxZ));

		int distAboveBottom = blockY - this.minY;
		int yDist = switch (this.terrainAdjustment) {
			case BURY, BEARD_THIN -> distAboveBottom;
			case BEARD_BOX, ENCAPSULATE -> Math.max(0, Math.max(this.minY - blockY, blockY - this.maxY));
			default -> 0;
		};

		double densityValue = switch (this.terrainAdjustment) {
			case BURY -> Beardifier.getBuryContribution(xDist, yDist * 0.5, zDist);
			case BEARD_THIN, BEARD_BOX -> Beardifier.getBeardContribution(xDist, yDist, zDist, distAboveBottom) * 0.8;
			case ENCAPSULATE -> Beardifier.getBuryContribution(xDist * 0.5, yDist * 0.5, zDist * 0.5) * 0.8;
			default -> 0;
		};

		return Mth.clamp(densityValue, this.minValue, this.maxValue);
	}

	@Override
	public double minValue() {
		return this.minValue;
	}

	@Override
	public double maxValue() {
		return this.maxValue;
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KEY_CODEC;
	}
}
