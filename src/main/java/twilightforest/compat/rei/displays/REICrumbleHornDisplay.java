package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.nbt.CompoundTag;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;

import java.util.List;

public class REICrumbleHornDisplay extends BasicDisplay {

	public final boolean isResultAir;

	public REICrumbleHornDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, boolean isResultAir) {
		super(inputs, outputs);
		this.isResultAir = isResultAir;
	}

	private REICrumbleHornDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CompoundTag tag) {
		this(inputs, outputs, tag.getBoolean("isResultAir"));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICrumbleHornCategory.CRUMBLE_HORN;
	}

	public static BasicDisplay.Serializer<REICrumbleHornDisplay> serializer() {
		return BasicDisplay.Serializer.ofRecipeLess(REICrumbleHornDisplay::new, (display, tag) -> tag.putBoolean("isResultAir", display.isResultAir));
	}
}
