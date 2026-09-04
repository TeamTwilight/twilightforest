package twilightforest.world.components;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CarverWallProvider {

	BlockState getState(RandomSource random, BlockPos pos);
}
