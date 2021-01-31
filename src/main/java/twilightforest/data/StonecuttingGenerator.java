package twilightforest.data;

import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator extends RecipeProvider {
	public StonecuttingGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		consumer.accept(stonecutting(TFBlocks.castle_pillar_encased.get(), TFBlocks.castle_stairs_encased.get()));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_bold.get(), TFBlocks.castle_stairs_bold.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick.get(), TFBlocks.castle_stairs_brick.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_worn.get(), TFBlocks.castle_stairs_worn.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_cracked.get(), TFBlocks.castle_stairs_cracked.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_mossy.get(), TFBlocks.castle_stairs_mossy.get()));
	}

	@Override
	public String getName() {
		return "Twilight Forest stonecutting recipes";
	}

	private static Wrapper stonecutting(IItemProvider input, IItemProvider output) {
		return stonecutting(input, output, 1);
	}

	private static Wrapper stonecutting(IItemProvider input, IItemProvider output, int count) {
		return new Wrapper(getIdFor(input.asItem(), output.asItem()), Ingredient.fromItems(input), output.asItem(), count);
	}

	private static ResourceLocation getIdFor(Item input, Item output) {
		String path = String.format("stonecutting/%s_to_%s", input.getRegistryName().getPath(), output.getRegistryName().getPath());
		return prefix(path);
	}

	// Wrapper that allows you to not have an advancement
	public static class Wrapper extends SingleItemRecipeBuilder.Result {
		public Wrapper(ResourceLocation id, Ingredient input, Item output, int count) {
			super(id, IRecipeSerializer.STONECUTTING, "", input, output, count, null, null);
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
