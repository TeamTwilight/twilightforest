package twilightforest.datagen.data.recipes;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Collections;
import java.util.Optional;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator {

	protected static void buildRecipes(HolderGetter<Item> getter, RecipeOutput output) {
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(getter, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(getter, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(getter, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(getter, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(getter, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(getter, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(getter, output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);

		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(getter, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(getter, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(getter, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(getter, output, TFBlocks.ETCHED_NAGASTONE, TFBlocks.NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.ETCHED_NAGASTONE, TFBlocks.NAGASTONE_STAIRS_RIGHT);
		stonecutting(getter, output, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
		stonecutting(getter, output, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);

		stonecutting(getter, output, TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.NAGASTONE_STAIRS_RIGHT);
		stonecutting(getter, output, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
		stonecutting(getter, output, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
		stonecutting(getter, output, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);

		stonecutting(getter, output, TFBlocks.DARK_LOG, TFBlocks.TOWERWOOD);
		stonecutting(getter, output, TFBlocks.DARK_WOOD, TFBlocks.TOWERWOOD);
		stonecutting(getter, output, TFBlocks.DARK_LOG, TFBlocks.ENCASED_TOWERWOOD);
		stonecutting(getter, output, TFBlocks.DARK_WOOD, TFBlocks.ENCASED_TOWERWOOD);

		stonecutting(getter, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(getter, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(getter, output, TFBlocks.MAZESTONE, TFBlocks.CUT_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(getter, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.MAZESTONE_BORDER);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.CUT_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(getter, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.MAZESTONE_BRICK);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.CUT_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(getter, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(getter, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(getter, output, TFBlocks.CUT_MAZESTONE, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(getter, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(getter, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(getter, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.CUT_MAZESTONE);
		stonecutting(getter, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(getter, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(getter, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.MAZESTONE_BORDER);
		stonecutting(getter, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.CUT_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(getter, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.MAZESTONE_BRICK);

		stonecutting(getter, output, TFBlocks.TWILIGHT_OAK_LOG, TFItems.HOLLOW_TWILIGHT_OAK_LOG);
		stonecutting(getter, output, TFBlocks.CANOPY_LOG, TFItems.HOLLOW_CANOPY_LOG);
		stonecutting(getter, output, TFBlocks.MANGROVE_LOG, TFItems.HOLLOW_MANGROVE_LOG);
		stonecutting(getter, output, TFBlocks.DARK_LOG, TFItems.HOLLOW_DARK_LOG);
		stonecutting(getter, output, TFBlocks.TIME_LOG, TFItems.HOLLOW_TIME_LOG);
		stonecutting(getter, output, TFBlocks.TRANSFORMATION_LOG, TFItems.HOLLOW_TRANSFORMATION_LOG);
		stonecutting(getter, output, TFBlocks.MINING_LOG, TFItems.HOLLOW_MINING_LOG);
		stonecutting(getter, output, TFBlocks.SORTING_LOG, TFItems.HOLLOW_SORTING_LOG);

		stonecutting(getter, output, Blocks.OAK_LOG, TFItems.HOLLOW_OAK_LOG);
		stonecutting(getter, output, Blocks.SPRUCE_LOG, TFItems.HOLLOW_SPRUCE_LOG);
		stonecutting(getter, output, Blocks.BIRCH_LOG, TFItems.HOLLOW_BIRCH_LOG);
		stonecutting(getter, output, Blocks.JUNGLE_LOG, TFItems.HOLLOW_JUNGLE_LOG);
		stonecutting(getter, output, Blocks.ACACIA_LOG, TFItems.HOLLOW_ACACIA_LOG);
		stonecutting(getter, output, Blocks.DARK_OAK_LOG, TFItems.HOLLOW_DARK_OAK_LOG);
		stonecutting(getter, output, Blocks.CRIMSON_STEM, TFItems.HOLLOW_CRIMSON_STEM);
		stonecutting(getter, output, Blocks.WARPED_STEM, TFItems.HOLLOW_WARPED_STEM);
		stonecutting(getter, output, Blocks.MANGROVE_LOG, TFItems.HOLLOW_VANGROVE_LOG);
		stonecutting(getter, output, Blocks.CHERRY_LOG, TFItems.HOLLOW_CHERRY_LOG);
		stonecutting(getter, output, Blocks.PALE_OAK_LOG, TFItems.HOLLOW_PALE_OAK_LOG);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.TWISTED_STONE);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.BOLD_STONE_PILLAR);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.TWISTED_STONE_PILLAR);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.SPIRAL_BRICKS);
		stonecutting(getter, output, TFBlocks.TWISTED_STONE, TFBlocks.TWISTED_STONE_PILLAR);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(getter, output, Blocks.STONE, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_ARCS, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_ARCS, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_CURVES, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_CURVES, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_LINES, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(getter, output, TFBlocks.TERRORCOTTA_LINES, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(getter, output, TFBlocks.UNDERBRICK, TFBlocks.UNDERBRICK_FLOOR);
	}

	private static void stonecutting(HolderGetter<Item> getter, RecipeOutput recipe, ItemLike input, ItemLike output) {
		stonecutting(getter, recipe, input, output, 1);
	}

	private static void stonecutting(HolderGetter<Item> getter, RecipeOutput recipe, ItemLike input, ItemLike output, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output.asItem(), count).unlockedBy("has_block", has(getter, input)).save(recipe, getIdFor(input, output));
	}

	private static ResourceKey<Recipe<?>> getIdFor(ItemLike input, ItemLike output) {
		String path = String.format("stonecutting/%s/%s", BuiltInRegistries.ITEM.getKey(input.asItem()).getPath(), BuiltInRegistries.ITEM.getKey(output.asItem()).getPath());
		return ResourceKey.create(Registries.RECIPE, prefix(path));
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(HolderGetter<Item> getter, ItemLike item) {
		return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.item().of(getter, item).build())));
	}
}
