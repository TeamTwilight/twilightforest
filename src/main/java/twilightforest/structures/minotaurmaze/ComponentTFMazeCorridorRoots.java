package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;

import java.util.Random;

public class ComponentTFMazeCorridorRoots extends ComponentTFMazeCorridor {

	public ComponentTFMazeCorridorRoots() {
		super();
	}

	public ComponentTFMazeCorridorRoots(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		for (int x = 1; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				int freq = x;
				if (rand.nextInt(freq + 2) > 0) {
					int length = rand.nextInt(6);

					//place dirt above ceiling
					this.setBlockState(world, Blocks.DIRT.getDefaultState(), x, 6, z, sbb);

					// roots
					for (int y = 6 - length; y < 6; y++) {
						this.setBlockState(world, TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.ROOT_STRAND), x, y, z, sbb);
					}

					// occasional gravel
					if (rand.nextInt(freq + 1) > 1) {
						this.setBlockState(world, Blocks.GRAVEL.getDefaultState(), x, 1, z, sbb);

						if (rand.nextInt(freq + 1) > 1) {
							this.setBlockState(world, Blocks.GRAVEL.getDefaultState(), x, 2, z, sbb);
						}
					}
				}
			}
		}
		return true;
	}

}
