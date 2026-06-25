package twilightforest.item.recipe.travellers;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearModifierShapelessRecipe extends TravellersGearModifierRecipe {
	protected final NonNullList<Ingredient> ingredients;

	public TravellersGearModifierShapelessRecipe(NonNullList<Ingredient> ingredients, ResourceKey<TravellersModifier> travellersModifier) {
		super(travellersModifier);
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		if (!super.matches(input, level))
			return false;
		if (input.ingredientCount() != this.ingredients.size())
			return false;
		List<ItemStack> nonEmptyItems = new ArrayList<>(input.ingredientCount());
		for (ItemStack item : input.items()) {
			if (!item.isEmpty())
				nonEmptyItems.add(item);
		}
		return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public int getWidth() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public int getHeight() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public boolean isShapeless() {
		return true;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.MODIFIER_SHAPELESS_RECIPE_SERIALIZER.get();
	}

	public static class Serializer extends AbstractModifierRecipeSerializer<TravellersGearModifierShapelessRecipe> {
		public Serializer() {
			super(RecordCodecBuilder.mapCodec(instance -> instance.group(
				NonNullList.codecOf(Ingredient.CODEC)
					.fieldOf("ingredients")
					.forGetter(recipe -> recipe.ingredients),
				ResourceKey.codec(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
					.fieldOf("modifier_key")
					.forGetter(recipe -> recipe.travellersModifierKey)
			).apply(instance, TravellersGearModifierShapelessRecipe::new)));
		}
	}

	public static final RecipeSerializer<TravellersGearModifierShapelessRecipe> SERIALIZER = new RecipeSerializer<>(new Serializer().codec(), new Serializer().streamCodec());
}
