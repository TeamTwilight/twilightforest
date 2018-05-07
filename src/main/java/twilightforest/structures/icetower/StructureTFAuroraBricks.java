package twilightforest.structures.icetower;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFAuroraBrick;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class StructureTFAuroraBricks extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random par1Random, int x, int y, int z, boolean wall) {
		if (!wall) {
			this.blockstate = Blocks.AIR.getDefaultState();
		} else {
			this.blockstate = TFBlocks.aurora_block.getDefaultState().withProperty(
					BlockTFAuroraBrick.VARIANT,
					Math.abs(x + z) % 16
			);
		}
	}

}
