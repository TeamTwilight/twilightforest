package twilightforest.world.components.layer.vanillalegacy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;

import java.util.function.LongFunction;

public enum SmoothLayer implements CastleTransformer {
	INSTANCE;

	@Override
	public ResourceKey<Biome> apply(Context context, ResourceKey<Biome> up, ResourceKey<Biome> right, ResourceKey<Biome> down, ResourceKey<Biome> left, ResourceKey<Biome> center) {
		boolean xMatch = right == left;
		boolean zMatch = up == down;
		if (xMatch == zMatch) {
			if (xMatch) {
				return context.nextRandom(2) == 0 ? left : up;
			} else {
				return center;
			}
		} else {
			return xMatch ? left : up;
		}
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
			return BiomeLayerTypes.SMOOTH.get();
		}
	}
}
