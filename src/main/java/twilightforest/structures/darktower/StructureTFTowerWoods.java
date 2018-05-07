package twilightforest.structures.darktower;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFTowerWood;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerWoodVariant;

import java.util.Random;

public class StructureTFTowerWoods extends StructureComponent.BlockSelector {

	@Override
	public void selectBlocks(Random par1Random, int x, int y, int z, boolean isWall) {
		if (!isWall) {
			this.blockstate = Blocks.AIR.getDefaultState();
		} else {
			float randFloat = par1Random.nextFloat();

			if (randFloat < 0.1F) {
				this.blockstate = TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.CRACKED);
			} else if (randFloat < 0.2F) {
				this.blockstate = TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.MOSSY);
			} else if (randFloat < 0.225F) {
				this.blockstate = TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.INFESTED);
			} else {
				this.blockstate = TFBlocks.tower_wood.getDefaultState();
			}
		}
	}

}
