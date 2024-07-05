package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MayappleBlock extends TFPlantBlock {

	public static final MapCodec<MayappleBlock> CODEC = simpleCodec(MayappleBlock::new);
	private static final VoxelShape MAYAPPLE_SHAPE = box(4, 0, 4, 13, 6, 13);

	public MayappleBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return MAYAPPLE_SHAPE;
	}
}
