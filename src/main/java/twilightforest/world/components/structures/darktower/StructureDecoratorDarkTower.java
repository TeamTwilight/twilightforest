package twilightforest.world.components.structures.darktower;

import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelectorFactory;

public class StructureDecoratorDarkTower extends TFStructureDecorator {

	public StructureDecoratorDarkTower() {
		this.blockState = TFBlocks.TOWERWOOD.get().defaultBlockState();
		this.accentState = TFBlocks.ENCASED_TOWERWOOD.get().defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.SPRUCE_STAIRS.defaultBlockState();
		this.pillarState = TFBlocks.ENCASED_TOWERWOOD.get().defaultBlockState();
		this.platformState = TFBlocks.ENCASED_TOWERWOOD.get().defaultBlockState();
		this.randomBlocks = SimpleRandomBlockSelectorFactory.getTowerwood();
	}

}
