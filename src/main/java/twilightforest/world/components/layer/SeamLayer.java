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
import twilightforest.util.Codecs;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;

import java.util.List;
import java.util.function.LongFunction;

public record SeamLayer(ResourceKey<Biome> partitioningBiome, List<ResourceKey<Biome>> excludedBiomeNeighbors, List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> excludedBiomeIntersections) implements CastleTransformer {
	@Override
	public ResourceKey<Biome> apply(Context context, ResourceKey<Biome> up, ResourceKey<Biome> left, ResourceKey<Biome> down, ResourceKey<Biome> right, ResourceKey<Biome> mid) {
		if (this.shouldPartition(mid, left) || this.shouldPartition(mid, right) || this.shouldPartition(mid, down) || this.shouldPartition(mid, up)) {
			return this.partitioningBiome;
		} else {
			return mid;
		}
	}

	private boolean shouldPartition(ResourceKey<Biome> biome1, ResourceKey<Biome> biome2) {
		if (biome1 == biome2)
			return false;

		for (ResourceKey<Biome> excludedBiome : this.excludedBiomeNeighbors)
			if (testEitherBiome(biome1, biome2, excludedBiome))
				return false;

		for (Pair<ResourceKey<Biome>, ResourceKey<Biome>> excludedIntersection : this.excludedBiomeIntersections)
			if (testEitherBiomeAND(biome1, biome2, excludedIntersection.getFirst(), excludedIntersection.getSecond()))
				return false;

		return true;
	}

	private static boolean testEitherBiomeAND(ResourceKey<Biome> test1, ResourceKey<Biome> test2, ResourceKey<Biome> predicate1, ResourceKey<Biome> predicate2) {
		return (test1 == predicate1 && test2 == predicate2) || (test2 == predicate1 && test1 == predicate2);
	}

	private static boolean testEitherBiome(ResourceKey<Biome> test1, ResourceKey<Biome> test2, ResourceKey<Biome> predicate) {
		return test1 == predicate || test2 == predicate;
	}

	public static final class Factory implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				ResourceKey.codec(Registries.BIOME).fieldOf("dividing_biome").forGetter(Factory::partitioningBiome),
				ResourceKey.codec(Registries.BIOME).listOf().fieldOf("excluded_neighbor_biomes").forGetter(Factory::excludedBiomeNeighbors),
				ResourceKey.codec(Registries.BIOME).listOf().comapFlatMap(Codecs::arrayToPair, p -> List.of(p.getFirst(), p.getSecond())).listOf().fieldOf("excluded_biome_intersections").forGetter(Factory::excludedBiomeIntersections),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));

		private final long salt;
		private final ResourceKey<Biome> partitioningBiome;
		private final List<ResourceKey<Biome>> excludedBiomeNeighbors;
		private final List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> excludedBiomeIntersections;
		private final Holder<BiomeLayerFactory> parent;
		private final SeamLayer instance;

		public Factory(long salt, ResourceKey<Biome> partitioningBiome, List<ResourceKey<Biome>> excludedBiomeNeighbors, List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> excludedBiomeIntersections, Holder<BiomeLayerFactory> parent) {
			this.salt = salt;
			this.partitioningBiome = partitioningBiome;
			this.excludedBiomeNeighbors = excludedBiomeNeighbors;
			this.excludedBiomeIntersections = excludedBiomeIntersections;
			this.parent = parent;

			this.instance = new SeamLayer(partitioningBiome, excludedBiomeNeighbors, excludedBiomeIntersections);
		}

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return this.instance.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.SEAM.get();
		}

		public long salt() {
			return this.salt;
		}

		public ResourceKey<Biome> partitioningBiome() {
			return this.partitioningBiome;
		}

		public List<ResourceKey<Biome>> excludedBiomeNeighbors() {
			return this.excludedBiomeNeighbors;
		}

		public List<Pair<ResourceKey<Biome>, ResourceKey<Biome>>> excludedBiomeIntersections() {
			return this.excludedBiomeIntersections;
		}

		public Holder<BiomeLayerFactory> parent() {
			return this.parent;
		}
	}
}
