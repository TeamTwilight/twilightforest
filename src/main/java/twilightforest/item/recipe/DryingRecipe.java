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

public class DryingRecipe implements Recipe<RecipeInput> {

private final Ingredient ingredient;
private final ItemStackTemplate result;
private final int dryingTime;

public DryingRecipe(Ingredient ingredient, ItemStackTemplate result, int dryingTime) {
this.ingredient = ingredient;
this.result = result;
this.dryingTime = dryingTime;
}

@Override
public boolean matches(RecipeInput input, Level level) {
return this.ingredient.test(input.getItem(0));
}

@Override
public ItemStack assemble(RecipeInput input) {
return this.result.create().copy();
}

@Override
public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
return TFRecipes.DRYING_SERIALIZER.get();
}

@Override
public RecipeType<? extends Recipe<RecipeInput>> getType() {
return TFRecipes.DRYING_RECIPE.get();
}

@Override
public boolean showNotification() {
return false;
}

@Override
public String group() {
return "";
}

@Override
public PlacementInfo placementInfo() {
return PlacementInfo.create(this.ingredient);
}

@Override
public RecipeBookCategory recipeBookCategory() {
return RecipeBookCategories.FURNACE_MISC;
}

public Ingredient getInput() {
return this.ingredient;
}

public ItemStack getResult() {
return this.result.create();
}

public int getDryingTime() {
return this.dryingTime;
}

public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
Ingredient.CODEC.fieldOf("input").forGetter(o -> o.ingredient),
ItemStackTemplate.MAP_CODEC.fieldOf("result").forGetter(o -> o.result),
Codec.INT.fieldOf("filter_time").forGetter(o -> o.dryingTime)
).apply(instance, DryingRecipe::new));

public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.composite(
	Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
	ItemStackTemplate.STREAM_CODEC, o -> o.result,
	ByteBufCodecs.INT, o -> o.dryingTime,
	DryingRecipe::new
);

public static final RecipeSerializer<DryingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
}
