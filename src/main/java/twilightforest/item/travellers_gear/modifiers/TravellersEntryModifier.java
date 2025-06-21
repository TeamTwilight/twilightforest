package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class TravellersEntryModifier implements TravellersModifier {
	protected final ItemAttributeModifiers.Entry activeModifier;
	protected final ItemAttributeModifiers.Entry deactivatedModifier;
	protected final String tooltipTranslationKey;
	public TravellersEntryModifier(ItemAttributeModifiers.Entry activeModifier, ItemAttributeModifiers.Entry deactivatedModifier) {
		this.activeModifier = activeModifier;
		this.deactivatedModifier = deactivatedModifier;
		this.tooltipTranslationKey = "travellers_gear.ability." + StringUtils.substringAfterLast(this.activeModifier.modifier().id().toString(), ':');
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		List<ItemAttributeModifiers.Entry> entries = stack.getAttributeModifiers().modifiers();
		Optional<ItemAttributeModifiers.Entry> modifiers = entries.stream().filter(entry -> entry.modifier().is(deactivatedModifier.modifier().id()) || entry.modifier().is(activeModifier.modifier().id())).findAny();
		return modifiers.isPresent();
	}

	@Override
	public String getTooltipTranslationKey() {
		return tooltipTranslationKey;
	}

	public void activate(ItemAttributeModifierEvent event) {
		boolean wasDeactivated = event.removeModifier(deactivatedModifier.attribute(), deactivatedModifier.modifier().id());
		if (wasDeactivated)
			event.addModifier(activeModifier.attribute(), activeModifier.modifier(), activeModifier.slot());
	}

	public void deactivate(ItemAttributeModifierEvent event) {
		boolean wasActivated = event.removeModifier(activeModifier.attribute(), activeModifier.modifier().id());
		if (wasActivated)
			event.addModifier(deactivatedModifier.attribute(), deactivatedModifier.modifier(), deactivatedModifier.slot());
	}

	public ItemAttributeModifiers.Entry getModifier() {
		return activeModifier;
	}
}
