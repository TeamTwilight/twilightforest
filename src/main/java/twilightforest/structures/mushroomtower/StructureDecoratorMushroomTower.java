package twilightforest.structures.mushroomtower;

import net.minecraft.block.Blocks;
import net.minecraft.block.HugeMushroomBlock;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorMushroomTower extends StructureTFDecorator {

	public StructureDecoratorMushroomTower() {
		this.blockState = Blocks.MUSHROOM_STEM.getDefaultState().with(HugeMushroomBlock.UP, false).with(HugeMushroomBlock.DOWN, false);
		this.accentState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.SPRUCE_STAIRS.getDefaultState();
		this.pillarState = Blocks.MUSHROOM_STEM.getDefaultState().with(HugeMushroomBlock.UP, false).with(HugeMushroomBlock.DOWN, false);
		this.floorState = Blocks.OAK_PLANKS.getDefaultState();
	}
}
