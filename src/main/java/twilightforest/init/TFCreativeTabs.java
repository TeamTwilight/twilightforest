package twilightforest.init;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Unit;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.components.item.SkullCandles;
import twilightforest.config.TFConfig;
import twilightforest.entity.MagicPaintingVariant;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TFCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TwilightForestMod.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = TABS.register("blocks", () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup.twilightforest.blocks"))
		.icon(() -> new ItemStack(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE))
		.displayItems((parameters, output) -> {
			output.accept(TFBlocks.TWILIGHT_OAK_LOG);
			output.accept(TFBlocks.TWILIGHT_OAK_WOOD);
			output.accept(TFItems.HOLLOW_TWILIGHT_OAK_LOG);
			output.accept(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);
			output.accept(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD);
			output.accept(TFBlocks.TWILIGHT_OAK_PLANKS);
			output.accept(TFBlocks.TWILIGHT_OAK_STAIRS);
			output.accept(TFBlocks.TWILIGHT_OAK_SLAB);
			output.accept(TFBlocks.TWILIGHT_OAK_CHEST);
			output.accept(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST);
			output.accept(TFBlocks.TWILIGHT_OAK_FENCE);
			output.accept(TFBlocks.TWILIGHT_OAK_GATE);
			output.accept(TFBlocks.TWILIGHT_OAK_BANISTER);
			output.accept(TFBlocks.TWILIGHT_OAK_DRYING_RACK);
			output.accept(TFBlocks.TWILIGHT_OAK_DOOR);
			output.accept(TFBlocks.TWILIGHT_OAK_TRAPDOOR);
			output.accept(TFBlocks.TWILIGHT_OAK_PLATE);
			output.accept(TFBlocks.TWILIGHT_OAK_BUTTON);
			output.accept(TFBlocks.TWILIGHT_OAK_SIGN);
			output.accept(TFBlocks.TWILIGHT_OAK_HANGING_SIGN);
			output.accept(TFBlocks.TWILIGHT_OAK_LEAVES);
			output.accept(TFBlocks.TWILIGHT_OAK_SAPLING);

			output.accept(TFBlocks.CANOPY_LOG);
			output.accept(TFBlocks.CANOPY_WOOD);
			output.accept(TFItems.HOLLOW_CANOPY_LOG);
			output.accept(TFBlocks.STRIPPED_CANOPY_LOG);
			output.accept(TFBlocks.STRIPPED_CANOPY_WOOD);
			output.accept(TFBlocks.CANOPY_PLANKS);
			output.accept(TFBlocks.CANOPY_STAIRS);
			output.accept(TFBlocks.CANOPY_SLAB);
			output.accept(TFBlocks.CANOPY_CHEST);
			output.accept(TFBlocks.CANOPY_TRAPPED_CHEST);
			output.accept(TFBlocks.CANOPY_BOOKSHELF);
			output.accept(TFBlocks.CHISELED_CANOPY_BOOKSHELF);
			output.accept(TFBlocks.CANOPY_WINDOW);
			output.accept(TFBlocks.CANOPY_WINDOW_PANE);
			output.accept(TFBlocks.CANOPY_FENCE);
			output.accept(TFBlocks.CANOPY_GATE);
			output.accept(TFBlocks.CANOPY_BANISTER);
			output.accept(TFBlocks.CANOPY_DRYING_RACK);
			output.accept(TFBlocks.CANOPY_DOOR);
			output.accept(TFBlocks.CANOPY_TRAPDOOR);
			output.accept(TFBlocks.CANOPY_PLATE);
			output.accept(TFBlocks.CANOPY_BUTTON);
			output.accept(TFBlocks.CANOPY_SIGN);
			output.accept(TFBlocks.CANOPY_HANGING_SIGN);
			output.accept(TFBlocks.CANOPY_LEAVES);
			output.accept(TFBlocks.CANOPY_SAPLING);

			output.accept(TFBlocks.MANGROVE_LOG);
			output.accept(TFBlocks.MANGROVE_WOOD);
			output.accept(TFItems.HOLLOW_MANGROVE_LOG);
			output.accept(TFBlocks.STRIPPED_MANGROVE_LOG);
			output.accept(TFBlocks.STRIPPED_MANGROVE_WOOD);
			output.accept(TFBlocks.MANGROVE_ROOT);
			output.accept(TFBlocks.MANGROVE_PLANKS);
			output.accept(TFBlocks.MANGROVE_STAIRS);
			output.accept(TFBlocks.MANGROVE_SLAB);
			output.accept(TFBlocks.MANGROVE_CHEST);
			output.accept(TFBlocks.MANGROVE_TRAPPED_CHEST);
			output.accept(TFBlocks.MANGROVE_FENCE);
			output.accept(TFBlocks.MANGROVE_GATE);
			output.accept(TFBlocks.MANGROVE_BANISTER);
			output.accept(TFBlocks.MANGROVE_DRYING_RACK);
			output.accept(TFBlocks.MANGROVE_DOOR);
			output.accept(TFBlocks.MANGROVE_TRAPDOOR);
			output.accept(TFBlocks.MANGROVE_PLATE);
			output.accept(TFBlocks.MANGROVE_BUTTON);
			output.accept(TFBlocks.MANGROVE_SIGN);
			output.accept(TFBlocks.MANGROVE_HANGING_SIGN);
			output.accept(TFBlocks.MANGROVE_LEAVES);
			output.accept(TFBlocks.MANGROVE_SAPLING);

			output.accept(TFBlocks.DARK_LOG);
			output.accept(TFBlocks.DARK_WOOD);
			output.accept(TFItems.HOLLOW_DARK_LOG);
			output.accept(TFBlocks.STRIPPED_DARK_LOG);
			output.accept(TFBlocks.STRIPPED_DARK_WOOD);
			output.accept(TFBlocks.DARK_PLANKS);
			output.accept(TFBlocks.DARK_STAIRS);
			output.accept(TFBlocks.DARK_SLAB);
			output.accept(TFBlocks.DARK_CHEST);
			output.accept(TFBlocks.DARK_TRAPPED_CHEST);
			output.accept(TFBlocks.DARK_FENCE);
			output.accept(TFBlocks.DARK_GATE);
			output.accept(TFBlocks.DARK_BANISTER);
			output.accept(TFBlocks.DARK_DRYING_RACK);
			output.accept(TFBlocks.DARK_DOOR);
			output.accept(TFBlocks.DARK_TRAPDOOR);
			output.accept(TFBlocks.DARK_PLATE);
			output.accept(TFBlocks.DARK_BUTTON);
			output.accept(TFBlocks.DARK_SIGN);
			output.accept(TFBlocks.DARK_HANGING_SIGN);
			output.accept(TFBlocks.DARK_LEAVES);
			output.accept(TFBlocks.DARKWOOD_SAPLING);

			output.accept(TFBlocks.TIME_LOG);
			output.accept(TFBlocks.TIME_WOOD);
			output.accept(TFBlocks.TIME_LOG_CORE);
			output.accept(TFItems.HOLLOW_TIME_LOG);
			output.accept(TFBlocks.STRIPPED_TIME_LOG);
			output.accept(TFBlocks.STRIPPED_TIME_WOOD);
			output.accept(TFBlocks.TIME_PLANKS);
			output.accept(TFBlocks.TIME_STAIRS);
			output.accept(TFBlocks.TIME_SLAB);
			output.accept(TFBlocks.TIME_CHEST);
			output.accept(TFBlocks.TIME_TRAPPED_CHEST);
			output.accept(TFBlocks.TIME_FENCE);
			output.accept(TFBlocks.TIME_GATE);
			output.accept(TFBlocks.TIME_BANISTER);
			output.accept(TFBlocks.TIME_DRYING_RACK);
			output.accept(TFBlocks.TIME_DOOR);
			output.accept(TFBlocks.TIME_TRAPDOOR);
			output.accept(TFBlocks.TIME_PLATE);
			output.accept(TFBlocks.TIME_BUTTON);
			output.accept(TFBlocks.TIME_SIGN);
			output.accept(TFBlocks.TIME_HANGING_SIGN);
			output.accept(TFBlocks.TIME_LEAVES);
			output.accept(TFBlocks.TIME_SAPLING);

			output.accept(TFBlocks.TRANSFORMATION_LOG);
			output.accept(TFBlocks.TRANSFORMATION_WOOD);
			output.accept(TFBlocks.TRANSFORMATION_LOG_CORE);
			output.accept(TFItems.HOLLOW_TRANSFORMATION_LOG);
			output.accept(TFBlocks.STRIPPED_TRANSFORMATION_LOG);
			output.accept(TFBlocks.STRIPPED_TRANSFORMATION_WOOD);
			output.accept(TFBlocks.TRANSFORMATION_PLANKS);
			output.accept(TFBlocks.TRANSFORMATION_STAIRS);
			output.accept(TFBlocks.TRANSFORMATION_SLAB);
			output.accept(TFBlocks.TRANSFORMATION_CHEST);
			output.accept(TFBlocks.TRANSFORMATION_TRAPPED_CHEST);
			output.accept(TFBlocks.TRANSFORMATION_FENCE);
			output.accept(TFBlocks.TRANSFORMATION_GATE);
			output.accept(TFBlocks.TRANSFORMATION_BANISTER);
			output.accept(TFBlocks.TRANSFORMATION_DRYING_RACK);
			output.accept(TFBlocks.TRANSFORMATION_DOOR);
			output.accept(TFBlocks.TRANSFORMATION_TRAPDOOR);
			output.accept(TFBlocks.TRANSFORMATION_PLATE);
			output.accept(TFBlocks.TRANSFORMATION_BUTTON);
			output.accept(TFBlocks.TRANSFORMATION_SIGN);
			output.accept(TFBlocks.TRANSFORMATION_HANGING_SIGN);
			output.accept(TFBlocks.TRANSFORMATION_LEAVES);
			output.accept(TFBlocks.TRANSFORMATION_SAPLING);

			output.accept(TFBlocks.MINING_LOG);
			output.accept(TFBlocks.MINING_WOOD);
			output.accept(TFBlocks.MINING_LOG_CORE);
			output.accept(TFItems.HOLLOW_MINING_LOG);
			output.accept(TFBlocks.STRIPPED_MINING_LOG);
			output.accept(TFBlocks.STRIPPED_MINING_WOOD);
			output.accept(TFBlocks.MINING_PLANKS);
			output.accept(TFBlocks.MINING_STAIRS);
			output.accept(TFBlocks.MINING_SLAB);
			output.accept(TFBlocks.MINING_CHEST);
			output.accept(TFBlocks.MINING_TRAPPED_CHEST);
			output.accept(TFBlocks.MINING_FENCE);
			output.accept(TFBlocks.MINING_GATE);
			output.accept(TFBlocks.MINING_BANISTER);
			output.accept(TFBlocks.MINING_DRYING_RACK);
			output.accept(TFBlocks.MINING_DOOR);
			output.accept(TFBlocks.MINING_TRAPDOOR);
			output.accept(TFBlocks.MINING_PLATE);
			output.accept(TFBlocks.MINING_BUTTON);
			output.accept(TFBlocks.MINING_SIGN);
			output.accept(TFBlocks.MINING_HANGING_SIGN);
			output.accept(TFBlocks.MINING_LEAVES);
			output.accept(TFBlocks.MINING_SAPLING);

			output.accept(TFBlocks.SORTING_LOG);
			output.accept(TFBlocks.SORTING_WOOD);
			output.accept(TFBlocks.SORTING_LOG_CORE);
			output.accept(TFItems.HOLLOW_SORTING_LOG);
			output.accept(TFBlocks.STRIPPED_SORTING_LOG);
			output.accept(TFBlocks.STRIPPED_SORTING_WOOD);
			output.accept(TFBlocks.SORTING_PLANKS);
			output.accept(TFBlocks.SORTING_STAIRS);
			output.accept(TFBlocks.SORTING_SLAB);
			output.accept(TFBlocks.SORTING_CHEST);
			output.accept(TFBlocks.SORTING_TRAPPED_CHEST);
			output.accept(TFBlocks.SORTING_FENCE);
			output.accept(TFBlocks.SORTING_GATE);
			output.accept(TFBlocks.SORTING_BANISTER);
			output.accept(TFBlocks.SORTING_DRYING_RACK);
			output.accept(TFBlocks.SORTING_DOOR);
			output.accept(TFBlocks.SORTING_TRAPDOOR);
			output.accept(TFBlocks.SORTING_PLATE);
			output.accept(TFBlocks.SORTING_BUTTON);
			output.accept(TFBlocks.SORTING_SIGN);
			output.accept(TFBlocks.SORTING_HANGING_SIGN);
			output.accept(TFBlocks.SORTING_LEAVES);
			output.accept(TFBlocks.SORTING_SAPLING);

			output.accept(TFBlocks.ROOT_BLOCK);
			output.accept(TFBlocks.LIVEROOT_BLOCK);
			output.accept(TFBlocks.HOLLOW_OAK_SAPLING);
			output.accept(TFBlocks.RAINBOW_OAK_LEAVES);
			output.accept(TFBlocks.RAINBOW_OAK_SAPLING);

			output.accept(TFBlocks.MOSS_PATCH);
			output.accept(TFBlocks.MAYAPPLE);
			output.accept(TFBlocks.CLOVER_PATCH);
			output.accept(TFBlocks.FIDDLEHEAD);
			output.accept(TFBlocks.MUSHGLOOM);
			output.accept(TFBlocks.TORCHBERRY_PLANT);
			output.accept(TFItems.FALLEN_LEAVES);
			output.accept(TFBlocks.ROOT_STRAND);
			output.accept(TFBlocks.HEDGE);
			output.accept(TFBlocks.IRON_OREBERRY);
			output.accept(TFBlocks.GOLD_OREBERRY);
			output.accept(TFBlocks.COPPER_OREBERRY);
			output.accept(TFBlocks.ESSENCE_OREBERRY);
			output.accept(TFBlocks.RASPBERRY_BUSH);
			output.accept(TFBlocks.BLUEBERRY_BUSH);
			output.accept(TFBlocks.BLACKBERRY_BUSH);
			output.accept(TFBlocks.MALOBERRY_BUSH);
			output.accept(TFBlocks.BLIGHTBERRY_BUSH);
			output.accept(TFBlocks.DUSKBERRY_BUSH);
			output.accept(TFBlocks.SKYBERRY_BUSH);
			output.accept(TFBlocks.STINGBERRY_BUSH);
			output.accept(TFItems.MASON_JAR.get().getDefaultInstance());
			output.accept(TFItems.FIREFLY_JAR.get().getDefaultInstance());
			output.accept(TFItems.CICADA_JAR.get().getDefaultInstance());
			output.accept(TFBlocks.FIREFLY_SPAWNER);
			output.accept(TFBlocks.FIREFLY);
			output.accept(TFBlocks.CICADA);
			output.accept(TFBlocks.MOONWORM);

			output.accept(TFItems.NAGA_TROPHY);
			output.accept(TFItems.LICH_TROPHY);
			output.accept(TFItems.MINOSHROOM_TROPHY);
			output.accept(TFItems.HYDRA_TROPHY);
			output.accept(TFItems.KNIGHT_PHANTOM_TROPHY);
			output.accept(TFItems.UR_GHAST_TROPHY);
			output.accept(TFItems.ALPHA_YETI_TROPHY);
			output.accept(TFItems.SNOW_QUEEN_TROPHY);
			output.accept(TFItems.QUEST_RAM_TROPHY);

			output.accept(TFBlocks.NAGA_BOSS_SPAWNER);
			output.accept(TFBlocks.LICH_BOSS_SPAWNER);
			output.accept(TFBlocks.MINOSHROOM_BOSS_SPAWNER);
			output.accept(TFBlocks.HYDRA_BOSS_SPAWNER);
			output.accept(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER);
			output.accept(TFBlocks.UR_GHAST_BOSS_SPAWNER);
			output.accept(TFBlocks.ALPHA_YETI_BOSS_SPAWNER);
			output.accept(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER);
			output.accept(TFBlocks.SINISTER_SPAWNER);

			output.accept(TFBlocks.ETCHED_NAGASTONE);
			output.accept(TFBlocks.CRACKED_ETCHED_NAGASTONE);
			output.accept(TFBlocks.MOSSY_ETCHED_NAGASTONE);
			output.accept(TFBlocks.NAGASTONE_PILLAR);
			output.accept(TFBlocks.CRACKED_NAGASTONE_PILLAR);
			output.accept(TFBlocks.MOSSY_NAGASTONE_PILLAR);
			output.accept(TFBlocks.NAGASTONE_STAIRS_LEFT);
			output.accept(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
			output.accept(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
			output.accept(TFBlocks.NAGASTONE_STAIRS_RIGHT);
			output.accept(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);
			output.accept(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
			output.accept(TFBlocks.NAGASTONE_HEAD);
			output.accept(TFBlocks.NAGASTONE);

			output.accept(TFBlocks.SPIRAL_BRICKS);
			output.accept(TFBlocks.BOLD_STONE_PILLAR);
			output.accept(TFBlocks.TWISTED_STONE);
			output.accept(TFBlocks.TWISTED_STONE_PILLAR);
			output.accept(TFBlocks.TERRORCOTTA_LINES);
			output.accept(TFBlocks.TERRORCOTTA_CURVES);
			output.accept(TFBlocks.TERRORCOTTA_ARCS);
			createDefaultSkullCandle(output, TFItems.SKELETON_SKULL_CANDLE);
			createDefaultSkullCandle(output, TFItems.WITHER_SKELETON_SKULL_CANDLE);
			createDefaultSkullCandle(output, TFItems.PLAYER_SKULL_CANDLE);
			createDefaultSkullCandle(output, TFItems.ZOMBIE_SKULL_CANDLE);
			createDefaultSkullCandle(output, TFItems.CREEPER_SKULL_CANDLE);
			createDefaultSkullCandle(output, TFItems.PIGLIN_SKULL_CANDLE);
			createCaskets(output);
			output.accept(TFBlocks.SKULL_CHEST);
			output.accept(TFBlocks.WROUGHT_IRON_FENCE);
			output.accept(TFBlocks.CANDELABRA);
			output.accept(TFBlocks.CORONATION_CARPET);

			output.accept(TFItems.HUGE_WATER_LILY);
			output.accept(TFItems.HUGE_LILY_PAD);

			output.accept(TFBlocks.MAZE_SLIME_BLOCK);
			output.accept(TFBlocks.MAZESTONE);
			output.accept(TFBlocks.MAZESTONE_BRICK);
			output.accept(TFBlocks.CRACKED_MAZESTONE);
			output.accept(TFBlocks.MOSSY_MAZESTONE);
			output.accept(TFBlocks.DECORATIVE_MAZESTONE);
			output.accept(TFBlocks.CUT_MAZESTONE);
			output.accept(TFBlocks.MAZESTONE_MOSAIC);
			output.accept(TFBlocks.MAZESTONE_BORDER);

			output.accept(TFBlocks.SMOKER);
			output.accept(TFBlocks.FIRE_JET);

			output.accept(TFBlocks.UNDERBRICK);
			output.accept(TFBlocks.CRACKED_UNDERBRICK);
			output.accept(TFBlocks.MOSSY_UNDERBRICK);
			output.accept(TFBlocks.UNDERBRICK_FLOOR);
			output.accept(TFBlocks.STRONGHOLD_SHIELD);
			output.accept(TFBlocks.TROPHY_PEDESTAL);

			output.accept(TFBlocks.TOWERWOOD);
			output.accept(TFBlocks.CRACKED_TOWERWOOD);
			output.accept(TFBlocks.MOSSY_TOWERWOOD);
			output.accept(TFBlocks.INFESTED_TOWERWOOD);
			output.accept(TFBlocks.ENCASED_TOWERWOOD);
			output.accept(TFBlocks.ENCASED_SMOKER);
			output.accept(TFBlocks.ENCASED_FIRE_JET);
			output.accept(TFBlocks.GHAST_TRAP);
			output.accept(TFBlocks.VANISHING_BLOCK);
			output.accept(TFBlocks.REAPPEARING_BLOCK);
			output.accept(TFBlocks.LOCKED_VANISHING_BLOCK);
			output.accept(TFBlocks.CARMINITE_BUILDER);
			output.accept(TFBlocks.ANTIBUILDER);
			output.accept(TFBlocks.CARMINITE_REACTOR);

			output.accept(TFBlocks.AURORA_BLOCK);
			output.accept(TFBlocks.AURORA_PILLAR);
			output.accept(TFBlocks.AURORA_SLAB);

			output.accept(TFBlocks.UBEROUS_SOIL);
			output.accept(TFBlocks.HUGE_STALK);
			output.accept(TFBlocks.BEANSTALK_LEAVES);
			output.accept(TFBlocks.TROLLSTEINN);
			output.accept(TFBlocks.TROLLVIDR);
			output.accept(TFBlocks.UNRIPE_TROLLBER);
			output.accept(TFBlocks.TROLLBER);
			output.accept(TFBlocks.HUGE_MUSHGLOOM);
			output.accept(TFBlocks.HUGE_MUSHGLOOM_STEM);
			output.accept(TFBlocks.WISPY_CLOUD);
			output.accept(TFBlocks.FLUFFY_CLOUD);
			output.accept(TFBlocks.RAINY_CLOUD);
			output.accept(TFBlocks.SNOWY_CLOUD);
			output.accept(TFBlocks.GIANT_COBBLESTONE);
			output.accept(TFBlocks.GIANT_LOG);
			output.accept(TFBlocks.GIANT_LEAVES);
			output.accept(TFBlocks.GIANT_OBSIDIAN);

			output.accept(TFBlocks.BROWN_THORNS);
			output.accept(TFBlocks.GREEN_THORNS);
			output.accept(TFBlocks.BURNT_THORNS);
			output.accept(TFBlocks.THORN_ROSE);
			output.accept(TFBlocks.THORN_LEAVES);
			output.accept(TFBlocks.DEADROCK);
			output.accept(TFBlocks.CRACKED_DEADROCK);
			output.accept(TFBlocks.WEATHERED_DEADROCK);

			output.accept(TFBlocks.CASTLE_BRICK);
			output.accept(TFBlocks.WORN_CASTLE_BRICK);
			output.accept(TFBlocks.CRACKED_CASTLE_BRICK);
			output.accept(TFBlocks.MOSSY_CASTLE_BRICK);
			output.accept(TFBlocks.THICK_CASTLE_BRICK);
			output.accept(TFBlocks.CASTLE_ROOF_TILE);
			output.accept(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
			output.accept(TFBlocks.ENCASED_CASTLE_BRICK_TILE);
			output.accept(TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
			output.accept(TFBlocks.BOLD_CASTLE_BRICK_TILE);
			output.accept(TFBlocks.CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.WORN_CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
			output.accept(TFBlocks.PINK_CASTLE_RUNE_BRICK);
			output.accept(TFBlocks.YELLOW_CASTLE_RUNE_BRICK);
			output.accept(TFBlocks.BLUE_CASTLE_RUNE_BRICK);
			output.accept(TFBlocks.VIOLET_CASTLE_RUNE_BRICK);
			output.accept(TFBlocks.PINK_CASTLE_DOOR);
			output.accept(TFBlocks.YELLOW_CASTLE_DOOR);
			output.accept(TFBlocks.BLUE_CASTLE_DOOR);
			output.accept(TFBlocks.VIOLET_CASTLE_DOOR);
			output.accept(TFBlocks.PINK_FORCE_FIELD);
			output.accept(TFBlocks.ORANGE_FORCE_FIELD);
			output.accept(TFBlocks.GREEN_FORCE_FIELD);
			output.accept(TFBlocks.BLUE_FORCE_FIELD);
			output.accept(TFBlocks.VIOLET_FORCE_FIELD);

			output.accept(TFBlocks.UNCRAFTING_TABLE);
			output.accept(TFBlocks.IRON_LADDER);
			output.accept(TFBlocks.ROPE);
			output.accept(TFBlocks.IRONWOOD_BLOCK);
			output.accept(TFBlocks.STEELEAF_BLOCK);
			output.accept(TFBlocks.FIERY_BLOCK);
			output.accept(TFBlocks.KNIGHTMETAL_BLOCK);
			output.accept(TFBlocks.CARMINITE_BLOCK);
			output.accept(TFBlocks.ARCTIC_FUR_BLOCK);
		}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = TABS.register("items", () -> CreativeModeTab.builder()
		.withTabsBefore(BLOCKS.getKey())
		.title(Component.translatable("itemGroup.twilightforest.items"))
		.icon(() -> new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE))
		.displayItems((parameters, output) -> {
			output.accept(TFItems.MAGIC_MAP);
			output.accept(TFItems.MAZE_MAP);
			output.accept(TFItems.ORE_MAP);
			output.accept(TFItems.RAVEN_FEATHER);
			output.accept(TFItems.MAGIC_MAP_FOCUS);
			output.accept(TFItems.MAZE_MAP_FOCUS);
			output.accept(TFItems.CHARM_OF_LIFE_1);
			output.accept(TFItems.CHARM_OF_LIFE_2);
			output.accept(TFItems.CHARM_OF_KEEPING_1);
			output.accept(TFItems.CHARM_OF_KEEPING_2);
			output.accept(TFItems.CHARM_OF_KEEPING_3);
			output.accept(TFItems.TRANSFORMATION_POWDER);
			output.accept(TFItems.LIVEROOT);
			output.accept(TFItems.RAW_IRONWOOD);
			output.accept(TFItems.IRONWOOD_INGOT);
			output.accept(TFItems.STEELEAF_INGOT);
			output.accept(TFItems.MAZE_SLIME_BALL);
			output.accept(TFItems.NAGA_SCALE);
			output.accept(TFItems.WROUGHT_IRON_BAR);
			output.accept(TFItems.ARMOR_SHARD);
			output.accept(TFItems.ARMOR_SHARD_CLUSTER);
			output.accept(TFItems.KNIGHTMETAL_INGOT);
			output.accept(TFItems.KNIGHTMETAL_RING);
			output.accept(TFItems.TANNIN);
			output.accept(TFItems.TREATED_LEATHER);
			output.accept(TFItems.TANNED_LEATHER);
			output.accept(TFItems.FIERY_BLOOD);
			output.accept(TFItems.FIERY_TEARS);
			output.accept(TFItems.FIERY_INGOT);
			output.accept(TFItems.ARCTIC_FUR);
			output.accept(TFItems.ALPHA_YETI_FUR);
			output.accept(TFItems.BRITTLE_FLASK);
			output.accept(TFItems.GREATER_FLASK);
			output.accept(TFItems.EXANIMATE_ESSENCE);
			output.accept(TFItems.CROWN_SPLINTER);
			output.accept(TFBlocks.RED_THREAD);
			output.accept(TFItems.BORER_ESSENCE);
			output.accept(TFItems.CARMINITE);
			output.accept(TFItems.TOWER_KEY);
			output.accept(TFItems.MAGIC_BEANS);
			output.accept(TFItems.IRON_BERRY);
			output.accept(TFItems.GOLD_BERRY);
			output.accept(TFItems.COPPER_BERRY);
			output.accept(TFItems.ESSENCE_BERRY);
			output.accept(TFItems.MUSIC_DISC_THREAD);
			output.accept(TFItems.MUSIC_DISC_FINDINGS);
			output.accept(TFItems.MUSIC_DISC_RADIANCE);
			output.accept(TFItems.MUSIC_DISC_STEPS);
			output.accept(TFItems.MUSIC_DISC_MOTION);
			output.accept(TFItems.MUSIC_DISC_WAYFARER);
			output.accept(TFItems.MUSIC_DISC_HOME);
			output.accept(TFItems.MUSIC_DISC_MAKER);
			output.accept(TFItems.MUSIC_DISC_SUPERSTITIOUS);
			output.accept(TFItems.NAGA_BANNER_PATTERN);
			output.accept(TFItems.LICH_BANNER_PATTERN);
			output.accept(TFItems.MINOSHROOM_BANNER_PATTERN);
			output.accept(TFItems.HYDRA_BANNER_PATTERN);
			output.accept(TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
			output.accept(TFItems.UR_GHAST_BANNER_PATTERN);
			output.accept(TFItems.ALPHA_YETI_BANNER_PATTERN);
			output.accept(TFItems.SNOW_QUEEN_BANNER_PATTERN);
			output.accept(TFItems.QUEST_RAM_BANNER_PATTERN);
			output.accept(TFItems.TWILIGHT_OAK_BOAT);
			output.accept(TFItems.CANOPY_BOAT);
			output.accept(TFItems.MANGROVE_BOAT);
			output.accept(TFItems.DARK_BOAT);
			output.accept(TFItems.TIME_BOAT);
			output.accept(TFItems.TRANSFORMATION_BOAT);
			output.accept(TFItems.MINING_BOAT);
			output.accept(TFItems.SORTING_BOAT);
			output.accept(TFItems.TWILIGHT_OAK_CHEST_BOAT);
			output.accept(TFItems.CANOPY_CHEST_BOAT);
			output.accept(TFItems.MANGROVE_CHEST_BOAT);
			output.accept(TFItems.DARK_CHEST_BOAT);
			output.accept(TFItems.TIME_CHEST_BOAT);
			output.accept(TFItems.TRANSFORMATION_CHEST_BOAT);
			output.accept(TFItems.MINING_CHEST_BOAT);
			output.accept(TFItems.SORTING_CHEST_BOAT);
			createSpawnEggsAlphabetical(output);
		}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT = TABS.register("equipment", () -> CreativeModeTab.builder()
		.withTabsBefore(ITEMS.getKey())
		.title(Component.translatable("itemGroup.twilightforest.equipment"))
		.icon(() -> new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()))
		.displayItems((parameters, output) -> {
			HolderLookup.RegistryLookup<Enchantment> lookup = parameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
			generateGearWithEnchants(output, TFItems.IRONWOOD_HELMET, new EnchantmentInstance(lookup.getOrThrow(Enchantments.AQUA_AFFINITY), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_CHESTPLATE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_LEGGINGS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_BOOTS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FEATHER_FALLING), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_SWORD, new EnchantmentInstance(lookup.getOrThrow(Enchantments.KNOCKBACK), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_SHOVEL, new EnchantmentInstance(lookup.getOrThrow(Enchantments.UNBREAKING), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_PICKAXE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.EFFICIENCY), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_AXE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FORTUNE), 1));
			generateGearWithEnchants(output, TFItems.IRONWOOD_HOE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.EFFICIENCY), 1));
			generateGearWithEnchants(output, TFItems.STEELEAF_HELMET, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_CHESTPLATE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.BLAST_PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_LEGGINGS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FIRE_PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_BOOTS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FEATHER_FALLING), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_SWORD, new EnchantmentInstance(lookup.getOrThrow(Enchantments.LOOTING), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_SHOVEL, new EnchantmentInstance(lookup.getOrThrow(Enchantments.EFFICIENCY), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_PICKAXE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FORTUNE), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_AXE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.EFFICIENCY), 2));
			generateGearWithEnchants(output, TFItems.STEELEAF_HOE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FORTUNE), 2));
			output.accept(TFItems.KNIGHTMETAL_HELMET);
			output.accept(TFItems.KNIGHTMETAL_CHESTPLATE);
			output.accept(TFItems.KNIGHTMETAL_LEGGINGS);
			output.accept(TFItems.KNIGHTMETAL_BOOTS);
			output.accept(TFItems.KNIGHTMETAL_SWORD);
			output.accept(TFItems.KNIGHTMETAL_PICKAXE);
			output.accept(TFItems.KNIGHTMETAL_AXE);
			output.accept(TFItems.BLOCK_AND_CHAIN);
			output.accept(TFItems.KNIGHTMETAL_SHIELD);
			output.accept(TFItems.FIERY_HELMET);
			output.accept(TFItems.FIERY_CHESTPLATE);
			output.accept(TFItems.FIERY_LEGGINGS);
			output.accept(TFItems.FIERY_BOOTS);
			output.accept(TFItems.FIERY_SWORD);
			output.accept(TFItems.FIERY_PICKAXE);
			generateGearWithEnchants(output, TFItems.MAZEBREAKER_PICKAXE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.EFFICIENCY), 4), new EnchantmentInstance(lookup.getOrThrow(Enchantments.UNBREAKING), 3), new EnchantmentInstance(lookup.getOrThrow(Enchantments.FORTUNE), 2));
			output.accept(TFItems.GOLDEN_MINOTAUR_AXE);
			output.accept(TFItems.DIAMOND_MINOTAUR_AXE);
			output.accept(TFItems.ARCTIC_HELMET);
			output.accept(TFItems.ARCTIC_CHESTPLATE);
			output.accept(TFItems.ARCTIC_LEGGINGS);
			output.accept(TFItems.ARCTIC_BOOTS);
			output.accept(TFItems.ICE_SWORD);
			output.accept(TFItems.TRIPLE_BOW);
			output.accept(TFItems.SEEKER_BOW);
			output.accept(TFItems.ICE_BOW);
			output.accept(TFItems.ENDER_BOW);
			generateGearWithEnchants(output, TFItems.YETI_HELMET, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.YETI_CHESTPLATE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.YETI_LEGGINGS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 2));
			generateGearWithEnchants(output, TFItems.YETI_BOOTS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 2), new EnchantmentInstance(lookup.getOrThrow(Enchantments.FEATHER_FALLING), 4));
			output.accept(TFItems.GIANT_SWORD);
			output.accept(TFItems.GIANT_PICKAXE);
			createGlassSwordAndLoreVer(output);
			output.accept(TFItems.ICE_BOMB);
			output.accept(TFItems.PHANTOM_HELMET);
			output.accept(TFItems.PHANTOM_CHESTPLATE);
			generateGearWithEnchants(output, TFItems.NAGA_CHESTPLATE, new EnchantmentInstance(lookup.getOrThrow(Enchantments.FIRE_PROTECTION), 3));
			generateGearWithEnchants(output, TFItems.NAGA_LEGGINGS, new EnchantmentInstance(lookup.getOrThrow(Enchantments.PROTECTION), 3));
			output.accept(TFItems.MYSTIC_CROWN);
			output.accept(TFItems.TWILIGHT_SCEPTER);
			output.accept(TFItems.LIFEDRAIN_SCEPTER);
			output.accept(TFItems.ZOMBIE_SCEPTER);
			output.accept(TFItems.FORTIFICATION_SCEPTER);
			output.accept(TFItems.TRAVELLERS_GOGGLES);
			output.accept(TFItems.TRAVELLERS_VEST);
			output.accept(TFItems.TRAVELLERS_WINGS);
			output.accept(TFItems.TRAVELLERS_BOOTS);
			output.accept(TFItems.TRAVELLERS_BELT);
			output.accept(TFItems.TRAVELLERS_GLOVES);
			output.accept(TFItems.LAMP_OF_CINDERS);
			output.accept(TFItems.EMPERORS_CLOTH);
			output.accept(TFItems.ORE_MAGNET);
			output.accept(TFItems.ORE_METER);
			output.accept(TFItems.POCKET_WATCH);
			output.accept(TFItems.MOON_DIAL);
			output.accept(TFItems.CRUMBLE_HORN);
			output.accept(TFItems.PEACOCK_FEATHER_FAN);
			output.accept(TFItems.MOONWORM_QUEEN);
		}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FOOD = TABS.register("food", () -> CreativeModeTab.builder()
		.withTabsBefore(EQUIPMENT.getKey())
		.title(Component.translatable("itemGroup.twilightforest.food"))
		.icon(() -> new ItemStack(TFItems.COOKED_MEEF.get()))
		.displayItems((parameters, output) -> {
			output.accept(TFItems.TORCHBERRIES);
			output.accept(TFItems.RASPBERRY);
			output.accept(TFItems.BLUEBERRY);
			output.accept(TFItems.BLACKBERRY);
			output.accept(TFItems.MALOBERRY);
			output.accept(TFItems.BLIGHTBERRY);
			output.accept(TFItems.DUSKBERRY);
			output.accept(TFItems.SKYBERRY);
			output.accept(TFItems.STINGBERRY);
			output.accept(TFItems.BERRY_MEDLEY);
			output.accept(TFItems.MOSS_SOUP);
			output.accept(TFItems.RAW_VENISON);
			output.accept(TFItems.COOKED_VENISON);
			output.accept(TFItems.RAW_MEEF);
			output.accept(TFItems.COOKED_MEEF);
			output.accept(TFItems.BEEF_JERKY);
			output.accept(TFItems.CHICKEN_JERKY);
			output.accept(TFItems.PORK_JERKY);
			output.accept(TFItems.MUTTON_JERKY);
			output.accept(TFItems.RABBIT_JERKY);
			output.accept(TFItems.MEEF_JERKY);
			output.accept(TFItems.VENISON_JERKY);
			output.accept(TFItems.MONSTER_JERKY);
			output.accept(TFItems.COD_JERKY);
			output.accept(TFItems.SALMON_JERKY);
			output.accept(TFItems.TROPICAL_FISH_JERKY);
			output.accept(TFItems.FUGU_JERKY);
			output.accept(TFItems.GELATINOUS_SLIME_DROP);
			output.accept(TFItems.GELATINOUS_MAZE_SLIME_DROP);
			output.accept(TFItems.MAZE_WAFER);
			output.accept(TFItems.MEEF_STROGANOFF);
			output.accept(TFItems.HYDRA_CHOP);
			output.accept(TFItems.EXPERIMENT_115);
		}).build());

	private static void generateGearWithEnchants(CreativeModeTab.Output output, ItemLike item, EnchantmentInstance... instances) {
		ItemStack stack = new ItemStack(item);
		if (TFConfig.defaultItemEnchants) {
			for (EnchantmentInstance enchant : instances) {
				stack.enchant(enchant.enchantment, enchant.level);
			}
		}
		output.accept(stack);
	}

	private static void createSpawnEggsAlphabetical(CreativeModeTab.Output output) {
		Collection<? extends Item> eggs = TFEntities.SPAWN_EGGS.getEntries().stream().map(DeferredHolder::value).toList();
		eggs.forEach(output::accept);
	}

	private static void createDefaultSkullCandle(CreativeModeTab.Output output, ItemLike item) {
		ItemStack stack = new ItemStack(item);
		stack.set(TFDataComponents.SKULL_CANDLES, new SkullCandles(AbstractSkullCandleBlock.CandleColors.PLAIN.getValue(), 1));
		output.accept(stack);
	}

	private static void createCaskets(CreativeModeTab.Output output) {
		for (int i = 0; i< 3; i++) {
			ItemStack stack = new ItemStack(TFItems.KEEPSAKE_CASKET.get());
			stack.set(TFDataComponents.CASKET_DAMAGE, i);
			output.accept(stack);
		}
	}

	private static void createGlassSwordAndLoreVer(CreativeModeTab.Output output) {
		output.accept(TFItems.GLASS_SWORD);

		ItemStack loreSword = new ItemStack(TFItems.GLASS_SWORD.get());

		List<Component> GLASS_SWORD_COMPONENTS = List.of(Component.translatable("item.twilightforest.glass_sword.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		loreSword.set(DataComponents.LORE, new ItemLore(GLASS_SWORD_COMPONENTS, GLASS_SWORD_COMPONENTS));
		loreSword.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
		loreSword.set(TFDataComponents.INFINITE_GLASS_SWORD, Unit.INSTANCE);

		output.accept(loreSword);
	}

	private static final Comparator<Holder<MagicPaintingVariant>> MAGIC_COMPARATOR = Comparator.comparing(Holder::value, Comparator.<MagicPaintingVariant>comparingInt((variant) ->
		variant.height() * variant.width()).thenComparing(MagicPaintingVariant::width));

	public static void addToTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			event.insertAfter(new ItemStack(Items.OAK_WOOD), TFItems.HOLLOW_OAK_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.SPRUCE_WOOD), TFItems.HOLLOW_SPRUCE_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.BIRCH_WOOD), TFItems.HOLLOW_BIRCH_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.JUNGLE_WOOD), TFItems.HOLLOW_JUNGLE_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.ACACIA_WOOD), TFItems.HOLLOW_ACACIA_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.DARK_OAK_WOOD), TFItems.HOLLOW_DARK_OAK_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CRIMSON_HYPHAE), TFItems.HOLLOW_CRIMSON_STEM.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.WARPED_HYPHAE), TFItems.HOLLOW_WARPED_STEM.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.MANGROVE_WOOD), TFItems.HOLLOW_VANGROVE_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CHERRY_WOOD), TFItems.HOLLOW_CHERRY_LOG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

			event.insertAfter(new ItemStack(Items.OAK_FENCE_GATE), TFBlocks.OAK_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.SPRUCE_FENCE_GATE), TFBlocks.SPRUCE_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.BIRCH_FENCE_GATE), TFBlocks.BIRCH_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.JUNGLE_FENCE_GATE), TFBlocks.JUNGLE_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.ACACIA_FENCE_GATE), TFBlocks.ACACIA_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.DARK_OAK_FENCE_GATE), TFBlocks.DARK_OAK_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CRIMSON_FENCE_GATE), TFBlocks.CRIMSON_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.WARPED_FENCE_GATE), TFBlocks.WARPED_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.MANGROVE_FENCE_GATE), TFBlocks.VANGROVE_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.BAMBOO_FENCE_GATE), TFBlocks.BAMBOO_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CHERRY_FENCE_GATE), TFBlocks.CHERRY_DRYING_RACK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

			event.insertAfter(new ItemStack(Items.OAK_FENCE_GATE), TFBlocks.OAK_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.SPRUCE_FENCE_GATE), TFBlocks.SPRUCE_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.BIRCH_FENCE_GATE), TFBlocks.BIRCH_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.JUNGLE_FENCE_GATE), TFBlocks.JUNGLE_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.ACACIA_FENCE_GATE), TFBlocks.ACACIA_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.DARK_OAK_FENCE_GATE), TFBlocks.DARK_OAK_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CRIMSON_FENCE_GATE), TFBlocks.CRIMSON_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.WARPED_FENCE_GATE), TFBlocks.WARPED_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.MANGROVE_FENCE_GATE), TFBlocks.VANGROVE_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.BAMBOO_FENCE_GATE), TFBlocks.BAMBOO_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertAfter(new ItemStack(Items.CHERRY_FENCE_GATE), TFBlocks.CHERRY_BANISTER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		} else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			event.insertAfter(new ItemStack(Items.IRON_NUGGET), TFItems.COPPER_NUGGET.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		} else if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
			event.getParameters().holders().lookupOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS)
				.listElements().sorted(MAGIC_COMPARATOR)
				.forEach(holder -> {
					ItemStack itemstack = new ItemStack(TFItems.MAGIC_PAINTING.get());
					itemstack.set(TFDataComponents.MAGIC_PAINTING_VARIANT, holder);
					event.accept(itemstack);
				});
		}
	}
}
