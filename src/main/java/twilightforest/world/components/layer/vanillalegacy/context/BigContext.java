package twilightforest.world.components.layer.vanillalegacy.context;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.layer.vanillalegacy.Area;

public interface BigContext<R extends Area> extends Context {
	void initRandom(long x, long z);

	R createResult(Area transformer);

	R createResult(Area transformer, R layer);

	R createResult(Area transformer, R layer1, R layer2);

	default ResourceKey<Biome> random(ResourceKey<Biome> biome1, ResourceKey<Biome> biome2) {
		return this.nextRandom(2) == 0 ? biome1 : biome2;
	}

	default ResourceKey<Biome> random(ResourceKey<Biome> biome1, ResourceKey<Biome> biome2, ResourceKey<Biome> biome3, ResourceKey<Biome> biome4) {
		int i = this.nextRandom(4);
		if (i == 0) {
			return biome1;
		} else if (i == 1) {
			return biome2;
		} else {
			return i == 2 ? biome3 : biome4;
		}
	}
}