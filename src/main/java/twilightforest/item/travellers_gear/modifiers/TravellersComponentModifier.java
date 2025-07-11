package twilightforest.item.travellers_gear.modifiers;


import com.google.common.base.Objects;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

public class TravellersComponentModifier implements InsertableTravellersModifier {
	protected final TypedDataComponent<?> typedDataComponent;
	protected final String tooltipTranslationKey;

	private TravellersComponentModifier(TypedDataComponent<?> typedDataComponent) {
		this.typedDataComponent = typedDataComponent;
		this.tooltipTranslationKey = "travellers_gear.modifier." + StringUtils.substringAfterLast(typedDataComponent.type().toString(), ':');
	}

	public <T> TravellersComponentModifier(DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value) {
		this(new TypedDataComponent<>(dataComponent.get(), value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addModifier(ItemStack stack) {
		stack.set((DataComponentType<Object>) typedDataComponent.type(), typedDataComponent.value());
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.get(typedDataComponent.type()) != null;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		stack.remove(typedDataComponent.type());
	}

	@Override
	public String getTooltipTranslationKey() {
		return tooltipTranslationKey;
	}

	public ResourceLocation getDataComponentTypeId() {
		return ResourceLocation.parse(Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this.typedDataComponent.type()));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(typedDataComponent);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TravellersComponentModifier travellersGearComponentModifier)
			return travellersGearComponentModifier.typedDataComponent.equals(typedDataComponent);
		return false;
	}

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersComponentModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DataComponentMap.CODEC
			.fieldOf("data_component_map").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.typedDataComponent.type(), o.typedDataComponent.value()).build())
	).apply(instance, (map) -> {
		if (map.size() != 1)
			throw new IllegalArgumentException("Expected exactly one entry in this data component map");
		TypedDataComponent<?> dataComponent = map.stream().findFirst().orElseThrow();
		return new TravellersComponentModifier(dataComponent);
	}));
}
