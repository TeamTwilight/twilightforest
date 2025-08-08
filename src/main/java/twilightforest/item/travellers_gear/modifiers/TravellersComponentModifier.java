package twilightforest.item.travellers_gear.modifiers;


import com.google.common.base.Objects;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TravellersComponentModifier implements InsertableTravellersModifier {
	@Nullable
	private static String TYPE_NAME = null;
	protected final TypedDataComponent<?> typedDataComponent;
	protected final ResourceLocation name;

	private TravellersComponentModifier(ResourceLocation name, TypedDataComponent<?> typedDataComponent) {
		this.typedDataComponent = typedDataComponent;
		this.name = name;
	}

	public <T> TravellersComponentModifier(ResourceLocation name, DataComponentType<T> dataComponent, T value) {
		this(name, new TypedDataComponent<>(dataComponent, value));
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
	public ResourceLocation getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, typedDataComponent);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TravellersComponentModifier modifier)
			return modifier.name.equals(name) && modifier.typedDataComponent.equals(typedDataComponent);
		return false;
	}

	public static void setTypeName(String typeName) {
		TYPE_NAME = typeName;
		TravellersModifierTypes.TYPE_TO_CODEC.put(typeName, MAP_CODEC);
	}

	@Override
	public String getTypeName() {
		TravellersModifierTypes.initialize();
		return TYPE_NAME;
	}

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersComponentModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("name").forGetter(o -> o.name),
		DataComponentMap.CODEC.fieldOf("data_component_map").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.typedDataComponent.type(), o.typedDataComponent.value()).build())
	).apply(instance, (location, map) -> {
		if (map.size() != 1)
			throw new IllegalArgumentException("Expected exactly one entry in this data component map");
		TypedDataComponent<?> dataComponent = map.stream().findFirst().orElseThrow();
		return new TravellersComponentModifier(location, dataComponent);
	}));
}
