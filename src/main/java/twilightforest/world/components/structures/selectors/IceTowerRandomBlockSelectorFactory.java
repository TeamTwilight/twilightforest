package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class IceTowerRandomBlockSelectorFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.AURORA_BLOCK.get().defaultBlockState(), 1F)
			)
		);
	}
}