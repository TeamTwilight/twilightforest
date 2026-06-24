package twilightforest.compat.rei.fillers;

import me.shedaniel.rei.api.client.entry.type.BuiltinClientEntryTypes;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapedDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;

import java.util.Collection;
import java.util.List;

public class REITravellersGearModifierRecipeFiller implements CraftingRecipeFiller<TravellersGearModifierRecipe> {
	@Override
	public Collection<Display> apply(RecipeHolder<TravellersGearModifierRecipe> recipeHolder) {
		TravellersGearModifierRecipe recipe = recipeHolder.value();
		List<Ingredient> ingredients = recipe.getIngredients();
		ItemStack output = TravellersGearModifierRecipe.getModifiableArmorFromIngredients(ingredients).copy();
		recipe.applyModifier(Minecraft.getInstance().level.registryAccess(), output, ingredients);
		Renderer renderer = EntryStack.of(VanillaEntryTypes.ITEM.getDefinition(), output);
		return List.of(
			new DefaultCustomShapedDisplay(
				recipeHolder,
				EntryIngredients.ofIngredients(ingredients),
				// using BuiltinClientEntryTypes.RENDERING allows us to avoid showing modifier recipes as a way to get craft Traveller's Gear
				List.of(EntryIngredient.of(EntryStack.of(BuiltinClientEntryTypes.RENDERING, renderer))),
				recipe.getWidth(), recipe.getHeight()
			)
		);
	}

	@Override
	public Class<TravellersGearModifierRecipe> getRecipeClass() {
		return TravellersGearModifierRecipe.class;
	}
}
