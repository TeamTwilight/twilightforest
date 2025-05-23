package twilightforest.world.components.layer.vanillalegacy.context;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class LazyAreaContext implements BigContext<LazyArea> {
	private final ConcurrentHashMap<Long, ResourceKey<Biome>> cache;
	private final LinkedBlockingQueue<Long> evictionQueue;
	private final int maxCache;
	private final long seed;

	public LazyAreaContext(int maxCache, long salt) {
		this.seed = mixSeed(WorldUtil.getOverworldSeed(), salt);
		this.cache = new ConcurrentHashMap<>();
		this.evictionQueue = new LinkedBlockingQueue<>();
		this.maxCache = maxCache;
	}

	public long getSeed() {
		return seed;
	}

	@Override
	public LazyArea createResult(Area transformer) {
		return new LazyArea(this.cache, this.evictionQueue, this.maxCache, transformer);
	}

	@Override
	public LazyArea createResult(Area transformer, LazyArea layer) {
		return new LazyArea(this.cache, this.evictionQueue, Math.min(1024, layer.getMaxCache() * 4), transformer);
	}

	@Override
	public LazyArea createResult(Area transformer, LazyArea layer1, LazyArea layer2) {
		return new LazyArea(this.cache, this.evictionQueue, Math.min(1024, Math.max(layer1.getMaxCache(), layer2.getMaxCache()) * 4), transformer);
	}

	private static long mixSeed(long seed, long salt) {
		long i = LinearCongruentialGenerator.next(salt, salt);
		i = LinearCongruentialGenerator.next(i, salt);
		i = LinearCongruentialGenerator.next(i, salt);
		long j = LinearCongruentialGenerator.next(seed, i);
		j = LinearCongruentialGenerator.next(j, i);
		return LinearCongruentialGenerator.next(j, i);
	}
}
