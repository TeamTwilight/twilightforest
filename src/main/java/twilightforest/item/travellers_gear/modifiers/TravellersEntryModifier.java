package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;
import java.util.Optional;

public record TravellersEntryModifier(ResourceLocation name, List<ItemAttributeModifiers.Entry> activeModifiers, List<ItemAttributeModifiers.Entry> deactivatedModifiers, Optional<DataComponentType<?>> markerComponent) implements InsertableTravellersModifier {

	public static final MapCodec<TravellersEntryModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("name").forGetter(TravellersEntryModifier::name),
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("active_modifiers").forGetter(TravellersEntryModifier::activeModifiers),
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("deactivated_modifiers").forGetter(TravellersEntryModifier::deactivatedModifiers),
		DataComponentType.CODEC.optionalFieldOf("component").forGetter(TravellersEntryModifier::markerComponent)
	).apply(instance, (name, active, deactivated, component) -> {
		if (active.size() != deactivated.size()) {
			throw new IllegalArgumentException(String.format("Active and deactivated modifier lists must have the same sizes:%n%s%n%s", active, deactivated));
		}
		return new TravellersEntryModifier(name, active, deactivated, component);
	}));

	public TravellersEntryModifier(ResourceLocation name, List<ItemAttributeModifiers.Entry> activeModifiers, List<ItemAttributeModifiers.Entry> deactivatedModifiers) {
		this(name, activeModifiers, deactivatedModifiers, Optional.empty());
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addModifier(ItemStack stack) {
		if (this.markerComponent().isPresent()) {
			stack.set((DataComponentType<Unit>) this.markerComponent().get(), Unit.INSTANCE);

			ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
			for (ItemAttributeModifiers.Entry entry : this.activeModifiers()) {
				modifiers = modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
			}
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);

			return true;
		}
		return false;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		this.markerComponent().ifPresent(stack::remove);
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		Optional<ItemAttributeModifiers.Entry> modifiers = entries.stream().filter(entry -> entry.modifier().is(this.deactivatedModifiers().getFirst().modifier().id()) || entry.modifier().is(this.activeModifiers().getFirst().modifier().id())).findAny();
		return modifiers.isPresent() || this.markerComponent().map(stack::has).orElse(false);
	}

	public void activate(ItemAttributeModifierEvent event) {
		for (int i = 0; i < this.activeModifiers().size(); i++) {
			var deactivated = this.deactivatedModifiers().get(i);
			var active = this.activeModifiers().get(i);
			boolean wasDeactivated = event.removeModifier(deactivated.attribute(), deactivated.modifier().id());
			if (wasDeactivated)
				event.addModifier(active.attribute(), active.modifier(), active.slot());
		}
	}

	public void deactivate(ItemAttributeModifierEvent event) {
		for (int i = 0; i < this.activeModifiers().size(); i++) {
			var deactivated = this.deactivatedModifiers().get(i);
			var active = this.activeModifiers().get(i);
			boolean wasActivated = event.removeModifier(active.attribute(), active.modifier().id());
			if (wasActivated)
				event.addModifier(deactivated.attribute(), deactivated.modifier(), deactivated.slot());
		}
	}
}
