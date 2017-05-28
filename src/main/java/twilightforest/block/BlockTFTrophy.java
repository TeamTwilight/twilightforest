package twilightforest.block;

import java.util.List;

import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFTrophy;

public class BlockTFTrophy extends BlockSkull implements ModelRegisterCallback
{
    private static final AxisAlignedBB HYDRA_Y_BB = new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
    private static final AxisAlignedBB HYDRA_X_BB = new AxisAlignedBB(0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
    private static final AxisAlignedBB HYDRA_Z_BB = new AxisAlignedBB(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
    private static final AxisAlignedBB URGHAST_BB = new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F);

    public BlockTFTrophy() {
        setDefaultState(blockState.getBaseState().withProperty(BlockSkull.NODROP, false).withProperty(BlockSkull.FACING, EnumFacing.UP));
	}

    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess par1IBlockAccess, BlockPos pos)
    {
        EnumFacing facing = state.getValue(BlockSkull.FACING);
    	TileEntityTFTrophy trophy = (TileEntityTFTrophy)par1IBlockAccess.getTileEntity(pos);

        if (trophy != null && trophy.getSkullType() == 0)
        {
        	// hydra bounds
        	switch (facing)
        	{
        	case UP:
        	default:
        		return HYDRA_Y_BB;
        	case NORTH:
        	case SOUTH:
        		return HYDRA_Z_BB;
        	case WEST:
        	case EAST:
        		return HYDRA_X_BB;
        	}
        }
        else if (trophy != null && trophy.getSkullType() == 3)
        {
        	return URGHAST_BB;
        }
        else
        {
        	return super.getBoundingBox(state, par1IBlockAccess, pos);
        }
    }

    @Override
	public TileEntity createTileEntity(World var1, IBlockState state) {
        return new TileEntityTFTrophy();
	}

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityTFTrophy) {
            return new ItemStack(TFItems.trophy, 1, ((TileEntityTFTrophy) te).getSkullType());
        }
        return null;
    }

    // [VanillaCopy] of superclass, relevant edits indicated
    @Override
    public List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        {
            if (!((Boolean)state.getValue(NODROP)).booleanValue())
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntitySkull)
                {
                    TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                    ItemStack itemstack = new ItemStack(TFItems.trophy, 1, tileentityskull.getSkullType()); // TF - use our item

                    /*if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null)
                    {
                        itemstack.setTagCompound(new NBTTagCompound());
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                        itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                    }*/// TF - don't set player skins

                    ret.add(itemstack);
                }
            }
        }
        return ret;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(NODROP).ignore(FACING).build());
	}

}
