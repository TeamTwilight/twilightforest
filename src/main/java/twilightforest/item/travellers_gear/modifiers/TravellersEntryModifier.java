package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record TravellersEntryModifier(List<ItemAttributeModifiers.Entry> activeModifiers, List<ItemAttributeModifiers.Entry> deactivatedModifiers, boolean builtin) implements InsertableTravellersModifier {

	public static final MapCodec<TravellersEntryModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("active_modifiers").forGetter(TravellersEntryModifier::activeModifiers),
		ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("deactivated_modifiers").forGetter(TravellersEntryModifier::deactivatedModifiers),
		Codec.BOOL.fieldOf("builtin_modifier").forGetter(TravellersEntryModifier::builtin)
	).apply(instance, (active, deactivated, builtin) -> {
		if (active.size() != deactivated.size()) {
			throw new IllegalArgumentException(String.format("Active and deactivated modifier lists must have the same sizes:%n%s%n%s", active, deactivated));
		}
		return new TravellersEntryModifier(active, deactivated, builtin);
	}));

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}

	@Override
	public boolean addModifier(ItemStack stack) {
		if (this.builtin()) return false;
		if (stack.getMaxDamage() - 1 <= stack.getDamageValue()) {
			ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
			for (ItemAttributeModifiers.Entry entry : this.activeModifiers()) {
				modifiers = modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
			}
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
		}

		return true;
	}

	@Override
	public void removeModifier(ItemStack stack) {
		if (this.builtin()) return;
		List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>();
		var modifiers = stack.getAttributeModifiers();
		modifiers.modifiers().forEach(entry -> {
			if (!this.activeModifiers.contains(entry) && !this.deactivatedModifiers.contains(entry)) {
				newEntries.add(entry);
			}
		});
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(newEntries, modifiers.showInTooltip()));
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		Optional<ItemAttributeModifiers.Entry> modifiers = entries.stream().filter(entry -> entry.modifier().is(this.deactivatedModifiers().getFirst().modifier().id()) || entry.modifier().is(this.activeModifiers().getFirst().modifier().id())).findAny();
		return modifiers.isPresent();
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
