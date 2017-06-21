package twilightforest.structures.mushroomtower;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.init.Blocks;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorMushroomTower extends StructureTFDecorator {


	public StructureDecoratorMushroomTower() {
		this.blockState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
		this.accentState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE);
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.SPRUCE_STAIRS.getDefaultState();
		this.pillarState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
		this.floorState = Blocks.PLANKS.getDefaultState();
	}

}
