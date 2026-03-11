package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FiddleheadBlock extends TFPlantBlock {

	public static final MapCodec<FiddleheadBlock> CODEC = simpleCodec(FiddleheadBlock::new);
	private static final VoxelShape FIDDLEHEAD_SHAPE = Block.box(3, 0, 3, 13, 14, 13);

	public FiddleheadBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return FIDDLEHEAD_SHAPE;
	}
}
