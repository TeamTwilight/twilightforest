package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.Leaves3Variant;
import twilightforest.enums.ThornVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class BlockTFThorns extends BlockTFConnectableRotatedPillar implements ModelRegisterCallback {

	public static final IProperty<ThornVariant> VARIANT = PropertyEnum.create("variant", ThornVariant.class);

	private static final float THORN_DAMAGE = 4.0F;

	BlockTFThorns() {
		super(Material.WOOD, MapColor.OBSIDIAN, 10);
		this.setHardness(50.0F);
		this.setResistance(2000.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);

		if (hasVariant()) {
			this.setDefaultState(this.getDefaultState().withProperty(VARIANT, ThornVariant.BROWN));
		}
	}

	@Override
	protected IProperty[] getAdditionalProperties() {
		return new IProperty[]{VARIANT};
	}

	protected boolean hasVariant() {
		return true;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return hasVariant() ? super.getMetaFromState(state) | state.getValue(VARIANT).ordinal() : super.getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return hasVariant() ? super.getStateFromMeta(meta).withProperty(VARIANT, ThornVariant.values()[meta & 0b11]) : super.getStateFromMeta(meta);
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(VARIANT) == ThornVariant.GREEN ? MapColor.FOLIAGE : super.getMapColor(state, world, pos);
	}

	@Override
	protected boolean canConnectTo(IBlockState state, IBlockState otherState, IBlockAccess world, BlockPos pos, EnumFacing connectTo) {
		return (otherState.getBlock() instanceof BlockTFThorns
				|| otherState.getBlock() == TFBlocks.thorn_rose
				||(otherState.getBlock() == TFBlocks.twilight_leaves_3 && otherState.getValue(BlockTFLeaves3.VARIANT) == Leaves3Variant.THORN)
				|| otherState.getMaterial() == Material.GRASS
				|| otherState.getMaterial() == Material.GROUND)
				|| super.canConnectTo(state, otherState, world, pos, connectTo);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return hasVariant() ? state.getValue(VARIANT).ordinal() : 0;
	}

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		entity.attackEntityFrom(DamageSource.CACTUS, THORN_DAMAGE);
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockTFThorns && state.getValue(AXIS) == EnumFacing.Axis.Y)
			onEntityCollision(world, pos, state, entity);

		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean harvest) {
		if (!player.capabilities.isCreativeMode) {
			if (!world.isRemote) {
				// grow back
				world.setBlockState(pos, state, 2);
				// grow more
				this.doThornBurst(world, pos, state);
			}
		} else {
			world.setBlockToAir(pos);
		}

		return true;
	}

	@Override
	@Deprecated
	public EnumPushReaction getPushReaction(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(World world, BlockPos pos, IBlockState state) {
		switch (state.getValue(AXIS)) {
			case Y:
				growThorns(world, pos, EnumFacing.UP);
				growThorns(world, pos, EnumFacing.DOWN);
				break;
			case X:
				growThorns(world, pos, EnumFacing.EAST);
				growThorns(world, pos, EnumFacing.WEST);
				break;
			case Z:
				growThorns(world, pos, EnumFacing.NORTH);
				growThorns(world, pos, EnumFacing.SOUTH);
				break;
		}

		// also try three random directions
		growThorns(world, pos, EnumFacing.random(world.rand));
		growThorns(world, pos, EnumFacing.random(world.rand));
		growThorns(world, pos, EnumFacing.random(world.rand));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(World world, BlockPos pos, EnumFacing dir) {
		int length = 1 + world.rand.nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (world.isAirBlock(dPos)) {
				world.setBlockState(dPos, getDefaultState().withProperty(AXIS, dir.getAxis()).withProperty(VARIANT, ThornVariant.GREEN), 2);
			} else {
				break;
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		int range = 4;
		int exRange = range + 1;

		if (world.isAreaLoaded(pos, exRange)) {
			for (BlockPos pos_ : WorldUtil.getAllAround(pos, range)) {
				IBlockState state_ = world.getBlockState(pos_);
				if (state_.getBlock().isLeaves(state_, world, pos_)) {
					state.getBlock().beginLeavesDecay(state_, world, pos_);
				}
			}
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		for (int i = 0; i < (hasVariant() ? ThornVariant.values().length : 1); i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face.getAxis() != state.getValue(AXIS) ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return (blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockTFThorns || super.shouldSideBeRendered(blockState, blockAccess, pos, side));
	}
}
