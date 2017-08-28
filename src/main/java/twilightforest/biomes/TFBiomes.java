package twilightforest.biomes;

import net.minecraft.world.biome.Biome;
import twilightforest.TwilightForestMod;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("all")
@ObjectHolder(TwilightForestMod.ID)
public class TFBiomes {
	@ObjectHolder("twilight_lake")
	public static final Biome tfLake;
	@ObjectHolder("twilight_forest")
	public static final Biome twilightForest;
	@ObjectHolder("dense_twilight_forest")
	public static final Biome denseTwilightForest;
	@ObjectHolder("twilight_highlands")
	public static final Biome highlands;
	@ObjectHolder("mushroom_forest")
	public static final Biome mushrooms;
	@ObjectHolder("twilight_swamp")
	public static final Biome tfSwamp;
	@ObjectHolder("twilight_stream")
	public static final Biome stream;
	@ObjectHolder("snowy_forest")
	public static final Biome snowy_forest;
	@ObjectHolder("twilight_glacier")
	public static final Biome glacier;
	@ObjectHolder("twilight_clearing")
	public static final Biome clearing;
	@ObjectHolder("oak_savannah")
	public static final Biome oakSavanna;
	@ObjectHolder("firefly_forest")
	public static final Biome fireflyForest;
	@ObjectHolder("deep_mushroom_forest")
	public static final Biome deepMushrooms;
	@ObjectHolder("dark_forest")
	public static final Biome darkForest;
	@ObjectHolder("enchanted_forest")
	public static final Biome enchantedForest;
	@ObjectHolder("fire_swamp")
	public static final Biome fireSwamp;
	@ObjectHolder("dark_forest_center")
	public static final Biome darkForestCenter;
	@ObjectHolder("highlands_center")
	public static final Biome highlandsCenter;
	@ObjectHolder("thornlands")
	public static final Biome thornlands;

	//Much as I hate doing this, it tricks IntelliJ into thinking they're not null
	static {
		tfLake = null;
		twilightForest = null;
		denseTwilightForest = null;
		highlands = null;
		mushrooms = null;
		tfSwamp = null;
		stream = null;
		snowy_forest = null;
		glacier = null;
		clearing = null;
		oakSavanna = null;
		fireflyForest = null;
		deepMushrooms = null;
		darkForest = null;
		enchantedForest = null;
		fireSwamp = null;
		darkForestCenter = null;
		highlandsCenter = null;
		thornlands = null;

	}
}
