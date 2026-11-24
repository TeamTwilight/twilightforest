package twilightforest.datagen.data.tags;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockItemTags;
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

		this.tag(TFBlockTags.LICH_DOES_NOT_TELEPORT_TO).addTag(BlockTags.DANGEROUS_FOR_TELEPORTATION);

		this.tag(BlockTags.FLOWER_POTS).add(
			TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.key(),
			TFBlocks.POTTED_CANOPY_SAPLING.key(),
			TFBlocks.POTTED_MANGROVE_SAPLING.key(),
			TFBlocks.POTTED_DARKWOOD_SAPLING.key(),
			TFBlocks.POTTED_RAINBOW_OAK_SAPLING.key(),
			TFBlocks.POTTED_HOLLOW_OAK_SAPLING.key(),
			TFBlocks.POTTED_TIME_SAPLING.key(),
			TFBlocks.POTTED_TRANSFORMATION_SAPLING.key(),
			TFBlocks.POTTED_MINING_SAPLING.key(),
			TFBlocks.POTTED_SORTING_SAPLING.key(),
			TFBlocks.POTTED_MAYAPPLE.key(),
			TFBlocks.POTTED_FIDDLEHEAD.key(),
			TFBlocks.POTTED_MUSHGLOOM.key(),
			TFBlocks.POTTED_THORN.key(),
			TFBlocks.POTTED_GREEN_THORN.key(),
			TFBlocks.POTTED_DEAD_THORN.key());

		this.tag(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.key(),
			TFBlocks.CANOPY_WALL_SIGN.key(),
			TFBlocks.MANGROVE_WALL_SIGN.key(),
			TFBlocks.DARK_WALL_SIGN.key(),
			TFBlocks.TIME_WALL_SIGN.key(),
			TFBlocks.TRANSFORMATION_WALL_SIGN.key(),
			TFBlocks.MINING_WALL_SIGN.key(),
			TFBlocks.SORTING_WALL_SIGN.key());

		this.tag(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.key(),
			TFBlocks.CANOPY_WALL_HANGING_SIGN.key(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.key(),
			TFBlocks.DARK_WALL_HANGING_SIGN.key(),
			TFBlocks.TIME_WALL_HANGING_SIGN.key(),
			TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.key(),
			TFBlocks.MINING_WALL_HANGING_SIGN.key(),
			TFBlocks.SORTING_WALL_HANGING_SIGN.key());

		this.tag(BlockTags.DRAGON_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.key(), TFBlocks.FAKE_DIAMOND.key(), TFBlocks.FAKE_GOLD.key());
		this.tag(BlockTags.WITHER_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.key(), TFBlocks.FAKE_GOLD.key());
		this.tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.key());
		this.tag(BlockTags.FIRE).add(TFBlocks.OMINOUS_FIRE.key());
		this.tag(BlockTags.BEACON_BASE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);
		this.tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.key(), TFBlocks.ROPE.key(), TFBlocks.ROOT_STRAND.key()).addTag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE);
		this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.key());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).addTag(TFBlockTags.TF_CHESTS);
		this.tag(BlockTags.EDIBLE_FOR_SHEEP).add(TFBlocks.MAYAPPLE.key(), TFBlocks.FIDDLEHEAD.key());
		this.tag(BlockTags.CAN_GLIDE_THROUGH).add(TFBlocks.ROOT_STRAND.key());
		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(TFBlocks.HUGE_LILY_PAD.key(), TFBlocks.FALLEN_LEAVES.key(), TFBlocks.ROOT_STRAND.key());
		this.tag(BlockTags.DRIPSTONE_REPLACEABLE).add(TFBlocks.TROLLSTEINN.key(), TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key());
		this.tag(BlockTags.MOSS_REPLACEABLE).add(TFBlocks.TROLLSTEINN.key(), TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key());
		this.tag(BlockTags.AZALEA_ROOT_REPLACEABLE).add(TFBlocks.TROLLSTEINN.key(), TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key());
		this.tag(BlockTags.FOREST_ROCK_CAN_PLACE_ON).add(TFBlocks.TROLLSTEINN.key(), TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key());
		this.tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.key());
		this.toolTierTags();
		this.tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.key(), TFBlocks.MANGROVE_ROOT.key(), TFBlocks.SINISTER_SPAWNER.key());
		this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.key());
		this.tag(BlockTags.SCULK_REPLACEABLE).add(TFBlocks.TROLLSTEINN.key());
		this.tag(BlockTags.REPLACEABLE_BY_TREES).add(
			TFBlocks.HARDENED_DARK_LEAVES.key(),
			TFBlocks.MAYAPPLE.key(),
			TFBlocks.FIDDLEHEAD.key(),
			TFBlocks.MOSS_PATCH.key(),
			TFBlocks.CLOVER_PATCH.key(),
			TFBlocks.MUSHGLOOM.key(),
			TFBlocks.FIREFLY.key(),
			TFBlocks.CICADA.key(),
			TFBlocks.FALLEN_LEAVES.key(),
			TFBlocks.TORCHBERRY_PLANT.key(),
			TFBlocks.ROOT_STRAND.key(),
			TFBlocks.ROOT_BLOCK.key(),
			TFBlocks.RASPBERRY_BUSH.key(),
			TFBlocks.BLUEBERRY_BUSH.key(),
			TFBlocks.BLACKBERRY_BUSH.key(),
			TFBlocks.MALOBERRY_BUSH.key()
		);
		this.tag(BlockTags.REPLACEABLE_BY_MUSHROOMS).add(TFBlocks.MAYAPPLE.key(), TFBlocks.FIDDLEHEAD.key(), TFBlocks.FALLEN_LEAVES.key());
		this.tag(BlockTags.PREVENTS_NEARBY_LEAF_DECAY).add(TFBlocks.HUGE_STALK.key(), TFBlocks.BROWN_THORNS.key(), TFBlocks.GREEN_THORNS.key());
		this.tag(BlockTags.SUPPORTS_CROPS).add(TFBlocks.UBEROUS_SOIL.key());
		this.tag(BlockTags.SUPPORTS_BIG_DRIPLEAF).add(TFBlocks.UBEROUS_SOIL.key());
		this.tag(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).add(TFBlocks.UBEROUS_SOIL.key());
		this.tag(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.key());
		this.tag(BlockTags.REPLACEABLE).addAll(TFBlocks.BLOCKS.getEntries().stream().map(Holder::value).filter(b -> b.defaultBlockState().canBeReplaced()).map(b -> b.builtInRegistryHolder().key()).toList());
		this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.key());

		this.tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.key(), TFBlocks.STRONGHOLD_SHIELD.key(),
			TFBlocks.TIME_LOG_CORE.key(), TFBlocks.TRANSFORMATION_LOG_CORE.key(),
			TFBlocks.MINING_LOG_CORE.key(), TFBlocks.SORTING_LOG_CORE.key(),
			TFBlocks.ANTIBUILDER.key(), TFBlocks.BUILT_BLOCK.key(),
			TFBlocks.FAKE_DIAMOND.key(), TFBlocks.FAKE_GOLD.key(),
			TFBlocks.REACTOR_DEBRIS.key(), TFBlocks.LOCKED_VANISHING_BLOCK.key(), TFBlocks.VANISHING_BLOCK.key(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.key(), TFBlocks.REAPPEARING_BLOCK.key(),
			TFBlocks.BEANSTALK_GROWER.key(), TFBlocks.GIANT_COBBLESTONE.key(),
			TFBlocks.GIANT_LOG.key(), TFBlocks.GIANT_LEAVES.key(),
			TFBlocks.GIANT_OBSIDIAN.key(), TFBlocks.BROWN_THORNS.key(),
			TFBlocks.GREEN_THORNS.key(), TFBlocks.BURNT_THORNS.key(),
			TFBlocks.PINK_FORCE_FIELD.key(), TFBlocks.ORANGE_FORCE_FIELD.key(),
			TFBlocks.GREEN_FORCE_FIELD.key(), TFBlocks.BLUE_FORCE_FIELD.key(),
			TFBlocks.VIOLET_FORCE_FIELD.key(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.key(),
			TFBlocks.NAGA_BOSS_SPAWNER.key(), TFBlocks.LICH_BOSS_SPAWNER.key(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.key(), TFBlocks.HYDRA_BOSS_SPAWNER.key(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.key(), TFBlocks.UR_GHAST_BOSS_SPAWNER.key(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.key(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.key());
		this.tag(Tags.Blocks.SKULLS).add(
			TFBlocks.SKELETON_SKULL_CANDLE.key(),
			TFBlocks.SKELETON_WALL_SKULL_CANDLE.key(),
			TFBlocks.WITHER_SKELE_SKULL_CANDLE.key(),
			TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.key(),
			TFBlocks.PLAYER_SKULL_CANDLE.key(),
			TFBlocks.PLAYER_WALL_SKULL_CANDLE.key(),
			TFBlocks.ZOMBIE_SKULL_CANDLE.key(),
			TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.key(),
			TFBlocks.CREEPER_SKULL_CANDLE.key(),
			TFBlocks.CREEPER_WALL_SKULL_CANDLE.key(),
			TFBlocks.PIGLIN_SKULL_CANDLE.key(),
			TFBlocks.PIGLIN_WALL_SKULL_CANDLE.key());

		this.tag(TFBlockTags.HOLLOW_LOGS_HORIZONTAL).add(
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.key(),
			TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.key(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.key(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.key()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_VERTICAL).add(
			TFBlocks.HOLLOW_OAK_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.key(),
			TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.key(),
			TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_DARK_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_TIME_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_MINING_LOG_VERTICAL.key(),
			TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.key()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE).add(
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.key(),
			TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.key(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.key(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.key()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS).addTags(TFBlockTags.HOLLOW_LOGS_HORIZONTAL, TFBlockTags.HOLLOW_LOGS_VERTICAL, TFBlockTags.HOLLOW_LOGS_CLIMBABLE);



		this.tag(TFBlockTags.MAZESTONE).add(
			TFBlocks.MAZESTONE.key(), TFBlocks.MAZESTONE_BRICK.key(),
			TFBlocks.CRACKED_MAZESTONE.key(), TFBlocks.MOSSY_MAZESTONE.key(),
			TFBlocks.CUT_MAZESTONE.key(), TFBlocks.DECORATIVE_MAZESTONE.key(),
			TFBlocks.MAZESTONE_MOSAIC.key(), TFBlocks.MAZESTONE_BORDER.key());

		this.tag(TFBlockTags.CASTLE_BLOCKS).add(
			TFBlocks.CASTLE_BRICK.key(), TFBlocks.WORN_CASTLE_BRICK.key(),
			TFBlocks.CRACKED_CASTLE_BRICK.key(), TFBlocks.MOSSY_CASTLE_BRICK.key(),
			TFBlocks.CASTLE_ROOF_TILE.key(), TFBlocks.THICK_CASTLE_BRICK.key(),
			TFBlocks.BOLD_CASTLE_BRICK_TILE.key(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.key(),
			TFBlocks.ENCASED_CASTLE_BRICK_TILE.key(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.key(),
			TFBlocks.CASTLE_BRICK_STAIRS.key(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.key(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.key(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.key(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.key(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.key(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.key(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.key(),
			TFBlocks.PINK_CASTLE_DOOR.key(), TFBlocks.YELLOW_CASTLE_DOOR.key(),
			TFBlocks.BLUE_CASTLE_DOOR.key(), TFBlocks.VIOLET_CASTLE_DOOR.key()
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
				TFBlocks.FIDDLEHEAD.key(),
				TFBlocks.MOSS_PATCH.key(),
				TFBlocks.MAYAPPLE.key(),
				TFBlocks.CLOVER_PATCH.key(),
				TFBlocks.MUSHGLOOM.key(),
				TFBlocks.FALLEN_LEAVES.key(),
				TFBlocks.GIANT_LEAVES.key(),
				TFBlocks.HARDENED_DARK_LEAVES.key(),
				TFBlocks.RASPBERRY_BUSH.key(),
				TFBlocks.BLACKBERRY_BUSH.key(),
				TFBlocks.BLUEBERRY_BUSH.key(),
				TFBlocks.MALOBERRY_BUSH.key(),
				TFBlocks.COPPER_OREBERRY_BUSH.key(),
				TFBlocks.IRON_OREBERRY_BUSH.key(),
				TFBlocks.GOLD_OREBERRY_BUSH.key(),
				TFBlocks.ESSENCE_OREBERRY_BUSH.key()
			).addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockItemTags.SAPLINGS.block(), BlockTags.CROPS);

		this.tag(TFBlockTags.GENERATED_PORTAL_DECO)
			.add(BlockItemIds.BROWN_MUSHROOM.block(), BlockItemIds.RED_MUSHROOM.block(),
				BlockItemIds.SHORT_GRASS.block(), BlockItemIds.FERN.block(),
				BlockItemIds.BLUE_ORCHID.block(), BlockItemIds.AZURE_BLUET.block(),
				BlockItemIds.LILY_OF_THE_VALLEY.block(), BlockItemIds.OXEYE_DAISY.block(),
				BlockItemIds.ALLIUM.block(), BlockItemIds.CORNFLOWER.block(),
				BlockItemIds.WHITE_TULIP.block(), BlockItemIds.PINK_TULIP.block(),
				BlockItemIds.ORANGE_TULIP.block(), BlockItemIds.RED_TULIP.block(),
				TFBlocks.MUSHGLOOM.key(),
				TFBlocks.MAYAPPLE.key(),
				TFBlocks.FIDDLEHEAD.key());

		this.tag(TFBlockTags.DARK_TOWER_ALLOWED_POTS)
			.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.key(), TFBlocks.POTTED_CANOPY_SAPLING.key(), TFBlocks.POTTED_MANGROVE_SAPLING.key(),
				TFBlocks.POTTED_DARKWOOD_SAPLING.key(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.key(), TFBlocks.POTTED_MAYAPPLE.key(),
				TFBlocks.POTTED_FIDDLEHEAD.key(), TFBlocks.POTTED_MUSHGLOOM.key())
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
			.add(TFBlocks.NAGA_TROPHY.key(), TFBlocks.NAGA_WALL_TROPHY.key())
			.add(TFBlocks.LICH_TROPHY.key(), TFBlocks.LICH_WALL_TROPHY.key())
			.add(TFBlocks.MINOSHROOM_TROPHY.key(), TFBlocks.MINOSHROOM_WALL_TROPHY.key())
			.add(TFBlocks.HYDRA_TROPHY.key(), TFBlocks.HYDRA_WALL_TROPHY.key())
			.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.key(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.key())
			.add(TFBlocks.UR_GHAST_TROPHY.key(), TFBlocks.UR_GHAST_WALL_TROPHY.key())
			.add(TFBlocks.ALPHA_YETI_TROPHY.key(), TFBlocks.ALPHA_YETI_WALL_TROPHY.key())
			.add(TFBlocks.SNOW_QUEEN_TROPHY.key(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.key())
			.add(TFBlocks.QUEST_RAM_TROPHY.key(), TFBlocks.QUEST_RAM_WALL_TROPHY.key());

		this.tag(TFBlockTags.FIRE_JET_FUEL).add(BlockIds.LAVA);
		this.tag(TFBlockTags.ICE_BOMB_REPLACEABLES)
			.add(TFBlocks.MAYAPPLE.key(), TFBlocks.FIDDLEHEAD.key(), BlockItemIds.SHORT_GRASS.block(), BlockItemIds.TALL_GRASS.block(), BlockItemIds.FERN.block(), BlockItemIds.LARGE_FERN.block())
			.addTag(BlockTags.FLOWERS);

		this.tag(TFBlockTags.PLANTS_HANG_ON)
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
			.add(BlockItemIds.MOSS_BLOCK.block(), TFBlocks.MANGROVE_ROOT.key(), TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key());

		this.tag(TFBlockTags.OREBERRY_BUSHES_SURVIVE)
				.addTags(Tags.BlockItems.STONES.block())
				.addTags(BlockTags.STONE_BRICKS)
				.addTags(Tags.BlockItems.ORES_IN_GROUND_STONE.block())
				.addTags(Tags.BlockItems.ORES_IN_GROUND_DEEPSLATE.block())
				.addTags(Tags.BlockItems.ORES_IN_GROUND_NETHERRACK.block())
				.addTags(Tags.BlockItems.COBBLESTONES.block())
				.addTags(Tags.BlockItems.NETHERRACKS.block())
				.add(TFBlocks.GIANT_COBBLESTONE.key())
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
				.add(TFBlocks.MAYAPPLE.key());

		this.tag(TFBlockTags.TF_BERRY_BUSHES_SURVIVE)
				.addTags(BlockTags.SUBSTRATE_OVERWORLD)
				.add(BlockItemIds.SNOW_BLOCK.block());

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_SURVIVE)
				.addTag(Tags.BlockItems.NETHERRACKS.block())
				.addTag(Tags.BlockItems.ORES_IN_GROUND_NETHERRACK.block())
				.add(BlockItemIds.BLACKSTONE.block())
				.add(BlockItemIds.SOUL_SAND.block())
				.add(BlockItemIds.SOUL_SOIL.block());

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_DIE)
				.addTags(BlockTags.NYLIUM);

		this.tag(TFBlockTags.COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
			TFBlocks.NAGA_BOSS_SPAWNER.key(),
			TFBlocks.LICH_BOSS_SPAWNER.key(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.key(),
			TFBlocks.HYDRA_BOSS_SPAWNER.key(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.key(),
			TFBlocks.UR_GHAST_BOSS_SPAWNER.key(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.key(),
			TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.key(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.key(),
			TFBlocks.STRONGHOLD_SHIELD.key(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.key(),
			TFBlocks.LOCKED_VANISHING_BLOCK.key(),
			TFBlocks.PINK_FORCE_FIELD.key(),
			TFBlocks.ORANGE_FORCE_FIELD.key(),
			TFBlocks.GREEN_FORCE_FIELD.key(),
			TFBlocks.BLUE_FORCE_FIELD.key(),
			TFBlocks.VIOLET_FORCE_FIELD.key(),
			TFBlocks.SKULL_CHEST.key(),
			TFBlocks.KEEPSAKE_CASKET.key(),
			TFBlocks.TROPHY_PEDESTAL.key()
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
		this.tag(TFBlockTags.DEADROCK).add(TFBlocks.DEADROCK.key(), TFBlocks.CRACKED_DEADROCK.key(), TFBlocks.WEATHERED_DEADROCK.key());
		this.tag(TFBlockTags.ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
			.add(BlockIds.NETHER_PORTAL)
			.addTag(TFBlockTags.DEADROCK)
			.add(TFBlocks.CASTLE_BRICK.key(), TFBlocks.THICK_CASTLE_BRICK.key(), TFBlocks.MOSSY_CASTLE_BRICK.key(), TFBlocks.CASTLE_ROOF_TILE.key(), TFBlocks.WORN_CASTLE_BRICK.key())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.key(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.key(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.key(), TFBlocks.PINK_CASTLE_RUNE_BRICK.key())
			.add(TFBlocks.PINK_FORCE_FIELD.key(), TFBlocks.ORANGE_FORCE_FIELD.key(), TFBlocks.GREEN_FORCE_FIELD.key(), TFBlocks.BLUE_FORCE_FIELD.key(), TFBlocks.VIOLET_FORCE_FIELD.key())
			.add(TFBlocks.BROWN_THORNS.key(), TFBlocks.GREEN_THORNS.key());

		this.tag(TFBlockTags.ANTIBUILDER_IGNORES).add(
			BlockItemIds.REDSTONE_LAMP.block(),
			BlockItemIds.TNT.block(),
			BlockIds.WATER,
			TFBlocks.ANTIBUILDER.key(),
			TFBlocks.CARMINITE_BUILDER.key(),
			TFBlocks.BUILT_BLOCK.key(),
			TFBlocks.REACTOR_DEBRIS.key(),
			TFBlocks.CARMINITE_REACTOR.key(),
			TFBlocks.REAPPEARING_BLOCK.key(),
			TFBlocks.GHAST_TRAP.key(),
			TFBlocks.FAKE_DIAMOND.key(),
			TFBlocks.FAKE_GOLD.key()
		).addTag(TFBlockTags.COMMON_PROTECTIONS)/*.addOptional(Identifier.parse("gravestone:gravestone"))*/; //TODO: Restore

		this.tag(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS).add(BlockItemIds.LEVER.block()).add(TFBlocks.ANTIBUILDER.key()).addTags(BlockTags.BUTTONS, Tags.BlockItems.CHESTS.block());

		// TODO add more grave mods to this list
		this.tag(TFBlockTags.PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.key())
			.add(TFBlocks.KEEPSAKE_CASKET.key())
			/*.addOptional(Identifier.fromNamespaceAndPath("gravestone", "gravestone"))*/;

		this.tag(TFBlockTags.CANNOT_TROLL_CAVE_HOLLOW)
			.add(BlockItemIds.RED_MUSHROOM_BLOCK.block())
			.add(BlockItemIds.BROWN_MUSHROOM_BLOCK.block())
			.add(TFBlocks.HUGE_MUSHGLOOM.key());

		this.tag(TFBlockTags.ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
			BlockTags.SUBSTRATE_OVERWORLD,
			Tags.BlockItems.GRAVELS.block(),
			Tags.BlockItems.SANDS.block(),
			BlockTags.NYLIUM,
			BlockTags.BASE_STONE_OVERWORLD,
			BlockTags.BASE_STONE_NETHER,
			Tags.BlockItems.END_STONES.block(),
			BlockTags.DEEPSLATE_ORE_REPLACEABLES,
			BlockTags.STONE_ORE_REPLACEABLES,
			TFBlockTags.ROOT_GROUND
		);

		this.tag(TFBlockTags.ORE_MAGNET_IGNORE).addTag(BlockItemTags.COAL_ORES.block());
		this.tag(TFBlockTags.MINING_CORE_EXCLUDED).addTag(BlockItemTags.COAL_ORES.block());
		this.tag(TFBlockTags.ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.key());
		this.tag(TFBlockTags.ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.key());
		this.tag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.key(),
			TFBlocks.CANOPY_CHEST.key(),
			TFBlocks.MANGROVE_CHEST.key(),
			TFBlocks.DARK_CHEST.key(),
			TFBlocks.TIME_CHEST.key(),
			TFBlocks.TRANSFORMATION_CHEST.key(),
			TFBlocks.MINING_CHEST.key(),
			TFBlocks.SORTING_CHEST.key());

		// For anything that permits replacement during Worldgen
		this.tag(TFBlockTags.WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES);
		this.tag(TFBlockTags.ROOT_TRACE_SKIP).addTag(BlockTags.LOGS).add(TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key(), TFBlocks.MANGROVE_ROOT.key(), TFBlocks.TIME_WOOD.key()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);
		this.tag(TFBlockTags.DRUID_PROJECTILE_REPLACEABLE).addTags(BlockTags.LEAVES, BlockTags.LOGS, BlockTags.PLANKS, BlockTags.REPLACEABLE_BY_TREES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SCULK_REPLACEABLE, Tags.BlockItems.ORES.block());
		this.tag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE).addTag(BlockTags.SUBSTRATE_OVERWORLD).add(BlockItemIds.MYCELIUM.block()).add(BlockItemIds.PODZOL.block()).add(BlockItemIds.CRIMSON_NYLIUM.block()).add(BlockItemIds.WARPED_NYLIUM.block());
		this.tag(TFBlockTags.TIME_CORE_EXCLUDED).add(BlockIds.NETHER_PORTAL);
		this.tag(TFBlockTags.ORE_METER_TARGETABLE)
			.addTag(Tags.BlockItems.ORES.block())
			.addTag(BlockTags.BASE_STONE_OVERWORLD)
			.addTag(BlockTags.BASE_STONE_NETHER)
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
			.addTag(Tags.BlockItems.SANDS.block())
			.addTag(Tags.BlockItems.SANDSTONE_BLOCKS.block())
			.addTag(BlockTags.TERRACOTTA)
			.addTag(Tags.BlockItems.GRAVELS.block())
			.addTag(BlockTags.NYLIUM)
			.addTag(TFBlockTags.ROOT_ORES)
			.add(BlockItemIds.BUDDING_AMETHYST.block())
			.add(BlockItemIds.CALCITE.block())
			.add(BlockItemIds.SOUL_SAND.block())
			.add(BlockItemIds.SOUL_SOIL.block());

		this.tag(TFBlockTags.PENGUINS_SPAWNABLE_ON).addTag(BlockTags.ICE);
		this.tag(TFBlockTags.GIANTS_SPAWNABLE_ON).addTag(TFBlockTags.CLOUDS);
		this.tag(TFBlockTags.SUPPORTS_STALAGMITES).addTag(TFBlockTags.DEADROCK).add(BlockItemIds.PACKED_ICE.block());
		this.tag(TFBlockTags.CARVER_REPLACEABLES).add(BlockItemIds.SNOW_BLOCK.block());
		this.tag(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);
		this.tag(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK, BlockTags.WITHER_IMMUNE)
			.add(TFBlocks.TIME_LOG_CORE.key(), TFBlocks.TRANSFORMATION_LOG_CORE.key(), TFBlocks.MINING_LOG_CORE.key(), TFBlocks.SORTING_LOG_CORE.key())
			.add(TFBlocks.GIANT_OBSIDIAN.key());

		this.tag(TFBlockTags.SMALL_LAKES_DONT_REPLACE).addTags(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LOGS, BlockTags.LEAVES)
			.add(TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key(), BlockItemIds.MUSHROOM_STEM.block());
	}

	@SuppressWarnings("unchecked")
	private void toolTierTags() {
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
			TFBlocks.HEDGE.key(),
			TFBlocks.ROOT_BLOCK.key(),
			TFBlocks.LIVEROOT_BLOCK.key(),
			TFBlocks.MANGROVE_ROOT.key(),
			TFBlocks.UNCRAFTING_TABLE.key(),
			TFBlocks.ENCASED_SMOKER.key(),
			TFBlocks.ENCASED_FIRE_JET.key(),
			TFBlocks.TIME_LOG_CORE.key(),
			TFBlocks.TRANSFORMATION_LOG_CORE.key(),
			TFBlocks.MINING_LOG_CORE.key(),
			TFBlocks.SORTING_LOG_CORE.key(),
			TFBlocks.REAPPEARING_BLOCK.key(),
			TFBlocks.VANISHING_BLOCK.key(),
			TFBlocks.ANTIBUILDER.key(),
			TFBlocks.CARMINITE_REACTOR.key(),
			TFBlocks.CARMINITE_BUILDER.key(),
			TFBlocks.GHAST_TRAP.key(),
			TFBlocks.HUGE_STALK.key(),
			TFBlocks.HUGE_MUSHGLOOM.key(),
			TFBlocks.HUGE_MUSHGLOOM_STEM.key(),
			TFBlocks.CINDER_LOG.key(),
			TFBlocks.CINDER_WOOD.key(),
			TFBlocks.IRONWOOD_BLOCK.key(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.key(),
			TFBlocks.CANOPY_BOOKSHELF.key(),
			TFBlocks.TWILIGHT_OAK_CHEST.key(),
			TFBlocks.CANOPY_CHEST.key(),
			TFBlocks.MANGROVE_CHEST.key(),
			TFBlocks.DARK_CHEST.key(),
			TFBlocks.TIME_CHEST.key(),
			TFBlocks.TRANSFORMATION_CHEST.key(),
			TFBlocks.MINING_CHEST.key(),
			TFBlocks.SORTING_CHEST.key(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.key(),
			TFBlocks.CANOPY_TRAPPED_CHEST.key(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.key(),
			TFBlocks.DARK_TRAPPED_CHEST.key(),
			TFBlocks.TIME_TRAPPED_CHEST.key(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.key(),
			TFBlocks.MINING_TRAPPED_CHEST.key(),
			TFBlocks.SORTING_TRAPPED_CHEST.key(),
			TFBlocks.HUGE_LILY_PAD.key(),
			TFBlocks.ENCASED_TOWERWOOD.key()
		).addTags(TFBlockTags.BANISTERS, TFBlockTags.HOLLOW_LOGS, TFBlockTags.TOWERWOOD, TFBlockTags.DRYING_RACKS);

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
			//vanilla doesnt use the leaves tag
			TFBlocks.TWILIGHT_OAK_LEAVES.key(),
			TFBlocks.CANOPY_LEAVES.key(),
			TFBlocks.MANGROVE_LEAVES.key(),
			TFBlocks.DARK_LEAVES.key(),
			TFBlocks.RAINBOW_OAK_LEAVES.key(),
			TFBlocks.TIME_LEAVES.key(),
			TFBlocks.TRANSFORMATION_LEAVES.key(),
			TFBlocks.MINING_LEAVES.key(),
			TFBlocks.SORTING_LEAVES.key(),
			TFBlocks.THORN_LEAVES.key(),
			TFBlocks.THORN_ROSE.key(),
			TFBlocks.BEANSTALK_LEAVES.key(),
			TFBlocks.STEELEAF_BLOCK.key(),
			TFBlocks.ARCTIC_FUR_BLOCK.key()
		);

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
			TFBlocks.NAGASTONE.key(),
			TFBlocks.NAGASTONE_HEAD.key(),
			TFBlocks.STRONGHOLD_SHIELD.key(),
			TFBlocks.TROPHY_PEDESTAL.key(),
			TFBlocks.AURORA_PILLAR.key(),
			TFBlocks.AURORA_SLAB.key(),
			TFBlocks.UNDERBRICK.key(),
			TFBlocks.MOSSY_UNDERBRICK.key(),
			TFBlocks.CRACKED_UNDERBRICK.key(),
			TFBlocks.UNDERBRICK_FLOOR.key(),
			TFBlocks.TROLLSTEINN.key(),
			TFBlocks.GIANT_LEAVES.key(),
			TFBlocks.GIANT_OBSIDIAN.key(),
			TFBlocks.GIANT_COBBLESTONE.key(),
			TFBlocks.GIANT_LOG.key(),
			TFBlocks.CINDER_FURNACE.key(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.key(),
			//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.key(),
			//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.key(),
			//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.key(),
			//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.key(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.key(),
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.key(),
			TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.key(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.key(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.key(),
			TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.key(),
			//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.key(),
			//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.key(),
			//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.key(),
			//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.key(),
			TFBlocks.KNIGHTMETAL_BLOCK.key(),
			TFBlocks.IRONWOOD_BLOCK.key(),
			TFBlocks.FIERY_BLOCK.key(),
			TFBlocks.CARMINITE_BLOCK.key(),
			TFBlocks.SPIRAL_BRICKS.key(),
			TFBlocks.ETCHED_NAGASTONE.key(),
			TFBlocks.NAGASTONE_PILLAR.key(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.key(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.key(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.IRON_LADDER.key(),
			TFBlocks.CANDELABRA.key(),
			TFBlocks.TWISTED_STONE.key(),
			TFBlocks.TWISTED_STONE_PILLAR.key(),
			TFBlocks.SKULL_CHEST.key(),
			TFBlocks.KEEPSAKE_CASKET.key(),
			TFBlocks.BOLD_STONE_PILLAR.key(),
			TFBlocks.TERRORCOTTA_CURVES.key(),
			TFBlocks.TERRORCOTTA_LINES.key(),
			TFBlocks.TERRORCOTTA_ARCS.key(),
			TFBlocks.SINISTER_SPAWNER.key()
		).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK);

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
			TFBlocks.SMOKER.key(),
			TFBlocks.FIRE_JET.key(),
			TFBlocks.UBEROUS_SOIL.key()
		);

		this.tag(BlockTags.SWORD_EFFICIENT).add(TFBlocks.HUGE_LILY_PAD.key());

		this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(
			TFBlocks.NAGASTONE.key(),
			TFBlocks.NAGASTONE_HEAD.key(),
			TFBlocks.ETCHED_NAGASTONE.key(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.key(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.key(),
			TFBlocks.NAGASTONE_PILLAR.key(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.key(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.key(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.key(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.key(),
			TFBlocks.SPIRAL_BRICKS.key(),
			TFBlocks.TWISTED_STONE.key(),
			TFBlocks.TWISTED_STONE_PILLAR.key(),
			TFBlocks.BOLD_STONE_PILLAR.key(),
			TFBlocks.TERRORCOTTA_CURVES.key(),
			TFBlocks.TERRORCOTTA_LINES.key(),
			TFBlocks.TERRORCOTTA_ARCS.key(),
			TFBlocks.AURORA_PILLAR.key(),
			TFBlocks.AURORA_SLAB.key(),
			TFBlocks.TROLLSTEINN.key()
		);

		this.tag(BlockTags.NEEDS_STONE_TOOL).add(
			TFBlocks.UNDERBRICK.key(),
			TFBlocks.CRACKED_UNDERBRICK.key(),
			TFBlocks.MOSSY_UNDERBRICK.key(),
			TFBlocks.UNDERBRICK_FLOOR.key(),
			TFBlocks.IRON_LADDER.key()
		);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
			TFBlocks.FIERY_BLOCK.key(),
			TFBlocks.KNIGHTMETAL_BLOCK.key()
		);

		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TFBlocks.AURORA_BLOCK.key()).addTags(TFBlockTags.CASTLE_BLOCKS, TFBlockTags.MAZESTONE, TFBlockTags.DEADROCK);

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
