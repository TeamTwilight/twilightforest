package twilightforest.item.recipe;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersComponentModifier;

public class TravellersGearModifierShapedRecipe extends TravellersGearModifierRecipe {
	protected final ShapedRecipePattern pattern;
	public TravellersGearModifierShapedRecipe(ShapedRecipePattern pattern, TravellersComponentModifier travellersModifier) {
		super(travellersModifier);
		this.pattern = pattern;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!super.matches(input, level))
			return false;
		return pattern.matches(input);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return pattern.height() <= height && pattern.width() <= width;
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
					.forGetter(recipe -> recipe.travellersModifier)
			).apply(instance, TravellersGearModifierShapedRecipe::new)));
		}
	}
}
