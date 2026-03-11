package twilightforest.world.components.structures.darktower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.TFStructureDecorator;
import twilightforest.world.components.structures.lichtower.*;

import java.util.ArrayList;

public class DarkTowerWingComponent extends TowerWingComponent {
	protected boolean keyTower = false;
	protected final ArrayList<EnumDarkTowerDoor> openingTypes = new ArrayList<>();

	public DarkTowerWingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFDTWin.get(), nbt);
	}

	public DarkTowerWingComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.keyTower = nbt.getBoolean("keyTower");

		this.readDoorsTypesFromArray(nbt.getIntArray("doorTypeInts"));
	}

	protected DarkTowerWingComponent(StructurePieceType piece, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(piece, i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * Turn the openings array into an array of ints.
	 */
	private int[] getDoorsTypesAsIntArray() {
		int[] ret = new int[this.openingTypes.size()];

		int idx = 0;

		for (EnumDarkTowerDoor doorType : openingTypes) {
			ret[idx++] = doorType.ordinal();
		}

		return ret;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);

		tagCompound.putBoolean("keyTower", this.keyTower);

		tagCompound.putIntArray("doorTypeInts", this.getDoorsTypesAsIntArray());
	}

	/**
	 * Read in opening types from int array
	 */
	private void readDoorsTypesFromArray(int[] intArray) {
		for (int typeInt : intArray) {
			this.openingTypes.add(EnumDarkTowerDoor.values()[typeInt]);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// add a roof?
		makeARoof(parent, list, rand);

		// add a beard
		makeABeard(parent, list, rand);

		if (size > 10) {
			// sub towers
			for (Rotation direction : RotationUtil.ROTATIONS) {
				int[] dest = getValidOpening(rand, direction);
				int childHeight = validateChildHeight(height - 4 + rand.nextInt(10) - rand.nextInt(10));

				boolean madeWing = makeTowerWing(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], size - 2, childHeight, direction);

				// occasional balcony
				if (!madeWing && (direction == Rotation.CLOCKWISE_180 || rand.nextBoolean())) {
					makeTowerBalcony(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], direction);
				}
			}
		} else if (rand.nextInt(4) == 0 && !(this instanceof DarkTowerEntranceComponent)) {
			// occasional balcony on small towers too
			Rotation direction = RotationUtil.ROTATIONS[rand.nextInt(4)];
			int[] dest = getValidOpening(rand, direction);
			makeTowerBalcony(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], direction);
		}
	}

	protected int validateChildHeight(int childHeight) {
		return (childHeight / 4) * 4 + 1;
	}

	/**
	 * Attach a roof to this tower.
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();

		TowerRoofComponent roof = switch (rand.nextInt(5)) {
			case 2 -> new DarkTowerRoofCactusComponent(index, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			case 3 -> new DarkTowerRoofRingsComponent(index, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			case 4 -> new DarkTowerRoofFourPostComponent(index, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			default -> new DarkTowerRoofAntennaComponent(index, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		};

		list.addPiece(roof);
		roof.addChildren(this, list, rand);
		roofType = roof.getClass();
	}

	@Override
	protected void makeAttachedRoof(StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();
		TowerRoofComponent roof;

		// this is our preferred roof type:
		if (roofType == null && rand.nextInt(32) != 0) {
			tryToFitRoof(list, rand, new TowerRoofGableForwardsComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ()));
		}

		// this is for roofs that don't fit.
		if (roofType == null && rand.nextInt(8) != 0) {
			tryToFitRoof(list, rand, new TowerRoofSlabForwardsComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ()));
		}

		// finally, if we're cramped for space, try this
		if (roofType == null && rand.nextInt(32) != 0) {
			// fall through to this next roof
			roof = new TowerRoofAttachedSlabComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}

		// last resort
		if (roofType == null) {
			// fall through to this next roof
			roof = new TowerRoofFenceComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}
	}

	/**
	 * Add a beard to this structure.  There is only one type of beard.
	 */
	@Override
	public void makeABeard(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		DarkTowerBeardComponent beard = new DarkTowerBeardComponent(this.getGenDepth() + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(beard);
		beard.addChildren(this, list, rand);
	}

	/**
	 * Make another wing just like this one
	 */
	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 8) {
			return false;
		}

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 5, direction);

		if (dx[1] + wingHeight > 250) {
			// end of the world!
			return false;
		}

		DarkTowerBridgeComponent bridge = new DarkTowerBridgeComponent(TFStructurePieceTypes.TFDTBri.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(bridge.getBoundingBox());
		if (intersect == null || intersect == this) {
			intersect = list.findCollisionPiece(bridge.getWingBB());
		} else {
			return false;
		}
		if (intersect == null || intersect == this) {
			list.addPiece(bridge);
			bridge.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	protected boolean makeTowerBalcony(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 5, direction);

		DarkTowerBalconyComponent balcony = new DarkTowerBalconyComponent(index, dx[0], dx[1], dx[2], direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(balcony.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.addPiece(balcony);
			balcony.addChildren(this, list, rand);
			addOpening(x, y, z, rotation, EnumDarkTowerDoor.REAPPEARING);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// make walls
		makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		// clear inside
		generateAirBox(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// sky light
//		nullifySkyLightForBoundingBox(world);

		if (this.size > 9) {
			// half floors, always starting at y = 4
			addHalfFloors(world, decoRNG, sbb, 4, height - 1);
		} else {
			if (decoRNG.nextInt(3) == 0) {
				addSmallTimberBeams(world, decoRNG, sbb, 4, height - 1);
			} else {
				addHalfFloors(world, decoRNG, sbb, 4, height - 1);
			}
		}

		// openings
		makeOpenings(world, sbb);

		// destroy some towers
		if (decoRNG.nextBoolean() && !this.isKeyTower() && this.height > 8) {
			int blobs = 1;

			if (this.size > 9 && decoRNG.nextBoolean()) {
				blobs++;
			}

			for (int i = 0; i < blobs; i++) {

				// find a random spot in the tower
				int x = decoRNG.nextInt(size);
				int y = decoRNG.nextInt(height - 7) + 2;
				int z = decoRNG.nextInt(size);

				destroyTower(world, decoRNG, x, y, z, 3, sbb);
			}
		}
	}

	/**
	 * Add a destruction burst
	 */
	protected void destroyTower(WorldGenLevel world, RandomSource decoRNG, int x, int y, int z, int amount, BoundingBox sbb) {
		//makeNetherburst(world, decoRNG, 16, 100, 40, x, y, z, 0, sbb);

		int initialRadius = decoRNG.nextInt(amount) + amount;

		drawBlob(world, x, y, z, initialRadius, AIR, sbb);

		for (int i = 0; i < 3; i++) {
			int dx = x + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);
			int dy = y + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);
			int dz = z + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);

			netherTransformBlob(world, decoRNG, dx, dy, dz, initialRadius - 1, sbb);
			drawBlob(world, dx, dy, dz, initialRadius - 2, AIR, sbb);
		}
	}

	private void netherTransformBlob(WorldGenLevel world, RandomSource inRand, int sx, int sy, int sz, int rad, BoundingBox sbb) {

		RandomSource rand = RandomSource.create(inRand.nextLong());

		// then trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			for (byte dy = 0; dy <= rad; dy++) {
				for (byte dz = 0; dz <= rad; dz++) {
					// determine how far we are from the center.
					byte dist;
					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}

					// if we're inside the blob, fill it
					if (dist <= rad) {
						// do eight at a time for easiness!
						testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz + dz, sbb);
						testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz + dz, sbb);
						testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz - dz, sbb);
						testAndChangeToNetherrack(world, rand, sx - dx, sy + dy, sz + dz, sbb);
						testAndChangeToNetherrack(world, rand, sx - dx, sy + dy, sz - dz, sbb);
						testAndChangeToNetherrack(world, rand, sx + dx, sy - dy, sz + dz, sbb);
						testAndChangeToNetherrack(world, rand, sx + dx, sy - dy, sz - dz, sbb);
						testAndChangeToNetherrack(world, rand, sx - dx, sy - dy, sz + dz, sbb);
						testAndChangeToNetherrack(world, rand, sx - dx, sy - dy, sz - dz, sbb);
					}
				}
			}
		}
	}

	private void testAndChangeToNetherrack(WorldGenLevel world, RandomSource rand, int x, int y, int z, BoundingBox sbb) {
		if (this.getBlock(world, x, y, z, sbb).getBlock() != Blocks.AIR) {
			this.placeBlock(world, Blocks.NETHERRACK.defaultBlockState(), x, y, z, sbb);

			if (this.getBlock(world, x, y + 1, z, sbb).getBlock() == Blocks.AIR && rand.nextBoolean()) {
				this.placeBlock(world, Blocks.FIRE.defaultBlockState(), x, y + 1, z, sbb);
			}
		}
	}

	/**
	 * Draw a giant blob of whatevs (okay, it's going to be leaves).
	 */
	private void drawBlob(WorldGenLevel world, int sx, int sy, int sz, int rad, BlockState state, BoundingBox sbb) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			for (byte dy = 0; dy <= rad; dy++) {
				for (byte dz = 0; dz <= rad; dz++) {
					// determine how far we are from the center.
					byte dist;
					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}

					// if we're inside the blob, fill it
					if (dist <= rad) {
						// do eight at a time for easiness!
						this.placeBlock(world, state, sx + dx, sy + dy, sz + dz, sbb);
						this.placeBlock(world, state, sx + dx, sy + dy, sz - dz, sbb);
						this.placeBlock(world, state, sx - dx, sy + dy, sz + dz, sbb);
						this.placeBlock(world, state, sx - dx, sy + dy, sz - dz, sbb);
						this.placeBlock(world, state, sx + dx, sy - dy, sz + dz, sbb);
						this.placeBlock(world, state, sx + dx, sy - dy, sz - dz, sbb);
						this.placeBlock(world, state, sx - dx, sy - dy, sz + dz, sbb);
						this.placeBlock(world, state, sx - dx, sy - dy, sz - dz, sbb);
					}
				}
			}
		}
	}

	/**
	 * Add a bunch of random half floors
	 */
	@SuppressWarnings("fallthrough")
	private void addHalfFloors(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int bottom, int top) {

		int spacing = 4;//this.size > 9 ? 4 : 3;
		Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY() + bottom) % 3];

		if (bottom == 0) {
			bottom += spacing;
		}

		// fill with half floors
		for (int y = bottom; y < top; y += spacing) {
			rotation = rotation.getRotated(Rotation.CLOCKWISE_180);

			if (y >= top - spacing) {
				makeFullFloor(world, sbb, y);
				if (isDeadEnd()) {
					decorateTreasureRoom(world, sbb, rotation, y, 4, this.deco);
				}
			} else {
				makeHalfFloor(world, sbb, rotation, y);

				// decorate
				// FIXME: Case 1 gets double weight when size >= 11
				switch (rand.nextInt(8)) {
					case 0:
						if (this.size < 11) {
							decorateReappearingFloor(world, sbb, rotation, y);
							break;
						}
					case 1:
						decorateSpawner(world, rand, sbb, rotation, y);
						break;
					case 2:
						decorateLounge(world, sbb, rotation, y);
						break;
					case 3:
						decorateLibrary(world, sbb, rotation, y);
						break;
					case 4:
						decorateExperimentPulser(world, sbb, rotation, y);
						break;
					case 5:
						decorateExperimentLamp(world, sbb, rotation, y);
						break;
					case 6:
						decoratePuzzleChest(world, sbb, rotation, y);
						break;
				}
			}

			addStairsDown(world, sbb, rotation, y, size - 2, spacing);
			if (this.size > 9) {
				// double wide staircase
				addStairsDown(world, sbb, rotation, y, size - 3, spacing);
			}
		}

		rotation = rotation.getRotated(Rotation.CLOCKWISE_180);

		// stairs to roof
		addStairsDown(world, sbb, rotation, this.height - 1, size - 2, spacing);
	}

	/**
	 * Dark tower half floors
	 */
	protected void makeHalfFloor(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		this.fillBlocksRotated(world, sbb, size / 2, y, 1, size - 2, y, size - 2, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, size / 2 - 1, y, 1, size / 2 - 1, y, size - 2, deco.accentState, rotation);
	}

	/**
	 * Dark tower full floors
	 */
	protected void makeFullFloor(WorldGenLevel world, BoundingBox sbb, int y) {
		// half floor
		this.generateBox(world, sbb, 1, y, 1, size - 2, y, size - 2, deco.blockState, Blocks.AIR.defaultBlockState(), false);
		this.generateBox(world, sbb, size / 2, y, 1, size / 2, y, size - 2, deco.accentState, Blocks.AIR.defaultBlockState(), true);
	}

	/**
	 * Dark tower treasure rooms!
	 */
	protected void decorateTreasureRoom(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y, int spacing, TFStructureDecorator myDeco) {
		//treasure chest!
		int x = this.size / 2;
		int z = this.size / 2;

		this.makePillarFrame(world, sbb, this.deco, rotation, x - 1, y, z - 1, true);

		setBlockStateRotated(world, myDeco.platformState, x, y + 1, z, rotation, sbb);

		placeTreasureAtCurrentPosition(world, x, y + 2, z, this.isKeyTower() ? TFLootTables.DARKTOWER_KEY : TFLootTables.DARKTOWER_CACHE, sbb);
	}

	private void decorateSpawner(WorldGenLevel world, RandomSource rand, BoundingBox sbb, Rotation rotation, int y) {
		int x = this.size > 9 ? 4 : 3;
		int z = this.size > 9 ? 5 : 4;

		EntityType<?> mobID;

		if (this.size > 9) {
			mobID = rand.nextBoolean() ? TFEntities.CARMINITE_GOLEM.get() : TFEntities.CARMINITE_BROODLING.get();
		} else {
			mobID = TFEntities.CARMINITE_BROODLING.get();
		}

		// pillar frame
		this.makePillarFrame(world, sbb, this.deco, rotation, x, y, z, true);
		this.setSpawnerRotated(world, x + 1, y + 2, z + 1, rotation, mobID, sbb);
	}

	/**
	 * A lounge with a couch and table
	 */
	private void decorateLounge(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		int cx = this.size > 9 ? 9 : 7;
		int cz = this.size > 9 ? 4 : 3;

		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), cx, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), cx, y + 1, cz + 1, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, false), cx, y + 1, cz + 2, rotation, sbb);

		cx = this.size > 9 ? 5 : 3;

		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, true), cx, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, getSlabState(Blocks.SPRUCE_SLAB.defaultBlockState(), SlabType.TOP), cx, y + 1, cz + 1, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, true), cx, y + 1, cz + 2, rotation, sbb);
	}

	/**
	 * Decorate with a pressure plate triggered reappearing floor.  Only suitable for small towers
	 */
	private void decorateReappearingFloor(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		final BlockState inactiveReappearing = TFBlocks.REAPPEARING_BLOCK.get().defaultBlockState();
		final BlockState woodenPressurePlate = Blocks.OAK_PRESSURE_PLATE.defaultBlockState();
		// floor
		this.fillBlocksRotated(world, sbb, 4, y, 3, 7, y, 5, inactiveReappearing, rotation);
		// plates
		this.fillBlocksRotated(world, sbb, 4, y + 1, 2, 7, y + 1, 2, woodenPressurePlate, rotation);
		this.fillBlocksRotated(world, sbb, 4, y + 1, 6, 7, y + 1, 6, woodenPressurePlate, rotation);
	}

	/**
	 * Decorate with a redstone device that turns a lamp on or off
	 */
	private void decorateExperimentLamp(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {

		int cx = this.size > 9 ? 5 : 3;
		int cz = this.size > 9 ? 5 : 4;

		final BlockState redstoneLamp = Blocks.REDSTONE_LAMP.defaultBlockState();

		setBlockStateRotated(world, Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP), cx, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, redstoneLamp, cx, y + 2, cz, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, cx, y + 1, cz + 1, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.NORTH, false), cx, y + 1, cz + 2, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, cx, y + 3, cz - 1, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.SOUTH, true), cx, y + 3, cz - 2, rotation, sbb);
	}

	protected static BlockState getLeverState(BlockState initialState, AttachFace face, Direction direction, boolean isPowered) {
		switch (direction) {
			case NORTH:
			case SOUTH:
			case EAST:
			case WEST:
				//All Horizontal facings are as they should
				break;
			case UP:
			case DOWN:
			default:
				//Levers cannot face Up or Down, as it is a Horizontal Face
				direction = Direction.NORTH;
		}
		return initialState.setValue(HorizontalDirectionalBlock.FACING, direction)
			.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, face)
			.setValue(LeverBlock.POWERED, isPowered);
	}

	/**
	 * Decorate with a redstone device that pulses a block back and forth
	 */
	private void decorateExperimentPulser(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {

		int cx = this.size > 9 ? 6 : 5;
		int cz = this.size > 9 ? 4 : 3;

		BlockState redstoneWire = Blocks.REDSTONE_WIRE.defaultBlockState();
		BlockState woodenPressurePlate = Blocks.OAK_PRESSURE_PLATE.defaultBlockState();
		BlockState stickyPiston = Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH);
		BlockState unpoweredRepeater = Blocks.REPEATER.defaultBlockState().setValue(DiodeBlock.POWERED, false).setValue(HorizontalDirectionalBlock.FACING, Direction.WEST).setValue(RepeaterBlock.DELAY, 2);

		setBlockStateRotated(world, stickyPiston, cx, y + 1, cz + 1, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, cx, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, redstoneWire, cx + 1, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, woodenPressurePlate, cx + 2, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, unpoweredRepeater, cx - 1, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, redstoneWire, cx - 2, y + 1, cz, rotation, sbb);
		setBlockStateRotated(world, redstoneWire, cx - 2, y + 1, cz + 1, rotation, sbb);
		setBlockStateRotated(world, redstoneWire, cx - 1, y + 1, cz + 1, rotation, sbb);
	}

	/**
	 * Decorate with some bookshelves
	 */
	private void decorateLibrary(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		int bx = this.size > 9 ? 4 : 3;
		int bz = this.size > 9 ? 3 : 2;

		makeSmallBookshelf(world, sbb, rotation, y, bx, bz);

		bx = this.size > 9 ? 9 : 7;
		bz = this.size > 9 ? 3 : 2;
		makeSmallBookshelf(world, sbb, rotation, y, bx, bz);
	}

	protected void makeSmallBookshelf(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y, int bx, int bz) {
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, false), bx, y + 1, bz, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, true), bx, y + 2, bz, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), bx, y + 1, bz + 3, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, true), bx, y + 2, bz + 3, rotation, sbb);
		final BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
		setBlockStateRotated(world, bookshelf, bx, y + 1, bz + 1, rotation, sbb);
		setBlockStateRotated(world, bookshelf, bx, y + 2, bz + 1, rotation, sbb);
		setBlockStateRotated(world, bookshelf, bx, y + 1, bz + 2, rotation, sbb);
		setBlockStateRotated(world, bookshelf, bx, y + 2, bz + 2, rotation, sbb);
	}

	/**
	 * A chest with an extremely simple puzzle
	 */
	private void decoratePuzzleChest(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		int x = this.size > 9 ? 4 : 3;
		int z = this.size > 9 ? 5 : 4;
		// pillar frameframe
		this.makePillarFrame(world, sbb, this.deco, rotation, x, y, z, true);

		// reinforce with towerwood
		setBlockStateRotated(world, deco.platformState, x + 1, y + 1, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x + 2, y + 1, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x, y + 1, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x + 1, y + 1, z + 2, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x + 1, y + 1, z, rotation, sbb);

		setBlockStateRotated(world, deco.blockState, x + 2, y + 3, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x, y + 3, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x + 1, y + 3, z + 2, rotation, sbb);
		setBlockStateRotated(world, AIR, x + 1, y + 3, z, rotation, sbb);
		setBlockStateRotated(world, deco.blockState, x + 1, y + 3, z + 1, rotation, sbb);
		setBlockStateRotated(world, Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH), x + 1, y + 3, z - 1, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, x + 1, y + 3, z - 2, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.WEST, false), x + 2, y + 3, z - 2, rotation, sbb);

		placeTreasureRotated(world, x + 1, y + 2, z + 1, getOrientation(), rotation, TFLootTables.DARKTOWER_CACHE, sbb);
	}

	/**
	 * Make a 3x3x3 pillar frame
	 */
	protected void makePillarFrame(WorldGenLevel world, BoundingBox sbb, TFStructureDecorator myDeco, Rotation rotation, int x, int y, int z, boolean fenced) {
		makePillarFrame(world, sbb, myDeco, rotation, x, y, z, 3, 3, 3, fenced);
	}

	/**
	 * Place one of the architectural features that I frequently overuse in my structures
	 */
	protected void makePillarFrame(WorldGenLevel world, BoundingBox sbb, TFStructureDecorator myDeco, Rotation rotation, int x, int y, int z, int width, int height, int length, boolean fenced) {
		// fill in posts
		for (int dx = 0; dx < width; dx++) {
			for (int dz = 0; dz < length; dz++) {
				if ((dx % 3 == 0 || dx == width - 1) && (dz % 3 == 0 || dz == length - 1)) {
					for (int py = 1; py <= height; py++) {
						setBlockStateRotated(world, myDeco.pillarState, x + dx, y + py, z + dz, rotation, sbb);
					}
				} else {
					if (dx == 0) {
						final BlockState southStairs = getStairState(deco.stairState, Direction.WEST, false);
						setBlockStateRotated(world, southStairs, x + dx, y + 1, z + dz, rotation, sbb);
						setBlockStateRotated(world, southStairs.setValue(StairBlock.HALF, Half.TOP), x + dx, y + height, z + dz, rotation, sbb);
					} else if (dx == width - 1) {
						final BlockState northStairs = getStairState(deco.stairState, Direction.EAST, false);
						setBlockStateRotated(world, northStairs, x + dx, y + 1, z + dz, rotation, sbb);
						setBlockStateRotated(world, northStairs.setValue(StairBlock.HALF, Half.TOP), x + dx, y + height, z + dz, rotation, sbb);
					} else if (dz == 0) {
						final BlockState westStairs = getStairState(deco.stairState, Direction.NORTH, false);
						setBlockStateRotated(world, westStairs, x + dx, y + 1, z + dz, rotation, sbb);
						setBlockStateRotated(world, westStairs.setValue(StairBlock.HALF, Half.TOP), x + dx, y + height, z + dz, rotation, sbb);
					} else if (dz == length - 1) {
						final BlockState eastStairs = getStairState(deco.stairState, Direction.SOUTH, false);
						setBlockStateRotated(world, eastStairs, x + dx, y + 1, z + dz, rotation, sbb);
						setBlockStateRotated(world, eastStairs.setValue(StairBlock.HALF, Half.TOP), x + dx, y + height, z + dz, rotation, sbb);
					}

					if (fenced && (dx == 0 || dx == width - 1 || dz == 0 || dz == length - 1)) {
						for (int fy = 2; fy <= height - 1; fy++) {
							setBlockStateRotated(world, myDeco.fenceState, x + dx, y + fy, z + dz, rotation, sbb);
						}
					}
				}
			}
		}
	}

	/**
	 * Dark tower half floors
	 */
	protected void addStairsDown(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y, int sz, int spacing) {
		// stairs
		for (int i = 0; i < spacing; i++) {
			int sx = size - 3 - i;

			this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), sx, y - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, deco.accentState, sx, y - 1 - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, AIR, sx, y + 1 - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, AIR, sx, y + 2 - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, AIR, sx - 1, y + 2 - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, AIR, sx, y + 3 - i, sz, rotation, sbb);
			this.setBlockStateRotated(world, AIR, sx - 1, y + 3 - i, sz, rotation, sbb);
		}
	}

	/**
	 * Add a bunch of timber beams
	 */
	protected void addSmallTimberBeams(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int bottom, int top) {

		int spacing = 4;
		Rotation rotation = Rotation.NONE;
		if (bottom == 0) {
			bottom += spacing;
		}

		// fill with 3/4 floors
		for (int y = bottom; y < top; y += spacing) {
			rotation = rotation.getRotated(Rotation.CLOCKWISE_90);

			if (y >= top - spacing && isDeadEnd()) {
				makeTimberFloor(world, sbb, rotation, y);

				TFStructureDecorator logDeco = new StructureDecoratorDarkTower();

				logDeco.pillarState = TFBlocks.DARK_LOG.get().defaultBlockState();
				logDeco.platformState = TFBlocks.DARK_LOG.get().defaultBlockState();

				decorateTreasureRoom(world, sbb, rotation, y, 4, logDeco);
			} else {
				makeSmallTimberBeams(world, rand, sbb, rotation, y, y == bottom && bottom != spacing);
			}
		}
	}

	/**
	 * Make a mostly soid timber floor
	 */
	protected void makeTimberFloor(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		BlockState beamID = TFBlocks.DARK_LOG.get().defaultBlockState();
		BlockState beamStateNS = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		BlockState beamStateUD = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		BlockState beamStateEW = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		for (int z = 1; z < size - 1; z++) {
			for (int x = 1; x < size - 1; x++) {
				if (x < z) {
					setBlockStateRotated(world, beamStateNS, x, y, z, rotation, sbb);
				} else {
					setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
				}
			}
		}

		// beams going down
		for (int by = 1; by < 4; by++) {
			BlockState ladder = Blocks.LADDER.defaultBlockState();
			setBlockStateRotated(world, beamStateUD, 2, y - by, 2, rotation, sbb);
			setBlockStateRotated(world, ladder.setValue(LadderBlock.FACING, Direction.WEST), 2 + 1, y - by, 2, rotation, sbb);
			setBlockStateRotated(world, beamStateUD, 6, y - by, 6, rotation, sbb);
			setBlockStateRotated(world, ladder.setValue(LadderBlock.FACING, Direction.EAST), 6 - 1, y - by, 6, rotation, sbb);
		}

		// holes for entrance
		setBlockStateRotated(world, AIR, 3, y, 2, rotation, sbb);
		setBlockStateRotated(world, AIR, 5, y, 6, rotation, sbb);
	}

	/**
	 * Make a lattice of log blocks
	 */
	protected void makeSmallTimberBeams(WorldGenLevel world, RandomSource rand, BoundingBox sbb, Rotation rotation, int y, boolean bottom) {
		BlockState beamID = TFBlocks.DARK_LOG.get().defaultBlockState();
		BlockState beamStateNS = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState beamStateUD = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		BlockState beamStateEW = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);

		// two beams going e/w
		for (int z = 1; z < size - 1; z++) {
			setBlockStateRotated(world, beamStateEW, 2, y, z, rotation, sbb);
			setBlockStateRotated(world, beamStateEW, 6, y, z, rotation, sbb);
		}

		// a few random cross beams
		int z = pickBetweenExcluding(3, size - 3, rand, 2, 2, 6);
		for (int x = 3; x < 6; x++) {
			setBlockStateRotated(world, beamStateNS, x, y, z, rotation, sbb);
		}

		// beams going down
		int x1 = 2;
		int z1 = rand.nextBoolean() ? 2 : 6;
		int x3 = 6;
		int z3 = rand.nextBoolean() ? 2 : 6;

		for (int by = 1; by < 4; by++) {
			final BlockState ladder = Blocks.LADDER.defaultBlockState();
			if (!bottom || checkPost(world, x1, y - 4, z1, rotation, sbb)) {
				setBlockStateRotated(world, beamStateUD, x1, y - by, z1, rotation, sbb);
				setBlockStateRotated(world, ladder.setValue(LadderBlock.FACING, Direction.WEST), x1 + 1, y - by, z1, rotation, sbb);
			}
			if (!bottom || checkPost(world, x3, y - 4, z3, rotation, sbb)) {
				setBlockStateRotated(world, beamStateUD, x3, y - by, z3, rotation, sbb);
				setBlockStateRotated(world, ladder.setValue(LadderBlock.FACING, Direction.EAST), x3 - 1, y - by, z3, rotation, sbb);
			}
		}
	}

	/**
	 * Utility function to pick a random number between two values, excluding three specified values
	 */
	protected int pickBetweenExcluding(int low, int high, RandomSource rand, int k, int l, int m) {
		int result;

		do {
			result = rand.nextInt(high - low) + low;
		}
		while (result == k || result == l || result == m);

		return result;
	}

	/**
	 * Pick one of the three specified values at random
	 */
	protected int pickFrom(RandomSource rand, int i, int j, int k) {
		return switch (rand.nextInt(3)) {
			case 1 -> j;
			case 2 -> k;
			default -> i;
		};
	}

	/**
	 * Utility function for beam maze that checks if we should build a beam all the way down -- is there a valid spot to end it?
	 */
	protected boolean checkPost(WorldGenLevel world, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		int worldX = this.getXWithOffsetRotated(x, z, rotation);
		int worldY = this.getWorldY(y);
		int worldZ = this.getZWithOffsetRotated(x, z, rotation);
		final BlockPos vec = new BlockPos(worldX, worldY, worldZ);
		if (!sbb.isInside(vec)) return false;
		BlockState blockState = world.getBlockState(vec);
		return blockState.getBlock() != Blocks.AIR && blockState != deco.accentState;
	}

	/**
	 * Generate walls for the tower with the distinct pattern of blocks and accent blocks
	 */
	protected void makeEncasedWalls(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (x != minX && x != maxX && y != minY && y != maxY && z != minZ && z != maxZ) {
					} else {
						// wall
						if (((y == minY || y == maxY) && ((x == minY || x == maxX) || (z == minZ || z == maxZ)))
							|| ((z == minZ || z == maxZ) && ((x == minY || x == maxX) || (y == minY || y == maxY)))) {
							this.placeBlock(world, deco.accentState, x, y, z, sbb);
						} else {
							StructurePiece.BlockSelector blocker = deco.randomBlocks;

							blocker.next(rand, x, y, z, true);
							this.placeBlock(world, blocker.getNext(), x, y, z, sbb);
						}
					}
				}
			}
		}

		// corners
		this.placeBlock(world, deco.accentState, minX + 1, minY + 1, minZ, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, minY + 1, maxZ, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, minY + 1, minZ, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, minY + 1, maxZ, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, maxY - 1, minZ, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, maxY - 1, maxZ, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, maxY - 1, minZ, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, maxY - 1, maxZ, sbb);

		this.placeBlock(world, deco.accentState, minX, minY + 1, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, minX, minY + 1, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, maxX, minY + 1, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, maxX, minY + 1, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, minX, maxY - 1, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, minX, maxY - 1, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, maxX, maxY - 1, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, maxX, maxY - 1, maxZ - 1, sbb);

		this.placeBlock(world, deco.accentState, minX + 1, minY, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, minY, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, minY, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, minY, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, maxY, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, minX + 1, maxY, maxZ - 1, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, maxY, minZ + 1, sbb);
		this.placeBlock(world, deco.accentState, maxX - 1, maxY, maxZ - 1, sbb);
	}

	/**
	 * Gets a random position in the specified direction that connects to a floor currently in the tower.
	 */
	@Override
	public int[] getValidOpening(RandomSource rand, Rotation direction) {
		int verticalOffset = this.size == 19 ? 5 : 4;

		// for directions 0 or 2, the wall lies along the z axis
		if (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) {
			int rx = direction == Rotation.NONE ? size - 1 : 0;
			int rz = this.size / 2;
			int ry = this.height - verticalOffset;

			return new int[]{rx, ry, rz};
		}

		// for directions 1 or 3, the wall lies along the x axis
		if (direction == Rotation.CLOCKWISE_90 || direction == Rotation.COUNTERCLOCKWISE_90) {
			int rx = this.size / 2;
			int rz = direction == Rotation.CLOCKWISE_90 ? size - 1 : 0;
			int ry = this.height - verticalOffset;

			return new int[]{rx, ry, rz};
		}

		return new int[]{0, 0, 0};
	}

	/**
	 * Add an opening to the outside (or another tower) in the specified direction.
	 */
	@Override
	public void addOpening(int dx, int dy, int dz, Rotation direction) {
		this.addOpening(dx, dy, dz, direction, EnumDarkTowerDoor.VANISHING);
	}

	/**
	 * Add an opening where we keep track of the kind of opening
	 */
	protected void addOpening(int dx, int dy, int dz, Rotation direction, EnumDarkTowerDoor type) {
		super.addOpening(dx, dy, dz, direction);
		this.openingTypes.add(openings.indexOf(new BlockPos(dx, dy, dz)), type);
	}

	/**
	 * Iterate through the openings on our list and add them to the tower
	 */
	@Override
	protected void makeOpenings(WorldGenLevel world, BoundingBox sbb) {
		for (int i = 0; i < openings.size(); i++) {
			BlockPos doorCoords = openings.get(i);

			EnumDarkTowerDoor doorType;
			if (openingTypes.size() > i) {
				doorType = openingTypes.get(i);
			} else {
				doorType = EnumDarkTowerDoor.VANISHING;
			}

			switch (doorType) {
				case REAPPEARING -> makeReappearingDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
				case LOCKED -> makeLockedDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
				default -> makeDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
			}
		}
	}

	/**
	 * Make an opening in this tower for a door.
	 */
	@Override
	protected void makeDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		// nullify sky light
//		nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);

		final BlockState inactiveVanish = TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get().defaultBlockState();

		// clear the door
		if (dx == 0 || dx == size - 1) {
			this.generateBox(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveVanish, AIR, false);
		}
		if (dz == 0 || dz == size - 1) {
			this.generateBox(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveVanish, AIR, false);
		}
	}

	/**
	 * Make a 3x3 tower door that reappears
	 */
	protected void makeReappearingDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		// nullify sky light
//		nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);

		final BlockState inactiveReappearing = TFBlocks.REAPPEARING_BLOCK.get().defaultBlockState();

		// clear the door
		if (dx == 0 || dx == size - 1) {
			this.generateBox(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveReappearing, AIR, false);
		}
		if (dz == 0 || dz == size - 1) {
			this.generateBox(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveReappearing, AIR, false);
		}
	}

	/**
	 * Make a 3x3 tower door that is locked
	 */
	protected void makeLockedDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		// nullify sky light
//		nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);

		// clear the door
		final BlockState lockedVanish = TFBlocks.LOCKED_VANISHING_BLOCK.get().defaultBlockState();
		final BlockState inactiveVanish = TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get().defaultBlockState();

		if (dx == 0 || dx == size - 1) {
			this.generateBox(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveVanish, AIR, false);
			this.placeBlock(world, lockedVanish, dx, dy, dz + 1, sbb);
			this.placeBlock(world, lockedVanish, dx, dy, dz - 1, sbb);
			this.placeBlock(world, lockedVanish, dx, dy + 2, dz + 1, sbb);
			this.placeBlock(world, lockedVanish, dx, dy + 2, dz - 1, sbb);
		}
		if (dz == 0 || dz == size - 1) {
			this.generateBox(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
			this.generateBox(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveVanish, AIR, false);
			this.placeBlock(world, lockedVanish, dx + 1, dy, dz, sbb);
			this.placeBlock(world, lockedVanish, dx - 1, dy, dz, sbb);
			this.placeBlock(world, lockedVanish, dx + 1, dy + 2, dz, sbb);
			this.placeBlock(world, lockedVanish, dx - 1, dy + 2, dz, sbb);
		}
	}

	/**
	 * Returns true if this tower has only one exit.
	 */
	@Override
	public boolean isDeadEnd() {
		// we have to modify this to ignore door type 2 since that leads to balconies
		int nonBalconies = 0;

		for (EnumDarkTowerDoor type : openingTypes) {
			if (type != EnumDarkTowerDoor.REAPPEARING) {
				nonBalconies++;
			}
		}

		return nonBalconies <= 1;
	}

	public boolean isKeyTower() {
		return keyTower;
	}

	public void setKeyTower(boolean keyTower) {
		this.keyTower = keyTower;
	}
}
