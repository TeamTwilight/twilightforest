package twilightforest.item.travellers_gear.modifiers;


import com.google.common.base.Objects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TravellersGearComponentModifier implements TravellersModifier {
	protected final TypedDataComponent<?> typedDataComponent;
	protected final String tooltip;
	protected ResourceLocation datagenOnlyComponentId;

	private TravellersGearComponentModifier(TypedDataComponent<?> typedDataComponent, String tooltip) {
		this.typedDataComponent = typedDataComponent;
		this.tooltip = tooltip;
	}

	public <T> TravellersGearComponentModifier(DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value, String tooltip) {
		this(new TypedDataComponent<>(dataComponent.get(), value), tooltip);
		datagenOnlyComponentId = dataComponent.getId();
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
	public String getTooltipString() {
		return tooltip;
	}

	public ResourceLocation getDatagenOnlyComponentId() {
		return datagenOnlyComponentId;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(typedDataComponent, tooltip);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TravellersGearComponentModifier travellersGearComponentModifier)
			return travellersGearComponentModifier.typedDataComponent.equals(typedDataComponent) &&
				travellersGearComponentModifier.tooltip.equals(tooltip);
		return false;
	}

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersGearComponentModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DataComponentMap.CODEC.fieldOf("data_component_map").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.typedDataComponent.type(), o.typedDataComponent.value()).build()),
		Codec.STRING.fieldOf("tooltip").forGetter(o -> o.tooltip)
	).apply(instance, (map, tooltip) -> {
		if (map.size() != 1)
			throw new IllegalArgumentException("Expected exactly one entry in this data component map");
		TypedDataComponent<?> dataComponent = map.stream().findFirst().orElseThrow();
		return new TravellersGearComponentModifier(dataComponent, tooltip);
	}));
}
