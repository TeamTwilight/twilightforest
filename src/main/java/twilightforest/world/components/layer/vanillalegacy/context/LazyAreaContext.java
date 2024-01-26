package twilightforest.world.components.layer.vanillalegacy.context;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import twilightforest.init.TFDimensionSettings;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;

public class LazyAreaContext implements BigContext<LazyArea> {
	private final Long2ObjectLinkedOpenHashMap<ResourceKey<Biome>> cache;
	private final int maxCache;
	private final long seed;
	private long rval;

	public LazyAreaContext(int maxCache, long salt) {
		this.seed = mixSeed(TFDimensionSettings.seed, salt);
		this.cache = new Long2ObjectLinkedOpenHashMap<>(16, 0.25F);
		this.cache.defaultReturnValue(Biomes.THE_VOID);
		this.maxCache = maxCache;
	}

	@Override
	public LazyArea createResult(Area transformer) {
		return new LazyArea(this.cache, this.maxCache, transformer);
	}

	@Override
	public LazyArea createResult(Area transformer, LazyArea layer) {
		return new LazyArea(this.cache, Math.min(1024, layer.getMaxCache() * 4), transformer);
	}

	@Override
	public LazyArea createResult(Area transformer, LazyArea layer1, LazyArea layer2) {
		return new LazyArea(this.cache, Math.min(1024, Math.max(layer1.getMaxCache(), layer2.getMaxCache()) * 4), transformer);
	}

	@Override
	public void initRandom(long x, long z) {
		long i = this.seed;
		i = LinearCongruentialGenerator.next(i, x);
		i = LinearCongruentialGenerator.next(i, z);
		i = LinearCongruentialGenerator.next(i, x);
		i = LinearCongruentialGenerator.next(i, z);
		this.rval = i;
	}

	@Override
	public int nextRandom(int limit) {
		int i = Math.floorMod(this.rval >> 24, limit);
		this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
		return i;
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
