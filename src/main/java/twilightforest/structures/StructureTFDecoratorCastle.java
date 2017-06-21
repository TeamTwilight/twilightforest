package twilightforest.structures;

import net.minecraft.block.BlockQuartz;
import net.minecraft.init.Blocks;
import twilightforest.block.BlockTFCastleBlock;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.CastleBrickVariant;

public class StructureTFDecoratorCastle extends StructureTFDecorator {

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.castleBlock.getDefaultState();
		this.accentState = Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);
		this.roofState = TFBlocks.castleBlock.getDefaultState().withProperty(BlockTFCastleBlock.VARIANT, CastleBrickVariant.ROOF);
		this.pillarState = Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Y);
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.QUARTZ_STAIRS.getDefaultState();
		this.randomBlocks = new StructureTFCastleBlocks();
	}

}
