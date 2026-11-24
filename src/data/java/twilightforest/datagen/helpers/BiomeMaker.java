package twilightforest.datagen.helpers;

import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import net.minecraft.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import org.jetbrains.annotations.Unmodifiable;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.chunkgenerators.TerrainColumn;

import java.util.List;
import java.util.function.Consumer;

public final class BiomeMaker extends BiomeHelper {
	public static @Unmodifiable List<TerrainColumn> makeBiomeList(HolderGetter<Biome> biomeRegistry, Holder<Biome> undergroundBiome) {
		return List.of(
			biomeColumnWithUnderground(-0.7f, 4.20f, 1.0f, biomeRegistry, TFBiomes.FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.8f, 3.9f, 1.0f, biomeRegistry, TFBiomes.DENSE_FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.75f, 4.15f, 1.0f, biomeRegistry, TFBiomes.FIREFLY_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.05f, 1.15f, 1.0f, biomeRegistry, TFBiomes.CLEARING, undergroundBiome),
			biomeColumnWithUnderground(-0.05f, 2.0f, 1.0f, biomeRegistry, TFBiomes.OAK_SAVANNAH, undergroundBiome),
			biomeColumnWithUnderground(-0.1f, 0.001f, 1.35f, biomeRegistry, TFBiomes.STREAM, undergroundBiome),
			biomeColumnWithUnderground(-2.2f, 2.0f, 1.0f, biomeRegistry, TFBiomes.LAKE, undergroundBiome),

			biomeColumnWithUnderground(0.0f, 2.0f, 1.0f, biomeRegistry, TFBiomes.MUSHROOM_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.0f, 1.75f, 1.0f, biomeRegistry, TFBiomes.DENSE_MUSHROOM_FOREST, undergroundBiome),

			biomeColumnWithUnderground(-0.5f, 4.0f, 1.0f, biomeRegistry, TFBiomes.ENCHANTED_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.0f, 2.25f, 1.0f, biomeRegistry, TFBiomes.SPOOKY_FOREST, undergroundBiome),

			biomeColumnWithUnderground(-0.6f, 1.7f, 1.0f, biomeRegistry, TFBiomes.SWAMP, undergroundBiome),
			biomeColumnWithUnderground(0.2f, 1.25f, 1.0f, biomeRegistry, TFBiomes.FIRE_SWAMP, undergroundBiome),

			biomeColumnWithUnderground(0.1f, 1.25f, 0.5f, biomeRegistry, TFBiomes.DARK_FOREST, undergroundBiome),
			biomeColumnWithUnderground(0.1f, 1.125f, 1.0f, biomeRegistry, TFBiomes.DARK_FOREST_CENTER, undergroundBiome),

			biomeColumnWithUnderground(0.0f, 2.45f, 0.5f, biomeRegistry, TFBiomes.SNOWY_FOREST, undergroundBiome),
			biomeColumnWithUnderground(-0.05f, 1.75f, 1.0f, biomeRegistry, TFBiomes.GLACIER, undergroundBiome),

			biomeColumnWithUnderground(3.0f, 2.25f, 0.135f, biomeRegistry, TFBiomes.HIGHLANDS, biomeRegistry.getOrThrow(TFBiomes.HIGHLANDS_UNDERGROUND)),
			biomeColumnToBedrock(5.5f, 1.75f, 1.15f, biomeRegistry, TFBiomes.THORNLANDS),
			biomeColumnToBedrock(12.0f, 0.75f, 1.0f, biomeRegistry, TFBiomes.FINAL_PLATEAU)
		);
	}

	private static TerrainColumn biomeColumnWithUnderground(float noiseDepth, float noiseScale, float weight, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key, Holder<Biome> undergroundBiome) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), DensityFunctions.constant(weight), biomeHolder, treeMap -> {
			// This will put the transition boundary around Y-8
			treeMap.put(Math.min(noiseDepth - 1, -1), biomeHolder);
			treeMap.put(Math.min(noiseDepth - 3, -3), undergroundBiome);
		});
	}

	private static TerrainColumn biomeColumnToBedrock(float noiseDepth, float noiseScale, float weight, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), DensityFunctions.constant(weight), biomeHolder, treeMap -> treeMap.put(0, biomeHolder));
	}

	private static TerrainColumn makeColumn(DensityFunction noiseDepth, DensityFunction noiseScale, DensityFunction noiseWeight, Holder<Biome> biomeHolder, Consumer<Double2ObjectSortedMap<Holder<Biome>>> layerBuilder) {
		return new TerrainColumn(biomeHolder, Util.make(new Double2ObjectAVLTreeMap<>(), layerBuilder), noiseDepth, noiseScale, noiseWeight);
	}
}
