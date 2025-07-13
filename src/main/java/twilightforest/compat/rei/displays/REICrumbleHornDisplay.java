package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.compat.rei.TFREIClientPlugin;
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

	public static REICrumbleHornDisplay of(Block input, Block output) {
		boolean isResultAir = output.defaultBlockState().isAir();
		List<EntryIngredient> inputs = List.of(EntryIngredients.of(input));
		List<EntryIngredient> outputs = isResultAir
			? List.of(EntryIngredient.of(EntryStack.of(TFREIClientPlugin.ENTITY_DEFINITION, TFREIClientPlugin.createItemEntity(new ItemStack(input)))))
			: List.of(EntryIngredients.of(output));
		return new REICrumbleHornDisplay(inputs, outputs, isResultAir);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICrumbleHornCategory.CRUMBLE_HORN;
	}

	public static BasicDisplay.Serializer<REICrumbleHornDisplay> serializer() {
		return BasicDisplay.Serializer.ofRecipeLess(REICrumbleHornDisplay::new, (display, tag) -> tag.putBoolean("isResultAir", display.isResultAir));
	}
}
