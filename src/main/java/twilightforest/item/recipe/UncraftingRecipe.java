package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

public class UncraftingRecipe extends ShapedRecipe {
	public static final MapCodec<UncraftingRecipe> MAP_CODEC =
		RecordCodecBuilder.mapCodec(i -> i.group(
			Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
			Codec.INT.optionalFieldOf("cost", -1).forGetter(o -> o.cost),
			Ingredient.CODEC.fieldOf("input").forGetter(o -> o.input),
			Codec.INT.optionalFieldOf("input_count", 1).forGetter(o -> o.count),
			ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern)
		).apply(i, UncraftingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, UncraftingRecipe> STREAM_CODEC =
		StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
			ByteBufCodecs.VAR_INT, o -> o.cost,
			Ingredient.CONTENTS_STREAM_CODEC, o -> o.input,
			ByteBufCodecs.VAR_INT, o -> o.count,
			ShapedRecipePattern.STREAM_CODEC, o -> o.pattern,
			UncraftingRecipe::new
		);

	public static final RecipeSerializer<UncraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Recipe.CommonInfo commonInfo;
	private final int cost;
	private final Ingredient input;
	private final int count;
	private final ShapedRecipePattern pattern;

	public UncraftingRecipe(CommonInfo commonInfo, int cost, Ingredient input, int count, ShapedRecipePattern pattern) {
		super(commonInfo, new CraftingBookInfo(CraftingBookCategory.MISC, "uncrafting"), pattern, new ItemStackTemplate(Items.AIR, count));
		this.commonInfo = commonInfo;
		this.cost = cost;
		this.input = input;
		this.count = count;
		this.pattern = pattern;
	}

	@Override //This method is never used, but it has to be implemented
	public boolean matches(CraftingInput input, Level level) {
		return false;
	}

	@Override //We have to implement this method, can't really be used since we have multiple outputs
	public ItemStack assemble(CraftingInput input) {
		return ItemStack.EMPTY;
	}

	//Checks if the itemStack is a part of the ingredient when UncraftingMenu's getRecipesFor() method iterates through all recipes.
	public boolean isItemStackAnIngredient(ItemStack stack) {
		return this.input.test(stack) && stack.getCount() >= this.count;
	}

	// Let's not talk about this...
	@Override
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return (RecipeSerializer<ShapedRecipe>) (RecipeSerializer<?>) SERIALIZER;
	}

	// Or this...
	@Override
	public RecipeType<CraftingRecipe> getType() {
		return (RecipeType<CraftingRecipe>) (RecipeType<?>) TFRecipes.UNCRAFTING_RECIPE.get();
	}

	public Ingredient getInput() {
		return this.input;
	}

	public int getCount() {
		return this.count;
	}

	public int getCost() {
		return this.cost;
	}
}