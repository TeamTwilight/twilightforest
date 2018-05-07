package twilightforest.structures;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFCastleBlock;
import twilightforest.block.TFBlocks;
import twilightforest.enums.CastleBrickVariant;

import java.util.Random;

public class StructureTFCastleBlocks extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random par1Random, int x, int y, int z, boolean isWall) {
		if (!isWall) {
			blockstate = Blocks.AIR.getDefaultState();
		} else {
			float randFloat = par1Random.nextFloat();
			CastleBrickVariant variant = null;

			if (randFloat < 0.1F) {
				variant = CastleBrickVariant.WORN;
			} else if (randFloat < 0.2F) {
				variant = CastleBrickVariant.CRACKED;
			} else {
				variant = CastleBrickVariant.NORMAL;
			}

			blockstate = TFBlocks.castle_brick.getDefaultState().withProperty(BlockTFCastleBlock.VARIANT, variant);
		}
	}

}
