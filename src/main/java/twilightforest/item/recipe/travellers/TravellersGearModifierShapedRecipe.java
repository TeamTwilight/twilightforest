package twilightforest.item.recipe.travellers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.*;
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
	public PlacementInfo placementInfo() {
		return PlacementInfo.createFromOptionals(pattern.ingredients());
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
}
