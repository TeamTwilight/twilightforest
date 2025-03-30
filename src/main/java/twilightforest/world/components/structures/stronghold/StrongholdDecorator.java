package twilightforest.world.components.structures.stronghold;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import tamaized.beanification.Autowired;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;
import twilightforest.world.components.structures.selectors.KnightStonesRandomBlockSelectorFactory;

public class StrongholdDecorator extends TFStructureDecorator {
	@Autowired
	private static KnightStonesRandomBlockSelectorFactory knightStones;

	public StrongholdDecorator() {
		this.blockState = TFBlocks.UNDERBRICK.get().defaultBlockState();
		this.accentState = TFBlocks.CRACKED_UNDERBRICK.get().defaultBlockState();
		this.fenceState = Blocks.COBBLESTONE_WALL.defaultBlockState();
		this.stairState = Blocks.STONE_BRICK_STAIRS.defaultBlockState();
		this.pillarState = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
		this.platformState = Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP);
		this.randomBlocks = knightStones.make();
	}
}
