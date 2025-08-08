package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;
import java.util.Optional;

public class TravellersEntryModifier implements TravellersModifier {
	protected final List<ItemAttributeModifiers.Entry> activeModifiers;
	protected final List<ItemAttributeModifiers.Entry> deactivatedModifiers;
	protected final ResourceLocation name;

	public TravellersEntryModifier(ResourceLocation name, List<ItemAttributeModifiers.Entry> activeModifiers, List<ItemAttributeModifiers.Entry> deactivatedModifiers) {
		this.activeModifiers = activeModifiers;
		this.deactivatedModifiers = deactivatedModifiers;
		this.name = name;
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		Optional<ItemAttributeModifiers.Entry> modifiers = entries.stream().filter(entry -> entry.modifier().is(this.deactivatedModifiers.getFirst().modifier().id()) || entry.modifier().is(this.activeModifiers.getFirst().modifier().id())).findAny();
		return modifiers.isPresent();
	}

	@Override
	public ResourceLocation getName() {
		return this.name;
	}

	public void activate(ItemAttributeModifierEvent event) {
		for (int i = 0; i < this.activeModifiers.size(); i++) {
			var deactivated = this.deactivatedModifiers.get(i);
			var active = this.activeModifiers.get(i);
			boolean wasDeactivated = event.removeModifier(deactivated.attribute(), deactivated.modifier().id());
			if (wasDeactivated)
				event.addModifier(active.attribute(), active.modifier(), active.slot());
		}
	}

	public void deactivate(ItemAttributeModifierEvent event) {
		for (int i = 0; i < this.activeModifiers.size(); i++) {
			var deactivated = this.deactivatedModifiers.get(i);
			var active = this.activeModifiers.get(i);
			boolean wasActivated = event.removeModifier(active.attribute(), active.modifier().id());
			if (wasActivated)
				event.addModifier(deactivated.attribute(), deactivated.modifier(), deactivated.slot());
		}
	}

	public static final MapCodec<TravellersEntryModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("name")
				.forGetter(modifier -> modifier.name),
			ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("active_modifiers")
				.forGetter(modifier -> modifier.activeModifiers),
			ItemAttributeModifiers.Entry.CODEC.listOf().fieldOf("deactivated_modifiers")
				.forGetter(modifier -> modifier.deactivatedModifiers)
		).apply(instance, (name, active, deactivated) -> {
			if (active.size() != deactivated.size()) {
				throw new IllegalArgumentException(String.format("Active and deactivated modifier lists must have the same sizes:\n{}\n{}", active, deactivated));
			}
			return new TravellersEntryModifier(name, active, deactivated);
		}));
}
