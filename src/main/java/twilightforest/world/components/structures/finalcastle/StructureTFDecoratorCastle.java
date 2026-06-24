package twilightforest.world.components.structures.finalcastle;

import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Autowired;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;
import twilightforest.world.components.structures.selectors.CastleRandomBlockSelectorFactory;

public class StructureTFDecoratorCastle extends TFStructureDecorator {
	@Autowired
	private static CastleRandomBlockSelectorFactory castle;

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.CASTLE_BRICK.get().defaultBlockState();
		this.accentState = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
		this.roofState = TFBlocks.CASTLE_ROOF_TILE.get().defaultBlockState();
		this.pillarState = TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.QUARTZ_STAIRS.defaultBlockState();
		this.randomBlocks = castle.make();
	}

}
