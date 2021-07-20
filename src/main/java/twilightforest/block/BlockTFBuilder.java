package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.tileentity.*;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFBuilder extends Block {

	public static final EnumProperty<TowerDeviceVariant> STATE = EnumProperty.create("state", TowerDeviceVariant.class);

	public BlockTFBuilder(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(STATE);
	}

	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!world.isRemote && state.get(STATE) == TowerDeviceVariant.BUILDER_INACTIVE && world.isBlockPowered(pos)) {
			world.setBlockState(pos, state.with(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) {
			return;
		}

		TowerDeviceVariant variant = state.get(STATE);

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE && world.isBlockPowered(pos)) {
			world.setBlockState(pos, state.with(STATE, TowerDeviceVariant.BUILDER_ACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
			world.getPendingBlockTicks().scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && !world.isBlockPowered(pos)) {
			world.setBlockState(pos, state.with(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
			world.playSound(null, pos, TFSounds.BUILDER_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
			world.getPendingBlockTicks().scheduleTick(pos, this, 4);
		}

		if (variant == TowerDeviceVariant.BUILDER_TIMEOUT && !world.isBlockPowered(pos)) {
			world.setBlockState(pos, state.with(STATE, TowerDeviceVariant.BUILDER_INACTIVE));
		}
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		TowerDeviceVariant variant = state.get(STATE);

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE && world.isBlockPowered(pos)) {
			this.letsBuild(world, pos);
		}

		if (variant == TowerDeviceVariant.BUILDER_INACTIVE || variant == TowerDeviceVariant.BUILDER_TIMEOUT) {
			for (Direction e : Direction.values()) {
				activateBuiltBlocks(world, pos.offset(e));
			}
		}
	}

	private void letsBuild(World world, BlockPos pos) {
		TileEntityTFTowerBuilder tileEntity = (TileEntityTFTowerBuilder) world.getTileEntity(pos);

		if (tileEntity != null && !tileEntity.makingBlocks) {
			tileEntity.startBuilding();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(STATE) == TowerDeviceVariant.BUILDER_ACTIVE) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(World worldIn, BlockPos pos) {
		Random random = worldIn.rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = pos.getX() + random.nextFloat();
			double d2 = pos.getY() + random.nextFloat();
			double d3 = pos.getZ() + random.nextFloat();

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube(worldIn, pos)) {
				d2 = pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube(worldIn, pos)) {
				d2 = pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube(worldIn, pos)) {
				d3 = pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube(worldIn, pos)) {
				d3 = pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube(worldIn, pos)) {
				d1 = pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube(worldIn, pos)) {
				d1 = pos.getX() - d0;
			}

			float f1 = 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1) {
				worldIn.addParticle(new RedstoneParticleData(f1, f2, f3, 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void activateBuiltBlocks(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() == TFBlocks.built_block.get() && !state.get(BlockTFBuiltTranslucent.ACTIVE)) {
			world.setBlockState(pos, state.with(BlockTFBuiltTranslucent.ACTIVE, true));
			world.playSound(null, pos, TFSounds.BUILDER_REPLACE, SoundCategory.BLOCKS, 0.3F, 0.6F);
			world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), /*state.getBlock().tickRate(world)*/ 15); //TODO: Potentially incorrect, but we aren't allowed block tick rates
		}
	}

	/**
	 * We need variable, metadata-based tick rates
	 */

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(STATE) == TowerDeviceVariant.BUILDER_ACTIVE;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.get(STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? new TileEntityTFTowerBuilder() : null;
	}
}
