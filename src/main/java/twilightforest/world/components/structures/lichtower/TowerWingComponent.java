package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.EntityUtil;
import twilightforest.util.RotationUtil;
import twilightforest.util.TFStructureHelper;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation", "unused"})
public class TowerWingComponent extends TFStructureComponentOld {

	public TowerWingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFLTWin.get(), nbt);
	}

	public TowerWingComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);

		this.size = nbt.getInt("towerSize");
		this.height = nbt.getInt("towerHeight");

		this.readOpeningsFromArray(nbt.getIntArray("doorInts"));

		this.highestOpening = nbt.getInt("highestOpening");
		// too lazy to do this as a loop
		this.openingTowards[0] = nbt.getBoolean("openingTowards0");
		this.openingTowards[1] = nbt.getBoolean("openingTowards1");
		this.openingTowards[2] = nbt.getBoolean("openingTowards2");
		this.openingTowards[3] = nbt.getBoolean("openingTowards3");
	}

	public int size;
	protected int height;
	protected Class<? extends TowerRoofComponent> roofType;

	protected ArrayList<BlockPos> openings = new ArrayList<BlockPos>();
	protected int highestOpening;
	protected boolean[] openingTowards = new boolean[]{false, false, true, false};

	protected TowerWingComponent(StructurePieceType type, int i, int x, int y, int z) {
		super(type, i, x, y, z);
		this.highestOpening = 0;
	}

	protected TowerWingComponent(StructurePieceType type, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(type, i, x, y, z);

		this.size = pSize;
		this.height = pHeight;
		this.setOrientation(direction);

		this.highestOpening = 0;

		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction, false);
	}

	/**
	 * Turn the openings array into an array of ints.
	 */
	private int[] getDoorsAsIntArray() {
		IntBuffer ibuffer = IntBuffer.allocate(this.openings.size() * 3);

		for (BlockPos door : openings) {
			ibuffer.put(door.getX());
			ibuffer.put(door.getY());
			ibuffer.put(door.getZ());
		}

		return ibuffer.array();
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);

		tagCompound.putInt("towerSize", this.size);
		tagCompound.putInt("towerHeight", this.height);

		tagCompound.putIntArray("doorInts", this.getDoorsAsIntArray());

		tagCompound.putInt("highestOpening", this.highestOpening);
		tagCompound.putBoolean("openingTowards0", this.openingTowards[0]);
		tagCompound.putBoolean("openingTowards1", this.openingTowards[1]);
		tagCompound.putBoolean("openingTowards2", this.openingTowards[2]);
		tagCompound.putBoolean("openingTowards3", this.openingTowards[3]);
	}

	/**
	 * Read in openings from int array
	 */
	private void readOpeningsFromArray(int[] intArray) {
		for (int i = 0; i < intArray.length; i += 3) {
			BlockPos door = new BlockPos(intArray[i], intArray[i + 1], intArray[i + 2]);

			this.openings.add(door);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// add a roof?
		makeARoof(parent, list, rand);

		// add a beard
		makeABeard(parent, list, rand);


		if (size > 4) {
			// sub towers
			for (final Rotation towerRotation : RotationUtil.ROTATIONS) {
				if (towerRotation == Rotation.CLOCKWISE_180) continue;
				int[] dest = getValidOpening(rand, towerRotation);
				if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 2, height - 4, towerRotation) && this.size > 8) {
					if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 4, height - 6, towerRotation)) {
						makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 6, height - 12, towerRotation);
					}
				}
			}
		}
	}

	public boolean makeTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 6) {
			return false;
		}

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		if (rand.nextInt(6) == 0) {
			return makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation);
			// or I don't know if we just want to continue instead if the bridge fails. 
			// I think there are very few circumstances where we can make a wing and not a bridge
		}

		TowerWingComponent wing = new TowerWingComponent(TFStructurePieceTypes.TFLTWin.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(wing.boundingBox);
		if (intersect == null || intersect == this) {
			list.addPiece(wing);
			wing.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			if (rand.nextInt(3) > 0) {
				return makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation);
			} else {
				// I guess we're done for this branch
				return false;
			}
		}
	}


	protected boolean makeBridge(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// bridges are size 3 always
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);
		// adjust height for those stupid little things
		if (wingSize == 3 && wingHeight > 10) {
			wingHeight = 6 + rand.nextInt(5);
		}
		TowerBridgeComponent bridge = new TowerBridgeComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(bridge.boundingBox);
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

	/**
	 * Add an opening to the outside (or another tower) in the specified direction.
	 */
	public void addOpening(int dx, int dy, int dz, Rotation direction) {
		openingTowards[direction.ordinal()] = true;
		if (dy > highestOpening) {
			highestOpening = dy;
		}
		openings.add(new BlockPos(dx, dy, dz));
	}

	/**
	 * Add an opening to the outside (or another tower) in the specified facing.
	 */
	public void addOpening(int dx, int dy, int dz, Direction facing) {
		this.addOpening(dx, dy, dz, RotationUtil.getRelativeRotation(this.getOrientation(), facing));
	}

	/**
	 * Add a beard to this structure.  There is only one type of beard.
	 */
	public void makeABeard(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {

		boolean attached = parent.getBoundingBox().minY() < this.boundingBox.minY();

		int index = this.getGenDepth();
		TowerBeardComponent beard;
		if (attached) {
			beard = new TowerBeardAttachedComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		} else {
			beard = new TowerBeardComponent(TFStructurePieceTypes.TFLTBea.get(), index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		}
		list.addPiece(beard);
		beard.addChildren(this, list, rand);
	}


	/**
	 * Attach a roof to this tower.
	 * <p>
	 * This function keeps trying roofs starting with the largest and fanciest, and then keeps trying smaller and plainer ones
	 */
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {

		// we are attached if our parent is taller than we are
		boolean attached = parent.getBoundingBox().maxY() > this.boundingBox.maxY();

		if (attached) {
			makeAttachedRoof(list, rand);
		} else {
			makeFreestandingRoof(list, rand);
		}

	}


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
	 * Check to see if this roof fits.  If it does:
	 * Add the specified roof to this tower and set the roofType variable.
	 */
	protected void tryToFitRoof(StructurePieceAccessor list, RandomSource rand, TowerRoofComponent roof) {
		if (roof.fits(this, list)) {
			list.addPiece(roof);
			roof.addChildren(this, list, rand);
			roofType = roof.getClass();
		}
	}

	protected void makeFreestandingRoof(StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();
		TowerRoofComponent roof;

		// most roofs that fit fancy roofs will be this
		if (roofType == null && rand.nextInt(8) != 0) {
			roof = new TowerRoofPointyOverhangComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}

		// don't pass by this one if it fits
		if (roofType == null) {
			roof = new TowerRoofStairsOverhangComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}

		// don't pass by this one if it fits
		if (roofType == null) {
			roof = new TowerRoofStairsComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}

		if (roofType == null && rand.nextInt(53) != 0) {
			// fall through to this next roof
			roof = new TowerRoofSlabComponent(TFStructurePieceTypes.TFLTRS.get(), index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}

		if (roofType == null) {
			// fall through to this next roof
			roof = new TowerRoofFenceComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
			tryToFitRoof(list, rand, roof);
		}
	}

	@Override
	public void postProcess(WorldGenLevel worldIn, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		generateBox(worldIn, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, TFStructureComponentOld.getStrongholdStones());

		// clear inside
		generateAirBox(worldIn, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// sky light
//		nullifySkyLightForBoundingBox(world);

		// marker blocks
//        setBlockState(world, Blocks.WOOL, this.coordBaseMode, size / 2, 2, size / 2, sbb);
//        setBlockState(world, Blocks.GOLD_BLOCK, 0, 0, 0, 0, sbb);

		// stairs!
		if (highestOpening > 1) {
			makeStairs(worldIn, rand, sbb);
		}

		// decorate?
		decorateThisTower(worldIn, generator, sbb);

		// windows
		makeWindows(worldIn, sbb, size < 4);

		// throw a bunch of opening markers in there
//        makeOpeningMarkers(world, rand, 100, sbb);

		// openings
		makeOpenings(worldIn, sbb);
	}

	/**
	 * Puts some colorful markers by possible openings in this tower.  Debug only.
	 *
	 * @param numMarkers How many markers to make
	 */
	protected void makeOpeningMarkers(WorldGenLevel world, RandomSource rand, int numMarkers, BoundingBox sbb) {
		if (size > 4) {
			final BlockState woolWhite = Blocks.WHITE_WOOL.defaultBlockState();
			final BlockState woolOrange = Blocks.ORANGE_WOOL.defaultBlockState();
			final BlockState woolMagenta = Blocks.MAGENTA_WOOL.defaultBlockState();
			final BlockState woolLightBlue = Blocks.LIGHT_BLUE_WOOL.defaultBlockState();

			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.NONE);
				placeBlock(world, woolWhite, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.CLOCKWISE_90);
				placeBlock(world, woolOrange, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.CLOCKWISE_180);
				placeBlock(world, woolMagenta, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.COUNTERCLOCKWISE_90);
				placeBlock(world, woolLightBlue, spot[0], spot[1], spot[2], sbb);
			}
		}
	}


	/**
	 * Add some appropriate decorations to this tower
	 */
	protected void decorateThisTower(WorldGenLevel world, ChunkGenerator generator, BoundingBox sbb) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) * (this.boundingBox.minZ() * 756839L));

		if (size > 3) {
			// only decorate towers with more than one available square inside.
			if (isDeadEnd()) {
				decorateDeadEnd(world, decoRNG, sbb);
			} else {
				// for now we'll just assume that any tower with more than one exit is a stair tower
				decorateStairTower(world, generator, decoRNG, sbb);
			}
		}
	}

	/**
	 * Decorates a dead end tower.  These towers have no stairs, and will be the focus of our interior design.
	 */
	protected void decorateDeadEnd(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		final BlockState birchPlanks = Blocks.BIRCH_PLANKS.defaultBlockState();
		int floors = (this.height - 1) / 5;

		// divide the tower into floors
		int floorHeight = this.height / floors;
		for (int i = 1; i < floors; i++) {
			// put down a floor
			for (int x = 1; x < size - 1; x++) {
				for (int z = 1; z < size - 1; z++) {
					placeBlock(world, birchPlanks, x, (i * floorHeight), z, sbb);
				}
			}
		}

		if (floors > 1) {
			Rotation ladderDir = Rotation.COUNTERCLOCKWISE_90;
			Rotation downLadderDir;

			// decorate bottom floor
			decorateFloor(world, rand, 0, 1, floorHeight, ladderDir, null, sbb);

			// decorate middle floors
			for (int i = 1; i < floors - 1; i++) {
				int bottom = 1 + floorHeight * i;
				int top = floorHeight * (i + 1);

				downLadderDir = ladderDir;
				ladderDir = ladderDir.getRotated(Rotation.CLOCKWISE_90);

				decorateFloor(world, rand, i, bottom, top, ladderDir, downLadderDir, sbb);
			}

			// decorate top floor
			decorateFloor(world, rand, floors, 1 + floorHeight * (floors - 1), height - 1, null, ladderDir, sbb);
		} else {
			// just one floor, decorate that, no ladders
			decorateFloor(world, rand, 0, 1, height - 1, null, null, sbb);
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 */
	protected void decorateFloor(WorldGenLevel world, RandomSource rand, int floor, int bottom, int top, @Nullable Rotation ladderUpDir, @Nullable Rotation ladderDownDir, BoundingBox sbb) {

		final BlockState ladder = Blocks.LADDER.defaultBlockState();
		if (ladderUpDir != null) {
			// add ladder going up
			final BlockState ladderUp = ladder.setValue(LadderBlock.FACING, ladderUpDir.rotate(Direction.EAST));

			int dx = getLadderX(ladderUpDir);
			int dz = getLadderZ(ladderUpDir);
			for (int dy = bottom; dy < top; dy++) {
				placeBlock(world, ladderUp, dx, dy, dz, sbb);
			}
		}

		if (ladderDownDir != null) {
			// add ladder going down
			final BlockState ladderDown = ladder.setValue(LadderBlock.FACING, ladderDownDir.rotate(Direction.EAST));
			int dx = getLadderX(ladderDownDir);
			int dz = getLadderZ(ladderDownDir);
			for (int dy = bottom - 1; dy < bottom + 2; dy++) {
				placeBlock(world, ladderDown, dx, dy, dz, sbb);
			}
		}

		// some of these go only on the bottom floor (ladderDownDir == null) and some go only on upper floors
		if (rand.nextInt(7) == 0 && ladderDownDir == null) {
			decorateWell(world, rand, bottom, sbb);
		} else if (rand.nextInt(7) == 0 && ladderDownDir == null) {
			decorateSkeletonRoom(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(6) == 0 && ladderDownDir == null) {
			decorateZombieRoom(world, rand, bottom, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(5) == 0 && ladderDownDir == null) {
			decorateCactusRoom(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(4) == 0 && ladderDownDir != null) {
			decorateTreasureChest(world, bottom, top, sbb);
		} else if (rand.nextInt(5) == 0) {
			decorateSpiderWebs(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(12) == 0 && ladderDownDir != null) {
			// these are annoying
			decorateSolidRock(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(3) == 0) {
			decorateFullLibrary(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else {
			decorateLibrary(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		}
	}

	/**
	 * Decorate this floor with a scenic well.
	 */
	protected void decorateWell(WorldGenLevel world, RandomSource rand, int bottom, BoundingBox sbb) {
		int cx = size / 2;

		BlockState waterOrLava = rand.nextInt(4) == 0 ? Blocks.LAVA.defaultBlockState() : Blocks.WATER.defaultBlockState();

		if (size > 5) {
			// actual well structure
			final BlockState stoneBricks = Blocks.STONE_BRICKS.defaultBlockState();
			final BlockState stoneSlabs = TFStructureHelper.stoneSlab;

			placeBlock(world, stoneBricks, cx - 1, bottom, cx - 1, sbb);
			placeBlock(world, stoneSlabs, cx - 1, bottom + 1, cx - 1, sbb);
			placeBlock(world, stoneBricks, cx, bottom, cx - 1, sbb);
			placeBlock(world, stoneBricks, cx + 1, bottom, cx - 1, sbb);
			placeBlock(world, stoneSlabs, cx + 1, bottom + 1, cx - 1, sbb);
			placeBlock(world, stoneBricks, cx - 1, bottom, cx, sbb);
			placeBlock(world, waterOrLava, cx, bottom, cx, sbb);
			placeBlock(world, stoneBricks, cx + 1, bottom, cx, sbb);
			placeBlock(world, stoneBricks, cx - 1, bottom, cx + 1, sbb);
			placeBlock(world, stoneSlabs, cx - 1, bottom + 1, cx + 1, sbb);
			placeBlock(world, stoneBricks, cx, bottom, cx + 1, sbb);
			placeBlock(world, stoneBricks, cx + 1, bottom, cx + 1, sbb);
			placeBlock(world, stoneSlabs, cx + 1, bottom + 1, cx + 1, sbb);
		}

		placeBlock(world, waterOrLava, cx, bottom - 1, cx, sbb);
	}

	/**
	 * Add a skeleton spawner on this floor and decorate it in an appropriately scary manner.
	 */
	protected void decorateSkeletonRoom(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// skeleton spawner
		setSpawner(world, size / 2, bottom + 2, size / 2, sbb, EntityType.SKELETON);

		// floor-to-ceiling chains
		ArrayList<BlockPos> chainList = new ArrayList<>();
		chainList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size + 2; i++) {
			BlockPos chain = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(chain, chainList)) {
				// if it doesn't collide, manufacture it and add it to the list
				for (int dy = bottom; dy < top; dy++) {
					placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), chain.getX(), dy, chain.getZ(), sbb);
				}
				chainList.add(chain);
			}
		}

		// spider webs in the corner
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				if (dx == 1 || dx == size - 2 || dz == 1 || dz == size - 2) {
					// side of the room
					if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
						// not an occupied position
						placeBlock(world, Blocks.COBWEB.defaultBlockState(), dx, top - 1, dz, sbb);
					}
				}
			}
		}

	}


	/**
	 * Add a zombie spawner on this floor and decorate it in an appropriately scary manner.
	 */
	protected void decorateZombieRoom(WorldGenLevel world, RandomSource rand, int bottom, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// zombie spawner
		setSpawner(world, size / 2, bottom + 2, size / 2, sbb, EntityType.ZOMBIE);
		final BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
		final BlockState soulSand = Blocks.SOUL_SAND.defaultBlockState();
		final BlockState brownMushroom = Blocks.BROWN_MUSHROOM.defaultBlockState();

		// random brown mushrooms
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
					// not an occupied position
					if (rand.nextInt(5) == 0) {
						placeBlock(world, brownMushroom, dx, bottom, dz, sbb);
					}
				}
			}
		}

		// slab tables
		ArrayList<BlockPos> slabList = new ArrayList<>();
		slabList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size - 1; i++) {
			BlockPos slab = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(slab, slabList)) {
				// if it doesn't collide, manufacture it and add it to the list

				placeBlock(world, ironBars, slab.getX(), bottom, slab.getZ(), sbb);
				placeBlock(world, TFStructureHelper.birchSlab, slab.getX(), bottom + 1, slab.getZ(), sbb);
				placeBlock(world, soulSand, slab.getX(), bottom + 2, slab.getZ(), sbb);
				slabList.add(slab);
			}
		}
	}

	/**
	 * Fill this room with sand and floor-to-ceiling cacti
	 */
	protected void decorateCactusRoom(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// sand & random dead bush
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				// sand
				placeBlock(world, Blocks.SAND.defaultBlockState(), dx, bottom - 1, dz, sbb);
				if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
					// not an occupied position
					if (rand.nextInt(4) == 0) {
						placeBlock(world, Blocks.DEAD_BUSH.defaultBlockState(), dx, bottom, dz, sbb);
					}
				}
			}
		}

		// cacti
		ArrayList<BlockPos> cactusList = new ArrayList<>();
		cactusList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size + 12; i++) {
			BlockPos cactus = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(cactus, cactusList)) {
				// if it doesn't collide, manufacture it and add it to the list
				for (int dy = bottom; dy < top; dy++) {
					placeBlock(world, Blocks.CACTUS.defaultBlockState(), cactus.getX(), dy, cactus.getZ(), sbb);
				}
				cactusList.add(cactus);
			}
		}
	}

	/**
	 * Decorate this floor with an enticing treasure chest.
	 */
	protected void decorateTreasureChest(WorldGenLevel world, int bottom, int top, BoundingBox sbb) {
		int cx = size / 2;
		final BlockState stoneBrick = Blocks.STONE_BRICKS.defaultBlockState();

		// bottom decoration
		final BlockState stoneBrickStairs = Blocks.STONE_BRICK_STAIRS.defaultBlockState();
		final BlockState topStoneBrickStairs = stoneBrickStairs.setValue(StairBlock.HALF, Half.TOP);

		placeBlock(world, stoneBrick, cx, bottom, cx, sbb);
		placeBlock(world, stoneBrick, cx, top-1, cx, sbb);
		if(size < 6) {
			surroundBlockCardinalRotated(world, stoneBrickStairs, cx, bottom, cx, sbb);

			surroundBlockCardinalRotated(world, topStoneBrickStairs, cx, top-1, cx, sbb);
		} else {
			surroundBlockCardinalRotated(world, stoneBrickStairs, cx, bottom, cx, sbb);
			surroundBlockCorners(world, stoneBrick, cx, bottom, cx, sbb);

			// pillars
			for (int cy = bottom + 1; cy < top - 1; cy++) {
				surroundBlockCorners(world, stoneBrick, cx, cy, cx, sbb);
			}

			surroundBlockCardinalRotated(world, topStoneBrickStairs, cx, top-1, cx, sbb);
			surroundBlockCorners(world, stoneBrick, cx, top-1, cx, sbb);
		}

		placeTreasureAtCurrentPosition(world, cx, bottom + 1, cx, TFLootTables.TOWER_ROOM, sbb);
	}

	/**
	 * Decorate this floor with a mass of messy spider webs.
	 */
	protected void decorateSpiderWebs(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		for (int dy = bottom; dy < top; dy++) {
			int chance = (top - dy + 2);
			for (int dx = 1; dx <= size - 2; dx++) {
				for (int dz = 1; dz <= size - 2; dz++) {
					if (!isLadderPos(dx, dz, ladderUpDir, ladderDownDir) && rand.nextInt(chance) == 0) {
						placeBlock(world, Blocks.COBWEB.defaultBlockState(), dx, dy, dz, sbb);
					}
				}
			}
		}

		// 20% chance of a spider spawner!
		if (rand.nextInt(5) == 0) {
			EntityType<?> spiderName = switch (rand.nextInt(4)) {
				case 3 -> EntityType.CAVE_SPIDER;
				case 2 -> TFEntities.SWARM_SPIDER.get();
				case 1 -> TFEntities.HEDGE_SPIDER.get();
				default -> EntityType.SPIDER;
			};

			setSpawner(world, size / 2, bottom + 2, size / 2, sbb, spiderName);

		} else {
			decorateFurniture(world, rand, bottom, size - 2, sbb);
		}
	}

	/**
	 * Place some furniture around the room.  This should probably only be called on larger towers.
	 */
	protected void decorateFurniture(WorldGenLevel world, RandomSource rand, int bottom, int freeSpace, BoundingBox sbb) {
		// 66% chance of a table
		if (rand.nextInt(3) > 0) {
			placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), size / 2, bottom, size / 2, sbb);
			placeBlock(world, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), size / 2, bottom + 1, size / 2, sbb);
		}

		// chairs!
		final BlockState spruceStairs = Blocks.SPRUCE_STAIRS.defaultBlockState();
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			placeBlock(world, spruceStairs.setValue(StairBlock.FACING, Direction.WEST), size / 2 + 1, bottom, size / 2, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			placeBlock(world, spruceStairs.setValue(StairBlock.FACING, Direction.NORTH), size / 2, bottom, size / 2 + 1, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			placeBlock(world, spruceStairs.setValue(StairBlock.FACING, Direction.EAST), size / 2 - 1, bottom, size / 2, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			placeBlock(world, spruceStairs.setValue(StairBlock.FACING, Direction.SOUTH), size / 2, bottom, size / 2 - 1, sbb);
		}
	}

	/**
	 * Decorate this floor with solid rock
	 */
	protected void decorateSolidRock(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		for (int dy = bottom; dy < top; dy++) {
			for (int dx = 1; dx <= size - 2; dx++) {
				for (int dz = 1; dz <= size - 2; dz++) {
					if (!isLadderPos(dx, dz, ladderUpDir, ladderDownDir) && rand.nextInt(9) != 0) {
						placeBlock(world, Blocks.STONE.defaultBlockState(), dx, dy, dz, sbb);
					}
				}
			}
		}

		//TODO: maybe seed a few ores in there.
	}

	/**
	 * Decorate this floor with an orderly library
	 */
	protected void decorateLibrary(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// put some bookshelves around the room
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				for (int dy = bottom; dy < top - 1; dy++) {
					if (dx == 1 || dx == size - 2 || dz == 1 || dz == size - 2) {
						// side of the room
						if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
							// not an occupied position
							placeBlock(world, Blocks.BOOKSHELF.defaultBlockState(), dx, dy, dz, sbb);
						}
					}
				}
			}
		}
		// treasure?!?!
		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateLibraryTreasure(world, rand, top, ladderUpDir, ladderDownDir, sbb);
		}

		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateFurniture(world, rand, bottom, size - 2, sbb);
		}
	}

	/**
	 * Place a library treasure chest somewhere in the library
	 */
	@SuppressWarnings("fallthrough")
	protected void decorateLibraryTreasure(WorldGenLevel world, RandomSource rand, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// FIXME: case 3 gets slightly higher chance than others
		switch (rand.nextInt(4)) {
			case 0:
			default:
				if (!isLadderPos(2, 1, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, 2, top - 2, 1, TFLootTables.TOWER_LIBRARY, sbb);
					break;
				}
			case 1:
				if (!isLadderPos(size - 2, 2, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, size - 2, top - 2, 2, TFLootTables.TOWER_LIBRARY, sbb);
					break;
				}
			case 2:
				if (!isLadderPos(size - 3, size - 2, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, size - 3, top - 2, size - 2, TFLootTables.TOWER_LIBRARY, sbb);
					break;
				}
			case 3:
				if (!isLadderPos(1, size - 3, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, 1, top - 2, size - 3, TFLootTables.TOWER_LIBRARY, sbb);
					break;
				}
		}
	}

	/**
	 * Decorate this floor with an overflowing library
	 */
	protected void decorateFullLibrary(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		// put some bookshelves around the room
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				for (int dy = bottom; dy < top; dy++) {
					if (dx % 2 != 0 && ((dz >= dx && dz <= size - dx - 1) || (dz >= size - dx - 1 && dz <= dx))
							|| dz % 2 != 0 && ((dx >= dz && dx <= size - dz - 1) || (dx >= size - dz - 1 && dx <= dz))) {
						// concentric rings
						if (!isWindowPos(dx, dy, dz) && !isOpeningPos(dx, dy, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
							// not an occupied position
							placeBlock(world, Blocks.BOOKSHELF.defaultBlockState(), dx, dy, dz, sbb);
						}
					}
				}
			}
		}
		// treasure?!?!
		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateLibraryTreasure(world, rand, top, ladderUpDir, ladderDownDir, sbb);
		}
	}

	/**
	 * "Decorate" with a lot of TNT.
	 * <p>
	 * This is not called at the moment, since I added monsters and the monsters set off the trap.  Perhaps I need a better way of activating it.
	 */
	protected void decorateTrap(WorldGenLevel world, int bottom, int top, BoundingBox sbb) {
		for (int dx = 2; dx <= size - 3; dx++) {
			for (int dz = 2; dz <= size - 3; dz++) {
				if (dx == 2 || dx == size - 3 || dz == 2 || dz == size - 3) {
					placeBlock(world, Blocks.TNT.defaultBlockState(), dx, -1, dz, sbb);
				}
			}
		}
		for (int dy = bottom - 2; dy < top - 2; dy++) {
			placeBlock(world, Blocks.TNT.defaultBlockState(), 1, dy, 1, sbb);
			placeBlock(world, Blocks.TNT.defaultBlockState(), 1, dy, size - 2, sbb);
			placeBlock(world, Blocks.TNT.defaultBlockState(), size - 2, dy, 1, sbb);
			placeBlock(world, Blocks.TNT.defaultBlockState(), size - 2, dy, size - 2, sbb);
		}
	}


	/**
	 * Checks if there is a window at the specified x and z.  Does not check the Y.
	 */
	protected boolean isWindowPos(int x, int z) {
		if (x == 1 && z == size / 2) {
			return true;
		}
		if (x == size - 2 && z == size / 2) {
			return true;
		}
		if (x == size / 2 && z == 1) {
			return true;
		}
		return x == size / 2 && z == size - 2;

		// okay, looks good
	}

	/**
	 * Checks if there is a window at the specified x, y, and z.
	 */
	protected boolean isWindowPos(int x, int y, int z) {
		int checkYDir = -1;

		if (x == 1 && z == size / 2) {
			checkYDir = 2;
		} else if (x == size - 2 && z == size / 2) {
			checkYDir = 0;
		} else if (x == size / 2 && z == 1) {
			checkYDir = 3;
		} else if (x == size / 2 && z == size - 2) {
			checkYDir = 1;
		}

		if (checkYDir > -1) {
			// check if we are at one of the Y positions with a window.
			return !openingTowards[checkYDir] && (y == 2 || y == 3 || (height > 8 && (y == height - 3 || y == height - 4)));
		} else {
			// okay, looks good
			return false;
		}
	}

	/**
	 * Checks if putting a block at the specified x, y, and z would block an opening.
	 * TODO: this could be much smarter.  Although since there's usually only one opening, I guess it's not bad.
	 */
	protected boolean isOpeningPos(int x, int y, int z) {
		for (BlockPos door : openings) {
			// determine which wall we're at
			BlockPos.MutableBlockPos inside = new BlockPos.MutableBlockPos(door.getX(), door.getY(), door.getZ());
			if (inside.getX() == 0) {
				inside.move(Direction.EAST);
			} else if (inside.getX() == size - 1) {
				inside.move(Direction.WEST);
			} else if (inside.getZ() == 0) {
				inside.move(Direction.SOUTH);
			} else if (inside.getZ() == size - 1) {
				inside.move(Direction.NORTH);
			}
			// check the block
			if (inside.getX() == x && inside.getZ() == z && (inside.getY() == y || inside.getY() + 1 == y)) {
				return true;
			}
		}
		// guess not!
		return false;
	}

	protected boolean isLadderPos(int x, int z, Rotation ladderUpDir, Rotation ladderDownDir) {
		if (ladderUpDir != null && x == getLadderX(ladderUpDir) && z == getLadderZ(ladderUpDir)) {
			return true;
		}
		return ladderDownDir != null && x == getLadderX(ladderDownDir) && z == getLadderZ(ladderDownDir);

		// okay, looks good
	}

	/**
	 * Gets the X coordinate of the ladder on the specified wall.
	 */
	protected int getLadderX(Rotation ladderDir) {
		return switch (ladderDir) {
			case NONE -> size - 2;
			case CLOCKWISE_90 -> size / 2 + 1;
			case CLOCKWISE_180 -> 1;
			case COUNTERCLOCKWISE_90 -> size / 2 - 1;
			//default -> size / 2;
		};
	}

	/**
	 * Gets the Z coordinate of the ladder on the specified wall.
	 */
	protected int getLadderZ(Rotation ladderDir) {
		return switch (ladderDir) {
			case NONE -> size / 2 - 1;
			case CLOCKWISE_90 -> size - 2;
			case CLOCKWISE_180 -> size / 2 + 1;
			case COUNTERCLOCKWISE_90 -> 1;
			//default -> size / 2;
		};
	}

	/**
	 * Decorate a tower with stairs.
	 * <p>
	 * We have two schemes here.  We can either decorate the whole tower with a
	 * decoration that rises the entire height of the tower (such as a pillar)
	 * or we can divide the tower into the "stair" section on the bottom and the
	 * "attic" section at the top and decorate those seperately.
	 */
	protected void decorateStairTower(WorldGenLevel world, ChunkGenerator generator, RandomSource rand, BoundingBox sbb) {

		// if it's tall enough, consider adding extra floors onto the top.
		if (height - highestOpening > 8) {
			int base = highestOpening + 3;
			int floors = (this.height - base) / 5;

			// divide the tower into floors
			int floorHeight = (this.height - base) / floors;
			for (int i = 0; i < floors; i++) {
				// put down a floor
				for (int x = 1; x < size - 1; x++) {
					for (int z = 1; z < size - 1; z++) {
						placeBlock(world, TFStructureHelper.birchPlanks, x, (i * floorHeight + base), z, sbb);
					}
				}
			}

			Rotation ladderDir = Rotation.NONE;
			Rotation downLadderDir;

			// place a ladder going up
			//TODO: make this ladder connect better to the stairs
			int dx = getLadderX(ladderDir);
			int dz = getLadderZ(ladderDir);
			final BlockState defaultState = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, ladderDir.rotate(Direction.EAST));
			for (int dy = 1; dy < 3; dy++) {
				placeBlock(world, defaultState, dx, base - dy, dz, sbb);
			}

			// decorate middle floors
			for (int i = 0; i < floors - 1; i++) {
				int bottom = base + 1 + floorHeight * i;
				int top = base + floorHeight * (i + 1);

				downLadderDir = ladderDir;
				ladderDir = ladderDir.getRotated(Rotation.CLOCKWISE_90);

				decorateFloor(world, rand, i, bottom, top, ladderDir, downLadderDir, sbb);
			}

			// decorate top floor
			decorateFloor(world, rand, floors, base + 1 + floorHeight * (floors - 1), height - 1, null, ladderDir, sbb);

			// decorate below the bottom floor, into the stairs
			if (base > 8) {
				switch (rand.nextInt(4)) {
					case 0 -> decorateChandelier(world, rand, base + 1, sbb);
					case 1 -> decorateHangingChains(world, rand, base + 1, sbb);
					case 2 -> decorateFloatingBooks(world, rand, base + 1, sbb);
					case 3 -> decorateFloatingVines(world, rand, base + 1, sbb);
				}
			}
		} else {
			// decorate the top normally
			if (size > 5) {
				switch (rand.nextInt(4)) {
					case 0 -> decorateChandelier(world, rand, height, sbb);
					case 1 -> decorateHangingChains(world, rand, height, sbb);
					case 2 -> decorateFloatingBooks(world, rand, height, sbb);
					case 3 -> decorateFloatingVines(world, rand, height, sbb);
				}
			} else if (size > 3) {
				switch (rand.nextInt(3)) {
					case 0 -> decorateHangingChains(world, rand, height, sbb);
					case 1 -> decorateFloatingBooks(world, rand, height, sbb);
					case 2 -> decorateFloatingVines(world, rand, height, sbb);
				}
			}
		}

		decorateStairFloor(world, generator, rand, sbb);
	}

	/**
	 * Decorate the bottom floor of this tower.
	 * <p>
	 * This is for towers with stairs at the bottom, not towers divided into floors
	 */
	protected void decorateStairFloor(WorldGenLevel world, ChunkGenerator generator, RandomSource rand, BoundingBox sbb) {
		// decorate the bottom
		if (size > 5) {
			if (rand.nextInt(3) == 0) {
				decorateStairWell(world, rand, sbb);
			} else if (rand.nextInt(3) > 0 || this.size >= 15) {
				// a few empty bottoms
				decoratePlanter(world, generator, rand, sbb);
			}
		}
	}

	/**
	 * Make a chandelier.  The chandelier hangs down a random amount between the top of the tower and the highest opening.
	 */
	protected void decorateChandelier(WorldGenLevel world, RandomSource rand, int decoTop, BoundingBox sbb) {
		if (decoTop < 8 || size < 8) {
			return;
		}

		int cx = size / 2;
		int cy = decoTop - rand.nextInt(decoTop - 7) - 2;
		int cz = size / 2;

		final BlockState oakFence = Blocks.OAK_FENCE.defaultBlockState();

		//setDebugEntity(world, cx, cy, cz, sbb, "TowerWing.decorateChandelier");
		surroundBlockCardinal(world, oakFence, cx, cy, cz, sbb);
		surroundBlockCardinal(world, oakFence, cx, cy + 1, cz, sbb);

		for (int y = cy; y < decoTop - 1; y++) {
			placeBlock(world, oakFence, cx, y, cz, sbb);
		}
	}

	/**
	 * Decorates a tower with chains hanging down.
	 * <p>
	 * The chains go from the ceiling to just above the highest doorway.
	 */
	protected void decorateHangingChains(WorldGenLevel world, RandomSource rand, int decoTop, BoundingBox sbb) {
		// a list of existing chains
		ArrayList<BlockPos> chainList = new ArrayList<BlockPos>();
		// try size + 2 times to find a chain that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos chain = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(chain, chainList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int length = 1 + rand.nextInt(decoTop - 7);
				decorateOneChain(world, rand, chain.getX(), decoTop, length, chain.getZ(), sbb);
				chainList.add(chain);
			}
		}
	}

	/**
	 * Return true if the specified coords are orthogonally adjacent to any other coords on the list.
	 */
	protected boolean chainCollides(BlockPos coords, List<BlockPos> list) {
		for (BlockPos existing : list) {
			// if x is within 1 and z is equal, we collide
			if (coords.getZ() == existing.getZ() && Math.abs(coords.getX() - existing.getX()) <= 1) {
				return true;
			}
			// similarly, if z is within 1 and x is equal, we collide
			if (coords.getX() == existing.getX() && Math.abs(coords.getZ() - existing.getZ()) <= 1) {
				return true;
			}
		}
		// we're good
		return false;
	}

	protected void decorateOneChain(WorldGenLevel world, RandomSource rand, int dx, int decoTop, int length, int dz, BoundingBox sbb) {
		for (int y = 1; y <= length; y++) {
			placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx, decoTop - y - 1, dz, sbb);
		}
		// make the "ball" at the end.
		BlockState ballBlock = switch (rand.nextInt(10)) {
			case 0 -> Blocks.IRON_BLOCK.defaultBlockState();
			case 1 -> Blocks.BOOKSHELF.defaultBlockState();
			case 2 -> Blocks.NETHERRACK.defaultBlockState();
			case 3 -> Blocks.SOUL_SAND.defaultBlockState();
			case 4 -> Blocks.GLASS.defaultBlockState();
			case 5 -> Blocks.LAPIS_BLOCK.defaultBlockState();
			case 6 -> Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
			default -> Blocks.GLOWSTONE.defaultBlockState();
		};
		placeBlock(world, ballBlock, dx, decoTop - length - 2, dz, sbb);
	}

	/**
	 * Decorates a tower with an array of floating bookshelves.
	 */
	protected void decorateFloatingBooks(WorldGenLevel world, RandomSource rand, int decoTop, BoundingBox sbb) {
		// a list of existing bookshelves
		ArrayList<BlockPos> shelfList = new ArrayList<>();
		// try size + 2 times to find a shelf that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos shelf = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(shelf, shelfList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int bottom = 2 + rand.nextInt(decoTop - 7);
				int top = rand.nextInt(bottom - 1) + 2;
				for (int y = top; y <= bottom; y++) {
					placeBlock(world, Blocks.BOOKSHELF.defaultBlockState(), shelf.getX(), decoTop - y, shelf.getZ(), sbb);
				}
				shelfList.add(shelf);
			}
		}
	}

	/**
	 * Decorates a tower with an array of floating vines, attached to mossy cobblestone.
	 */
	protected void decorateFloatingVines(WorldGenLevel world, RandomSource rand, int decoTop, BoundingBox sbb) {
		final BlockState mossyCobbleStone = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
		final BlockState vine = Blocks.VINE.defaultBlockState();
		final BlockState vineNorth = vine.setValue(VineBlock.NORTH, true);
		final BlockState vineSouth = vine.setValue(VineBlock.SOUTH, true);
		final BlockState vineEast = vine.setValue(VineBlock.EAST, true);
		final BlockState vineWest = vine.setValue(VineBlock.WEST, true);

		// a list of existing blocks
		ArrayList<BlockPos> mossList = new ArrayList<>();
		// try size + 2 times to find a rock pillar that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos moss = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(moss, mossList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int bottom = 2 + rand.nextInt(decoTop - 7);
				int top = rand.nextInt(bottom - 1) + 2;
				for (int y = top; y <= bottom; y++) {
					placeBlock(world, mossyCobbleStone, moss.getX(), decoTop - y, moss.getZ(), sbb);
					// surround it with vines
					placeBlock(world, vineEast, moss.getX() + 1, decoTop - y, moss.getZ(), sbb);
					placeBlock(world, vineWest, moss.getX() - 1, decoTop - y, moss.getZ(), sbb);
					placeBlock(world, vineSouth, moss.getX(), decoTop - y, moss.getZ() + 1, sbb);
					placeBlock(world, vineNorth, moss.getX(), decoTop - y, moss.getZ() - 1, sbb);
				}
				mossList.add(moss);
			}
		}

		// put vines on the sides of the tower.
		for (int y = highestOpening + 3; y < decoTop - 1; y++) {
			for (int x = 1; x < size - 1; x++) {
				if (rand.nextInt(3) == 0) {
					placeBlock(world, vineSouth, x, y, 1, sbb);
				}
				if (rand.nextInt(3) == 0) {
					placeBlock(world, vineNorth, x, y, size - 2, sbb);
				}
			}
			for (int z = 1; z < size - 1; z++) {
				if (rand.nextInt(3) == 0) {
					placeBlock(world, vineEast, 1, y, z, sbb);
				}
				if (rand.nextInt(3) == 0) {
					placeBlock(world, vineWest, size - 2, y, z, sbb);
				}
			}
		}
	}

	/**
	 * Makes a planter.  Depending on the situation, it can be filled with trees, flowers, or crops
	 */
	protected void decoratePlanter(WorldGenLevel world, ChunkGenerator generator, RandomSource rand, BoundingBox sbb) {
		int cx = size / 2;

		surroundBlockCardinal(world, TFStructureHelper.stoneSlab, cx, 1, cx, sbb);

		if (size > 7)
			surroundBlockCorners(world, TFStructureHelper.stoneSlabDouble, cx, 1, cx, sbb);


		// place a cute planted thing
		placeBlock(world, Blocks.GRASS_BLOCK.defaultBlockState(), cx, 1, cx, sbb);

		int i = rand.nextInt(6);
		final BlockState plant = TFStructureHelper.randomPlant(i);

		final BlockPos pos = getBlockPosWithOffset(cx, 2, cx);

		if (i > 4 && !world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(TFStructureHelper.randomTree(rand.nextInt(4))).place(world, generator, world.getRandom(), pos)) {
			//if tree placement fails, place the potted sapling
			this.placeBlock(world, plant, cx, 2, cx, sbb);
		} else {
			this.placeBlock(world, plant, cx, 2, cx, sbb);
		}
	}

	/**
	 * Decorate the floor of this stair tower with a scenic well.
	 */
	protected void decorateStairWell(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		int cx = size / 2;
		int cy = 1;

		final BlockState waterOrLava = rand.nextInt(4) == 0 ? Blocks.LAVA.defaultBlockState() : Blocks.WATER.defaultBlockState();

		final BlockState stoneSlab = Blocks.SMOOTH_STONE_SLAB.defaultBlockState();
		final BlockState stoneBrick = Blocks.STONE_BRICKS.defaultBlockState();

		if (size > 7) {
			// actual well structure
			placeBlock(world, stoneBrick, cx - 1, cy, cx - 1, sbb);
			placeBlock(world, stoneSlab, cx - 1, cy + 1, cx - 1, sbb);
			placeBlock(world, stoneBrick, cx, cy, cx - 1, sbb);
			placeBlock(world, stoneBrick, cx + 1, cy, cx - 1, sbb);
			placeBlock(world, stoneSlab, cx + 1, cy + 1, cx - 1, sbb);
			placeBlock(world, stoneBrick, cx - 1, cy, cx, sbb);
			placeBlock(world, waterOrLava, cx, cy, cx, sbb);
			placeBlock(world, stoneBrick, cx + 1, cy, cx, sbb);
			placeBlock(world, stoneBrick, cx - 1, cy, cx + 1, sbb);
			placeBlock(world, stoneSlab, cx - 1, cy + 1, cx + 1, sbb);
			placeBlock(world, stoneBrick, cx, cy, cx + 1, sbb);
			placeBlock(world, stoneBrick, cx + 1, cy, cx + 1, sbb);
			placeBlock(world, stoneSlab, cx + 1, cy + 1, cx + 1, sbb);
		}

		placeBlock(world, waterOrLava, cx, 0, cx, sbb);
	}

	/**
	 * Returns true if this tower has only one exit.
	 */
	public boolean isDeadEnd() {
		return openings.size() == 1;
	}

	/**
	 * Iterate through the openings on our list and add them to the tower
	 */
	protected void makeOpenings(WorldGenLevel world, BoundingBox sbb) {
		for (BlockPos door : openings) {
			makeDoorOpening(world, door.getX(), door.getY(), door.getZ(), sbb);
		}
	}

	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	protected void makeDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		placeBlock(world, AIR, dx, dy, dz, sbb);
		placeBlock(world, AIR, dx, dy + 1, dz, sbb);

		if (getBlock(world, dx, dy + 2, dz, sbb).getBlock() != Blocks.AIR) {
			BlockState state = TFStructureHelper.stoneSlabDouble;
			placeBlock(world, state, dx, dy + 2, dz, sbb);
		}
	}

	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	public int[] getValidOpening(RandomSource rand, Rotation direction) {
		// variables!
		int wLength = size - 2; // wall length
		int offset = 1; // wall thickness

		// size 15 towers have funny landings, so don't generate on the very edge
		if (this.size == 15) {
			wLength = 11;
			offset = 2;
		}

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
	protected int getYByStairs(int rx, RandomSource rand, Rotation direction) {
		// initialize some variables
		int rise = 1;
		int base = 0;

		if (size == 15) {
			rise = 10;
			// we lie a little here to get the towers off the ground
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 23 : 28;
		}
		if (size == 9) {
			rise = 6;
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 2 : 5;
		}
		if (size == 7) {
			rise = 4;
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 2 : 4;
		}
		if (size == 5) {
			rise = 4;
			// bleh, a switch.
			base = switch (direction) {
				case NONE -> 3;
				case CLOCKWISE_90 -> 2;
				case CLOCKWISE_180 -> 5;
				case COUNTERCLOCKWISE_90 -> 4;
			};
		}

		int flights = ((height - 6 - base) / rise) + 1;

		if (base > 0 && flights > 0) {
			// pick a flight of stairs to be on
			int flightChosen = rand.nextInt(flights);
			// calculate where we would be if rx = 0
			int dy = (flightChosen * rise) + base;
			// the staircase (a/de)scends across the room.
			if (size == 15) {
				// blech, another dumb kludge here
				dy -= (direction == Rotation.NONE || direction == Rotation.COUNTERCLOCKWISE_90) ? (rx - 2) / 2 : (size - rx - 3) / 2;
			} else {
				// the rest are fairly normal
				dy -= (direction == Rotation.NONE || direction == Rotation.COUNTERCLOCKWISE_90) ? (rx - 1) / 2 : (size - rx - 2) / 2;
			}
//			// even xs can be one higher if they want.
//			if (rx % 2 == 0 && size != 15) {
//				dy += rand.nextInt(2);
//			}
			// don't go through the floor
			if (dy < 1) {
				dy = 1;
			}

			return dy;
		}

		return 0;
	}

	/**
	 * Makes 3 windows outside this tower.
	 * <p>
	 * The function currently looks "outside" to see if the window will be blocked, but it can't see into the future, so a tower built after this one may block it.
	 * <p>
	 * Maybe this could eventually have access to the list of bounding boxes for better accuracy?
	 */
	protected void makeWindows(WorldGenLevel world, BoundingBox sbb, boolean real) {

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			boolean realWindows = real && !openingTowards[rotation.ordinal()];
			makeWindowBlock(world, size - 1, 2, size / 2, rotation, sbb, realWindows);
			makeWindowBlock(world, size - 1, 3, size / 2, rotation, sbb, realWindows);
			makeWindowBase(world, size - 1, 1, size / 2, rotation, sbb);
			if (height > 8) {
				makeWindowBlock(world, size - 1, height - 3, size / 2, rotation, sbb, realWindows);
				makeWindowBlock(world, size - 1, height - 4, size / 2, rotation, sbb, realWindows);
				makeWindowBase(world, size - 1, height - 5, size / 2, rotation, sbb);
			}
		}
	}

	/**
	 * Makes a window block.  Specify a point in a wall, and this function checks to
	 * see if it is blocked on the inside or outside, and if not, adds a pane of glass.
	 */
	protected void makeWindowBlock(WorldGenLevel world, int x, int y, int z, Rotation rotation, BoundingBox sbb, boolean realWindows) {
		Direction temp = this.getOrientation();
		this.setOrientation(rotation.rotate(temp));

		// look outside
		Block outside = getBlock(world, x + 1, y, z, sbb).getBlock();

		// look inside
		Block inside = getBlock(world, x - 1, y, z, sbb).getBlock();

		// make a window!
		if (realWindows && inside == Blocks.AIR && outside == Blocks.AIR) {
			placeBlock(world, Blocks.GLASS_PANE.defaultBlockState(), x, y, z, sbb);
		} else {
			// cobblestone where the window might have been
			placeBlock(world, Blocks.COBBLESTONE.defaultBlockState(), x, y, z, sbb);
		}

		this.setOrientation(temp);
	}

	/**
	 * Makes a window base
	 */
	protected void makeWindowBase(WorldGenLevel world, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		Direction temp = this.getOrientation();
		this.setOrientation(rotation.rotate(temp));
		BlockState state = TFStructureHelper.stoneSlabDouble;
		placeBlock(world, state, x, y, z, sbb);
		this.setOrientation(temp);
	}

	/**
	 * Add stairs to this tower.
	 */
	protected boolean makeStairs(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		if (this.size == 15) {
			return makeStairs15(world, rand, sbb);
		}
		if (this.size == 9) {
			return makeStairs9(world, rand, sbb);
		}
		if (this.size == 7) {
			return makeStairs7(world, rand, sbb);
		}
		if (this.size == 5) {
			return makeStairs5(world, rand, sbb);
		}

		return false;
	}

	/**
	 * Stair maker for a size 5 tower
	 */
	protected boolean makeStairs5(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		// staircases rotating around the tower
		int rise = 1;
//		int numFlights = ((this.height - 3) / rise) - 1;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs5flight(world, sbb, i * rise, getRotation(Rotation.NONE, i * 3), true);
		}

		return true;
	}

	/**
	 * Function called by makeStairs5 to place stair blocks
	 */
	protected void makeStairs5flight(WorldGenLevel world, BoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		Direction temp = this.getOrientation();

		this.setOrientation(rotation.rotate(temp));

		final BlockState bottomSlab = useBirchWood ?
				TFStructureHelper.birchSlab :
				TFStructureHelper.stoneSlab;
		final BlockState topSlab = useBirchWood ?
				TFStructureHelper.birchSlabTop :
				TFStructureHelper.stoneSlabTop;

		placeBlock(world, bottomSlab, 2, 1 + height, 3, sbb);
		placeBlock(world, topSlab, 3, 1 + height, 3, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Stair maker for a size 7 tower
	 */
	protected boolean makeStairs7(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		// foot of stairs
		placeBlock(world, TFStructureHelper.birchSlab, 1, 1, 4, sbb);
		placeBlock(world, TFStructureHelper.birchSlabTop, 1, 1, 5, sbb);

		placeBlock(world, TFStructureHelper.stoneSlab, 5, 1, 2, sbb);
		placeBlock(world, TFStructureHelper.stoneSlabTop, 5, 1, 1, sbb);

		// staircases rotating around the tower
		int rise = 2;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs7flight(world, sbb, 1 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs7flight(world, sbb, 1 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 */
	protected void makeStairs7flight(WorldGenLevel world, BoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		final Direction temp = this.getOrientation();

		this.setOrientation(rotation.rotate(temp));
		final BlockState slabBottom = useBirchWood ?
				TFStructureHelper.birchSlab :
				TFStructureHelper.stoneSlab;
		final BlockState slabTop = useBirchWood ?
				TFStructureHelper.birchSlabTop :
				TFStructureHelper.stoneSlabTop;

		placeBlock(world, slabBottom, 2, 1 + height, 5, sbb);
		placeBlock(world, slabTop, 3, 1 + height, 5, sbb);
		placeBlock(world, slabBottom, 4, 2 + height, 5, sbb);
		placeBlock(world, slabTop, 5, 2 + height, 5, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Stair maker for a size 9 tower
	 */
	protected boolean makeStairs9(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {

		// foot of stairs
		placeBlock(world, TFStructureHelper.birchSlab, 1, 1, 6, sbb);
		placeBlock(world, TFStructureHelper.birchSlabTop, 1, 1, 7, sbb);

		placeBlock(world, TFStructureHelper.stoneSlab, 7, 1, 2, sbb);
		placeBlock(world, TFStructureHelper.stoneSlabTop, 7, 1, 1, sbb);

		// staircases rotating around the tower
		int rise = 3;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs9flight(world, sbb, 1 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs9flight(world, sbb, 1 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 */
	protected void makeStairs9flight(WorldGenLevel world, BoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		//TODO: Can we just... not do this?
		Direction temp = this.getOrientation();
		this.setOrientation(rotation.rotate(temp));

		final BlockState slabBot = useBirchWood ?
				TFStructureHelper.birchSlab :
				TFStructureHelper.stoneSlab;
		final BlockState slabTop = useBirchWood ?
				TFStructureHelper.birchSlabTop :
				TFStructureHelper.stoneSlabTop;

		placeBlock(world, slabBot, 2, 1 + height, 7, sbb);
		placeBlock(world, slabTop, 3, 1 + height, 7, sbb);
		placeBlock(world, slabBot, 4, 2 + height, 7, sbb);
		placeBlock(world, slabTop, 5, 2 + height, 7, sbb);
		placeBlock(world, slabBot, 6, 3 + height, 7, sbb);
		placeBlock(world, slabTop, 7, 3 + height, 7, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Stair maker for a size 15 tower
	 */
	protected boolean makeStairs15(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		final BlockState planks = Blocks.BIRCH_PLANKS.defaultBlockState();
		final BlockState oakFence = Blocks.OAK_FENCE.defaultBlockState();
		final BlockState birchSlab = TFStructureHelper.birchSlab;
		final BlockState stoneSlab = TFStructureHelper.stoneSlab;
		final BlockState doubleStoneSlab = TFStructureHelper.stoneSlabDouble;

		// foot of stairs
		placeBlock(world, birchSlab, 1, 1, 9, sbb);
		placeBlock(world, birchSlab, 2, 1, 9, sbb);

		placeBlock(world, planks, 1, 1, 10, sbb);
		placeBlock(world, planks, 2, 1, 10, sbb);
		placeBlock(world, birchSlab, 1, 2, 11, sbb);
		placeBlock(world, birchSlab, 2, 2, 11, sbb);
		placeBlock(world, planks, 1, 2, 12, sbb);
		placeBlock(world, planks, 2, 2, 12, sbb);
		placeBlock(world, planks, 1, 2, 13, sbb);
		placeBlock(world, planks, 2, 2, 13, sbb);

		placeBlock(world, planks, 3, 2, 11, sbb);
		placeBlock(world, oakFence, 3, 3, 11, sbb);
		placeBlock(world, oakFence, 3, 4, 11, sbb);
		placeBlock(world, planks, 3, 1, 10, sbb);
		placeBlock(world, oakFence, 3, 2, 10, sbb);
		placeBlock(world, oakFence, 3, 3, 10, sbb);
		placeBlock(world, planks, 3, 1, 9, sbb);
		placeBlock(world, oakFence, 3, 2, 9, sbb);

		placeBlock(world, stoneSlab, 13, 1, 5, sbb);
		placeBlock(world, stoneSlab, 12, 1, 5, sbb);
		placeBlock(world, doubleStoneSlab, 13, 1, 4, sbb);
		placeBlock(world, doubleStoneSlab, 12, 1, 4, sbb);
		placeBlock(world, stoneSlab, 13, 2, 3, sbb);
		placeBlock(world, stoneSlab, 12, 2, 3, sbb);
		placeBlock(world, doubleStoneSlab, 13, 2, 2, sbb);
		placeBlock(world, doubleStoneSlab, 12, 2, 2, sbb);
		placeBlock(world, doubleStoneSlab, 13, 2, 1, sbb);
		placeBlock(world, doubleStoneSlab, 12, 2, 1, sbb);
		placeBlock(world, doubleStoneSlab, 11, 2, 3, sbb);
		placeBlock(world, oakFence, 11, 3, 3, sbb);
		placeBlock(world, oakFence, 11, 4, 3, sbb);
		placeBlock(world, doubleStoneSlab, 11, 1, 4, sbb);
		placeBlock(world, oakFence, 11, 2, 4, sbb);
		placeBlock(world, oakFence, 11, 3, 4, sbb);
		placeBlock(world, doubleStoneSlab, 11, 1, 5, sbb);
		placeBlock(world, oakFence, 11, 2, 5, sbb);


		// staircases rotating around the tower
		int rise = 5;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs15flight(world, rand, sbb, 2 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs15flight(world, rand, sbb, 2 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	private Rotation getRotation(Rotation startRotation, int rotations) {
		final int totalIncrements = startRotation.ordinal() + rotations;
		return RotationUtil.ROTATIONS[totalIncrements & 3];
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 * pretty sure this is the one that makes the main staircase
	 */
	protected void makeStairs15flight(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		Direction temp = this.getOrientation();

		this.setOrientation(rotation.rotate(temp));

		final BlockState oakFence = Blocks.OAK_FENCE.defaultBlockState();

		final BlockState slabBot = useBirchWood ?
				TFStructureHelper.birchSlab :
				TFStructureHelper.stoneSlab;
		final BlockState slabTop = useBirchWood ?
				TFStructureHelper.birchSlabTop :
				TFStructureHelper.stoneSlabTop;
		final BlockState slabDoub = useBirchWood ?
				TFStructureHelper.birchPlanks :
				TFStructureHelper.stoneSlabDouble;

		placeBlock(world, slabBot, 3, 1 + height, 13, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 4, 1 + height, 13, slabTop);
		placeBlock(world, slabBot, 5, 2 + height, 13, sbb);
		placeBlock(world, slabTop, 6, 2 + height, 13, sbb);
		placeBlock(world, slabBot, 7, 3 + height, 13, sbb);
		placeBlock(world, slabTop, 8, 3 + height, 13, sbb);
		placeBlock(world, slabBot, 9, 4 + height, 13, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 10, 4 + height, 13, slabTop);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 11, 5 + height, 13, slabBot);
		placeBlock(world, slabTop, 12, 5 + height, 13, sbb);
		placeBlock(world, slabTop, 13, 5 + height, 13, sbb);

		maybeGenerateBlock(world, sbb, rand, 0.9F, 3, 1 + height, 12, slabBot);
		placeBlock(world, slabTop, 4, 1 + height, 12, sbb);
		placeBlock(world, slabBot, 5, 2 + height, 12, sbb);
		placeBlock(world, slabTop, 6, 2 + height, 12, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 7, 3 + height, 12, slabBot);
		placeBlock(world, slabTop, 8, 3 + height, 12, sbb);
		placeBlock(world, slabBot, 9, 4 + height, 12, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 10, 4 + height, 12, slabTop);
		placeBlock(world, slabBot, 11, 5 + height, 12, sbb);
		placeBlock(world, slabTop, 12, 5 + height, 12, sbb);
		placeBlock(world, slabTop, 13, 5 + height, 12, sbb);

		placeBlock(world, slabDoub, 4, 1 + height, 11, sbb);
		placeBlock(world, slabDoub, 5, 2 + height, 11, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 6, 2 + height, 11, slabTop);
		placeBlock(world, slabDoub, 7, 3 + height, 11, sbb);
		maybeGenerateBlock(world, sbb, rand, 0.9F, 8, 3 + height, 11, slabTop);
		placeBlock(world, slabDoub, 9, 4 + height, 11, sbb);
		placeBlock(world, slabTop, 10, 4 + height, 11, sbb);
		placeBlock(world, slabDoub, 11, 5 + height, 11, sbb);

		placeBlock(world, oakFence, 4, 2 + height, 11, sbb);
		placeBlock(world, oakFence, 5, 3 + height, 11, sbb);
		placeBlock(world, oakFence, 6, 3 + height, 11, sbb);
		placeBlock(world, oakFence, 7, 4 + height, 11, sbb);
		placeBlock(world, oakFence, 8, 4 + height, 11, sbb);
		placeBlock(world, oakFence, 9, 5 + height, 11, sbb);
		placeBlock(world, oakFence, 10, 5 + height, 11, sbb);
		placeBlock(world, oakFence, 11, 6 + height, 11, sbb);

		placeBlock(world, oakFence, 4, 3 + height, 11, sbb);
		placeBlock(world, oakFence, 6, 4 + height, 11, sbb);
		placeBlock(world, oakFence, 8, 5 + height, 11, sbb);
		placeBlock(world, oakFence, 10, 6 + height, 11, sbb);
		placeBlock(world, oakFence, 11, 7 + height, 11, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Makes paintings of the minimum size or larger on the specified wall
	 */
	protected void generatePaintingsOnWall(WorldGenLevel world, RandomSource rand, int howMany, int floorLevel, Direction direction, int minSize, BoundingBox sbb) {
		for (int i = 0; i < howMany; i++) {
			// get some random coordinates on the wall in the chunk
			BlockPos pCoords = getRandomWallSpot(rand, floorLevel, direction, sbb);

			if (pCoords == null) continue;
			// initialize a painting object
			EntityUtil.tryHangPainting(world, pCoords, direction, EntityUtil.getPaintingOfSize(rand, minSize));
		}
	}

	/**
	 * This returns the real-world coordinates of a possible painting or torch spot on the specified wall of this tower.
	 */
	@Nullable
	protected BlockPos getRandomWallSpot(RandomSource rand, int floorLevel, Direction direction, BoundingBox sbb) {
		int minX = this.boundingBox.minX() + 2;
		int maxX = this.boundingBox.maxX() - 2;

		int minY = this.boundingBox.minY() + floorLevel + 2;
		int maxY = this.boundingBox.maxY() - 2;

		int minZ = this.boundingBox.minZ() + 2;
		int maxZ = this.boundingBox.maxZ() - 2;

		// constrain the paintings to one wall
		// these directions correspond to painting facing directions, not necessarily to the structure orienting directions
		if (direction == Direction.SOUTH) {
			minZ = this.boundingBox.minZ();
			maxZ = this.boundingBox.minZ();
		}
		else if (direction == Direction.WEST) {
			maxX = this.boundingBox.maxX();
			minX = this.boundingBox.maxX();
		}
		else if (direction == Direction.NORTH) {
			maxZ = this.boundingBox.maxZ();
			minZ = this.boundingBox.maxZ();
		}
		else if (direction == Direction.EAST) {
			minX = this.boundingBox.minX();
			maxX = this.boundingBox.minX();
		}

		// try 30 times to get a proper result
		for (int i = 0; i < 30; i++) {
			int cx = minX + (maxX > minX ? rand.nextInt(maxX - minX) : 0);
			int cy = minY + (maxY > minY ? rand.nextInt(maxY - minY) : 0);
			int cz = minZ + (maxZ > minZ ? rand.nextInt(maxZ - minZ) : 0);

			final BlockPos blockPos = new BlockPos(cx, cy, cz).relative(direction);
			if (sbb.isInside(blockPos)) {
				return blockPos;
			}
		}

		// I guess we didn't get one
		TwilightForestMod.LOGGER.info("ComponentTFTowerWing#getRandomWallSpot - We didn't find a valid random spot on the wall.");
		return null;
	}

	/**
	 * This method is for final castle towers actually.
	 * We need to break up this class into a more abstract tower class and a concrete lich tower class one day
	 */
	protected void makeGlyphBranches(WorldGenLevel world, RandomSource rand, BlockState colour, BoundingBox sbb) {
		// pick a random side of the tower
		Rotation rotation = RotationUtil.ROTATIONS[rand.nextInt(4)];

		// start somewhere in the lower part
		int startHeight = this.height > 1 ? rand.nextInt((int) (this.height * 0.66F)) : this.height;

		// near the middle
		int startZ = 3 + rand.nextInt(this.size - 6);

		// make a line all the way down to the foundation
		int dx = this.getXWithOffsetRotated(0, startZ, rotation);
		int dz = this.getZWithOffsetRotated(0, startZ, rotation);
		if (sbb.isInside(new BlockPos(dx, this.boundingBox.minY() + 1, dz))) {
			for (int dy = this.getWorldY(startHeight); dy > 0; dy--) {
				final BlockPos pos = new BlockPos(dx, dy, dz);
				if (world.getBlockState(pos).is(BlockTagGenerator.CASTLE_BLOCKS) && world.getBlockState(pos).isRedstoneConductor(world, pos)) {
					world.setBlock(pos, colour, 2);
				} else {
					break;
				}
			}
		}

		// go left a little
		int leftOffset = startZ - (1 + rand.nextInt(3));
		int leftHeight = rand.nextInt(Math.max(this.height - startHeight, 1));
		if (leftOffset >= 0) {
			for (int z = startZ; z > leftOffset; z--) {
				this.setBlockStateRotated(world, colour, 0, startHeight, z, rotation, sbb);
			}
			for (int y = startHeight; y < (startHeight + leftHeight); y++) {
				this.setBlockStateRotated(world, colour, 0, y, leftOffset, rotation, sbb);
			}
		}

		// go right a little
		int rightOffset = startZ + (1 + rand.nextInt(3));
		int rightHeight = rand.nextInt(Math.max(this.height - startHeight, 1));
		if (rightOffset < this.size - 1) {
			for (int z = startZ; z < rightOffset; z++) {
				this.setBlockStateRotated(world, colour, 0, startHeight, z, rotation, sbb);
			}
			for (int y = startHeight; y < (startHeight + rightHeight); y++) {
				this.setBlockStateRotated(world, colour, 0, y, rightOffset, rotation, sbb);
			}
		}
	}

	@Override
	public int getGroundLevelDelta() {
		return 1;
	}
}
