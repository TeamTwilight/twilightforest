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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.Map;
import java.util.Set;

public final class WorldUtil {
	private WorldUtil() {}

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

	public static int getGeneratorSeaLevel(LevelAccessor level) {
		return level.getChunkSource() instanceof ServerChunkCache chunkSource
				? chunkSource.chunkMap.generator().getSeaLevel()
				: TFGenerationSettings.SEALEVEL; // TFGenerationSettings.SEALEVEL Should only ever hit if this method is called on client
	}

	@Nullable
	public static Pair<BlockPos, Holder<Structure>> findNearestMapLandmark(ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int chunkSearchRadius, @SuppressWarnings("unused") boolean skipKnownStructures) {
		ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

		Map<LandmarkGridPlacement, Set<Holder<Structure>>> seekStructures = new Object2ObjectArrayMap<>();

		for (Holder<Structure> holder : targetStructures) {
			for (StructurePlacement structureplacement : state.getPlacementsForStructure(holder)) {
				if (structureplacement instanceof LandmarkGridPlacement landmarkPlacement) {
					seekStructures.computeIfAbsent(landmarkPlacement, v -> new ObjectArraySet<>()).add(holder);
				}
			}
		}

		if (seekStructures.isEmpty()) return null;

		double distance = Double.MAX_VALUE;

		@Nullable Pair<BlockPos, Holder<Structure>> nearest = null;

		for (BlockPos landmarkCenterPosition : LegacyLandmarkPlacements.landmarkCenterScanner(pos, chunkSearchRadius)) {
			for (Map.Entry<LandmarkGridPlacement, Set<Holder<Structure>>> landmarkPlacement : seekStructures.entrySet()) {
				if (!landmarkPlacement.getKey().isStructureChunk(state, landmarkCenterPosition.getX() >> 4, landmarkCenterPosition.getZ() >> 4)) continue;

				for (Holder<Structure> targetStructure : targetStructures) {
					if (landmarkPlacement.getValue().contains(targetStructure)) {
                        Holder<Biome> biome = level.getBiome(landmarkCenterPosition);

						if (targetStructure.value().biomes().contains(biome)) {
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

		return nearest;
	}
}
