package twilightforest.datagen.data.tags;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFItemTags;

public abstract class BlockItemTagGenerator {

	@SuppressWarnings("unchecked")
	protected void run() {

		//------------
		//VANILLA TAGS
		//------------

		this.tag(BlockTags.PLANKS, ItemTags.PLANKS).add(
				TFBlocks.TWILIGHT_OAK_PLANKS.key(),
				TFBlocks.CANOPY_PLANKS.key(),
				TFBlocks.MANGROVE_PLANKS.key(),
				TFBlocks.DARK_PLANKS.key(),
				TFBlocks.TIME_PLANKS.key(),
				TFBlocks.TRANSFORMATION_PLANKS.key(),
				TFBlocks.MINING_PLANKS.key(),
				TFBlocks.SORTING_PLANKS.key())
			.addTag(TFBlockTags.TOWERWOOD);

		this.tag(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS).add(
			TFBlocks.TWILIGHT_OAK_BUTTON.key(),
			TFBlocks.CANOPY_BUTTON.key(),
			TFBlocks.MANGROVE_BUTTON.key(),
			TFBlocks.DARK_BUTTON.key(),
			TFBlocks.TIME_BUTTON.key(),
			TFBlocks.TRANSFORMATION_BUTTON.key(),
			TFBlocks.MINING_BUTTON.key(),
			TFBlocks.SORTING_BUTTON.key());

		this.tag(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS).add(TFBlocks.CORONATION_CARPET.key());
		this.tag(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS).add(
			TFBlocks.TWILIGHT_OAK_DOOR.key(),
			TFBlocks.CANOPY_DOOR.key(),
			TFBlocks.MANGROVE_DOOR.key(),
			TFBlocks.DARK_DOOR.key(),
			TFBlocks.TIME_DOOR.key(),
			TFBlocks.TRANSFORMATION_DOOR.key(),
			TFBlocks.MINING_DOOR.key(),
			TFBlocks.SORTING_DOOR.key());

		this.tag(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS).add(
			TFBlocks.TWILIGHT_OAK_STAIRS.key(),
			TFBlocks.CANOPY_STAIRS.key(),
			TFBlocks.MANGROVE_STAIRS.key(),
			TFBlocks.DARK_STAIRS.key(),
			TFBlocks.TIME_STAIRS.key(),
			TFBlocks.TRANSFORMATION_STAIRS.key(),
			TFBlocks.MINING_STAIRS.key(),
			TFBlocks.SORTING_STAIRS.key());

		this.tag(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS).add(
			TFBlocks.TWILIGHT_OAK_SLAB.key(),
			TFBlocks.CANOPY_SLAB.key(),
			TFBlocks.MANGROVE_SLAB.key(),
			TFBlocks.DARK_SLAB.key(),
			TFBlocks.TIME_SLAB.key(),
			TFBlocks.TRANSFORMATION_SLAB.key(),
			TFBlocks.MINING_SLAB.key(),
			TFBlocks.SORTING_SLAB.key());

		this.tag(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES).add(
			TFBlocks.TWILIGHT_OAK_FENCE.key(),
			TFBlocks.CANOPY_FENCE.key(),
			TFBlocks.MANGROVE_FENCE.key(),
			TFBlocks.DARK_FENCE.key(),
			TFBlocks.TIME_FENCE.key(),
			TFBlocks.TRANSFORMATION_FENCE.key(),
			TFBlocks.MINING_FENCE.key(),
			TFBlocks.SORTING_FENCE.key());

		this.tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES).add(
			TFBlocks.TWILIGHT_OAK_GATE.key(),
			TFBlocks.CANOPY_GATE.key(),
			TFBlocks.MANGROVE_GATE.key(),
			TFBlocks.DARK_GATE.key(),
			TFBlocks.TIME_GATE.key(),
			TFBlocks.TRANSFORMATION_GATE.key(),
			TFBlocks.MINING_GATE.key(),
			TFBlocks.SORTING_GATE.key());

		this.tag(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES).add(
			TFBlocks.TWILIGHT_OAK_PLATE.key(),
			TFBlocks.CANOPY_PLATE.key(),
			TFBlocks.MANGROVE_PLATE.key(),
			TFBlocks.DARK_PLATE.key(),
			TFBlocks.TIME_PLATE.key(),
			TFBlocks.TRANSFORMATION_PLATE.key(),
			TFBlocks.MINING_PLATE.key(),
			TFBlocks.SORTING_PLATE.key());

		this.tag(BlockItemTags.SAPLINGS.block(), BlockItemTags.SAPLINGS.item()).add(
			TFBlocks.TWILIGHT_OAK_SAPLING.key(),
			TFBlocks.CANOPY_SAPLING.key(),
			TFBlocks.MANGROVE_SAPLING.key(),
			TFBlocks.DARKWOOD_SAPLING.key(),
			TFBlocks.TIME_SAPLING.key(),
			TFBlocks.TRANSFORMATION_SAPLING.key(),
			TFBlocks.MINING_SAPLING.key(),
			TFBlocks.SORTING_SAPLING.key(),
			TFBlocks.HOLLOW_OAK_SAPLING.key(),
			TFBlocks.RAINBOW_OAK_SAPLING.key());


		this.tag(BlockItemTags.LOGS_THAT_BURN.block(), BlockItemTags.LOGS_THAT_BURN.item()).addTags(
			TFBlockTags.TWILIGHT_OAK_LOGS,
			TFBlockTags.CANOPY_LOGS,
			TFBlockTags.MANGROVE_LOGS,
			TFBlockTags.DARKWOOD_LOGS,
			TFBlockTags.TIME_LOGS,
			TFBlockTags.TRANSFORMATION_LOGS,
			TFBlockTags.MINING_LOGS,
			TFBlockTags.SORTING_LOGS);

		this.tag(BlockItemTags.SLABS.block(), BlockItemTags.SLABS.item()).add(TFBlocks.AURORA_SLAB.key());
		this.tag(BlockTags.WALLS, ItemTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.key());
		this.tag(BlockItemTags.STAIRS.block(), BlockItemTags.STAIRS.item()).add(
			TFBlocks.CASTLE_BRICK_STAIRS.key(),
			TFBlocks.WORN_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.BOLD_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.key());

		this.tag(BlockTags.LEAVES, ItemTags.LEAVES).add(
			TFBlocks.RAINBOW_OAK_LEAVES.key(),
			TFBlocks.TWILIGHT_OAK_LEAVES.key(),
			TFBlocks.CANOPY_LEAVES.key(),
			TFBlocks.MANGROVE_LEAVES.key(),
			TFBlocks.DARK_LEAVES.key(),
			TFBlocks.TIME_LEAVES.key(),
			TFBlocks.TRANSFORMATION_LEAVES.key(),
			TFBlocks.MINING_LEAVES.key(),
			TFBlocks.SORTING_LEAVES.key(),
			TFBlocks.THORN_LEAVES.key(),
			TFBlocks.BEANSTALK_LEAVES.key());

		this.tag(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS).add(
			TFBlocks.TWILIGHT_OAK_TRAPDOOR.key(),
			TFBlocks.CANOPY_TRAPDOOR.key(),
			TFBlocks.MANGROVE_TRAPDOOR.key(),
			TFBlocks.DARK_TRAPDOOR.key(),
			TFBlocks.TIME_TRAPDOOR.key(),
			TFBlocks.TRANSFORMATION_TRAPDOOR.key(),
			TFBlocks.MINING_TRAPDOOR.key(),
			TFBlocks.SORTING_TRAPDOOR.key());

		this.tag(BlockItemTags.SMALL_FLOWERS.block(), BlockItemTags.SMALL_FLOWERS.item()).add(TFBlocks.THORN_ROSE.key());
		this.tag(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS).addTag(TFBlockTags.CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.key());
		this.tag(BlockTags.DIRT, ItemTags.DIRT).add(TFBlocks.UBEROUS_SOIL.key());
		this.tag(BlockTags.STANDING_SIGNS, ItemTags.SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.key(),
			TFBlocks.CANOPY_SIGN.key(),
			TFBlocks.MANGROVE_SIGN.key(),
			TFBlocks.DARK_SIGN.key(),
			TFBlocks.TIME_SIGN.key(),
			TFBlocks.TRANSFORMATION_SIGN.key(),
			TFBlocks.MINING_SIGN.key(),
			TFBlocks.SORTING_SIGN.key());

		this.tag(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.key(),
			TFBlocks.CANOPY_HANGING_SIGN.key(),
			TFBlocks.MANGROVE_HANGING_SIGN.key(),
			TFBlocks.DARK_HANGING_SIGN.key(),
			TFBlocks.TIME_HANGING_SIGN.key(),
			TFBlocks.TRANSFORMATION_HANGING_SIGN.key(),
			TFBlocks.MINING_HANGING_SIGN.key(),
			TFBlocks.SORTING_HANGING_SIGN.key());

		//------------
		// COMMON TAGS
		//------------

		this.tag(Tags.BlockItems.FENCE_GATES_WOODEN.block(), Tags.BlockItems.FENCE_GATES_WOODEN.item()).add(
			TFBlocks.TWILIGHT_OAK_GATE.key(),
			TFBlocks.CANOPY_GATE.key(),
			TFBlocks.MANGROVE_GATE.key(),
			TFBlocks.DARK_GATE.key(),
			TFBlocks.TIME_GATE.key(),
			TFBlocks.TRANSFORMATION_GATE.key(),
			TFBlocks.MINING_GATE.key(),
			TFBlocks.SORTING_GATE.key());

		this.tag(Tags.BlockItems.CHESTS_WOODEN.block(), Tags.BlockItems.CHESTS_WOODEN.item()).add(
			TFBlocks.TWILIGHT_OAK_CHEST.key(),
				TFBlocks.CANOPY_CHEST.key(),
				TFBlocks.MANGROVE_CHEST.key(),
				TFBlocks.DARK_CHEST.key(),
				TFBlocks.TIME_CHEST.key(),
				TFBlocks.TRANSFORMATION_CHEST.key(),
				TFBlocks.MINING_CHEST.key(),
				TFBlocks.SORTING_CHEST.key())
			.add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.key(),
				TFBlocks.CANOPY_TRAPPED_CHEST.key(),
				TFBlocks.MANGROVE_TRAPPED_CHEST.key(),
				TFBlocks.DARK_TRAPPED_CHEST.key(),
				TFBlocks.TIME_TRAPPED_CHEST.key(),
				TFBlocks.TRANSFORMATION_TRAPPED_CHEST.key(),
				TFBlocks.MINING_TRAPPED_CHEST.key(),
				TFBlocks.SORTING_TRAPPED_CHEST.key());

		this.tag(Tags.BlockItems.CHESTS_TRAPPED.block(), Tags.BlockItems.CHESTS_TRAPPED.item()).add(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.key(),
			TFBlocks.CANOPY_TRAPPED_CHEST.key(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.key(),
			TFBlocks.DARK_TRAPPED_CHEST.key(),
			TFBlocks.TIME_TRAPPED_CHEST.key(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.key(),
			TFBlocks.MINING_TRAPPED_CHEST.key(),
			TFBlocks.SORTING_TRAPPED_CHEST.key());

		this.tag(Tags.BlockItems.STORAGE_BLOCKS.block(), Tags.BlockItems.STORAGE_BLOCKS.item()).addTags(
			TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR,
			TFBlockTags.STORAGE_BLOCKS_CARMINITE,
			TFBlockTags.STORAGE_BLOCKS_FIERY,
			TFBlockTags.STORAGE_BLOCKS_IRONWOOD,
			TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL,
			TFBlockTags.STORAGE_BLOCKS_STEELEAF,
			TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME);

		this.tag(Tags.BlockItems.STORAGE_BLOCKS_SLIME.block(), Tags.BlockItems.STORAGE_BLOCKS_SLIME.item()).add(TFBlocks.MAZE_SLIME_BLOCK.key());
		this.tag(Tags.BlockItems.NATURAL_LOGS.block(), Tags.BlockItems.NATURAL_LOGS.item()).add(
			TFBlocks.TWILIGHT_OAK_LOG.key(),
			TFBlocks.CANOPY_LOG.key(),
			TFBlocks.MANGROVE_LOG.key(),
			TFBlocks.DARK_LOG.key(),
			TFBlocks.TIME_LOG.key(),
			TFBlocks.TRANSFORMATION_LOG.key(),
			TFBlocks.MINING_LOG.key(),
			TFBlocks.SORTING_LOG.key());

		this.tag(Tags.BlockItems.NATURAL_WOODS.block(), Tags.BlockItems.NATURAL_WOODS.item()).add(
			TFBlocks.TWILIGHT_OAK_WOOD.key(),
			TFBlocks.CANOPY_WOOD.key(),
			TFBlocks.MANGROVE_WOOD.key(),
			TFBlocks.DARK_WOOD.key(),
			TFBlocks.TIME_WOOD.key(),
			TFBlocks.TRANSFORMATION_WOOD.key(),
			TFBlocks.MINING_WOOD.key(),
			TFBlocks.SORTING_WOOD.key());

		this.tag(Tags.BlockItems.STRIPPED_LOGS.block(), Tags.BlockItems.STRIPPED_LOGS.item()).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.key(),
			TFBlocks.STRIPPED_CANOPY_LOG.key(),
			TFBlocks.STRIPPED_MANGROVE_LOG.key(),
			TFBlocks.STRIPPED_DARK_LOG.key(),
			TFBlocks.STRIPPED_TIME_LOG.key(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.key(),
			TFBlocks.STRIPPED_MINING_LOG.key(),
			TFBlocks.STRIPPED_SORTING_LOG.key());

		this.tag(Tags.BlockItems.STRIPPED_WOODS.block(), Tags.BlockItems.STRIPPED_WOODS.item()).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.key(),
			TFBlocks.STRIPPED_CANOPY_WOOD.key(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.key(),
			TFBlocks.STRIPPED_DARK_WOOD.key(),
			TFBlocks.STRIPPED_TIME_WOOD.key(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.key(),
			TFBlocks.STRIPPED_MINING_WOOD.key(),
			TFBlocks.STRIPPED_SORTING_WOOD.key());

		this.tag(Tags.BlockItems.BOOKSHELVES.block(), Tags.BlockItems.BOOKSHELVES.item()).add(TFBlocks.CANOPY_BOOKSHELF.key());
		this.tag(Tags.BlockItems.GLASS_BLOCKS.block(), Tags.BlockItems.GLASS_BLOCKS.item()).add(TFBlocks.AURORALIZED_GLASS.key());
		this.tag(Tags.BlockItems.PLAYER_WORKSTATIONS_CRAFTING_TABLES.block(), Tags.BlockItems.PLAYER_WORKSTATIONS_CRAFTING_TABLES.item()).add(TFBlocks.UNCRAFTING_TABLE.key());
		this.tag(Tags.BlockItems.ROPES.block(), Tags.BlockItems.ROPES.item()).add(TFBlocks.ROPE.key());

		//-----------
		//  TF TAGS
		//-----------

		this.tag(TFBlockTags.TWILIGHT_OAK_LOGS, TFItemTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.key(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.key(), TFBlocks.TWILIGHT_OAK_WOOD.key(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.key());
		this.tag(TFBlockTags.CANOPY_LOGS, TFItemTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.key(), TFBlocks.STRIPPED_CANOPY_LOG.key(), TFBlocks.CANOPY_WOOD.key(), TFBlocks.STRIPPED_CANOPY_WOOD.key());
		this.tag(TFBlockTags.MANGROVE_LOGS, TFItemTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.key(), TFBlocks.STRIPPED_MANGROVE_LOG.key(), TFBlocks.MANGROVE_WOOD.key(), TFBlocks.STRIPPED_MANGROVE_WOOD.key());
		this.tag(TFBlockTags.DARKWOOD_LOGS, TFItemTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.key(), TFBlocks.STRIPPED_DARK_LOG.key(), TFBlocks.DARK_WOOD.key(), TFBlocks.STRIPPED_DARK_WOOD.key());
		this.tag(TFBlockTags.TIME_LOGS, TFItemTags.TIME_LOGS).add(TFBlocks.TIME_LOG.key(), TFBlocks.STRIPPED_TIME_LOG.key(), TFBlocks.TIME_WOOD.key(), TFBlocks.STRIPPED_TIME_WOOD.key());
		this.tag(TFBlockTags.TRANSFORMATION_LOGS, TFItemTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.key(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.key(), TFBlocks.TRANSFORMATION_WOOD.key(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.key());
		this.tag(TFBlockTags.MINING_LOGS, TFItemTags.MINING_LOGS).add(TFBlocks.MINING_LOG.key(), TFBlocks.STRIPPED_MINING_LOG.key(), TFBlocks.MINING_WOOD.key(), TFBlocks.STRIPPED_MINING_WOOD.key());
		this.tag(TFBlockTags.SORTING_LOGS, TFItemTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.key(), TFBlocks.STRIPPED_SORTING_LOG.key(), TFBlocks.SORTING_WOOD.key(), TFBlocks.STRIPPED_SORTING_WOOD.key());

		this.tag(TFBlockTags.BANISTERS, TFItemTags.BANISTERS).add(
			TFBlocks.OAK_BANISTER.key(),
			TFBlocks.SPRUCE_BANISTER.key(),
			TFBlocks.BIRCH_BANISTER.key(),
			TFBlocks.JUNGLE_BANISTER.key(),
			TFBlocks.ACACIA_BANISTER.key(),
			TFBlocks.DARK_OAK_BANISTER.key(),
			TFBlocks.CRIMSON_BANISTER.key(),
			TFBlocks.WARPED_BANISTER.key(),
			TFBlocks.VANGROVE_BANISTER.key(),
			TFBlocks.BAMBOO_BANISTER.key(),
			TFBlocks.CHERRY_BANISTER.key(),
			TFBlocks.PALE_OAK_BANISTER.key(),

			TFBlocks.TWILIGHT_OAK_BANISTER.key(),
			TFBlocks.CANOPY_BANISTER.key(),
			TFBlocks.MANGROVE_BANISTER.key(),
			TFBlocks.DARK_BANISTER.key(),
			TFBlocks.TIME_BANISTER.key(),
			TFBlocks.TRANSFORMATION_BANISTER.key(),
			TFBlocks.MINING_BANISTER.key(),
			TFBlocks.SORTING_BANISTER.key()
		);

		this.tag(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR, TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.key());
		this.tag(TFBlockTags.STORAGE_BLOCKS_CARMINITE, TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.key());
		this.tag(TFBlockTags.STORAGE_BLOCKS_FIERY, TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.key());
		this.tag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.key());
		this.tag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.key());
		this.tag(TFBlockTags.STORAGE_BLOCKS_STEELEAF, TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.key());

		this.tag(TFBlockTags.DRYING_RACKS, TFItemTags.DRYING_RACKS).add(
			TFBlocks.OAK_DRYING_RACK.key(), TFBlocks.SPRUCE_DRYING_RACK.key(),
			TFBlocks.BIRCH_DRYING_RACK.key(), TFBlocks.JUNGLE_DRYING_RACK.key(),
			TFBlocks.ACACIA_DRYING_RACK.key(), TFBlocks.DARK_OAK_DRYING_RACK.key(),
			TFBlocks.CRIMSON_DRYING_RACK.key(), TFBlocks.WARPED_DRYING_RACK.key(),
			TFBlocks.VANGROVE_DRYING_RACK.key(), TFBlocks.BAMBOO_DRYING_RACK.key(),
			TFBlocks.CHERRY_DRYING_RACK.key(), TFBlocks.PALE_OAK_DRYING_RACK.key(),
			TFBlocks.TWILIGHT_OAK_DRYING_RACK.key(), TFBlocks.CANOPY_DRYING_RACK.key(),
			TFBlocks.MANGROVE_DRYING_RACK.key(), TFBlocks.DARK_DRYING_RACK.key(),
			TFBlocks.TIME_DRYING_RACK.key(), TFBlocks.TRANSFORMATION_DRYING_RACK.key(),
			TFBlocks.MINING_DRYING_RACK.key(), TFBlocks.SORTING_DRYING_RACK.key()
		);

		this.tag(TFBlockTags.TOWERWOOD, TFItemTags.TOWERWOOD).add(
			TFBlocks.TOWERWOOD.key(),
			TFBlocks.MOSSY_TOWERWOOD.key(),
			TFBlocks.CRACKED_TOWERWOOD.key(),
			TFBlocks.INFESTED_TOWERWOOD.key());

		this.tag(TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME, TFItemTags.STORAGE_BLOCKS_MAZE_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.key());

		this.tag(TFBlockTags.CLOUDS, TFItemTags.CLOUDS).add(
			TFBlocks.FLUFFY_CLOUD.key(),
			TFBlocks.WISPY_CLOUD.key(),
			TFBlocks.RAINY_CLOUD.key(),
			TFBlocks.SNOWY_CLOUD.key());
	}

	protected abstract TagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag);
}
