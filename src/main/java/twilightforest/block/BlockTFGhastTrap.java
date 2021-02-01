package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.tileentity.*;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFGhastTrap extends Block {
	public static final int ACTIVATE_EVENT = 0;
	public static final int DEACTIVATE_EVENT = 1;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFGhastTrap(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) {
			return;
		}

		if (!state.get(ACTIVE) && isInactiveTrapCharged(world, pos) && world.isBlockPowered(pos)) {
			for (ServerPlayerEntity player : world.getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(pos).grow(6.0D))) {
				TFAdvancements.ACTIVATED_GHAST_TRAP.trigger(player);
			}

			world.setBlockState(pos, state.with(ACTIVE, true));
			world.playSound(null, pos, TFSounds.JET_START, SoundCategory.BLOCKS, 0.3F, 0.6F);
			world.addBlockEvent(pos, this, ACTIVATE_EVENT, 0);
		}
	}

	@Override
	public boolean eventReceived(BlockState state, World world, BlockPos pos, int event, int payload) {
		TileEntity te = world.getTileEntity(pos);
		return te != null && te.receiveClientEvent(event, payload);
	}

	/**
	 * Check if the inactive trap block is fully charged
	 */
	private boolean isInactiveTrapCharged(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		return tileEntity instanceof TileEntityTFGhastTrapActive && ((TileEntityTFGhastTrapActive) tileEntity).isCharged();
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

			if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1) {
				worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTFGhastTrapActive();
	}
}
