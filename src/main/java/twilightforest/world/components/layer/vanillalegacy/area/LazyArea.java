package twilightforest.world.components.layer.vanillalegacy.area;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import twilightforest.world.components.layer.vanillalegacy.Area;

public class LazyArea implements Area {
	private final Area transformer;
	private final Long2ObjectLinkedOpenHashMap<ResourceKey<Biome>> cachedSamples;
	private final int maxCache;

	public LazyArea(Long2ObjectLinkedOpenHashMap<ResourceKey<Biome>> cache, int maxCache, Area transformer) {
		this.cachedSamples = cache;
		this.maxCache = maxCache;
		this.transformer = transformer;
	}

	@Override
	public ResourceKey<Biome> getBiome(int biomeX, int biomeZ) {
		long i = ChunkPos.asLong(biomeX, biomeZ);
		synchronized(this.cachedSamples) {
			ResourceKey<Biome> j = this.cachedSamples.get(i);
			if (j != null && j != Biomes.THE_VOID) {
				return j;
			} else {
				ResourceKey<Biome> k = this.transformer.getBiome(biomeX, biomeZ);
				this.cachedSamples.put(i, k);
				if (this.cachedSamples.size() > this.maxCache) {
					for(int l = 0; l < this.maxCache / 16; ++l) {
						this.cachedSamples.removeFirst();
					}
				}

				return k;
			}
		}
	}

	public int getMaxCache() {
		return this.maxCache;
	}
}
