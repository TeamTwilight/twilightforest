package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
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
		return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).overworld().getSeed();
	}

	public static RegistryAccess getRegistryAccess() {
		return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).registryAccess();
	}

	public static Difficulty getDifficulty() {
		return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldData().getDifficulty();
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

	// TODO: use vanilla weighted list instead
	public static <T> T getRandomElementWithWeights(List<Pair<T, Float>> list, RandomSource rng) {
		float totalWeight = (float) list.stream().mapToDouble(Pair::getSecond).sum();
		float randomValue = rng.nextFloat() * totalWeight;

		for (Pair<T, Float> pair : list) {
			randomValue -= pair.getSecond();
			if (randomValue < 0) {
				return pair.getFirst();
			}
		}
		return Util.getRandom(list, rng).getFirst(); // This line should never be reached if the input list is valid
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
							if (skipKnownStructures && structureManager.checkStructurePresence(ChunkPos.containing(landmarkCenterPosition), targetStructure.value(), landmarkPlacement.getKey(), true) == StructureCheckResult.START_PRESENT)
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

	public static int adjustForTerrain(Structure.GenerationContext context, int xMin, int zMin, int xMax, int zMax, int gridLength, Heightmap.Types type) {
		int subDivisions = gridLength - 1;
		IntList heights = new IntArrayList(gridLength * gridLength);

		for (int zStep = 0; zStep <= subDivisions; zStep++) {
			int zPos = Mth.lerpDiscrete((float) zStep / subDivisions, zMin, zMax);
			for (int xStep = 0; xStep <= subDivisions; xStep++) {
				int xPos = Mth.lerpDiscrete((float) xStep / subDivisions, xMin, xMax);

				heights.add(context.chunkGenerator().getFirstOccupiedHeight(xPos, zPos, type, context.heightAccessor(), context.randomState()));
			}
		}

		heights.sort(IntComparators.OPPOSITE_COMPARATOR); // sort from highest to lowest

		double weightedSum = 0;
		double totalWeight = 0;
		for (int i = 0; i < heights.size(); i++) {
			double weight = i + 1;
			weightedSum += weight * heights.getInt(i);
			totalWeight += weight;
		}
		return (int) Math.round(weightedSum / totalWeight);
	}

	public static int adjustForTerrain(Structure.GenerationContext context, int xInCenterChunk, int zInCenterChunk, int radiusFromCenterChunk, int gridLength) {
		int chunkOriginX = xInCenterChunk & ~0b1111;
		int chunkOriginZ = zInCenterChunk & ~0b1111;

		return WorldUtil.adjustForTerrain(context, chunkOriginX - radiusFromCenterChunk, chunkOriginZ - radiusFromCenterChunk, chunkOriginX + 15 + radiusFromCenterChunk, chunkOriginZ + 15 + radiusFromCenterChunk, gridLength, Heightmap.Types.WORLD_SURFACE_WG);
	}
}
