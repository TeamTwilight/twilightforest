package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import twilightforest.data.tags.BlockTagGenerator;

public abstract class TFPlantBlock extends BushBlock {

	protected TFPlantBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public static boolean canPlaceRootAt(BlockGetter reader, BlockPos pos) {
		return reader.getBlockState(pos.above()).is(BlockTagGenerator.PLANTS_HANG_ON);
	}
}
