package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TwilightForestMod;

public class TFBiomeTags {

	public static final TagKey<Biome> IS_TWILIGHT = create("in_twilight_forest");

	public static final TagKey<Biome> VALID_QUEST_GROVE_BIOMES = create("valid_quest_grove_biomes");

	public static final TagKey<Biome> VALID_HOLLOW_TREE_BIOMES = create("valid_hollow_tree_biomes");
	public static final TagKey<Biome> VALID_HEDGE_MAZE_BIOMES = create("valid_hedge_maze_biomes");
	public static final TagKey<Biome> VALID_HOLLOW_HILL_BIOMES = create("valid_hollow_hill_biomes");
	public static final TagKey<Biome> VALID_CAMP_BIOMES = create("valid_camp_biomes");
	public static final TagKey<Biome> VALID_MUSHROOM_TOWER_BIOMES = create("valid_mushroom_tower_biomes");

	public static final TagKey<Biome> VALID_NAGA_COURTYARD_BIOMES = create("valid_naga_courtyard_biomes");
	public static final TagKey<Biome> VALID_LICH_TOWER_BIOMES = create("valid_lich_tower_biomes");
	public static final TagKey<Biome> VALID_LABYRINTH_BIOMES = create("valid_labyrinth_biomes");
	public static final TagKey<Biome> VALID_HYDRA_LAIR_BIOMES = create("valid_hydra_lair_biomes");
	public static final TagKey<Biome> VALID_KNIGHT_STRONGHOLD_BIOMES = create("valid_knight_stronghold_biomes");
	public static final TagKey<Biome> VALID_DARK_TOWER_BIOMES = create("valid_dark_tower_biomes");
	public static final TagKey<Biome> VALID_YETI_CAVE_BIOMES = create("valid_yeti_cave_biomes");
	public static final TagKey<Biome> VALID_AURORA_PALACE_BIOMES = create("valid_aurora_palace_biomes");
	public static final TagKey<Biome> VALID_TROLL_CAVE_BIOMES = create("valid_troll_cave_biomes");
	public static final TagKey<Biome> VALID_GIANT_HOUSE_BIOMES = create("valid_giant_house_biomes");
	public static final TagKey<Biome> VALID_FINAL_CASTLE_BIOMES = create("valid_final_castle_biomes");

	private static TagKey<Biome> create(String tagName) {
		return TagKey.create(Registries.BIOME, TwilightForestMod.prefix(tagName));
	}
}
