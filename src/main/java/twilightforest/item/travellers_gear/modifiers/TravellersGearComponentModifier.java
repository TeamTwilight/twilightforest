package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TravellersGearComponentModifier<T> implements Modifier {
	protected final DataComponentType<T> dataComponent;
	protected final T value;
	protected final String tooltip;

	public TravellersGearComponentModifier(DataComponentType<T> dataComponent, T value, String tooltip) {
		this.dataComponent = dataComponent;
		this.value = value;
		this.tooltip = tooltip;
	}

	public TravellersGearComponentModifier(DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value, String tooltip) {
		this(dataComponent.get(), value, tooltip);
	}

	@Override
	public void addModifier(ItemStack stack) {
		stack.set(dataComponent, value);
	}

	@Override
	public void removeModifier(ItemStack stack) {
		stack.remove(dataComponent);
	}

	@Override
	public String getTooltipString() {
		return tooltip;
	}
}
