package twilightforest.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on {net.minecraft.world.biome.BiomeCache}.
 * This version allows specifying the grid size, and uses the alternate biome
 * layer used in world generation. Used for magic maps, and supports a centred
 * grid for this purpose.
 */
public class TFBiomeCache {

	private final Long2ObjectMap<Entry> entryMap = new Long2ObjectOpenHashMap<>();
	private final List<Entry> entries = new ArrayList<>();
	private final TFBiomeProvider provider;
	private final int gridSize;
	private final boolean offset;
	private long lastCleanupTime;

	public TFBiomeCache(TFBiomeProvider provider, int gridSize, boolean offset) {
		this.provider = provider;
		this.gridSize = gridSize;
		this.offset = offset;
	}

	private final class Entry {

		final Biome[] biomes = new Biome[gridSize * gridSize];
		final int x, z;
		long lastAccessTime;

		Entry(int x, int z) {
			this.x = x;
			this.z = z;
//			provider.getBiomesForGeneration(biomes, fromGrid(x), fromGrid(z), gridSize, gridSize, false);
		}
	}

	private Entry getEntry(int x, int z) {

		x = toGrid(x);
		z = toGrid(z);

		long key = getKey(x, z);
		Entry entry = this.entryMap.get(key);

		if (entry == null) {

			entry = new Entry(x, z);
			this.entryMap.put(key, entry);
			this.entries.add(entry);
		}

		entry.lastAccessTime = Util.milliTime();
		return entry;
	}

	public Biome[] getBiomes(int x, int z) {
		return getEntry(x, z).biomes;
	}

	public void cleanup() {

		long currentTime = Util.milliTime();
		long timeSinceCleanup = currentTime - this.lastCleanupTime;

		if (timeSinceCleanup > 7500L || timeSinceCleanup < 0L) {

			this.lastCleanupTime = currentTime;

			for (int i = 0; i < this.entries.size(); ++i) {

				Entry entry = this.entries.get(i);
				long timeSinceAccess = currentTime - entry.lastAccessTime;

				if (timeSinceAccess > 30000L || timeSinceAccess < 0L) {

					this.entries.remove(i--);
					long key = getKey(entry.x, entry.z);
					this.entryMap.remove(key);
				}
			}
		}
	}

	public boolean isGridAligned(int x, int z, int width, int height) {
		return width == gridSize && height == gridSize && gridOffset(x) == 0 && gridOffset(z) == 0;
	}

	private int gridOffset(int n) {
		return (n + (offset ? gridSize / 2 : 0)) % gridSize;
	}

	private int toGrid(int n) {
		return (n + (offset ? gridSize / 2 : 0)) / gridSize;
	}

	private int fromGrid(int n) {
		return n * gridSize - (offset ? gridSize / 2 : 0);
	}

	private static long getKey(int x, int z) {
		return Integer.toUnsignedLong(x) | Integer.toUnsignedLong(z) << 32;
	}
}
