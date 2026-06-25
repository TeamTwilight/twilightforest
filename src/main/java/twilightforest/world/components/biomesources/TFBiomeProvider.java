package twilightforest.world.components.biomesources;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.BiomeDensitySource;

import java.util.List;
import java.util.stream.Stream;

public class TFBiomeProvider extends BiomeSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(TFBiomeProvider.class);

	public static final MapCodec<TFBiomeProvider> TF_CODEC = RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).xmap(TFBiomeProvider::new, TFBiomeProvider::getBiomeConfig).fieldOf("terrain_data");

	private final Holder<BiomeDensitySource> biomeTerrainDataHolder;

	public TFBiomeProvider(Holder<BiomeDensitySource> biomeTerrainDataHolder) {
		super();

		this.biomeTerrainDataHolder = biomeTerrainDataHolder;
		LOGGER.info("[TF-BIOME] TFBiomeProvider created with terrain data: {}", biomeTerrainDataHolder.unwrapKey().map(k -> k.identifier().toString()).orElse("unknown"));
	}

	private Holder<BiomeDensitySource> getBiomeConfig() {
		return this.biomeTerrainDataHolder;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		Stream<Holder<Biome>> biomes = this.biomeTerrainDataHolder.value().collectPossibleBiomes();
		List<Holder<Biome>> biomeList = biomes.toList();
		LOGGER.info("[TF-BIOME] collectPossibleBiomes() returned {} biomes:", biomeList.size());
		for (Holder<Biome> biome : biomeList) {
			biome.unwrapKey().ifPresent(key -> LOGGER.info("[TF-BIOME]   - {} (type: {})", key.identifier(), biome.kind()));
		}
		return biomeList.stream();
	}

	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return TF_CODEC;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler sampler) {
		return this.biomeTerrainDataHolder.value().getNoiseBiome(biomeX, biomeY, biomeZ);
	}

	public Holder<Biome> getMainBiome(int biomeX, int biomeZ) {
		return this.biomeTerrainDataHolder.value().getBiomeColumnKey(biomeX, biomeZ);
	}

	@Deprecated
	public BiomeDensitySource getBiomeTerrain() {
		return this.biomeTerrainDataHolder.value();
	}

	@Override
	public void addDebugInfo(List<String> info, BlockPos cameraPos, Climate.Sampler sampler) {
		super.addDebugInfo(info, cameraPos, sampler);

		this.biomeTerrainDataHolder.value().addDebugInfo(info, cameraPos);
	}
}
