package twilightforest.data.tags;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.biomes.BiomeKeys;

import static twilightforest.TFConstants.MOD_ID;

public class BiomeTagGenerator extends BiomeTagsProvider {

	public static final TagKey<Biome> VALID_QUEST_GROVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_quest_grove_biomes"));

	public static final TagKey<Biome> VALID_HEDGE_MAZE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hedge_maze_biomes"));
	public static final TagKey<Biome> VALID_HOLLOW_HILL_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hollow_hill_biomes"));
	public static final TagKey<Biome> VALID_MUSHROOM_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_mushroom_tower_biomes"));

	public static final TagKey<Biome> VALID_NAGA_COURTYARD_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_naga_courtyard_biomes"));
	public static final TagKey<Biome> VALID_LICH_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_lich_tower_biomes"));
	public static final TagKey<Biome> VALID_LABYRINTH_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_labyrinth_biomes"));
	public static final TagKey<Biome> VALID_HYDRA_LAIR_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hydra_lair_biomes"));
	public static final TagKey<Biome> VALID_KNIGHT_STRONGHOLD_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_knight_stronghold_biomes"));
	public static final TagKey<Biome> VALID_DARK_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_dark_tower_biomes"));
	public static final TagKey<Biome> VALID_YETI_CAVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_yeti_cave_biomes"));
	public static final TagKey<Biome> VALID_AURORA_PALACE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_aurora_palace_biomes"));
	public static final TagKey<Biome> VALID_TROLL_CAVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_troll_cave_biomes"));
	public static final TagKey<Biome> VALID_FINAL_CASTLE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_final_castle_biomes"));

	public BiomeTagGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(VALID_QUEST_GROVE_BIOMES).add(BiomeKeys.ENCHANTED_FOREST);
		tag(VALID_MUSHROOM_TOWER_BIOMES).add(BiomeKeys.DENSE_MUSHROOM_FOREST);

		tag(VALID_HEDGE_MAZE_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_HOLLOW_HILL_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_NAGA_COURTYARD_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_LICH_TOWER_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_LABYRINTH_BIOMES).add(BiomeKeys.SWAMP);
		tag(VALID_HYDRA_LAIR_BIOMES).add(BiomeKeys.FIRE_SWAMP);
		tag(VALID_KNIGHT_STRONGHOLD_BIOMES).add(BiomeKeys.DARK_FOREST);
		tag(VALID_DARK_TOWER_BIOMES).add(BiomeKeys.DARK_FOREST_CENTER);
		tag(VALID_YETI_CAVE_BIOMES).add(BiomeKeys.SNOWY_FOREST);
		tag(VALID_AURORA_PALACE_BIOMES).add(BiomeKeys.GLACIER);
		tag(VALID_TROLL_CAVE_BIOMES).add(BiomeKeys.HIGHLANDS);
		tag(VALID_FINAL_CASTLE_BIOMES).add(BiomeKeys.FINAL_PLATEAU);
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}