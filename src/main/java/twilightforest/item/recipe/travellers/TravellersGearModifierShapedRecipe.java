package twilightforest.item.recipe.travellers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

public class TravellersGearModifierShapedRecipe extends TravellersGearModifierRecipe {
	public static final MapCodec<TravellersGearModifierShapedRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			ShapedRecipePattern.MAP_CODEC
				.fieldOf("pattern")
				.forGetter(recipe -> recipe.pattern),
			RegistryFixedCodec.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
				.fieldOf("modifier_key")
				.forGetter(TravellersGearModifierRecipe::getTravellersModifierHolder),
			Codec.BOOL
				.fieldOf("is_rotated")
				.forGetter(recipe -> recipe.isRotated)
		).apply(instance, TravellersGearModifierShapedRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, TravellersGearModifierShapedRecipe> STREAM_CODEC =
		StreamCodec.composite(
			ShapedRecipePattern.STREAM_CODEC,
			recipe -> recipe.pattern,

			ByteBufCodecs.holderRegistry(TFRegistries.Keys.TRAVELLERS_MODIFIERS),
			TravellersGearModifierRecipe::getTravellersModifierHolder,

			ByteBufCodecs.BOOL,
			recipe -> recipe.isRotated,

			TravellersGearModifierShapedRecipe::new
		);

	public static final RecipeSerializer<TravellersGearModifierShapedRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	protected final ShapedRecipePattern pattern;
	protected final boolean isRotated;

	public TravellersGearModifierShapedRecipe(ShapedRecipePattern pattern, Holder<TravellersModifier> travellersModifierHolder, boolean isRotated) {
		super(travellersModifierHolder);
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
	public PlacementInfo placementInfo() {
		return PlacementInfo.createFromOptionals(pattern.ingredients());
	}

	@Override
	public Identifier getId() {
		return super.getId().withSuffix(isRotated ? "_rotated" : "");
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
