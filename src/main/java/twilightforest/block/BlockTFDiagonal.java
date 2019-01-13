package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFDiagonal extends Block implements ModelRegisterCallback {
    public static final PropertyBool IS_ROTATED = PropertyBool.create("is_rotated");

    public BlockTFDiagonal(Material materialIn) {
        super(materialIn);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_ROTATED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(IS_ROTATED, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IS_ROTATED) ? 1 : 0;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return rot == Rotation.NONE || rot == Rotation.CLOCKWISE_180 ? state : state.withProperty(IS_ROTATED, !state.getValue(IS_ROTATED));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return mirrorIn == Mirror.NONE ? state : state.withProperty(IS_ROTATED, !state.getValue(IS_ROTATED));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(IS_ROTATED, EnumFacing.byHorizontalIndex(MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F) & 1) == EnumFacing.WEST);
    }
}
