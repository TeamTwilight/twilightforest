package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import twilightforest.advancements.TFAdvancements;
import twilightforest.tileentity.*;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFGhastTrap extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFGhastTrap() {
		super(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(10.0F, 35.0F).sound(SoundType.WOOD));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 15;
	}

	/**
	 * Change this block into an different device block
	 */
	private static void changeToBlockState(World world, BlockPos pos, BlockState state) {
		//Block thereBlock = world.getBlockState(pos).getBlock();

		//if (thereBlock == TFBlocks.tower_device || thereBlock == TFBlocks.tower_translucent) {
			world.setBlockState(pos, state, 3);
			//world.markBlockRangeForRenderUpdate(pos, pos);
			//.notifyNeighborsRespectDebug(pos, thereBlock, false);
		//}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		if (!state.get(ACTIVE) && isInactiveTrapCharged(world, pos) && world.isBlockPowered(pos)) {
			for (ServerPlayerEntity player : world.getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(pos).grow(6.0D))) {
				TFAdvancements.ACTIVATED_GHAST_TRAP.trigger(player);
			}

			changeToBlockState(world, pos, state.with(ACTIVE, true));
			world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
			//world.scheduleUpdate(pos, this, 4);
		}
	}

	/**
	 * Check if the inactive trap block is fully charged
	 */
	private boolean isInactiveTrapCharged(World world, BlockPos pos) {
		TileEntityTFGhastTrapInactive tileEntity = (TileEntityTFGhastTrapInactive) world.getTileEntity(pos);
		return tileEntity != null && tileEntity.isCharged();
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(World worldIn, BlockPos pos) {
		Random random = worldIn.rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() - d0;
			}

			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	@Deprecated
	public int getLightValue(BlockState state) {
		return state.get(ACTIVE) ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.get(ACTIVE) ? new TileEntityTFGhastTrapActive() : new TileEntityTFGhastTrapInactive();
	}

	//TODO: Move to loot table
//	@Override
//	public Item getItemDropped(BlockState state, Random random, int fortune) {
//		switch (state.getValue(VARIANT)) {
//			case ANTIBUILDER:
//				return Items.AIR;
//			default:
//				return Item.getItemFromBlock(this);
//		}
//	}
//
//	@Override
//	@Deprecated
//	protected boolean canSilkHarvest() {
//		return false;
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//		return false;
//	}
//
//	@Override
//	public int damageDropped(BlockState state) {
//		switch (state.getValue(VARIANT)) {
//			case REAPPEARING_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE);
//				break;
//			case BUILDER_ACTIVE:
//			case BUILDER_TIMEOUT:
//				state = state.with(VARIANT, TowerDeviceVariant.BUILDER_INACTIVE);
//				break;
//			case VANISH_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.VANISH_INACTIVE);
//				break;
//			case GHASTTRAP_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.GHASTTRAP_INACTIVE);
//				break;
//			case REACTOR_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.REACTOR_INACTIVE);
//				break;
//			default:
//				break;
//		}
//
//		return getMetaFromState(state);
//	}

	//TODO: Move to client
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
