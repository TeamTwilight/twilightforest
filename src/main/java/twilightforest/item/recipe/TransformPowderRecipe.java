package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFRecipes;

public record TransformPowderRecipe(EntityType<?> input, EntityType<?> result, boolean isReversible) implements Recipe<Container> {

	@Override
	public boolean matches(Container container, Level level) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.TRANSFORMATION_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.TRANSFORM_POWDER_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<TransformPowderRecipe> {

		private static final Codec<TransformPowderRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("from").forGetter(o -> o.input),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("to").forGetter(o -> o.result),
						Codec.BOOL.fieldOf("reversible").forGetter(o -> o.isReversible)
				).apply(instance, TransformPowderRecipe::new));

		@Override
		public Codec<TransformPowderRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public TransformPowderRecipe fromNetwork(FriendlyByteBuf buffer) {
			EntityType<?> input = buffer.readById(BuiltInRegistries.ENTITY_TYPE);
			EntityType<?> output = buffer.readById(BuiltInRegistries.ENTITY_TYPE);
			boolean reversible = buffer.readBoolean();
			return new TransformPowderRecipe(input, output, reversible);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TransformPowderRecipe recipe) {
			buffer.writeId(BuiltInRegistries.ENTITY_TYPE, recipe.input());
			buffer.writeId(BuiltInRegistries.ENTITY_TYPE, recipe.result());
			buffer.writeBoolean(recipe.isReversible());
		}
	}
}
