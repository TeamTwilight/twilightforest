package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class TowerwoodRandomBlockSelectorFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.CRACKED_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.MOSSY_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState(), 0.025F),
				Pair.of(TFBlocks.TOWERWOOD.get().defaultBlockState(), 0.775F)
			)
		);
	}
}
