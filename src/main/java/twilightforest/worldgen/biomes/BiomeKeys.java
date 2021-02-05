package twilightforest.worldgen.biomes;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import twilightforest.TwilightForestMod;

public class BiomeKeys {
	public static final RegistryKey<Biome> FOREST = makeKey("forest");
	public static final RegistryKey<Biome> DENSE_FOREST = makeKey("dense_forest");
	public static final RegistryKey<Biome> FIREFLY_FOREST = makeKey("firefly_forest");
	public static final RegistryKey<Biome> CLEARING = makeKey("clearing");
	public static final RegistryKey<Biome> OAK_SAVANNAH = makeKey("oak_savannah");
	public static final RegistryKey<Biome> STREAM = makeKey("stream");
	public static final RegistryKey<Biome> LAKE = makeKey("lake");

	public static final RegistryKey<Biome> MUSHROOM_FOREST = makeKey("mushroom_forest");
	public static final RegistryKey<Biome> DENSE_MUSHROOM_FOREST = makeKey("dense_mushroom_forest");

	public static final RegistryKey<Biome> ENCHANTED_FOREST = makeKey("enchanted_forest");
	public static final RegistryKey<Biome> SPOOKY_FOREST = makeKey("spooky_forest");

	public static final RegistryKey<Biome> SWAMP = makeKey("swamp");
	public static final RegistryKey<Biome> FIRE_SWAMP = makeKey("fire_swamp");

	public static final RegistryKey<Biome> DARK_FOREST = makeKey("dark_forest");
	public static final RegistryKey<Biome> DARK_FOREST_CENTER = makeKey("dark_forest_center");

	public static final RegistryKey<Biome> SNOWY_FOREST = makeKey("snowy_forest");
	public static final RegistryKey<Biome> GLACIER = makeKey("glacier");

	public static final RegistryKey<Biome> HIGHLANDS = makeKey("highlands");
	public static final RegistryKey<Biome> THORNLANDS = makeKey("thornlands");
	public static final RegistryKey<Biome> FINAL_PLATEAU = makeKey("final_plateau");

	private static RegistryKey<Biome> makeKey(String name) {
		return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, TwilightForestMod.prefix(name));
	}

	public static final BiomeDictionary.Type TWILIGHT = BiomeDictionary.Type.getType("TWILIGHT");

	public static void addBiomeTypes() {
		BiomeDictionary.addTypes(FOREST, TWILIGHT, BiomeDictionary.Type.FOREST);
		BiomeDictionary.addTypes(DENSE_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
		BiomeDictionary.addTypes(FIREFLY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.LUSH);
		BiomeDictionary.addTypes(CLEARING, TWILIGHT, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.addTypes(OAK_SAVANNAH, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.addTypes(STREAM, TWILIGHT, BiomeDictionary.Type.RIVER);
		BiomeDictionary.addTypes(LAKE, TWILIGHT, BiomeDictionary.Type.OCEAN);

		BiomeDictionary.addTypes(MUSHROOM_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM);
		BiomeDictionary.addTypes(DENSE_MUSHROOM_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM);

		BiomeDictionary.addTypes(ENCHANTED_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL);
		BiomeDictionary.addTypes(SPOOKY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);

		BiomeDictionary.addTypes(SWAMP, TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
		BiomeDictionary.addTypes(FIRE_SWAMP, TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.HOT);

		BiomeDictionary.addTypes(DARK_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
		BiomeDictionary.addTypes(DARK_FOREST_CENTER, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.MAGICAL);

		BiomeDictionary.addTypes(SNOWY_FOREST, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
		BiomeDictionary.addTypes(GLACIER, TWILIGHT, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.WASTELAND);

		BiomeDictionary.addTypes(HIGHLANDS, TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.CONIFEROUS);
		BiomeDictionary.addTypes(THORNLANDS, TWILIGHT, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND);
		BiomeDictionary.addTypes(FINAL_PLATEAU, TWILIGHT, BiomeDictionary.Type.MESA, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND);
	}
}
