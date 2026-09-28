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

public abstract class BlockItemTagGenerator {

	@SuppressWarnings("unchecked")
	protected void run() {

		//------------
		//VANILLA TAGS
		//------------

		this.tag(BlockTags.PLANKS, ItemTags.PLANKS).add(
				TFBlocks.TWILIGHT_OAK_PLANKS.get(),
				TFBlocks.CANOPY_PLANKS.get(),
				TFBlocks.MANGROVE_PLANKS.get(),
				TFBlocks.DARK_PLANKS.get(),
				TFBlocks.TIME_PLANKS.get(),
				TFBlocks.TRANSFORMATION_PLANKS.get(),
				TFBlocks.MINING_PLANKS.get(),
				TFBlocks.SORTING_PLANKS.get())
			.addTag(TFBlockTags.TOWERWOOD);

		this.tag(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS).add(
			TFBlocks.TWILIGHT_OAK_BUTTON.get(),
			TFBlocks.CANOPY_BUTTON.get(),
			TFBlocks.MANGROVE_BUTTON.get(),
			TFBlocks.DARK_BUTTON.get(),
			TFBlocks.TIME_BUTTON.get(),
			TFBlocks.TRANSFORMATION_BUTTON.get(),
			TFBlocks.MINING_BUTTON.get(),
			TFBlocks.SORTING_BUTTON.get());

		this.tag(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS).add(TFBlocks.CORONATION_CARPET.get());
		this.tag(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS).add(
			TFBlocks.TWILIGHT_OAK_DOOR.get(),
			TFBlocks.CANOPY_DOOR.get(),
			TFBlocks.MANGROVE_DOOR.get(),
			TFBlocks.DARK_DOOR.get(),
			TFBlocks.TIME_DOOR.get(),
			TFBlocks.TRANSFORMATION_DOOR.get(),
			TFBlocks.MINING_DOOR.get(),
			TFBlocks.SORTING_DOOR.get());

		this.tag(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS).add(
			TFBlocks.TWILIGHT_OAK_STAIRS.get(),
			TFBlocks.CANOPY_STAIRS.get(),
			TFBlocks.MANGROVE_STAIRS.get(),
			TFBlocks.DARK_STAIRS.get(),
			TFBlocks.TIME_STAIRS.get(),
			TFBlocks.TRANSFORMATION_STAIRS.get(),
			TFBlocks.MINING_STAIRS.get(),
			TFBlocks.SORTING_STAIRS.get());

		this.tag(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS).add(
			TFBlocks.TWILIGHT_OAK_SLAB.get(),
			TFBlocks.CANOPY_SLAB.get(),
			TFBlocks.MANGROVE_SLAB.get(),
			TFBlocks.DARK_SLAB.get(),
			TFBlocks.TIME_SLAB.get(),
			TFBlocks.TRANSFORMATION_SLAB.get(),
			TFBlocks.MINING_SLAB.get(),
			TFBlocks.SORTING_SLAB.get());

		this.tag(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES).add(
			TFBlocks.TWILIGHT_OAK_FENCE.get(),
			TFBlocks.CANOPY_FENCE.get(),
			TFBlocks.MANGROVE_FENCE.get(),
			TFBlocks.DARK_FENCE.get(),
			TFBlocks.TIME_FENCE.get(),
			TFBlocks.TRANSFORMATION_FENCE.get(),
			TFBlocks.MINING_FENCE.get(),
			TFBlocks.SORTING_FENCE.get());

		this.tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES).add(
			TFBlocks.TWILIGHT_OAK_GATE.get(),
			TFBlocks.CANOPY_GATE.get(),
			TFBlocks.MANGROVE_GATE.get(),
			TFBlocks.DARK_GATE.get(),
			TFBlocks.TIME_GATE.get(),
			TFBlocks.TRANSFORMATION_GATE.get(),
			TFBlocks.MINING_GATE.get(),
			TFBlocks.SORTING_GATE.get());

		this.tag(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES).add(
			TFBlocks.TWILIGHT_OAK_PLATE.get(),
			TFBlocks.CANOPY_PLATE.get(),
			TFBlocks.MANGROVE_PLATE.get(),
			TFBlocks.DARK_PLATE.get(),
			TFBlocks.TIME_PLATE.get(),
			TFBlocks.TRANSFORMATION_PLATE.get(),
			TFBlocks.MINING_PLATE.get(),
			TFBlocks.SORTING_PLATE.get());

		this.tag(BlockTags.SAPLINGS, ItemTags.SAPLINGS).add(
			TFBlocks.TWILIGHT_OAK_SAPLING.get(),
			TFBlocks.CANOPY_SAPLING.get(),
			TFBlocks.MANGROVE_SAPLING.get(),
			TFBlocks.DARKWOOD_SAPLING.get(),
			TFBlocks.TIME_SAPLING.get(),
			TFBlocks.TRANSFORMATION_SAPLING.get(),
			TFBlocks.MINING_SAPLING.get(),
			TFBlocks.SORTING_SAPLING.get(),
			TFBlocks.HOLLOW_OAK_SAPLING.get(),
			TFBlocks.RAINBOW_OAK_SAPLING.get());


		this.tag(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN).addTags(
			TFBlockTags.TWILIGHT_OAK_LOGS,
			TFBlockTags.CANOPY_LOGS,
			TFBlockTags.MANGROVE_LOGS,
			TFBlockTags.DARKWOOD_LOGS,
			TFBlockTags.TIME_LOGS,
			TFBlockTags.TRANSFORMATION_LOGS,
			TFBlockTags.MINING_LOGS,
			TFBlockTags.SORTING_LOGS);

		this.tag(BlockTags.SLABS, ItemTags.SLABS).add(TFBlocks.AURORA_SLAB.get());
		this.tag(BlockTags.WALLS, ItemTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.get());
		this.tag(BlockTags.STAIRS, ItemTags.STAIRS).add(
			TFBlocks.CASTLE_BRICK_STAIRS.get(),
			TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());

		this.tag(BlockTags.LEAVES, ItemTags.LEAVES).add(
			TFBlocks.RAINBOW_OAK_LEAVES.get(),
			TFBlocks.TWILIGHT_OAK_LEAVES.get(),
			TFBlocks.CANOPY_LEAVES.get(),
			TFBlocks.MANGROVE_LEAVES.get(),
			TFBlocks.DARK_LEAVES.get(),
			TFBlocks.TIME_LEAVES.get(),
			TFBlocks.TRANSFORMATION_LEAVES.get(),
			TFBlocks.MINING_LEAVES.get(),
			TFBlocks.SORTING_LEAVES.get(),
			TFBlocks.THORN_LEAVES.get(),
			TFBlocks.BEANSTALK_LEAVES.get());

		this.tag(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS).add(
			TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(),
			TFBlocks.CANOPY_TRAPDOOR.get(),
			TFBlocks.MANGROVE_TRAPDOOR.get(),
			TFBlocks.DARK_TRAPDOOR.get(),
			TFBlocks.TIME_TRAPDOOR.get(),
			TFBlocks.TRANSFORMATION_TRAPDOOR.get(),
			TFBlocks.MINING_TRAPDOOR.get(),
			TFBlocks.SORTING_TRAPDOOR.get());

		this.tag(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS).addTag(TFBlockTags.CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		this.tag(BlockTags.DIRT, ItemTags.DIRT).add(TFBlocks.UBEROUS_SOIL.get());
		this.tag(BlockTags.STANDING_SIGNS, ItemTags.SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.get(),
			TFBlocks.CANOPY_SIGN.get(),
			TFBlocks.MANGROVE_SIGN.get(),
			TFBlocks.DARK_SIGN.get(),
			TFBlocks.TIME_SIGN.get(),
			TFBlocks.TRANSFORMATION_SIGN.get(),
			TFBlocks.MINING_SIGN.get(),
			TFBlocks.SORTING_SIGN.get());

		this.tag(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(),
			TFBlocks.CANOPY_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_HANGING_SIGN.get(),
			TFBlocks.DARK_HANGING_SIGN.get(),
			TFBlocks.TIME_HANGING_SIGN.get(),
			TFBlocks.TRANSFORMATION_HANGING_SIGN.get(),
			TFBlocks.MINING_HANGING_SIGN.get(),
			TFBlocks.SORTING_HANGING_SIGN.get());

		//------------
		// COMMON TAGS
		//------------

		this.tag(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN).add(
			TFBlocks.TWILIGHT_OAK_GATE.get(),
			TFBlocks.CANOPY_GATE.get(),
			TFBlocks.MANGROVE_GATE.get(),
			TFBlocks.DARK_GATE.get(),
			TFBlocks.TIME_GATE.get(),
			TFBlocks.TRANSFORMATION_GATE.get(),
			TFBlocks.MINING_GATE.get(),
			TFBlocks.SORTING_GATE.get());

		this.tag(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN).add(
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
				TFBlocks.CANOPY_CHEST.get(),
				TFBlocks.MANGROVE_CHEST.get(),
				TFBlocks.DARK_CHEST.get(),
				TFBlocks.TIME_CHEST.get(),
				TFBlocks.TRANSFORMATION_CHEST.get(),
				TFBlocks.MINING_CHEST.get(),
				TFBlocks.SORTING_CHEST.get())
			.add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
				TFBlocks.CANOPY_TRAPPED_CHEST.get(),
				TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
				TFBlocks.DARK_TRAPPED_CHEST.get(),
				TFBlocks.TIME_TRAPPED_CHEST.get(),
				TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
				TFBlocks.MINING_TRAPPED_CHEST.get(),
				TFBlocks.SORTING_TRAPPED_CHEST.get());

		this.tag(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED).add(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get());

		this.tag(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS).addTags(
			TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR,
			TFBlockTags.STORAGE_BLOCKS_CARMINITE,
			TFBlockTags.STORAGE_BLOCKS_FIERY,
			TFBlockTags.STORAGE_BLOCKS_IRONWOOD,
			TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL,
			TFBlockTags.STORAGE_BLOCKS_STEELEAF,
			TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME);

		this.tag(Tags.Blocks.STORAGE_BLOCKS_SLIME, Tags.Items.STORAGE_BLOCKS_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.get());
		this.tag(Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(),
			TFBlocks.STRIPPED_CANOPY_LOG.get(),
			TFBlocks.STRIPPED_MANGROVE_LOG.get(),
			TFBlocks.STRIPPED_DARK_LOG.get(),
			TFBlocks.STRIPPED_TIME_LOG.get(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(),
			TFBlocks.STRIPPED_MINING_LOG.get(),
			TFBlocks.STRIPPED_SORTING_LOG.get());

		this.tag(Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(),
			TFBlocks.STRIPPED_CANOPY_WOOD.get(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.get(),
			TFBlocks.STRIPPED_DARK_WOOD.get(),
			TFBlocks.STRIPPED_TIME_WOOD.get(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(),
			TFBlocks.STRIPPED_MINING_WOOD.get(),
			TFBlocks.STRIPPED_SORTING_WOOD.get());

		//-----------
		//  TF TAGS
		//-----------

		this.tag(TFBlockTags.TWILIGHT_OAK_LOGS, TFItemTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		this.tag(TFBlockTags.CANOPY_LOGS, TFItemTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
		this.tag(TFBlockTags.MANGROVE_LOGS, TFItemTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		this.tag(TFBlockTags.DARKWOOD_LOGS, TFItemTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
		this.tag(TFBlockTags.TIME_LOGS, TFItemTags.TIME_LOGS).add(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
		this.tag(TFBlockTags.TRANSFORMATION_LOGS, TFItemTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		this.tag(TFBlockTags.MINING_LOGS, TFItemTags.MINING_LOGS).add(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
		this.tag(TFBlockTags.SORTING_LOGS, TFItemTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());

		this.tag(TFBlockTags.BANISTERS, TFItemTags.BANISTERS).add(
			TFBlocks.OAK_BANISTER.get(),
			TFBlocks.SPRUCE_BANISTER.get(),
			TFBlocks.BIRCH_BANISTER.get(),
			TFBlocks.JUNGLE_BANISTER.get(),
			TFBlocks.ACACIA_BANISTER.get(),
			TFBlocks.DARK_OAK_BANISTER.get(),
			TFBlocks.CRIMSON_BANISTER.get(),
			TFBlocks.WARPED_BANISTER.get(),
			TFBlocks.VANGROVE_BANISTER.get(),
			TFBlocks.BAMBOO_BANISTER.get(),
			TFBlocks.CHERRY_BANISTER.get(),
			TFBlocks.PALE_OAK_BANISTER.get(),

			TFBlocks.TWILIGHT_OAK_BANISTER.get(),
			TFBlocks.CANOPY_BANISTER.get(),
			TFBlocks.MANGROVE_BANISTER.get(),
			TFBlocks.DARK_BANISTER.get(),
			TFBlocks.TIME_BANISTER.get(),
			TFBlocks.TRANSFORMATION_BANISTER.get(),
			TFBlocks.MINING_BANISTER.get(),
			TFBlocks.SORTING_BANISTER.get()
		);

		this.tag(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR, TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_CARMINITE, TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_FIERY, TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_STEELEAF, TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.get());

		this.tag(TFBlockTags.DRYING_RACKS, TFItemTags.DRYING_RACKS).add(
			TFBlocks.OAK_DRYING_RACK.get(), TFBlocks.SPRUCE_DRYING_RACK.get(),
			TFBlocks.BIRCH_DRYING_RACK.get(), TFBlocks.JUNGLE_DRYING_RACK.get(),
			TFBlocks.ACACIA_DRYING_RACK.get(), TFBlocks.DARK_OAK_DRYING_RACK.get(),
			TFBlocks.CRIMSON_DRYING_RACK.get(), TFBlocks.WARPED_DRYING_RACK.get(),
			TFBlocks.VANGROVE_DRYING_RACK.get(), TFBlocks.BAMBOO_DRYING_RACK.get(),
			TFBlocks.CHERRY_DRYING_RACK.get(), TFBlocks.PALE_OAK_DRYING_RACK.get(),
			TFBlocks.TWILIGHT_OAK_DRYING_RACK.get(), TFBlocks.CANOPY_DRYING_RACK.get(),
			TFBlocks.MANGROVE_DRYING_RACK.get(), TFBlocks.DARK_DRYING_RACK.get(),
			TFBlocks.TIME_DRYING_RACK.get(), TFBlocks.TRANSFORMATION_DRYING_RACK.get(),
			TFBlocks.MINING_DRYING_RACK.get(), TFBlocks.SORTING_DRYING_RACK.get()
		);

		this.tag(TFBlockTags.TOWERWOOD, TFItemTags.TOWERWOOD).add(
			TFBlocks.TOWERWOOD.get(),
			TFBlocks.MOSSY_TOWERWOOD.get(),
			TFBlocks.CRACKED_TOWERWOOD.get(),
			TFBlocks.INFESTED_TOWERWOOD.get());

		this.tag(TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME, TFItemTags.STORAGE_BLOCKS_MAZE_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.get());

		this.tag(TFBlockTags.CLOUDS, TFItemTags.CLOUDS).add(
			TFBlocks.FLUFFY_CLOUD.get(),
			TFBlocks.WISPY_CLOUD.get(),
			TFBlocks.RAINY_CLOUD.get(),
			TFBlocks.SNOWY_CLOUD.get());
	}

	protected abstract TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag);
}
