package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class GiantBlock extends Block {

	private boolean isSelfDestructing;

	public GiantBlock(Properties properties) {
		super(properties);
	}

	public static Iterable<BlockPos> getVolume(BlockPos pos) {
		return BlockPos.betweenClosed(
			pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11,
			pos.getX() | 0b11, pos.getY() | 0b11, pos.getZ() | 0b11
		);
	}

	public static int packCoords(BlockPos pos) {
		return packCoords(pos.getX(), pos.getY(), pos.getZ());
	}

	public static int packCoords(int x, int y, int z) {
		int length = 4;
		int bits = 2;

		int packedX = Mth.positiveModulo(x, length);
		int packedY = Mth.positiveModulo(y, length) << bits;
		int packedZ = Mth.positiveModulo(z, length) << (bits + bits);

		return packedX | packedY | packedZ;
	}

	public static BlockPos unpackCoords(int index) {
		int length = 4;
		int bits = 2;

		int unpackedX = Mth.positiveModulo(index, length);
		int unpackedY = Mth.positiveModulo(index >> bits, length);
		int unpackedZ = Mth.positiveModulo(index >> (bits + bits), length);

		return new BlockPos(unpackedX, unpackedY, unpackedZ);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		for (BlockPos dPos : getVolume(context.getClickedPos())) {
			if (!context.getLevel().getBlockState(dPos).canBeReplaced(context)) {
				return null;
			}
		}
		return super.getStateForPlacement(context);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!level.isClientSide()) {
			for (BlockPos dPos : getVolume(pos)) {
				level.setBlockAndUpdate(dPos, this.defaultBlockState());
			}
		}
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
		if (!this.isSelfDestructing && !this.isVolumeFilled(level, pos)) {
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
}
