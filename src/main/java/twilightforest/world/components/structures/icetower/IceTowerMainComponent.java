package twilightforest.world.components.structures.icetower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;


public class IceTowerMainComponent extends IceTowerWingComponent {
	public boolean hasBossWing = false;

	public IceTowerMainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFITMai.get(), nbt);
		this.hasBossWing = nbt.getBooleanOr("hasBossWing", false);
	}

	public IceTowerMainComponent(RandomSource rand, int index, int x, int y, int z) {
		this(rand, index, x + SIZE, y + 40, z + SIZE, Direction.NORTH);
	}

	public IceTowerMainComponent(RandomSource rand, int index, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFITMai.get(), index, x, y, z, SIZE, 31 + (rand.nextInt(3) * 10), rotation);

		// decorator
		if (this.deco == null) {
			this.deco = new IceTowerDecorator();
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("hasBossWing", this.hasBossWing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePiecesBuilder list, RandomSource rand) {
		while (!((IceTowerMainComponent) parent).hasBossWing) {
			rand.setSeed(rand.nextLong());
			openings.clear();
			addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
			list.pieces.clear();
			list.pieces.add(parent);

			// add entrance tower
			BoundingBox towerBB = BoundingBoxUtils.clone(this.boundingBox);

			for (StructurePiece structurecomponent : list.pieces)
				towerBB.encapsulate(structurecomponent.getBoundingBox());

			// TODO: make this more general
			BlockPos myDoor = this.openings.getFirst();
			BlockPos entranceDoor = myDoor;


			if (myDoor.getX() == 0) {
				int length = 30 + rand.nextInt(20);
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
			super.addChildren(parent, list, rand);
		}
	}

	private void makeEntranceBridge(StructurePiecesBuilder list, RandomSource rand, int index, int x, int y, int z, int length, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, 5, direction);

		IceTowerBridgeComponent bridge = new IceTowerBridgeComponent(index, dest.getX(), dest.getY(), dest.getZ(), length, 4, direction);

		list.addPiece(bridge);
		bridge.addChildren(list.pieces.getFirst(), list, rand);
	}

	public boolean makeEntranceTower(StructurePiecesBuilder list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		IceTowerWingComponent entrance = new IceTowerEntranceComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);

		list.addPiece(entrance);
		entrance.addChildren(list.pieces.getFirst(), list, rand);
		addOpening(x, y, z, rotation);
		return true;
	}
}
