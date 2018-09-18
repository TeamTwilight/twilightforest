package twilightforest.structures.stronghold;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFUnderBrick;
import twilightforest.block.TFBlocks;
import twilightforest.enums.UnderBrickVariant;

import java.util.Random;

public class StructureTFKnightStones extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random random, int x, int y, int z, boolean edge) {
		if (!edge) {
			this.blockstate = Blocks.AIR.getDefaultState();
		} else {
			float f = random.nextFloat();

			if (f < 0.2F) {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.CRACKED);
			} else if (f < 0.5F) {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.MOSSY);
			} else {
				this.blockstate = TFBlocks.underbrick.getDefaultState().withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.NORMAL);
			}
		}
	}

}
