package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class KnightStonesRandomBlockSelectorFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.CRACKED_UNDERBRICK.get().defaultBlockState(), 0.2F),
				Pair.of(TFBlocks.MOSSY_UNDERBRICK.get().defaultBlockState(), 0.3F),
				Pair.of(TFBlocks.UNDERBRICK.get().defaultBlockState(), 0.5F)
			)
		);
	}
}