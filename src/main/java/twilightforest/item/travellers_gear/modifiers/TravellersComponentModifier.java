package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record TravellersComponentModifier(TypedDataComponent<?> component) implements InsertableTravellersModifier {

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersComponentModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DataComponentMap.CODEC.fieldOf("component").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.component().type(), o.component().value()).build())
	).apply(instance, (map) -> {
		if (map.size() != 1)
			throw new IllegalArgumentException("Expected exactly one entry in this data component map");
		TypedDataComponent<?> dataComponent = map.stream().findFirst().orElseThrow();
		return new TravellersComponentModifier(dataComponent);
	}));

	public <T> TravellersComponentModifier(DataComponentType<T> component, T defaultValue) {
		this(new TypedDataComponent<>(component, defaultValue));
	}

	@Override
	public boolean isAbility() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addModifier(ItemStack stack) {
		stack.set((DataComponentType<Object>) this.component().type(), this.component().value());
		return true;
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.get(this.component().type()) != null;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		stack.remove(this.component().type());
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}
}
