package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FiddleheadBlock extends TFPlantBlock {

	private static final VoxelShape FIDDLEHEAD_SHAPE = Block.box(3, 0, 3, 13, 14, 13);

	public FiddleheadBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return FIDDLEHEAD_SHAPE;
	}
}
