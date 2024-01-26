package twilightforest.world.components.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.LongFunction;

/**
 * Puts key biomes in the proper positions
 *
 * @author Ben
 */
public record KeyBiomesLayer(List<ResourceKey<Biome>> keyBiomes) implements AreaTransformer1 {
	private static final Random RANDOM = new Random();

	@Override
	public int getParentX(int x) {
		return x | 3;
	}

	@Override
	public int getParentY(int z) {
		return z | 3;
	}

	@Override
	public ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z) {
		RANDOM.setSeed(TFDimensionSettings.seed + (x & -4) * 25117L + (z & -4) * 151121L);
		int ox = RANDOM.nextInt(2) + 1;
		int oz = RANDOM.nextInt(2) + 1;
		RANDOM.setSeed(TFDimensionSettings.seed + (x / 8) * 25117L + (z / 8) * 151121L);
		int offset = RANDOM.nextInt(3);
		if ((x & 3) == ox && (z & 3) == oz) {
			// determine which of the 4
			if ((x & 4) == 0) {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset);
				} else {
					return getKeyBiomeFor(offset + 1);
				}
			} else {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset + 2);
				} else {
					return getKeyBiomeFor(offset + 3);
				}
			}

		} else {
			return layer.getBiome(x, z);
		}
	}

	/**
	 * Determine which map "region" the specified points are in.  Assign the 0-3 of the index to the key biomes based on that region.
	 */
	private ResourceKey<Biome> getKeyBiomeFor(int index) {
		// do we need to shuffle this better?
		// the current version just "rotates" the 4 key biomes
		return this.keyBiomes.get(index & 0b11);
	}

	public static final class Factory implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				ResourceKey.codec(Registries.BIOME).listOf().comapFlatMap(list -> Util.fixedSize(list, 4), Function.identity()).fieldOf("key_biomes").forGetter(Factory::keyBiomes),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));
		private final long salt;
		private final List<ResourceKey<Biome>> keyBiomes;
		private final Holder<BiomeLayerFactory> parent;

		private final KeyBiomesLayer instance;

		public Factory(long salt, List<ResourceKey<Biome>> keyBiomes, Holder<BiomeLayerFactory> parent) {
			this.salt = salt;
			this.keyBiomes = keyBiomes;
			this.parent = parent;

			this.instance = new KeyBiomesLayer(keyBiomes);
		}

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return this.instance.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.KEY_BIOMES.get();
		}

		public long salt() {
			return this.salt;
		}

		public List<ResourceKey<Biome>> keyBiomes() {
			return this.keyBiomes;
		}

		public Holder<BiomeLayerFactory> parent() {
			return this.parent;
		}
	}
}
