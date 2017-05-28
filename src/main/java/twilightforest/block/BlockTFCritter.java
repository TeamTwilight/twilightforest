package twilightforest.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockTFCritter extends Block {

    private final float WIDTH = getWidth();
    private final AxisAlignedBB DOWN_BB = new AxisAlignedBB(0.5F - WIDTH, 1.0F - WIDTH * 2.0F, 0.2F, 0.5F + WIDTH, 1.0F, 0.8F);;
    private final AxisAlignedBB UP_BB = new AxisAlignedBB(0.5F - WIDTH, 0.0F, 0.2F, 0.5F + WIDTH, WIDTH * 2.0F, 0.8F);;
    private final AxisAlignedBB NORTH_BB = new AxisAlignedBB(0.5F - WIDTH, 0.2F, 1.0F - WIDTH * 2.0F, 0.5F + WIDTH, 0.8F, 1.0F);;
    private final AxisAlignedBB SOUTH_BB = new AxisAlignedBB(0.5F - WIDTH, 0.2F, 0.0F, 0.5F + WIDTH, 0.8F, WIDTH * 2.0F);;
    private final AxisAlignedBB WEST_BB = new AxisAlignedBB(1.0F - WIDTH * 2.0F, 0.2F, 0.5F - WIDTH, 1.0F, 0.8F, 0.5F + WIDTH);;
    private final AxisAlignedBB EAST_BB = new AxisAlignedBB(0.0F, 0.2F, 0.5F - WIDTH, WIDTH * 2.0F, 0.8F, 0.5F + WIDTH);

    protected BlockTFCritter()
    {
        super(Material.CIRCUITS);
		this.setHardness(0.0F);
		this.setCreativeTab(TFItems.creativeTab);
		this.setSoundType(SoundType.SLIME);
        this.setDefaultState(blockState.getBaseState().withProperty(TFBlockProperties.FACING, EnumFacing.UP));
    }

    public float getWidth() {
        return 0.15F;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TFBlockProperties.FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(TFBlockProperties.FACING)) {
            case DOWN: return 6;
            default: case UP: return 5;
            case NORTH: return 4;
            case SOUTH: return 3;
            case WEST: return 2;
            case EAST: return 1;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.UP;
        switch (meta) {
            case 6: facing = EnumFacing.DOWN; break;
            case 5: facing = EnumFacing.UP; break;
            case 4: facing = EnumFacing.NORTH; break;
            case 3: facing = EnumFacing.SOUTH; break;
            case 2: facing = EnumFacing.WEST; break;
            case 1: facing = EnumFacing.EAST; break;
        }
        return getDefaultState().withProperty(TFBlockProperties.FACING, facing);
    }
    
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        switch (state.getValue(TFBlockProperties.FACING)) {
            case DOWN: return DOWN_BB;
            case UP: default: return UP_BB;
            case NORTH: return NORTH_BB;
            case SOUTH: return SOUTH_BB;
            case WEST: return WEST_BB;
            case EAST: return EAST_BB;
        }
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        return NULL_AABB;
    }

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing e : EnumFacing.VALUES) {
            if (!canPlaceAt(world, pos.offset(e))) {
                return false;
            }
        }

		return true;
	}

	
	@Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing sideHit, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
    {
        IBlockState state = getDefaultState();

        if (canPlaceAt(world, pos.offset(sideHit.getOpposite()))) {
            state = state.withProperty(TFBlockProperties.FACING, sideHit);
        }
        
        return state;
    }

    @Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        dropCritterIfCantStay(world, pos);

        /*// for fireflies, schedule a lighting update todo 1.9 needed?
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) {
        	world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }*/
    }

    public boolean dropCritterIfCantStay(World world, BlockPos pos)
    {
        if(!canPlaceBlockAt(world, pos))
        {
            world.destroyBlock(pos, true);
            return false;
        } else
        {
            return true;
        }
    }

    @Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockID)
    {
        if(dropCritterIfCantStay(world, pos))
        {
            EnumFacing facing = state.getValue(TFBlockProperties.FACING);
            if (!canPlaceAt(world, pos.offset(facing.getOpposite()))) {
                world.destroyBlock(pos, true);
            }
        }
    }

    private boolean canPlaceAt(World world, BlockPos pos)
    {
    	return world.isBlockNormalCube(pos, true) || world.getBlockState(pos).getMaterial() == Material.LEAVES || world.getBlockState(pos).getMaterial() == Material.CACTUS;
    }	

    @Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
    public abstract TileEntity createTileEntity(World world, IBlockState state);

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
}
	
	
