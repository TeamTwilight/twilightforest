package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends BiomeTagsProvider {

	public static final TagKey<Biome> IS_TWILIGHT = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("in_twilight_forest"));

	public static final TagKey<Biome> VALID_QUEST_GROVE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_quest_grove_biomes"));

	public static final TagKey<Biome> VALID_HEDGE_MAZE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_hedge_maze_biomes"));
	public static final TagKey<Biome> VALID_HOLLOW_HILL_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_hollow_hill_biomes"));
	public static final TagKey<Biome> VALID_MUSHROOM_TOWER_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_mushroom_tower_biomes"));

	public static final TagKey<Biome> VALID_NAGA_COURTYARD_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_naga_courtyard_biomes"));
	public static final TagKey<Biome> VALID_LICH_TOWER_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_lich_tower_biomes"));
	public static final TagKey<Biome> VALID_LABYRINTH_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_labyrinth_biomes"));
	public static final TagKey<Biome> VALID_HYDRA_LAIR_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_hydra_lair_biomes"));
	public static final TagKey<Biome> VALID_KNIGHT_STRONGHOLD_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_knight_stronghold_biomes"));
	public static final TagKey<Biome> VALID_DARK_TOWER_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_dark_tower_biomes"));
	public static final TagKey<Biome> VALID_YETI_CAVE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_yeti_cave_biomes"));
	public static final TagKey<Biome> VALID_AURORA_PALACE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_aurora_palace_biomes"));
	public static final TagKey<Biome> VALID_TROLL_CAVE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_troll_cave_biomes"));
	public static final TagKey<Biome> VALID_GIANT_HOUSE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_giant_house_biomes"));
	public static final TagKey<Biome> VALID_FINAL_CASTLE_BIOMES = TagKey.create(Registries.BIOME, TwilightForestMod.prefix("valid_final_castle_biomes"));

	public BiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
		super(output, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(IS_TWILIGHT).add(
				TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
				TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
				TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
				TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST,
				TFBiomes.ENCHANTED_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST,
				TFBiomes.LAKE, TFBiomes.STREAM, TFBiomes.UNDERGROUND,
				TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP,
				TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER,
				TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER,
				TFBiomes.HIGHLANDS, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU
		);

		tag(VALID_QUEST_GROVE_BIOMES).add(TFBiomes.ENCHANTED_FOREST);
		tag(VALID_MUSHROOM_TOWER_BIOMES).add(TFBiomes.DENSE_MUSHROOM_FOREST);

		tag(VALID_HEDGE_MAZE_BIOMES).add(
				TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
				TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
				TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
				TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		tag(VALID_HOLLOW_HILL_BIOMES).add(
				TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
				TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
				TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
				TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		tag(VALID_NAGA_COURTYARD_BIOMES).add(
				TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
				TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
				TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
				TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		tag(VALID_LICH_TOWER_BIOMES).add(
				TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
				TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
				TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
				TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		tag(VALID_LABYRINTH_BIOMES).add(TFBiomes.SWAMP);
		tag(VALID_HYDRA_LAIR_BIOMES).add(TFBiomes.FIRE_SWAMP);
		tag(VALID_KNIGHT_STRONGHOLD_BIOMES).add(TFBiomes.DARK_FOREST);
		tag(VALID_DARK_TOWER_BIOMES).add(TFBiomes.DARK_FOREST_CENTER);
		tag(VALID_YETI_CAVE_BIOMES).add(TFBiomes.SNOWY_FOREST);
		tag(VALID_AURORA_PALACE_BIOMES).add(TFBiomes.GLACIER);
		tag(VALID_TROLL_CAVE_BIOMES).add(TFBiomes.HIGHLANDS);
		tag(VALID_GIANT_HOUSE_BIOMES).add(TFBiomes.HIGHLANDS);
		tag(VALID_FINAL_CASTLE_BIOMES).add(TFBiomes.FINAL_PLATEAU);

		//apparently using forge and vanilla tags allows other mods to spawn stuff in our biomes. Will keep these commented out here just in case we need to reference them later.
		//vanilla biome categories
//		tag(BiomeTags.IS_FOREST).add(
//				TFBiomes.FOREST, TFBiomes.DENSE_FOREST, TFBiomes.FIREFLY_FOREST,
//				TFBiomes.OAK_SAVANNAH, TFBiomes.MUSHROOM_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST,
//				TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER,
//				TFBiomes.SNOWY_FOREST, TFBiomes.HIGHLANDS
//		);
//		tag(BiomeTags.IS_MOUNTAIN).add(TFBiomes.HIGHLANDS);
//		tag(BiomeTags.IS_HILL).add(TFBiomes.THORNLANDS);

		//forge biome categories
//		tag(Tags.Biomes.IS_DENSE).add(TFBiomes.DENSE_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER, TFBiomes.SNOWY_FOREST, TFBiomes.THORNLANDS);
//		tag(Tags.Biomes.IS_SPARSE).add(TFBiomes.CLEARING, TFBiomes.OAK_SAVANNAH, TFBiomes.GLACIER, TFBiomes.FINAL_PLATEAU);
//		tag(Tags.Biomes.IS_PLAINS).add(TFBiomes.CLEARING);
//		tag(Tags.Biomes.IS_MUSHROOM).add(TFBiomes.MUSHROOM_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST);
//		tag(Tags.Biomes.IS_RARE).add(TFBiomes.ENCHANTED_FOREST, TFBiomes.SPOOKY_FOREST, TFBiomes.CLEARING, TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.LAKE);
//		tag(Tags.Biomes.IS_WATER).add(TFBiomes.LAKE, TFBiomes.STREAM);
//		tag(Tags.Biomes.IS_MAGICAL).add(TFBiomes.ENCHANTED_FOREST, TFBiomes.DARK_FOREST_CENTER);
//		tag(Tags.Biomes.IS_SPOOKY).add(TFBiomes.SPOOKY_FOREST, TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER);
//		tag(Tags.Biomes.IS_DEAD).add(TFBiomes.SPOOKY_FOREST, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		tag(Tags.Biomes.IS_SWAMP).add(TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP);
//		tag(Tags.Biomes.IS_SNOWY).add(TFBiomes.SNOWY_FOREST);
//		tag(Tags.Biomes.IS_CONIFEROUS).add(TFBiomes.SNOWY_FOREST, TFBiomes.HIGHLANDS);
//		tag(Tags.Biomes.IS_COLD).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
//		tag(Tags.Biomes.IS_WASTELAND).add(TFBiomes.GLACIER, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		tag(Tags.Biomes.IS_DRY).add(TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		tag(Tags.Biomes.IS_PLATEAU).add(TFBiomes.FINAL_PLATEAU);
//		tag(Tags.Biomes.IS_UNDERGROUND).add(TFBiomes.UNDERGROUND);

		//other vanilla tags
		tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(IS_TWILIGHT);
		tag(BiomeTags.WITHOUT_PATROL_SPAWNS).addTag(IS_TWILIGHT);
		tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(IS_TWILIGHT);

		//even though we won't spawn vanilla frogs, we'll still add support for the variants
		tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		tag(BiomeTags.SPAWNS_SNOW_FOXES).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		tag(BiomeTags.SNOW_GOLEM_MELTS).add(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP);
		tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP);

		tag(BiomeTags.HAS_CLOSER_WATER_FOG).add(TFBiomes.SPOOKY_FOREST, TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP);
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}