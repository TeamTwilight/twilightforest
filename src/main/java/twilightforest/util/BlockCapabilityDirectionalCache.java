package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Caches sided block capabilities for loaded block entities, separated by level.
 *
 * <p>Entries within a level are weakly keyed by the target block entity rather than globally keyed by position.
 * This keeps identical positions in different levels separate and allows the directional caches to be collected
 * after NeoForge invalidates them on a normal block or chunk unload.</p>
 *
 * <p>{@link BlockCapabilityCache} retains its {@link ServerLevel}, and level unload does not fire the usual chunk
 * invalidations. Call {@link #clear(ServerLevel)} from {@code LevelEvent.Unload} to release the level explicitly.</p>
 */
public final class BlockCapabilityDirectionalCache<R> {

	private final BlockCapability<R, @Nullable Direction> capability;
	private final Map<ServerLevel, Map<BlockEntity, EnumMap<Direction, BlockCapabilityCache<R, @Nullable Direction>>>> data = new IdentityHashMap<>();

	public BlockCapabilityDirectionalCache(BlockCapability<R, @Nullable Direction> capability) {
		this.capability = capability;
	}

	@Nullable
	public R get(ServerLevel level, BlockEntity blockEntity, Direction direction) {
		Map<BlockEntity, EnumMap<Direction, BlockCapabilityCache<R, @Nullable Direction>>> levelCaches = this.data.computeIfAbsent(
			level,
			ignored -> new WeakHashMap<>()
		);
		EnumMap<Direction, BlockCapabilityCache<R, @Nullable Direction>> directionalCaches = levelCaches.computeIfAbsent(
			blockEntity,
			ignored -> new EnumMap<>(Direction.class)
		);
		BlockPos blockPos = blockEntity.getBlockPos();
		BlockCapabilityCache<R, @Nullable Direction> cache = directionalCaches.get(direction);
		if (cache == null || cache.level() != level || !cache.pos().equals(blockPos)) {
			cache = BlockCapabilityCache.create(this.capability, level, blockPos, direction);
			directionalCaches.put(direction, cache);
		}
		return cache.getCapability();
	}

	public void clear(ServerLevel level) {
		this.data.remove(level);
	}
}
