package twilightforest.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;

@EventBusSubscriber
public final class RegistryBiomeEvent {
	@SuppressWarnings("OverlyCoupledMethod")
	@SubscribeEvent
	public static void onRegisterBiomes(Register<Biome> event) {
		final BiomeRegistry biomes = new BiomeRegistry(event.getRegistry());

		biomes.register(
				"twilight_lake",
				new TFBiomeTwilightLake(
						new BiomeProperties("Twilight Lake")
								.setTemperature(0.66F)
								.setRainfall(1)
								.setBaseHeight(-1.8F)
								.setHeightVariation(0.1F)
				),
				Type.OCEAN
		);

		biomes.register(
				"twilight_forest",
				new TFBiomeBase(
						new BiomeProperties("Twilight Forest")
				),
				Type.FOREST
		);

		biomes.register(
				"dense_twilight_forest",
				new TFBiomeTwilightForestVariant(
						new BiomeProperties("Dense Twilight Forest")
								.setWaterColor(0x005522)
								.setTemperature(0.7F)
								.setRainfall(0.8F)
								.setBaseHeight(0.2F)
								.setHeightVariation(0.2F)
				),
				Type.FOREST, Type.DENSE
		);

		biomes.register(
				"twilight_highlands",
				new TFBiomeHighlands(
						new BiomeProperties("Twilight Highlands")
								.setTemperature(0.4F)
								.setRainfall(0.7F)
								.setBaseHeight(3.5F)
								.setHeightVariation(0.05F)
				),
				Type.FOREST, Type.MOUNTAIN, Type.CONIFEROUS
		);

		biomes.register(
				"mushroom_forest",
				new TFBiomeMushrooms(
						new BiomeProperties("Mushroom Forest")
								.setTemperature(0.8F)
								.setRainfall(0.8F)
				),
				Type.FOREST, Type.MUSHROOM
		);

		biomes.register(
				"twilight_swamp",
				new TFBiomeSwamp(
						new BiomeProperties("Twilight Swamp")
								.setTemperature(0.8F)
								.setRainfall(0.9F)
								.setBaseHeight(-0.125F)
								.setHeightVariation(0.125F)
								.setWaterColor(0xE0FFAE)
				),
				Type.SWAMP, Type.WET
		);

		biomes.register(
				"twilight_stream",
				new TFBiomeStream(
						new BiomeProperties("Twilight Stream")
								.setTemperature(0.5F)
								.setRainfall(0.1F)
								.setBaseHeight(-0.5F)
								.setHeightVariation(0)
				),
				Type.RIVER
		);

		biomes.register(
				"snowy_forest",
				new TFBiomeSnow(
						new BiomeProperties("Snowy Forest")
								.setTemperature(0.09F)
								.setRainfall(0.9F)
								.setBaseHeight(0.2F)
								.setHeightVariation(0.2F)
				),
				Type.FOREST, Type.SNOWY, Type.COLD, Type.CONIFEROUS
		);

		biomes.register(
				"twilight_glacier",
				new TFBiomeGlacier(
						new BiomeProperties("Twilight Glacier")
								.setTemperature(0)
								.setRainfall(0.1F)
				),
				Type.COLD, Type.SNOWY, Type.WASTELAND
		);

		biomes.register(
				"twilight_clearing",
				new TFBiomeClearing(
						new BiomeProperties("Twilight Clearing")
								.setTemperature(0.8F)
								.setRainfall(0.4F)
								.setBaseHeight(0.125F)
								.setHeightVariation(0.05F)
				),
				Type.PLAINS, Type.SPARSE
		);

		biomes.register(
				"oak_savannah",
				new TFBiomeOakSavanna(
						new BiomeProperties("Oak Savanna")
								.setTemperature(0.9F)
								.setRainfall(0)
								.setBaseHeight(0.2F)
								.setHeightVariation(0.2F)
				),
				Type.FOREST, Type.SPARSE
		);

		biomes.register(
				"firefly_forest",
				new TFBiomeFireflyForest(
						new BiomeProperties("Firefly Forest")
								.setTemperature(0.5F)
								.setRainfall(1)
								.setBaseHeight(0.125F)
								.setHeightVariation(0.05F)
				),
				Type.FOREST, Type.LUSH
		);

		biomes.register(
				"deep_mushroom_forest",
				new TFBiomeDeepMushrooms(
						new BiomeProperties("Deep Mushroom Forest")
								.setTemperature(0.8F)
								.setRainfall(1)
								.setBaseHeight(0.125F)
								.setHeightVariation(0.05F)
				),
				Type.FOREST, Type.MUSHROOM
		);

		biomes.register(
				"dark_forest",
				new TFBiomeDarkForest(
						new BiomeProperties("Dark Forest")
								.setTemperature(0.7F)
								.setRainfall(0.8F)
								.setBaseHeight(0.125F)
								.setHeightVariation(0.05F)
				),
				Type.FOREST, Type.DENSE, Type.SPOOKY
		);

		biomes.register(
				"enchanted_forest",
				new TFBiomeEnchantedForest(
						new BiomeProperties("Enchanted Forest")
				),
				Type.FOREST, Type.MAGICAL
		);

		biomes.register(
				"fire_swamp",
				new TFBiomeFireSwamp(
						new BiomeProperties("Fire Swamp")
								.setTemperature(1)
								.setRainfall(0.4F)
								.setWaterColor(0x6C2C2C)
								.setBaseHeight(0.1F)
								.setHeightVariation(0.2F)
				),
				Type.SWAMP, Type.WASTELAND, Type.HOT
		);

		biomes.register(
				"dark_forest_center",
				new TFBiomeDarkForestCenter(
						new BiomeProperties("Dark Forest Center")
								.setBaseHeight(0.125F)
								.setHeightVariation(0.05F)
				),
				Type.FOREST, Type.DENSE, Type.SPOOKY, Type.MAGICAL
		);

		biomes.register(
				"highlands_center",
				new TFBiomeFinalPlateau(
						new BiomeProperties("Highlands Center")
								.setTemperature(0.3F)
								.setRainfall(0.2F)
								.setBaseHeight(10.5F)
								.setHeightVariation(0.025F)
				),
				Type.MESA, Type.DEAD, Type.DRY, Type.WASTELAND
		);

		biomes.register(
				"thornlands",
				new TFBiomeThornlands(
						new BiomeProperties("Thornlands")
								.setTemperature(0.3F)
								.setRainfall(0.2F)
								.setBaseHeight(6)
								.setHeightVariation(0.1F)
				),
				Type.HILLS, Type.DEAD, Type.DRY, Type.WASTELAND
		);
	}

	private static class BiomeRegistry {

		private final IForgeRegistry<Biome> registry;

		BiomeRegistry(IForgeRegistry<Biome> registry) {

			this.registry = registry;
		}

		public void register(String registryName, Biome biome, Type... biomeTypes) {
			biome.setRegistryName(TwilightForestMod.ID, registryName);
			registry.register(biome);
			BiomeDictionary.addTypes(biome, biomeTypes);
		}
	}
}
