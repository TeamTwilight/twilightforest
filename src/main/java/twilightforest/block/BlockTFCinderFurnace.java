package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.tileentity.TileEntityTFCinderFurnace;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFCinderFurnace extends Block {

	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	BlockTFCinderFurnace() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(7.0F).lightValue(15));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(LIT) ? 15 : 0;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIT, FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTFCinderFurnace();
	}

	@Override
	@Deprecated
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	@Override
	@Deprecated
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityTFCinderFurnace) {
			player.openContainer((TileEntityTFCinderFurnace) world.getTileEntity(pos));
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			((FurnaceTileEntity) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			Blocks.FURNACE.animateTick(state, world, pos, random);
		}
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityTFCinderFurnace) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTFCinderFurnace)tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	//	@Override
//	public Item getItemDropped(BlockState state, Random rand, int fortune) {
//		return Item.getItemFromBlock(TFBlocks.cinder_furnace);
//	}
//
//	@Override
//	public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
//		return new ItemStack(Item.getItemFromBlock(TFBlocks.cinder_furnace));
//	}
}
