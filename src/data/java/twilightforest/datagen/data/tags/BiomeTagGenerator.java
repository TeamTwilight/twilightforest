package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.tags.TFBiomeTags;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends BiomeTagsProvider {

	public BiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		this.tag(TFBiomeTags.IS_TWILIGHT).add(
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

		this.tag(TFBiomeTags.VALID_QUEST_GROVE_BIOMES).add(TFBiomes.ENCHANTED_FOREST);
		this.tag(TFBiomeTags.VALID_MUSHROOM_TOWER_BIOMES).add(TFBiomes.DENSE_MUSHROOM_FOREST);

		this.tag(TFBiomeTags.VALID_CAMP_BIOMES).add(
			TFBiomes.OAK_SAVANNAH, TFBiomes.CLEARING, TFBiomes.MUSHROOM_FOREST, TFBiomes.FOREST, TFBiomes.FIREFLY_FOREST
		);

		this.tag(TFBiomeTags.VALID_HOLLOW_TREE_BIOMES).add(
			TFBiomes.DENSE_FOREST, TFBiomes.FIRE_SWAMP,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.ENCHANTED_FOREST
		);
		this.tag(TFBiomeTags.VALID_HEDGE_MAZE_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.tag(TFBiomeTags.VALID_HOLLOW_HILL_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.tag(TFBiomeTags.VALID_NAGA_COURTYARD_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.tag(TFBiomeTags.VALID_LICH_TOWER_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.tag(TFBiomeTags.VALID_LABYRINTH_BIOMES).add(TFBiomes.SWAMP);
		this.tag(TFBiomeTags.VALID_HYDRA_LAIR_BIOMES).add(TFBiomes.FIRE_SWAMP);
		this.tag(TFBiomeTags.VALID_KNIGHT_STRONGHOLD_BIOMES).add(TFBiomes.DARK_FOREST);
		this.tag(TFBiomeTags.VALID_DARK_TOWER_BIOMES).add(TFBiomes.DARK_FOREST_CENTER);
		this.tag(TFBiomeTags.VALID_YETI_CAVE_BIOMES).add(TFBiomes.SNOWY_FOREST);
		this.tag(TFBiomeTags.VALID_AURORA_PALACE_BIOMES).add(TFBiomes.GLACIER);
		this.tag(TFBiomeTags.VALID_TROLL_CAVE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.tag(TFBiomeTags.VALID_GIANT_HOUSE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.tag(TFBiomeTags.VALID_FINAL_CASTLE_BIOMES).add(TFBiomes.FINAL_PLATEAU);

		//apparently using forge and vanilla tags allows other mods to spawn stuff in our biomes. Will keep these commented out here just in case we need to reference them later.
		//vanilla biome categories
//		this.tag(BiomeTags.IS_FOREST).add(
//				TFBiomes.FOREST, TFBiomes.DENSE_FOREST, TFBiomes.FIREFLY_FOREST,
//				TFBiomes.OAK_SAVANNAH, TFBiomes.MUSHROOM_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST,
//				TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER,
//				TFBiomes.SNOWY_FOREST, TFBiomes.HIGHLANDS
//		);
//		this.tag(BiomeTags.IS_MOUNTAIN).add(TFBiomes.HIGHLANDS);
//		this.tag(BiomeTags.IS_HILL).add(TFBiomes.THORNLANDS);

		//forge biome categories
//		this.tag(Tags.Biomes.IS_DENSE).add(TFBiomes.DENSE_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER, TFBiomes.SNOWY_FOREST, TFBiomes.THORNLANDS);
//		this.tag(Tags.Biomes.IS_SPARSE).add(TFBiomes.CLEARING, TFBiomes.OAK_SAVANNAH, TFBiomes.GLACIER, TFBiomes.FINAL_PLATEAU);
//		this.tag(Tags.Biomes.IS_PLAINS).add(TFBiomes.CLEARING);
//		this.tag(Tags.Biomes.IS_MUSHROOM).add(TFBiomes.MUSHROOM_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST);
//		this.tag(Tags.Biomes.IS_RARE).add(TFBiomes.ENCHANTED_FOREST, TFBiomes.SPOOKY_FOREST, TFBiomes.CLEARING, TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.LAKE);
//		this.tag(Tags.Biomes.IS_WATER).add(TFBiomes.LAKE, TFBiomes.STREAM);
//		this.tag(Tags.Biomes.IS_MAGICAL).add(TFBiomes.ENCHANTED_FOREST, TFBiomes.DARK_FOREST_CENTER);
//		this.tag(Tags.Biomes.IS_SPOOKY).add(TFBiomes.SPOOKY_FOREST, TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER);
//		this.tag(Tags.Biomes.IS_DEAD).add(TFBiomes.SPOOKY_FOREST, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		this.tag(Tags.Biomes.IS_SWAMP).add(TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP);
//		this.tag(Tags.Biomes.IS_SNOWY).add(TFBiomes.SNOWY_FOREST);
//		this.tag(Tags.Biomes.IS_CONIFEROUS).add(TFBiomes.SNOWY_FOREST, TFBiomes.HIGHLANDS);
//		this.tag(Tags.Biomes.IS_COLD).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
//		this.tag(Tags.Biomes.IS_WASTELAND).add(TFBiomes.GLACIER, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		this.tag(Tags.Biomes.IS_DRY).add(TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU);
//		this.tag(Tags.Biomes.IS_PLATEAU).add(TFBiomes.FINAL_PLATEAU);
//		this.tag(Tags.Biomes.IS_UNDERGROUND).add(TFBiomes.UNDERGROUND);

		//other vanilla tags
		this.tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(TFBiomeTags.IS_TWILIGHT);
		this.tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(TFBiomeTags.IS_TWILIGHT);

		//even though we won't spawn vanilla frogs, we'll still add support for the variants
		this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP);

		this.tag(BiomeTags.SPAWNS_SNOW_FOXES).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		this.tag(BiomeTags.SPAWNS_WHITE_RABBITS).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}