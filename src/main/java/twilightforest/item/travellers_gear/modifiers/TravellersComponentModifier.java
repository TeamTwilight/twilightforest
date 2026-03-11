package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record TravellersComponentModifier(EquipmentSlotGroup group, TypedDataComponent<?> component, List<Component> description) implements InsertableTravellersModifier {

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersComponentModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		EquipmentSlotGroup.CODEC.fieldOf("equipment_slots").validate(TravellersModifier::validateEquipment).forGetter(TravellersComponentModifier::group),
		DataComponentMap.CODEC.fieldOf("component").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.component().type(), o.component().value()).build()),
		ComponentSerialization.CODEC.listOf().optionalFieldOf("description", List.of()).forGetter(TravellersComponentModifier::description)
	).apply(instance, (group, map, description) -> {
		if (map.size() != 1)
			throw new IllegalArgumentException("Expected exactly one entry in this data component map");
		TypedDataComponent<?> dataComponent = map.stream().findFirst().orElseThrow();
		return new TravellersComponentModifier(group, dataComponent, description);
	}));

	public <T> TravellersComponentModifier(EquipmentSlotGroup group, DataComponentType<T> component, T defaultValue, List<Component> description) {
		this(group, new TypedDataComponent<>(component, defaultValue), description);
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

	@Override
	public List<Component> getDescription() {
		return this.description;
	}
}
