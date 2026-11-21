package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.RedThreadBlockEntity;
import twilightforest.init.TFBlocks;

public class RedThreadBlock extends MultifaceBlock implements EntityBlock {

	public RedThreadBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
		return ctx.getItemInHand().is(TFBlocks.RED_THREAD.asItem());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedThreadBlockEntity(pos, state);
	}
}
