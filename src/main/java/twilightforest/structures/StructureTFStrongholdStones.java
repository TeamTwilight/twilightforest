package twilightforest.structures;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureTFStrongholdStones extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random par1Random, int par2, int par3, int par4, boolean par5) {
        if (!par5)
        {
            blockstate = Blocks.AIR.getDefaultState();
        }
        else
        {
            float var6 = par1Random.nextFloat();

            if (var6 < 0.2F)
            {
                blockstate = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
            }
            else if (var6 < 0.5F)
            {
                blockstate = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
            }
            else if (var6 < 0.55F)
            {
                blockstate = Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONEBRICK);
            }
            else
            {
                blockstate = Blocks.STONEBRICK.getDefaultState();
            }
        }
	}

}
