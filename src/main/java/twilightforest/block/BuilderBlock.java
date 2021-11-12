package twilightforest.block;

import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.block.entity.CarminiteBuilderBlockEntity;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.enums.TowerDeviceVariant;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;

public class BuilderBlock extends BaseEntityBlock {

	public static final EnumProperty<TowerDeviceVariant> STATE = EnumProperty.create("state", TowerDeviceVariant.class);

	public BuilderBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!world.isClientSide && state.getValue(STATE) == TowerDeviceVariant.BUILDER_INACTIVE && world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isClientSide) {
			return;
		}

		TowerDeviceVariant variant = state.getValue(STATE);

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE && world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
			world.getBlockTicks().scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && !world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
			world.getBlockTicks().scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_TIMEOUT && !world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
		}
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		TowerDeviceVariant variant = state.getValue(STATE);

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && world.hasNeighborSignal(pos)) {
			this.letsBuild(world, pos);
		}

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE || variant == TowerDeviceVariant.BUILDER_TIMEOUT) {
			((CarminiteBuilderBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).resetStats();
			for (Direction e : Direction.values()) {
				activateBuiltBlocks(world, pos.relative(e));
			}
		}
	}

	private void letsBuild(Level world, BlockPos pos) {
		CarminiteBuilderBlockEntity tileEntity = (CarminiteBuilderBlockEntity) world.getBlockEntity(pos);

		if (tileEntity != null && !tileEntity.makingBlocks) {
			tileEntity.startBuilding();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (state.getValue(STATE) == TowerDeviceVariant.BUILDER_ACTIVE) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(Level worldIn, BlockPos pos) {
		Random random = worldIn.random;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = pos.getX() + random.nextFloat();
			double d2 = pos.getY() + random.nextFloat();
			double d3 = pos.getZ() + random.nextFloat();

			if (i == 0 && !worldIn.getBlockState(pos.above()).isSolidRender(worldIn, pos)) {
				d2 = pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.below()).isSolidRender(worldIn, pos)) {
				d2 = pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isSolidRender(worldIn, pos)) {
				d3 = pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isSolidRender(worldIn, pos)) {
				d3 = pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isSolidRender(worldIn, pos)) {
				d1 = pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isSolidRender(worldIn, pos)) {
				d1 = pos.getX() - d0;
			}

			float f1 = 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1) {
				worldIn.addParticle(new DustParticleOptions(new Vector3f(f1, f2, f3), 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void activateBuiltBlocks(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() == TFBlocks.BUILT_BLOCK.get() && !state.getValue(TranslucentBuiltBlock.ACTIVE)) {
			world.setBlockAndUpdate(pos, state.setValue(TranslucentBuiltBlock.ACTIVE, true));
			world.playSound(null, pos, TFSounds.BUILDER_REPLACE, SoundSource.BLOCKS, 0.3F, 0.6F);
			world.getBlockTicks().scheduleTick(pos, state.getBlock(), 10);
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
