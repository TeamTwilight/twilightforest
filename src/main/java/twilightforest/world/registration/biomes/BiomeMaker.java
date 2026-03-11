package twilightforest.world.registration.biomes;

import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.Unmodifiable;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.chunkgenerators.TerrainColumn;

import java.util.List;
import java.util.function.Consumer;

public final class BiomeMaker extends BiomeHelper {
	public static @Unmodifiable List<TerrainColumn> makeBiomeList(HolderGetter<Biome> biomeRegistry, Holder<Biome> undergroundBiome) {
		return List.of(
			biomeColumnWithUnderground(-0.7D, 4.20D, 1.0D, biomeRegistry, TFBiomes.FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.8D, 3.9D, 1.0D, biomeRegistry, TFBiomes.DENSE_FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.75D, 4.15D, 1.0D, biomeRegistry, TFBiomes.FIREFLY_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.05D, 1.15D, 1.0D, biomeRegistry, TFBiomes.CLEARING, undergroundBiome),
			biomeColumnWithUnderground(-0.05D, 2.0D, 1.0D, biomeRegistry, TFBiomes.OAK_SAVANNAH, undergroundBiome),
			biomeColumnWithUnderground(-0.1D, 0.001D, 1.35D, biomeRegistry, TFBiomes.STREAM, undergroundBiome),
			biomeColumnWithUnderground(-2.2D, 2.0D, 1.0D, biomeRegistry, TFBiomes.LAKE, undergroundBiome),

			biomeColumnWithUnderground(0.0D, 2.0D, 1.0D, biomeRegistry, TFBiomes.MUSHROOM_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.0D, 1.75D, 1.0D, biomeRegistry, TFBiomes.DENSE_MUSHROOM_FOREST, undergroundBiome),

			biomeColumnWithUnderground(-0.5D, 4.0D, 1.0D, biomeRegistry, TFBiomes.ENCHANTED_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.0D, 2.25D, 1.0D, biomeRegistry, TFBiomes.SPOOKY_FOREST, undergroundBiome),

			biomeColumnWithUnderground(-0.6D, 1.7D, 1.0D, biomeRegistry, TFBiomes.SWAMP, undergroundBiome),
			biomeColumnWithUnderground(0.2D, 1.25D, 1.0D, biomeRegistry, TFBiomes.FIRE_SWAMP, undergroundBiome),

			biomeColumnWithUnderground(0.1D, 1.25D, 0.5D, biomeRegistry, TFBiomes.DARK_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.1D, 1.125D, 1.0D, biomeRegistry, TFBiomes.DARK_FOREST_CENTER, undergroundBiome),

			biomeColumnWithUnderground(0.0D, 2.45D, 0.5D, biomeRegistry, TFBiomes.SNOWY_FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.05D, 1.75D, 1.0D, biomeRegistry, TFBiomes.GLACIER, undergroundBiome),

			biomeColumnWithUnderground(3.0D, 2.25D, 0.135D, biomeRegistry, TFBiomes.HIGHLANDS, biomeRegistry.getOrThrow(TFBiomes.HIGHLANDS_UNDERGROUND)),
			biomeColumnToBedrock(5.5D, 1.75D, 1.15D, biomeRegistry, TFBiomes.THORNLANDS),
			biomeColumnToBedrock(12.0D, 0.75D, 1.0D, biomeRegistry, TFBiomes.FINAL_PLATEAU)
		);
	}

	private static TerrainColumn biomeColumnWithUnderground(double noiseDepth, double noiseScale, double weight, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key, Holder<Biome> undergroundBiome) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), DensityFunctions.constant(weight), biomeHolder, treeMap -> {
			// This will put the transition boundary around Y-8
			treeMap.put(Math.min(noiseDepth - 1, -1), biomeHolder);
			treeMap.put(Math.min(noiseDepth - 3, -3), undergroundBiome);
		});
	}

	private static TerrainColumn biomeColumnToBedrock(double noiseDepth, double noiseScale, double weight, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), DensityFunctions.constant(weight), biomeHolder, treeMap -> treeMap.put(0, biomeHolder));
	}

	private static TerrainColumn makeColumn(DensityFunction noiseDepth, DensityFunction noiseScale, DensityFunction noiseWeight, Holder<Biome> biomeHolder, Consumer<Double2ObjectSortedMap<Holder<Biome>>> layerBuilder) {
		return new TerrainColumn(biomeHolder, Util.make(new Double2ObjectAVLTreeMap<>(), layerBuilder), noiseDepth, noiseScale, noiseWeight);
	}
}
