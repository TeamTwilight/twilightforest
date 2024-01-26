package twilightforest.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

public class TFRemapper {

	public static void addRegistryAliases() {
		DeferredRegister<Block> blockReg = TFBlocks.BLOCKS;
		DeferredRegister<EntityType<?>> entityReg = TFEntities.ENTITIES;
		DeferredRegister<Item> itemReg = TFItems.ITEMS;
		
		remapEntryFromRegistries("yeti_trophy", "alpha_yeti_trophy", blockReg, itemReg);
		remapEntryFromRegistries("yeti_wall_trophy", "alpha_yeti_wall_trophy", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_naga", "naga_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_lich", "lich_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_minoshroom", "minoshroom_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_hydra", "hydra_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_knight_phantom", "knight_phantom_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_ur_ghast", "ur_ghast_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_alpha_yeti", "alpha_yeti_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_snow_queen", "snow_queen_boss_spawner", blockReg, itemReg);
		remapEntryFromRegistries("boss_spawner_final_boss", "final_boss_boss_spawner", blockReg, itemReg);

		remapEntryFromRegistries("etched_nagastone_weathered", "cracked_etched_nagastone", blockReg, itemReg);
		remapEntryFromRegistries("etched_nagastone_mossy", "mossy_etched_nagastone", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_pillar_weathered", "cracked_nagastone_pillar", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_pillar_mossy", "mossy_nagastone_pillar", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_stairs_weathered_left", "cracked_nagastone_stairs_left", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_stairs_mossy_left", "mossy_nagastone_stairs_left", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_stairs_weathered_right", "cracked_nagastone_stairs_right", blockReg, itemReg);
		remapEntryFromRegistries("nagastone_stairs_mossy_right", "mossy_nagastone_stairs_right", blockReg, itemReg);
		remapEntryFromRegistries("naga_stone_head", "nagastone_head", blockReg, itemReg);
		remapEntryFromRegistries("naga_stone", "nagastone", blockReg, itemReg);

		remapEntryFromRegistries("stone_twist", "twisted_stone", blockReg, itemReg);
		remapEntryFromRegistries("stone_twist_thin", "twisted_stone_pillar", blockReg, itemReg);
		remapEntryFromRegistries("stone_pillar_bold", "bold_stone_pillar", blockReg, itemReg);
		remapEntryFromRegistries("empty_bookshelf", "empty_canopy_bookshelf", blockReg, itemReg);

		remapEntryFromRegistries("huge_lilypad", "huge_lily_pad", blockReg, itemReg);
		remapEntryFromRegistries("huge_waterlily", "huge_water_lily", blockReg, itemReg);

		remapEntryFromRegistries("maze_stone", "mazestone", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_brick", "mazestone_brick", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_cracked", "cracked_mazestone", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_mossy", "mossy_mazestone", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_decorative", "decorative_mazestone", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_chiseled", "cut_mazestone", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_border", "mazestone_border", blockReg, itemReg);
		remapEntryFromRegistries("maze_stone_mosaic", "mazestone_mosaic", blockReg, itemReg);

		remapEntryFromRegistries("underbrick_cracked", "cracked_underbrick", blockReg, itemReg);
		remapEntryFromRegistries("underbrick_mossy", "mossy_underbrick", blockReg, itemReg);

		remapEntryFromRegistries("tower_wood", "towerwood", blockReg, itemReg);
		remapEntryFromRegistries("tower_wood_cracked", "cracked_towerwood", blockReg, itemReg);
		remapEntryFromRegistries("tower_wood_mossy", "mossy_towerwood", blockReg, itemReg);
		remapEntryFromRegistries("tower_wood_infested", "infested_towerwood", blockReg, itemReg);
		remapEntryFromRegistries("tower_wood_encased", "encased_towerwood", blockReg, itemReg);

		remapEntryFromRegistries("deadrock_cracked", "cracked_deadrock", blockReg, itemReg);
		remapEntryFromRegistries("deadrock_weathered", "weathered_deadrock", blockReg, itemReg);

		remapEntryFromRegistries("castle_brick_worn", "worn_castle_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_brick_cracked", "cracked_castle_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_brick_mossy", "mossy_castle_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_brick_frame", "thick_castle_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_brick_roof", "castle_roof_tile", blockReg, itemReg);
		remapEntryFromRegistries("castle_pillar_encased", "encased_castle_brick_pillar", blockReg, itemReg);
		remapEntryFromRegistries("castle_pillar_encased_tile", "encased_castle_brick_tile", blockReg, itemReg);
		remapEntryFromRegistries("castle_pillar_bold", "bold_castle_brick_pillar", blockReg, itemReg);
		remapEntryFromRegistries("castle_pillar_bold_tile", "bold_castle_brick_tile", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_brick", "castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_worn", "worn_castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_cracked", "cracked_castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_mossy", "mossy_castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_encased", "encased_castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_stairs_bold", "bold_castle_brick_stairs", blockReg, itemReg);
		remapEntryFromRegistries("castle_rune_brick_pink", "pink_castle_rune_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_rune_brick_yellow", "yellow_castle_rune_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_rune_brick_blue", "blue_castle_rune_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_rune_brick_purple", "violet_castle_rune_brick", blockReg, itemReg);
		remapEntryFromRegistries("castle_door_pink", "pink_castle_door", blockReg, itemReg);
		remapEntryFromRegistries("castle_door_yellow", "yellow_castle_door", blockReg, itemReg);
		remapEntryFromRegistries("castle_door_blue", "blue_castle_door", blockReg, itemReg);
		remapEntryFromRegistries("castle_door_purple", "violet_castle_door", blockReg, itemReg);
		remapEntryFromRegistries("force_field_pink", "pink_force_field", blockReg, itemReg);
		remapEntryFromRegistries("force_field_orange", "orange_force_field", blockReg, itemReg);
		remapEntryFromRegistries("force_field_green", "green_force_field", blockReg, itemReg);
		remapEntryFromRegistries("force_field_blue", "blue_force_field", blockReg, itemReg);
		remapEntryFromRegistries("force_field_purple", "violet_force_field", blockReg, itemReg);

		remapEntryFromRegistries("rainboak_leaves", "rainbow_oak_leaves", blockReg, itemReg);
		remapEntryFromRegistries("rainboak_sapling", "rainbow_oak_sapling", blockReg, itemReg);
		remapEntryFromRegistries("potted_rainboak_sapling", "potted_rainbow_oak_sapling", blockReg, itemReg);

		remapEntryFromRegistries("dark_gate", "dark_fence_gate", blockReg, itemReg);
		remapEntryFromRegistries("dark_plate", "dark_pressure_plate", blockReg, itemReg);
		remapEntryFromRegistries("darkwood_sign", "dark_sign", blockReg, itemReg);
		remapEntryFromRegistries("darkwood_wall_sign", "dark_wall_sign", blockReg, itemReg);
		remapEntryFromRegistries("darkwood_banister", "dark_banister", blockReg, itemReg);

		remapEntryFromRegistries("trans_planks", "transformation_planks", blockReg, itemReg);
		remapEntryFromRegistries("trans_slab", "transformation_slab", blockReg, itemReg);
		remapEntryFromRegistries("trans_stairs", "transformation_stairs", blockReg, itemReg);
		remapEntryFromRegistries("trans_button", "transformation_button", blockReg, itemReg);
		remapEntryFromRegistries("trans_fence", "transformation_fence", blockReg, itemReg);
		remapEntryFromRegistries("trans_gate", "transformation_fence_gate", blockReg, itemReg);
		remapEntryFromRegistries("trans_plate", "transformation_pressure_plate", blockReg, itemReg);
		remapEntryFromRegistries("trans_door", "transformation_door", blockReg, itemReg);
		remapEntryFromRegistries("trans_trapdoor", "transformation_trapdoor", blockReg, itemReg);
		remapEntryFromRegistries("trans_sign", "transformation_sign", blockReg, itemReg);
		remapEntryFromRegistries("trans_wall_sign", "transformation_wall_sign", blockReg, itemReg);
		remapEntryFromRegistries("trans_banister", "transformation_banister", blockReg, itemReg);

		remapEntryFromRegistries("mine_planks", "mining_planks", blockReg, itemReg);
		remapEntryFromRegistries("mine_slab", "mining_slab", blockReg, itemReg);
		remapEntryFromRegistries("mine_stairs", "mining_stairs", blockReg, itemReg);
		remapEntryFromRegistries("mine_button", "mining_button", blockReg, itemReg);
		remapEntryFromRegistries("mine_fence", "mining_fence", blockReg, itemReg);
		remapEntryFromRegistries("mine_gate", "mining_fence_gate", blockReg, itemReg);
		remapEntryFromRegistries("mine_plate", "mining_pressure_plate", blockReg, itemReg);
		remapEntryFromRegistries("mine_door", "mining_door", blockReg, itemReg);
		remapEntryFromRegistries("mine_trapdoor", "mining_trapdoor", blockReg, itemReg);
		remapEntryFromRegistries("mine_sign", "mining_sign", blockReg, itemReg);
		remapEntryFromRegistries("mine_wall_sign", "mining_wall_sign", blockReg, itemReg);
		remapEntryFromRegistries("mine_banister", "mining_banister", blockReg, itemReg);

		remapEntryFromRegistries("sort_planks", "sorting_planks", blockReg, itemReg);
		remapEntryFromRegistries("sort_slab", "sorting_slab", blockReg, itemReg);
		remapEntryFromRegistries("sort_stairs", "sorting_stairs", blockReg, itemReg);
		remapEntryFromRegistries("sort_button", "sorting_button", blockReg, itemReg);
		remapEntryFromRegistries("sort_fence", "sorting_fence", blockReg, itemReg);
		remapEntryFromRegistries("sort_gate", "sorting_fence_gate", blockReg, itemReg);
		remapEntryFromRegistries("sort_plate", "sorting_pressure_plate", blockReg, itemReg);
		remapEntryFromRegistries("sort_door", "sorting_door", blockReg, itemReg);
		remapEntryFromRegistries("sort_trapdoor", "sorting_trapdoor", blockReg, itemReg);
		remapEntryFromRegistries("sort_sign", "sorting_sign", blockReg, itemReg);
		remapEntryFromRegistries("sort_wall_sign", "sorting_wall_sign", blockReg, itemReg);
		remapEntryFromRegistries("sort_banister", "sorting_banister", blockReg, itemReg);

		remapEntry(itemReg, "shield_scepter", "fortification_scepter");
		remapEntry(itemReg, "magic_map", "filled_magic_map");
		remapEntry(itemReg, "maze_map", "filled_maze_map");
		remapEntry(itemReg, "ore_map", "filled_ore_map");
		remapEntry(itemReg, "magic_map_empty", "magic_map");
		remapEntry(itemReg, "maze_map_empty", "maze_map");
		remapEntry(itemReg, "ore_map_empty", "ore_map");
		remapEntry(itemReg, "ironwood_raw", "raw_ironwood");
		remapEntry(itemReg, "minotaur_axe_gold", "gold_minotaur_axe");
		remapEntry(itemReg, "minotaur_axe", "diamond_minotaur_axe");
		remapEntry(itemReg, "peacock_fan", "peacock_feather_fan");
		remapEntry(itemReg, "alpha_fur", "alpha_yeti_fur");
		remapEntry(itemReg, "questing_ram_banner_pattern", "quest_ram_banner_pattern");

		remapEntry(itemReg, "bunny_spawn_egg", "dwarf_rabbit_spawn_egg");
		remapEntry(itemReg, "goblin_knight_lower_spawn_egg", "lower_goblin_knight_spawn_egg");
		remapEntry(itemReg, "mini_ghast_spawn_egg", "carminite_ghastling_spawn_egg");
		remapEntry(itemReg, "tower_ghast_spawn_egg", "carminite_ghastguard_spawn_egg");
		remapEntry(itemReg, "tower_golem_spawn_egg", "carminite_golem_spawn_egg");
		remapEntry(itemReg, "tower_broodling_spawn_egg", "carminite_broodling_spawn_egg");
		remapEntry(itemReg, "tower_termite_spawn_egg", "towerwood_borer_spawn_egg");
		remapEntry(itemReg, "wild_boar_spawn_egg", "boar_spawn_egg");
		remapEntry(itemReg, "yeti_alpha_spawn_egg", "alpha_yeti_spawn_egg");

		remapEntry(entityReg, "wild_boar", "boar");
		remapEntry(entityReg, "bunny", "dwarf_rabbit");
		remapEntry(entityReg, "mini_ghast", "carminite_ghastling");
		remapEntry(entityReg, "tower_ghast", "carminite_ghastguard");
		remapEntry(entityReg, "tower_golem", "carminite_golem");
		remapEntry(entityReg, "tower_broodling", "carminite_broodling");
		remapEntry(entityReg, "tower_termite", "towerwood_borer");
		remapEntry(entityReg, "goblin_knight_upper", "upper_goblin_knight");
		remapEntry(entityReg, "goblin_knight_lower", "lower_goblin_knight");
		remapEntry(entityReg, "yeti_alpha", "alpha_yeti");
	}

	private static void remapEntry(DeferredRegister<?> registry, String oldId, String newId) {
		registry.addAlias(TwilightForestMod.prefix(oldId), TwilightForestMod.prefix(newId));
	}

	private static void remapEntryFromRegistries(String oldId, String newId, DeferredRegister<?>... registries) {
		for (DeferredRegister<?> registry : registries) {
			registry.addAlias(TwilightForestMod.prefix(oldId), TwilightForestMod.prefix(newId));
		}
	}
}
