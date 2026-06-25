package twilightforest.item.recipe.travellers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

public class TravellersGearModifierShapedRecipe extends TravellersGearModifierRecipe {
	protected final ShapedRecipePattern pattern;
	protected final boolean isRotated;

	public TravellersGearModifierShapedRecipe(ShapedRecipePattern pattern, ResourceKey<TravellersModifier> travellersModifier, boolean isRotated) {
		super(travellersModifier);
		this.pattern = pattern;
		this.isRotated = isRotated;
	}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		if (!super.matches(input, level))
			return false;
		return pattern.matches(input);
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
		NonNullList<Ingredient> list = NonNullList.create();
		for (var opt : pattern.ingredients()) {
			list.add(opt.orElse(Ingredient.of()));
		}
		return list;
	}

	@Override
	public Identifier getId() {
		return super.getId().withSuffix(isRotated ? "_rotated" : "");
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.MODIFIER_SHAPED_RECIPE_SERIALIZER.get();
	}

	public static class Serializer extends AbstractModifierRecipeSerializer<TravellersGearModifierShapedRecipe> {
		public Serializer() {
			super(RecordCodecBuilder.mapCodec(instance -> instance.group(
				ShapedRecipePattern.MAP_CODEC
					.fieldOf("pattern")
					.forGetter(recipe -> recipe.pattern),
				ResourceKey.codec(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
					.fieldOf("modifier_key")
					.forGetter(recipe -> recipe.travellersModifierKey),
				Codec.BOOL
					.fieldOf("is_rotated")
					.forGetter(recipe -> recipe.isRotated)
			).apply(instance, TravellersGearModifierShapedRecipe::new)));
		}
	}

	public static final RecipeSerializer<TravellersGearModifierShapedRecipe> SERIALIZER = new RecipeSerializer<>(new Serializer().codec(), new Serializer().streamCodec());
}
