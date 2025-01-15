package twilightforest.world.components.structures.finalcastle;

import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelectorFactory;

public class StructureTFDecoratorCastle extends TFStructureDecorator {

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.CASTLE_BRICK.get().defaultBlockState();
		this.accentState = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
		this.roofState = TFBlocks.CASTLE_ROOF_TILE.get().defaultBlockState();
		this.pillarState = TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.QUARTZ_STAIRS.defaultBlockState();
		this.randomBlocks = SimpleRandomBlockSelectorFactory.getCastleBlocks();
	}

}
