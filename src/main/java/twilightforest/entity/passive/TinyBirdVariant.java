package twilightforest.entity.passive;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import twilightforest.TFRegistries;

import java.util.Objects;
import java.util.Optional;

public record TinyBirdVariant(ResourceLocation texture) {
	public static TinyBirdVariant getRandomVariant(RandomSource random) {
		return TFRegistries.TINY_BIRD_VARIANT.stream().toArray(TinyBirdVariant[]::new)[random.nextInt(TFRegistries.TINY_BIRD_VARIANT.keySet().size())];
	}

	public static Optional<TinyBirdVariant> getVariant(String id) {
		return Optional.ofNullable(TFRegistries.TINY_BIRD_VARIANT.get(new ResourceLocation(id)));
	}

	public static String getVariantId(TinyBirdVariant variant) {
		return Objects.requireNonNull(TFRegistries.TINY_BIRD_VARIANT.getKey(variant)).toString();
	}
}
