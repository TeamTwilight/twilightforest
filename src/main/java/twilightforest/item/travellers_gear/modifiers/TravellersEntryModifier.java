package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record TravellersEntryModifier(EquipmentSlotGroup group, List<ItemAttributeModifiers.Entry> modifiers, DataComponentType<Unit> markerComponent, List<Component> description, boolean builtin) implements InsertableTravellersModifier {

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersEntryModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		EquipmentSlotGroup.CODEC.fieldOf("equipment_slots").validate(TravellersModifier::validateEquipment).forGetter(TravellersEntryModifier::group),
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(TravellersEntryModifier::modifiers),
		DataComponentType.CODEC.fieldOf("component").xmap(component -> (DataComponentType<Unit>) component, object -> object).forGetter(TravellersEntryModifier::markerComponent),
		ComponentSerialization.CODEC.listOf().optionalFieldOf("description", List.of()).forGetter(TravellersEntryModifier::description),
		Codec.BOOL.fieldOf("builtin_modifier").orElse(false).forGetter(TravellersEntryModifier::builtin)
	).apply(instance, TravellersEntryModifier::new));

	public TravellersEntryModifier(EquipmentSlotGroup group, List<ItemAttributeModifiers.Entry> modifiers, Supplier<DataComponentType<Unit>> markerComponent, List<Component> description, boolean builtin) {
		this(group, modifiers, markerComponent.get(), description, builtin);
	}

	public TravellersEntryModifier(EquipmentSlotGroup group, List<ItemAttributeModifiers.Entry> modifiers, Supplier<DataComponentType<Unit>> markerComponent, boolean builtin) {
		this(group, modifiers, markerComponent, List.of(), builtin);
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}

	@Override
	public boolean addModifier(ItemStack stack) {
		if (!this.builtin()) {
			ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
			for (ItemAttributeModifiers.Entry entry : this.modifiers()) {
				modifiers = modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
			}
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
		}
		stack.set(this.markerComponent(), Unit.INSTANCE);
		return true;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		if (!this.builtin()) {
			List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>();
			var modifiers = stack.getAttributeModifiers();
			modifiers.modifiers().forEach(entry -> {
				if (!this.modifiers().contains(entry)) {
					newEntries.add(entry);
				}
			});
			var builder = ItemAttributeModifiers.builder();
			newEntries.forEach(entry -> builder.add(entry.attribute(), entry.modifier(), entry.slot()));
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
			stack.remove(this.markerComponent());
		}
	}

	@Override
	public boolean isAbility() {
		return this.builtin();
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.has(this.markerComponent());
	}

	@Override
	public List<Component> getDescription() {
		return this.description;
	}
}
