package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDimensionData;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class WorldUtil {
	private WorldUtil() {
	}

	public static long getOverworldSeed() {
		return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldData().worldGenOptions().seed();
	}

	/**
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllAround(BlockPos center, int range) {
		return BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	/**
	 * Floors both corners of the bounding box to integers
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllInBB(AABB bb) {
		return BlockPos.betweenClosed((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
	}

	public static BlockPos randomOffset(RandomSource random, BlockPos pos, int range) {
		return randomOffset(random, pos, range, range, range);
	}

	public static BlockPos randomOffset(RandomSource random, BlockPos pos, int rx, int ry, int rz) {
		int dx = random.nextInt(rx * 2 + 1) - rx;
		int dy = random.nextInt(ry * 2 + 1) - ry;
		int dz = random.nextInt(rz * 2 + 1) - rz;
		return pos.offset(dx, dy, dz);
	}

	public static <T> T getRandomElement(List<T> list, RandomSource rng) {
		return list.get(rng.nextInt(list.size()));
	}

	public static <T> T getRandomElementWithWeights(List<Pair<T, Integer>> list, RandomSource rng) {
		int totalWeight = list.stream().mapToInt(Pair::getSecond).sum();
		int randomValue = rng.nextInt(totalWeight);

		for (Pair<T, Integer> pair : list) {
			randomValue -= pair.getSecond();
			if (randomValue < 0) {
				return pair.getFirst();
			}
		}
		return getRandomElement(list, rng).getFirst(); // This line should never be reached if input list is valid
	}

	public static int getGeneratorSeaLevel(LevelAccessor level) {
		return level.getChunkSource() instanceof ServerChunkCache chunkSource
			? chunkSource.chunkMap.generator().getSeaLevel()
			: TFDimensionData.SEALEVEL; // Should only ever hit if this method is called on client FIXME Fix causes
	}

	public static Optional<Pair<BlockPos, Holder<Structure>>> findNearestMapLandmark(ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int chunkSearchRadius, boolean skipKnownStructures) {
		ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

		Map<LandmarkGridPlacement, Set<Holder<Structure>>> seekStructures = new Object2ObjectArrayMap<>();

		for (Holder<Structure> holder : targetStructures) {
			for (StructurePlacement structureplacement : state.getPlacementsForStructure(holder)) {
				if (structureplacement instanceof LandmarkGridPlacement landmarkPlacement) {
					seekStructures.computeIfAbsent(landmarkPlacement, v -> new ObjectArraySet<>()).add(holder);
				}
			}
		}

		if (seekStructures.isEmpty()) return Optional.empty();

		double distance = Double.MAX_VALUE;

		@Nullable Pair<BlockPos, Holder<Structure>> nearest = null;
		StructureManager structureManager = level.structureManager();

		for (BlockPos landmarkCenterPosition : LegacyLandmarkPlacements.landmarkCenterScanner(pos, chunkSearchRadius)) {
			for (Map.Entry<LandmarkGridPlacement, Set<Holder<Structure>>> landmarkPlacement : seekStructures.entrySet()) {
				if (!landmarkPlacement.getKey().isStructureChunk(state, landmarkCenterPosition.getX() >> 4, landmarkCenterPosition.getZ() >> 4))
					continue;

				for (Holder<Structure> targetStructure : targetStructures) {
					if (landmarkPlacement.getValue().contains(targetStructure)) {
						Holder<Biome> biome = level.getBiome(landmarkCenterPosition);

						if (targetStructure.value().biomes().contains(biome)) {
							if (skipKnownStructures && structureManager.checkStructurePresence(new ChunkPos(landmarkCenterPosition), targetStructure.value(), landmarkPlacement.getKey(), true) == StructureCheckResult.START_PRESENT)
								break;

							final double newDistance = landmarkCenterPosition.distToLowCornerSqr(pos.getX(), 0, pos.getZ());

							if (newDistance < distance) {
								nearest = new Pair<>(landmarkCenterPosition, targetStructure);
								distance = newDistance;
							}
						}
					}
				}
			}
		}

		return Optional.ofNullable(nearest);
	}
}
