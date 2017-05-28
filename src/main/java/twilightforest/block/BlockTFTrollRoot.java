package twilightforest.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.item.TFItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockTFTrollRoot extends Block implements IShearable {

	protected BlockTFTrollRoot() {
		super(Material.PLANTS);
        this.setTickRandomly(true);
		this.setCreativeTab(TFItems.creativeTab);
		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this));
        return ret;
	}

	private boolean canBlockStay(World world, BlockPos pos) {
    	return canPlaceRootBelow(world, pos.up());
    }
    
    public static boolean canPlaceRootBelow(World world, BlockPos pos) {
    	IBlockState state = world.getBlockState(pos);
        Block blockAbove = state.getBlock();
    	
    	return state.getMaterial() == Material.ROCK || blockAbove == TFBlocks.trollVidr || blockAbove == TFBlocks.trollBer || blockAbove == TFBlocks.unripeTrollBer;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos);
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World par1World, BlockPos pos) {
    	return NULL_AABB;
    }
    
    @Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

	@Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(world, pos);
    }
	
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        this.checkAndDropBlock(world, pos);
    }
    
    private void checkAndDropBlock(World world, BlockPos pos) {
        if (!this.canBlockStay(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
