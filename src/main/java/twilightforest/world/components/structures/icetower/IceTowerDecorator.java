package twilightforest.world.components.structures.icetower;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;

public class IceTowerDecorator extends TFStructureDecorator {

	public IceTowerDecorator() {
		this.blockState = TFBlocks.AURORA_BLOCK.get().defaultBlockState();
		this.accentState = Blocks.BIRCH_PLANKS.defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.BIRCH_STAIRS.defaultBlockState();
		this.pillarState = TFBlocks.AURORA_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		this.platformState = Blocks.BIRCH_SLAB.defaultBlockState();
		this.floorState = Blocks.BIRCH_PLANKS.defaultBlockState();
		this.randomBlocks = new IceTowerProcessor();
	}

}
