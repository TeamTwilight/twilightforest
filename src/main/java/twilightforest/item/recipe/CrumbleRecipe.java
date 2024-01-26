package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFRecipes;

public record CrumbleRecipe(Block input, Block result) implements Recipe<Container> {

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
		return TFRecipes.CRUMBLE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.CRUMBLE_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<CrumbleRecipe> {

		private static final Codec<CrumbleRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						BuiltInRegistries.BLOCK.byNameCodec().fieldOf("from").forGetter(o -> o.input),
						BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to").forGetter(o -> o.result)
				).apply(instance, CrumbleRecipe::new));

		@Override
		public Codec<CrumbleRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public CrumbleRecipe fromNetwork(FriendlyByteBuf buffer) {
			Block input = buffer.readById(BuiltInRegistries.BLOCK);
			Block output = buffer.readById(BuiltInRegistries.BLOCK);
			return new CrumbleRecipe(input, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CrumbleRecipe recipe) {
			buffer.writeId(BuiltInRegistries.BLOCK, recipe.input);
			buffer.writeId(BuiltInRegistries.BLOCK, recipe.result);
		}
	}
}
