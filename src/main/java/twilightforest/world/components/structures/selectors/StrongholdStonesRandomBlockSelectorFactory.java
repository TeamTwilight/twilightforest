package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Component;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

@Component
public class StrongholdStonesRandomBlockSelectorFactory {
	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.2F),
				Pair.of(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.3F),
				Pair.of(Blocks.INFESTED_STONE_BRICKS.defaultBlockState(), 0.05F),
				Pair.of(Blocks.STONE_BRICKS.defaultBlockState(), 0.45F)
			)
		);
	}
}