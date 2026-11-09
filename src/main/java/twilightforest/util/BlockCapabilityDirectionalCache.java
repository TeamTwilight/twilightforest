package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockCapabilityDirectionalCache<R> {

	private final BlockCapability<R, Direction> capability;

	private final Map<BlockPos, Map<Direction, BlockCapabilityCache<R, Direction>>> data = new HashMap<>();

	public BlockCapabilityDirectionalCache(BlockCapability<R, Direction> capability) {
		this.capability = capability;
	}

	public List<R> getAllSides(ServerLevel level, BlockPos pos) {
		Map<Direction, BlockCapabilityCache<R, Direction>> caches = data.get(pos);
		if (caches == null) {
			BlockPos key = pos.immutable();
			caches = new EnumMap<>(Direction.class);
			for (Direction side : Direction.values()) {
				caches.put(side, BlockCapabilityCache.create(capability, level, key, side, () -> true, () -> data.remove(key)));
			}
			data.put(key, caches);
		}

		List<R> handlers = new ArrayList<>();
		for (BlockCapabilityCache<R, Direction> cache : caches.values()) {
			R handler = cache.getCapability();
			if (handler != null) {
				handlers.add(handler);
			}
		}
		return handlers;
	}
}
