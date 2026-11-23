package twilightforest.world.components.chunkgenerators;

import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

import java.util.List;

public final class CustomTerrainBeardifier extends Beardifier {
	private final Beardifier vanilla;
	private final List<DensitySampler> customDensities;

	public CustomTerrainBeardifier(Beardifier vanilla, List<DensitySampler> customDensities) {
		super(List.of(), List.of(), null); // Base states not used; defers to vanilla
		this.vanilla = vanilla;
		this.customDensities = customDensities;
	}

	@Override
	public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
		this.vanilla.sampleVolume(context, outputBuffer, volume);

		try (ScopedDensityBuffer customBuffer = context.acquireBuffer(volume)) {
            for (DensitySampler customDensity : this.customDensities) {
                customDensity.sampleVolume(context, customBuffer, volume);

                for (int index = 0; index < outputBuffer.size(); index++) {
                    outputBuffer.addTo(index, customBuffer.get(index));
                }
            }
		}
	}

	@Override
	public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
		float density = this.vanilla.sampleValue(context, blockX, blockY, blockZ);

        for (DensitySampler customDensity : this.customDensities) {
            density += customDensity.sampleValue(context, blockX, blockY, blockZ);
        }

		return density;
	}
}
