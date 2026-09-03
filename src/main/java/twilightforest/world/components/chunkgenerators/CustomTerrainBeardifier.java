package twilightforest.world.components.chunkgenerators;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.List;

public record CustomTerrainBeardifier(DensityFunctions.BeardifierOrMarker vanilla, List<DensityFunction> customDensities) implements DensityFunctions.BeardifierOrMarker {

	@Override
	public double compute(FunctionContext context) {
		double density = this.vanilla.compute(context);

		for (int i = 0; i < this.customDensities.size(); i++) {
			density += this.customDensities.get(i).compute(context);
		}

		return density;
	}

	@Override
	public double minValue() {
		return this.vanilla.minValue();
	}

	@Override
	public double maxValue() {
		return this.vanilla.maxValue();
	}
}
