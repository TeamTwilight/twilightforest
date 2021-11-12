package twilightforest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TFRemapper {

	@SubscribeEvent
	public static void remapBlocks(RegistryEvent.MissingMappings<Block> event) {
		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
			if(mapping.key.getNamespace().equals(TwilightForestMod.ID)) {
				remapBlock(mapping, "yeti_trophy", "alpha_yeti_trophy");
				remapBlock(mapping, "yeti_wall_trophy", "alpha_yeti_wall_trophy");
				remapBlock(mapping, "boss_spawner_naga", "naga_boss_spawner");
				remapBlock(mapping, "boss_spawner_lich", "lich_boss_spawner");
				remapBlock(mapping, "boss_spawner_minoshroom", "minoshroom_boss_spawner");
				remapBlock(mapping, "boss_spawner_hydra", "hydra_boss_spawner");
				remapBlock(mapping, "boss_spawner_knight_phantom", "knight_phantom_boss_spawner");
				remapBlock(mapping, "boss_spawner_ur_ghast", "ur_ghast_boss_spawner");
				remapBlock(mapping, "boss_spawner_alpha_yeti", "alpha_yeti_boss_spawner");
				remapBlock(mapping, "boss_spawner_snow_queen", "snow_queen_boss_spawner");
				remapBlock(mapping, "boss_spawner_final_boss", "final_boss_boss_spawner");

				remapBlock(mapping, "etched_nagastone_weathered", "cracked_etched_nagastone");
				remapBlock(mapping, "etched_nagastone_mossy", "mossy_etched_nagastone");
				remapBlock(mapping, "nagastone_pillar_weathered", "cracked_nagastone_pillar");
				remapBlock(mapping, "nagastone_pillar_mossy", "mossy_nagastone_pillar");
				remapBlock(mapping, "nagastone_stairs_weathered_left", "cracked_nagastone_stairs_left");
				remapBlock(mapping, "nagastone_stairs_mossy_left", "mossy_nagastone_stairs_left");
				remapBlock(mapping, "nagastone_stairs_weathered_right", "cracked_nagastone_stairs_right");
				remapBlock(mapping, "nagastone_stairs_mossy_right", "mossy_nagastone_stairs_right");
				remapBlock(mapping, "naga_stone_head", "nagastone_head");
				remapBlock(mapping, "naga_stone", "nagastone");

				remapBlock(mapping, "stone_twist", "twisted_stone");
				remapBlock(mapping, "stone_twist_thin", "twisted_stone_pillar");
				remapBlock(mapping, "stone_pillar_bold", "bold_stone_pillar");
				remapBlock(mapping, "empty_bookshelf", "empty_canopy_bookshelf");

				remapBlock(mapping, "huge_lilypad", "huge_lily_pad");
				remapBlock(mapping, "huge_waterlily", "huge_water_lily");

				remapBlock(mapping, "maze_stone", "mazestone");
				remapBlock(mapping, "maze_stone_brick", "mazestone_brick");
				remapBlock(mapping, "maze_stone_cracked", "cracked_mazestone");
				remapBlock(mapping, "maze_stone_mossy", "mossy_mazestone");
				remapBlock(mapping, "maze_stone_decorative", "decorative_mazestone");
				remapBlock(mapping, "maze_stone_chiseled", "cut_mazestone");
				remapBlock(mapping, "maze_stone_border", "mazestone_border");
				remapBlock(mapping, "maze_stone_mosaic", "mazestone_mosaic");

				remapBlock(mapping, "underbrick_cracked", "cracked_underbrick");
				remapBlock(mapping, "underbrick_mossy", "mossy_underbrick");

				remapBlock(mapping, "tower_wood", "towerwood");
				remapBlock(mapping, "tower_wood_cracked", "cracked_towerwood");
				remapBlock(mapping, "tower_wood_mossy", "mossy_towerwood");
				remapBlock(mapping, "tower_wood_infested", "infested_towerwood");
				remapBlock(mapping, "tower_wood_encased", "encased_towerwood");

				remapBlock(mapping, "deadrock_cracked", "cracked_deadrock");
				remapBlock(mapping, "deadrock_weathered", "weathered_deadrock");

				remapBlock(mapping, "castle_brick_worn", "worn_castle_brick");
				remapBlock(mapping, "castle_brick_cracked", "cracked_castle_brick");
				remapBlock(mapping, "castle_brick_mossy", "mossy_castle_brick");
				remapBlock(mapping, "castle_brick_frame", "thick_castle_brick");
				remapBlock(mapping, "castle_brick_roof", "castle_roof_tile");
				remapBlock(mapping, "castle_pillar_encased", "encased_castle_brick_pillar");
				remapBlock(mapping, "castle_pillar_encased_tile", "encased_castle_brick_tile");
				remapBlock(mapping, "castle_pillar_bold", "bold_castle_brick_pillar");
				remapBlock(mapping, "castle_pillar_bold_tile", "bold_castle_brick_tile");
				remapBlock(mapping, "castle_stairs_brick", "castle_brick_stairs");
				remapBlock(mapping, "castle_stairs_worn", "worn_castle_brick_stairs");
				remapBlock(mapping, "castle_stairs_cracked", "cracked_castle_brick_stairs");
				remapBlock(mapping, "castle_stairs_mossy", "mossy_castle_brick_stairs");
				remapBlock(mapping, "castle_stairs_encased", "encased_castle_brick_stairs");
				remapBlock(mapping, "castle_stairs_bold", "bold_castle_brick_stairs");
				remapBlock(mapping, "castle_rune_brick_pink", "pink_castle_rune_brick");
				remapBlock(mapping, "castle_rune_brick_yellow", "yellow_castle_rune_brick");
				remapBlock(mapping, "castle_rune_brick_blue", "blue_castle_rune_brick");
				remapBlock(mapping, "castle_rune_brick_purple", "violet_castle_rune_brick");
				remapBlock(mapping, "castle_door_pink", "pink_castle_door");
				remapBlock(mapping, "castle_door_yellow", "yellow_castle_door");
				remapBlock(mapping, "castle_door_blue", "blue_castle_door");
				remapBlock(mapping, "castle_door_purple", "violet_castle_door");
				remapBlock(mapping, "force_field_pink", "pink_force_field");
				remapBlock(mapping, "force_field_orange", "orange_force_field");
				remapBlock(mapping, "force_field_green", "green_force_field");
				remapBlock(mapping, "force_field_blue", "blue_force_field");
				remapBlock(mapping, "force_field_purple", "violet_force_field");

				remapBlock(mapping, "rainboak_leaves", "rainbow_oak_leaves");
				remapBlock(mapping, "rainboak_sapling", "rainbow_oak_sapling");
				remapBlock(mapping, "potted_rainboak_sapling", "potted_rainbow_oak_sapling");

				//please let this work
				remapBlock(mapping, "trans_", "transformation_");
				remapBlock(mapping, "mine_", "mining_");
				remapBlock(mapping, "sort_", "sorting_");

				remapBlock(mapping, "_gate", "_fence_gate");
				remapBlock(mapping, "_plate", "_pressure_plate");

			}
		}
	}

	@SubscribeEvent
	public static void remapItems(RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
			if(mapping.key.getNamespace().equals(TwilightForestMod.ID)) {
				remapItem(mapping, "yeti_trophy", "alpha_yeti_trophy");
				remapItem(mapping, "boss_spawner_naga", "naga_boss_spawner");
				remapItem(mapping, "boss_spawner_lich", "lich_boss_spawner");
				remapItem(mapping, "boss_spawner_minoshroom", "minoshroom_boss_spawner");
				remapItem(mapping, "boss_spawner_hydra", "hydra_boss_spawner");
				remapItem(mapping, "boss_spawner_knight_phantom", "knight_phantom_boss_spawner");
				remapItem(mapping, "boss_spawner_ur_ghast", "ur_ghast_boss_spawner");
				remapItem(mapping, "boss_spawner_alpha_yeti", "alpha_yeti_boss_spawner");
				remapItem(mapping, "boss_spawner_snow_queen", "snow_queen_boss_spawner");
				remapItem(mapping, "boss_spawner_final_boss", "final_boss_boss_spawner");

				remapItem(mapping, "etched_nagastone_weathered", "cracked_etched_nagastone");
				remapItem(mapping, "etched_nagastone_mossy", "mossy_etched_nagastone");
				remapItem(mapping, "nagastone_pillar_weathered", "cracked_nagastone_pillar");
				remapItem(mapping, "nagastone_pillar_mossy", "mossy_nagastone_pillar");
				remapItem(mapping, "nagastone_stairs_weathered_left", "cracked_nagastone_stairs_left");
				remapItem(mapping, "nagastone_stairs_mossy_left", "mossy_nagastone_stairs_left");
				remapItem(mapping, "nagastone_stairs_weathered_right", "cracked_nagastone_stairs_right");
				remapItem(mapping, "nagastone_stairs_mossy_right", "mossy_nagastone_stairs_right");
				remapItem(mapping, "naga_stone_head", "nagastone_head");
				remapItem(mapping, "naga_stone", "nagastone");

				remapItem(mapping, "stone_twist", "twisted_stone");
				remapItem(mapping, "stone_twist_thin", "twisted_stone_pillar");
				remapItem(mapping, "stone_pillar_bold", "bold_stone_pillar");
				remapItem(mapping, "empty_bookshelf", "empty_canopy_bookshelf");

				remapItem(mapping, "huge_lilypad", "huge_lily_pad");
				remapItem(mapping, "huge_waterlily", "huge_water_lily");

				remapItem(mapping, "maze_stone", "mazestone");
				remapItem(mapping, "maze_stone_brick", "mazestone_brick");
				remapItem(mapping, "maze_stone_cracked", "cracked_mazestone");
				remapItem(mapping, "maze_stone_mossy", "mossy_mazestone");
				remapItem(mapping, "maze_stone_decorative", "decorative_mazestone");
				remapItem(mapping, "maze_stone_chiseled", "cut_mazestone");
				remapItem(mapping, "maze_stone_border", "mazestone_border");
				remapItem(mapping, "maze_stone_mosaic", "mazestone_mosaic");

				remapItem(mapping, "underbrick_cracked", "cracked_underbrick");
				remapItem(mapping, "underbrick_mossy", "mossy_underbrick");

				remapItem(mapping, "tower_wood", "towerwood");
				remapItem(mapping, "tower_wood_cracked", "cracked_towerwood");
				remapItem(mapping, "tower_wood_mossy", "mossy_towerwood");
				remapItem(mapping, "tower_wood_infested", "infested_towerwood");
				remapItem(mapping, "tower_wood_encased", "encased_towerwood");

				remapItem(mapping, "deadrock_cracked", "cracked_deadrock");
				remapItem(mapping, "deadrock_weathered", "weathered_deadrock");

				remapItem(mapping, "castle_brick_worn", "worn_castle_brick");
				remapItem(mapping, "castle_brick_cracked", "cracked_castle_brick");
				remapItem(mapping, "castle_brick_mossy", "mossy_castle_brick");
				remapItem(mapping, "castle_brick_frame", "thick_castle_brick");
				remapItem(mapping, "castle_brick_roof", "castle_roof_tile");
				remapItem(mapping, "castle_pillar_encased", "encased_castle_brick_pillar");
				remapItem(mapping, "castle_pillar_encased_tile", "encased_castle_brick_tile");
				remapItem(mapping, "castle_pillar_bold", "bold_castle_brick_pillar");
				remapItem(mapping, "castle_pillar_bold_tile", "bold_castle_brick_tile");
				remapItem(mapping, "castle_stairs_brick", "castle_brick_stairs");
				remapItem(mapping, "castle_stairs_worn", "worn_castle_brick_stairs");
				remapItem(mapping, "castle_stairs_cracked", "cracked_castle_brick_stairs");
				remapItem(mapping, "castle_stairs_mossy", "mossy_castle_brick_stairs");
				remapItem(mapping, "castle_stairs_encased", "encased_castle_brick_stairs");
				remapItem(mapping, "castle_stairs_bold", "bold_castle_brick_stairs");
				remapItem(mapping, "castle_rune_brick_pink", "pink_castle_rune_brick");
				remapItem(mapping, "castle_rune_brick_yellow", "yellow_castle_rune_brick");
				remapItem(mapping, "castle_rune_brick_blue", "blue_castle_rune_brick");
				remapItem(mapping, "castle_rune_brick_purple", "violet_castle_rune_brick");
				remapItem(mapping, "castle_door_pink", "pink_castle_door");
				remapItem(mapping, "castle_door_yellow", "yellow_castle_door");
				remapItem(mapping, "castle_door_blue", "blue_castle_door");
				remapItem(mapping, "castle_door_purple", "violet_castle_door");
				remapItem(mapping, "force_field_pink", "pink_force_field");
				remapItem(mapping, "force_field_orange", "orange_force_field");
				remapItem(mapping, "force_field_green", "green_force_field");
				remapItem(mapping, "force_field_blue", "blue_force_field");
				remapItem(mapping, "force_field_purple", "violet_force_field");

				remapItem(mapping, "rainboak_leaves", "rainbow_oak_leaves");
				remapItem(mapping, "rainboak_sapling", "rainbow_oak_sapling");
				remapItem(mapping, "potted_rainboak_sapling", "potted_rainbow_oak_sapling");

				//please let this work
				remapItem(mapping, "trans_", "transformation_");
				remapItem(mapping, "mine_", "mining_");
				remapItem(mapping, "sort_", "sorting_");

				remapItem(mapping, "_gate", "_fence_gate");
				remapItem(mapping, "_plate", "_pressure_plate");

				remapItem(mapping, "shield_scepter", "fortification_scepter");
				remapItem(mapping, "magic_map", "filled_magic_map");
				remapItem(mapping, "maze_map", "filled_maze_map");
				remapItem(mapping, "ore_map", "filled_ore_map");
				remapItem(mapping, "magic_map_empty", "magic_map");
				remapItem(mapping, "maze_map_empty", "maze_map");
				remapItem(mapping, "ore_map_empty", "ore_map");
				remapItem(mapping, "ironwood_raw", "raw_ironwood");
				remapItem(mapping, "minotaur_axe_gold", "gold_minotaur_axe");
				remapItem(mapping, "minotaur_axe", "diamond_minotaur_axe");
				remapItem(mapping, "peacock_fan", "peacock_feather_fan");
				remapItem(mapping, "alpha_fur", "alpha_yeti_fur");
				remapItem(mapping, "questing_ram_banner_pattern", "quest_ram_banner_pattern");

				remapItem(mapping, "bunny_spawn_egg", "dwarf_rabbit_spawn_egg");
				remapItem(mapping, "goblin_knight_lower_spawn_egg", "lower_goblin_knight_spawn_egg");
				remapItem(mapping, "mini_ghast_spawn_egg", "carminite_ghastling_spawn_egg");
				remapItem(mapping, "tower_ghast_spawn_egg", "carminite_ghastguard_spawn_egg");
				remapItem(mapping, "tower_golem_spawn_egg", "carminite_golem_spawn_egg");
				remapItem(mapping, "tower_broodling_spawn_egg", "carminite_broodling_spawn_egg");
				remapItem(mapping, "tower_termite_spawn_egg", "towerwood_borer_spawn_egg");
				remapItem(mapping, "wild_boar_spawn_egg", "boar_spawn_egg");
				remapItem(mapping, "yeti_alpha_spawn_egg", "alpha_yeti_spawn_egg");
			}
		}
	}

	@SubscribeEvent
	public static void remapEntities(RegistryEvent.MissingMappings<EntityType<?>> event) {
		for (RegistryEvent.MissingMappings.Mapping<EntityType<?>> mapping : event.getAllMappings()) {
			if (mapping.key.getNamespace().equals(TwilightForestMod.ID)) {
				remapEntity(mapping, "wild_boar", "boar");
				remapEntity(mapping, "bunny", "dwarf_rabbit");
				remapEntity(mapping, "mini_ghast", "carminite_ghastling");
				remapEntity(mapping, "tower_ghast", "carminite_ghastguard");
				remapEntity(mapping, "tower_golem", "carminite_golem");
				remapEntity(mapping, "tower_broodling", "carminite_broodling");
				remapEntity(mapping, "tower_termite", "towerwood_borer");
				remapEntity(mapping, "goblin_knight_upper", "upper_goblin_knight");
				remapEntity(mapping, "goblin_knight_lower", "lower_goblin_knight");
				remapEntity(mapping, "yeti_alpha", "alpha_yeti");
			}
		}
	}

	private static void remapBlock(RegistryEvent.MissingMappings.Mapping<Block> mapping, String oldId, String newId) {
		if(mapping.key.getPath().contains(oldId)) {
			String newName = mapping.key.getPath().replace(oldId, newId);
			ResourceLocation remap = new ResourceLocation(TwilightForestMod.ID, newName);
			mapping.remap(ForgeRegistries.BLOCKS.getValue(remap));
		}
	}

	private static void remapItem(RegistryEvent.MissingMappings.Mapping<Item> mapping, String oldId, String newId) {
		if(mapping.key.getPath().contains(oldId)) {
			String newName = mapping.key.getPath().replace(oldId, newId);
			ResourceLocation remap = new ResourceLocation(TwilightForestMod.ID, newName);
			mapping.remap(ForgeRegistries.ITEMS.getValue(remap));
		}
	}

	private static void remapEntity(RegistryEvent.MissingMappings.Mapping<EntityType<?>> mapping, String oldId, String newId) {
		if(mapping.key.getPath().contains(oldId)) {
			String newName = mapping.key.getPath().replace(oldId, newId);
			ResourceLocation remap = new ResourceLocation(TwilightForestMod.ID, newName);
			mapping.remap(ForgeRegistries.ENTITIES.getValue(remap));
		}
	}

}
