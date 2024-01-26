package twilightforest.entity.passive;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import twilightforest.TFRegistries;

import java.util.Objects;
import java.util.Optional;

public record DwarfRabbitVariant(ResourceLocation texture) {

	public static DwarfRabbitVariant getRandomVariant(RandomSource random) {
		return TFRegistries.DWARF_RABBIT_VARIANT.stream().toArray(DwarfRabbitVariant[]::new)[random.nextInt(TFRegistries.DWARF_RABBIT_VARIANT.keySet().size())];
	}

	public static Optional<DwarfRabbitVariant> getVariant(String id) {
		return Optional.ofNullable(TFRegistries.DWARF_RABBIT_VARIANT.get(new ResourceLocation(id)));
	}

	public static String getVariantId(DwarfRabbitVariant variant) {
		return Objects.requireNonNull(TFRegistries.DWARF_RABBIT_VARIANT.getKey(variant)).toString();
	}
}
