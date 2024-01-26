package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.TFSignBlockEntity;

public class TFSignBlock extends StandingSignBlock {
	public TFSignBlock(WoodType type, Properties properties) {
		super(type, properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFSignBlockEntity(pos, state);
	}
}
