package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFCinderFurnace;

import java.util.Random;

public class BlockTFCinderFurnace extends Block implements ModelRegisterCallback {

	private static boolean keepInventory;
	private final boolean isBurning;

	BlockTFCinderFurnace(Boolean isLit) {
		super(Material.WOOD);

		this.isBurning = isLit;

		this.setHardness(7.0F);

		this.setLightLevel(isLit ? 1F : 0);

		if (!isLit) {
			this.setCreativeTab(TFItems.creativeTab);
		}

		this.setDefaultState(blockState.getBaseState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockFurnace.FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockFurnace.FACING).getHorizontalIndex();
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public TileEntity createTileEntity(World p_149915_1_, IBlockState state) {
		return new TileEntityTFCinderFurnace();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityTFCinderFurnace) {
			player.openGui(TwilightForestMod.instance, TwilightForestMod.GUI_ID_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
		if (itemStack.hasDisplayName()) {
			((TileEntityFurnace) world.getTileEntity(pos)).setCustomInventoryName(itemStack.getDisplayName());
		}
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, TFBlocks.cinder_furnace_lit.getDefaultState().withProperty(BlockFurnace.FACING, iblockstate.getValue(BlockFurnace.FACING)), 3);
		} else {
			worldIn.setBlockState(pos, TFBlocks.cinder_furnace.getDefaultState().withProperty(BlockFurnace.FACING, iblockstate.getValue(BlockFurnace.FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (this.isBurning) {
			Blocks.LIT_FURNACE.randomDisplayTick(state, world, pos, random);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityFurnace) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityFurnace) tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(TFBlocks.cinder_furnace);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(TFBlocks.cinder_furnace));
	}
}
