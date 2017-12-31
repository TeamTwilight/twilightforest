package twilightforest.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.Locale;

public class BlockTFNagastoneStairs extends BlockStairs implements ModelRegisterCallback {

	public static final PropertyEnum<LeftRight> DIRECTION = PropertyEnum.create("direction", LeftRight.class);

	BlockTFNagastoneStairs(IBlockState state) {
		super(state);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(this.getDefaultState().withProperty(DIRECTION, LeftRight.LEFT));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HALF, SHAPE, DIRECTION);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (state.getValue(DIRECTION) == LeftRight.RIGHT ? 8 : 0);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta & 0b0111).withProperty(DIRECTION, (meta & 0b1000) == 8 ? LeftRight.RIGHT : LeftRight.LEFT);
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 8));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DIRECTION) == LeftRight.RIGHT ? 8 : 0;
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return super.withMirror(state, mirrorIn).withProperty(DIRECTION, state.getValue(DIRECTION) == LeftRight.LEFT ? LeftRight.RIGHT : LeftRight.LEFT);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToState(this, 0, getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		ModelUtils.registerToState(this, 8, getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(DIRECTION, LeftRight.RIGHT));
	}

	private enum LeftRight implements IStringSerializable {
		LEFT,
		RIGHT;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
