package twilightforest.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.block.enums.ThornVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTFThorns extends BlockRotatedPillar implements ModelRegisterCallback {

	public static final PropertyEnum<ThornVariant> VARIANT = PropertyEnum.create("variant", ThornVariant.class);
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");

    private static final float THORN_DAMAGE = 4.0F;
	private static final AxisAlignedBB Y_BB = new AxisAlignedBB(0.1875, 0, 0.1875, 0.8125, 1F, 0.8125);
	private static final AxisAlignedBB X_BB = new AxisAlignedBB(0, 0.1875, 0.1875, 1F, 0.8125, 0.8125);
	private static final AxisAlignedBB Z_BB = new AxisAlignedBB(0.1875, 0.1875, 0, 0.8125, 0.8125, 1F);
    
    private String[] names;

	protected BlockTFThorns() {
		super(Material.WOOD);
		this.setNames(new String[] {"brown", "green"});
		this.setHardness(50.0F);
		this.setResistance(2000.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState()
				.withProperty(AXIS, EnumFacing.Axis.Y)
				.withProperty(VARIANT, ThornVariant.BROWN)
				.withProperty(DOWN, false).withProperty(UP, false)
				.withProperty(NORTH, false).withProperty(SOUTH, false)
				.withProperty(WEST, false).withProperty(EAST, false)
		);
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AXIS, VARIANT, DOWN, UP, NORTH, SOUTH, WEST, EAST);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return super.getMetaFromState(state) | state.getValue(VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return super.getStateFromMeta(meta).withProperty(VARIANT, ThornVariant.values()[meta & 0b11]);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(DOWN, canConnectTo(state, world, pos, EnumFacing.DOWN))
				.withProperty(UP, canConnectTo(state, world, pos, EnumFacing.UP))
				.withProperty(NORTH, canConnectTo(state, world, pos, EnumFacing.NORTH))
				.withProperty(SOUTH, canConnectTo(state, world, pos, EnumFacing.SOUTH))
				.withProperty(WEST, canConnectTo(state, world, pos, EnumFacing.WEST))
				.withProperty(EAST, canConnectTo(state, world, pos, EnumFacing.EAST));
	}

	private boolean canConnectTo(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing connectTo)
	{
		IBlockState otherState = world.getBlockState(pos.offset(connectTo));
		return (otherState.getBlock() == TFBlocks.thorns || otherState.getBlock() == TFBlocks.burntThorns)
				&& state.getValue(AXIS) != connectTo.getAxis();
	}

	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
    {
    	switch (state.getValue(AXIS)) {
    	case Y:
    	default:
        	return Y_BB;
    	case X:
        	return X_BB;
    	case Z:
        	return Z_BB;
    	}
    }

	@Override
    @SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        return this.getCollisionBoundingBox(state, world, pos);
    }

	@Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
    	entity.attackEntityFrom(DamageSource.cactus, THORN_DAMAGE);
    }

	@Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean harvest) {
    	if (!player.capabilities.isCreativeMode) {
    		if (!world.isRemote) {
    			// grow back
    			world.setBlockState(pos, state, 2);
    			// grow more
    			this.doThornBurst(world, pos, state);
    		}
    	} else {
            world.setBlockToAir(pos);
    	}
    	
    	return true;
    }

	@Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.BLOCK;
    }

    /**
     * Grow thorns out of both the ends, then maybe in another direction too
     */
    private void doThornBurst(World world, BlockPos pos, IBlockState state) {
		switch (state.getValue(AXIS)) {
		case Y:
			growThorns(world, pos, EnumFacing.UP);
			growThorns(world, pos, EnumFacing.DOWN);
			break;
		case X:
			growThorns(world, pos, EnumFacing.EAST);
			growThorns(world, pos, EnumFacing.WEST);
			break;
		case Z:
			growThorns(world, pos, EnumFacing.NORTH);
			growThorns(world, pos, EnumFacing.SOUTH);
			break;
		}
		
		// also try three random directions
		growThorns(world, pos, EnumFacing.random(world.rand));
		growThorns(world, pos, EnumFacing.random(world.rand));
		growThorns(world, pos, EnumFacing.random(world.rand));
	}

    /**
     * grow several green thorns in the specified direction
     */
	private void growThorns(World world, BlockPos pos, EnumFacing dir) {
		int length = 1 + world.rand.nextInt(3);
		
		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (world.isAirBlock(dPos)) {
				world.setBlockState(dPos, getDefaultState().withProperty(AXIS, dir.getAxis()).withProperty(VARIANT, ThornVariant.GREEN), 2);
			} else {
				break;
			}
		}
	}

	@Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        byte range = 4;
        int exRange = range + 1;

        if (world.isAreaLoaded(pos, exRange))
        {
            for (int dx = -range; dx <= range; ++dx)
            {
                for (int dy = -range; dy <= range; ++dy)
                {
                    for (int dz = -range; dz <= range; ++dz)
                    {
						BlockPos pos_ = pos.add(dx, dy, dz);
						IBlockState state_ = world.getBlockState(pos_);
                        if (state_.getBlock().isLeaves(state_, world, pos_))
                        {
                            state.getBlock().beginLeavesDecay(state_, world, pos_);
                        }
                    }
                }
            }
        }
    }

	@Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

	@Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	for (int i = 0; i < getNames().length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}
}
