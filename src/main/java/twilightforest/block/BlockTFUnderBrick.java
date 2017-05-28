package twilightforest.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.UnderBrickVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

public class BlockTFUnderBrick extends Block implements ModelRegisterCallback {

    public static final PropertyEnum<UnderBrickVariant> VARIANT = PropertyEnum.create("variant", UnderBrickVariant.class);

	public BlockTFUnderBrick() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, UnderBrickVariant.NORMAL));
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, UnderBrickVariant.values()[meta]);
	}

    @Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }
    
    @Override
	public int damageDropped(IBlockState state) {
    	return getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}
}
