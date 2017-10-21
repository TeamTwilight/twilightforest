package twilightforest.structures.lichtower;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;


public class ComponentTFTowerRoofFence extends ComponentTFTowerRoof {

	public ComponentTFTowerRoofFence() {
		super();
	}

	public ComponentTFTowerRoofFence(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = 0;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}

	/**
	 * A fence around the roof!
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		int y = height + 1;
		for (int x = 0; x <= size - 1; x++) {
			for (int z = 0; z <= size - 1; z++) {
				if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
					setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x, y, z, sbb);
				}
			}
		}
		return true;
	}

}
