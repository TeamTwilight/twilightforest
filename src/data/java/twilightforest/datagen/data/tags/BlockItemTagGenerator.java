package twilightforest.datagen.data.tags;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFItemTags;

// FIXME: Address removed tags
public abstract class BlockItemTagGenerator {

	@SuppressWarnings("unchecked")
	protected void run() {

		//------------
		//VANILLA TAGS
		//------------

		this.tag(BlockTags.PLANKS, ItemTags.PLANKS).add(
				TFBlocks.TWILIGHT_OAK_PLANKS.getKey(),
				TFBlocks.CANOPY_PLANKS.getKey(),
				TFBlocks.MANGROVE_PLANKS.getKey(),
				TFBlocks.DARK_PLANKS.getKey(),
				TFBlocks.TIME_PLANKS.getKey(),
				TFBlocks.TRANSFORMATION_PLANKS.getKey(),
				TFBlocks.MINING_PLANKS.getKey(),
				TFBlocks.SORTING_PLANKS.getKey())
			.addTag(TFBlockTags.TOWERWOOD);

		this.tag(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS).add(
			TFBlocks.TWILIGHT_OAK_BUTTON.getKey(),
			TFBlocks.CANOPY_BUTTON.getKey(),
			TFBlocks.MANGROVE_BUTTON.getKey(),
			TFBlocks.DARK_BUTTON.getKey(),
			TFBlocks.TIME_BUTTON.getKey(),
			TFBlocks.TRANSFORMATION_BUTTON.getKey(),
			TFBlocks.MINING_BUTTON.getKey(),
			TFBlocks.SORTING_BUTTON.getKey());

		this.tag(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS).add(TFBlocks.CORONATION_CARPET.getKey());
		this.tag(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS).add(
			TFBlocks.TWILIGHT_OAK_DOOR.getKey(),
			TFBlocks.CANOPY_DOOR.getKey(),
			TFBlocks.MANGROVE_DOOR.getKey(),
			TFBlocks.DARK_DOOR.getKey(),
			TFBlocks.TIME_DOOR.getKey(),
			TFBlocks.TRANSFORMATION_DOOR.getKey(),
			TFBlocks.MINING_DOOR.getKey(),
			TFBlocks.SORTING_DOOR.getKey());

		this.tag(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS).add(
			TFBlocks.TWILIGHT_OAK_STAIRS.getKey(),
			TFBlocks.CANOPY_STAIRS.getKey(),
			TFBlocks.MANGROVE_STAIRS.getKey(),
			TFBlocks.DARK_STAIRS.getKey(),
			TFBlocks.TIME_STAIRS.getKey(),
			TFBlocks.TRANSFORMATION_STAIRS.getKey(),
			TFBlocks.MINING_STAIRS.getKey(),
			TFBlocks.SORTING_STAIRS.getKey());

		this.tag(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS).add(
			TFBlocks.TWILIGHT_OAK_SLAB.getKey(),
			TFBlocks.CANOPY_SLAB.getKey(),
			TFBlocks.MANGROVE_SLAB.getKey(),
			TFBlocks.DARK_SLAB.getKey(),
			TFBlocks.TIME_SLAB.getKey(),
			TFBlocks.TRANSFORMATION_SLAB.getKey(),
			TFBlocks.MINING_SLAB.getKey(),
			TFBlocks.SORTING_SLAB.getKey());

		this.tag(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES).add(
			TFBlocks.TWILIGHT_OAK_FENCE.getKey(),
			TFBlocks.CANOPY_FENCE.getKey(),
			TFBlocks.MANGROVE_FENCE.getKey(),
			TFBlocks.DARK_FENCE.getKey(),
			TFBlocks.TIME_FENCE.getKey(),
			TFBlocks.TRANSFORMATION_FENCE.getKey(),
			TFBlocks.MINING_FENCE.getKey(),
			TFBlocks.SORTING_FENCE.getKey());

		this.tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES).add(
			TFBlocks.TWILIGHT_OAK_GATE.getKey(),
			TFBlocks.CANOPY_GATE.getKey(),
			TFBlocks.MANGROVE_GATE.getKey(),
			TFBlocks.DARK_GATE.getKey(),
			TFBlocks.TIME_GATE.getKey(),
			TFBlocks.TRANSFORMATION_GATE.getKey(),
			TFBlocks.MINING_GATE.getKey(),
			TFBlocks.SORTING_GATE.getKey());

		this.tag(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES).add(
			TFBlocks.TWILIGHT_OAK_PLATE.getKey(),
			TFBlocks.CANOPY_PLATE.getKey(),
			TFBlocks.MANGROVE_PLATE.getKey(),
			TFBlocks.DARK_PLATE.getKey(),
			TFBlocks.TIME_PLATE.getKey(),
			TFBlocks.TRANSFORMATION_PLATE.getKey(),
			TFBlocks.MINING_PLATE.getKey(),
			TFBlocks.SORTING_PLATE.getKey());

		this.tag(BlockTags.SAPLINGS, ItemTags.SAPLINGS).add(
			TFBlocks.TWILIGHT_OAK_SAPLING.getKey(),
			TFBlocks.CANOPY_SAPLING.getKey(),
			TFBlocks.MANGROVE_SAPLING.getKey(),
			TFBlocks.DARKWOOD_SAPLING.getKey(),
			TFBlocks.TIME_SAPLING.getKey(),
			TFBlocks.TRANSFORMATION_SAPLING.getKey(),
			TFBlocks.MINING_SAPLING.getKey(),
			TFBlocks.SORTING_SAPLING.getKey(),
			TFBlocks.HOLLOW_OAK_SAPLING.getKey(),
			TFBlocks.RAINBOW_OAK_SAPLING.getKey());


		this.tag(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN).addTags(
			TFBlockTags.TWILIGHT_OAK_LOGS,
			TFBlockTags.CANOPY_LOGS,
			TFBlockTags.MANGROVE_LOGS,
			TFBlockTags.DARKWOOD_LOGS,
			TFBlockTags.TIME_LOGS,
			TFBlockTags.TRANSFORMATION_LOGS,
			TFBlockTags.MINING_LOGS,
			TFBlockTags.SORTING_LOGS);

		this.tag(BlockTags.SLABS, ItemTags.SLABS).add(TFBlocks.AURORA_SLAB.getKey());
		this.tag(BlockTags.WALLS, ItemTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.getKey());
		this.tag(BlockTags.STAIRS, ItemTags.STAIRS).add(
			TFBlocks.CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.WORN_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.BOLD_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey());

		this.tag(BlockTags.LEAVES, ItemTags.LEAVES).add(
			TFBlocks.RAINBOW_OAK_LEAVES.getKey(),
			TFBlocks.TWILIGHT_OAK_LEAVES.getKey(),
			TFBlocks.CANOPY_LEAVES.getKey(),
			TFBlocks.MANGROVE_LEAVES.getKey(),
			TFBlocks.DARK_LEAVES.getKey(),
			TFBlocks.TIME_LEAVES.getKey(),
			TFBlocks.TRANSFORMATION_LEAVES.getKey(),
			TFBlocks.MINING_LEAVES.getKey(),
			TFBlocks.SORTING_LEAVES.getKey(),
			TFBlocks.THORN_LEAVES.getKey(),
			TFBlocks.BEANSTALK_LEAVES.getKey());

		this.tag(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS).add(
			TFBlocks.TWILIGHT_OAK_TRAPDOOR.getKey(),
			TFBlocks.CANOPY_TRAPDOOR.getKey(),
			TFBlocks.MANGROVE_TRAPDOOR.getKey(),
			TFBlocks.DARK_TRAPDOOR.getKey(),
			TFBlocks.TIME_TRAPDOOR.getKey(),
			TFBlocks.TRANSFORMATION_TRAPDOOR.getKey(),
			TFBlocks.MINING_TRAPDOOR.getKey(),
			TFBlocks.SORTING_TRAPDOOR.getKey());

		this.tag(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.getKey());
		this.tag(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS).addTag(TFBlockTags.CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());
		this.tag(BlockTags.DIRT, ItemTags.DIRT).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(BlockTags.STANDING_SIGNS, ItemTags.SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.getKey(),
			TFBlocks.CANOPY_SIGN.getKey(),
			TFBlocks.MANGROVE_SIGN.getKey(),
			TFBlocks.DARK_SIGN.getKey(),
			TFBlocks.TIME_SIGN.getKey(),
			TFBlocks.TRANSFORMATION_SIGN.getKey(),
			TFBlocks.MINING_SIGN.getKey(),
			TFBlocks.SORTING_SIGN.getKey());

		this.tag(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.getKey(),
			TFBlocks.CANOPY_HANGING_SIGN.getKey(),
			TFBlocks.MANGROVE_HANGING_SIGN.getKey(),
			TFBlocks.DARK_HANGING_SIGN.getKey(),
			TFBlocks.TIME_HANGING_SIGN.getKey(),
			TFBlocks.TRANSFORMATION_HANGING_SIGN.getKey(),
			TFBlocks.MINING_HANGING_SIGN.getKey(),
			TFBlocks.SORTING_HANGING_SIGN.getKey());

		//------------
		// COMMON TAGS
		//------------

		this.tag(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN).add(
			TFBlocks.TWILIGHT_OAK_GATE.getKey(),
			TFBlocks.CANOPY_GATE.getKey(),
			TFBlocks.MANGROVE_GATE.getKey(),
			TFBlocks.DARK_GATE.getKey(),
			TFBlocks.TIME_GATE.getKey(),
			TFBlocks.TRANSFORMATION_GATE.getKey(),
			TFBlocks.MINING_GATE.getKey(),
			TFBlocks.SORTING_GATE.getKey());

		this.tag(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN).add(
			TFBlocks.TWILIGHT_OAK_CHEST.getKey(),
				TFBlocks.CANOPY_CHEST.getKey(),
				TFBlocks.MANGROVE_CHEST.getKey(),
				TFBlocks.DARK_CHEST.getKey(),
				TFBlocks.TIME_CHEST.getKey(),
				TFBlocks.TRANSFORMATION_CHEST.getKey(),
				TFBlocks.MINING_CHEST.getKey(),
				TFBlocks.SORTING_CHEST.getKey())
			.add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getKey(),
				TFBlocks.CANOPY_TRAPPED_CHEST.getKey(),
				TFBlocks.MANGROVE_TRAPPED_CHEST.getKey(),
				TFBlocks.DARK_TRAPPED_CHEST.getKey(),
				TFBlocks.TIME_TRAPPED_CHEST.getKey(),
				TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getKey(),
				TFBlocks.MINING_TRAPPED_CHEST.getKey(),
				TFBlocks.SORTING_TRAPPED_CHEST.getKey());

		this.tag(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED).add(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getKey(),
			TFBlocks.CANOPY_TRAPPED_CHEST.getKey(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.getKey(),
			TFBlocks.DARK_TRAPPED_CHEST.getKey(),
			TFBlocks.TIME_TRAPPED_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getKey(),
			TFBlocks.MINING_TRAPPED_CHEST.getKey(),
			TFBlocks.SORTING_TRAPPED_CHEST.getKey());

		this.tag(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS).addTags(
			TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR,
			TFBlockTags.STORAGE_BLOCKS_CARMINITE,
			TFBlockTags.STORAGE_BLOCKS_FIERY,
			TFBlockTags.STORAGE_BLOCKS_IRONWOOD,
			TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL,
			TFBlockTags.STORAGE_BLOCKS_STEELEAF,
			TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME);

		this.tag(Tags.Blocks.STORAGE_BLOCKS_SLIME, Tags.Items.STORAGE_BLOCKS_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.getKey());
		this.tag(Tags.Blocks.NATURAL_LOGS, Tags.Items.NATURAL_LOGS).add(
			TFBlocks.TWILIGHT_OAK_LOG.getKey(),
			TFBlocks.CANOPY_LOG.getKey(),
			TFBlocks.MANGROVE_LOG.getKey(),
			TFBlocks.DARK_LOG.getKey(),
			TFBlocks.TIME_LOG.getKey(),
			TFBlocks.TRANSFORMATION_LOG.getKey(),
			TFBlocks.MINING_LOG.getKey(),
			TFBlocks.SORTING_LOG.getKey());

		this.tag(Tags.Blocks.NATURAL_WOODS, Tags.Items.NATURAL_WOODS).add(
			TFBlocks.TWILIGHT_OAK_WOOD.getKey(),
			TFBlocks.CANOPY_WOOD.getKey(),
			TFBlocks.MANGROVE_WOOD.getKey(),
			TFBlocks.DARK_WOOD.getKey(),
			TFBlocks.TIME_WOOD.getKey(),
			TFBlocks.TRANSFORMATION_WOOD.getKey(),
			TFBlocks.MINING_WOOD.getKey(),
			TFBlocks.SORTING_WOOD.getKey());

		this.tag(Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.getKey(),
			TFBlocks.STRIPPED_CANOPY_LOG.getKey(),
			TFBlocks.STRIPPED_MANGROVE_LOG.getKey(),
			TFBlocks.STRIPPED_DARK_LOG.getKey(),
			TFBlocks.STRIPPED_TIME_LOG.getKey(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.getKey(),
			TFBlocks.STRIPPED_MINING_LOG.getKey(),
			TFBlocks.STRIPPED_SORTING_LOG.getKey());

		this.tag(Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.getKey(),
			TFBlocks.STRIPPED_CANOPY_WOOD.getKey(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.getKey(),
			TFBlocks.STRIPPED_DARK_WOOD.getKey(),
			TFBlocks.STRIPPED_TIME_WOOD.getKey(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.getKey(),
			TFBlocks.STRIPPED_MINING_WOOD.getKey(),
			TFBlocks.STRIPPED_SORTING_WOOD.getKey());

		this.tag(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES).add(TFBlocks.CANOPY_BOOKSHELF.getKey());
		this.tag(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS).add(TFBlocks.AURORALIZED_GLASS.getKey());
		this.tag(Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES, Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES).add(TFBlocks.UNCRAFTING_TABLE.getKey());
		this.tag(Tags.Blocks.ROPES, Tags.Items.ROPES).add(TFBlocks.ROPE.getKey());

		//-----------
		//  TF TAGS
		//-----------

		this.tag(TFBlockTags.TWILIGHT_OAK_LOGS, TFItemTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.getKey(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.getKey(), TFBlocks.TWILIGHT_OAK_WOOD.getKey(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.getKey());
		this.tag(TFBlockTags.CANOPY_LOGS, TFItemTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.getKey(), TFBlocks.STRIPPED_CANOPY_LOG.getKey(), TFBlocks.CANOPY_WOOD.getKey(), TFBlocks.STRIPPED_CANOPY_WOOD.getKey());
		this.tag(TFBlockTags.MANGROVE_LOGS, TFItemTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.getKey(), TFBlocks.STRIPPED_MANGROVE_LOG.getKey(), TFBlocks.MANGROVE_WOOD.getKey(), TFBlocks.STRIPPED_MANGROVE_WOOD.getKey());
		this.tag(TFBlockTags.DARKWOOD_LOGS, TFItemTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.getKey(), TFBlocks.STRIPPED_DARK_LOG.getKey(), TFBlocks.DARK_WOOD.getKey(), TFBlocks.STRIPPED_DARK_WOOD.getKey());
		this.tag(TFBlockTags.TIME_LOGS, TFItemTags.TIME_LOGS).add(TFBlocks.TIME_LOG.getKey(), TFBlocks.STRIPPED_TIME_LOG.getKey(), TFBlocks.TIME_WOOD.getKey(), TFBlocks.STRIPPED_TIME_WOOD.getKey());
		this.tag(TFBlockTags.TRANSFORMATION_LOGS, TFItemTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.getKey(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.getKey(), TFBlocks.TRANSFORMATION_WOOD.getKey(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.getKey());
		this.tag(TFBlockTags.MINING_LOGS, TFItemTags.MINING_LOGS).add(TFBlocks.MINING_LOG.getKey(), TFBlocks.STRIPPED_MINING_LOG.getKey(), TFBlocks.MINING_WOOD.getKey(), TFBlocks.STRIPPED_MINING_WOOD.getKey());
		this.tag(TFBlockTags.SORTING_LOGS, TFItemTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.getKey(), TFBlocks.STRIPPED_SORTING_LOG.getKey(), TFBlocks.SORTING_WOOD.getKey(), TFBlocks.STRIPPED_SORTING_WOOD.getKey());

		this.tag(TFBlockTags.BANISTERS, TFItemTags.BANISTERS).add(
			TFBlocks.OAK_BANISTER.getKey(),
			TFBlocks.SPRUCE_BANISTER.getKey(),
			TFBlocks.BIRCH_BANISTER.getKey(),
			TFBlocks.JUNGLE_BANISTER.getKey(),
			TFBlocks.ACACIA_BANISTER.getKey(),
			TFBlocks.DARK_OAK_BANISTER.getKey(),
			TFBlocks.CRIMSON_BANISTER.getKey(),
			TFBlocks.WARPED_BANISTER.getKey(),
			TFBlocks.VANGROVE_BANISTER.getKey(),
			TFBlocks.BAMBOO_BANISTER.getKey(),
			TFBlocks.CHERRY_BANISTER.getKey(),
			TFBlocks.PALE_OAK_BANISTER.getKey(),

			TFBlocks.TWILIGHT_OAK_BANISTER.getKey(),
			TFBlocks.CANOPY_BANISTER.getKey(),
			TFBlocks.MANGROVE_BANISTER.getKey(),
			TFBlocks.DARK_BANISTER.getKey(),
			TFBlocks.TIME_BANISTER.getKey(),
			TFBlocks.TRANSFORMATION_BANISTER.getKey(),
			TFBlocks.MINING_BANISTER.getKey(),
			TFBlocks.SORTING_BANISTER.getKey()
		);

		this.tag(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR, TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_CARMINITE, TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_FIERY, TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_STEELEAF, TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.getKey());

		this.tag(TFBlockTags.DRYING_RACKS, TFItemTags.DRYING_RACKS).add(
			TFBlocks.OAK_DRYING_RACK.getKey(), TFBlocks.SPRUCE_DRYING_RACK.getKey(),
			TFBlocks.BIRCH_DRYING_RACK.getKey(), TFBlocks.JUNGLE_DRYING_RACK.getKey(),
			TFBlocks.ACACIA_DRYING_RACK.getKey(), TFBlocks.DARK_OAK_DRYING_RACK.getKey(),
			TFBlocks.CRIMSON_DRYING_RACK.getKey(), TFBlocks.WARPED_DRYING_RACK.getKey(),
			TFBlocks.VANGROVE_DRYING_RACK.getKey(), TFBlocks.BAMBOO_DRYING_RACK.getKey(),
			TFBlocks.CHERRY_DRYING_RACK.getKey(), TFBlocks.PALE_OAK_DRYING_RACK.getKey(),
			TFBlocks.TWILIGHT_OAK_DRYING_RACK.getKey(), TFBlocks.CANOPY_DRYING_RACK.getKey(),
			TFBlocks.MANGROVE_DRYING_RACK.getKey(), TFBlocks.DARK_DRYING_RACK.getKey(),
			TFBlocks.TIME_DRYING_RACK.getKey(), TFBlocks.TRANSFORMATION_DRYING_RACK.getKey(),
			TFBlocks.MINING_DRYING_RACK.getKey(), TFBlocks.SORTING_DRYING_RACK.getKey()
		);

		this.tag(TFBlockTags.TOWERWOOD, TFItemTags.TOWERWOOD).add(
			TFBlocks.TOWERWOOD.getKey(),
			TFBlocks.MOSSY_TOWERWOOD.getKey(),
			TFBlocks.CRACKED_TOWERWOOD.getKey(),
			TFBlocks.INFESTED_TOWERWOOD.getKey());

		this.tag(TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME, TFItemTags.STORAGE_BLOCKS_MAZE_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.getKey());

		this.tag(TFBlockTags.CLOUDS, TFItemTags.CLOUDS).add(
			TFBlocks.FLUFFY_CLOUD.getKey(),
			TFBlocks.WISPY_CLOUD.getKey(),
			TFBlocks.RAINY_CLOUD.getKey(),
			TFBlocks.SNOWY_CLOUD.getKey());
	}

	// FIXME
	protected abstract TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag);
}
