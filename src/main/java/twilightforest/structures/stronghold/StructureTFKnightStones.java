package twilightforest.structures.stronghold;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFUnderBrick;
import twilightforest.block.TFBlocks;
import twilightforest.enums.UnderBrickVariant;

import java.util.Random;

public class StructureTFKnightStones extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random par1Random, int par2, int par3, int par4, boolean par5) {
		if (!par5) {
			this.blockstate = Blocks.AIR.getDefaultState();
		} else {
			float var6 = par1Random.nextFloat();

			if (var6 < 0.2F) {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.CRACKED);
			} else if (var6 < 0.5F) {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.MOSSY);
			} else {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.NORMAL);
			}
		}
	}

}
