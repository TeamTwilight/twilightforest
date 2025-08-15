package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record TravellersEntryModifier(List<ItemAttributeModifiers.Entry> modifiers, Optional<DataComponentType<Unit>> markerComponent) implements InsertableTravellersModifier {

	@SuppressWarnings("unchecked")
	public static final MapCodec<TravellersEntryModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(TravellersEntryModifier::modifiers),
		DataComponentType.CODEC.optionalFieldOf("component").xmap(componentType -> componentType.map(dataComponentType -> (DataComponentType<Unit>) dataComponentType), object -> object.map(dataComponentType -> dataComponentType)).forGetter(TravellersEntryModifier::markerComponent)
	).apply(instance, TravellersEntryModifier::new));

	public TravellersEntryModifier(List<ItemAttributeModifiers.Entry> modifiers) {
		this(modifiers, Optional.empty());
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}

	@Override
	public boolean addModifier(ItemStack stack) {
		if (this.markerComponent().isPresent()) {
			if (stack.getMaxDamage() - 1 <= stack.getDamageValue()) {
				ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
				for (ItemAttributeModifiers.Entry entry : this.modifiers()) {
					modifiers = modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
				}
				stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
			}

			stack.set(this.markerComponent().get(), Unit.INSTANCE);
			return true;
		}
		return false;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		if (this.markerComponent().isPresent()) {
			List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>();
			var modifiers = stack.getAttributeModifiers();
			modifiers.modifiers().forEach(entry -> {
				if (!this.modifiers().contains(entry)) {
					newEntries.add(entry);
				}
			});
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(newEntries, modifiers.showInTooltip()));
			this.markerComponent().ifPresent(stack::remove);
		}
	}

	@Override
	public boolean isAbility() {
		return this.markerComponent().isEmpty();
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		return this.markerComponent().map(stack::has).orElse(false) || entries.stream().anyMatch(entry -> entry.modifier().is(this.modifiers().getFirst().modifier().id()));
	}
}
