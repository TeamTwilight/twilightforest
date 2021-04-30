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
		consumer.accept(stonecutting(TFBlocks.castle_brick.get(), TFBlocks.castle_brick_frame.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_cracked.get(), TFBlocks.castle_brick_frame.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_worn.get(), TFBlocks.castle_brick_frame.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_mossy.get(), TFBlocks.castle_brick_frame.get()));

		consumer.accept(stonecutting(TFBlocks.castle_brick.get(), TFBlocks.castle_brick_worn.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_frame.get(), TFBlocks.castle_pillar_bold.get()));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_bold.get(), TFBlocks.castle_pillar_bold_tile.get()));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_encased.get(), TFBlocks.castle_pillar_encased_tile.get()));

		consumer.accept(stonecutting(TFBlocks.castle_pillar_encased.get(), TFBlocks.castle_stairs_encased.get()));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_bold.get(), TFBlocks.castle_stairs_bold.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick.get(), TFBlocks.castle_stairs_brick.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_worn.get(), TFBlocks.castle_stairs_worn.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_cracked.get(), TFBlocks.castle_stairs_cracked.get()));
		consumer.accept(stonecutting(TFBlocks.castle_brick_mossy.get(), TFBlocks.castle_stairs_mossy.get()));

		consumer.accept(stonecutting(TFBlocks.etched_nagastone.get(), TFBlocks.nagastone_stairs_left.get()));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone.get(), TFBlocks.nagastone_stairs_right.get()));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_mossy.get(), TFBlocks.nagastone_stairs_mossy_left.get()));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_mossy.get(), TFBlocks.nagastone_stairs_mossy_right.get()));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_weathered.get(), TFBlocks.nagastone_stairs_weathered_left.get()));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_weathered.get(), TFBlocks.nagastone_stairs_weathered_right.get()));
		
		consumer.accept(stonecutting(TFBlocks.dark_log.get(), TFBlocks.tower_wood.get()));
		consumer.accept(stonecutting(TFBlocks.dark_wood.get(), TFBlocks.tower_wood.get()));
		consumer.accept(stonecutting(TFBlocks.tower_wood.get(), TFBlocks.tower_wood_encased.get()));
		
		consumer.accept(stonecutting(TFBlocks.maze_stone.get(), TFBlocks.maze_stone_border.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone.get(), TFBlocks.maze_stone_brick.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone.get(), TFBlocks.maze_stone_chiseled.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone.get(), TFBlocks.maze_stone_decorative.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone.get(), TFBlocks.maze_stone_mosaic.get()));

		consumer.accept(stonecutting(TFBlocks.maze_stone_brick.get(), TFBlocks.maze_stone_border.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick.get(), TFBlocks.maze_stone_chiseled.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick.get(), TFBlocks.maze_stone_decorative.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick.get(), TFBlocks.maze_stone_mosaic.get()));

		consumer.accept(stonecutting(TFBlocks.maze_stone_border.get(), TFBlocks.maze_stone_brick.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border.get(), TFBlocks.maze_stone_chiseled.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border.get(), TFBlocks.maze_stone_decorative.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border.get(), TFBlocks.maze_stone_mosaic.get()));

		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled.get(), TFBlocks.maze_stone_border.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled.get(), TFBlocks.maze_stone_brick.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled.get(), TFBlocks.maze_stone_decorative.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled.get(), TFBlocks.maze_stone_mosaic.get()));

		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative.get(), TFBlocks.maze_stone_border.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative.get(), TFBlocks.maze_stone_chiseled.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative.get(), TFBlocks.maze_stone_brick.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative.get(), TFBlocks.maze_stone_mosaic.get()));

		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic.get(), TFBlocks.maze_stone_border.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic.get(), TFBlocks.maze_stone_chiseled.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic.get(), TFBlocks.maze_stone_decorative.get()));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic.get(), TFBlocks.maze_stone_brick.get()));
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
		String path = String.format("stonecutting/%s/%s", input.getRegistryName().getPath(), output.getRegistryName().getPath());
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
