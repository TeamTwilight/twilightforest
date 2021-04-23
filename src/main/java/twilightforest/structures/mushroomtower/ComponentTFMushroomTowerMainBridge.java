package twilightforest.structures.mushroomtower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFMushroomTowerMainBridge extends ComponentTFMushroomTowerBridge {

	public ComponentTFMushroomTowerMainBridge(TemplateManager manager, CompoundNBT nbt) {
		super(TFMushroomTowerPieces.TFMTMB, nbt);
	}

	protected ComponentTFMushroomTowerMainBridge(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		// bridge only 11 long
		super(TFMushroomTowerPieces.TFMTMB, feature, i, x, y, z, 11, pHeight, direction);
	}

	@Override
	public boolean makeTowerWing(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		// make a new size 15 main tower
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 15, direction);

		// adjust coordinates to fit an existing tower
		dx = adjustCoordinates(dx[0], dx[1], dx[2], 15, direction, list);

		ComponentTFMushroomTowerMain wing = new ComponentTFMushroomTowerMain(getFeatureType(), index, dx[0], dx[1], dx[2], 15, wingHeight, direction);

		list.add(wing);
		wing.buildComponent(list.get(0), list, rand);
		addOpening(x, y, z, rotation);

		return true;
	}
}
