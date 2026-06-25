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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

import java.util.List;
import java.util.Optional;

public class NoTemplateSmithingRecipe implements SmithingRecipe {

	private final Ingredient base;
	private final Ingredient addition;
	private final List<TypedDataComponent<?>> additionalData;

	public NoTemplateSmithingRecipe(Ingredient base, Ingredient addition, List<TypedDataComponent<?>> additionalData) {
		this.base = base;
		this.addition = addition;
		this.additionalData = additionalData;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(SmithingRecipeInput input, Level level) {
		if (!input.getItem(0).isEmpty() || !this.base.test(input.getItem(1)) || !this.addition.test(input.getItem(2))) return false;
		ItemStack armor = input.getItem(1);

		for (TypedDataComponent<?> data : this.additionalData)
			if (armor.has(data.type()))
				return false;

		return true;
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput input) {
		return Util.make(input.getItem(1).copy(), this::setComponents);
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
	public boolean showNotification() {
		return true;
	}

	@Override
	public String group() {
		return "";
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.createFromOptionals(List.of(Optional.empty(), Optional.of(this.base), Optional.of(this.addition)));
	}

	@Override
	public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
		return TFRecipes.NO_TEMPLATE_SMITHING_SERIALIZER.get();
	}

	public Ingredient getBase() {
		return this.base;
	}

	public Ingredient getAddition() {
		return this.addition;
	}

	private List<TypedDataComponent<?>> additionalData() {
		return this.additionalData;
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

	private static final Codec<List<TypedDataComponent<?>>> DATA_COMPONENT_CODEC = DataComponentMap.CODEC.xmap(typedDataComponents -> typedDataComponents.stream().toList(), typedDataComponents -> {
		DataComponentMap.Builder builder = DataComponentMap.builder();

		for (TypedDataComponent<?> typedDataComponent : typedDataComponents)
			setComponent(typedDataComponent, builder);

		return builder.build();
	});

	public static final MapCodec<NoTemplateSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Ingredient.CODEC.fieldOf("base").forGetter(NoTemplateSmithingRecipe::getBase),
			Ingredient.CODEC.fieldOf("addition").forGetter(NoTemplateSmithingRecipe::getAddition),
			DATA_COMPONENT_CODEC.optionalFieldOf("additional_data", List.of()).forGetter(NoTemplateSmithingRecipe::additionalData)
		).apply(instance, NoTemplateSmithingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, NoTemplateSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
		Ingredient.CONTENTS_STREAM_CODEC, NoTemplateSmithingRecipe::getBase,
		Ingredient.CONTENTS_STREAM_CODEC, NoTemplateSmithingRecipe::getAddition,
		TypedDataComponent.STREAM_CODEC.apply(ByteBufCodecs.list()), NoTemplateSmithingRecipe::additionalData,
		NoTemplateSmithingRecipe::new
	);

	public static final RecipeSerializer<NoTemplateSmithingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
}
