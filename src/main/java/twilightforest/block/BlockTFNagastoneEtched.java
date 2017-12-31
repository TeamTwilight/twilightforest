package twilightforest.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

public class BlockTFNagastoneEtched extends BlockDirectional implements ModelRegisterCallback {
    protected BlockTFNagastoneEtched() {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TFItems.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.isSneaking() ? facing.getOpposite() : facing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta % EnumFacing.values().length]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelUtils.registerToState(this, 0, this.getDefaultState());
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
