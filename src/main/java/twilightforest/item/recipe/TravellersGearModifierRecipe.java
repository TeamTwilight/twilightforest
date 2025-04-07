package twilightforest.item.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.TravellersGearComponentModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

import java.util.Optional;
import java.util.stream.Stream;

public class TravellersGearModifierRecipe extends CustomRecipe {
	protected final Either<ShapedRecipePattern, NonNullList<Ingredient>> pattern;
	protected final TravellersGearComponentModifier travellersModifier;
	public TravellersGearModifierRecipe(Either<ShapedRecipePattern, NonNullList<Ingredient>> pattern, TravellersGearComponentModifier travellersModifier) {
		super(CraftingBookCategory.EQUIPMENT);
		this.pattern = pattern;
		this.travellersModifier = travellersModifier;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		for (ItemStack stack : input.items()) {
			if (TravellersModifiers.countModifiers(stack) >= 3)
				return false;
		}
		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		Optional<ItemStack> travellerArmorStack = getTravellersArmor(input).findFirst();
		if (travellerArmorStack.isEmpty()) {
			TwilightForestMod.LOGGER.error("No traveller's gear item found for {}. Please report this to https://github.com/TeamTwilight/twilightforest/issues", input);
			return ItemStack.EMPTY;
		}
		ItemStack stack = travellerArmorStack.get().copy();
		travellersModifier.addModifier(stack);
		return stack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return pattern.map(
			shapedRecipePattern -> width >= shapedRecipePattern.width() && height >= shapedRecipePattern.height(),
			ingredients -> ingredients.size() <= width * height
		);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.MODIFIER_SERIALIZER.get();
	}

	protected static Stream<ItemStack> getTravellersArmor(CraftingInput input) {
		return input.items().stream().filter(stack -> stack.getItem() instanceof TravellersArmorItem);
	}

	public static class Serializer implements RecipeSerializer<TravellersGearModifierRecipe> {
		private static final Codec<ShapedRecipePattern> SHAPED_RECIPE_PATTERN_CODEC = ShapedRecipePattern.MAP_CODEC.codec();
		private static final Codec<NonNullList<Ingredient>> NON_NULL_LIST_INGREDIENT_CODEC = NonNullList.codecOf(Ingredient.CODEC);

		public static final MapCodec<TravellersGearModifierRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.either(SHAPED_RECIPE_PATTERN_CODEC, NON_NULL_LIST_INGREDIENT_CODEC).fieldOf("pattern").forGetter(recipe -> recipe.pattern),
				TravellersGearComponentModifier.MAP_CODEC.fieldOf("modifier").forGetter(recipe -> recipe.travellersModifier)
			).apply(instance, TravellersGearModifierRecipe::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<TravellersGearModifierRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		public static TravellersGearModifierRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
			return friendlyByteBuf.readJsonWithCodec(CODEC.codec());
		}

		public static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TravellersGearModifierRecipe recipe) {
			friendlyByteBuf.writeJsonWithCodec(CODEC.codec(), recipe);
		}
	}
}
