package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.CarminiteBuilderBlockEntity;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import java.util.Objects;

public class BuilderBlock extends BaseEntityBlock {

	public static final EnumProperty<TowerDeviceVariant> STATE = EnumProperty.create("state", TowerDeviceVariant.class);
	public static final MapCodec<BuilderBlock> CODEC = simpleCodec(BuilderBlock::new);

	public BuilderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void activateBuiltBlocks(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() == TFBlocks.BUILT_BLOCK.get() && !state.getValue(TranslucentBuiltBlock.ACTIVE)) {
			level.setBlockAndUpdate(pos, state.setValue(TranslucentBuiltBlock.ACTIVE, true));
			level.playSound(null, pos, TFSounds.BUILDER_REPLACE.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
			level.scheduleTick(pos, state.getBlock(), 10);
		}
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	@SuppressWarnings("deprecation")
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STATE);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide() && state.getValue(STATE) == TowerDeviceVariant.BUILDER_INACTIVE && level.hasNeighborSignal(pos)) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			level.playSound(null, pos, TFSounds.BUILDER_ON.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (level.isClientSide()) {
			return;
		}

		TowerDeviceVariant variant = state.getValue(STATE);

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE && level.hasNeighborSignal(pos)) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			level.playSound(null, pos, TFSounds.BUILDER_ON.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
			level.scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && !level.hasNeighborSignal(pos)) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
			level.playSound(null, pos, TFSounds.BUILDER_OFF.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
			level.scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_TIMEOUT && !level.hasNeighborSignal(pos)) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		TowerDeviceVariant variant = state.getValue(STATE);

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && level.hasNeighborSignal(pos)) {
			this.letsBuild(level, pos);
		}

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE || variant == TowerDeviceVariant.BUILDER_TIMEOUT) {
			((CarminiteBuilderBlockEntity) Objects.requireNonNull(level.getBlockEntity(pos))).resetStats();
			for (Direction e : Direction.values()) {
				activateBuiltBlocks(level, pos.relative(e));
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!newState.is(state.getBlock())) {
			for (Direction e : Direction.values()) {
				activateBuiltBlocks(level, pos.relative(e));
			}
		}
		super.onRemove(state, level, pos, newState, moving);
	}

	private void letsBuild(Level level, BlockPos pos) {
		CarminiteBuilderBlockEntity tileEntity = (CarminiteBuilderBlockEntity) level.getBlockEntity(pos);

		if (tileEntity != null && !tileEntity.makingBlocks) {
			tileEntity.startBuilding();
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(STATE) == TowerDeviceVariant.BUILDER_ACTIVE) {
			this.sparkle(level, pos);
		}
	}

	// [VanillaCopy] RedstoneOreBlock.spawnParticles. Unchanged.
	public void sparkle(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			if (!level.getBlockState(blockpos).isSolidRender()) {
				Direction.Axis axis = direction.getAxis();
				double d1 = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getStepX() : (double) random.nextFloat();
				double d2 = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getStepY() : (double) random.nextFloat();
				double d3 = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getStepZ() : (double) random.nextFloat();
				level.addParticle(DustParticleOptions.REDSTONE, (double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CarminiteBuilderBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return state.getValue(STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? createTickerHelper(type, TFBlockEntities.TOWER_BUILDER.get(), CarminiteBuilderBlockEntity::tick) : null;
	}
}
