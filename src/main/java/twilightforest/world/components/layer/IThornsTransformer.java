package twilightforest.world.components.layer;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;
import twilightforest.world.components.layer.vanillalegacy.traits.DimensionOffset1Transformer;

/**
 * Works like ICastleTransformer, but has extra parameters for diagonal checks
 * TODO: Verify the logic
 */
public interface IThornsTransformer extends AreaTransformer1, DimensionOffset1Transformer {
	ResourceKey<Biome> apply(Context noise, ResourceKey<Biome> north, ResourceKey<Biome> west, ResourceKey<Biome> south, ResourceKey<Biome> east, ResourceKey<Biome> middle, ResourceKey<Biome> nw, ResourceKey<Biome> sw, ResourceKey<Biome> se, ResourceKey<Biome> ne);

	@Override
	default ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z) {
		return this.apply(context,
			layer.getBiome(this.getParentX(x + 1), this.getParentY(z)),
			layer.getBiome(this.getParentX(x + 2), this.getParentY(z + 1)),
			layer.getBiome(this.getParentX(x + 1), this.getParentY(z + 2)),
			layer.getBiome(this.getParentX(x), this.getParentY(z + 1)),
			layer.getBiome(this.getParentX(x + 1), this.getParentY(z + 1)),
			layer.getBiome(this.getParentX(x + 2), this.getParentY(z)),
			layer.getBiome(this.getParentX(x + 2), this.getParentY(z + 2)),
			layer.getBiome(this.getParentX(x), this.getParentY(z + 2)),
			layer.getBiome(this.getParentX(x), this.getParentY(z))
		);
	}
}
