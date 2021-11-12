package twilightforest.world.components.structures.finalcastle;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class CastleBlockProcessor extends StructurePiece.BlockSelector {

	@Override
	public void next(Random random, int x, int y, int z, boolean isWall) {
		if (!isWall) {
			next = Blocks.AIR.defaultBlockState();
		} else {
			float randFloat = random.nextFloat();

			if (randFloat < 0.1F) {
				next = TFBlocks.WORN_CASTLE_BRICK.get().defaultBlockState();
			} else if (randFloat < 0.2F) {
				next = TFBlocks.CRACKED_CASTLE_BRICK.get().defaultBlockState();
			} else {
				next = TFBlocks.CASTLE_BRICK.get().defaultBlockState();
			}
		}
	}

}
