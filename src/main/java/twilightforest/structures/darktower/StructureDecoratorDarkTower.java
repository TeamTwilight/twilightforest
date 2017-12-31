package twilightforest.structures.darktower;

import net.minecraft.init.Blocks;
import twilightforest.block.BlockTFTowerWood;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerWoodVariant;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorDarkTower extends StructureTFDecorator {

	public StructureDecoratorDarkTower() {
		this.blockState = TFBlocks.towerWood.getDefaultState();
		this.accentState = TFBlocks.towerWood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.ENCASED);
		this.fenceState = Blocks.OAK_FENCE.getDefaultState();
		this.stairState = Blocks.SPRUCE_STAIRS.getDefaultState();
		this.pillarState = TFBlocks.towerWood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.ENCASED);
		this.platformState = TFBlocks.towerWood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.ENCASED);
		this.randomBlocks = new StructureTFTowerWoods();
	}

}
