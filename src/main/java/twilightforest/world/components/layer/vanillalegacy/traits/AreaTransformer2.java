package twilightforest.world.components.layer.vanillalegacy.traits;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.RandomContext;

public interface AreaTransformer2 extends DimensionTransformer {
	default <R extends Area> R run(BigContext<R> context, R area1, R area2) {
		return context.createResult((x, z) -> {
			RandomContext randomContext = new RandomContext(context.getSeed());
			randomContext.initRandom(x, z);
			return this.applyPixel(randomContext, area1, area2, x, z);
		}, area1, area2);
	}

	ResourceKey<Biome> applyPixel(RandomContext randomContext, Area layer1, Area layer2, int x, int z);
}
