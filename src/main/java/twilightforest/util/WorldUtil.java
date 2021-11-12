package twilightforest.util;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.Random;

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

	public static BlockPos randomOffset(Random random, BlockPos pos, int range) {
		return randomOffset(random, pos, range, range, range);
	}

	public static BlockPos randomOffset(Random random, BlockPos pos, int rx, int ry, int rz) {
		int dx = random.nextInt(rx * 2 + 1) - rx;
		int dy = random.nextInt(ry * 2 + 1) - ry;
		int dz = random.nextInt(rz * 2 + 1) - rz;
		return pos.offset(dx, dy, dz);
	}

	@Nullable
	public static ChunkGeneratorTwilight getChunkGenerator(LevelAccessor level) {
		if (level instanceof ServerLevel serverLevel && serverLevel.getChunkSource().generator instanceof ChunkGeneratorTwilight chunkGenerator)
			return chunkGenerator;

		return null;
	}

	public static int getSeaLevel(Level world) {
		return getSeaLevel(getChunkGenerator(world));
	}

	public static int getSeaLevel(ChunkSource source) {
		return source instanceof ServerChunkCache serverCache ? getSeaLevel(serverCache.generator) : TFGenerationSettings.SEALEVEL;
	}

	public static int getSeaLevel(ChunkGenerator generator) {
		if (generator instanceof ChunkGeneratorTwilight) {
			return generator.getSeaLevel();
		} else return TFGenerationSettings.SEALEVEL;
	}

	public static int getBaseHeight(LevelAccessor level, int x, int z, Heightmap.Types type) {
		if (level.getChunkSource() instanceof ServerChunkCache chunkSource) {
			return chunkSource.generator.getBaseHeight(x, z, type, level);
		} else {
			return level.getHeight(type, x, z);
		}
	}
}
