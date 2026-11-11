package twilightforest.datagen.data.tags;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

		new BlockItemTagGenerator() {
			@Override
			protected TagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return BlockTagGenerator.this.tag(blockTag);
			}
		}.run();

		this.tag(BlockTags.SLABS).add(TFBlocks.AURORA_SLAB.getKey());

		this.tag(BlockTags.STAIRS).add(
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

		this.tag(BlockTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.getKey());

		this.tag(BlockTags.FLOWER_POTS).add(
			TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.getKey(),
			TFBlocks.POTTED_CANOPY_SAPLING.getKey(),
			TFBlocks.POTTED_MANGROVE_SAPLING.getKey(),
			TFBlocks.POTTED_DARKWOOD_SAPLING.getKey(),
			TFBlocks.POTTED_RAINBOW_OAK_SAPLING.getKey(),
			TFBlocks.POTTED_HOLLOW_OAK_SAPLING.getKey(),
			TFBlocks.POTTED_TIME_SAPLING.getKey(),
			TFBlocks.POTTED_TRANSFORMATION_SAPLING.getKey(),
			TFBlocks.POTTED_MINING_SAPLING.getKey(),
			TFBlocks.POTTED_SORTING_SAPLING.getKey(),
			TFBlocks.POTTED_MAYAPPLE.getKey(),
			TFBlocks.POTTED_FIDDLEHEAD.getKey(),
			TFBlocks.POTTED_MUSHGLOOM.getKey(),
			TFBlocks.POTTED_THORN.getKey(),
			TFBlocks.POTTED_GREEN_THORN.getKey(),
			TFBlocks.POTTED_DEAD_THORN.getKey());

		this.tag(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.getKey(),
			TFBlocks.CANOPY_WALL_SIGN.getKey(),
			TFBlocks.MANGROVE_WALL_SIGN.getKey(),
			TFBlocks.DARK_WALL_SIGN.getKey(),
			TFBlocks.TIME_WALL_SIGN.getKey(),
			TFBlocks.TRANSFORMATION_WALL_SIGN.getKey(),
			TFBlocks.MINING_WALL_SIGN.getKey(),
			TFBlocks.SORTING_WALL_SIGN.getKey());

		this.tag(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.getKey(),
			TFBlocks.CANOPY_WALL_HANGING_SIGN.getKey(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.getKey(),
			TFBlocks.DARK_WALL_HANGING_SIGN.getKey(),
			TFBlocks.TIME_WALL_HANGING_SIGN.getKey(),
			TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.getKey(),
			TFBlocks.MINING_WALL_HANGING_SIGN.getKey(),
			TFBlocks.SORTING_WALL_HANGING_SIGN.getKey());

		this.tag(BlockTags.DRAGON_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.getKey(), TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey());
		this.tag(BlockTags.WITHER_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey());
		this.tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.getKey());
		this.tag(BlockTags.FIRE).add(TFBlocks.OMINOUS_FIRE.getKey());
		this.tag(BlockTags.BEACON_BASE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);
		this.tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.getKey(), TFBlocks.ROPE.getKey(), TFBlocks.ROOT_STRAND.getKey()).addTag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE);
		this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.getKey());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).addTag(TFBlockTags.TF_CHESTS);
		this.tag(BlockTags.EDIBLE_FOR_SHEEP).add(TFBlocks.MAYAPPLE.getKey(), TFBlocks.FIDDLEHEAD.getKey());
		this.tag(BlockTags.CAN_GLIDE_THROUGH).add(TFBlocks.ROOT_STRAND.getKey());
		this.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(TFBlocks.TROLLSTEINN.getKey());
		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(TFBlocks.HUGE_LILY_PAD.getKey(), TFBlocks.FALLEN_LEAVES.getKey(), TFBlocks.ROOT_STRAND.getKey());
		this.tag(BlockTags.DRIPSTONE_REPLACEABLE).add(TFBlocks.TROLLSTEINN.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());
		this.tag(BlockTags.MOSS_REPLACEABLE).add(TFBlocks.TROLLSTEINN.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());
		this.tag(BlockTags.AZALEA_ROOT_REPLACEABLE).add(TFBlocks.TROLLSTEINN.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());
		this.tag(BlockTags.FOREST_ROCK_CAN_PLACE_ON).add(TFBlocks.TROLLSTEINN.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());
		this.tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());
		this.toolTierTags();
		this.tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.getKey(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.SINISTER_SPAWNER.getKey());
		this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.getKey());
		this.tag(BlockTags.SCULK_REPLACEABLE).add(TFBlocks.TROLLSTEINN.getKey());
		this.tag(BlockTags.REPLACEABLE_BY_TREES).add(
			TFBlocks.HARDENED_DARK_LEAVES.getKey(),
			TFBlocks.MAYAPPLE.getKey(),
			TFBlocks.FIDDLEHEAD.getKey(),
			TFBlocks.MOSS_PATCH.getKey(),
			TFBlocks.CLOVER_PATCH.getKey(),
			TFBlocks.MUSHGLOOM.getKey(),
			TFBlocks.FIREFLY.getKey(),
			TFBlocks.CICADA.getKey(),
			TFBlocks.FALLEN_LEAVES.getKey(),
			TFBlocks.TORCHBERRY_PLANT.getKey(),
			TFBlocks.ROOT_STRAND.getKey(),
			TFBlocks.ROOT_BLOCK.getKey(),
			TFBlocks.RASPBERRY_BUSH.getKey(),
			TFBlocks.BLUEBERRY_BUSH.getKey(),
			TFBlocks.BLACKBERRY_BUSH.getKey(),
			TFBlocks.MALOBERRY_BUSH.getKey()
		);
		this.tag(BlockTags.REPLACEABLE_BY_MUSHROOMS).add(TFBlocks.MAYAPPLE.getKey(), TFBlocks.FIDDLEHEAD.getKey(), TFBlocks.FALLEN_LEAVES.getKey());
		this.tag(BlockTags.PREVENTS_NEARBY_LEAF_DECAY).add(TFBlocks.HUGE_STALK.getKey(), TFBlocks.BROWN_THORNS.getKey(), TFBlocks.GREEN_THORNS.getKey());
		this.tag(BlockTags.SUPPORTS_CROPS).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(BlockTags.SUPPORTS_BIG_DRIPLEAF).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.getKey());
		this.tag(BlockTags.REPLACEABLE).addAll(TFBlocks.BLOCKS.getEntries().stream().map(Holder::value).filter(b -> b.defaultBlockState().canBeReplaced()).map(b -> b.builtInRegistryHolder().key()).toList());
		this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.getKey());

		this.tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.getKey(), TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(),
			TFBlocks.MINING_LOG_CORE.getKey(), TFBlocks.SORTING_LOG_CORE.getKey(),
			TFBlocks.ANTIBUILDER.getKey(), TFBlocks.BUILT_BLOCK.getKey(),
			TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey(),
			TFBlocks.REACTOR_DEBRIS.getKey(), TFBlocks.LOCKED_VANISHING_BLOCK.getKey(), TFBlocks.VANISHING_BLOCK.getKey(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.getKey(), TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.BEANSTALK_GROWER.getKey(), TFBlocks.GIANT_COBBLESTONE.getKey(),
			TFBlocks.GIANT_LOG.getKey(), TFBlocks.GIANT_LEAVES.getKey(),
			TFBlocks.GIANT_OBSIDIAN.getKey(), TFBlocks.BROWN_THORNS.getKey(),
			TFBlocks.GREEN_THORNS.getKey(), TFBlocks.BURNT_THORNS.getKey(),
			TFBlocks.PINK_FORCE_FIELD.getKey(), TFBlocks.ORANGE_FORCE_FIELD.getKey(),
			TFBlocks.GREEN_FORCE_FIELD.getKey(), TFBlocks.BLUE_FORCE_FIELD.getKey(),
			TFBlocks.VIOLET_FORCE_FIELD.getKey(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.getKey(),
			TFBlocks.NAGA_BOSS_SPAWNER.getKey(), TFBlocks.LICH_BOSS_SPAWNER.getKey(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.getKey(), TFBlocks.HYDRA_BOSS_SPAWNER.getKey(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.getKey(), TFBlocks.UR_GHAST_BOSS_SPAWNER.getKey(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.getKey(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.getKey());
		this.tag(Tags.Blocks.SKULLS).add(
			TFBlocks.SKELETON_SKULL_CANDLE.getKey(),
			TFBlocks.SKELETON_WALL_SKULL_CANDLE.getKey(),
			TFBlocks.WITHER_SKELE_SKULL_CANDLE.getKey(),
			TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.getKey(),
			TFBlocks.PLAYER_SKULL_CANDLE.getKey(),
			TFBlocks.PLAYER_WALL_SKULL_CANDLE.getKey(),
			TFBlocks.ZOMBIE_SKULL_CANDLE.getKey(),
			TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.getKey(),
			TFBlocks.CREEPER_SKULL_CANDLE.getKey(),
			TFBlocks.CREEPER_WALL_SKULL_CANDLE.getKey(),
			TFBlocks.PIGLIN_SKULL_CANDLE.getKey(),
			TFBlocks.PIGLIN_WALL_SKULL_CANDLE.getKey());

		this.tag(TFBlockTags.HOLLOW_LOGS_HORIZONTAL).add(
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_VERTICAL).add(
			TFBlocks.HOLLOW_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE).add(
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS).addTags(TFBlockTags.HOLLOW_LOGS_HORIZONTAL, TFBlockTags.HOLLOW_LOGS_VERTICAL, TFBlockTags.HOLLOW_LOGS_CLIMBABLE);



		this.tag(TFBlockTags.MAZESTONE).add(
			TFBlocks.MAZESTONE.getKey(), TFBlocks.MAZESTONE_BRICK.getKey(),
			TFBlocks.CRACKED_MAZESTONE.getKey(), TFBlocks.MOSSY_MAZESTONE.getKey(),
			TFBlocks.CUT_MAZESTONE.getKey(), TFBlocks.DECORATIVE_MAZESTONE.getKey(),
			TFBlocks.MAZESTONE_MOSAIC.getKey(), TFBlocks.MAZESTONE_BORDER.getKey());

		this.tag(TFBlockTags.CASTLE_BLOCKS).add(
			TFBlocks.CASTLE_BRICK.getKey(), TFBlocks.WORN_CASTLE_BRICK.getKey(),
			TFBlocks.CRACKED_CASTLE_BRICK.getKey(), TFBlocks.MOSSY_CASTLE_BRICK.getKey(),
			TFBlocks.CASTLE_ROOF_TILE.getKey(), TFBlocks.THICK_CASTLE_BRICK.getKey(),
			TFBlocks.BOLD_CASTLE_BRICK_TILE.getKey(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.getKey(),
			TFBlocks.ENCASED_CASTLE_BRICK_TILE.getKey(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.getKey(),
			TFBlocks.CASTLE_BRICK_STAIRS.getKey(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.getKey(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.getKey(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.getKey(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.getKey(),
			TFBlocks.PINK_CASTLE_DOOR.getKey(), TFBlocks.YELLOW_CASTLE_DOOR.getKey(),
			TFBlocks.BLUE_CASTLE_DOOR.getKey(), TFBlocks.VIOLET_CASTLE_DOOR.getKey()
		);

		this.tag(TFBlockTags.MAZEBREAKER_ACCELERATED).addTag(TFBlockTags.MAZESTONE).addTag(TFBlockTags.CASTLE_BLOCKS);
		this.tag(TFBlockTags.PORTAL_POOL).add(BlockIds.WATER);

		//GUIDELINE: The portal frame requires dirt. The blocks we use are all essentially that with their nature unchanged (e.g unlike Mud) and remain similar-looking.
		this.tag(TFBlockTags.PORTAL_EDGE).add(BlockItemIds.FARMLAND.block()).addTags(BlockTags.DIRT, BlockTags.GRASS_BLOCKS);

		//GUIDELINE: Organic vegetation. No vegetation from alien, hostile worlds like the Nether or End.
		//Vegetation from dimensions more similar to the Overworld (e.g. The Aether, Tropics, etc.) would logically work.
		//Must be placed on top of the frame blocks and/or supported by them (e.g. no Cocoa Beans that float above it/are grown from another block).
		this.tag(TFBlockTags.PORTAL_DECO).add(
				BlockItemIds.BAMBOO.block(), BlockIds.BAMBOO_SAPLING,
				BlockItemIds.SHORT_GRASS.block(), BlockItemIds.TALL_GRASS.block(),
				BlockItemIds.FERN.block(), BlockItemIds.LARGE_FERN.block(),
				BlockItemIds.DEAD_BUSH.block(), BlockItemIds.BUSH.block(), BlockItemIds.FIREFLY_BUSH.block(),
				BlockItemIds.SUGAR_CANE.block(),
				BlockItemIds.SWEET_BERRY_CROP.block(),
				BlockItemIds.GLOW_LICHEN.block(),
				BlockItemIds.RED_MUSHROOM.block(), BlockItemIds.BROWN_MUSHROOM.block(),
				BlockIds.ATTACHED_MELON_STEM, BlockIds.ATTACHED_PUMPKIN_STEM,
				BlockItemIds.PINK_PETALS.block(), BlockItemIds.WILDFLOWERS.block(),
				BlockItemIds.LEAF_LITTER.block(),
				BlockItemIds.SHORT_DRY_GRASS.block(), BlockItemIds.TALL_DRY_GRASS.block(),
				BlockItemIds.BIG_DRIPLEAF.block(), BlockIds.BIG_DRIPLEAF_STEM,
				BlockItemIds.SMALL_DRIPLEAF.block(),
				TFBlocks.FIDDLEHEAD.getKey(),
				TFBlocks.MOSS_PATCH.getKey(),
				TFBlocks.MAYAPPLE.getKey(),
				TFBlocks.CLOVER_PATCH.getKey(),
				TFBlocks.MUSHGLOOM.getKey(),
				TFBlocks.FALLEN_LEAVES.getKey(),
				TFBlocks.GIANT_LEAVES.getKey(),
				TFBlocks.HARDENED_DARK_LEAVES.getKey(),
				TFBlocks.RASPBERRY_BUSH.getKey(),
				TFBlocks.BLACKBERRY_BUSH.getKey(),
				TFBlocks.BLUEBERRY_BUSH.getKey(),
				TFBlocks.MALOBERRY_BUSH.getKey(),
				TFBlocks.COPPER_OREBERRY_BUSH.getKey(),
				TFBlocks.IRON_OREBERRY_BUSH.getKey(),
				TFBlocks.GOLD_OREBERRY_BUSH.getKey(),
				TFBlocks.ESSENCE_OREBERRY_BUSH.getKey(),
				BlockItemIds.ACACIA_SAPLING.block(),
				BlockItemIds.BIRCH_SAPLING.block(),
				BlockItemIds.CHERRY_SAPLING.block(),
				BlockItemIds.DARK_OAK_SAPLING.block(),
				BlockItemIds.JUNGLE_SAPLING.block(),
				BlockItemIds.OAK_SAPLING.block(),
				BlockItemIds.PALE_OAK_SAPLING.block(),
				BlockItemIds.SPRUCE_SAPLING.block(),
				TFBlocks.TWILIGHT_OAK_SAPLING.getKey(),
				TFBlocks.CANOPY_SAPLING.getKey(),
				TFBlocks.MANGROVE_SAPLING.getKey(),
				TFBlocks.DARKWOOD_SAPLING.getKey(),
				TFBlocks.TIME_SAPLING.getKey(),
				TFBlocks.TRANSFORMATION_SAPLING.getKey(),
				TFBlocks.MINING_SAPLING.getKey(),
				TFBlocks.SORTING_SAPLING.getKey(),
				TFBlocks.HOLLOW_OAK_SAPLING.getKey(),
				TFBlocks.RAINBOW_OAK_SAPLING.getKey()
			).addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockTags.CROPS);

		this.tag(TFBlockTags.GENERATED_PORTAL_DECO)
			.add(BlockItemIds.BROWN_MUSHROOM.block(), BlockItemIds.RED_MUSHROOM.block(),
				BlockItemIds.SHORT_GRASS.block(), BlockItemIds.FERN.block(),
				BlockItemIds.BLUE_ORCHID.block(), BlockItemIds.AZURE_BLUET.block(),
				BlockItemIds.LILY_OF_THE_VALLEY.block(), BlockItemIds.OXEYE_DAISY.block(),
				BlockItemIds.ALLIUM.block(), BlockItemIds.CORNFLOWER.block(),
				BlockItemIds.WHITE_TULIP.block(), BlockItemIds.PINK_TULIP.block(),
				BlockItemIds.ORANGE_TULIP.block(), BlockItemIds.RED_TULIP.block(),
				TFBlocks.MUSHGLOOM.getKey(),
				TFBlocks.MAYAPPLE.getKey(),
				TFBlocks.FIDDLEHEAD.getKey());

		this.tag(TFBlockTags.DARK_TOWER_ALLOWED_POTS)
			.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.getKey(), TFBlocks.POTTED_CANOPY_SAPLING.getKey(), TFBlocks.POTTED_MANGROVE_SAPLING.getKey(),
				TFBlocks.POTTED_DARKWOOD_SAPLING.getKey(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.getKey(), TFBlocks.POTTED_MAYAPPLE.getKey(),
				TFBlocks.POTTED_FIDDLEHEAD.getKey(), TFBlocks.POTTED_MUSHGLOOM.getKey())
			.add(BlockItemIds.FLOWER_POT.block(), BlockIds.POTTED_POPPY, BlockIds.POTTED_BLUE_ORCHID, BlockIds.POTTED_ALLIUM, BlockIds.POTTED_AZURE_BLUET,
				BlockIds.POTTED_RED_TULIP, BlockIds.POTTED_ORANGE_TULIP, BlockIds.POTTED_WHITE_TULIP, BlockIds.POTTED_PINK_TULIP,
				BlockIds.POTTED_OXEYE_DAISY, BlockIds.POTTED_DANDELION, BlockIds.POTTED_OAK_SAPLING, BlockIds.POTTED_SPRUCE_SAPLING,
				BlockIds.POTTED_BIRCH_SAPLING, BlockIds.POTTED_JUNGLE_SAPLING, BlockIds.POTTED_ACACIA_SAPLING, BlockIds.POTTED_DARK_OAK_SAPLING,
				BlockIds.POTTED_RED_MUSHROOM, BlockIds.POTTED_BROWN_MUSHROOM, BlockIds.POTTED_DEAD_BUSH, BlockIds.POTTED_FERN,
				BlockIds.POTTED_CACTUS, BlockIds.POTTED_CORNFLOWER, BlockIds.POTTED_LILY_OF_THE_VALLEY, BlockIds.POTTED_WITHER_ROSE,
				BlockIds.POTTED_BAMBOO, BlockIds.POTTED_CRIMSON_FUNGUS, BlockIds.POTTED_WARPED_FUNGUS, BlockIds.POTTED_CRIMSON_ROOTS,
				BlockIds.POTTED_WARPED_ROOTS, BlockIds.POTTED_AZALEA_BUSH, BlockIds.POTTED_FLOWERING_AZALEA_BUSH, BlockIds.POTTED_MANGROVE_PROPAGULE,
				BlockIds.POTTED_CHERRY_SAPLING, BlockIds.POTTED_TORCHFLOWER);

		this.tag(TFBlockTags.TROPHY_PEDESTAL_ACTIVATION_BLOCKS)
			.add(TFBlocks.NAGA_TROPHY.getKey(), TFBlocks.NAGA_WALL_TROPHY.getKey())
			.add(TFBlocks.LICH_TROPHY.getKey(), TFBlocks.LICH_WALL_TROPHY.getKey())
			.add(TFBlocks.MINOSHROOM_TROPHY.getKey(), TFBlocks.MINOSHROOM_WALL_TROPHY.getKey())
			.add(TFBlocks.HYDRA_TROPHY.getKey(), TFBlocks.HYDRA_WALL_TROPHY.getKey())
			.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.getKey(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.getKey())
			.add(TFBlocks.UR_GHAST_TROPHY.getKey(), TFBlocks.UR_GHAST_WALL_TROPHY.getKey())
			.add(TFBlocks.ALPHA_YETI_TROPHY.getKey(), TFBlocks.ALPHA_YETI_WALL_TROPHY.getKey())
			.add(TFBlocks.SNOW_QUEEN_TROPHY.getKey(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.getKey())
			.add(TFBlocks.QUEST_RAM_TROPHY.getKey(), TFBlocks.QUEST_RAM_WALL_TROPHY.getKey());

		this.tag(TFBlockTags.FIRE_JET_FUEL).add(BlockIds.LAVA);
		this.tag(TFBlockTags.ICE_BOMB_REPLACEABLES)
			.add(TFBlocks.MAYAPPLE.getKey(), TFBlocks.FIDDLEHEAD.getKey(), BlockItemIds.SHORT_GRASS.block(), BlockItemIds.TALL_GRASS.block(), BlockItemIds.FERN.block(), BlockItemIds.LARGE_FERN.block())
			.addTag(BlockTags.FLOWERS);

		this.tag(TFBlockTags.PLANTS_HANG_ON)
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
			.add(BlockItemIds.MOSS_BLOCK.block(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());

		this.tag(TFBlockTags.OREBERRY_BUSHES_SURVIVE)
				.addTags(Tags.Blocks.STONES)
				.addTags(BlockTags.STONE_BRICKS)
				.addTags(Tags.Blocks.ORES_IN_GROUND_STONE)
				.addTags(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
				.addTags(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
				.addTags(Tags.Blocks.COBBLESTONES)
				.addTags(Tags.Blocks.NETHERRACKS)
				.add(TFBlocks.GIANT_COBBLESTONE.getKey())
				.add(BlockItemIds.POLISHED_ANDESITE.block())
				.add(BlockItemIds.POLISHED_DIORITE.block())
				.add(BlockItemIds.POLISHED_GRANITE.block())
				.add(BlockItemIds.SMOOTH_STONE.block())
				.add(BlockItemIds.INFESTED_CHISELED_STONE_BRICKS.block())
				.add(BlockItemIds.INFESTED_CRACKED_STONE_BRICKS.block())
				.add(BlockItemIds.INFESTED_MOSSY_STONE_BRICKS.block())
				.add(BlockItemIds.INFESTED_STONE_BRICKS.block());

		this.tag(TFBlockTags.TF_BERRY_BUSHES_REPLACE)
				.addTags(BlockTags.REPLACEABLE)
				.addTags(BlockTags.FLOWERS)
				.add(TFBlocks.MAYAPPLE.getKey());

		this.tag(TFBlockTags.TF_BERRY_BUSHES_SURVIVE)
				.addTags(BlockTags.SUBSTRATE_OVERWORLD)
				.add(BlockItemIds.SNOW_BLOCK.block());

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_SURVIVE)
				.addTag(Tags.Blocks.NETHERRACKS)
				.addTag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
				.add(BlockItemIds.BLACKSTONE.block())
				.add(BlockItemIds.SOUL_SAND.block())
				.add(BlockItemIds.SOUL_SOIL.block());

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_DIE)
				.addTags(BlockTags.NYLIUM);

		this.tag(TFBlockTags.COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
			TFBlocks.NAGA_BOSS_SPAWNER.getKey(),
			TFBlocks.LICH_BOSS_SPAWNER.getKey(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.getKey(),
			TFBlocks.HYDRA_BOSS_SPAWNER.getKey(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.getKey(),
			TFBlocks.UR_GHAST_BOSS_SPAWNER.getKey(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.getKey(),
			TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.getKey(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.getKey(),
			TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.getKey(),
			TFBlocks.LOCKED_VANISHING_BLOCK.getKey(),
			TFBlocks.PINK_FORCE_FIELD.getKey(),
			TFBlocks.ORANGE_FORCE_FIELD.getKey(),
			TFBlocks.GREEN_FORCE_FIELD.getKey(),
			TFBlocks.BLUE_FORCE_FIELD.getKey(),
			TFBlocks.VIOLET_FORCE_FIELD.getKey(),
			TFBlocks.SKULL_CHEST.getKey(),
			TFBlocks.KEEPSAKE_CASKET.getKey(),
			TFBlocks.TROPHY_PEDESTAL.getKey()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
			BlockItemIds.BARRIER.block(),
			BlockItemIds.BEDROCK.block(),
			BlockIds.END_PORTAL,
			BlockItemIds.END_PORTAL_FRAME.block(),
			BlockIds.END_GATEWAY,
			BlockItemIds.COMMAND_BLOCK.block(),
			BlockItemIds.REPEATING_COMMAND_BLOCK.block(),
			BlockItemIds.CHAIN_COMMAND_BLOCK.block(),
			BlockItemIds.STRUCTURE_BLOCK.block(),
			BlockItemIds.JIGSAW.block(),
			BlockIds.MOVING_PISTON,
			BlockItemIds.LIGHT.block(),
			BlockItemIds.REINFORCED_DEEPSLATE.block()
		);

		this.tag(TFBlockTags.CARMINITE_REACTOR_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS);
		this.tag(TFBlockTags.CARMINITE_REACTOR_ORES).add(BlockItemIds.NETHER_QUARTZ_ORE.block(), BlockItemIds.NETHER_GOLD_ORE.block());
		this.tag(TFBlockTags.DEADROCK).add(TFBlocks.DEADROCK.getKey(), TFBlocks.CRACKED_DEADROCK.getKey(), TFBlocks.WEATHERED_DEADROCK.getKey());
		this.tag(TFBlockTags.ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
			.add(BlockIds.NETHER_PORTAL)
			.addTag(TFBlockTags.DEADROCK)
			.add(TFBlocks.CASTLE_BRICK.getKey(), TFBlocks.THICK_CASTLE_BRICK.getKey(), TFBlocks.MOSSY_CASTLE_BRICK.getKey(), TFBlocks.CASTLE_ROOF_TILE.getKey(), TFBlocks.WORN_CASTLE_BRICK.getKey())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.getKey(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.getKey(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.getKey(), TFBlocks.PINK_CASTLE_RUNE_BRICK.getKey())
			.add(TFBlocks.PINK_FORCE_FIELD.getKey(), TFBlocks.ORANGE_FORCE_FIELD.getKey(), TFBlocks.GREEN_FORCE_FIELD.getKey(), TFBlocks.BLUE_FORCE_FIELD.getKey(), TFBlocks.VIOLET_FORCE_FIELD.getKey())
			.add(TFBlocks.BROWN_THORNS.getKey(), TFBlocks.GREEN_THORNS.getKey());

		this.tag(TFBlockTags.ANTIBUILDER_IGNORES).add(
			BlockItemIds.REDSTONE_LAMP.block(),
			BlockItemIds.TNT.block(),
			BlockIds.WATER,
			TFBlocks.ANTIBUILDER.getKey(),
			TFBlocks.CARMINITE_BUILDER.getKey(),
			TFBlocks.BUILT_BLOCK.getKey(),
			TFBlocks.REACTOR_DEBRIS.getKey(),
			TFBlocks.CARMINITE_REACTOR.getKey(),
			TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.GHAST_TRAP.getKey(),
			TFBlocks.FAKE_DIAMOND.getKey(),
			TFBlocks.FAKE_GOLD.getKey()
		).addTag(TFBlockTags.COMMON_PROTECTIONS)/*.addOptional(Identifier.parse("gravestone:gravestone"))*/; //TODO: Restore

		this.tag(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS).add(BlockItemIds.LEVER.block()).add(TFBlocks.ANTIBUILDER.getKey()).addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS);

		// TODO add more grave mods to this list
		this.tag(TFBlockTags.PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.getKey())
			.add(TFBlocks.KEEPSAKE_CASKET.getKey())
			/*.addOptional(Identifier.fromNamespaceAndPath("gravestone", "gravestone"))*/;

		this.tag(TFBlockTags.CANNOT_TROLL_CAVE_HOLLOW)
			.add(BlockItemIds.RED_MUSHROOM_BLOCK.block())
			.add(BlockItemIds.BROWN_MUSHROOM_BLOCK.block())
			.add(TFBlocks.HUGE_MUSHGLOOM.getKey());

		this.tag(TFBlockTags.ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
			BlockTags.SUBSTRATE_OVERWORLD,
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

		this.tag(TFBlockTags.ORE_MAGNET_IGNORE).addTag(Tags.Blocks.ORES_COAL);
		this.tag(TFBlockTags.MINING_CORE_EXCLUDED).addTag(Tags.Blocks.ORES_COAL);
		this.tag(TFBlockTags.ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.getKey());
		this.tag(TFBlockTags.ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.getKey());
		this.tag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.getKey(),
			TFBlocks.CANOPY_CHEST.getKey(),
			TFBlocks.MANGROVE_CHEST.getKey(),
			TFBlocks.DARK_CHEST.getKey(),
			TFBlocks.TIME_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_CHEST.getKey(),
			TFBlocks.MINING_CHEST.getKey(),
			TFBlocks.SORTING_CHEST.getKey());

		// For anything that permits replacement during Worldgen
		this.tag(TFBlockTags.WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES);
		this.tag(TFBlockTags.ROOT_TRACE_SKIP).addTag(BlockTags.LOGS).add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.TIME_WOOD.getKey()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);
		this.tag(TFBlockTags.DRUID_PROJECTILE_REPLACEABLE).addTags(BlockTags.LEAVES, BlockTags.LOGS, BlockTags.PLANKS, BlockTags.OVERWORLD_CARVER_REPLACEABLES, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.REPLACEABLE_BY_TREES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SCULK_REPLACEABLE, Tags.Blocks.ORES);
		this.tag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE).addTag(BlockTags.SUBSTRATE_OVERWORLD).add(BlockItemIds.MYCELIUM.block()).add(BlockItemIds.PODZOL.block()).add(BlockItemIds.CRIMSON_NYLIUM.block()).add(BlockItemIds.WARPED_NYLIUM.block());
		this.tag(TFBlockTags.TIME_CORE_EXCLUDED).add(BlockIds.NETHER_PORTAL);
		this.tag(TFBlockTags.ORE_METER_TARGETABLE)
			.addTag(Tags.Blocks.ORES)
			.addTag(BlockTags.BASE_STONE_OVERWORLD)
			.addTag(BlockTags.BASE_STONE_NETHER)
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
			.addTag(Tags.Blocks.SANDS)
			.addTag(Tags.Blocks.SANDSTONE_BLOCKS)
			.addTag(BlockTags.TERRACOTTA)
			.addTag(Tags.Blocks.GRAVELS)
			.addTag(BlockTags.NYLIUM)
			.addTag(TFBlockTags.ROOT_ORES)
			.add(BlockItemIds.BUDDING_AMETHYST.block())
			.add(BlockItemIds.CALCITE.block())
			.add(BlockItemIds.SOUL_SAND.block())
			.add(BlockItemIds.SOUL_SOIL.block());

		this.tag(TFBlockTags.PENGUINS_SPAWNABLE_ON).addTag(BlockTags.ICE);
		this.tag(TFBlockTags.GIANTS_SPAWNABLE_ON).addTag(TFBlockTags.CLOUDS);
		this.tag(TFBlockTags.SUPPORTS_STALAGMITES).addTag(TFBlockTags.DEADROCK).add(BlockItemIds.PACKED_ICE.block());
		this.tag(TFBlockTags.CARVER_REPLACEABLES).addTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(BlockItemIds.SNOW_BLOCK.block());
		this.tag(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);
		this.tag(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK, BlockTags.WITHER_IMMUNE)
			.add(TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(), TFBlocks.MINING_LOG_CORE.getKey(), TFBlocks.SORTING_LOG_CORE.getKey())
			.add(TFBlocks.GIANT_OBSIDIAN.getKey());

		this.tag(TFBlockTags.SMALL_LAKES_DONT_REPLACE).addTags(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LOGS, BlockTags.LEAVES)
			.add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), BlockItemIds.MUSHROOM_STEM.block());
	}

	@SuppressWarnings("unchecked")
	private void toolTierTags() {
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
			TFBlocks.HEDGE.getKey(),
			TFBlocks.ROOT_BLOCK.getKey(),
			TFBlocks.LIVEROOT_BLOCK.getKey(),
			TFBlocks.MANGROVE_ROOT.getKey(),
			TFBlocks.UNCRAFTING_TABLE.getKey(),
			TFBlocks.ENCASED_SMOKER.getKey(),
			TFBlocks.ENCASED_FIRE_JET.getKey(),
			TFBlocks.TIME_LOG_CORE.getKey(),
			TFBlocks.TRANSFORMATION_LOG_CORE.getKey(),
			TFBlocks.MINING_LOG_CORE.getKey(),
			TFBlocks.SORTING_LOG_CORE.getKey(),
			TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.VANISHING_BLOCK.getKey(),
			TFBlocks.ANTIBUILDER.getKey(),
			TFBlocks.CARMINITE_REACTOR.getKey(),
			TFBlocks.CARMINITE_BUILDER.getKey(),
			TFBlocks.GHAST_TRAP.getKey(),
			TFBlocks.HUGE_STALK.getKey(),
			TFBlocks.HUGE_MUSHGLOOM.getKey(),
			TFBlocks.HUGE_MUSHGLOOM_STEM.getKey(),
			TFBlocks.CINDER_LOG.getKey(),
			TFBlocks.CINDER_WOOD.getKey(),
			TFBlocks.IRONWOOD_BLOCK.getKey(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.getKey(),
			TFBlocks.CANOPY_BOOKSHELF.getKey(),
			TFBlocks.TWILIGHT_OAK_CHEST.getKey(),
			TFBlocks.CANOPY_CHEST.getKey(),
			TFBlocks.MANGROVE_CHEST.getKey(),
			TFBlocks.DARK_CHEST.getKey(),
			TFBlocks.TIME_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_CHEST.getKey(),
			TFBlocks.MINING_CHEST.getKey(),
			TFBlocks.SORTING_CHEST.getKey(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getKey(),
			TFBlocks.CANOPY_TRAPPED_CHEST.getKey(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.getKey(),
			TFBlocks.DARK_TRAPPED_CHEST.getKey(),
			TFBlocks.TIME_TRAPPED_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getKey(),
			TFBlocks.MINING_TRAPPED_CHEST.getKey(),
			TFBlocks.SORTING_TRAPPED_CHEST.getKey(),
			TFBlocks.HUGE_LILY_PAD.getKey(),
			TFBlocks.ENCASED_TOWERWOOD.getKey()
		).addTags(TFBlockTags.BANISTERS, TFBlockTags.HOLLOW_LOGS, TFBlockTags.TOWERWOOD, TFBlockTags.DRYING_RACKS);

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
			//vanilla doesnt use the leaves tag
			TFBlocks.TWILIGHT_OAK_LEAVES.getKey(),
			TFBlocks.CANOPY_LEAVES.getKey(),
			TFBlocks.MANGROVE_LEAVES.getKey(),
			TFBlocks.DARK_LEAVES.getKey(),
			TFBlocks.RAINBOW_OAK_LEAVES.getKey(),
			TFBlocks.TIME_LEAVES.getKey(),
			TFBlocks.TRANSFORMATION_LEAVES.getKey(),
			TFBlocks.MINING_LEAVES.getKey(),
			TFBlocks.SORTING_LEAVES.getKey(),
			TFBlocks.THORN_LEAVES.getKey(),
			TFBlocks.THORN_ROSE.getKey(),
			TFBlocks.BEANSTALK_LEAVES.getKey(),
			TFBlocks.STEELEAF_BLOCK.getKey(),
			TFBlocks.ARCTIC_FUR_BLOCK.getKey()
		);

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
			TFBlocks.NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_HEAD.getKey(),
			TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.TROPHY_PEDESTAL.getKey(),
			TFBlocks.AURORA_PILLAR.getKey(),
			TFBlocks.AURORA_SLAB.getKey(),
			TFBlocks.UNDERBRICK.getKey(),
			TFBlocks.MOSSY_UNDERBRICK.getKey(),
			TFBlocks.CRACKED_UNDERBRICK.getKey(),
			TFBlocks.UNDERBRICK_FLOOR.getKey(),
			TFBlocks.TROLLSTEINN.getKey(),
			TFBlocks.GIANT_LEAVES.getKey(),
			TFBlocks.GIANT_OBSIDIAN.getKey(),
			TFBlocks.GIANT_COBBLESTONE.getKey(),
			TFBlocks.GIANT_LOG.getKey(),
			TFBlocks.CINDER_FURNACE.getKey(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.KNIGHTMETAL_BLOCK.getKey(),
			TFBlocks.IRONWOOD_BLOCK.getKey(),
			TFBlocks.FIERY_BLOCK.getKey(),
			TFBlocks.CARMINITE_BLOCK.getKey(),
			TFBlocks.SPIRAL_BRICKS.getKey(),
			TFBlocks.ETCHED_NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_PILLAR.getKey(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.getKey(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.getKey(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.IRON_LADDER.getKey(),
			TFBlocks.CANDELABRA.getKey(),
			TFBlocks.TWISTED_STONE.getKey(),
			TFBlocks.TWISTED_STONE_PILLAR.getKey(),
			TFBlocks.SKULL_CHEST.getKey(),
			TFBlocks.KEEPSAKE_CASKET.getKey(),
			TFBlocks.BOLD_STONE_PILLAR.getKey(),
			TFBlocks.TERRORCOTTA_CURVES.getKey(),
			TFBlocks.TERRORCOTTA_LINES.getKey(),
			TFBlocks.TERRORCOTTA_ARCS.getKey(),
			TFBlocks.SINISTER_SPAWNER.getKey()
		).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK);

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
			TFBlocks.SMOKER.getKey(),
			TFBlocks.FIRE_JET.getKey(),
			TFBlocks.UBEROUS_SOIL.getKey()
		);

		this.tag(BlockTags.SWORD_EFFICIENT).add(TFBlocks.HUGE_LILY_PAD.getKey());

		this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(
			TFBlocks.NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_HEAD.getKey(),
			TFBlocks.ETCHED_NAGASTONE.getKey(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.getKey(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_PILLAR.getKey(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.getKey(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.getKey(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.SPIRAL_BRICKS.getKey(),
			TFBlocks.TWISTED_STONE.getKey(),
			TFBlocks.TWISTED_STONE_PILLAR.getKey(),
			TFBlocks.BOLD_STONE_PILLAR.getKey(),
			TFBlocks.TERRORCOTTA_CURVES.getKey(),
			TFBlocks.TERRORCOTTA_LINES.getKey(),
			TFBlocks.TERRORCOTTA_ARCS.getKey(),
			TFBlocks.AURORA_PILLAR.getKey(),
			TFBlocks.AURORA_SLAB.getKey(),
			TFBlocks.TROLLSTEINN.getKey()
		);

		this.tag(BlockTags.NEEDS_STONE_TOOL).add(
			TFBlocks.UNDERBRICK.getKey(),
			TFBlocks.CRACKED_UNDERBRICK.getKey(),
			TFBlocks.MOSSY_UNDERBRICK.getKey(),
			TFBlocks.UNDERBRICK_FLOOR.getKey(),
			TFBlocks.IRON_LADDER.getKey()
		);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
			TFBlocks.FIERY_BLOCK.getKey(),
			TFBlocks.KNIGHTMETAL_BLOCK.getKey()
		);

		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TFBlocks.AURORA_BLOCK.getKey()).addTags(TFBlockTags.CASTLE_BLOCKS, TFBlockTags.MAZESTONE, TFBlockTags.DEADROCK);

		this.tag(TFBlockTags.INCORRECT_FOR_IRONWOOD_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_FIERY_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_STEELEAF_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_KNIGHTMETAL_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GIANT_TOOL).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_ICE_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GLASS_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}
