package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class NoTemplateSmithingRecipe extends SimpleSmithingRecipe {
	private static final Codec<List<TypedDataComponent<?>>> DATA_COMPONENT_CODEC = DataComponentMap.CODEC.xmap(typedDataComponents -> typedDataComponents.stream().toList(), typedDataComponents -> {
		DataComponentMap.Builder builder = DataComponentMap.builder();

		for (TypedDataComponent<?> typedDataComponent : typedDataComponents)
			setComponent(typedDataComponent, builder);

		return builder.build();
	});

	public static final MapCodec<NoTemplateSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
		Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
		Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition),
		DATA_COMPONENT_CODEC.optionalFieldOf("additional_data", List.of()).forGetter(o -> o.additionalData)
	).apply(i, NoTemplateSmithingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, NoTemplateSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
		Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.base,
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.addition,
		TypedDataComponent.STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.additionalData,
		NoTemplateSmithingRecipe::new
	);

	public static final RecipeSerializer<NoTemplateSmithingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient base;
	private final Ingredient addition;
	private final List<TypedDataComponent<?>> additionalData;

	public NoTemplateSmithingRecipe(Recipe.CommonInfo commonInfo, Ingredient base, Ingredient addition, List<TypedDataComponent<?>> additionalData) {
		super(commonInfo);
		this.base = base;
		this.addition = addition;
		this.additionalData = additionalData;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(SmithingRecipeInput input, Level level) {
		if (!Ingredient.testOptionalIngredient(this.templateIngredient(), input.template()) || !this.base.test(input.base()) || !this.addition.test(input.addition())) return false;

		for (TypedDataComponent<?> data : this.additionalData)
			if (input.base().has(data.type()))
				return false;

		return true;
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput input) {
		return Util.make(input.base().copy(), this::setComponents);
	}

	@Override
	public Optional<Ingredient> templateIngredient() {
		return Optional.empty();
	}

	@Override
	public Ingredient baseIngredient() {
		return this.base;
	}

	@Override
	public Optional<Ingredient> additionIngredient() {
		return Optional.of(this.addition);
	}

	@Override
	public RecipeSerializer<NoTemplateSmithingRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	protected PlacementInfo createPlacementInfo() {
		return PlacementInfo.create(
			List.of(this.base, this.addition)
		);
	}

	private void setComponents(ItemStack itemstack) {
		for (TypedDataComponent<?> data : this.additionalData)
			setComponent(data, itemstack);
	}

	private static <T> void setComponent(TypedDataComponent<T> data, ItemStack stack) {
		stack.set(data.type(), data.value());
	}

	private static <T> void setComponent(TypedDataComponent<T> data, DataComponentMap.Builder builder) {
		builder.set(data.type(), data.value());
	}
}
