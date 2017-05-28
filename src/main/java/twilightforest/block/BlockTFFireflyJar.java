package twilightforest.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.entity.passive.EntityTFTinyFirefly;
import twilightforest.item.TFItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTFFireflyJar extends Block implements ModelRegisterCallback {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);

	protected BlockTFFireflyJar() {
		super(Material.GLASS);
		this.setHardness(0.3F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setLightLevel(1.0F);
	}

    @Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
	public int getLightValue(IBlockState state)
    {
    	return 15;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return AABB;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
    	double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);
    	double dy = pos.getY() - 0.1F + ((rand.nextFloat() - rand.nextFloat()) * 0.4F);
    	double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);

    	EntityTFTinyFirefly tinyfly = new EntityTFTinyFirefly(world, dx, dy, dz);
    	world.addWeatherEffect(tinyfly);

    	dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);
    	dy = pos.getY() - 0.1F + ((rand.nextFloat() - rand.nextFloat()) * 0.4F);
    	dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);

    	tinyfly = new EntityTFTinyFirefly(world, dx, dy, dz);
    	world.addWeatherEffect(tinyfly);
    }

    @Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

}
