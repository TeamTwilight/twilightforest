package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class TravellersEntryModifier implements TravellersModifier {
	protected final List<ItemAttributeModifiers.Entry> activeModifiers;
	protected final List<ItemAttributeModifiers.Entry> deactivatedModifiers;
	protected final String tooltipTranslationKey;
	protected final ResourceLocation name;

	public TravellersEntryModifier(ResourceLocation name, List<ItemAttributeModifiers.Entry> activeModifiers, List<ItemAttributeModifiers.Entry> deactivatedModifiers) {
		this.activeModifiers = activeModifiers;
		this.deactivatedModifiers = deactivatedModifiers;
		this.name = name;
		this.tooltipTranslationKey = "travellers_gear.ability." + name.toString().replace(":", ".");
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		Optional<ItemAttributeModifiers.Entry> modifiers = entries.stream().filter(entry -> entry.modifier().is(this.deactivatedModifiers.getFirst().modifier().id()) || entry.modifier().is(this.activeModifiers.getFirst().modifier().id())).findAny();
		return modifiers.isPresent();
	}

	@Override
	public String getTooltipTranslationKey() {
		return this.tooltipTranslationKey;
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

	public static ItemAttributeModifiers.Builder addModifiers(ItemAttributeModifiers.Builder attributes, TravellersEntryModifier modifier) {
		for (ItemAttributeModifiers.Entry entry : modifier.activeModifiers) {
			attributes.add(entry.attribute(), entry.modifier(), entry.slot());
		}
		return attributes;
	}
}
