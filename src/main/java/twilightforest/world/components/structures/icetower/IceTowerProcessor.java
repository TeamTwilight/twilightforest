package twilightforest.world.components.structures.icetower;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.init.TFBlocks;

public class IceTowerProcessor extends StructurePiece.BlockSelector {

	@Override
	public void next(RandomSource random, int x, int y, int z, boolean wall) {
		if (!wall) {
			this.next = Blocks.AIR.defaultBlockState();
		} else {
			this.next = TFBlocks.AURORA_BLOCK.get().defaultBlockState();
		}
	}
}
