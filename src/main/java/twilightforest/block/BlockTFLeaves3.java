package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;
import twilightforest.enums.Leaves3Variant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFLeaves3 extends BlockLeaves implements ModelRegisterCallback {

	public static final PropertyEnum<Leaves3Variant> VARIANT = PropertyEnum.create("variant", Leaves3Variant.class);

	protected BlockTFLeaves3() {
		this.setCreativeTab(TFItems.creativeTab);
		this.setLightOpacity(1);
		this.setDefaultState(
				blockState.getBaseState()
						.withProperty(CHECK_DECAY, true)
						.withProperty(DECAYABLE, true)
						.withProperty(VARIANT, Leaves3Variant.THORN)
		);
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return TFConfig.performance.leavesLightOpacity;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return TFConfig.performance.leavesFullCube;
	}

	// [VanillaCopy] BlockLeavesNew.getMetaFromState - could subclass, but different VARIANT property
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i |= state.getValue(VARIANT).ordinal();

		if (!state.getValue(DECAYABLE)) {
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY)) {
			i |= 8;
		}

		return i;
	}

	// [VanillaCopy] BlockLeavesNew.getStateFromMeta - could subclass, but different VARIANT property
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		int variant = meta & 3;
		final Leaves3Variant[] values = Leaves3Variant.values();

		return getDefaultState()
				.withProperty(VARIANT, values[variant % values.length])
				.withProperty(DECAYABLE, (meta & 4) == 0)
				.withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, VARIANT);
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return BlockPlanks.EnumType.OAK;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, world.getBlockState(pos).getValue(VARIANT).ordinal());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		return TFItems.magic_beans;
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		for (int i = 0; i < Leaves3Variant.values().length; i++) {
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this, 1, world.getBlockState(pos).getValue(VARIANT).ordinal()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(CHECK_DECAY).ignore(DECAYABLE).build());
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 30;
	}

	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}
}
