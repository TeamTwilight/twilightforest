package twilightforest.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;

public class BlockTFStairs extends BlockStairs implements ModelRegisterCallback {
    protected BlockTFStairs(IBlockState modelState) {
        super(modelState);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelUtils.registerToState(this, 0, getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
    }
}
