package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.MoonwormBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.loot.TFLootTables;

public class MoonwormBlock extends CritterBlock implements SimpleWaterloggedBlock {

	public static final MapCodec<MoonwormBlock> CODEC = simpleCodec(MoonwormBlock::new);

	public MoonwormBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MoonwormBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.MOONWORM.get(), MoonwormBlockEntity::tick);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(WATERLOGGED));
	}

	@Override
	public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
		return true;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		//allow waterlogging but allow for fluids to destroy moonworms if it flows into them
		if (fluidState.isSource()) {
			return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
		} else {
			if (!state.isAir() && fluidState.getType() instanceof FlowingFluid fluid) {
				//would have been lovely if FlowingFluid.beforeDestroyingBlock wasnt protected...
				if (fluid instanceof WaterFluid) {
					BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
					Block.dropResources(state, level, pos, blockentity);
				} else if (fluid instanceof LavaFluid) {
					level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
				}
			}

			level.setBlock(pos, fluidState.createLegacyBlock(), 3);
			return false;
		}
	}

	@Override
	public ResourceKey<LootTable> getSquishLootTable() {
		return TFLootTables.MOONWORM_SQUISH_DROPS;
	}
}
