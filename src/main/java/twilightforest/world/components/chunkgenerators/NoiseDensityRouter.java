package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.*;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.BiomeDensitySource;

/**
 * A DensityFunction implementation that enables Biomes to influence terrain formulations, if in the noise chunk generator.
 * Plan your biome density functions accordingly! Don't use anything that's vertically sensitive
 *
 * @param biomeDensitySourceHolder A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
 * @param lowerDensityBound  Lower clamp bound
 * @param upperDensityBound  Upper clamp bound
 */
public record NoiseDensityRouter(Holder<BiomeDensitySource> biomeDensitySourceHolder, float lowerDensityBound, float upperDensityBound, float depthScalar) implements DensityFunction {
	public static final MapCodec<NoiseDensityRouter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(NoiseDensityRouter::biomeDensitySourceHolder),
		Codec.floatRange(-64, 0).fieldOf("lower_density_bound").forGetter(NoiseDensityRouter::lowerDensityBound),
		Codec.floatRange(0, 64).fieldOf("upper_density_bound").forGetter(NoiseDensityRouter::upperDensityBound),
		Codec.floatRange(0, 32).orElse(8f).fieldOf("depth_scalar").forGetter(NoiseDensityRouter::depthScalar)
	).apply(inst, NoiseDensityRouter::new));

	@Override
	public DensitySampler compileSampler(CompileContext context) {
		return new Sampler(this.biomeDensitySourceHolder.value().compileSampler(context));
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	@Override
	public Interval range() {
		return Interval.of(this.lowerDensityBound, this.upperDensityBound);
	}

	@Override
	public @Axes int domainAxes() {
		return AXIS_X | AXIS_Z;
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public record Sampler(BiomeDensitySource.TerrainSampler terrain) implements DensitySampler {

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			return this.terrain.sampleTerrain(context, blockX, blockY, blockZ).scale;
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			int index = 0;

			for (int z = 0; z < volume.sizeZ(); z++) {
				int blockZ = volume.blockZ(z);

				for (int x = 0; x < volume.sizeX(); x++) {
					float scale = this.sampleValue(context, volume.blockX(x), volume.minBlockY(), blockZ);

					// samples along xz-plane fills columns in the buffer's voxel space
					outputBuffer.setRange(index, volume.sizeY(), scale);
					index += volume.sizeY();
				}
			}
		}
	}
}
