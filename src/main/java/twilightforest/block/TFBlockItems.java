package twilightforest.block;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.client.ISTER;
import twilightforest.item.*;
import twilightforest.block.entity.TFBlockEntities;

import java.util.Objects;
import java.util.function.Consumer;

public class TFBlockItems {

	public static void registerBlockItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> r = event.getRegistry();

		r.register(trophyBlock(TFBlocks.NAGA_TROPHY, TFBlocks.NAGA_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.LICH_TROPHY, TFBlocks.LICH_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.MINOSHROOM_TROPHY, TFBlocks.MINOSHROOM_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.HYDRA_TROPHY, TFBlocks.HYDRA_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.UR_GHAST_TROPHY, TFBlocks.UR_GHAST_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.ALPHA_YETI_TROPHY, TFBlocks.ALPHA_YETI_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY));
		r.register(trophyBlock(TFBlocks.QUEST_RAM_TROPHY, TFBlocks.QUEST_RAM_WALL_TROPHY));

		r.register(blockItem(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE));
		r.register(blockItem(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE));
		r.register(blockItem(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE));
//      r.register(blockItem(TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE));

		r.register(blockItem(TFBlocks.NAGA_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.LICH_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.MINOSHROOM_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.HYDRA_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.UR_GHAST_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.ALPHA_YETI_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER));
		r.register(blockItem(TFBlocks.FINAL_BOSS_BOSS_SPAWNER));

		r.register(blockItem(TFBlocks.ETCHED_NAGASTONE));
		r.register(blockItem(TFBlocks.CRACKED_ETCHED_NAGASTONE));
		r.register(blockItem(TFBlocks.MOSSY_ETCHED_NAGASTONE));
		r.register(blockItem(TFBlocks.NAGASTONE_PILLAR));
		r.register(blockItem(TFBlocks.CRACKED_NAGASTONE_PILLAR));
		r.register(blockItem(TFBlocks.MOSSY_NAGASTONE_PILLAR));
		r.register(blockItem(TFBlocks.NAGASTONE_STAIRS_LEFT));
		r.register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT));
		r.register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT));
		r.register(blockItem(TFBlocks.NAGASTONE_STAIRS_RIGHT));
		r.register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT));
		r.register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT));
		r.register(blockItem(TFBlocks.NAGASTONE_HEAD));
		r.register(blockItem(TFBlocks.NAGASTONE));
		r.register(blockItem(TFBlocks.SPIRAL_BRICKS));
//      r.register(blockItem(TFBlocks.TERRORCOTTA_CIRCLE));
//      r.register(blockItem(TFBlocks.TERRORCOTTA_DIAGONAL));
//      r.register(blockItem(TFBlocks.LAPIS_BLOCK));
		r.register(blockItem(TFBlocks.TWISTED_STONE));
		r.register(blockItem(TFBlocks.TWISTED_STONE_PILLAR));
		r.register(makeBlockItem(new BlockItem(TFBlocks.KEEPSAKE_CASKET.get(), TFItems.defaultBuilder().fireResistant()) {
			@Override
			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
				consumer.accept(new IItemRenderProperties() {
					@Override
					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
						return new ISTER(TFBlockEntities.KEEPSAKE_CASKET.getId());
					}
				});
			}
		}, TFBlocks.KEEPSAKE_CASKET));
		r.register(blockItem(TFBlocks.CANDELABRA));
		r.register(blockItem(TFBlocks.BOLD_STONE_PILLAR));
		r.register(blockItem(TFBlocks.DEATH_TOME_SPAWNER));
		r.register(blockItem(TFBlocks.EMPTY_CANOPY_BOOKSHELF));
		r.register(skullCandleItem(TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE));
		r.register(skullCandleItem(TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE));
		r.register(skullCandleItem(TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE));
		r.register(skullCandleItem(TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE));
		r.register(skullCandleItem(TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE));
		r.register(makeBlockItem(new HugeWaterLilyItem(TFBlocks.HUGE_WATER_LILY.get(), TFItems.defaultBuilder()), TFBlocks.HUGE_WATER_LILY));
		r.register(makeBlockItem(new HugeLilyPadItem(TFBlocks.HUGE_LILY_PAD.get(), TFItems.defaultBuilder()), TFBlocks.HUGE_LILY_PAD));
		r.register(blockItem(TFBlocks.MAZESTONE));
		r.register(blockItem(TFBlocks.MAZESTONE_BRICK));
		r.register(blockItem(TFBlocks.CRACKED_MAZESTONE));
		r.register(blockItem(TFBlocks.MOSSY_MAZESTONE));
		r.register(blockItem(TFBlocks.DECORATIVE_MAZESTONE));
		r.register(blockItem(TFBlocks.CUT_MAZESTONE));
		r.register(blockItem(TFBlocks.MAZESTONE_BORDER));
		r.register(blockItem(TFBlocks.MAZESTONE_MOSAIC));
		r.register(blockItem(TFBlocks.SMOKER));
		r.register(blockItem(TFBlocks.ENCASED_SMOKER));
		r.register(blockItem(TFBlocks.FIRE_JET));
		r.register(blockItem(TFBlocks.ENCASED_FIRE_JET));
		r.register(blockItem(TFBlocks.STRONGHOLD_SHIELD));
		r.register(blockItem(TFBlocks.TROPHY_PEDESTAL));
		r.register(blockItem(TFBlocks.UNDERBRICK));
		r.register(blockItem(TFBlocks.CRACKED_UNDERBRICK));
		r.register(blockItem(TFBlocks.MOSSY_UNDERBRICK));
		r.register(blockItem(TFBlocks.UNDERBRICK_FLOOR));
		r.register(blockItem(TFBlocks.TOWERWOOD));
		r.register(blockItem(TFBlocks.CRACKED_TOWERWOOD));
		r.register(blockItem(TFBlocks.MOSSY_TOWERWOOD));
		r.register(blockItem(TFBlocks.INFESTED_TOWERWOOD));
		r.register(blockItem(TFBlocks.ENCASED_TOWERWOOD));
		r.register(blockItem(TFBlocks.VANISHING_BLOCK));
		r.register(blockItem(TFBlocks.REAPPEARING_BLOCK));
		r.register(blockItem(TFBlocks.LOCKED_VANISHING_BLOCK));
		r.register(blockItem(TFBlocks.CARMINITE_BUILDER));
		r.register(blockItem(TFBlocks.ANTIBUILDER));
		r.register(blockItem(TFBlocks.CARMINITE_REACTOR));
		r.register(blockItem(TFBlocks.GHAST_TRAP));
		r.register(blockItem(TFBlocks.AURORA_BLOCK));
		r.register(blockItem(TFBlocks.AURORA_PILLAR));
		r.register(blockItem(TFBlocks.AURORA_SLAB));
		r.register(blockItem(TFBlocks.AURORALIZED_GLASS));
		r.register(blockItem(TFBlocks.TROLLSTEINN));
		r.register(blockItem(TFBlocks.TROLLVIDR));
		r.register(blockItem(TFBlocks.UNRIPE_TROLLBER));
		r.register(blockItem(TFBlocks.TROLLBER));
		r.register(blockItem(TFBlocks.HUGE_MUSHGLOOM));
		r.register(blockItem(TFBlocks.HUGE_MUSHGLOOM_STEM));
		r.register(blockItem(TFBlocks.UBEROUS_SOIL));
		r.register(blockItem(TFBlocks.HUGE_STALK));
		r.register(blockItem(TFBlocks.BEANSTALK_LEAVES));
		r.register(blockItem(TFBlocks.WISPY_CLOUD));
		r.register(blockItem(TFBlocks.FLUFFY_CLOUD));
		r.register(blockItem(TFBlocks.GIANT_COBBLESTONE));
		r.register(blockItem(TFBlocks.GIANT_LOG));
		r.register(blockItem(TFBlocks.GIANT_LEAVES));
		r.register(blockItem(TFBlocks.GIANT_OBSIDIAN));
		r.register(blockItem(TFBlocks.DEADROCK));
		r.register(blockItem(TFBlocks.CRACKED_DEADROCK));
		r.register(blockItem(TFBlocks.WEATHERED_DEADROCK));
		r.register(blockItem(TFBlocks.BROWN_THORNS));
		r.register(blockItem(TFBlocks.GREEN_THORNS));
		r.register(blockItem(TFBlocks.BURNT_THORNS));
		r.register(blockItem(TFBlocks.THORN_ROSE));
		r.register(blockItem(TFBlocks.THORN_LEAVES));
		r.register(blockItem(TFBlocks.CASTLE_BRICK));
		r.register(blockItem(TFBlocks.WORN_CASTLE_BRICK));
		r.register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK));
		r.register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK));
		r.register(blockItem(TFBlocks.THICK_CASTLE_BRICK));
		r.register(blockItem(TFBlocks.CASTLE_ROOF_TILE));
		r.register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR));
		r.register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE));
		r.register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR));
		r.register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		r.register(blockItem(TFBlocks.CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.WORN_CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_STAIRS));
		r.register(blockItem(TFBlocks.PINK_CASTLE_RUNE_BRICK));
		r.register(blockItem(TFBlocks.YELLOW_CASTLE_RUNE_BRICK));
		r.register(blockItem(TFBlocks.BLUE_CASTLE_RUNE_BRICK));
		r.register(blockItem(TFBlocks.VIOLET_CASTLE_RUNE_BRICK));
		r.register(blockItem(TFBlocks.PINK_CASTLE_DOOR));
		r.register(blockItem(TFBlocks.YELLOW_CASTLE_DOOR));
		r.register(blockItem(TFBlocks.BLUE_CASTLE_DOOR));
		r.register(blockItem(TFBlocks.VIOLET_CASTLE_DOOR));
		r.register(blockItem(TFBlocks.PINK_FORCE_FIELD));
		r.register(blockItem(TFBlocks.ORANGE_FORCE_FIELD));
		r.register(blockItem(TFBlocks.GREEN_FORCE_FIELD));
		r.register(blockItem(TFBlocks.BLUE_FORCE_FIELD));
		r.register(blockItem(TFBlocks.VIOLET_FORCE_FIELD));

		r.register(blockItem(TFBlocks.UNCRAFTING_TABLE));
		r.register(blockItem(TFBlocks.CINDER_FURNACE));
		r.register(blockItem(TFBlocks.CINDER_LOG));
		r.register(blockItem(TFBlocks.CINDER_WOOD));
		r.register(blockItem(TFBlocks.SLIDER));
		r.register(blockItem(TFBlocks.IRON_LADDER));

		r.register(blockItem(TFBlocks.IRONWOOD_BLOCK));
		r.register(blockItem(TFBlocks.STEELEAF_BLOCK));
		r.register(blockItem(TFBlocks.FIERY_BLOCK));
		r.register(blockItem(TFBlocks.KNIGHTMETAL_BLOCK));
		r.register(blockItem(TFBlocks.CARMINITE_BLOCK));
		r.register(blockItem(TFBlocks.ARCTIC_FUR_BLOCK));

		r.register(blockItem(TFBlocks.MOSS_PATCH));
		r.register(blockItem(TFBlocks.MAYAPPLE));
		r.register(blockItem(TFBlocks.CLOVER_PATCH));
		r.register(blockItem(TFBlocks.FIDDLEHEAD));
		r.register(blockItem(TFBlocks.MUSHGLOOM));
		r.register(blockItem(TFBlocks.TORCHBERRY_PLANT));
		r.register(blockItem(TFBlocks.ROOT_STRAND));
		r.register(blockItem(TFBlocks.FALLEN_LEAVES));
		r.register(wearableBlock(TFBlocks.FIREFLY, TFBlockEntities.FIREFLY));
		r.register(wearableBlock(TFBlocks.CICADA, TFBlockEntities.CICADA));
		r.register(wearableBlock(TFBlocks.MOONWORM, TFBlockEntities.MOONWORM));
		r.register(blockItem(TFBlocks.FIREFLY_JAR));
		r.register(blockItem(TFBlocks.FIREFLY_SPAWNER));
		r.register(blockItem(TFBlocks.CICADA_JAR));
		r.register(blockItem(TFBlocks.HEDGE));
		r.register(blockItem(TFBlocks.ROOT_BLOCK));
		r.register(blockItem(TFBlocks.LIVEROOT_BLOCK));
		r.register(blockItem(TFBlocks.MANGROVE_ROOT));

		r.register(blockItem(TFBlocks.TWILIGHT_OAK_LEAVES));
		r.register(blockItem(TFBlocks.CANOPY_LEAVES));
		r.register(blockItem(TFBlocks.MANGROVE_LEAVES));
		r.register(blockItem(TFBlocks.DARK_LEAVES));
		r.register(blockItem(TFBlocks.TIME_LEAVES));
		r.register(blockItem(TFBlocks.TRANSFORMATION_LEAVES));
		r.register(blockItem(TFBlocks.MINING_LEAVES));
		r.register(blockItem(TFBlocks.SORTING_LEAVES));
		r.register(blockItem(TFBlocks.RAINBOW_OAK_LEAVES));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_LOG));
		r.register(blockItem(TFBlocks.CANOPY_LOG));
		r.register(blockItem(TFBlocks.MANGROVE_LOG));
		r.register(blockItem(TFBlocks.DARK_LOG));
		r.register(blockItem(TFBlocks.TIME_LOG));
		r.register(blockItem(TFBlocks.TRANSFORMATION_LOG));
		r.register(blockItem(TFBlocks.MINING_LOG));
		r.register(blockItem(TFBlocks.SORTING_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_CANOPY_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_MANGROVE_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_DARK_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_TIME_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_TRANSFORMATION_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_MINING_LOG));
		r.register(blockItem(TFBlocks.STRIPPED_SORTING_LOG));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_WOOD));
		r.register(blockItem(TFBlocks.CANOPY_WOOD));
		r.register(blockItem(TFBlocks.MANGROVE_WOOD));
		r.register(blockItem(TFBlocks.DARK_WOOD));
		r.register(blockItem(TFBlocks.TIME_WOOD));
		r.register(blockItem(TFBlocks.TRANSFORMATION_WOOD));
		r.register(blockItem(TFBlocks.MINING_WOOD));
		r.register(blockItem(TFBlocks.SORTING_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_CANOPY_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_MANGROVE_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_DARK_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_TIME_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_TRANSFORMATION_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_MINING_WOOD));
		r.register(blockItem(TFBlocks.STRIPPED_SORTING_WOOD));

		r.register(blockItem(TFBlocks.TIME_LOG_CORE));
		r.register(blockItem(TFBlocks.TRANSFORMATION_LOG_CORE));
		r.register(blockItem(TFBlocks.MINING_LOG_CORE));
		r.register(blockItem(TFBlocks.SORTING_LOG_CORE));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_SAPLING));
		r.register(blockItem(TFBlocks.CANOPY_SAPLING));
		r.register(blockItem(TFBlocks.MANGROVE_SAPLING));
		r.register(blockItem(TFBlocks.DARKWOOD_SAPLING));
		r.register(blockItem(TFBlocks.HOLLOW_OAK_SAPLING));
		r.register(blockItem(TFBlocks.TIME_SAPLING));
		r.register(blockItem(TFBlocks.TRANSFORMATION_SAPLING));
		r.register(blockItem(TFBlocks.MINING_SAPLING));
		r.register(blockItem(TFBlocks.SORTING_SAPLING));
		r.register(blockItem(TFBlocks.RAINBOW_OAK_SAPLING));

		r.register(blockItem(TFBlocks.OAK_BANISTER));
		r.register(blockItem(TFBlocks.SPRUCE_BANISTER));
		r.register(blockItem(TFBlocks.BIRCH_BANISTER));
		r.register(blockItem(TFBlocks.JUNGLE_BANISTER));
		r.register(blockItem(TFBlocks.ACACIA_BANISTER));
		r.register(blockItem(TFBlocks.DARK_OAK_BANISTER));
		r.register(blockItem(TFBlocks.CRIMSON_BANISTER));
		r.register(blockItem(TFBlocks.WARPED_BANISTER));

		r.register(hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, "hollow_oak_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, "hollow_spruce_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, "hollow_birch_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, "hollow_jungle_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, "hollow_acacia_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, "hollow_dark_oak_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, "hollow_crimson_stem"));
		r.register(hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, "hollow_warped_stem"));

		r.register(hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, "hollow_twilight_oak_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, "hollow_canopy_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, "hollow_mangrove_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, "hollow_dark_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, "hollow_time_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, "hollow_transformation_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, "hollow_mining_log"));
		r.register(hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, "hollow_sorting_log"));

		r.register(blockItem(TFBlocks.TWILIGHT_OAK_PLANKS));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_STAIRS));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_SLAB));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_BUTTON));
		r.register(burningItem(TFBlocks.TWILIGHT_OAK_FENCE, 300));
		r.register(burningItem(TFBlocks.TWILIGHT_OAK_GATE, 300));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_PLATE));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_TRAPDOOR));
		r.register(tallBlock(TFBlocks.TWILIGHT_OAK_DOOR));
		r.register(signBlock(TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN));
		r.register(blockItem(TFBlocks.TWILIGHT_OAK_BANISTER));
		r.register(blockItem(TFBlocks.CANOPY_PLANKS));
		r.register(blockItem(TFBlocks.CANOPY_STAIRS));
		r.register(blockItem(TFBlocks.CANOPY_SLAB));
		r.register(blockItem(TFBlocks.CANOPY_BUTTON));
		r.register(burningItem(TFBlocks.CANOPY_FENCE, 300));
		r.register(burningItem(TFBlocks.CANOPY_GATE, 300));
		r.register(blockItem(TFBlocks.CANOPY_PLATE));
		r.register(blockItem(TFBlocks.CANOPY_TRAPDOOR));
		r.register(tallBlock(TFBlocks.CANOPY_DOOR));
		r.register(signBlock(TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN));
		r.register(blockItem(TFBlocks.CANOPY_BANISTER));
		r.register(blockItem(TFBlocks.CANOPY_BOOKSHELF));
		r.register(blockItem(TFBlocks.MANGROVE_PLANKS));
		r.register(blockItem(TFBlocks.MANGROVE_STAIRS));
		r.register(blockItem(TFBlocks.MANGROVE_SLAB));
		r.register(blockItem(TFBlocks.MANGROVE_BUTTON));
		r.register(burningItem(TFBlocks.MANGROVE_FENCE, 300));
		r.register(burningItem(TFBlocks.MANGROVE_GATE, 300));
		r.register(blockItem(TFBlocks.MANGROVE_PLATE));
		r.register(blockItem(TFBlocks.MANGROVE_TRAPDOOR));
		r.register(tallBlock(TFBlocks.MANGROVE_DOOR));
		r.register(signBlock(TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN));
		r.register(blockItem(TFBlocks.MANGROVE_BANISTER));
		r.register(blockItem(TFBlocks.DARK_PLANKS));
		r.register(blockItem(TFBlocks.DARK_STAIRS));
		r.register(blockItem(TFBlocks.DARK_SLAB));
		r.register(blockItem(TFBlocks.DARK_BUTTON));
		r.register(burningItem(TFBlocks.DARK_FENCE, 300));
		r.register(burningItem(TFBlocks.DARK_GATE, 300));
		r.register(blockItem(TFBlocks.DARK_PLATE));
		r.register(blockItem(TFBlocks.DARK_TRAPDOOR));
		r.register(tallBlock(TFBlocks.DARK_DOOR));
		r.register(signBlock(TFBlocks.DARKWOOD_SIGN, TFBlocks.DARKWOOD_WALL_SIGN));
		r.register(blockItem(TFBlocks.DARKWOOD_BANISTER));
		r.register(blockItem(TFBlocks.TIME_PLANKS));
		r.register(blockItem(TFBlocks.TIME_STAIRS));
		r.register(blockItem(TFBlocks.TIME_SLAB));
		r.register(blockItem(TFBlocks.TIME_BUTTON));
		r.register(burningItem(TFBlocks.TIME_FENCE, 300));
		r.register(burningItem(TFBlocks.TIME_GATE, 300));
		r.register(blockItem(TFBlocks.TIME_PLATE));
		r.register(blockItem(TFBlocks.TIME_TRAPDOOR));
		r.register(tallBlock(TFBlocks.TIME_DOOR));
		r.register(signBlock(TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN));
		r.register(blockItem(TFBlocks.TIME_BANISTER));
		r.register(blockItem(TFBlocks.TRANSFORMATION_PLANKS));
		r.register(blockItem(TFBlocks.TRANSFORMATION_STAIRS));
		r.register(blockItem(TFBlocks.TRANSFORMATION_SLAB));
		r.register(blockItem(TFBlocks.TRANSFORMATION_BUTTON));
		r.register(burningItem(TFBlocks.TRANSFORMATION_FENCE, 300));
		r.register(burningItem(TFBlocks.TRANSFORMATION_GATE, 300));
		r.register(blockItem(TFBlocks.TRANSFORMATION_PLATE));
		r.register(blockItem(TFBlocks.TRANSFORMATION_TRAPDOOR));
		r.register(tallBlock(TFBlocks.TRANSFORMATION_DOOR));
		r.register(signBlock(TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN));
		r.register(blockItem(TFBlocks.TRANSFORMATION_BANISTER));
		r.register(blockItem(TFBlocks.MINING_PLANKS));
		r.register(blockItem(TFBlocks.MINING_STAIRS));
		r.register(blockItem(TFBlocks.MINING_SLAB));
		r.register(blockItem(TFBlocks.MINING_BUTTON));
		r.register(burningItem(TFBlocks.MINING_FENCE, 300));
		r.register(burningItem(TFBlocks.MINING_GATE, 300));
		r.register(blockItem(TFBlocks.MINING_PLATE));
		r.register(blockItem(TFBlocks.MINING_TRAPDOOR));
		r.register(tallBlock(TFBlocks.MINING_DOOR));
		r.register(signBlock(TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN));
		r.register(blockItem(TFBlocks.MINING_BANISTER));
		r.register(blockItem(TFBlocks.SORTING_PLANKS));
		r.register(blockItem(TFBlocks.SORTING_STAIRS));
		r.register(blockItem(TFBlocks.SORTING_SLAB));
		r.register(blockItem(TFBlocks.SORTING_BUTTON));
		r.register(burningItem(TFBlocks.SORTING_FENCE, 300));
		r.register(burningItem(TFBlocks.SORTING_GATE, 300));
		r.register(blockItem(TFBlocks.SORTING_PLATE));
		r.register(blockItem(TFBlocks.SORTING_TRAPDOOR));
		r.register(tallBlock(TFBlocks.SORTING_DOOR));
		r.register(signBlock(TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN));
		r.register(blockItem(TFBlocks.SORTING_BANISTER));

		makeBEWLRItem(r, TFBlocks.TWILIGHT_OAK_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.CANOPY_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.MANGROVE_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.DARKWOOD_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.TIME_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.TRANSFORMATION_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.MINING_CHEST, TFBlockEntities.TF_CHEST.getId());
		makeBEWLRItem(r, TFBlocks.SORTING_CHEST, TFBlockEntities.TF_CHEST.getId());
	}

	private static <B extends Block> Item hollowLog(RegistryObject<HollowLogHorizontal> horizontalLog, RegistryObject<HollowLogVertical> verticalLog, RegistryObject<HollowLogClimbable> climbable, String name) {
		return new HollowLogItem(horizontalLog, verticalLog, climbable, TFItems.defaultBuilder()).setRegistryName(TwilightForestMod.ID, name);
	}

	private static <B extends Block> Item blockItem(RegistryObject<B> block) {
		return makeBlockItem(new BlockItem(block.get(), TFItems.defaultBuilder()), block);
	}

	private static <B extends AbstractSkullCandleBlock> Item skullCandleItem(RegistryObject<B> floor, RegistryObject<B> wall) {
		return makeBlockItem(new SkullCandleItem(floor.get(), wall.get(), TFItems.defaultBuilder().rarity(Rarity.UNCOMMON)) {
			@Override
			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
				consumer.accept(new IItemRenderProperties() {
					@Override
					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
						return new ISTER(TFBlockEntities.SKULL_CANDLE.getId());
					}
				});
			}
		}, floor);
	}

	private static <B extends Block> Item burningItem(RegistryObject<B> block, int burntime) {
		return makeBlockItem(new FurnaceFuelItem(block.get(), TFItems.defaultBuilder(), burntime), block);
	}

	private static <B extends Block, W extends Block> Item trophyBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return makeBlockItem(new TrophyItem(block.get(), wallblock.get(), TFItems.defaultBuilder().rarity(TwilightForestMod.getRarity())) {
			@Override
			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
				consumer.accept(new IItemRenderProperties() {
					@Override
					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
						return new ISTER(TFBlockEntities.TROPHY.getId());
					}
				});
			}
		}, block);
	}

	private static <T extends Block, E extends BlockEntity> Item wearableBlock(RegistryObject<T> block, RegistryObject<BlockEntityType<E>> tileentity) {
		return makeBlockItem(new WearableItem(block.get(), TFItems.defaultBuilder()) {
			@Override
			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
				consumer.accept(new IItemRenderProperties() {
					@Override
					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
						return new ISTER(tileentity.getId());
					}
				});
			}
		}, block);
	}

	private static <B extends Block> Item tallBlock(RegistryObject<B> block) {
		return makeBlockItem(new DoubleHighBlockItem(block.get(), TFItems.defaultBuilder()), block);
	}

	private static <B extends Block, W extends Block> Item signBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return makeBlockItem(new SignItem(TFItems.defaultBuilder().stacksTo(16), block.get(), wallblock.get()), block);
	}

	private static Item makeBlockItem(Item blockitem, RegistryObject<? extends Block> block) {
		return blockitem.setRegistryName(Objects.requireNonNull(block.getId()));
	}

	private static void makeBEWLRItem(IForgeRegistry<Item> r, RegistryObject<? extends Block> block, ResourceLocation rl) {
		r.register(makeBlockItem(new BlockItem(block.get(), TFItems.defaultBuilder()) {
			@Override
			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
				consumer.accept(new IItemRenderProperties() {
					@Override
					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
						return new ISTER(rl);
					}
				});
			}
		}, block));
	}
}
