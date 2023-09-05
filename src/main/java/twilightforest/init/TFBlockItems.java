package twilightforest.init;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.HollowLogClimbable;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.HollowLogVertical;
import twilightforest.client.ISTER;
import twilightforest.item.*;

import java.util.Objects;
import java.util.function.Consumer;

//TODO move this out of the register event. I would ideally like to register these alongside blocks
public class TFBlockItems {

	public static void registerBlockItems(RegisterEvent event) {
		if(Objects.equals(event.getForgeRegistry(), ForgeRegistries.ITEMS)) {

			register(event, trophyBlock(TFBlocks.NAGA_TROPHY, TFBlocks.NAGA_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.LICH_TROPHY, TFBlocks.LICH_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.MINOSHROOM_TROPHY, TFBlocks.MINOSHROOM_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.HYDRA_TROPHY, TFBlocks.HYDRA_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.UR_GHAST_TROPHY, TFBlocks.UR_GHAST_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.ALPHA_YETI_TROPHY, TFBlocks.ALPHA_YETI_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY));
			register(event, trophyBlock(TFBlocks.QUEST_RAM_TROPHY, TFBlocks.QUEST_RAM_WALL_TROPHY));

			register(event, blockItem(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE));
			register(event, blockItem(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE));
			register(event, blockItem(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE));
//      register(event, blockItem(TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE));

			register(event, blockItem(TFBlocks.NAGA_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.LICH_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.MINOSHROOM_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.HYDRA_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.UR_GHAST_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.ALPHA_YETI_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER));
			register(event, blockItem(TFBlocks.FINAL_BOSS_BOSS_SPAWNER));

			register(event, blockItem(TFBlocks.ETCHED_NAGASTONE));
			register(event, blockItem(TFBlocks.CRACKED_ETCHED_NAGASTONE));
			register(event, blockItem(TFBlocks.MOSSY_ETCHED_NAGASTONE));
			register(event, blockItem(TFBlocks.NAGASTONE_PILLAR));
			register(event, blockItem(TFBlocks.CRACKED_NAGASTONE_PILLAR));
			register(event, blockItem(TFBlocks.MOSSY_NAGASTONE_PILLAR));
			register(event, blockItem(TFBlocks.NAGASTONE_STAIRS_LEFT));
			register(event, blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT));
			register(event, blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT));
			register(event, blockItem(TFBlocks.NAGASTONE_STAIRS_RIGHT));
			register(event, blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT));
			register(event, blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT));
			register(event, blockItem(TFBlocks.NAGASTONE_HEAD));
			register(event, blockItem(TFBlocks.NAGASTONE));
			register(event, blockItem(TFBlocks.SPIRAL_BRICKS));
//      register(event, blockItem(TFBlocks.TERRORCOTTA_CIRCLE));
//      register(event, blockItem(TFBlocks.TERRORCOTTA_DIAGONAL));
//      register(event, blockItem(TFBlocks.LAPIS_BLOCK));
			register(event, blockItem(TFBlocks.TWISTED_STONE));
			register(event, blockItem(TFBlocks.TWISTED_STONE_PILLAR));
			register(event, new BlockItem(TFBlocks.KEEPSAKE_CASKET.get(), new Item.Properties().fireResistant()) {
				@Override
				public void initializeClient(Consumer<IClientItemExtensions> consumer) {
					consumer.accept(new IClientItemExtensions() {
						@Override
						public BlockEntityWithoutLevelRenderer getCustomRenderer() {
							return new ISTER();
						}
					});
				}
			});
			register(event, blockItem(TFBlocks.CANDELABRA));
			register(event, blockItem(TFBlocks.BOLD_STONE_PILLAR));
			register(event, blockItem(TFBlocks.DEATH_TOME_SPAWNER));
			register(event, blockItem(TFBlocks.EMPTY_CANOPY_BOOKSHELF));
			register(event, skullCandleItem(TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE));
			register(event, skullCandleItem(TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE));
			register(event, skullCandleItem(TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE));
			register(event, skullCandleItem(TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE));
			register(event, skullCandleItem(TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE));
			register(event, new HugeWaterLilyItem(TFBlocks.HUGE_WATER_LILY.get(), new Item.Properties()));
			register(event, new HugeLilyPadItem(TFBlocks.HUGE_LILY_PAD.get(), new Item.Properties()));
			register(event, blockItem(TFBlocks.MAZESTONE));
			register(event, blockItem(TFBlocks.MAZESTONE_BRICK));
			register(event, blockItem(TFBlocks.CRACKED_MAZESTONE));
			register(event, blockItem(TFBlocks.MOSSY_MAZESTONE));
			register(event, blockItem(TFBlocks.DECORATIVE_MAZESTONE));
			register(event, blockItem(TFBlocks.CUT_MAZESTONE));
			register(event, blockItem(TFBlocks.MAZESTONE_BORDER));
			register(event, blockItem(TFBlocks.MAZESTONE_MOSAIC));
			register(event, blockItem(TFBlocks.SMOKER));
			register(event, blockItem(TFBlocks.ENCASED_SMOKER));
			register(event, blockItem(TFBlocks.FIRE_JET));
			register(event, blockItem(TFBlocks.ENCASED_FIRE_JET));
			register(event, blockItem(TFBlocks.STRONGHOLD_SHIELD));
			register(event, blockItem(TFBlocks.TROPHY_PEDESTAL));
			register(event, blockItem(TFBlocks.UNDERBRICK));
			register(event, blockItem(TFBlocks.CRACKED_UNDERBRICK));
			register(event, blockItem(TFBlocks.MOSSY_UNDERBRICK));
			register(event, blockItem(TFBlocks.UNDERBRICK_FLOOR));
			register(event, blockItem(TFBlocks.TOWERWOOD));
			register(event, blockItem(TFBlocks.CRACKED_TOWERWOOD));
			register(event, blockItem(TFBlocks.MOSSY_TOWERWOOD));
			register(event, blockItem(TFBlocks.INFESTED_TOWERWOOD));
			register(event, blockItem(TFBlocks.ENCASED_TOWERWOOD));
			register(event, blockItem(TFBlocks.VANISHING_BLOCK));
			register(event, blockItem(TFBlocks.REAPPEARING_BLOCK));
			register(event, blockItem(TFBlocks.LOCKED_VANISHING_BLOCK));
			register(event, blockItem(TFBlocks.CARMINITE_BUILDER));
			register(event, blockItem(TFBlocks.ANTIBUILDER));
			register(event, blockItem(TFBlocks.CARMINITE_REACTOR));
			register(event, blockItem(TFBlocks.GHAST_TRAP));
			register(event, blockItem(TFBlocks.AURORA_BLOCK));
			register(event, blockItem(TFBlocks.AURORA_PILLAR));
			register(event, blockItem(TFBlocks.AURORA_SLAB));
			register(event, blockItem(TFBlocks.AURORALIZED_GLASS));
			register(event, blockItem(TFBlocks.TROLLSTEINN));
			register(event, blockItem(TFBlocks.TROLLVIDR));
			register(event, blockItem(TFBlocks.UNRIPE_TROLLBER));
			register(event, blockItem(TFBlocks.TROLLBER));
			register(event, blockItem(TFBlocks.HUGE_MUSHGLOOM));
			register(event, blockItem(TFBlocks.HUGE_MUSHGLOOM_STEM));
			register(event, blockItem(TFBlocks.UBEROUS_SOIL));
			register(event, blockItem(TFBlocks.HUGE_STALK));
			register(event, blockItem(TFBlocks.BEANSTALK_LEAVES));
			register(event, blockItem(TFBlocks.WISPY_CLOUD));
			register(event, blockItem(TFBlocks.FLUFFY_CLOUD));
			register(event, blockItem(TFBlocks.GIANT_COBBLESTONE));
			register(event, blockItem(TFBlocks.GIANT_LOG));
			register(event, blockItem(TFBlocks.GIANT_LEAVES));
			register(event, blockItem(TFBlocks.GIANT_OBSIDIAN));
			register(event, blockItem(TFBlocks.DEADROCK));
			register(event, blockItem(TFBlocks.CRACKED_DEADROCK));
			register(event, blockItem(TFBlocks.WEATHERED_DEADROCK));
			register(event, blockItem(TFBlocks.BROWN_THORNS));
			register(event, blockItem(TFBlocks.GREEN_THORNS));
			register(event, blockItem(TFBlocks.BURNT_THORNS));
			register(event, blockItem(TFBlocks.THORN_ROSE));
			register(event, blockItem(TFBlocks.THORN_LEAVES));
			register(event, blockItem(TFBlocks.CASTLE_BRICK));
			register(event, blockItem(TFBlocks.WORN_CASTLE_BRICK));
			register(event, blockItem(TFBlocks.CRACKED_CASTLE_BRICK));
			register(event, blockItem(TFBlocks.MOSSY_CASTLE_BRICK));
			register(event, blockItem(TFBlocks.THICK_CASTLE_BRICK));
			register(event, blockItem(TFBlocks.CASTLE_ROOF_TILE));
			register(event, blockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR));
			register(event, blockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE));
			register(event, blockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR));
			register(event, blockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE));
			register(event, blockItem(TFBlocks.CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.WORN_CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.BOLD_CASTLE_BRICK_STAIRS));
			register(event, blockItem(TFBlocks.PINK_CASTLE_RUNE_BRICK));
			register(event, blockItem(TFBlocks.YELLOW_CASTLE_RUNE_BRICK));
			register(event, blockItem(TFBlocks.BLUE_CASTLE_RUNE_BRICK));
			register(event, blockItem(TFBlocks.VIOLET_CASTLE_RUNE_BRICK));
			register(event, blockItem(TFBlocks.PINK_CASTLE_DOOR));
			register(event, blockItem(TFBlocks.YELLOW_CASTLE_DOOR));
			register(event, blockItem(TFBlocks.BLUE_CASTLE_DOOR));
			register(event, blockItem(TFBlocks.VIOLET_CASTLE_DOOR));
			register(event, blockItem(TFBlocks.PINK_FORCE_FIELD));
			register(event, blockItem(TFBlocks.ORANGE_FORCE_FIELD));
			register(event, blockItem(TFBlocks.GREEN_FORCE_FIELD));
			register(event, blockItem(TFBlocks.BLUE_FORCE_FIELD));
			register(event, blockItem(TFBlocks.VIOLET_FORCE_FIELD));

			register(event, blockItem(TFBlocks.UNCRAFTING_TABLE));
			register(event, blockItem(TFBlocks.CINDER_FURNACE));
			register(event, blockItem(TFBlocks.CINDER_LOG));
			register(event, blockItem(TFBlocks.CINDER_WOOD));
			register(event, blockItem(TFBlocks.SLIDER));
			register(event, blockItem(TFBlocks.IRON_LADDER));

			register(event, blockItem(TFBlocks.IRONWOOD_BLOCK));
			register(event, blockItem(TFBlocks.STEELEAF_BLOCK));
			register(event, fireImmuneBlock(TFBlocks.FIERY_BLOCK));
			register(event, blockItem(TFBlocks.KNIGHTMETAL_BLOCK));
			register(event, blockItem(TFBlocks.CARMINITE_BLOCK));
			register(event, blockItem(TFBlocks.ARCTIC_FUR_BLOCK));

			register(event, blockItem(TFBlocks.MOSS_PATCH));
			register(event, blockItem(TFBlocks.MAYAPPLE));
			register(event, blockItem(TFBlocks.CLOVER_PATCH));
			register(event, blockItem(TFBlocks.FIDDLEHEAD));
			register(event, blockItem(TFBlocks.MUSHGLOOM));
			register(event, blockItem(TFBlocks.TORCHBERRY_PLANT));
			register(event, blockItem(TFBlocks.ROOT_STRAND));
			register(event, placeOnWaterBlockItemReplaceable(TFBlocks.FALLEN_LEAVES));
			register(event, wearableBlock(TFBlocks.FIREFLY, TFBlockEntities.FIREFLY));
			register(event, wearableBlock(TFBlocks.CICADA, TFBlockEntities.CICADA));
			register(event, wearableBlock(TFBlocks.MOONWORM, TFBlockEntities.MOONWORM));
			register(event, blockItem(TFBlocks.FIREFLY_JAR));
			register(event, blockItem(TFBlocks.FIREFLY_SPAWNER));
			register(event, blockItem(TFBlocks.CICADA_JAR));
			register(event, blockItem(TFBlocks.HEDGE));
			register(event, blockItem(TFBlocks.ROOT_BLOCK));
			register(event, blockItem(TFBlocks.LIVEROOT_BLOCK));
			register(event, blockItem(TFBlocks.MANGROVE_ROOT));

			register(event, blockItem(TFBlocks.TWILIGHT_OAK_LEAVES));
			register(event, blockItem(TFBlocks.CANOPY_LEAVES));
			register(event, blockItem(TFBlocks.MANGROVE_LEAVES));
			register(event, blockItem(TFBlocks.DARK_LEAVES));
			register(event, blockItem(TFBlocks.TIME_LEAVES));
			register(event, blockItem(TFBlocks.TRANSFORMATION_LEAVES));
			register(event, blockItem(TFBlocks.MINING_LEAVES));
			register(event, blockItem(TFBlocks.SORTING_LEAVES));
			register(event, blockItem(TFBlocks.RAINBOW_OAK_LEAVES));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_LOG));
			register(event, blockItem(TFBlocks.CANOPY_LOG));
			register(event, blockItem(TFBlocks.MANGROVE_LOG));
			register(event, blockItem(TFBlocks.DARK_LOG));
			register(event, blockItem(TFBlocks.TIME_LOG));
			register(event, blockItem(TFBlocks.TRANSFORMATION_LOG));
			register(event, blockItem(TFBlocks.MINING_LOG));
			register(event, blockItem(TFBlocks.SORTING_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_CANOPY_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_MANGROVE_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_DARK_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_TIME_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_TRANSFORMATION_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_MINING_LOG));
			register(event, blockItem(TFBlocks.STRIPPED_SORTING_LOG));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_WOOD));
			register(event, blockItem(TFBlocks.CANOPY_WOOD));
			register(event, blockItem(TFBlocks.MANGROVE_WOOD));
			register(event, blockItem(TFBlocks.DARK_WOOD));
			register(event, blockItem(TFBlocks.TIME_WOOD));
			register(event, blockItem(TFBlocks.TRANSFORMATION_WOOD));
			register(event, blockItem(TFBlocks.MINING_WOOD));
			register(event, blockItem(TFBlocks.SORTING_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_CANOPY_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_MANGROVE_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_DARK_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_TIME_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_TRANSFORMATION_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_MINING_WOOD));
			register(event, blockItem(TFBlocks.STRIPPED_SORTING_WOOD));

			register(event, blockItem(TFBlocks.TIME_LOG_CORE));
			register(event, blockItem(TFBlocks.TRANSFORMATION_LOG_CORE));
			register(event, blockItem(TFBlocks.MINING_LOG_CORE));
			register(event, blockItem(TFBlocks.SORTING_LOG_CORE));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_SAPLING));
			register(event, blockItem(TFBlocks.CANOPY_SAPLING));
			register(event, blockItem(TFBlocks.MANGROVE_SAPLING));
			register(event, blockItem(TFBlocks.DARKWOOD_SAPLING));
			register(event, blockItem(TFBlocks.HOLLOW_OAK_SAPLING));
			register(event, blockItem(TFBlocks.TIME_SAPLING));
			register(event, blockItem(TFBlocks.TRANSFORMATION_SAPLING));
			register(event, blockItem(TFBlocks.MINING_SAPLING));
			register(event, blockItem(TFBlocks.SORTING_SAPLING));
			register(event, blockItem(TFBlocks.RAINBOW_OAK_SAPLING));

			register(event, burningItem(TFBlocks.OAK_BANISTER, 300));
			register(event, burningItem(TFBlocks.SPRUCE_BANISTER, 300));
			register(event, burningItem(TFBlocks.BIRCH_BANISTER, 300));
			register(event, burningItem(TFBlocks.JUNGLE_BANISTER, 300));
			register(event, burningItem(TFBlocks.ACACIA_BANISTER, 300));
			register(event, burningItem(TFBlocks.DARK_OAK_BANISTER, 300));
			register(event, burningItem(TFBlocks.CRIMSON_BANISTER, 300));
			register(event, burningItem(TFBlocks.WARPED_BANISTER, 300));
			register(event, burningItem(TFBlocks.VANGROVE_BANISTER, 300));

			registerWithCustomName(event, "hollow_oak_log", hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_spruce_log", hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_birch_log", hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_jungle_log", hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_acacia_log", hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_dark_oak_log", hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_crimson_stem", hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE));
			registerWithCustomName(event, "hollow_warped_stem", hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE));
			registerWithCustomName(event, "hollow_vangrove_log", hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE));

			registerWithCustomName(event, "hollow_twilight_oak_log", hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_canopy_log", hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_mangrove_log", hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_dark_log", hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_time_log", hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_transformation_log", hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_mining_log", hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE));
			registerWithCustomName(event, "hollow_sorting_log", hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE));

			register(event, blockItem(TFBlocks.TWILIGHT_OAK_PLANKS));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_STAIRS));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_SLAB));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_BUTTON));
			register(event, burningItem(TFBlocks.TWILIGHT_OAK_FENCE, 300));
			register(event, burningItem(TFBlocks.TWILIGHT_OAK_GATE, 300));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_PLATE));
			register(event, blockItem(TFBlocks.TWILIGHT_OAK_TRAPDOOR));
			register(event, tallBlock(TFBlocks.TWILIGHT_OAK_DOOR));
			register(event, signBlock(TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN));
			register(event, burningItem(TFBlocks.TWILIGHT_OAK_BANISTER, 300));
			register(event, blockItem(TFBlocks.CANOPY_PLANKS));
			register(event, blockItem(TFBlocks.CANOPY_STAIRS));
			register(event, blockItem(TFBlocks.CANOPY_SLAB));
			register(event, blockItem(TFBlocks.CANOPY_BUTTON));
			register(event, burningItem(TFBlocks.CANOPY_FENCE, 300));
			register(event, burningItem(TFBlocks.CANOPY_GATE, 300));
			register(event, blockItem(TFBlocks.CANOPY_PLATE));
			register(event, blockItem(TFBlocks.CANOPY_TRAPDOOR));
			register(event, tallBlock(TFBlocks.CANOPY_DOOR));
			register(event, signBlock(TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN));
			register(event, burningItem(TFBlocks.CANOPY_BANISTER, 300));
			register(event, blockItem(TFBlocks.CANOPY_BOOKSHELF));
			register(event, blockItem(TFBlocks.MANGROVE_PLANKS));
			register(event, blockItem(TFBlocks.MANGROVE_STAIRS));
			register(event, blockItem(TFBlocks.MANGROVE_SLAB));
			register(event, blockItem(TFBlocks.MANGROVE_BUTTON));
			register(event, burningItem(TFBlocks.MANGROVE_FENCE, 300));
			register(event, burningItem(TFBlocks.MANGROVE_GATE, 300));
			register(event, blockItem(TFBlocks.MANGROVE_PLATE));
			register(event, blockItem(TFBlocks.MANGROVE_TRAPDOOR));
			register(event, tallBlock(TFBlocks.MANGROVE_DOOR));
			register(event, signBlock(TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN));
			register(event, burningItem(TFBlocks.MANGROVE_BANISTER, 300));
			register(event, blockItem(TFBlocks.DARK_PLANKS));
			register(event, blockItem(TFBlocks.DARK_STAIRS));
			register(event, blockItem(TFBlocks.DARK_SLAB));
			register(event, blockItem(TFBlocks.DARK_BUTTON));
			register(event, burningItem(TFBlocks.DARK_FENCE, 300));
			register(event, burningItem(TFBlocks.DARK_GATE, 300));
			register(event, blockItem(TFBlocks.DARK_PLATE));
			register(event, blockItem(TFBlocks.DARK_TRAPDOOR));
			register(event, tallBlock(TFBlocks.DARK_DOOR));
			register(event, signBlock(TFBlocks.DARK_SIGN, TFBlocks.DARK_WALL_SIGN));
			register(event, burningItem(TFBlocks.DARK_BANISTER, 300));
			register(event, blockItem(TFBlocks.TIME_PLANKS));
			register(event, blockItem(TFBlocks.TIME_STAIRS));
			register(event, blockItem(TFBlocks.TIME_SLAB));
			register(event, blockItem(TFBlocks.TIME_BUTTON));
			register(event, burningItem(TFBlocks.TIME_FENCE, 300));
			register(event, burningItem(TFBlocks.TIME_GATE, 300));
			register(event, blockItem(TFBlocks.TIME_PLATE));
			register(event, blockItem(TFBlocks.TIME_TRAPDOOR));
			register(event, tallBlock(TFBlocks.TIME_DOOR));
			register(event, signBlock(TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN));
			register(event, burningItem(TFBlocks.TIME_BANISTER, 300));
			register(event, blockItem(TFBlocks.TRANSFORMATION_PLANKS));
			register(event, blockItem(TFBlocks.TRANSFORMATION_STAIRS));
			register(event, blockItem(TFBlocks.TRANSFORMATION_SLAB));
			register(event, blockItem(TFBlocks.TRANSFORMATION_BUTTON));
			register(event, burningItem(TFBlocks.TRANSFORMATION_FENCE, 300));
			register(event, burningItem(TFBlocks.TRANSFORMATION_GATE, 300));
			register(event, blockItem(TFBlocks.TRANSFORMATION_PLATE));
			register(event, blockItem(TFBlocks.TRANSFORMATION_TRAPDOOR));
			register(event, tallBlock(TFBlocks.TRANSFORMATION_DOOR));
			register(event, signBlock(TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN));
			register(event, burningItem(TFBlocks.TRANSFORMATION_BANISTER, 300));
			register(event, blockItem(TFBlocks.MINING_PLANKS));
			register(event, blockItem(TFBlocks.MINING_STAIRS));
			register(event, blockItem(TFBlocks.MINING_SLAB));
			register(event, blockItem(TFBlocks.MINING_BUTTON));
			register(event, burningItem(TFBlocks.MINING_FENCE, 300));
			register(event, burningItem(TFBlocks.MINING_GATE, 300));
			register(event, blockItem(TFBlocks.MINING_PLATE));
			register(event, blockItem(TFBlocks.MINING_TRAPDOOR));
			register(event, tallBlock(TFBlocks.MINING_DOOR));
			register(event, signBlock(TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN));
			register(event, burningItem(TFBlocks.MINING_BANISTER, 300));
			register(event, blockItem(TFBlocks.SORTING_PLANKS));
			register(event, blockItem(TFBlocks.SORTING_STAIRS));
			register(event, blockItem(TFBlocks.SORTING_SLAB));
			register(event, blockItem(TFBlocks.SORTING_BUTTON));
			register(event, burningItem(TFBlocks.SORTING_FENCE, 300));
			register(event, burningItem(TFBlocks.SORTING_GATE, 300));
			register(event, blockItem(TFBlocks.SORTING_PLATE));
			register(event, blockItem(TFBlocks.SORTING_TRAPDOOR));
			register(event, tallBlock(TFBlocks.SORTING_DOOR));
			register(event, signBlock(TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN));
			register(event, burningItem(TFBlocks.SORTING_BANISTER, 300));

			makeBEWLRItem(event, TFBlocks.TWILIGHT_OAK_CHEST);
			makeBEWLRItem(event, TFBlocks.CANOPY_CHEST);
			makeBEWLRItem(event, TFBlocks.MANGROVE_CHEST);
			makeBEWLRItem(event, TFBlocks.DARK_CHEST);
			makeBEWLRItem(event, TFBlocks.TIME_CHEST);
			makeBEWLRItem(event, TFBlocks.TRANSFORMATION_CHEST);
			makeBEWLRItem(event, TFBlocks.MINING_CHEST);
			makeBEWLRItem(event, TFBlocks.SORTING_CHEST);
		}
	}

	private static BlockItem hollowLog(RegistryObject<HollowLogHorizontal> horizontalLog, RegistryObject<HollowLogVertical> verticalLog, RegistryObject<HollowLogClimbable> climbable) {
		return new HollowLogItem(horizontalLog, verticalLog, climbable, new Item.Properties());
	}

	private static <B extends Block> BlockItem blockItem(RegistryObject<B> block) {
		return new BlockItem(block.get(), new Item.Properties());
	}

	private static <B extends Block> BlockItem placeOnWaterBlockItemReplaceable(RegistryObject<B> block) {
		return new BlockItem(block.get(), new Item.Properties()) {
			@Override
			public InteractionResult useOn(UseOnContext context) {
				return context.getLevel().getBlockState(context.getClickedPos()).is(block.get()) ? super.useOn(context) : InteractionResult.PASS;
			}
			@Override
			public InteractionResultHolder<ItemStack> use(Level p_220231_, Player p_220232_, InteractionHand p_220233_) {
				BlockHitResult blockhitresult = getPlayerPOVHitResult(p_220231_, p_220232_, ClipContext.Fluid.SOURCE_ONLY);
				BlockHitResult blockhitresult1 = blockhitresult.withPosition(blockhitresult.getBlockPos().above());
				InteractionResult interactionresult = super.useOn(new UseOnContext(p_220232_, p_220233_, blockhitresult1));
				return new InteractionResultHolder<>(interactionresult, p_220232_.getItemInHand(p_220233_));
			}
		};
	}

	private static <B extends Block> BlockItem fireImmuneBlock(RegistryObject<B> block) {
		return new BlockItem(block.get(), new Item.Properties().fireResistant());
	}

	private static <B extends AbstractSkullCandleBlock> BlockItem skullCandleItem(RegistryObject<B> floor, RegistryObject<B> wall) {
		return new SkullCandleItem(floor.get(), wall.get(), new Item.Properties().rarity(Rarity.UNCOMMON)) {
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(new IClientItemExtensions() {
					@Override
					public BlockEntityWithoutLevelRenderer getCustomRenderer() {
						return new ISTER();
					}
				});
			}
		};
	}

	private static <B extends Block> BlockItem burningItem(RegistryObject<B> block, int burntime) {
		return new FurnaceFuelItem(block.get(), new Item.Properties(), burntime);
	}

	private static <B extends Block, W extends Block> BlockItem trophyBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return new TrophyItem(block.get(), wallblock.get(), new Item.Properties().rarity(TwilightForestMod.getRarity())) {
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(new IClientItemExtensions() {
					@Override
					public BlockEntityWithoutLevelRenderer getCustomRenderer() {
						return new ISTER();
					}
				});
			}
		};
	}

	private static <T extends Block, E extends BlockEntity> BlockItem wearableBlock(RegistryObject<T> block, RegistryObject<BlockEntityType<E>> tileentity) {
		return new WearableItem(block.get(), new Item.Properties()) {
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(new IClientItemExtensions() {
					@Override
					public BlockEntityWithoutLevelRenderer getCustomRenderer() {
						return new ISTER();
					}
				});
			}
		};
	}

	private static <B extends Block> BlockItem tallBlock(RegistryObject<B> block) {
		return new DoubleHighBlockItem(block.get(), new Item.Properties());
	}

	private static <B extends Block, W extends Block> BlockItem signBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return new SignItem(new Item.Properties().stacksTo(16), block.get(), wallblock.get());
	}

	private static void makeBEWLRItem(RegisterEvent event, RegistryObject<? extends Block> block) {
		register(event, new BlockItem(block.get(), new Item.Properties()) {
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(new IClientItemExtensions() {
					@Override
					public BlockEntityWithoutLevelRenderer getCustomRenderer() {
						return new ISTER();
					}
				});
			}
		});
	}

	private static void register(RegisterEvent event, BlockItem item) {
		event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ForgeRegistries.BLOCKS.getKey(item.getBlock()), item));
	}

	private static void registerWithCustomName(RegisterEvent event, String name, BlockItem item) {
		event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(name, item));
	}
}
