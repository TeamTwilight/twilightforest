package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFAdvancements;
import twilightforest.block.entity.GhastTrapBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

public class GhastTrapBlock extends BaseEntityBlock {

	public static final MapCodec<GhastTrapBlock> CODEC = simpleCodec(GhastTrapBlock::new);
	public static final int ACTIVATE_EVENT = 0;
	public static final int DEACTIVATE_EVENT = 1;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public GhastTrapBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ACTIVE);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
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
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int event, int payload) {
		BlockEntity te = level.getBlockEntity(pos);
		return te != null && te.triggerEvent(event, payload);
	}

	/**
	 * Check if the inactive trap block is fully charged
	 */
	private boolean isInactiveTrapCharged(Level level, BlockPos pos) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		return tileEntity instanceof GhastTrapBlockEntity && ((GhastTrapBlockEntity) tileEntity).isCharged();
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = pos.getX() + random.nextFloat();
			double d2 = pos.getY() + random.nextFloat();
			double d3 = pos.getZ() + random.nextFloat();

			if (i == 0 && !level.getBlockState(pos.above()).isSolidRender(level, pos)) {
				d2 = pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !level.getBlockState(pos.below()).isSolidRender(level, pos)) {
				d2 = pos.getY() - d0;
			}

			if (i == 2 && !level.getBlockState(pos.south()).isSolidRender(level, pos)) {
				d3 = pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !level.getBlockState(pos.north()).isSolidRender(level, pos)) {
				d3 = pos.getZ() - d0;
			}

			if (i == 4 && !level.getBlockState(pos.east()).isSolidRender(level, pos)) {
				d1 = pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !level.getBlockState(pos.west()).isSolidRender(level, pos)) {
				d1 = pos.getX() - d0;
			}

			if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1) {
				level.addParticle(DustParticleOptions.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GhastTrapBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.GHAST_TRAP.get(), GhastTrapBlockEntity::tick);
	}
}
