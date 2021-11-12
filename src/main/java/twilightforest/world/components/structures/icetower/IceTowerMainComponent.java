package twilightforest.world.components.structures.icetower;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class IceTowerMainComponent extends IceTowerWingComponent {
	public boolean hasBossWing = false;

	public IceTowerMainComponent(ServerLevel level, CompoundTag nbt) {
		super(IceTowerPieces.TFITMai, nbt);
		this.hasBossWing = nbt.getBoolean("hasBossWing");
	}

	public IceTowerMainComponent(TFFeature feature, Random rand, int index, int x, int y, int z) {
		this(feature, rand, index, x + SIZE, y + 40, z + SIZE, Direction.NORTH);
	}

	public IceTowerMainComponent(TFFeature feature, Random rand, int index, int x, int y, int z, Direction rotation) {
		super(IceTowerPieces.TFITMai, feature, index, x, y, z, SIZE, 31 + (rand.nextInt(3) * 10), rotation);

		// decorator
		if (this.deco == null) {
			this.deco = new IceTowerDecorator();
		}
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);
		tagCompound.putBoolean("hasBossWing", this.hasBossWing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		super.addChildren(parent, list, rand);

		// add entrance tower
		BoundingBox towerBB = new BoundingBox(this.boundingBox.getCenter());

		if (list instanceof StructureStart<?> start)
			for (StructurePiece structurecomponent : start.getPieces())
				towerBB.encapsulate(structurecomponent.getBoundingBox());

		// TODO: make this more general
		BlockPos myDoor = this.openings.get(0);
		BlockPos entranceDoor = new BlockPos(myDoor);


		if (myDoor.getX() == 0) {
			int length = this.getBoundingBox().minX() - towerBB.minX();
			if (length >= 0) {
				entranceDoor = entranceDoor.west(length);
				makeEntranceBridge(list, rand, this.getGenDepth() + 1, myDoor.getX(), myDoor.getY(), myDoor.getZ(), length, Rotation.CLOCKWISE_180);
			}
		}
		if (myDoor.getX() == this.size - 1) {
			entranceDoor = entranceDoor.east(towerBB.maxX() - this.getBoundingBox().maxX());
		}
		if (myDoor.getZ() == 0) {
			entranceDoor = entranceDoor.south(towerBB.minZ() - this.getBoundingBox().minZ());
		}
		//FIXME: AtomicBlom I don't get it, should this not be getZ, and entranceDoor.north?
		if (myDoor.getX() == this.size - 1) {
			entranceDoor = entranceDoor.south(towerBB.maxZ() - this.getBoundingBox().maxZ());
		}

		makeEntranceTower(list, rand, this.getGenDepth() + 1, entranceDoor.getX(), entranceDoor.getY(), entranceDoor.getZ(), SIZE, 11, this.rotation);
	}

	private void makeEntranceBridge(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, int length, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, 5, direction);

		IceTowerBridgeComponent bridge = new IceTowerBridgeComponent(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), length, direction);

		list.addPiece(bridge);
		if (list instanceof StructureStart<?> start) {
			bridge.addChildren(start.getPieces().get(0), list, rand);
		}
	}

	public boolean makeEntranceTower(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		IceTowerWingComponent entrance = new IceTowerEntranceComponent(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);

		list.addPiece(entrance);
		if (list instanceof StructureStart<?> start) {
			entrance.addChildren(start.getPieces().get(0), list, rand);
		}
		addOpening(x, y, z, rotation);
		return true;
	}
}
