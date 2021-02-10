package twilightforest.structures.darktower;

import net.minecraft.block.Blocks;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorDarkTower extends StructureTFDecorator {

	public StructureDecoratorDarkTower() {
		this.blockState = TFBlocks.tower_wood.get().getDefaultState();
		this.accentState = TFBlocks.tower_wood_encased.get().getDefaultState();
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.SPRUCE_STAIRS.getDefaultState();
		this.pillarState = TFBlocks.tower_wood_encased.get().getDefaultState();
		this.platformState = TFBlocks.tower_wood_encased.get().getDefaultState();
		this.randomBlocks = new StructureTFTowerWoods();
	}

}
