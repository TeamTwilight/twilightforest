package twilightforest.world.components.layer;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.TerrainColumn;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeDensitySource {
	public static final Codec<BiomeDensitySource> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
		TerrainColumn.CODEC.listOf().fieldOf("biome_landscape").xmap(l -> l.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), m -> m.values().stream().sorted(Comparator.comparing(TerrainColumn::getResourceKey)).toList()).forGetter(o -> o.biomeList),
		BiomeLayerStack.HOLDER_CODEC.fieldOf("biome_layer_config").forGetter(BiomeDensitySource::getBiomeConfig)
	).apply(instance, instance.stable(BiomeDensitySource::new)));

	private final Map<ResourceKey<Biome>, TerrainColumn> biomeList;

	private final Holder<BiomeLayerFactory> genBiomeConfig;
	private final Supplier<LazyArea> genBiomes;

	public BiomeDensitySource(List<TerrainColumn> list, Holder<BiomeLayerFactory> biomeLayerFactory) {
		this(list.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), biomeLayerFactory);
	}

	public BiomeDensitySource(Map<ResourceKey<Biome>, TerrainColumn> list, Holder<BiomeLayerFactory> biomeLayerFactory) {
		super();

		this.genBiomeConfig = biomeLayerFactory;
		this.genBiomes = Suppliers.memoize(() -> this.genBiomeConfig.value().build(salt -> new LazyAreaContext(25, salt)));

		this.biomeList = list;
	}

	public BiomeDensitySource recreate() {
		return new BiomeDensitySource(this.biomeList, this.genBiomeConfig);
	}

	private Holder<BiomeLayerFactory> getBiomeConfig() {
		return this.genBiomeConfig;
	}

	@NotNull
	public Holder<Biome> getBiomeColumnKey(int biomeX, int biomeZ) {
		return this.biomeList.get(this.genBiomes.get().getBiome(biomeX, biomeZ)).getMainBiome();
	}

	public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		return this.biomeList.get(this.genBiomes.get().getBiome(biomeX, biomeZ)).getBiome(biomeY);
	}

	public Optional<TerrainColumn> getTerrainColumn(int biomeX, int biomeZ) {
		return this.getTerrainColumn(this.genBiomes.get().getBiome(biomeX, biomeZ));
	}

	public Optional<TerrainColumn> getTerrainColumn(ResourceKey<Biome> biome) {
		return this.biomeList.values().stream().filter(p -> p.is(biome)).findFirst();
	}

	// Only used for building a cache
	public Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.biomeList.values().stream().flatMap(TerrainColumn::getBiomes);
	}

	public void addDebugInfo(List<String> info, BlockPos cameraPos) {
		ResourceKey<Biome> biomeKey = this.genBiomes.get().getBiome(cameraPos.getX() >> 2, cameraPos.getZ() >> 2);
		TerrainColumn biomeColumn = this.biomeList.get(biomeKey);
		Holder<Biome> biomeAtY = biomeColumn.getBiome(cameraPos.getY() >> 2);
		info.add("BiomeDensitySource at " + cameraPos + ":");
		info.add("Twilight Biome Column:");
		biomeColumn.getBiomesDebug(info::add);
		info.add("Primary Biome: " + biomeKey.location());
		info.add("Biome at elevation: " + biomeAtY.unwrapKey().map(ResourceKey::location).map(ResourceLocation::toString).orElse("NOT REFERENCED"));
	}

	public static final class DensityData {
		public final double depth;
		public final double scale;

		public DensityData(double depth, double scale) {
			this.depth = depth;
			this.scale = scale;
		}
	}

	// Thanks k.jpg!

	private static final double BLEND_RADIUS = 8.75;
	private static final int BLEND_RADIUS_INT = Mth.floor(BLEND_RADIUS + 1.0);
	private static final int BLOCK_XYZ_OFFSET = QuartPos.SIZE / 2;

	public DensityData sampleTerrain(int blockX, int blockZ, DensityFunction.FunctionContext context) {
		double totalScale = 0.0;
		double totalMappedDepth = 0.0;
		double totalContribution = 0.0;

		int blockXWithOffset = blockX - BLOCK_XYZ_OFFSET;
		int blockZWithOffset = blockZ - BLOCK_XYZ_OFFSET;

		int xQuartStart = (blockXWithOffset - BLEND_RADIUS_INT) >> QuartPos.BITS;
		int zQuartStart = (blockZWithOffset - BLEND_RADIUS_INT) >> QuartPos.BITS;
		int xQuartEnd = (blockXWithOffset + BLEND_RADIUS_INT) >> QuartPos.BITS;
		int zQuartEnd = (blockZWithOffset + BLEND_RADIUS_INT) >> QuartPos.BITS;
		int xCount = xQuartEnd - xQuartStart + 1;
		int zCount = zQuartEnd - zQuartStart + 1;

		double xQuartDelta = (blockXWithOffset - (xQuartStart << QuartPos.BITS)) * (1.0 / QuartPos.SIZE);
		double zQuartDelta = (blockZWithOffset - (zQuartStart << QuartPos.BITS)) * (1.0 / QuartPos.SIZE);

		for (int cz = 0, cx = 0; ; ) {
			double dX = xQuartDelta - cx;
			double dZ = zQuartDelta - cz;

			double distSq = dX * dX + dZ * dZ;

			if (distSq < BLEND_RADIUS * BLEND_RADIUS) {
				Optional<TerrainColumn> terrainColumn = this.getTerrainColumn(cx + xQuartStart, cz + zQuartStart);
				if (terrainColumn.isPresent()) {
					double falloff = BLEND_RADIUS * BLEND_RADIUS;

					double neighborDepth = terrainColumn.get().depth(context);
					double neighborScale = terrainColumn.get().scale(context);

					falloff *= Math.exp((distSq * 2f + neighborDepth) * -0.4f);

					totalMappedDepth += neighborDepth * falloff;
					totalScale += neighborScale * falloff;
					totalContribution += falloff;
				}
			}

			cz++;
			if (cz < zCount) continue;
			cz = 0;
			cx++;
			if (cx >= xCount) break;
		}

		double depthNormalized = totalMappedDepth / totalContribution;
		double scaleNormalized = totalScale / totalContribution;

		return new DensityData(depthNormalized, scaleNormalized);
	}
}
