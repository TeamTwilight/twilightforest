package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedBlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.Objects;
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
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return BlockTagGenerator.this.tag(blockTag);
			}
		}.run();

		this.tag(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.get(),
			TFBlocks.CANOPY_WALL_SIGN.get(),
			TFBlocks.MANGROVE_WALL_SIGN.get(),
			TFBlocks.DARK_WALL_SIGN.get(),
			TFBlocks.TIME_WALL_SIGN.get(),
			TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
			TFBlocks.MINING_WALL_SIGN.get(),
			TFBlocks.SORTING_WALL_SIGN.get());

		this.tag(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(),
			TFBlocks.CANOPY_WALL_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(),
			TFBlocks.DARK_WALL_HANGING_SIGN.get(),
			TFBlocks.TIME_WALL_HANGING_SIGN.get(),
			TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(),
			TFBlocks.MINING_WALL_HANGING_SIGN.get(),
			TFBlocks.SORTING_WALL_HANGING_SIGN.get());

		this.tag(BlockTags.FLOWER_POTS).add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(), TFBlocks.POTTED_DARKWOOD_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), TFBlocks.POTTED_TIME_SAPLING.get(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), TFBlocks.POTTED_MINING_SAPLING.get(), TFBlocks.POTTED_SORTING_SAPLING.get(), TFBlocks.POTTED_MAYAPPLE.get(), TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.POTTED_MUSHGLOOM.get(), TFBlocks.POTTED_THORN.get(), TFBlocks.POTTED_GREEN_THORN.get(), TFBlocks.POTTED_DEAD_THORN.get());

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
			TFBlocks.ROOT_BLOCK.get(),
			TFBlocks.RASPBERRY_BUSH.get(),
			TFBlocks.BLUEBERRY_BUSH.get(),
			TFBlocks.BLACKBERRY_BUSH.get(),
			TFBlocks.MALOBERRY_BUSH.get()
		);

		this.tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.get(), TFBlocks.ROPE.get(), TFBlocks.ROOT_STRAND.get()).addTag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE);

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



		this.tag(BlockTags.BEACON_BASE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(TFBlockTags.PORTAL_POOL).add(Blocks.WATER);

		//GUIDELINE: The portal frame requires dirt. The blocks we use are all essentially that with their nature unchanged (e.g unlike Mud) and remain similar-looking.
		this.tag(TFBlockTags.PORTAL_EDGE).add(Blocks.FARMLAND).addTags(BlockTags.DIRT, BlockTags.GRASS_BLOCKS);

		//GUIDELINE: Organic vegetation. No vegetation from alien, hostile worlds like the Nether or End.
		//Vegetation from dimensions more similar to the Overworld (e.g. The Aether, Tropics, etc.) would logically work.
		//Must be placed on top of the frame blocks and/or supported by them (e.g. no Cocoa Beans that float above it/are grown from another block).
		this.tag(TFBlockTags.PORTAL_DECO).add(
				Blocks.BAMBOO, Blocks.BAMBOO_SAPLING,
				Blocks.SHORT_GRASS, Blocks.TALL_GRASS,
				Blocks.FERN, Blocks.LARGE_FERN,
				Blocks.DEAD_BUSH, Blocks.BUSH, Blocks.FIREFLY_BUSH,
				Blocks.SUGAR_CANE,
				Blocks.SWEET_BERRY_BUSH,
				Blocks.GLOW_LICHEN,
				Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
				Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM,
				Blocks.PINK_PETALS, Blocks.WILDFLOWERS,
				Blocks.LEAF_LITTER,
				Blocks.SHORT_DRY_GRASS, Blocks.TALL_DRY_GRASS,
				Blocks.BIG_DRIPLEAF, Blocks.BIG_DRIPLEAF_STEM,
				Blocks.SMALL_DRIPLEAF,
				TFBlocks.FIDDLEHEAD.get(),
				TFBlocks.MOSS_PATCH.get(),
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.CLOVER_PATCH.get(),
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.FALLEN_LEAVES.get(),
				TFBlocks.GIANT_LEAVES.get(),
				TFBlocks.HARDENED_DARK_LEAVES.get(),
				TFBlocks.RASPBERRY_BUSH.get(),
				TFBlocks.BLACKBERRY_BUSH.get(),
				TFBlocks.BLUEBERRY_BUSH.get(),
				TFBlocks.MALOBERRY_BUSH.get(),
				TFBlocks.COPPER_OREBERRY_BUSH.get(),
				TFBlocks.IRON_OREBERRY_BUSH.get(),
				TFBlocks.GOLD_OREBERRY_BUSH.get(),
				TFBlocks.ESSENCE_OREBERRY_BUSH.get())
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
				Blocks.POTTED_WARPED_ROOTS, Blocks.POTTED_AZALEA, Blocks.POTTED_FLOWERING_AZALEA, Blocks.POTTED_MANGROVE_PROPAGULE,
				Blocks.POTTED_CHERRY_SAPLING, Blocks.POTTED_TORCHFLOWER);

		this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.get());

		this.tag(TFBlockTags.TROPHY_PEDESTAL_ACTIVATION_BLOCKS)
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
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
			.add(Blocks.MOSS_BLOCK, TFBlocks.MANGROVE_ROOT.get(), TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get());

		this.tag(TFBlockTags.OREBERRY_BUSHES_SURVIVE)
				.addTags(Tags.Blocks.STONES)
				.addTags(BlockTags.STONE_BRICKS)
				.addTags(Tags.Blocks.ORES_IN_GROUND_STONE)
				.addTags(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
				.addTags(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
				.addTags(Tags.Blocks.COBBLESTONES)
				.addTags(Tags.Blocks.NETHERRACKS)
				.add(TFBlocks.GIANT_COBBLESTONE.get())
				.add(Blocks.POLISHED_ANDESITE)
				.add(Blocks.POLISHED_DIORITE)
				.add(Blocks.POLISHED_GRANITE)
				.add(Blocks.SMOOTH_STONE)
				.add(Blocks.INFESTED_CHISELED_STONE_BRICKS)
				.add(Blocks.INFESTED_CRACKED_STONE_BRICKS)
				.add(Blocks.INFESTED_MOSSY_STONE_BRICKS)
				.add(Blocks.INFESTED_STONE_BRICKS);

		this.tag(TFBlockTags.TF_BERRY_BUSHES_REPLACE)
				.addTags(BlockTags.REPLACEABLE)
				.addTags(BlockTags.FLOWERS)
				.add(TFBlocks.MAYAPPLE.get());

		this.tag(TFBlockTags.TF_BERRY_BUSHES_SURVIVE)
				.addTags(BlockTags.SUBSTRATE_OVERWORLD)
				.add(Blocks.SNOW_BLOCK);

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_SURVIVE)
				.addTag(Tags.Blocks.NETHERRACKS)
				.addTag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
				.add(Blocks.BLACKSTONE)
				.add(Blocks.SOUL_SAND)
				.add(Blocks.SOUL_SOIL);

		this.tag(TFBlockTags.DARK_TOWER_BERRY_BUSHES_DIE)
				.addTags(BlockTags.NYLIUM);

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
		).addTag(TFBlockTags.COMMON_PROTECTIONS)/*.addOptional(Identifier.parse("gravestone:gravestone"))*/; //TODO: Restore

		this.tag(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS).add(Blocks.LEVER).add(TFBlocks.ANTIBUILDER.get()).addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS);

		// TODO add more grave mods to this list
		this.tag(TFBlockTags.PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.get())
			.add(TFBlocks.KEEPSAKE_CASKET.get())
			/*.addOptional(Identifier.fromNamespaceAndPath("gravestone", "gravestone"))*/;

		this.tag(TFBlockTags.CANNOT_TROLL_CAVE_HOLLOW)
			.add(Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.BROWN_MUSHROOM_BLOCK)
			.add(TFBlocks.HUGE_MUSHGLOOM.get());

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

		this.tag(TFBlockTags.ORE_MAGNET_IGNORE).addTag(BlockTags.COAL_ORES);
		this.tag(TFBlockTags.MINING_CORE_EXCLUDED).addTag(BlockTags.COAL_ORES);

		this.tag(TFBlockTags.ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.get());
		this.tag(TFBlockTags.ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.get());

		this.tag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.SORTING_CHEST.get());

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
			.addTag(BlockTags.SUBSTRATE_OVERWORLD)
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
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get(),
			TFBlocks.HUGE_LILY_PAD.get(),
			TFBlocks.ENCASED_TOWERWOOD.get()
		).addTags(TFBlockTags.BANISTERS, TFBlockTags.HOLLOW_LOGS, TFBlockTags.TOWERWOOD, TFBlockTags.DRYING_RACKS);

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
			TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(),
			TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(),
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
			TFBlocks.CANDELABRA.get(),
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

		this.tag(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).add(TFBlocks.UBEROUS_SOIL.get());

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

		this.tag(BlockTags.FIRE)
			.add(TFBlocks.OMINOUS_FIRE.get());
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}
