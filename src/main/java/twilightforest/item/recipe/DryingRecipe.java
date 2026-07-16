package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

public class DryingRecipe extends SingleItemRecipe {
	public static final MapCodec<DryingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
		Ingredient.CODEC.fieldOf("input").forGetter(SingleItemRecipe::input),
		ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result()),
		Codec.INT.fieldOf("filter_time").forGetter(o -> o.dryingTime)
	).apply(i, DryingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.composite(
		CommonInfo.STREAM_CODEC, o -> o.commonInfo,
		Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input,
		ItemStackTemplate.STREAM_CODEC, o -> o.result(),
		ByteBufCodecs.INT, o -> o.dryingTime,
		DryingRecipe::new);

	public static final RecipeSerializer<DryingRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);


	private final int dryingTime;

	public DryingRecipe(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result, int dryingTime) {
		super(commonInfo, input, result);
		this.dryingTime = dryingTime;
	}

	@Override
	public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<? extends SingleItemRecipe> getType() {
		return TFRecipes.DRYING_RECIPE.get();
	}

	// Consider adding a category in the future
	@Override
	public RecipeBookCategory recipeBookCategory() {
		return null;
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return this.input().test(input.item());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public String group() {
		return "";
	}

	public Ingredient getInput() {
		return this.input();
	}

	public ItemStackTemplate getResult() {
		return this.result();
	}

	public int getDryingTime() {
		return this.dryingTime;
	}
}
