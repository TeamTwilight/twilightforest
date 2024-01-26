package twilightforest.data;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Collections;
import java.util.Optional;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator {

	protected static void buildRecipes(RecipeOutput output) {
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.CASTLE_BRICK.get(), TFBlocks.CASTLE_ROOF_TILE.get());

		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());

		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());

		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());

		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());

		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.CASTLE_ROOF_TILE.get());

		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.CASTLE_ROOF_TILE.get());

		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());

		stonecutting(output, TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get());
		stonecutting(output, TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get());
		stonecutting(output, TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());

		stonecutting(output, TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get());
		stonecutting(output, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get());
		stonecutting(output, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get());
		stonecutting(output, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());

		stonecutting(output, TFBlocks.DARK_LOG.get(), TFBlocks.TOWERWOOD.get());
		stonecutting(output, TFBlocks.DARK_WOOD.get(), TFBlocks.TOWERWOOD.get());
		stonecutting(output, TFBlocks.DARK_LOG.get(), TFBlocks.ENCASED_TOWERWOOD.get());
		stonecutting(output, TFBlocks.DARK_WOOD.get(), TFBlocks.ENCASED_TOWERWOOD.get());

		stonecutting(output, TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get());
		stonecutting(output, TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get());
		stonecutting(output, TFBlocks.MAZESTONE.get(), TFBlocks.CUT_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get());

		stonecutting(output, TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.MAZESTONE_BORDER.get());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.CUT_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.DECORATIVE_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.MAZESTONE_MOSAIC.get());

		stonecutting(output, TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.MAZESTONE_BRICK.get());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.CUT_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.DECORATIVE_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.MAZESTONE_MOSAIC.get());

		stonecutting(output, TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get());

		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.CUT_MAZESTONE.get());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get());

		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BORDER.get());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.CUT_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.DECORATIVE_MAZESTONE.get());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BRICK.get());

		stonecutting(output, TFBlocks.TWILIGHT_OAK_LOG.get(), TFItems.HOLLOW_TWILIGHT_OAK_LOG.get());
		stonecutting(output, TFBlocks.CANOPY_LOG.get(), TFItems.HOLLOW_CANOPY_LOG.get());
		stonecutting(output, TFBlocks.MANGROVE_LOG.get(), TFItems.HOLLOW_MANGROVE_LOG.get());
		stonecutting(output, TFBlocks.DARK_LOG.get(), TFItems.HOLLOW_DARK_LOG.get());
		stonecutting(output, TFBlocks.TIME_LOG.get(), TFItems.HOLLOW_TIME_LOG.get());
		stonecutting(output, TFBlocks.TRANSFORMATION_LOG.get(), TFItems.HOLLOW_TRANSFORMATION_LOG.get());
		stonecutting(output, TFBlocks.MINING_LOG.get(), TFItems.HOLLOW_MINING_LOG.get());
		stonecutting(output, TFBlocks.SORTING_LOG.get(), TFItems.HOLLOW_SORTING_LOG.get());

		stonecutting(output, Blocks.OAK_LOG, TFItems.HOLLOW_OAK_LOG.get());
		stonecutting(output, Blocks.SPRUCE_LOG, TFItems.HOLLOW_SPRUCE_LOG.get());
		stonecutting(output, Blocks.BIRCH_LOG, TFItems.HOLLOW_BIRCH_LOG.get());
		stonecutting(output, Blocks.JUNGLE_LOG, TFItems.HOLLOW_JUNGLE_LOG.get());
		stonecutting(output, Blocks.ACACIA_LOG, TFItems.HOLLOW_ACACIA_LOG.get());
		stonecutting(output, Blocks.DARK_OAK_LOG, TFItems.HOLLOW_DARK_OAK_LOG.get());
		stonecutting(output, Blocks.CRIMSON_STEM, TFItems.HOLLOW_CRIMSON_STEM.get());
		stonecutting(output, Blocks.WARPED_STEM, TFItems.HOLLOW_WARPED_STEM.get());
		stonecutting(output, Blocks.MANGROVE_LOG, TFItems.HOLLOW_VANGROVE_LOG.get());
		stonecutting(output, Blocks.CHERRY_LOG, TFItems.HOLLOW_CHERRY_LOG.get());
		stonecutting(output, Blocks.STONE, TFBlocks.TWISTED_STONE.get());
		stonecutting(output, Blocks.STONE, TFBlocks.BOLD_STONE_PILLAR.get());
		stonecutting(output, Blocks.STONE, TFBlocks.TWISTED_STONE_PILLAR.get());
		stonecutting(output, Blocks.STONE, TFBlocks.SPIRAL_BRICKS.get());
		stonecutting(output, TFBlocks.TWISTED_STONE.get(), TFBlocks.TWISTED_STONE_PILLAR.get());

		stonecutting(output, TFBlocks.UNDERBRICK.get(), TFBlocks.UNDERBRICK_FLOOR.get());
	}

	private static void stonecutting(RecipeOutput recipe, ItemLike input, ItemLike output) {
		stonecutting(recipe, input, output, 1);
	}

	private static void stonecutting(RecipeOutput recipe, ItemLike input, ItemLike output, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output.asItem(), count).unlockedBy("has_block", has(input)).save(recipe, getIdFor(input, output));
	}

	private static ResourceLocation getIdFor(ItemLike input, ItemLike output) {
		String path = String.format("stonecutting/%s/%s", BuiltInRegistries.ITEM.getKey(input.asItem()).getPath(), BuiltInRegistries.ITEM.getKey(output.asItem()).getPath());
		return prefix(path);
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
		return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.item().of(item).build())));
	}
}
