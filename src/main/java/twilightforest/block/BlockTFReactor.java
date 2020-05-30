package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import twilightforest.tileentity.*;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockTFReactor extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFReactor() {
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
			//world.notifyNeighborsRespectDebug(pos, thereBlock, false);
		//}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		if (!state.get(ACTIVE) && isReactorReady(world, pos)) {
			// check if we should fire up the reactor
			changeToBlockState(world, pos, state.with(ACTIVE, true));
		}
	}

	/**
	 * Check if the reactor has all the specified things around it
	 */
	private boolean isReactorReady(World world, BlockPos pos) {
		return Arrays.stream(Direction.values())
				.allMatch(e -> world.getBlockState(pos.offset(e)).getBlock() == Blocks.REDSTONE_BLOCK);
	}

	@Override
	@Deprecated
	public int getLightValue(BlockState state) {
		return state.get(ACTIVE) ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(ACTIVE);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.get(ACTIVE) ? new TileEntityTFCReactorActive() : null;
	}

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
