package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

public class DryingRecipe extends SingleItemRecipe {

	private final int dryingTime;

	public DryingRecipe(Ingredient ingredient, ItemStack result, int dryingTime) {
		super(TFRecipes.DRYING_RECIPE.get(), TFRecipes.DRYING_SERIALIZER.get(), "", ingredient, result);
		this.dryingTime = dryingTime;
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return this.ingredient.test(input.item());
	}

	public Ingredient getInput() {
		return this.ingredient;
	}

	public ItemStack getResult() {
		return this.result;
	}

	public int getDryingTime() {
		return this.dryingTime;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class Serializer implements RecipeSerializer<DryingRecipe> {

		public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(o -> o.ingredient),
			ItemStack.STRICT_CODEC.fieldOf("result").forGetter(o -> o.result),
			Codec.INT.fieldOf("filter_time").forGetter(o -> o.dryingTime)
		).apply(instance, DryingRecipe::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
			ItemStack.STREAM_CODEC, o -> o.result,
			ByteBufCodecs.INT, o -> o.dryingTime,
			DryingRecipe::new);

		@Override
		public MapCodec<DryingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
