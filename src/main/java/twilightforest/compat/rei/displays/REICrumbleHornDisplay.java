package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.DefaultCompostingDisplay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;

import java.util.List;
import java.util.Optional;

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
