package twilightforest.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.TFStructures;

import java.util.Map;
import java.util.Optional;

public class LegacyLandmarkPlacements {
    private static final Map<ResourceKey<Biome>, ResourceKey<Structure>> BIOME_2_STRUCTURES = new ImmutableMap.Builder<ResourceKey<Biome>, ResourceKey<Structure>>()
            //.put(TFBiomes.DENSE_MUSHROOM_FOREST.location(), MUSHROOM_TOWER)
            .put(TFBiomes.ENCHANTED_FOREST, TFStructures.QUEST_GROVE)
            .put(TFBiomes.LAKE, TFStructures.QUEST_ISLAND)
            .put(TFBiomes.SWAMP, TFStructures.LABYRINTH)
            .put(TFBiomes.FIRE_SWAMP, TFStructures.HYDRA_LAIR)
            .put(TFBiomes.DARK_FOREST, TFStructures.KNIGHT_STRONGHOLD)
            .put(TFBiomes.DARK_FOREST_CENTER, TFStructures.DARK_TOWER)
            .put(TFBiomes.SNOWY_FOREST, TFStructures.YETI_CAVE)
            .put(TFBiomes.GLACIER, TFStructures.AURORA_PALACE)
            .put(TFBiomes.HIGHLANDS, TFStructures.TROLL_CAVE)
            .put(TFBiomes.FINAL_PLATEAU, TFStructures.FINAL_CASTLE)
            .build();

    public static final SimpleWeightedRandomList<ResourceKey<Structure>> VARIETY_LANDMARKS = Util.make(() -> {
        SimpleWeightedRandomList.Builder<ResourceKey<Structure>> varietyLandmarks = new SimpleWeightedRandomList.Builder<>();

        varietyLandmarks.add(TFStructures.HOLLOW_HILL_SMALL, 6);
        varietyLandmarks.add(TFStructures.HOLLOW_HILL_MEDIUM, 3);
        varietyLandmarks.add(TFStructures.HOLLOW_HILL_LARGE, 1);
        varietyLandmarks.add(TFStructures.HEDGE_MAZE, 2);
        varietyLandmarks.add(TFStructures.NAGA_COURTYARD, 2);
        varietyLandmarks.add(TFStructures.LICH_TOWER, 2);

        return varietyLandmarks.build();
    });

	public static boolean blockNearLandmarkCenter(int blockX, int blockZ, int range) {
		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				if (LegacyLandmarkPlacements.chunkHasLandmarkCenter(blockX >> 4 + x, blockZ >> 4 + z))
					return true;
			}
		}
		return false;
	}

    public static boolean blockIsInLandmarkCenter(int blockX, int blockZ) {
        return chunkHasLandmarkCenter(blockX >> 4, blockZ >> 4);
    }

    public static boolean chunkHasLandmarkCenter(int chunkX, int chunkZ) {
        BlockPos nearestCenter = getNearestCenterXZ(chunkX, chunkZ);

        return chunkX == nearestCenter.getX() >> 4 && chunkZ == nearestCenter.getZ() >> 4;
    }

    public static int manhattanDistanceFromLandmarkCenter(int chunkX, int chunkZ) {
        BlockPos nearestCenter = getNearestCenterXZ(chunkX, chunkZ);

        int deltaChunkX = Math.abs(chunkX - (nearestCenter.getX() >> 4));
        int deltaChunkZ = Math.abs(chunkZ - (nearestCenter.getZ() >> 4));

        return deltaChunkX + deltaChunkZ;
    }

    public static ResourceKey<Structure> pickLandmarkAtBlock(int blockX, int blockZ, LevelReader world) {
        return pickLandmarkForChunk(blockX >> 4, blockZ >> 4, world);
    }

    /**
     * What feature would go near this chunk.
     * @return The feature in the chunk "region"
     */
    public static ResourceKey<Structure> pickLandmarkForChunk(int chunkX, int chunkZ, LevelReader world) {
        // set the chunkX and chunkZ to the center of the biome
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);

        // what biome is at the center of the chunk?
        Optional<ResourceKey<Biome>> biomeResourceKey = world.getBiome(pos).unwrapKey();

        if (biomeResourceKey.isPresent()) {
            ResourceKey<Structure> biomeFeature = BIOME_2_STRUCTURES.get(biomeResourceKey.get());

            if(biomeFeature != null)
                return biomeFeature;
        }

        return pickVarietyLandmark(chunkX, chunkZ);
    }

    public static ResourceKey<Structure> pickVarietyLandmark(int chunkX, int chunkZ) {
        // set the chunkX and chunkZ to the center of the biome in case they arent already
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
        int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);

        // plant two lich towers near the center of each 2048x2048 map area
        if ((regionOffsetX == 4 && regionOffsetZ == 5) || (regionOffsetX == 4 && regionOffsetZ == 3)) {
            return TFStructures.LICH_TOWER;
        }

        // also two nagas
        if ((regionOffsetX == 5 && regionOffsetZ == 4) || (regionOffsetX == 3 && regionOffsetZ == 4)) {
            return TFStructures.NAGA_COURTYARD;
        }

        // okay, well that takes care of most special cases
        return VARIETY_LANDMARKS
                .getRandomValue(new LegacyRandomSource(TFDimensionSettings.seed + chunkX * 25117L + chunkZ * 151121L))
                .orElse(TFStructures.HOLLOW_HILL_SMALL);
    }

    public static XZQuadrantIterator<BlockPos> landmarkCenterScanner(BlockPos searchFocus, int gridSearchRadius) {
        return new XZQuadrantIterator<>((searchFocus.getX() >> 4) & ~0b1111, (searchFocus.getZ() >> 4) & ~0b1111, false, gridSearchRadius, 16, LegacyLandmarkPlacements::getNearestCenterXZ);
    }

    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXZ(int chunkX, int chunkZ) {
        return getNearestCenterXZ(chunkX, chunkZ, 0);
    }

    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXZ(int chunkX, int chunkZ, int height) {
        // generate random number for the whole biome area
        int regionX = (chunkX + 8) >> 4;
        int regionZ = (chunkZ + 8) >> 4;

        long seed = regionX * 3129871L ^ regionZ * 116129781L;
        seed = seed * seed * 42317861L + seed * 7L;

        int num0 = (int) (seed >> 12 & 3L);
        int num1 = (int) (seed >> 15 & 3L);
        int num2 = (int) (seed >> 18 & 3L);
        int num3 = (int) (seed >> 21 & 3L);

        // slightly randomize center of biome (+/- 3)
        int centerX = 8 + num0 - num1;
        int centerZ = 8 + num2 - num3;

        // centers are offset strangely depending on +/-
        int ccz;
        if (regionZ >= 0) {
            ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
        } else {
            ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
        }

        int ccx;
        if (regionX >= 0) {
            ccx = (regionX * 16 + centerX - 8) * 16 + 8;
        } else {
            ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
        }

        return new BlockPos(ccx, height, ccz);
    }
}
