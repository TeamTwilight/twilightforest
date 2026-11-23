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
 * The terrain sample ignores Y. Do not use vertically sensitive functions in a TerrainColumn.
 *
 * @param biomeDensitySourceHolder A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
 * @param lowerDensityBound        Lower clamp bound
 * @param upperDensityBound        Upper clamp bound
 * @param baseFactor               Density function (can be constant) for the height of the vertical y-gradient at a given X-Z position. A biome speeds or slows this vertical rate of change.
 * @param baseOffset               Density function (can be constant) for the elevation of the vertical y-gradient at a given X-Z position. A biome moves it up and down.
 */
public record TerrainDensityRouter(Holder<BiomeDensitySource> biomeDensitySourceHolder, float lowerDensityBound, float upperDensityBound, float depthScalar, DensityFunction baseFactor, DensityFunction baseOffset) implements DensityFunction {

	public static final MapCodec<TerrainDensityRouter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(TerrainDensityRouter::biomeDensitySourceHolder),
		Codec.floatRange(-64, 0).fieldOf("lower_density_bound").forGetter(TerrainDensityRouter::lowerDensityBound),
		Codec.floatRange(0, 64).fieldOf("upper_density_bound").forGetter(TerrainDensityRouter::upperDensityBound),
		Codec.floatRange(0, 32).orElse(8.0f).fieldOf("depth_scalar").forGetter(TerrainDensityRouter::depthScalar),
		DensityFunction.CODEC.fieldOf("base_factor").forGetter(TerrainDensityRouter::baseFactor),
		DensityFunction.CODEC.fieldOf("base_offset").forGetter(TerrainDensityRouter::baseOffset)
	).apply(inst, TerrainDensityRouter::new));

	@Override
	public DensitySampler compileSampler(CompileContext context) {
		return new Sampler(
			this.biomeDensitySourceHolder.value().compileSampler(context),
			this.baseFactor.compileSampler(context),
			this.baseOffset.compileSampler(context)
		);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction baseFactor = rule.rewrite(this.baseFactor);
		DensityFunction baseOffset = rule.rewrite(this.baseOffset);

		return baseFactor == this.baseFactor && baseOffset == this.baseOffset
			? this
			: new TerrainDensityRouter(this.biomeDensitySourceHolder, this.lowerDensityBound, this.upperDensityBound, this.depthScalar, baseFactor, baseOffset);
	}

	@Override
	public Interval range() {
		return Interval.of(this.lowerDensityBound, this.upperDensityBound);
	}

	@Override
	public @Axes int domainAxes() {
		return AXIS_X | AXIS_Z | this.baseFactor.domainAxes() | this.baseOffset.domainAxes();
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public record Sampler(BiomeDensitySource.TerrainSampler terrain, DensitySampler baseFactor, DensitySampler baseOffset) implements DensitySampler {

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			float terrainDepth = this.terrain.sampleTerrain(context, blockX, blockY, blockZ).depth;

			return this.route(context, terrainDepth, blockX, blockY, blockZ);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			int index = 0;

			for (int z = 0; z < volume.sizeZ(); z++) {
				int blockZ = volume.blockZ(z);

				for (int x = 0; x < volume.sizeX(); x++) {
					int blockX = volume.blockX(x);
					float terrainDepth = this.terrain.sampleTerrain(context, blockX, volume.minBlockY(), blockZ).depth;

					for (int y = 0; y < volume.sizeY(); y++) {
						outputBuffer.set(index++, this.route(context, terrainDepth, blockX, volume.blockY(y), blockZ));
					}
				}
			}
		}

		private float route(SamplerContext context, float terrainDepth, int blockX, int blockY, int blockZ) {
			float depth = this.baseOffset.sampleValue(context, blockX, blockY, blockZ) + terrainDepth * this.baseFactor.sampleValue(context, blockX, blockY, blockZ);

			return depth + terrainDepth;
		}
	}
}
