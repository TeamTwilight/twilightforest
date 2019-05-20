package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockQuartz;
import net.minecraft.init.Blocks;
import twilightforest.block.BlockTFCastleBlock;
import twilightforest.block.BlockTFCastlePillar;
import twilightforest.block.TFBlocks;
import twilightforest.enums.CastleBrickVariant;
import twilightforest.enums.CastlePillarVariant;
import twilightforest.structures.StructureTFDecorator;

public class StructureTFDecoratorCastle extends StructureTFDecorator {

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.castle_brick.getDefaultState();
		this.accentState = Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);
		this.roofState = TFBlocks.castle_brick.getDefaultState().withProperty(BlockTFCastleBlock.VARIANT, CastleBrickVariant.ROOF);
		this.pillarState = TFBlocks.castle_pillar.getDefaultState().withProperty(BlockTFCastlePillar.VARIANT, CastlePillarVariant.BOLD);
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = TFBlocks.castle_stairs_brick.getDefaultState();
		this.randomBlocks = new StructureTFCastleBlocks();
	}

}
