package twilightforest.world.components.structures.icetower;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.block.AuroraBrickBlock;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class IceTowerProcessor extends StructurePiece.BlockSelector {

	@Override
	public void next(Random random, int x, int y, int z, boolean wall) {
		if (!wall) {
			this.next = Blocks.AIR.defaultBlockState();
		} else {
			this.next = TFBlocks.AURORA_BLOCK.get().defaultBlockState().setValue(
					AuroraBrickBlock.VARIANT,
					Math.abs(x + z) % 16
			);
		}
	}

}
