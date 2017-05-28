package twilightforest.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.block.enums.Leaves3Variant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

public class BlockTFLeaves3 extends BlockLeaves implements ModelRegisterCallback {

    public static final PropertyEnum<Leaves3Variant> VARIANT = PropertyEnum.create("variant", Leaves3Variant.class);

	protected BlockTFLeaves3() {
		setCreativeTab(TFItems.creativeTab);
        setDefaultState(
        		blockState.getBaseState()
				        .withProperty(CHECK_DECAY, true)
				        .withProperty(DECAYABLE, true)
				        .withProperty(VARIANT, Leaves3Variant.THORN)
        );
	}

	// [VanillaCopy] BlockLeavesNew.getMetaFromState - could subclass, but different VARIANT property
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i |= state.getValue(VARIANT).ordinal();

		if (!state.getValue(DECAYABLE))
		{
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY))
		{
			i |= 8;
		}

		return i;
	}

	// [VanillaCopy] BlockLeavesNew.getStateFromMeta - could subclass, but different VARIANT property
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
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
    public int damageDropped(IBlockState state)
    {
        return 4; //todo 1.9 verify
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(state) & 0b11);
    }
    
    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3)
    {
    	return TFItems.magicBeans;
    }
    
	@Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	for (int i = 0; i < Leaves3Variant.values().length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}


    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return ImmutableList.of(); // todo 1.9
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(CHECK_DECAY).ignore(DECAYABLE).build());
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}
}
