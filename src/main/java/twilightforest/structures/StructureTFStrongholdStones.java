package twilightforest.structures;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Random;

public class StructureTFStrongholdStones extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random random, int x, int y, int z, boolean wall) {
		if (!wall) {
			blockstate = Blocks.AIR.getDefaultState();
		} else {
			float f = random.nextFloat();

			if (f < 0.2F) {
				blockstate = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
			} else if (f < 0.5F) {
				blockstate = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
			} else if (f < 0.55F) {
				blockstate = Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONEBRICK);
			} else {
				blockstate = Blocks.STONEBRICK.getDefaultState();
			}
		}
	}

}
