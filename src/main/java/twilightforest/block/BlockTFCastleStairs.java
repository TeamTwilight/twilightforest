package twilightforest.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.CastlePillarVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

public class BlockTFCastleStairs extends BlockStairs implements ModelRegisterCallback {

	public static final PropertyEnum<CastlePillarVariant> VARIANT = PropertyEnum.create("variant", CastlePillarVariant.class);

	BlockTFCastleStairs(IBlockState state) {
		super(state);
		this.setHardness(100F);
		this.setResistance(35F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(getDefaultState().withProperty(VARIANT, CastlePillarVariant.ENCASED));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HALF, SHAPE, VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (state.getValue(VARIANT) == CastlePillarVariant.BOLD ? 8 : 0);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta & 0b0111).withProperty(VARIANT, (meta & 0b1000) == 8 ? CastlePillarVariant.BOLD : CastlePillarVariant.ENCASED);
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 8));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT) == CastlePillarVariant.BOLD ? 8 : 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToState(this, 0, getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		ModelUtils.registerToState(this, 8, getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(VARIANT, CastlePillarVariant.BOLD));
	}

}
