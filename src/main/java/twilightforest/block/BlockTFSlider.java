package twilightforest.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.entity.EntityTFSlideBlock;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFSlider extends BlockRotatedPillar implements ModelRegisterCallback {

	public static final PropertyInteger DELAY = PropertyInteger.create("delay", 0, 3);

	private static final int TICK_TIME = 80;
	private static final int OFFSET_TIME = 20;
	private static final int PLAYER_RANGE = 32;
	private static final float BLOCK_DAMAGE = 5;
	private static final AxisAlignedBB Y_BB = new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1F, 0.6875);
	private static final AxisAlignedBB Z_BB = new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 1F);
	private static final AxisAlignedBB X_BB = new AxisAlignedBB(0, 0.3125, 0.3125, 1F, 0.6875, 0.6875);

	protected BlockTFSlider() {
		super(Material.IRON);
		this.setCreativeTab(TFItems.creativeTab);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y).withProperty(DELAY, 0));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS, DELAY);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) | state.getValue(DELAY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(DELAY, meta & 0b11);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		switch (state.getValue(AXIS)) {
			case Y:
			default:
				return Y_BB;
			case X:
				return X_BB;
			case Z:
				return Z_BB;
		}
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random par5Random) {
		if (!world.isRemote && this.isConnectedInRange(world, pos)) {
			//world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, TwilightForestMod.ID + ":random.creakstart", 0.75F, 1.5F);

			EntityTFSlideBlock slideBlock = new EntityTFSlideBlock(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
			world.spawnEntity(slideBlock);
		}

		scheduleBlockUpdate(world, pos);
	}

	/**
	 * Check if there is any players in range, and also recursively check connected blocks
	 */
	public boolean isConnectedInRange(World world, BlockPos pos) {
		EnumFacing.Axis axis = world.getBlockState(pos).getValue(AXIS);

		switch (axis) {
			case Y:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.UP) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.DOWN);
			case X:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.WEST) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.EAST);
			case Z:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.NORTH) || this.isConnectedInRangeRecursive(world, pos, EnumFacing.SOUTH);
			default:
				return this.anyPlayerInRange(world, pos);
		}
	}

	private boolean isConnectedInRangeRecursive(World world, BlockPos pos, EnumFacing dir) {
		BlockPos dPos = pos.offset(dir);

		if (world.getBlockState(pos) == world.getBlockState(dPos)) {
			return this.anyPlayerInRange(world, dPos) || this.isConnectedInRangeRecursive(world, dPos, dir);
		} else {
			return false;
		}
	}

	private boolean anyPlayerInRange(World world, BlockPos pos) {
		return world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, PLAYER_RANGE, false) != null;
	}

	public void scheduleBlockUpdate(World world, BlockPos pos) {
		int offset = world.getBlockState(pos).getValue(DELAY);
		int update = TICK_TIME - ((int) (world.getWorldTime() - (offset * OFFSET_TIME)) % TICK_TIME);
		world.scheduleUpdate(pos, this, update);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		scheduleBlockUpdate(world, pos);
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		entity.attackEntityFrom(DamageSource.GENERIC, BLOCK_DAMAGE);
		if (entity instanceof EntityLivingBase) {
			double kx = (pos.getX() + 0.5 - entity.posX) * 2.0;
			double kz = (pos.getZ() + 0.5 - entity.posZ) * 2.0;

			((EntityLivingBase) entity).knockBack(null, 2, kx, kz);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(DELAY).build());
		for (int i = 0; i < 4; i++) {
			ModelUtils.registerToState(this, i, getDefaultState());
		}
	}
}
