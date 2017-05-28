package twilightforest.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

public class BlockTFUnripeTorchCluster extends BlockTFTrollRoot {
	
	private static final int RIPEN_THRESHHOLD = 6;

	@Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);

        if (world.getLight(pos) >= RIPEN_THRESHHOLD) {
        	// ripen!
        	world.setBlockState(pos, TFBlocks.trollBer.getDefaultState());
        }
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
