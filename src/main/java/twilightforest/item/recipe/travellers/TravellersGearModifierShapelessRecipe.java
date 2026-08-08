package twilightforest.item.recipe.travellers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import twilightforest.TFRegistries;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearModifierShapelessRecipe extends TravellersGearModifierRecipe {
	public static final MapCodec<TravellersGearModifierShapelessRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			NonNullList.codecOf(Ingredient.CODEC)
				.fieldOf("ingredients")
				.forGetter(recipe -> recipe.ingredients),
			RegistryFixedCodec.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
				.fieldOf("modifier_key")
				.forGetter(TravellersGearModifierRecipe::getTravellersModifierHolder)
		).apply(instance, TravellersGearModifierShapelessRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierShapelessRecipe> STREAM_CODEC =
		StreamCodec.composite(
			ByteBufCodecs.collection(NonNullList::createWithCapacity, Ingredient.CONTENTS_STREAM_CODEC),
			recipe -> recipe.ingredients,

			ByteBufCodecs.holderRegistry(TFRegistries.Keys.TRAVELLERS_MODIFIERS),
			TravellersGearModifierRecipe::getTravellersModifierHolder,

			TravellersGearModifierShapelessRecipe::new
		);

	public static final RecipeSerializer<TravellersGearModifierShapelessRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	protected final NonNullList<Ingredient> ingredients;

	public TravellersGearModifierShapelessRecipe(NonNullList<Ingredient> ingredients, Holder<TravellersModifier> travellersModifierHolder) {
		super(travellersModifierHolder);
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
		return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.create(ingredients);
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
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
