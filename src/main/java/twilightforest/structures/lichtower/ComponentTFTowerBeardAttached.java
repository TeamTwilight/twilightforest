package twilightforest.structures.lichtower;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;


public class ComponentTFTowerBeardAttached extends ComponentTFTowerBeard {

	public ComponentTFTowerBeardAttached() {
		super();
	}


	public ComponentTFTowerBeardAttached(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// just hang out at the very bottom of the tower
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().minY - this.height - 1, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().minY - 1, wing.getBoundingBox().maxZ);

	}


	/**
	 * Makes a pyramid-shaped beard
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		return makeAttachedBeard(world, rand, sbb);
	}

	private boolean makeAttachedBeard(World world, Random rand, StructureBoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = y + 1;
			int max = size - y;

			fillWithRandomizedBlocks(world, sbb, 0, height - y, min, max, height - y, max, false, rand, StructureTFComponentOld.getStrongholdStones());
		}
		return true;
	}

}
