package twilightforest.world.components.structures.darktower;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TowerwoodProcessor extends StructurePiece.BlockSelector {

	@Override
	public void next(Random random, int x, int y, int z, boolean isWall) {
		if (!isWall) {
			this.next = Blocks.AIR.defaultBlockState();
		} else {
			float randFloat = random.nextFloat();

			if (randFloat < 0.1F) {
				this.next = TFBlocks.CRACKED_TOWERWOOD.get().defaultBlockState();
			} else if (randFloat < 0.2F) {
				this.next = TFBlocks.MOSSY_TOWERWOOD.get().defaultBlockState();
			} else if (randFloat < 0.225F) {
				this.next = TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState();
			} else {
				this.next = TFBlocks.TOWERWOOD.get().defaultBlockState();
			}
		}
	}

}
