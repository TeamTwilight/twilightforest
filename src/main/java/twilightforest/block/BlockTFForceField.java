package twilightforest.block;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockTFForceField extends BlockTFConnectableRotatedPillar implements ModelRegisterCallback {
	public static final List<EnumDyeColor> VALID_COLORS = ImmutableList.of(EnumDyeColor.PURPLE, EnumDyeColor.PINK, EnumDyeColor.ORANGE, EnumDyeColor.GREEN, EnumDyeColor.BLUE);
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class, VALID_COLORS);

	BlockTFForceField() {
		super(Material.BARRIER, 7, 9);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
		this.setLightLevel(2F / 15F);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(this.getDefaultState().withProperty(COLOR, EnumDyeColor.PURPLE));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return VALID_COLORS.indexOf(state.getValue(COLOR)) + (((state.getValue(AXIS).ordinal() + 1) % 3) * 5);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(COLOR, VALID_COLORS.get(meta % 5)).withProperty(AXIS, EnumFacing.Axis.values()[((meta / 5) + 1) % 3]);
	}

	@Override
	protected IProperty[] getAdditionalProperties() {
		return new IProperty[]{ COLOR };
	}

	@Override
	protected AxisAlignedBB getSidedAABBStraight(EnumFacing facing, EnumFacing.Axis axis) {
		return makeQuickAABB(
				facing == EnumFacing.EAST  || axis == EnumFacing.Axis.X ? 16 : this.boundingBoxLower,
				facing == EnumFacing.UP    || axis == EnumFacing.Axis.Y ? 16 : this.boundingBoxLower,
				facing == EnumFacing.SOUTH || axis == EnumFacing.Axis.Z ? 16 : this.boundingBoxLower,
				facing == EnumFacing.WEST  || axis == EnumFacing.Axis.X ?  0 : this.boundingBoxUpper,
				facing == EnumFacing.DOWN  || axis == EnumFacing.Axis.Y ?  0 : this.boundingBoxUpper,
				facing == EnumFacing.NORTH || axis == EnumFacing.Axis.Z ?  0 : this.boundingBoxUpper);
	}

	@Override
	protected boolean canConnectTo(IBlockState state, IBlockState otherState, IBlockAccess world, BlockPos pos, EnumFacing connectTo) {
		BlockFaceShape blockFaceShape = otherState.getBlockFaceShape(world, pos, connectTo);

		return blockFaceShape == BlockFaceShape.SOLID
				|| blockFaceShape == BlockFaceShape.MIDDLE_POLE_THIN
				|| super.canConnectTo(state, otherState, world, pos, connectTo);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		for (int i = 0; i < COLOR.getAllowedValues().size(); i++) {
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face.getAxis() != state.getValue(AXIS) ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(TFBlocks.force_field, 1, VALID_COLORS.indexOf(state.getValue(COLOR)));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), "inventory");
		for (int i = 0; i < VALID_COLORS.size(); i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, mrl);
		}

		//ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(blockState.getProperties().toArray(new IProperty[blockState.getProperties().size()])).build());
	}
}
