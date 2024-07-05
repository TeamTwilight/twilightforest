package twilightforest.world.components.structures.mushroomtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerRoofComponent;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;


public class MushroomTowerWingComponent extends TowerWingComponent {

	private static final int RANGE = 200;
	protected static final int FLOOR_HEIGHT = 4;
	protected static final int MAIN_SIZE = 15;

	boolean hasBase = false;
	public boolean isAscender = false;

	public MushroomTowerWingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFMTWin.get(), nbt);
	}

	public MushroomTowerWingComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.hasBase = nbt.getBoolean("hasBase");
		this.isAscender = nbt.getBoolean("isAscender");
	}

	protected MushroomTowerWingComponent(StructurePieceType piece, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(piece, i, x, y, z, pSize, pHeight, direction);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);

		tagCompound.putBoolean("hasBase", this.hasBase);
		tagCompound.putBoolean("isAscender", this.isAscender);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// should we build a base?
		this.hasBase = size > 3;

		// if we are an ascender, build the destination tower
		if (this.isAscender) {

			int[] dest = getValidOpening(rand, Rotation.CLOCKWISE_180);
			dest[1] = this.height - 3;
			int childHeight = (rand.nextInt(3) + rand.nextInt(3) + 2) * FLOOR_HEIGHT + 1;
			boolean madeIt = makeMainBridge(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], childHeight, Rotation.CLOCKWISE_180);

			if (!madeIt) {
				TwilightForestMod.LOGGER.info("Did not make bridge back to new main");
			} else {
				TwilightForestMod.LOGGER.debug("Made bridge back to new main");
			}
		}

		// limit sprawl to a reasonable amount
		if (this.getGenDepth() < 5 && this.size > 6) {
			// make sub towers
			//for (int i = 0; i < 4; i++) {
			for (Rotation i : RotationUtil.ROTATIONS) {

				if (this.size < MAIN_SIZE && i == Rotation.CLOCKWISE_180) {
					continue;
				}

				int[] dest = getValidOpening(rand, i);

				int childHeight = (rand.nextInt(2) + rand.nextInt(2) + 2) * FLOOR_HEIGHT + 1;

				makeBridge(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], size - 4, childHeight, i);
			}
		}

		if (this.isHighest(this.boundingBox, this.size, list) || !this.hasBase) {
			// add a roof?
			makeARoof(parent, list, rand);
		}
	}

	/**
	 * Have we strayed more than range blocks away from the center?
	 */
	private boolean isOutOfRange(StructurePiece parent, int nx, int nz, int range) {
		final BoundingBox sbb = parent.getBoundingBox();
		final int centerX = sbb.minX() + (sbb.maxX() - sbb.minX() + 1) / 2;
		final int centerZ = sbb.minZ() + (sbb.maxZ() - sbb.minZ() + 1) / 2;
		return Math.abs(nx - centerX) > range
			|| Math.abs(nz - centerZ) > range;
	}

	/**
	 * Make a new wing
	 */
	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		// stop if out of range
		if (isOutOfRange(this, dx[0], dx[2], RANGE)) {
			return false;
		}

		// adjust coordinates to fit an existing tower
		if (wingSize > 3) {
			dx = adjustCoordinates(dx[0], dx[1], dx[2], wingSize, direction, list);
		}

		MushroomTowerWingComponent wing = new MushroomTowerWingComponent(TFStructurePieceTypes.TFMTWin.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(wing.getBoundingBox());
		if (intersect == null || intersect == this || intersect instanceof TowerRoofMushroomComponent) {

			// if we are coming from an ascender bridge, mark the destination component
			if (this instanceof MushroomTowerBridgeComponent && this.isAscender) {
				wing.isAscender = true;
			}

			list.addPiece(wing);
			wing.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adjust the coordinates for this tower to link up with any others within 3
	 */
	protected int[] adjustCoordinates(int x, int y, int z, int wingSize, Direction direction, StructurePieceAccessor list) {
		// go through list.  if there are any same size towers within wingSize, return their xyz instead
		if (list instanceof StructurePiecesBuilder start) {
			for (StructurePiece obj : start.pieces) {
				if (obj instanceof TowerWingComponent otherWing && !(obj instanceof MushroomTowerBridgeComponent)) {

					if (wingSize == otherWing.size && otherWing.getBoundingBox().intersects(x - 3, z - 3, x + 3, z + 3)) {
						return switch (direction) {
							case SOUTH -> new int[]{otherWing.getBoundingBox().minX(), y, otherWing.getBoundingBox().minZ()};
							case WEST -> new int[]{otherWing.getBoundingBox().maxX(), y, otherWing.getBoundingBox().minZ()};
							case NORTH -> new int[]{otherWing.getBoundingBox().maxX(), y, otherWing.getBoundingBox().maxZ()};
							case EAST -> new int[]{otherWing.getBoundingBox().minX(), y, otherWing.getBoundingBox().maxZ()};
							default -> new int[]{x, y, z};
						};
					}
				}
			}
		}

		return new int[]{x, y, z};
	}

	/**
	 * Are there (not) any other towers below this bounding box?
	 */
	private boolean isHighest(BoundingBox boundingBox, int size, StructurePieceAccessor list) {
		// go through list.  if there are any same size towers within wingSize, return their xyz instead

		BoundingBox boxAbove = new BoundingBox(
			boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(),
			boundingBox.maxX(), 256, boundingBox.maxZ()
		);

		if (list instanceof StructurePiecesBuilder start) {
			for (StructurePiece obj : start.pieces) {
				if (this != obj && obj instanceof TowerWingComponent otherWing && !(obj instanceof MushroomTowerBridgeComponent)) {

					if (size == otherWing.size && otherWing.getBoundingBox().intersects(boxAbove)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Make a mushroom roof!
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {

		TowerRoofComponent roof = new TowerRoofMushroomComponent(this.getGenDepth() + 1, this, 1.6F, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		if (!(list.findCollisionPiece(roof.getBoundingBox()) instanceof TowerRoofMushroomComponent)) {
			roof = new TowerRoofMushroomComponent(this.getGenDepth() + 1, this, 1.0F, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			if (!(list.findCollisionPiece(roof.getBoundingBox()) instanceof TowerRoofMushroomComponent)) {
				roof = new TowerRoofMushroomComponent(this.getGenDepth() + 1, this, 0.6F, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			}
		}
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	@Override
	protected boolean makeBridge(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		return this.makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation, false);
	}

	protected boolean makeBridge(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation, boolean ascender) {
		// bridges are size  always
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);

		// adjust height for those stupid little things
		if (wingSize == 3) {
			wingHeight = 4;
		}

		MushroomTowerBridgeComponent bridge = new MushroomTowerBridgeComponent(TFStructurePieceTypes.TFMTBri.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		bridge.isAscender = ascender;
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(bridge.getBoundingBox());
		if (intersect == null || intersect == this) {
			intersect = list.findCollisionPiece(bridge.getWingBB());
		} else {
			return false;
		}
		// okay, I think we can actually make one, as long as we're not still intersecting something.
		if (intersect == null || intersect == this) {
			list.addPiece(bridge);
			bridge.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	private boolean makeMainBridge(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingHeight, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);

		MushroomTowerMainBridgeComponent bridge = new MushroomTowerMainBridgeComponent(index, dx[0], dx[1], dx[2], wingHeight, direction);

		list.addPiece(bridge);
		bridge.addChildren(this, list, rand);
		addOpening(x, y, z, rotation);
		return true;
	}

	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	@Override
	public int[] getValidOpening(RandomSource rand, Rotation direction) {
		// variables!
		int wLength = Math.min(size / 3, 3); // wall length
		int offset = (size - wLength) / 2; // wall thickness

		// for directions 0 or 2, the wall lies along the z axis
		if (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) {
			int rx = direction == Rotation.NONE ? size - 1 : 0;
			int rz = offset + rand.nextInt(wLength);
			int ry = getYByStairs(rz, rand, direction);

			return new int[]{rx, ry, rz};
		}

		// for directions 1 or 3, the wall lies along the x axis
		if (direction == Rotation.CLOCKWISE_90 || direction == Rotation.COUNTERCLOCKWISE_90) {
			int rx = offset + rand.nextInt(wLength);
			int rz = direction == Rotation.CLOCKWISE_90 ? size - 1 : 0;
			int ry = getYByStairs(rx, rand, direction);

			return new int[]{rx, ry, rz};
		}

		return new int[]{0, 0, 0};
	}

	/**
	 * Gets a Y value where the stairs meet the specified X coordinate.
	 * Also works for Z coordinates.
	 */
	@Override
	protected int getYByStairs(int rx, RandomSource rand, Rotation direction) {

		int floors = this.height / FLOOR_HEIGHT;

		return FLOOR_HEIGHT + 1 + (rand.nextInt(floors - 1) * FLOOR_HEIGHT);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		makeTrunk(world, sbb);

		// make floors
		makeFloorsForTower(world, sbb);

		// openings
		makeOpenings(world, sbb);
	}

	private void makeTrunk(WorldGenLevel world, BoundingBox sbb) {
		int diameter = this.size / 2;
		int hollow = (int) (diameter * 0.8);

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.4));

				// make a trunk!
				if (dist <= diameter) {
					// make floor/ceiling
					placeBlock(world, deco.floorState, dx + diameter, 0, dz + diameter, sbb);
					placeBlock(world, deco.floorState, dx + diameter, height, dz + diameter, sbb);

					// make walls
					if (dist > hollow) {
						for (int dy = 0; dy <= height; dy++) {
							placeBlock(world, deco.blockState, dx + diameter, dy, dz + diameter, sbb);
						}
					} else {
						for (int dy = 1; dy <= height - 1; dy++) {
							placeBlock(world, AIR, dx + diameter, dy, dz + diameter, sbb);
						}
					}

					// make base
					if (this.hasBase) {
						this.fillColumnDown(world, deco.blockState, dx + diameter, -1, dz + diameter, sbb);
					}
				}
			}
		}
	}

	private void makeFloorsForTower(WorldGenLevel world, BoundingBox sbb) {
		int floors = this.height / FLOOR_HEIGHT;

		// divide the tower into floors
		for (int i = 0; i < floors; i++) {
			placeFloor(world, i * FLOOR_HEIGHT, sbb);
		}
	}

	private void placeFloor(WorldGenLevel world, int dy, BoundingBox sbb) {
		int diameter = this.size / 2;
		int hollow = (int) (diameter * 0.8);

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.4));

				// make a floor!
				if (dist <= hollow) {
					{
						placeBlock(world, this.isAscender ? Blocks.JUNGLE_PLANKS.defaultBlockState() : deco.floorState, dx + diameter, dy, dz + diameter, sbb);
					}
				}
			}
		}
	}

	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	@Override
	protected void makeDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		super.makeDoorOpening(world, dx, dy, dz, sbb);

		if (getBlock(world, dx, dy + 2, dz, sbb).getBlock() != Blocks.AIR) {
			placeBlock(world, deco.accentState, dx, dy + 2, dz, sbb);
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 */
	@Override
	protected void decorateFloor(WorldGenLevel world, RandomSource rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		//decorateWraparoundWallSteps(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
	}
}
