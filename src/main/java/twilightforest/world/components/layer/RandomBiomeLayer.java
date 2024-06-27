package twilightforest.world.components.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer0;

import java.util.List;
import java.util.function.LongFunction;

/**
 * Applies the twilight forest biomes to the map
 *
 * @author Ben
 */
public class RandomBiomeLayer implements AreaTransformer0 {
	private final int rareBiomeChance;

	private final List<ResourceKey<Biome>> commonBiomes;
	private final List<ResourceKey<Biome>> rareBiomes;

	public RandomBiomeLayer(int rareBiomeChance, List<ResourceKey<Biome>> commonBiomes, List<ResourceKey<Biome>> rareBiomes) {
		this.rareBiomeChance = rareBiomeChance;
		this.commonBiomes = commonBiomes;
		this.rareBiomes = rareBiomes;
	}

	@Override
	public ResourceKey<Biome> applyPixel(Context context, int x, int z) {
		if (context.nextRandom(this.rareBiomeChance) == 0) {
			// make specialBiomes biome
			return this.rareBiomes.get(context.nextRandom(this.rareBiomes.size()));
		} else {
			// make common biome
			return this.commonBiomes.get(context.nextRandom(this.commonBiomes.size()));
		}
	}

	public static final class Factory implements BiomeLayerFactory {
		public static final MapCodec<Factory> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
			Codec.INT.fieldOf("rare_biome_chance").forGetter(Factory::rareBiomeChance),
			ResourceKey.codec(Registries.BIOME).listOf().fieldOf("common_biomes").forGetter(Factory::commonBiomes),
			ResourceKey.codec(Registries.BIOME).listOf().fieldOf("rare_biomes").forGetter(Factory::rareBiomes)
		).apply(inst, Factory::new));
		private final long salt;
		private final int rareBiomeChance;
		private final List<ResourceKey<Biome>> commonBiomes;
		private final List<ResourceKey<Biome>> rareBiomes;

		private final RandomBiomeLayer instance;

		public Factory(long salt, int rareBiomeChance, List<ResourceKey<Biome>> commonBiomes, List<ResourceKey<Biome>> rareBiomes) {
			this.salt = salt;
			this.rareBiomeChance = rareBiomeChance;
			this.commonBiomes = commonBiomes;
			this.rareBiomes = rareBiomes;

			this.instance = new RandomBiomeLayer(rareBiomeChance, commonBiomes, rareBiomes);
		}

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return this.instance.run(contextFactory.apply(this.salt));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.RANDOM_BIOMES.get();
		}

		public long salt() {
			return this.salt;
		}

		public int rareBiomeChance() {
			return this.rareBiomeChance;
		}

		public List<ResourceKey<Biome>> commonBiomes() {
			return this.commonBiomes;
		}

		public List<ResourceKey<Biome>> rareBiomes() {
			return this.rareBiomes;
		}
	}
}
