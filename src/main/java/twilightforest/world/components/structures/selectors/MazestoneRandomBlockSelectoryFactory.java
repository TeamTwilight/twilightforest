package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class MazestoneRandomBlockSelectoryFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.MOSSY_MAZESTONE.get().defaultBlockState(), 0.2F),
				Pair.of(TFBlocks.CRACKED_MAZESTONE.get().defaultBlockState(), 0.3F),
				Pair.of(TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), 0.5F)
			)
		);
	}
}