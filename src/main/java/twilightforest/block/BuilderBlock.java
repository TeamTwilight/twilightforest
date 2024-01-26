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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.entity.CarminiteBuilderBlockEntity;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import java.util.Objects;

public class BuilderBlock extends BaseEntityBlock {

	public static final MapCodec<BuilderBlock> CODEC = simpleCodec(BuilderBlock::new);
	public static final EnumProperty<TowerDeviceVariant> STATE = EnumProperty.create("state", TowerDeviceVariant.class);

	public BuilderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
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
		builder.add(STATE);
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide() && state.getValue(STATE) == TowerDeviceVariant.BUILDER_INACTIVE && level.hasNeighborSignal(pos)) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			level.playSound(null, pos, TFSounds.BUILDER_ON.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
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
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(STATE) == TowerDeviceVariant.BUILDER_ACTIVE) {
			this.sparkle(level, pos);
		}
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

			float f1 = 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1) {
				level.addParticle(new DustParticleOptions(new Vector3f(f1, f2, f3), 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CarminiteBuilderBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return state.getValue(STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? createTickerHelper(type, TFBlockEntities.TOWER_BUILDER.get(), CarminiteBuilderBlockEntity::tick) : null;
	}
}
