package twilightforest.world.components.layer;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;

import java.util.List;
import java.util.function.LongFunction;

public record CompanionBiomesLayer(List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> biomeCompanions) implements CastleTransformer {
	@Override
	public ResourceKey<Biome> apply(Context context, ResourceKey<Biome> up, ResourceKey<Biome> right, ResourceKey<Biome> down, ResourceKey<Biome> left, ResourceKey<Biome> center) {
		for (Pair<ResourceKey<Biome>, ResourceKey<Biome>> biomeCompanion : this.biomeCompanions)
			if (isKey(biomeCompanion.getFirst(), center, left, right, up, down))
				return biomeCompanion.getSecond();

		return center;
	}

	/**
	 * Returns true if any of the surrounding biomes is the specified biome
	 */
	private static boolean isKey(ResourceKey<Biome> biome, ResourceKey<Biome> center, ResourceKey<Biome> right, ResourceKey<Biome> left, ResourceKey<Biome> up, ResourceKey<Biome> down) {
		return center != biome && (right == biome || left == biome || up == biome || down == biome);
	}

	public static final class Factory implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				Codec.mapPair(ResourceKey.codec(Registries.BIOME).fieldOf("key"), ResourceKey.codec(Registries.BIOME).fieldOf("companion")).codec().listOf().fieldOf("keys_to_companions").forGetter(Factory::biomeCompanions),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));
		private final long salt;
		private final List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> biomeCompanions;
		private final Holder<BiomeLayerFactory> parent;
		private final CompanionBiomesLayer instance;

		public Factory(long salt, List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> biomeCompanions, Holder<BiomeLayerFactory> parent) {
			this.salt = salt;
			this.biomeCompanions = biomeCompanions;
			this.parent = parent;

			this.instance = new CompanionBiomesLayer(biomeCompanions);
		}

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return this.instance.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.COMPANION_BIOMES.get();
		}

		public long salt() {
			return this.salt;
		}

		public List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> biomeCompanions() {
			return this.biomeCompanions;
		}

		public Holder<BiomeLayerFactory> parent() {
			return this.parent;
		}
	}
}
