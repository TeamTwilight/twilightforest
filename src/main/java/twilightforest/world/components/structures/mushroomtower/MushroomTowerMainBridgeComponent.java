package twilightforest.world.components.structures.mushroomtower;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class MushroomTowerMainBridgeComponent extends MushroomTowerBridgeComponent {

	public MushroomTowerMainBridgeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(MushroomTowerPieces.TFMTMB, nbt);
	}

	protected MushroomTowerMainBridgeComponent(TFFeature feature, int i, int x, int y, int z, int pHeight, Direction direction) {
		// bridge only 11 long
		super(MushroomTowerPieces.TFMTMB, feature, i, x, y, z, 11, pHeight, direction);
	}

	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		// make a new size 15 main tower
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 15, direction);

		// adjust coordinates to fit an existing tower
		dx = adjustCoordinates(dx[0], dx[1], dx[2], 15, direction, list);

		MushroomTowerMainComponent wing = new MushroomTowerMainComponent(getFeatureType(), index, dx[0], dx[1], dx[2], 15, wingHeight, direction);

		list.addPiece(wing);
		if (list instanceof StructurePiecesBuilder start) {
			wing.addChildren(start.pieces.get(0), list, rand);
		}
		addOpening(x, y, z, rotation);

		return true;
	}
}
