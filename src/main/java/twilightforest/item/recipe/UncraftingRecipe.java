package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;
import twilightforest.init.TFRecipes;

import java.util.Arrays;

public record UncraftingRecipe(int cost, Ingredient input, int count,
							   ShapedRecipePattern pattern) implements CraftingRecipe, IShapedRecipe<CraftingContainer> {

	@Override //This method is never used, but it has to be implemented
	public boolean matches(CraftingContainer container, Level level) {
		return false;
	}

	@Override //We have to implement this method, can't really be used since we have multiple outputs
	public ItemStack assemble(CraftingContainer container, HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override //We have to implement this method, returns the count just in case
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return new ItemStack(Items.AIR, this.count);
	}

	@Override //Could probably be set to return true, since the recipe serializer doesn't let a bigger number through.
	public boolean canCraftInDimensions(int width, int height) {
		return (width >= this.pattern().width() && height >= this.pattern().height());
	}

	//Checks if the itemStack is a part of the ingredient when UncraftingMenu's getRecipesFor() method iterates through all recipes.
	public boolean isItemStackAnIngredient(ItemStack stack) {
		return Arrays.stream(this.input().getItems()).anyMatch(i -> (stack.getItem() == i.getItem() && stack.getCount() >= this.count()));
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.UNCRAFTING_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.UNCRAFTING_RECIPE.get();
	}

	@Override
	public CraftingBookCategory category() {
		return CraftingBookCategory.MISC;
	}

	@Override
	public int getWidth() {
		return this.pattern().width();
	}

	@Override
	public int getHeight() {
		return this.pattern().height();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.pattern().ingredients();
	}

	public static class Serializer implements RecipeSerializer<UncraftingRecipe> {

		public static final MapCodec<UncraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf("cost", -1).forGetter(UncraftingRecipe::cost),
				Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(UncraftingRecipe::input),
				Codec.INT.optionalFieldOf("input_count", 1).forGetter(UncraftingRecipe::count),
				ShapedRecipePattern.MAP_CODEC.forGetter(UncraftingRecipe::pattern)
			).apply(instance, UncraftingRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, UncraftingRecipe> STREAM_CODEC = StreamCodec.of(UncraftingRecipe.Serializer::toNetwork, UncraftingRecipe.Serializer::fromNetwork);

		@Override
		public MapCodec<UncraftingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, UncraftingRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		public static UncraftingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			int cost = buf.readInt();
			Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
			int count = buf.readInt();
			ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
			return new UncraftingRecipe(cost, input, count, pattern);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buf, UncraftingRecipe recipe) {
			buf.writeInt(recipe.cost());
			Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input());
			buf.writeInt(recipe.count());
			ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern());
		}
	}
}