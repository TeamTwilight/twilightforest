package twilightforest.world.registration.biomes;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;

public class BiomeKeys {
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, TwilightForestMod.ID);

	public static final ResourceKey<Biome> FOREST = makeKey("forest");
	public static final ResourceKey<Biome> DENSE_FOREST = makeKey("dense_forest");
	public static final ResourceKey<Biome> FIREFLY_FOREST = makeKey("firefly_forest");
	public static final ResourceKey<Biome> CLEARING = makeKey("clearing");
	public static final ResourceKey<Biome> OAK_SAVANNAH = makeKey("oak_savannah");
	public static final ResourceKey<Biome> STREAM = makeKey("stream");
	public static final ResourceKey<Biome> LAKE = makeKey("lake");

	public static final ResourceKey<Biome> MUSHROOM_FOREST = makeKey("mushroom_forest");
	public static final ResourceKey<Biome> DENSE_MUSHROOM_FOREST = makeKey("dense_mushroom_forest");

	public static final ResourceKey<Biome> ENCHANTED_FOREST = makeKey("enchanted_forest");
	public static final ResourceKey<Biome> SPOOKY_FOREST = makeKey("spooky_forest");

	public static final ResourceKey<Biome> SWAMP = makeKey("swamp");
	public static final ResourceKey<Biome> FIRE_SWAMP = makeKey("fire_swamp");

	public static final ResourceKey<Biome> DARK_FOREST = makeKey("dark_forest");
	public static final ResourceKey<Biome> DARK_FOREST_CENTER = makeKey("dark_forest_center");

	public static final ResourceKey<Biome> SNOWY_FOREST = makeKey("snowy_forest");
	public static final ResourceKey<Biome> GLACIER = makeKey("glacier");

	public static final ResourceKey<Biome> HIGHLANDS = makeKey("highlands");
	public static final ResourceKey<Biome> THORNLANDS = makeKey("thornlands");
	public static final ResourceKey<Biome> FINAL_PLATEAU = makeKey("final_plateau");

	private static ResourceKey<Biome> makeKey(String name) {
		// Apparently this resolves biome shuffling /shrug
		BIOMES.register(name, () -> new Biome.BiomeBuilder()
				.precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NONE)
				//.depth(0)
				.downfall(0)
				//.scale(0)
				.temperature(0)
				.specialEffects(new BiomeSpecialEffects.Builder().fogColor(0).waterColor(0).waterFogColor(0).skyColor(0).build())
				.generationSettings(new BiomeGenerationSettings.Builder().build())
				.mobSpawnSettings(new MobSpawnSettings.Builder().build())
				.temperatureAdjustment(Biome.TemperatureModifier.NONE)
				.build());
		return ResourceKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix(name));
	}

	public static final BiomeDictionary.Type TWILIGHT = BiomeDictionary.Type.getType("TWILIGHT");

	public static void addBiomeTypes() {
		BiomeDictionary.addTypes(FOREST, TWILIGHT, BiomeDictionary.Type.FOREST);
		BiomeDictionary.addTypes(DENSE_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
		BiomeDictionary.addTypes(FIREFLY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST);
		BiomeDictionary.addTypes(CLEARING, TWILIGHT, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.RARE);
		BiomeDictionary.addTypes(OAK_SAVANNAH, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.addTypes(STREAM, TWILIGHT, BiomeDictionary.Type.RIVER);
		BiomeDictionary.addTypes(LAKE, TWILIGHT, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.RARE);

		BiomeDictionary.addTypes(MUSHROOM_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM);
		BiomeDictionary.addTypes(DENSE_MUSHROOM_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.RARE, BiomeDictionary.Type.DENSE);

		BiomeDictionary.addTypes(ENCHANTED_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
		BiomeDictionary.addTypes(SPOOKY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.RARE);

		BiomeDictionary.addTypes(SWAMP, TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
		BiomeDictionary.addTypes(FIRE_SWAMP, TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.HOT);

		BiomeDictionary.addTypes(DARK_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
		BiomeDictionary.addTypes(DARK_FOREST_CENTER, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.MAGICAL);

		BiomeDictionary.addTypes(SNOWY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.DENSE);
		BiomeDictionary.addTypes(GLACIER, TWILIGHT, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.SPARSE);

		BiomeDictionary.addTypes(HIGHLANDS, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.CONIFEROUS);
		BiomeDictionary.addTypes(THORNLANDS, TWILIGHT, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.DENSE);
		BiomeDictionary.addTypes(FINAL_PLATEAU, TWILIGHT, BiomeDictionary.Type.MESA, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.PLATEAU, BiomeDictionary.Type.SPARSE);
	}
}
