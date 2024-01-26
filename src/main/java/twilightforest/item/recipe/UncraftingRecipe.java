package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
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
	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override //We have to implement this method, returns the count just in case
	public ItemStack getResultItem(RegistryAccess access) {
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
	public int getRecipeWidth() {
		return this.pattern().width();
	}

	@Override
	public int getRecipeHeight() {
		return this.pattern().height();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.pattern().ingredients();
	}

	public static class Serializer implements RecipeSerializer<UncraftingRecipe> {

		public static final Codec<UncraftingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codec.INT.optionalFieldOf("cost", -1).forGetter(UncraftingRecipe::cost),
						Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(UncraftingRecipe::input),
						Codec.INT.optionalFieldOf("input_count", 1).forGetter(UncraftingRecipe::count),
						ShapedRecipePattern.MAP_CODEC.forGetter(UncraftingRecipe::pattern)
				).apply(instance, UncraftingRecipe::new)
		);

		@Override
		public Codec<UncraftingRecipe> codec() {
			return CODEC;
		}

		public UncraftingRecipe fromNetwork(FriendlyByteBuf buf) {
			int cost = buf.readInt();
			Ingredient input = Ingredient.fromNetwork(buf);
			int count = buf.readInt();
			ShapedRecipePattern pattern = ShapedRecipePattern.fromNetwork(buf);
			return new UncraftingRecipe(cost, input, count, pattern);
		}

		public void toNetwork(FriendlyByteBuf buf, UncraftingRecipe recipe) {
			buf.writeInt(recipe.cost());
			recipe.input().toNetwork(buf);
			buf.writeInt(recipe.count());
			recipe.pattern().toNetwork(buf);
		}
	}
}