package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

// Voidscape copy
public class BlockCapabilityDirectionalCache<R> {

	private final Map<BlockPosAndDirection, BlockCapabilityCache<R, Direction>> data = new HashMap<>();

	@Nullable
	public R get(BlockCapability<R, Direction> capability, ServerLevel level, BlockPos pos, Direction direction) {
		BlockCapabilityCache<R, Direction> cache = this.data.get(new BlockPosAndDirection(pos, direction));
		if (cache == null) {
			cache = BlockCapabilityCache.create(capability, level, pos, direction);
			this.data.put(new BlockPosAndDirection(pos, direction), cache);
		}
		return cache.getCapability();
	}

	private record BlockPosAndDirection(BlockPos pos, Direction direction) {

	}
}
