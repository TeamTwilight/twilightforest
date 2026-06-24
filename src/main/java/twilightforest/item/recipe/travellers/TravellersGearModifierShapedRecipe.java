package twilightforest.item.recipe.travellers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

public class TravellersGearModifierShapedRecipe extends TravellersGearModifierRecipe {
	protected final ShapedRecipePattern pattern;
	protected final boolean isRotated;

	public static final MapCodec<TravellersGearModifierShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ShapedRecipePattern.MAP_CODEC
			.fieldOf("pattern")
			.forGetter(recipe -> recipe.pattern),
		ResourceKey.codec(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
			.fieldOf("modifier_key")
			.forGetter(recipe -> recipe.travellersModifierKey),
		Codec.BOOL
			.fieldOf("is_rotated")
			.forGetter(recipe -> recipe.isRotated)
	).apply(instance, TravellersGearModifierShapedRecipe::new));

	public TravellersGearModifierShapedRecipe(ShapedRecipePattern pattern, ResourceKey<TravellersModifier> travellersModifier, boolean isRotated) {
		super(travellersModifier);
		this.pattern = pattern;
		this.isRotated = isRotated;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!super.matches(input, level))
			return false;
		return pattern.matches(input);
	}

	@Override
	public int getWidth() {
		return pattern.width();
	}

	@Override
	public int getHeight() {
		return pattern.height();
	}

	@Override
	public boolean isShapeless() {
		return false;
	}

	@Override
	public Identifier getId() {
		return super.getId().withSuffix(isRotated ? "_rotated" : "");
	}

	@Override
	public RecipeSerializer<TravellersGearModifierShapedRecipe> getSerializer() {
		return TFRecipes.MODIFIER_SHAPED_RECIPE_SERIALIZER.get();
	}

	public static StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierShapedRecipe> streamCodec() {
		return StreamCodec.of(TravellersGearModifierShapedRecipe::toNetwork, TravellersGearModifierShapedRecipe::fromNetwork);
	}

	private static TravellersGearModifierShapedRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
		RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
		JsonElement jsonelementDeserialized = GsonHelper.fromJson(new Gson(), buf.readUtf(), JsonElement.class);
		return CODEC.codec().decode(registryops, jsonelementDeserialized).getOrThrow().getFirst();
	}

	private static void toNetwork(RegistryFriendlyByteBuf buf, TravellersGearModifierShapedRecipe recipe) {
		RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
		JsonElement jsonelement = CODEC.codec().encodeStart(registryops, recipe).getOrThrow();
		buf.writeUtf(jsonelement.toString());
	}
}
