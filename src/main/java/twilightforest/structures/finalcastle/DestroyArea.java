package twilightforest.structures.finalcastle;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.ArrayList;
import java.util.Random;

/**
 * An area that we're going to destroy.  Default is just a StructureBoundingBox
 */
public class DestroyArea {

	StructureBoundingBox destroyBox;

	public DestroyArea(StructureBoundingBox tower, Random rand, int y) {
		// make a 4x4 area that's entirely within the tower bounding box

		int bx = tower.minX - 2 + rand.nextInt(tower.getXSize());
		int bz = tower.minZ - 2 + rand.nextInt(tower.getZSize());

		this.destroyBox = new StructureBoundingBox(bx, y - 10, bz, bx + 4, y, bz + 4);
	}

	public boolean isEntirelyAbove(int y) {
		return this.destroyBox.minY > y;
	}

	public boolean isVecInside(BlockPos pos) {
		return destroyBox.isVecInside(pos);
	}

	/**
	 * construct a new area that does not intersect any other areas in the list
	 */
	public static DestroyArea createNonIntersecting(StructureBoundingBox tower, Random rand, int y, ArrayList<DestroyArea> otherAreas) {
		int attempts = 100;

		DestroyArea area = null;
		for (int i = 0; i < attempts && area == null; i++) {
			DestroyArea testArea = new DestroyArea(tower, rand, y);

			if (otherAreas.size() == 0) {
				area = testArea;
			} else {
				for (DestroyArea otherArea : otherAreas) {
					if (otherArea == null || !testArea.intersectsWith(otherArea)) {
						area = testArea;
					}
				}
			}
		}
		return area;
	}

	/**
	 * We check if the box would intersect even if it was one block larger in the x and z directions
	 */
	private boolean intersectsWith(DestroyArea otherArea) {
		return this.destroyBox.intersectsWith(otherArea.destroyBox.minX - 1, otherArea.destroyBox.minZ - 1, otherArea.destroyBox.maxX + 1, otherArea.destroyBox.maxX + 1);
	}
}
