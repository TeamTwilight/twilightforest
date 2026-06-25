package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedBlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends ModdedBlockTagGenerator {

	public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(TFBlockTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		this.tag(TFBlockTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
		this.tag(TFBlockTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		this.tag(TFBlockTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
		this.tag(TFBlockTags.TIME_LOGS).add(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
		this.tag(TFBlockTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		this.tag(TFBlockTags.MINING_LOGS).add(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
		this.tag(TFBlockTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());
		this.tag(TFBlockTags.TF_LOGS).addTags(TFBlockTags.TWILIGHT_OAK_LOGS, TFBlockTags.CANOPY_LOGS, TFBlockTags.MANGROVE_LOGS, TFBlockTags.DARKWOOD_LOGS, TFBlockTags.TIME_LOGS, TFBlockTags.TRANSFORMATION_LOGS, TFBlockTags.MINING_LOGS, TFBlockTags.SORTING_LOGS);
		this.tag(BlockTags.LOGS).addTag(TFBlockTags.TF_LOGS);
		this.tag(BlockTags.LOGS_THAT_BURN).addTag(TFBlockTags.TF_LOGS);

		this.tag(BlockTags.SAPLINGS).add(TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.CANOPY_SAPLING.get(), TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.TIME_SAPLING.get(), TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.MINING_SAPLING.get(), TFBlocks.SORTING_SAPLING.get(), TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_SAPLING.get());
		this.tag(BlockTags.LEAVES).add(TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.TIME_LEAVES.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.SORTING_LEAVES.get(), TFBlocks.THORN_LEAVES.get(), TFBlocks.BEANSTALK_LEAVES.get());

		this.tag(BlockTags.PLANKS).add(TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.CANOPY_PLANKS.get(), TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.DARK_PLANKS.get(), TFBlocks.TIME_PLANKS.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.MINING_PLANKS.get(), TFBlocks.SORTING_PLANKS.get()).addTag(TFBlockTags.TOWERWOOD);
		this.tag(BlockTags.WOODEN_SLABS).add(TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.SORTING_SLAB.get());
		this.tag(BlockTags.SLABS).add(TFBlocks.AURORA_SLAB.get());
		this.tag(BlockTags.WOODEN_STAIRS).add(TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.SORTING_STAIRS.get());
		this.tag(BlockTags.STAIRS).add(TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());
		this.tag(BlockTags.WOODEN_FENCES).add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		this.tag(BlockTags.FENCE_GATES).add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		this.tag(BlockTags.WOODEN_BUTTONS).add(TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.SORTING_BUTTON.get());
		this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.SORTING_PLATE.get());

		this.tag(BlockTags.WOODEN_TRAPDOORS).add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.SORTING_TRAPDOOR.get());
		this.tag(BlockTags.WOODEN_DOORS).add(TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.SORTING_DOOR.get());

		this.tag(Tags.Blocks.CHESTS_WOODEN).add(TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.MANGROVE_CHEST.get(), TFBlocks.DARK_CHEST.get(), TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get());
		this.tag(Tags.Blocks.CHESTS_TRAPPED).add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get());

		this.tag(BlockTags.FLOWER_POTS).add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(), TFBlocks.POTTED_DARKWOOD_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), TFBlocks.POTTED_TIME_SAPLING.get(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), TFBlocks.POTTED_MINING_SAPLING.get(), TFBlocks.POTTED_SORTING_SAPLING.get(), TFBlocks.POTTED_MAYAPPLE.get(), TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.POTTED_MUSHGLOOM.get(), TFBlocks.POTTED_THORN.get(), TFBlocks.POTTED_GREEN_THORN.get(), TFBlocks.POTTED_DEAD_THORN.get());

		this.tag(BlockTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.get());

		this.tag(TFBlockTags.BANISTERS).add(
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

		this.tag(TFBlockTags.HOLLOW_LOGS_HORIZONTAL).add(
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(),
			TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_VERTICAL).add(
			TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(),
			TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE).add(
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(),
			TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS).addTags(TFBlockTags.HOLLOW_LOGS_HORIZONTAL, TFBlockTags.HOLLOW_LOGS_VERTICAL, TFBlockTags.HOLLOW_LOGS_CLIMBABLE);

		this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.get());
		this.tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.get());
		this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.get());
		this.tag(BlockTags.REPLACEABLE_BY_TREES).add(
			TFBlocks.HARDENED_DARK_LEAVES.get(),
			TFBlocks.MAYAPPLE.get(),
			TFBlocks.FIDDLEHEAD.get(),
			TFBlocks.MOSS_PATCH.get(),
			TFBlocks.CLOVER_PATCH.get(),
			TFBlocks.MUSHGLOOM.get(),
			TFBlocks.FIREFLY.get(),
			TFBlocks.FALLEN_LEAVES.get(),
			TFBlocks.TORCHBERRY_PLANT.get(),
			TFBlocks.ROOT_STRAND.get(),
			TFBlocks.ROOT_BLOCK.get());

		this.tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.get(), TFBlocks.ROPE.get(), TFBlocks.ROOT_STRAND.get()).addTag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE);

		this.tag(BlockTags.STANDING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.CANOPY_SIGN.get(),
			TFBlocks.MANGROVE_SIGN.get(), TFBlocks.DARK_SIGN.get(),
			TFBlocks.TIME_SIGN.get(), TFBlocks.TRANSFORMATION_SIGN.get(),
			TFBlocks.MINING_SIGN.get(), TFBlocks.SORTING_SIGN.get());

		this.tag(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
			TFBlocks.MANGROVE_WALL_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(),
			TFBlocks.TIME_WALL_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
			TFBlocks.MINING_WALL_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get());

		this.tag(BlockTags.CEILING_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.CANOPY_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.DARK_HANGING_SIGN.get(),
			TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_HANGING_SIGN.get(),
			TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.SORTING_HANGING_SIGN.get());

		this.tag(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(),
			TFBlocks.TIME_WALL_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(),
			TFBlocks.MINING_WALL_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get());

		this.tag(TFBlockTags.TOWERWOOD).add(TFBlocks.TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get());

		this.tag(TFBlockTags.MAZESTONE).add(
			TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get(),
			TFBlocks.CRACKED_MAZESTONE.get(), TFBlocks.MOSSY_MAZESTONE.get(),
			TFBlocks.CUT_MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get(),
			TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BORDER.get());

		this.tag(TFBlockTags.CASTLE_BLOCKS).add(
			TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get(),
			TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(),
			TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.THICK_CASTLE_BRICK.get(),
			TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(),
			TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(),
			TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
			TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get(),
			TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get()
		);

		this.tag(TFBlockTags.MAZEBREAKER_ACCELERATED).addTag(TFBlockTags.MAZESTONE).addTag(TFBlockTags.CASTLE_BLOCKS);

		this.tag(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.get());
		this.tag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.get());

		this.tag(BlockTags.BEACON_BASE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR, TFBlockTags.STORAGE_BLOCKS_CARMINITE, TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(BlockTags.DIRT).add(TFBlocks.UBEROUS_SOIL.get());
		this.tag(TFBlockTags.PORTAL_EDGE).add(Blocks.FARMLAND, Blocks.DIRT_PATH).addTags(BlockTags.DIRT, BlockTags.GRASS_BLOCKS);
		this.tag(TFBlockTags.PORTAL_POOL).add(Blocks.WATER);
		this.tag(TFBlockTags.PORTAL_DECO).add(
				Blocks.BAMBOO,
				Blocks.SHORT_GRASS, Blocks.TALL_GRASS,
				Blocks.FERN, Blocks.LARGE_FERN,
				Blocks.DEAD_BUSH,
				Blocks.SUGAR_CANE,
				Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
				Blocks.SWEET_BERRY_BUSH,
				Blocks.NETHER_WART,
				Blocks.COCOA,
				Blocks.VINE, Blocks.GLOW_LICHEN,
				Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
				Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS,
				Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM,
				Blocks.MOSS_CARPET,
				Blocks.PINK_PETALS,
				Blocks.BIG_DRIPLEAF,
				Blocks.BIG_DRIPLEAF_STEM,
				Blocks.SMALL_DRIPLEAF,
				TFBlocks.FIDDLEHEAD.get(),
				TFBlocks.MOSS_PATCH.get(),
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.CLOVER_PATCH.get(),
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.FALLEN_LEAVES.get(),
				TFBlocks.GIANT_LEAVES.get(),
				TFBlocks.STEELEAF_BLOCK.get(),
				TFBlocks.HARDENED_DARK_LEAVES.get())
			.addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockTags.SAPLINGS, BlockTags.CROPS);

		this.tag(TFBlockTags.GENERATED_PORTAL_DECO)
			.add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM,
				Blocks.SHORT_GRASS, Blocks.FERN,
				Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET,
				Blocks.LILY_OF_THE_VALLEY, Blocks.OXEYE_DAISY,
				Blocks.ALLIUM, Blocks.CORNFLOWER,
				Blocks.WHITE_TULIP, Blocks.PINK_TULIP,
				Blocks.ORANGE_TULIP, Blocks.RED_TULIP,
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.FIDDLEHEAD.get());

		this.tag(TFBlockTags.DARK_TOWER_ALLOWED_POTS)
			.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(),
				TFBlocks.POTTED_DARKWOOD_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_MAYAPPLE.get(),
				TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.POTTED_MUSHGLOOM.get())
			.add(Blocks.FLOWER_POT, Blocks.POTTED_POPPY, Blocks.POTTED_BLUE_ORCHID, Blocks.POTTED_ALLIUM, Blocks.POTTED_AZURE_BLUET,
				Blocks.POTTED_RED_TULIP, Blocks.POTTED_ORANGE_TULIP, Blocks.POTTED_WHITE_TULIP, Blocks.POTTED_PINK_TULIP,
				Blocks.POTTED_OXEYE_DAISY, Blocks.POTTED_DANDELION, Blocks.POTTED_OAK_SAPLING, Blocks.POTTED_SPRUCE_SAPLING,
				Blocks.POTTED_BIRCH_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, Blocks.POTTED_ACACIA_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING,
				Blocks.POTTED_RED_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, Blocks.POTTED_DEAD_BUSH, Blocks.POTTED_FERN,
				Blocks.POTTED_CACTUS, Blocks.POTTED_CORNFLOWER, Blocks.POTTED_LILY_OF_THE_VALLEY, Blocks.POTTED_WITHER_ROSE,
				Blocks.POTTED_BAMBOO, Blocks.POTTED_CRIMSON_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, Blocks.POTTED_CRIMSON_ROOTS,
				Blocks.POTTED_WARPED_ROOTS, Blocks.POTTED_AZALEA, Blocks.POTTED_FLOWERING_AZALEA, Blocks.POTTED_MANGROVE_PROPAGULE);

		this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.get());

		this.tag(TFBlockTags.TROPHIES)
			.add(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get())
			.add(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get())
			.add(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get())
			.add(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get())
			.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get())
			.add(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get())
			.add(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get())
			.add(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get())
			.add(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get());

		this.tag(TFBlockTags.FIRE_JET_FUEL).add(Blocks.LAVA);

		this.tag(TFBlockTags.ICE_BOMB_REPLACEABLES)
			.add(TFBlocks.MAYAPPLE.get(), TFBlocks.FIDDLEHEAD.get(), Blocks.SHORT_GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN)
			.addTag(BlockTags.FLOWERS);

		this.tag(TFBlockTags.PLANTS_HANG_ON)
			.addTag(BlockTags.DIRT)
			.add(Blocks.MOSS_BLOCK, TFBlocks.MANGROVE_ROOT.get(), TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get());

		this.tag(TFBlockTags.COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
			TFBlocks.NAGA_BOSS_SPAWNER.get(),
			TFBlocks.LICH_BOSS_SPAWNER.get(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(),
			TFBlocks.HYDRA_BOSS_SPAWNER.get(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(),
			TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(),
			TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
			TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get(),
			TFBlocks.LOCKED_VANISHING_BLOCK.get(),
			TFBlocks.PINK_FORCE_FIELD.get(),
			TFBlocks.ORANGE_FORCE_FIELD.get(),
			TFBlocks.GREEN_FORCE_FIELD.get(),
			TFBlocks.BLUE_FORCE_FIELD.get(),
			TFBlocks.VIOLET_FORCE_FIELD.get(),
			TFBlocks.SKULL_CHEST.get(),
			TFBlocks.KEEPSAKE_CASKET.get(),
			TFBlocks.TROPHY_PEDESTAL.get()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
			Blocks.BARRIER,
			Blocks.BEDROCK,
			Blocks.END_PORTAL,
			Blocks.END_PORTAL_FRAME,
			Blocks.END_GATEWAY,
			Blocks.COMMAND_BLOCK,
			Blocks.REPEATING_COMMAND_BLOCK,
			Blocks.CHAIN_COMMAND_BLOCK,
			Blocks.STRUCTURE_BLOCK,
			Blocks.JIGSAW,
			Blocks.MOVING_PISTON,
			Blocks.LIGHT,
			Blocks.REINFORCED_DEEPSLATE
		);

		this.tag(BlockTags.DRAGON_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.get(), TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		this.tag(BlockTags.WITHER_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		this.tag(TFBlockTags.CARMINITE_REACTOR_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS);

		this.tag(TFBlockTags.CARMINITE_REACTOR_ORES).add(Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE);

		this.tag(TFBlockTags.DEADROCK).add(TFBlocks.DEADROCK.get(), TFBlocks.CRACKED_DEADROCK.get(), TFBlocks.WEATHERED_DEADROCK.get());

		this.tag(TFBlockTags.ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
			.add(Blocks.NETHER_PORTAL)
			.addTag(TFBlockTags.DEADROCK)
			.add(TFBlocks.CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.WORN_CASTLE_BRICK.get())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_RUNE_BRICK.get())
			.add(TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(), TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(), TFBlocks.VIOLET_FORCE_FIELD.get())
			.add(TFBlocks.BROWN_THORNS.get(), TFBlocks.GREEN_THORNS.get());

		this.tag(TFBlockTags.ANTIBUILDER_IGNORES).add(
			Blocks.REDSTONE_LAMP,
			Blocks.TNT,
			Blocks.WATER,
			TFBlocks.ANTIBUILDER.get(),
			TFBlocks.CARMINITE_BUILDER.get(),
			TFBlocks.BUILT_BLOCK.get(),
			TFBlocks.REACTOR_DEBRIS.get(),
			TFBlocks.CARMINITE_REACTOR.get(),
			TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.GHAST_TRAP.get(),
			TFBlocks.FAKE_DIAMOND.get(),
			TFBlocks.FAKE_GOLD.get()
		).addTag(TFBlockTags.COMMON_PROTECTIONS);//.addOptional(Identifier.parse("gravestone:gravestone"));

		this.tag(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS).add(Blocks.LEVER).add(TFBlocks.ANTIBUILDER.get()).addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS);

		// TODO add more grave mods to this list
		this.tag(TFBlockTags.PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.get())
			.add(TFBlocks.KEEPSAKE_CASKET.get());
			//.addOptional(Identifier.fromNamespaceAndPath("gravestone", "gravestone"))

		this.tag(TFBlockTags.CANNOT_TROLL_CAVE_HOLLOW)
			.add(Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.BROWN_MUSHROOM_BLOCK)
			.add(TFBlocks.HUGE_MUSHGLOOM.get());

		this.tag(TFBlockTags.ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
			BlockTags.DIRT,
			Tags.Blocks.GRAVELS,
			Tags.Blocks.SANDS,
			BlockTags.NYLIUM,
			BlockTags.BASE_STONE_OVERWORLD,
			BlockTags.BASE_STONE_NETHER,
			Tags.Blocks.END_STONES,
			BlockTags.DEEPSLATE_ORE_REPLACEABLES,
			BlockTags.STONE_ORE_REPLACEABLES,
			TFBlockTags.ROOT_GROUND
		);

		this.tag(TFBlockTags.ORE_MAGNET_IGNORE).addTag(BlockTags.COAL_ORES);
		this.tag(TFBlockTags.MINING_CORE_EXCLUDED).addTag(BlockTags.COAL_ORES);

		this.tag(TFBlockTags.ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.get());
		this.tag(TFBlockTags.ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.get());

		this.tag(TFBlockTags.CLOUDS).add(TFBlocks.FLUFFY_CLOUD.get(), TFBlocks.WISPY_CLOUD.get(), TFBlocks.RAINY_CLOUD.get(), TFBlocks.SNOWY_CLOUD.get());

		this.tag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.SORTING_CHEST.get());

		this.tag(BlockTags.DAMPENS_VIBRATIONS).addTag(TFBlockTags.CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		this.tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());

		this.tag(BlockTags.SUPPORTS_SMALL_DRIPLEAF).add(TFBlocks.UBEROUS_SOIL.get());

		this.tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.MANGROVE_ROOT.get(), TFBlocks.SINISTER_SPAWNER.get());
		// For anything that permits replacement during Worldgen
		this.tag(TFBlockTags.WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES);

		this.tag(TFBlockTags.ROOT_TRACE_SKIP).addTag(BlockTags.LOGS).add(TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.MANGROVE_ROOT.get(), TFBlocks.TIME_WOOD.get()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);

		this.tag(TFBlockTags.DRUID_PROJECTILE_REPLACEABLE).addTags(BlockTags.LEAVES, BlockTags.LOGS, BlockTags.PLANKS, BlockTags.OVERWORLD_CARVER_REPLACEABLES, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.REPLACEABLE_BY_TREES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SCULK_REPLACEABLE, Tags.Blocks.ORES);

		this.tag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE).addTag(BlockTags.SUBSTRATE_OVERWORLD).add(Blocks.MYCELIUM).add(Blocks.PODZOL).add(Blocks.CRIMSON_NYLIUM).add(Blocks.WARPED_NYLIUM);

		this.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(TFBlocks.TROLLSTEINN.get());

		this.tag(TFBlockTags.TIME_CORE_EXCLUDED).add(Blocks.NETHER_PORTAL);

		this.tag(TFBlockTags.ORE_METER_TARGETABLE)
			.addTag(Tags.Blocks.ORES)
			.addTag(BlockTags.BASE_STONE_OVERWORLD)
			.addTag(BlockTags.BASE_STONE_NETHER)
			.addTag(BlockTags.DIRT)
			.addTag(Tags.Blocks.SANDS)
			.addTag(Tags.Blocks.SANDSTONE_BLOCKS)
			.addTag(BlockTags.TERRACOTTA)
			.addTag(Tags.Blocks.GRAVELS)
			.addTag(BlockTags.NYLIUM)
			.addTag(TFBlockTags.ROOT_ORES)
			.add(Blocks.BUDDING_AMETHYST)
			.add(Blocks.CALCITE)
			.add(Blocks.SOUL_SAND)
			.add(Blocks.SOUL_SOIL);

		this.tag(TFBlockTags.PENGUINS_SPAWNABLE_ON).addTag(BlockTags.ICE);
		this.tag(TFBlockTags.GIANTS_SPAWNABLE_ON).addTag(TFBlockTags.CLOUDS);

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
			TFBlocks.HEDGE.get(),
			TFBlocks.ROOT_BLOCK.get(),
			TFBlocks.LIVEROOT_BLOCK.get(),
			TFBlocks.MANGROVE_ROOT.get(),
			TFBlocks.UNCRAFTING_TABLE.get(),
			TFBlocks.ENCASED_SMOKER.get(),
			TFBlocks.ENCASED_FIRE_JET.get(),
			TFBlocks.TIME_LOG_CORE.get(),
			TFBlocks.TRANSFORMATION_LOG_CORE.get(),
			TFBlocks.MINING_LOG_CORE.get(),
			TFBlocks.SORTING_LOG_CORE.get(),
			TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.VANISHING_BLOCK.get(),
			TFBlocks.ANTIBUILDER.get(),
			TFBlocks.CARMINITE_REACTOR.get(),
			TFBlocks.CARMINITE_BUILDER.get(),
			TFBlocks.GHAST_TRAP.get(),
			TFBlocks.HUGE_STALK.get(),
			TFBlocks.HUGE_MUSHGLOOM.get(),
			TFBlocks.HUGE_MUSHGLOOM_STEM.get(),
			TFBlocks.CINDER_LOG.get(),
			TFBlocks.CINDER_WOOD.get(),
			TFBlocks.IRONWOOD_BLOCK.get(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.get(),
			TFBlocks.CANOPY_BOOKSHELF.get(),
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.SORTING_CHEST.get(),
			TFBlocks.HUGE_LILY_PAD.get()
		).addTags(TFBlockTags.BANISTERS, TFBlockTags.HOLLOW_LOGS, TFBlockTags.TOWERWOOD);

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
			//vanilla doesnt use the leaves tag
			TFBlocks.TWILIGHT_OAK_LEAVES.get(),
			TFBlocks.CANOPY_LEAVES.get(),
			TFBlocks.MANGROVE_LEAVES.get(),
			TFBlocks.DARK_LEAVES.get(),
			TFBlocks.RAINBOW_OAK_LEAVES.get(),
			TFBlocks.TIME_LEAVES.get(),
			TFBlocks.TRANSFORMATION_LEAVES.get(),
			TFBlocks.MINING_LEAVES.get(),
			TFBlocks.SORTING_LEAVES.get(),
			TFBlocks.THORN_LEAVES.get(),
			TFBlocks.THORN_ROSE.get(),
			TFBlocks.BEANSTALK_LEAVES.get(),
			TFBlocks.STEELEAF_BLOCK.get(),
			TFBlocks.ARCTIC_FUR_BLOCK.get()
		);

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
			TFBlocks.NAGASTONE.get(),
			TFBlocks.NAGASTONE_HEAD.get(),
			TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.TROPHY_PEDESTAL.get(),
			TFBlocks.AURORA_PILLAR.get(),
			TFBlocks.AURORA_SLAB.get(),
			TFBlocks.UNDERBRICK.get(),
			TFBlocks.MOSSY_UNDERBRICK.get(),
			TFBlocks.CRACKED_UNDERBRICK.get(),
			TFBlocks.UNDERBRICK_FLOOR.get(),
			TFBlocks.TROLLSTEINN.get(),
			TFBlocks.GIANT_LEAVES.get(),
			TFBlocks.GIANT_OBSIDIAN.get(),
			TFBlocks.GIANT_COBBLESTONE.get(),
			TFBlocks.GIANT_LOG.get(),
			TFBlocks.CINDER_FURNACE.get(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.get(),
			//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.get(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
			//TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(),
			//TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(),
			//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.get(),
			TFBlocks.KNIGHTMETAL_BLOCK.get(),
			TFBlocks.IRONWOOD_BLOCK.get(),
			TFBlocks.FIERY_BLOCK.get(),
			TFBlocks.CARMINITE_BLOCK.get(),
			TFBlocks.SPIRAL_BRICKS.get(),
			TFBlocks.ETCHED_NAGASTONE.get(),
			TFBlocks.NAGASTONE_PILLAR.get(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.get(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.IRON_LADDER.get(),
			TFBlocks.TWISTED_STONE.get(),
			TFBlocks.TWISTED_STONE_PILLAR.get(),
			TFBlocks.SKULL_CHEST.get(),
			TFBlocks.KEEPSAKE_CASKET.get(),
			TFBlocks.BOLD_STONE_PILLAR.get(),
			TFBlocks.TERRORCOTTA_CURVES.get(),
			TFBlocks.TERRORCOTTA_LINES.get(),
			TFBlocks.TERRORCOTTA_ARCS.get(),
			TFBlocks.SINISTER_SPAWNER.get()
		).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK);

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
			TFBlocks.SMOKER.get(),
			TFBlocks.FIRE_JET.get(),
			TFBlocks.UBEROUS_SOIL.get()
		);

		this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(
			TFBlocks.NAGASTONE.get(),
			TFBlocks.NAGASTONE_HEAD.get(),
			TFBlocks.ETCHED_NAGASTONE.get(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.get(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
			TFBlocks.NAGASTONE_PILLAR.get(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.get(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.get(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.SPIRAL_BRICKS.get(),
			TFBlocks.TWISTED_STONE.get(),
			TFBlocks.TWISTED_STONE_PILLAR.get(),
			TFBlocks.BOLD_STONE_PILLAR.get(),
			TFBlocks.TERRORCOTTA_CURVES.get(),
			TFBlocks.TERRORCOTTA_LINES.get(),
			TFBlocks.TERRORCOTTA_ARCS.get(),
			TFBlocks.AURORA_PILLAR.get(),
			TFBlocks.AURORA_SLAB.get(),
			TFBlocks.TROLLSTEINN.get()
		);

		this.tag(BlockTags.NEEDS_STONE_TOOL).add(
			TFBlocks.UNDERBRICK.get(),
			TFBlocks.CRACKED_UNDERBRICK.get(),
			TFBlocks.MOSSY_UNDERBRICK.get(),
			TFBlocks.UNDERBRICK_FLOOR.get(),
			TFBlocks.IRON_LADDER.get()
		);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
			TFBlocks.FIERY_BLOCK.get(),
			TFBlocks.KNIGHTMETAL_BLOCK.get()
		);

		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TFBlocks.AURORA_BLOCK.get()).addTags(TFBlockTags.CASTLE_BLOCKS, TFBlockTags.MAZESTONE, TFBlockTags.DEADROCK);

		this.tag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON).add(TFBlocks.UBEROUS_SOIL.get());
		this.tag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON).add(TFBlocks.UBEROUS_SOIL.get());

		this.tag(BlockTags.MOSS_REPLACEABLE).add(TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.TROLLSTEINN.get());

		this.tag(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.get());

		this.tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.get(), TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get(),
			TFBlocks.MINING_LOG_CORE.get(), TFBlocks.SORTING_LOG_CORE.get(),
			TFBlocks.ANTIBUILDER.get(), TFBlocks.BUILT_BLOCK.get(),
			TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get(),
			TFBlocks.REACTOR_DEBRIS.get(), TFBlocks.LOCKED_VANISHING_BLOCK.get(), TFBlocks.VANISHING_BLOCK.get(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get(), TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.BEANSTALK_GROWER.get(), TFBlocks.GIANT_COBBLESTONE.get(),
			TFBlocks.GIANT_LOG.get(), TFBlocks.GIANT_LEAVES.get(),
			TFBlocks.GIANT_OBSIDIAN.get(), TFBlocks.BROWN_THORNS.get(),
			TFBlocks.GREEN_THORNS.get(), TFBlocks.BURNT_THORNS.get(),
			TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(),
			TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(),
			TFBlocks.VIOLET_FORCE_FIELD.get(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
			TFBlocks.NAGA_BOSS_SPAWNER.get(), TFBlocks.LICH_BOSS_SPAWNER.get(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), TFBlocks.HYDRA_BOSS_SPAWNER.get(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get());

		this.tag(TFBlockTags.SUPPORTS_STALAGMITES).addTag(TFBlockTags.DEADROCK).add(Blocks.PACKED_ICE);

		this.tag(TFBlockTags.CARVER_REPLACEABLES).addTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(Blocks.SNOW_BLOCK);

		this.tag(TFBlockTags.INCORRECT_FOR_IRONWOOD_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_FIERY_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_STEELEAF_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_KNIGHTMETAL_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GIANT_TOOL).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_ICE_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GLASS_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);

		this.tag(Tags.Blocks.GLASS_BLOCKS).add(TFBlocks.AURORALIZED_GLASS.get());
		this.tag(Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES).add(TFBlocks.UNCRAFTING_TABLE.get());
		this.tag(Tags.Blocks.ROPES).add(TFBlocks.ROPE.get());

		this.tag(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE,
			BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);

		this.tag(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK, BlockTags.WITHER_IMMUNE)
			.add(TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get(), TFBlocks.MINING_LOG_CORE.get(), TFBlocks.SORTING_LOG_CORE.get())
			.add(TFBlocks.GIANT_OBSIDIAN.get());

		this.tag(TFBlockTags.SMALL_LAKES_DONT_REPLACE).addTags(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LOGS, BlockTags.LEAVES)
			.add(TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(), Blocks.MUSHROOM_STEM);

		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
			.add(TFBlocks.HUGE_LILY_PAD.get());

		this.tag(BlockTags.SWORD_EFFICIENT)
			.add(TFBlocks.HUGE_LILY_PAD.get());

		this.tag(Tags.Blocks.BOOKSHELVES)
			.add(TFBlocks.CANOPY_BOOKSHELF.get());

		this.tag(BlockTags.WOOL_CARPETS)
			.add(TFBlocks.CORONATION_CARPET.get());

		this.tag(BlockTags.FIRE)
			.add(TFBlocks.OMINOUS_FIRE.get());
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}
