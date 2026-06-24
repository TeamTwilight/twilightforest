package twilightforest.item.recipe.travellers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import twilightforest.TFRegistries;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearModifierShapelessRecipe extends TravellersGearModifierRecipe {
	protected final NonNullList<Ingredient> ingredients;

	public static final MapCodec<TravellersGearModifierShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		NonNullList.codecOf(Ingredient.CODEC)
			.fieldOf("ingredients")
			.forGetter(recipe -> recipe.ingredients),
		ResourceKey.codec(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
			.fieldOf("modifier_key")
			.forGetter(recipe -> recipe.travellersModifierKey)
	).apply(instance, TravellersGearModifierShapelessRecipe::new));

	public TravellersGearModifierShapelessRecipe(NonNullList<Ingredient> ingredients, ResourceKey<TravellersModifier> travellersModifier) {
		super(travellersModifier);
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!super.matches(input, level))
			return false;
		if (input.ingredientCount() != this.ingredients.size())
			return false;
		List<ItemStack> nonEmptyItems = new ArrayList<>(input.ingredientCount());
		for (ItemStack item : input.items()) {
			if (!item.isEmpty())
				nonEmptyItems.add(item);
		}
		RecipeMatcher.findMatches(nonEmptyItems, this.ingredients);
		return true;
	}

	@Override
	public int getWidth() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public int getHeight() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public boolean isShapeless() {
		return true;
	}

	@Override
	public RecipeSerializer<TravellersGearModifierShapelessRecipe> getSerializer() {
		return TFRecipes.MODIFIER_SHAPELESS_RECIPE_SERIALIZER.get();
	}

	public static StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierShapelessRecipe> streamCodec() {
		return StreamCodec.of(TravellersGearModifierShapelessRecipe::toNetwork, TravellersGearModifierShapelessRecipe::fromNetwork);
	}

	private static TravellersGearModifierShapelessRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
		RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
		JsonElement jsonelementDeserialized = GsonHelper.fromJson(new Gson(), buf.readUtf(), JsonElement.class);
		return CODEC.codec().decode(registryops, jsonelementDeserialized).getOrThrow().getFirst();
	}

	private static void toNetwork(RegistryFriendlyByteBuf buf, TravellersGearModifierShapelessRecipe recipe) {
		RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
		JsonElement jsonelement = CODEC.codec().encodeStart(registryops, recipe).getOrThrow();
		buf.writeUtf(jsonelement.toString());
	}
}
