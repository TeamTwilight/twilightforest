package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersComponentModifier;

public class TravellersGearModifierShapedRecipe extends TravellersGearModifierRecipe {
	protected final ShapedRecipePattern pattern;
	protected final boolean isRotated;

	public TravellersGearModifierShapedRecipe(ShapedRecipePattern pattern, TravellersComponentModifier travellersModifier, boolean isRotated) {
		super(travellersModifier);
		this.pattern = pattern;
		this.isRotated = isRotated;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!super.matches(input, level))
			return false;
		return pattern.matches(input);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return getHeight() <= height && getWidth() <= width;
	}

	@Override
	public int getWidth() {
		return pattern.width();
	}

	@Override
	public int getHeight() {
		return pattern.height();
	}

	@Override
	public boolean isShapeless() {
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return pattern.ingredients();
	}

	@Override
	public ResourceLocation getId() {
		return super.getId().withSuffix(isRotated ? "_rotated" : "");
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.MODIFIER_SHAPED_RECIPE_SERIALIZER.get();
	}

	public static class Serializer extends AbstractModifierRecipeSerializer<TravellersGearModifierShapedRecipe> {
		public Serializer() {
			super(RecordCodecBuilder.mapCodec(instance -> instance.group(
				ShapedRecipePattern.MAP_CODEC
					.fieldOf("pattern")
					.forGetter(recipe -> recipe.pattern),
				TravellersComponentModifier.MAP_CODEC
					.fieldOf("modifier")
					.forGetter(recipe -> recipe.travellersModifier),
				Codec.BOOL
					.fieldOf("is_rotated")
					.forGetter(recipe -> recipe.isRotated)
			).apply(instance, TravellersGearModifierShapedRecipe::new)));
		}
	}
}
