package twilightforest.world.components.layer.vanillalegacy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;

import java.util.function.LongFunction;

public enum ZoomLayer implements AreaTransformer1 {
	NORMAL,
	FUZZY {
		@Override
		protected ResourceKey<Biome> modeOrRandom(BigContext<?> context, ResourceKey<Biome> p_76980_, ResourceKey<Biome> p_76981_, ResourceKey<Biome> p_76982_, ResourceKey<Biome> p_76983_) {
			return context.random(p_76980_, p_76981_, p_76982_, p_76983_);
		}
	};

	private static final int ZOOM_BITS = 1;
	private static final int ZOOM_MASK = 1;

	@Override
    public int getParentX(int x) {
		return x >> 1;
	}

	@Override
	public int getParentY(int z) {
		return z >> 1;
	}

	@Override
	public ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z) {
		ResourceKey<Biome> i = layer.getBiome(this.getParentX(x), this.getParentY(z));
		context.initRandom(x >> 1 << 1, z >> 1 << 1);
		int j = x & 1;
		int k = z & 1;
		if (j == 0 && k == 0) {
			return i;
		} else {
			ResourceKey<Biome> l = layer.getBiome(this.getParentX(x), this.getParentY(z + 1));
			ResourceKey<Biome> i1 = context.random(i, l);
			if (j == 0 && k == 1) {
				return i1;
			} else {
				ResourceKey<Biome> j1 = layer.getBiome(this.getParentX(x + 1), this.getParentY(z));
				ResourceKey<Biome> k1 = context.random(i, j1);
				if (j == 1 && k == 0) {
					return k1;
				} else {
					ResourceKey<Biome> l1 = layer.getBiome(this.getParentX(x + 1), this.getParentY(z + 1));
					return this.modeOrRandom(context, i, j1, l, l1);
				}
			}
		}
	}

	protected ResourceKey<Biome> modeOrRandom(BigContext<?> p_76960_, ResourceKey<Biome> p_76961_, ResourceKey<Biome> p_76962_, ResourceKey<Biome> p_76963_, ResourceKey<Biome> p_76964_) {
		if (p_76962_ == p_76963_ && p_76963_ == p_76964_) {
			return p_76962_;
		} else if (p_76961_ == p_76962_ && p_76961_ == p_76963_) {
			return p_76961_;
		} else if (p_76961_ == p_76962_ && p_76961_ == p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76963_ && p_76961_ == p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76962_ && p_76963_ != p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76963_ && p_76962_ != p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76964_ && p_76962_ != p_76963_) {
			return p_76961_;
		} else if (p_76962_ == p_76963_ && p_76961_ != p_76964_) {
			return p_76962_;
		} else if (p_76962_ == p_76964_ && p_76961_ != p_76963_) {
			return p_76962_;
		} else {
			return p_76963_ == p_76964_ && p_76961_ != p_76962_ ? p_76963_ : p_76960_.random(p_76961_, p_76962_, p_76963_, p_76964_);
		}
	}

	public record Factory(long salt, boolean fuzzy, Holder<BiomeLayerFactory> parent) implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				Codec.BOOL.fieldOf("fuzzy").forGetter(Factory::fuzzy),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));

		// TODO Parameterize bit-shifting quantities
		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			LazyAreaContext seededContext = contextFactory.apply(this.salt);
			LazyArea parentLayer = this.parent.value().build(contextFactory);

			if (this.fuzzy)
				return FUZZY.run(seededContext, parentLayer);
			else
				return NORMAL.run(seededContext, parentLayer);
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.ZOOM.get();
		}
	}
}
