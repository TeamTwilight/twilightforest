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
import twilightforest.init.TFBiomes;
import twilightforest.world.components.chunkgenerators.TerrainColumn;

import java.util.List;
import java.util.function.Consumer;

public final class BiomeMaker extends BiomeHelper {
	public static List<TerrainColumn> makeBiomeList(HolderGetter<Biome> biomeRegistry, Holder<Biome> undergroundBiome) {
		return List.of(
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.1F, 0.2F, biomeRegistry, TFBiomes.DENSE_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.0625F, 0.05F, biomeRegistry, TFBiomes.FIREFLY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.005F, 0.005F, biomeRegistry, TFBiomes.CLEARING, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.1F, biomeRegistry, TFBiomes.OAK_SAVANNAH, undergroundBiome),
				biomeColumnWithUnderground(-0.77F, 0.1F, biomeRegistry, TFBiomes.STREAM, undergroundBiome),
				biomeColumnWithUnderground(-1.9998, 0.001F, biomeRegistry, TFBiomes.LAKE, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.MUSHROOM_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.05F, biomeRegistry, TFBiomes.DENSE_MUSHROOM_FOREST, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.ENCHANTED_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.SPOOKY_FOREST, undergroundBiome),

				biomeColumnWithUnderground(-0.3, 0.15F, biomeRegistry, TFBiomes.SWAMP, undergroundBiome),
				biomeColumnWithUnderground(0.3, 0.1F, biomeRegistry, TFBiomes.FIRE_SWAMP, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, TFBiomes.DARK_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, TFBiomes.DARK_FOREST_CENTER, undergroundBiome),

				biomeColumnWithUnderground(0.05F, 0.15F, biomeRegistry, TFBiomes.SNOWY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.GLACIER, undergroundBiome),

				biomeColumnWithUnderground(3, 0.25F, biomeRegistry, TFBiomes.HIGHLANDS, biomeRegistry.getOrThrow(TFBiomes.HIGHLANDS_UNDERGROUND)),
				biomeColumnToBedrock(5.5, 0.1F, biomeRegistry, TFBiomes.THORNLANDS),
				biomeColumnToBedrock(12, 0.025F, biomeRegistry, TFBiomes.FINAL_PLATEAU)
		);
	}

	private static TerrainColumn biomeColumnWithUnderground(double noiseDepth, double noiseScale, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key, Holder<Biome> undergroundBiome) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), biomeHolder, treeMap -> {
			// This will put the transition boundary around Y-8
			treeMap.put(Math.min(noiseDepth - 1, -1), biomeHolder);
			treeMap.put(Math.min(noiseDepth - 3, -3), undergroundBiome);
		});
	}

	private static TerrainColumn biomeColumnToBedrock(double noiseDepth, double noiseScale, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(DensityFunctions.constant(noiseDepth), DensityFunctions.constant(noiseScale), biomeHolder, treeMap -> treeMap.put(0, biomeHolder));
	}

	private static TerrainColumn makeColumn(DensityFunction noiseDepth, DensityFunction noiseScale, Holder<Biome> biomeHolder, Consumer<Double2ObjectSortedMap<Holder<Biome>>> layerBuilder) {
		return new TerrainColumn(biomeHolder, Util.make(new Double2ObjectAVLTreeMap<>(), layerBuilder), noiseDepth, noiseScale);
	}
}
