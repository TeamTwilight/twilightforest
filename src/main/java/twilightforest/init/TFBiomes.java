package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TwilightForestMod;

public class TFBiomes {

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
	public static final ResourceKey<Biome> HIGHLANDS_UNDERGROUND = makeKey("highlands_underground");

	public static final ResourceKey<Biome> THORNLANDS = makeKey("thornlands");
	public static final ResourceKey<Biome> FINAL_PLATEAU = makeKey("final_plateau");

	public static final ResourceKey<Biome> UNDERGROUND = makeKey("underground");

	private static ResourceKey<Biome> makeKey(String name) {
		return ResourceKey.create(Registries.BIOME, TwilightForestMod.prefix(name));
	}

}
