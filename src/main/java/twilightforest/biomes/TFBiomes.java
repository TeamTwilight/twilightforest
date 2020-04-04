package twilightforest.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.surfacebuilders.TFSurfaceBuilders;

public class TFBiomes {
	public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, TwilightForestMod.ID);

	public static final RegistryObject<Biome> tfLake = BIOMES.register("twilight_lake", () ->
			new TFBiomeTwilightLake(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.66F)
							.downfall(1)
							.depth(-1.8F)
							.scale(0.1F)
			));
	public static final RegistryObject<Biome> twilightForest = BIOMES.register("twilight_forest", () ->
			new TFBiomeBase(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
			));
	public static final RegistryObject<Biome> denseTwilightForest = BIOMES.register("dense_twilight_forest", () ->
			new TFBiomeTwilightForestVariant(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.waterColor(0x005522)
							.temperature(0.7F)
							.downfall(0.8F)
							.depth(0.2F)
							.scale(0.2F)
			));
	public static final RegistryObject<Biome> highlands = BIOMES.register("twilight_highlands", () ->
			new TFBiomeHighlands(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.HIGHLANDS.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.4F)
							.downfall(0.7F)
							.depth(3.5F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> mushrooms = BIOMES.register("mushroom_forest", () ->
			new TFBiomeMushrooms(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.8F)
							.downfall(0.8F)
			));
	public static final RegistryObject<Biome> tfSwamp = BIOMES.register("twilight_swamp", () ->
			new TFBiomeSwamp(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.8F)
							.downfall(0.9F)
							.depth(-0.125F)
							.scale(0.125F)
							.waterColor(0xE0FFAE)
			));
	public static final RegistryObject<Biome> stream = BIOMES.register("twilight_stream", () ->
			new TFBiomeStream(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.5F)
							.downfall(0.1F)
							.depth(-0.5F)
							.scale(0)
			));
	public static final RegistryObject<Biome> snowy_forest = BIOMES.register("snowy_forest", () ->
			new TFBiomeSnow(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.09F)
							.downfall(0.9F)
							.depth(0.2F)
							.scale(0.2F)
			));
	public static final RegistryObject<Biome> glacier = BIOMES.register("twilight_glacier", () ->
			new TFBiomeGlacier(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0)
							.downfall(0.1F)
							.precipitation(Biome.RainType.SNOW)
			));
	public static final RegistryObject<Biome> clearing = BIOMES.register("twilight_clearing", () ->
			new TFBiomeClearing(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.8F)
							.downfall(0.4F)
							.depth(0.125F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> oakSavanna = BIOMES.register("oak_savannah", () ->
			new TFBiomeOakSavanna(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.9F)
							.downfall(0)
							.depth(0.2F)
							.scale(0.2F)
			));
	public static final RegistryObject<Biome> fireflyForest = BIOMES.register("firefly_forest", () ->
			new TFBiomeFireflyForest(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.5F)
							.downfall(1)
							.depth(0.125F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> deepMushrooms = BIOMES.register("deep_mushroom_forest", () ->
			new TFBiomeDeepMushrooms(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.8F)
							.downfall(1)
							.depth(0.125F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> darkForest = BIOMES.register("dark_forest", () ->
			new TFBiomeDarkForest(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.7F)
							.downfall(0.8F)
							.depth(0.125F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> enchantedForest = BIOMES.register("enchanted_forest", () ->
			new TFBiomeEnchantedForest(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
			));
	public static final RegistryObject<Biome> fireSwamp = BIOMES.register("fire_swamp", () ->
			new TFBiomeFireSwamp(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(1)
							.downfall(0.4F)
							.waterColor(0x6C2C2C)
							.depth(0.1F)
							.scale(0.2F)
			));
	public static final RegistryObject<Biome> darkForestCenter = BIOMES.register("dark_forest_center", () ->
			new TFBiomeDarkForestCenter(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.depth(0.125F)
							.scale(0.05F)
			));
	public static final RegistryObject<Biome> highlandsCenter = BIOMES.register("highlands_center", () ->
			new TFBiomeFinalPlateau(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.PLATEAU.get(), TFSurfaceBuilders.FINAL_PLATEAU)
							.temperature(0.3F)
							.downfall(0.2F)
							.depth(10.5F)
							.scale(0.025F)
			));
	public static final RegistryObject<Biome> thornlands = BIOMES.register("thornlands", () ->
			new TFBiomeThornlands(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.PLATEAU.get(), TFSurfaceBuilders.FINAL_PLATEAU)
							.temperature(0.3F)
							.downfall(0.2F)
							.depth(6)
							.scale(0.1F)
			));
	public static final RegistryObject<Biome> spookyForest = BIOMES.register("spooky_forest", () ->
			new TFBiomeSpookyForest(
					new Biome.Builder()
							.surfaceBuilder(TFSurfaceBuilders.DEFAULT_TF.get(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
							.temperature(0.5F)
							.downfall(1)
							.depth(0.125F)
							.scale(0.05F)
							.waterColor(0XFA9111)
			));

	public static final BiomeDictionary.Type TWILIGHT = BiomeDictionary.Type.getType("TWILIGHT");

	public static void addBiomeTypes() {
		BiomeDictionary.addTypes(tfLake.get(), TWILIGHT, BiomeDictionary.Type.OCEAN);
		BiomeDictionary.addTypes(twilightForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST);
		BiomeDictionary.addTypes(denseTwilightForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
		BiomeDictionary.addTypes(highlands.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.CONIFEROUS);
		BiomeDictionary.addTypes(mushrooms.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM);
		BiomeDictionary.addTypes(tfSwamp.get(), TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
		BiomeDictionary.addTypes(stream.get(), TWILIGHT, BiomeDictionary.Type.RIVER);
		BiomeDictionary.addTypes(snowy_forest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
		BiomeDictionary.addTypes(glacier.get(), TWILIGHT, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.WASTELAND);
		BiomeDictionary.addTypes(clearing.get(), TWILIGHT, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.addTypes(oakSavanna.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.addTypes(fireflyForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.LUSH);
		BiomeDictionary.addTypes(deepMushrooms.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM);
		BiomeDictionary.addTypes(darkForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
		BiomeDictionary.addTypes(enchantedForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL);
		BiomeDictionary.addTypes(fireSwamp.get(), TWILIGHT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.HOT);
		BiomeDictionary.addTypes(darkForestCenter.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.MAGICAL);
		BiomeDictionary.addTypes(highlandsCenter.get(), TWILIGHT, BiomeDictionary.Type.MESA, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND);
		BiomeDictionary.addTypes(thornlands.get(), TWILIGHT, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND);
		BiomeDictionary.addTypes(spookyForest.get(), TWILIGHT, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
	}
}
