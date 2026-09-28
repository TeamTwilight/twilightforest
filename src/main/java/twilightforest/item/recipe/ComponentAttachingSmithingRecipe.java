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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.level.Level;
import tamaized.beanification.Autowired;
import twilightforest.util.ArmorUtil;

import java.util.List;
import java.util.Optional;

public class ComponentAttachingSmithingRecipe extends SimpleSmithingRecipe {

	private static final Codec<List<TypedDataComponent<?>>> DATA_COMPONENT_CODEC = DataComponentMap.CODEC.xmap(typedDataComponents -> typedDataComponents.stream().toList(), typedDataComponents -> {
		DataComponentMap.Builder builder = DataComponentMap.builder();

		for (TypedDataComponent<?> typedDataComponent : typedDataComponents)
			setComponent(typedDataComponent, builder);

		return builder.build();
	});

	public static final MapCodec<ComponentAttachingSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
		Ingredient.CODEC.optionalFieldOf("base").forGetter(o -> o.template),
		Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
		Ingredient.CODEC.optionalFieldOf("addition").forGetter(o -> o.addition),
		DATA_COMPONENT_CODEC.optionalFieldOf("additional_data", List.of()).forGetter(o -> o.additionalData)
	).apply(i, ComponentAttachingSmithingRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ComponentAttachingSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
		Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
		ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), o -> o.template,
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.base,
		ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), o -> o.addition,
		TypedDataComponent.STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.additionalData,
		ComponentAttachingSmithingRecipe::new
	);

	public static final RecipeSerializer<ComponentAttachingSmithingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	@Autowired
	private static ArmorUtil armorUtil;

	private final Optional<Ingredient> template;
	private final Ingredient base;
	private final Optional<Ingredient> addition;
	private final List<TypedDataComponent<?>> additionalData;

	public ComponentAttachingSmithingRecipe(Recipe.CommonInfo commonInfo, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, List<TypedDataComponent<?>> additionalData) {
		super(commonInfo);
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.additionalData = additionalData;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(SmithingRecipeInput input, Level level) {
		if (!Ingredient.testOptionalIngredient(this.templateIngredient(), input.template()) || !this.base.test(input.base()) || !Ingredient.testOptionalIngredient(this.additionIngredient(), input.addition())) return false;

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
		return this.template;
	}

	@Override
	public Ingredient baseIngredient() {
		return this.base;
	}

	@Override
	public Optional<Ingredient> additionIngredient() {
		return this.addition;
	}

	@Override
	public RecipeSerializer<ComponentAttachingSmithingRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	protected PlacementInfo createPlacementInfo() {
		return PlacementInfo.createFromOptionals(List.of(this.template, Optional.of(this.base), this.addition));
	}

	private void setComponents(ItemStack itemstack) {
		for (TypedDataComponent<?> data : this.additionalData)
			setComponent(data, itemstack);

		armorUtil.updateEmperorsClothEquippable(itemstack);
	}

	private static <T> void setComponent(TypedDataComponent<T> data, ItemStack stack) {
		stack.set(data.type(), data.value());
	}

	private static <T> void setComponent(TypedDataComponent<T> data, DataComponentMap.Builder builder) {
		builder.set(data.type(), data.value());
	}
}
