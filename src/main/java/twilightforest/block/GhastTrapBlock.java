package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.GhastTrapBlockEntity;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

public class GhastTrapBlock extends BaseEntityBlock {

	public static final int ACTIVATE_EVENT = 0;
	public static final int DEACTIVATE_EVENT = 1;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final MapCodec<GhastTrapBlock> CODEC = simpleCodec(GhastTrapBlock::new);

	public GhastTrapBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
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
		builder.add(ACTIVE);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (level.isClientSide()) {
			return;
		}

		if (!state.getValue(ACTIVE) && isInactiveTrapCharged(level, pos) && level.hasNeighborSignal(pos)) {
			for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(6.0D))) {
				TFAdvancements.ACTIVATED_GHAST_TRAP.get().trigger(player);
			}

			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			level.playSound(null, pos, TFSounds.JET_START.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
			level.blockEvent(pos, this, ACTIVATE_EVENT, 0);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int event, int id) {
		BlockEntity te = level.getBlockEntity(pos);
		return te != null && te.triggerEvent(event, id);
	}

	/**
	 * Check if the inactive trap block is fully charged
	 */
	private boolean isInactiveTrapCharged(Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof GhastTrapBlockEntity ghastTrap && ghastTrap.isCharged();
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
		return new GhastTrapBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.GHAST_TRAP.get(), GhastTrapBlockEntity::tick);
	}
}
