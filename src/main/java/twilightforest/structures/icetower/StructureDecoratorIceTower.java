package twilightforest.structures.icetower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorIceTower extends StructureTFDecorator {

	public StructureDecoratorIceTower() {
		this.blockState = TFBlocks.auroraBlock.getDefaultState();
		this.accentState = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.BIRCH_STAIRS.getDefaultState();
		this.pillarState = TFBlocks.auroraPillar.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
		this.platformState = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH);
		this.floorState = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		this.randomBlocks = new StructureTFAuroraBricks();
	}

}
