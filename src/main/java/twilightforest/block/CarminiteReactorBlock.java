package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.CarminiteReactorBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

import java.util.Arrays;

public class CarminiteReactorBlock extends BaseEntityBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final MapCodec<CarminiteReactorBlock> CODEC = simpleCodec(CarminiteReactorBlock::new);

	public CarminiteReactorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	@SuppressWarnings("deprecation")
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (level.isClientSide()) return;

		if (!state.getValue(ACTIVE) && this.isReactorReady(level, pos)) {
			// check if we should fire up the reactor
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
		}
	}

	/**
	 * Check if the reactor has all the specified things around it
	 */
	private boolean isReactorReady(Level level, BlockPos pos) {
		return Arrays.stream(Direction.values())
			.allMatch(e -> level.getBlockState(pos.relative(e)).is(Blocks.REDSTONE_BLOCK));
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!newState.is(state.getBlock())) {
			for (BlockPos offset : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
				BlockState checkState = level.getBlockState(offset);
				if (checkState.is(TFBlocks.FAKE_GOLD) || checkState.is(TFBlocks.FAKE_DIAMOND)) {
					level.destroyBlock(offset, false);
				}
			}
		}
		super.onRemove(state, level, pos, newState, moving);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CarminiteReactorBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CARMINITE_REACTOR.get(), CarminiteReactorBlockEntity::tick);
	}
}
