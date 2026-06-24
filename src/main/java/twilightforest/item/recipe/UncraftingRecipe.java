package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

import java.util.Arrays;

public class UncraftingRecipe extends ShapedRecipe {
	private final int cost;
	private final Ingredient input;
	private final int count;
	private final ShapedRecipePattern pattern;

	public static final MapCodec<UncraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.optionalFieldOf("cost", -1).forGetter(o -> o.cost),
			Ingredient.CODEC.fieldOf("input").forGetter(o -> o.input),
			Codec.INT.optionalFieldOf("input_count", 1).forGetter(o -> o.count),
			ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern)
		).apply(instance, UncraftingRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, UncraftingRecipe> STREAM_CODEC = StreamCodec.of(UncraftingRecipe::toNetwork, UncraftingRecipe::fromNetwork);

	public UncraftingRecipe(int cost, Ingredient input, int count, ShapedRecipePattern pattern) {
		super(new CommonInfo(false), new CraftingBookInfo(CraftingBookCategory.MISC, "uncrafting"), pattern, new ItemStackTemplate(Items.AIR, count));
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
		return Arrays.stream(this.input.items().toArray()).anyMatch(i -> (stack.getItem() == ((ItemStack) i).getItem() && stack.getCount() >= this.count));
	}

	@Override
	public RecipeType<CraftingRecipe> getType() {
		return TFRecipes.UNCRAFTING_RECIPE.get();
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

	public static StreamCodec<RegistryFriendlyByteBuf, UncraftingRecipe> streamCodec() {
		return STREAM_CODEC;
	}

	private static UncraftingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
		int cost = buf.readInt();
		Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
		int count = buf.readInt();
		ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
		return new UncraftingRecipe(cost, input, count, pattern);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buf, UncraftingRecipe recipe) {
		buf.writeInt(recipe.cost);
		Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
		buf.writeInt(recipe.count);
		ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
	}
}