package twilightforest.block;

import java.util.List;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockTFForceField extends BlockPane implements ModelRegisterCallback {

    private static final List<EnumDyeColor> COLORS = ImmutableList.of(EnumDyeColor.PURPLE, EnumDyeColor.PINK, EnumDyeColor.ORANGE, EnumDyeColor.GREEN, EnumDyeColor.BLUE);
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class, COLORS);
	
    protected BlockTFForceField() {
        super(Material.GRASS, false);
		this.setLightLevel(2F / 15F);
		this.setCreativeTab(TFItems.creativeTab);
        this.setDefaultState(blockState.getBaseState()
            .withProperty(NORTH, false).withProperty(SOUTH, false)
            .withProperty(WEST, false).withProperty(EAST, false)
            .withProperty(COLOR, EnumDyeColor.PURPLE));
	}

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, COLOR);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return COLORS.indexOf(state.getValue(COLOR));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(COLOR, COLORS.get(meta));
    }

    //@SideOnly(Side.CLIENT)
    //public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
    //	return 15 << 20 | 15 << 4;
    //}

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }
    
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < COLOR.getAllowedValues().size(); i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity)
    {
        // fill in the whole bounding box when we connect on all sides
        if (state.getValue(NORTH) && state.getValue(SOUTH) & state.getValue(WEST) && state.getValue(EAST)) {
            addCollisionBoxToList(pos, aabb, list, FULL_BLOCK_AABB);
        } else {
            super.addCollisionBoxToList(state, world, pos, aabb, list, entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        for (int i = 0; i < COLORS.size(); i++) {
            String variant = "inventory_" + COLORS.get(i).getName();
            ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), variant);
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, mrl);
        }
    }
}
