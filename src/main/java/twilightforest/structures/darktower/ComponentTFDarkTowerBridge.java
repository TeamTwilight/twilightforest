package twilightforest.structures.darktower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFDarkTowerBridge extends ComponentTFTowerWing {

	public ComponentTFDarkTowerBridge() {
	}

	private int dSize;
	private int dHeight;

	protected ComponentTFDarkTowerBridge(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, 5, 5, direction);

		this.dSize = pSize;
		this.dHeight = pHeight;
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
		makeTowerWing(list, rand, this.getComponentType(), 4, 1, 2, dSize, dHeight, Rotation.NONE);
	}


	@Override
	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 6) {
			return false;
		}

		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		if (dx[1] + wingHeight > 255) {
			// end of the world!
			return false;
		}

		ComponentTFTowerWing wing = new ComponentTFDarkTowerWing(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(wing);
			wing.buildComponent(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		// make walls
		fillWithBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, deco.blockState, deco.blockState, false);

		// accents
		for (int x = 0; x < size; x++) {
			this.setBlockState(world, deco.accentState, x, 0, 0, sbb);
			this.setBlockState(world, deco.accentState, x, height - 1, 0, sbb);
			this.setBlockState(world, deco.accentState, x, 0, size - 1, sbb);
			this.setBlockState(world, deco.accentState, x, height - 1, size - 1, sbb);
		}

		// nullify sky light
		nullifySkyLightForBoundingBox(world);

		// clear inside
		fillWithAir(world, sbb, 0, 1, 1, size - 1, height - 2, size - 2);


		return true;
	}

	/**
	 * Gets the bounding box of the tower wing we would like to make.
	 *
	 * @return
	 */
	public StructureBoundingBox getWingBB() {
		int[] dest = offsetTowerCoords(4, 1, 2, dSize, this.getCoordBaseMode());
		return StructureTFComponentOld.getComponentToAddBoundingBox(dest[0], dest[1], dest[2], 0, 0, 0, dSize - 1, dHeight - 1, dSize - 1, this.getCoordBaseMode());
	}

}
