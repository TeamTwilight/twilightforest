package twilightforest.events;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.block.SortLogCoreBlock;
import twilightforest.init.TFBlocks;

@Component
public final class SortingTreeEvents {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::clearCapabilityCache);
	}

	private void clearCapabilityCache(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			((SortLogCoreBlock) TFBlocks.SORTING_LOG_CORE.get()).clearCapabilityCache(level);
		}
	}
}
