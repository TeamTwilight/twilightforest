package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

public class DryingRecipe extends SingleItemRecipe {
	private final int dryingTime;
	private final Ingredient ingredient;

	public DryingRecipe(Ingredient ingredient, ItemStack result, int dryingTime) {
		super(new CommonInfo(false), ingredient, new ItemStackTemplate(result.getItem()));
		this.dryingTime = dryingTime;
		this.ingredient = ingredient;
	}

	public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Ingredient.CODEC.fieldOf("input").forGetter(o -> o.ingredient), ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result().create()), Codec.INT.fieldOf("filter_time").forGetter(o -> o.dryingTime)).apply(instance, DryingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient, ItemStack.STREAM_CODEC, o -> o.result().create(), ByteBufCodecs.INT, o -> o.dryingTime, DryingRecipe::new);

	@Override
	public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
		return TFRecipes.DRYING_SERIALIZER.get();
	}

	@Override
	public RecipeType<? extends SingleItemRecipe> getType() {
		return TFRecipes.DRYING_RECIPE.get();
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return new RecipeBookCategory();
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return this.ingredient.test(input.item());
	}

	public Ingredient getInput() {
		return this.ingredient;
	}

	public ItemStackTemplate getResult() {
		return this.result();
	}

	public int getDryingTime() {
		return this.dryingTime;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public String group() {
		return "";
	}
}
