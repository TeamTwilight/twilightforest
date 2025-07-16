package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

public class BuiltinTravellersComponentModifier implements TravellersModifier{
	protected final DataComponentType<?> dataComponentType;
	protected final String tooltipTranslationKey;
	protected final ResourceLocation name;

	public <T> BuiltinTravellersComponentModifier(ResourceLocation name, DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponentType) {
		this.dataComponentType = dataComponentType.get();
		this.name = name;
		this.tooltipTranslationKey = "travellers_gear.ability." + name.toString().replace(":", ".");
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.get(dataComponentType) != null;
	}

	@Override
	public String getTooltipTranslationKey() {
		return tooltipTranslationKey;
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}
}
