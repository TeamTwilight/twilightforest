package twilightforest.structures.mushroomtower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class ComponentTFMushroomTowerMainBridge extends ComponentTFMushroomTowerBridge {

	public ComponentTFMushroomTowerMainBridge() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected ComponentTFMushroomTowerMainBridge(int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		// bridge only 11 long
		super(i, x, y, z, 11, pHeight, direction);
	}

	@Override
	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		// make a new size 15 main tower
		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 15, direction);

		// adjust coordinates to fit an existing tower
		dx = adjustCoordinates(dx[0], dx[1], dx[2], 15, direction, list);

		ComponentTFMushroomTowerMain wing = new ComponentTFMushroomTowerMain(index, dx[0], dx[1], dx[2], 15, wingHeight, direction);

		list.add(wing);
		wing.buildComponent((StructureComponent) list.get(0), list, rand);
		addOpening(x, y, z, rotation);

		return true;
	}


}
