package twilightforest.world.components.layer.vanillalegacy.context;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;

public class RandomContext implements Context {
	private final long seed;
	private long rval;

	public RandomContext(long seed) {
		this.seed = seed;
	}

	public void initRandom(long x, long z) {
		long i = this.seed;
		i = LinearCongruentialGenerator.next(i, x);
		i = LinearCongruentialGenerator.next(i, z);
		i = LinearCongruentialGenerator.next(i, x);
		i = LinearCongruentialGenerator.next(i, z);
		this.rval = i;
	}

	@Override
	public long getSeed() {
		return this.seed;
	}

	public ResourceKey<Biome> random(ResourceKey<Biome> biome1, ResourceKey<Biome> biome2) {
		return this.nextRandom(2) == 0 ? biome1 : biome2;
	}

	public ResourceKey<Biome> random(ResourceKey<Biome> biome1, ResourceKey<Biome> biome2, ResourceKey<Biome> biome3, ResourceKey<Biome> biome4) {
		int i = this.nextRandom(4);
		if (i == 0) {
			return biome1;
		} else if (i == 1) {
			return biome2;
		} else {
			return i == 2 ? biome3 : biome4;
		}
	}

	public int nextRandom(int limit) {
		int result = Math.floorMod(this.rval >> 24, limit);
		this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
		return result;
	}
}