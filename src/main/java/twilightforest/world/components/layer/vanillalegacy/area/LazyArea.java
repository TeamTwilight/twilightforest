package twilightforest.world.components.layer.vanillalegacy.area;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import twilightforest.world.components.layer.vanillalegacy.Area;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class LazyArea implements Area {
	private final Area transformer;
	private final ConcurrentHashMap<Long, ResourceKey<Biome>> cachedSamples;
	private final LinkedBlockingQueue<Long> evictionQueue;
	private final int maxCache;

	public LazyArea(ConcurrentHashMap<Long, ResourceKey<Biome>> cache, LinkedBlockingQueue<Long> evictionQueue, int maxCache, Area transformer) {
		this.cachedSamples = cache;
		this.evictionQueue = evictionQueue;
		this.maxCache = maxCache;
		this.transformer = transformer;
	}

	@Override
	public ResourceKey<Biome> getBiome(int biomeX, int biomeZ) {
		long key = ChunkPos.pack(biomeX, biomeZ);
		ResourceKey<Biome> cached = cachedSamples.get(key);
		if (cached != null && cached != Biomes.THE_VOID)
			return cached;
		ResourceKey<Biome> computed = transformer.getBiome(biomeX, biomeZ);
		ResourceKey<Biome> existing = cachedSamples.putIfAbsent(key, computed);
		if (existing != null && existing != Biomes.THE_VOID)
			return existing;
		evictionQueue.offer(key);
		if (evictionQueue.size() > maxCache) {
			for (int i = 0, limit = maxCache / 16; i < limit; i++) {
				Long oldest = evictionQueue.poll();
				if (oldest != null)
					cachedSamples.remove(oldest);
			}
		}
		return computed;
	}

	public int getMaxCache() {
		return this.maxCache;
	}
}
