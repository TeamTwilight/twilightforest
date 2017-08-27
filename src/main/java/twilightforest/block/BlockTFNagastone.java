package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.NagastoneVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockTFNagastone extends Block  implements ModelRegisterCallback {
	public static final PropertyEnum<NagastoneVariant> VARIANT = PropertyEnum.create("variant", NagastoneVariant.class);

	public BlockTFNagastone() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);

		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, NagastoneVariant.SOLID));
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> stackList) {
		stackList.add(new ItemStack(this, 1, 0));
		stackList.add(new ItemStack(this, 1, 1));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, NagastoneVariant.values()[(meta & 15)]);
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	//      Blockstate to meta 0-3  will be heads
	//      Blockstate to meta 4-15 will be body components
	// BUT:
	//      Item meta 0 will be head
	//      Item meta 1 will be body
	// todo fix getStateForPlacement to respect this
	@Override
	public int damageDropped(IBlockState state) {
		return NagastoneVariant.isHead(state.getValue(VARIANT)) ? 0 : 1;
	}

	// Heads are manually placed, bodys are automatically connected
	// If player places head on horz side of block, use that block face. Else, defer to player rotation.
	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
		return meta == 0
				? this.getDefaultState().withProperty(VARIANT, NagastoneVariant.getHeadFromFacing(facing.getAxis().isHorizontal() ? facing : placer.getHorizontalFacing().getOpposite()))
				: this.getDefaultState();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState stateIn, Random rand) {
		if (NagastoneVariant.isHead(stateIn.getValue(VARIANT)))
			return;

		// If state is not a head then you may go ahead and update
		int connectionCount = 0;
		IBlockState stateOut;
		EnumFacing[] facings = new EnumFacing[2];

		// get sides
		for (EnumFacing side : EnumFacing.VALUES)
			if (world.getBlockState(pos.offset(side)).getBlock() == TFBlocks.nagastone)
				if (++connectionCount > 2) break;
				else facings[connectionCount - 1] = side;


		// if there are 2 sides that don't line on same axis, use an elbow part, else use axis part
		// if there is 1 side, then use an axis part
		// if there are 0 or greater than 2 sides, use solid
		// use default if there are more than 3 connections or 0
		switch (connectionCount) {
			case 1:
				facings[1] = facings[0]; // No null, for next statement
			case 2:
				stateOut = stateIn.withProperty(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
				break;
			default:
				stateOut = this.getDefaultState();
				break;
		}

		// if result matches state in world, no need to add more effort
		if (stateIn != stateOut) world.setBlockState(pos, stateOut);
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToState(this, 0, this.getDefaultState().withProperty(VARIANT, NagastoneVariant.EAST_HEAD));
		ModelUtils.registerToState(this, 1, this.getDefaultState().withProperty(VARIANT, NagastoneVariant.AXIS_X));
	}

}
