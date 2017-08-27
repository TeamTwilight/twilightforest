package twilightforest.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.List;

/**
 * Castle block makes a castle
 *
 * @author Ben
 */
public class BlockTFCastleMagic extends Block implements ModelRegisterCallback {
	public static final List<EnumDyeColor> VALID_COLORS = ImmutableList.of(EnumDyeColor.PINK, EnumDyeColor.BLUE, EnumDyeColor.YELLOW, EnumDyeColor.PURPLE);
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class, VALID_COLORS);

	public BlockTFCastleMagic() {
		super(Material.ROCK);
		this.setHardness(100F);
		this.setResistance(15F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(COLOR, EnumDyeColor.PINK));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return VALID_COLORS.indexOf(state.getValue(COLOR));
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(COLOR, VALID_COLORS.get(meta));
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, COLOR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
