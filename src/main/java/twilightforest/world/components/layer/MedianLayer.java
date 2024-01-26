package twilightforest.world.components.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;

import java.util.function.LongFunction;

public enum MedianLayer implements AreaTransformer1 {
	INSTANCE;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z) {
		ResourceKey[] biomes = new ResourceKey[9];

		for (int pos = 0; pos < 9; pos++) {
			biomes[pos] = layer.getBiome(x + (pos % 3), z + (pos / 3));
		}

		int biomeRecordIndex = 0;
		int biomeRecordCount = 0;

		int iterationQuantity;
		for (int index = 0; index < 9; index++) {
			iterationQuantity = 0;

			for (int test = 0; test < 9; test++) {
				if (biomes[index] == biomes[test]) {
					iterationQuantity++;

					if (iterationQuantity > 4) { // 5 out of 9 is a Home Run! Bye!
						return biomes[index];
					}
				}
			}

			// If there are two biomes with same dominating quantity, then randomly pick unless it is the central biome.
			if (biomeRecordCount == iterationQuantity && (index == 5 || (biomeRecordIndex != 5 && context.nextRandom(2) == 0))) {
				biomeRecordIndex = index;
			}

			if (biomeRecordCount > iterationQuantity) {
				biomeRecordIndex = index;
				biomeRecordCount = iterationQuantity;
			}
		}

		return biomes[biomeRecordIndex]; // default center biome
	}

	@Override
	public int getParentX(int x) {
		return x;
	}

	@Override
	public int getParentY(int z) {
		return z;
	}

	public record Factory(long salt, Holder<BiomeLayerFactory> parent) implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return INSTANCE.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.MEDIAN.get();
		}
	}
}
