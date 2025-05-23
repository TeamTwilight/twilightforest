package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class CastleRandomBlockSelectorFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.WORN_CASTLE_BRICK.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.CRACKED_CASTLE_BRICK.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.CASTLE_BRICK.get().defaultBlockState(), 0.8F)
			)
		);
	}
}