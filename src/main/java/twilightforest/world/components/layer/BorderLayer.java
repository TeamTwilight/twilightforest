package twilightforest.world.components.layer;

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

import java.util.function.LongFunction;

public record BorderLayer(ResourceKey<Biome> targetBiome, ResourceKey<Biome> borderBiome) implements IThornsTransformer {
	@Override
	public ResourceKey<Biome> apply(Context noise, ResourceKey<Biome> up, ResourceKey<Biome> left, ResourceKey<Biome> down, ResourceKey<Biome> right, ResourceKey<Biome> center, ResourceKey<Biome> nw, ResourceKey<Biome> sw, ResourceKey<Biome> se, ResourceKey<Biome> ne) {

		if (onBorder(this.targetBiome, center, right, left, up, down)) {
			return this.borderBiome;
		} else if (onBorder(this.targetBiome, center, ne, nw, se, sw)) {
			return this.borderBiome;
		} else {
			return center;
		}
	}

	/**
	 * Returns true if the center biome is not the specified biome and any of the surrounding biomes are the specified biomes
	 */
	private static boolean onBorder(ResourceKey<Biome> biomeID, ResourceKey<Biome> center, ResourceKey<Biome> right, ResourceKey<Biome> left, ResourceKey<Biome> up, ResourceKey<Biome> down) {
		return center != biomeID && (right == biomeID || left == biomeID || up == biomeID || down == biomeID);
	}

	public static final class Factory implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				ResourceKey.codec(Registries.BIOME).fieldOf("target_biome").forGetter(Factory::target),
				ResourceKey.codec(Registries.BIOME).fieldOf("bordering_biome").forGetter(Factory::borderingBiome),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));
		private final long salt;
		private final ResourceKey<Biome> target;
		private final ResourceKey<Biome> borderingBiome;
		private final Holder<BiomeLayerFactory> parent;

		private final BorderLayer instance;

		public Factory(long salt, ResourceKey<Biome> centerBiome, ResourceKey<Biome> borderingBiome, Holder<BiomeLayerFactory> parent) {
			this.salt = salt;
			this.target = centerBiome;
			this.borderingBiome = borderingBiome;
			this.parent = parent;

			this.instance = new BorderLayer(centerBiome, borderingBiome);
		}

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return this.instance.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.BORDER.get();
		}

		public long salt() {
			return this.salt;
		}

		public ResourceKey<Biome> target() {
			return this.target;
		}

		public ResourceKey<Biome> borderingBiome() {
			return this.borderingBiome;
		}

		public Holder<BiomeLayerFactory> parent() {
			return this.parent;
		}
	}
}
