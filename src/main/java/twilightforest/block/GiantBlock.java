package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GiantBlock extends Block {

	private boolean isSelfDestructing;

	public GiantBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		for (BlockPos dPos : getVolume(context.getClickedPos())) {
			if (!context.getLevel().getBlockState(dPos).canBeReplaced(context)) {
				return null;
			}
		}
		return defaultBlockState();
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!level.isClientSide()) {
			for (BlockPos dPos : getVolume(pos)) {
				level.setBlockAndUpdate(dPos, defaultBlockState());
			}
		}
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		super.onRemove(state, level, pos, newState, isMoving);
		if (!this.isSelfDestructing && !isVolumeFilled(level, pos)) {
			this.setGiantBlockToAir(level, pos);
		}
	}

	private void setGiantBlockToAir(Level level, BlockPos pos) {
		// prevent mutual infinite recursion
		this.isSelfDestructing = true;

		for (BlockPos iterPos : getVolume(pos)) {
			if (!pos.equals(iterPos) && level.getBlockState(iterPos).getBlock() == this) {
				level.destroyBlock(iterPos, false);
			}
		}

		this.isSelfDestructing = false;
	}

	private boolean isVolumeFilled(Level level, BlockPos pos) {
		for (BlockPos dPos : getVolume(pos)) {
			if (level.getBlockState(dPos).getBlock() != this) {
				return false;
			}
		}
		return true;
	}

	public static Iterable<BlockPos> getVolume(BlockPos pos) {
		return BlockPos.betweenClosed(
				pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11,
				pos.getX() | 0b11, pos.getY() | 0b11, pos.getZ() | 0b11
		);
	}
}
