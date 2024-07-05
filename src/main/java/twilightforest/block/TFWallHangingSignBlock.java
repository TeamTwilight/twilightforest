package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.block.entity.TFHangingSignBlockEntity;

public class TFWallHangingSignBlock extends WallHangingSignBlock {
	public TFWallHangingSignBlock(WoodType type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFHangingSignBlockEntity(pos, state);
	}
}
