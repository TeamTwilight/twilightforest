package twilightforest.world.components.structures.finalcastle;

import net.minecraft.world.level.block.Blocks;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;

public class StructureTFDecoratorCastle extends TFStructureDecorator {

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.CASTLE_BRICK.get().defaultBlockState();
		this.accentState = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
		this.roofState = TFBlocks.CASTLE_ROOF_TILE.get().defaultBlockState();
		this.pillarState = TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = TFBlocks.CASTLE_BRICK_STAIRS.get().defaultBlockState();
		this.randomBlocks = new CastleBlockProcessor();
	}

}
