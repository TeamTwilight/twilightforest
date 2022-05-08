package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class TransformPowderRecipe implements Recipe<Container> {

	private final ResourceLocation recipeID;
	private final EntityType<?> input;
	private final EntityType<?> result;

	public TransformPowderRecipe(ResourceLocation recipeID, EntityType<?> input, EntityType<?> result) {
		this.recipeID = recipeID;
		this.input = input;
		this.result = result;
	}

	public EntityType<?> getInput() {
		return input;
	}

	public EntityType<?> getResult() {
		return result;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeID;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.TRANSFORMATION_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.TRANSFORM_POWDER_RECIPE.get();
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TransformPowderRecipe> {

		@Override
		public TransformPowderRecipe fromJson(ResourceLocation id, JsonObject object) {
			EntityType<?> input = ForgeRegistries.ENTITIES.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(object, "from")));
			EntityType<?> output = ForgeRegistries.ENTITIES.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(object, "to")));
			if(input != null && output != null) {
				return new TransformPowderRecipe(id, input, output);
			}
			return new TransformPowderRecipe(id, EntityType.PIG, EntityType.ZOMBIFIED_PIGLIN);
		}

		@Nullable
		@Override
		public TransformPowderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			EntityType<?> input = buffer.readRegistryIdUnsafe(ForgeRegistries.ENTITIES);
			EntityType<?> output = buffer.readRegistryIdUnsafe(ForgeRegistries.ENTITIES);
			return new TransformPowderRecipe(id, input, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TransformPowderRecipe recipe) {
			buffer.writeRegistryIdUnsafe(ForgeRegistries.ENTITIES, recipe.input);
			buffer.writeRegistryIdUnsafe(ForgeRegistries.ENTITIES, recipe.result);
		}
	}
}
