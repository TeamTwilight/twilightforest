package twilightforest.world.components.structures.icetower;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.RotationUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.icetower.floordecorators.FloorParts;
import twilightforest.world.components.structures.icetower.floordecorators.FloorTypesAuroraPalace;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class IceTowerWingComponent extends TowerWingComponent {

	protected static final int SIZE = 11;
	private static final int RANGE = (int) (SIZE * 1.6F);

	boolean hasBase = false;
	protected int treasureFloor = -1;
	private boolean hasBoss;

	public IceTowerWingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFITWin.get(), nbt);
	}

	public IceTowerWingComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.hasBase = nbt.getBoolean("hasBase");
		this.treasureFloor = nbt.getInt("treasureFloor");
	}

	protected IceTowerWingComponent(StructurePieceType piece, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(piece, i, x, y, z, pSize, pHeight, direction);

		// decorator
		if (this.deco == null) {
			this.deco = new IceTowerDecorator();
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("hasBase", this.hasBase);
		tagCompound.putInt("treasureFloor", this.treasureFloor);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// should we build a base
		this.hasBase = this.shouldHaveBase(rand);

		// limit sprawl to a reasonable amount
		if (this.getGenDepth() < 5) {

			Rotation dirOffset = RotationUtil.ROTATIONS[rand.nextInt(RotationUtil.ROTATIONS.length)];

			// make sub towers
			for (final Rotation rotation : RotationUtil.ROTATIONS) {

				Rotation dir = dirOffset.getRotated(rotation);

				int[] dest = getValidOpening(rand, dir);

				if (this.getGenDepth() == 4 && (parent instanceof IceTowerMainComponent) && !((IceTowerMainComponent) parent).hasBossWing) {
					boolean hasBoss = makeBossTowerWing(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], 15, 41, dir);
					((IceTowerMainComponent) parent).hasBossWing = hasBoss;
				} else {
					int childHeight = (rand.nextInt(2) + rand.nextInt(2) + 2) * 10 + 1;
					makeTowerWing(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], SIZE, childHeight, dir);
				}
			}
		}

		// figure out where the treasure goes
		int floors = this.height / 10;

		// set treasure to a floor if we need to
		if (this.treasureFloor == -1 && floors > 1) {
			this.treasureFloor = rand.nextInt(floors - 1);
		}

		// add a roof?
		makeARoof(parent, list, rand);

		// beard?
		if (!this.hasBase) {
			makeABeard(parent, list, rand);
		}
	}

	protected boolean shouldHaveBase(RandomSource rand) {
		return this.getGenDepth() == 0 || rand.nextBoolean();
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
		if (list instanceof StructurePiecesBuilder start && !start.pieces.isEmpty() && isOutOfRange(start.pieces.get(0), dx[0], dx[2], RANGE)) {
			return false;
		}

		IceTowerWingComponent wing = new IceTowerWingComponent(TFStructurePieceTypes.TFITWin.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.addPiece(wing);
			if (list instanceof StructurePiecesBuilder start) {
				wing.addChildren(start.pieces.get(0), list, rand);
			}
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Make a new wing
	 */
	public boolean makeBossTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		IceTowerWingComponent wing = new IceTowerBossWingComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.addPiece(wing);
			if (list instanceof StructurePiecesBuilder start) {
				wing.addChildren(start.pieces.get(0), list, rand);
			}
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets a Y value where the stairs meet the specified X coordinate.
	 * Also works for Z coordinates.
	 */
	@Override
	protected int getYByStairs(int rx, RandomSource rand, Rotation direction) {

		int floors = this.height / 10;

		return 11 + (rand.nextInt(floors - 1) * 10);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// make walls
		//fillWithMetadataBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, deco.blockID, deco.blockMeta, AIR, false);
		generateBox(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, deco.randomBlocks);

		// clear inside
		generateAirBox(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		if (this.hasBase) {
			// deco to ground
			for (int x = 0; x < this.size; x++) {
				for (int z = 0; z < this.size; z++) {
					this.fillColumnDown(world, deco.blockState, x, -1, z, sbb);
				}
			}
		}

		// nullify sky light
//		this.nullifySkyLightForBoundingBox(world);

		// make floors
		makeFloorsForTower(world, decoRNG, sbb);

		// openings
		makeOpenings(world, sbb);
	}

	/**
	 * Nullify all the sky light in this component bounding box
	 */

	protected void makeFloorsForTower(WorldGenLevel world, RandomSource decoRNG, BoundingBox sbb) {
		int floorHeight = 10;
		int floors = this.height / floorHeight;
		int topFloor = floors - 1;

		Rotation ladderDir = Rotation.COUNTERCLOCKWISE_90;
		Rotation downLadderDir = ladderDir.getRotated(Rotation.CLOCKWISE_90);

		decorateTopFloor(world, decoRNG, topFloor, topFloor * floorHeight, (topFloor * floorHeight) + floorHeight, ladderDir, downLadderDir, sbb);

		List<Pair<FloorTypesAuroraPalace, Integer>> floorPlan = createFloorPlan(floors - 1, decoRNG);

		for (int i = floors - 2; i >= 0; i--) {
			placeFloor(world, decoRNG, sbb, floorHeight, i);
			Pair<FloorTypesAuroraPalace, Integer> floor = floorPlan.get(floors - 2 - i);
			ladderDir = Rotation.COUNTERCLOCKWISE_90;
			for (int t = 0; t < floor.getSecond(); t++) {
				ladderDir = ladderDir.getRotated(Rotation.CLOCKWISE_90);
			}
			downLadderDir = ladderDir.getRotated(Rotation.CLOCKWISE_90);

			if (this instanceof IceTowerBossWingComponent) {
				decorateFloor(world, decoRNG, i, i * floorHeight, (i + 1) * floorHeight, ladderDir, downLadderDir, sbb);
			} else {
				decorateFloor(world, decoRNG, floor.getFirst(), i, i * floorHeight, (i + 1) * floorHeight, ladderDir, downLadderDir, sbb);
			}
		}
	}

	private List<Pair<FloorTypesAuroraPalace, Integer>> createFloorPlan(int floorCount, RandomSource decoRNG) {
		List<Pair<FloorTypesAuroraPalace, Integer>> plan = new ArrayList<>();

		// we generate top to bottom because top floor is special and has requirements for anchors
		Set<FloorParts> currentlyBlockedParts = new HashSet<>(FloorTypesAuroraPalace.TOP.getFloorWith3x3Map().getBlockedFloorParts());

		while (plan.size() < floorCount) {
			Set<FloorParts> finalCurrentlyBlockedParts = currentlyBlockedParts;
			Map<FloorTypesAuroraPalace, List<Integer>> possibleFloors = Arrays.stream(FloorTypesAuroraPalace.values())
				.collect(Collectors.toMap(

					floorType -> floorType,
					floorType -> getAllowedRotations(floorType, finalCurrentlyBlockedParts)
				));

			possibleFloors.entrySet().removeIf(entry -> entry.getValue().isEmpty());
			possibleFloors.keySet().remove(FloorTypesAuroraPalace.TOP);
			if (possibleFloors.isEmpty()) {
				plan.clear();
				currentlyBlockedParts = new HashSet<>(FloorTypesAuroraPalace.TOP.getFloorWith3x3Map().getBlockedFloorParts());
				continue;
			}

			FloorTypesAuroraPalace chosenType = WorldUtil.getRandomElementWithWeights(
				possibleFloors.keySet().stream()
					.map(floorType -> new Pair<>(floorType, floorType.getWeight()))
					.collect(Collectors.toList()),
				decoRNG);
			int chosenRotation = WorldUtil.getRandomElement(possibleFloors.get(chosenType), decoRNG);
			plan.add(new Pair<>(chosenType, chosenRotation));

			currentlyBlockedParts = chosenType.getFloorWith3x3Map().getBlockedFloorParts()
				.stream()
				.map(part -> part.rotateClockwise(chosenRotation))
				.collect(Collectors.toSet());
			List<FloorParts> doorBlockedParts = getPartsBlockedByDoors(
				(floorCount - plan.size()) * 10,  // 10 is a height of the floor
				(floorCount - plan.size() + 1) * 10,
				Rotation.values()[(chosenRotation + 1) % 4]  // + 1 because default rotation is clockwise_90
			);
			currentlyBlockedParts.addAll(doorBlockedParts);
		}

		return plan;
	}

	private List<Integer> getAllowedRotations(FloorTypesAuroraPalace floorType, Set<FloorParts> currentlyBlockedParts) {
		return IntStream.range(0, 4)
			.filter(rotation -> isRotationAllowed(floorType, rotation, currentlyBlockedParts))
			.boxed()
			.collect(Collectors.toList());
	}

	private boolean isRotationAllowed(FloorTypesAuroraPalace floorType, int rotation, Set<FloorParts> currentlyBlockedParts) {
		Set<FloorParts> requiredParts = floorType.getFloorWith3x3Map().getRequiredFloorParts()
			.stream()
			.map(part -> part.rotateClockwise(rotation))
			.collect(Collectors.toSet());
		return Collections.disjoint(requiredParts, currentlyBlockedParts);
	}


	private HashMap<FloorTypesAuroraPalace, Double> calculateProbabilities(RandomSource decoRNG, int n, int minFloors, int additionFloors) {
		HashMap<FloorTypesAuroraPalace, Integer> floorAmount = new HashMap<>();
		for (int i = 0; i < n; i++) {
			List<Pair<FloorTypesAuroraPalace, Integer>> plan = createFloorPlan(decoRNG.nextInt(additionFloors) + minFloors, decoRNG);
			for (Pair<FloorTypesAuroraPalace, Integer> element : plan) {
				FloorTypesAuroraPalace floorType = element.getFirst();
				floorAmount.put(floorType, floorAmount.getOrDefault(floorType, 0) + 1);
			}
		}
		HashMap<FloorTypesAuroraPalace, Double> probabilities = new HashMap<>();
		for (FloorTypesAuroraPalace floorType : floorAmount.keySet()) {
			probabilities.put(floorType, ((double) floorAmount.get(floorType)) / floorAmount.values().stream().mapToInt(Integer::intValue).sum());
		}
		return probabilities;
	}

	private HashMap<FloorTypesAuroraPalace, HashMap<Integer, Double>> calculateFloorDistributions(RandomSource decoRNG, int n, int fixedFloors) {
		// Initialize a map to store the count of each floor type at each floor index
		HashMap<FloorTypesAuroraPalace, HashMap<Integer, Integer>> floorCounts = new HashMap<>();

		// Initialize floorCounts with empty maps for each FloorType
		for (FloorTypesAuroraPalace floorType : FloorTypesAuroraPalace.values()) {
			floorCounts.put(floorType, new HashMap<>());
		}

		// Simulate tower generation n times
		for (int i = 0; i < n; i++) {
			List<Pair<FloorTypesAuroraPalace, Integer>> plan = createFloorPlan(fixedFloors, decoRNG);
			for (int j = 0; j < plan.size(); j++) {
				Pair<FloorTypesAuroraPalace, Integer> element = plan.get(j);
				FloorTypesAuroraPalace floorType = element.getFirst();
				// Increment the count for this floor type at this floor index
				HashMap<Integer, Integer> floorIndexCount = floorCounts.get(floorType);
				floorIndexCount.put(j, floorIndexCount.getOrDefault(j, 0) + 1);
			}
		}

		// Calculate the probabilities for each floor type at each floor index
		HashMap<FloorTypesAuroraPalace, HashMap<Integer, Double>> probabilities = new HashMap<>();

		// Total number of generations for each floor index
		int totalGenerations = fixedFloors * n;

		for (FloorTypesAuroraPalace floorType : floorCounts.keySet()) {
			HashMap<Integer, Integer> floorIndexCount = floorCounts.get(floorType);
			HashMap<Integer, Double> floorIndexProbabilities = new HashMap<>();

			for (int floorIndex : floorIndexCount.keySet()) {
				floorIndexProbabilities.put(floorIndex, ((double) floorIndexCount.get(floorIndex)) / totalGenerations);
			}

			probabilities.put(floorType, floorIndexProbabilities);
		}

		return probabilities;
	}

	private Direction getChestDirection(Rotation rotation) {
		return rotation.rotate(getOrientation());
	}

	/**
	 * Put down planks or whatevs for a floor
	 */
	protected void placeFloor(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int floorHeight, int floor) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {
				placeBlock(world, deco.floorState, x, (floor * floorHeight) + floorHeight, z, sbb);
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
	@SuppressWarnings("fallthrough")
	protected void decorateFloor(WorldGenLevel world, RandomSource rand, FloorTypesAuroraPalace floorType, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		boolean hasTreasure = (this.treasureFloor == floor);

		switch (floorType) {
			case WRAPAROUND_WALL_STEPS:
				decorateWraparoundWallSteps(world, bottom, top, ladderUpDir, hasTreasure, sbb);
				break;
			case FAR_WALL_STEPS:
				decorateFarWallSteps(world, bottom, top, ladderUpDir, hasTreasure, sbb);
				break;
			case WRAPAROUND_WALL_STEPS_PILLARS:
				decorateWraparoundWallStepsPillars(world, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case PLATFORM:
				decoratePlatform(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case PILLAR_PARKOUR:
				decoratePillarParkour(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case PILLAR_PLATFORMS:
				decoratePillarPlatforms(world, bottom, top, ladderUpDir, hasTreasure, sbb);
				break;
			case PILLAR_PLATFORMS_OUTSIDE:
				decoratePillarPlatformsOutside(world, bottom, top, ladderUpDir, hasTreasure, sbb);
				break;
			case QUAD_PILLAR_STAIRS:
			default:
				decorateQuadPillarStairs(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
		}
	}

	private boolean isNoDoorAreaRotated(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Rotation rotation) {
		boolean isClear = true;
		// make a bounding box of the area
		BoundingBox exclusionBox = switch (rotation) {
			case CLOCKWISE_90 -> new BoundingBox(this.size - 1 - maxZ, minY, minX, this.size - 1 - minZ, maxY, maxX);
			case CLOCKWISE_180 -> new BoundingBox(this.size - 1 - maxX, minY, this.size - 1 - maxZ, this.size - 1 - minX, maxY, this.size - 1 - minZ);
			case COUNTERCLOCKWISE_90 -> new BoundingBox(minZ, minY, this.size - 1 - maxX, maxZ, maxY, this.size - 1 - minX);
			default -> new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		};

		for (BlockPos door : this.openings) {
			if (exclusionBox.isInside(door)) {
				isClear = false;
			}
		}

		return isClear;
	}

	private List<FloorParts> getPartsBlockedByDoors(int bottom, int top, Rotation rotation) {
		List<FloorParts> parts = List.of(
			FloorParts.LEFT_FRONT, FloorParts.FRONT, FloorParts.RIGHT_FRONT, FloorParts.LEFT, FloorParts.RIGHT, FloorParts.LEFT_BACK, FloorParts.BACK, FloorParts.RIGHT_BACK
		);

		List<int[]> coordinates = List.of(
			new int[]{0, 0, 3, 3},
			new int[]{4, 0, 6, 3},
			new int[]{8, 0, 10, 3},
			new int[]{0, 4, 3, 6},
			new int[]{8, 4, 10, 6},
			new int[]{0, 8, 3, 10},
			new int[]{4, 8, 6, 10},
			new int[]{8, 8, 10, 10}
		);

		return IntStream.range(0, parts.size())
			.filter(i -> !isNoDoorAreaRotated(
				coordinates.get(i)[0], bottom, coordinates.get(i)[1],
				coordinates.get(i)[2], top, coordinates.get(i)[3], rotation)
			)
			.mapToObj(parts::get)
			.collect(Collectors.toList());
	}

	protected void decorateTopFloor(WorldGenLevel world, RandomSource rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		if (rand.nextBoolean()) {
			decoratePillarsCorners(world, rand, bottom, top, ladderDownDir, sbb);
		} else {
			decoratePillarsGrid(world, rand, bottom, top, ladderDownDir, sbb);
		}

		// generate treasure last so it doesn't get deleted
		if (this.isDeadEnd()) {
			decorateTopFloorTreasure(world, bottom, ladderUpDir, sbb);
		}
	}

	private void decorateTopFloorTreasure(WorldGenLevel world, int bottom, Rotation rotation, BoundingBox sbb) {
		this.fillBlocksRotated(world, sbb, 5, bottom + 1, 5, 5, bottom + 4, 5, deco.pillarState, rotation);

		this.placeTreasureRotated(world, 5, bottom + 5, 5, getChestDirection(rotation), rotation, TFLootTables.AURORA_ROOM, false, sbb);
	}

	private void decoratePillars(WorldGenLevel world, int bottom, int top, Rotation rotation, BoundingBox sbb) {
		this.fillBlocksRotated(world, sbb, 3, bottom + 1, 3, 3, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 1, 3, 7, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 1, 7, 3, top - 1, 7, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 1, 7, 7, top - 1, 7, deco.pillarState, rotation);
	}

	private void decoratePillarsGrid(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation rotation, BoundingBox sbb) {
		final BlockState pillarEW = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 9, bottom + 5, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 7, 9, bottom + 5, 7, pillarNS, rotation);

		this.decoratePillars(world, bottom, top, rotation, sbb);
	}

	private void decoratePillarsCorners(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation rotation, BoundingBox sbb) {
		final BlockState pillarEW = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 9, bottom + 5, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 7, 9, bottom + 5, 7, pillarNS, rotation);

		this.fillAirRotated(world, sbb, 3, bottom + 5, 3, 7, bottom + 5, 7, rotation);

		this.decoratePillars(world, bottom, top, rotation, sbb);
	}

	private void decorateFarWallSteps(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, boolean hasTreasure, BoundingBox sbb) {

		// far set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 10 - (z / 2);
			this.setBlockStateRotated(world, (z % 2 == 0) ? deco.pillarState : deco.platformState, 9, y, z, ladderUpDir, sbb);
			for (int by = bottom + 1; by < y; by++) {
				this.setBlockStateRotated(world, deco.pillarState, 9, by, z, ladderUpDir, sbb);
			}
		}

		// near set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 1 + (z / 2);
			this.setBlockStateRotated(world, (z % 2 == 0) ? deco.platformState : deco.pillarState, 8, y, z, ladderUpDir, sbb);
			for (int by = bottom + 1; by < y; by++) {
				this.setBlockStateRotated(world, deco.pillarState, 8, by, z, ladderUpDir, sbb);
			}
		}

		// entry stair
		this.setBlockStateRotated(world, deco.platformState, 7, bottom + 1, 1, ladderUpDir, sbb);

		// clear floor above
		for (int z = 2; z < 7; z++) {
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
		}

		final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 1, bottom + 8, 5, getChestDirection(ladderUpDir).getCounterClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
			//int beamMetaNS = ((this.coordBaseMode + ladderUpDir) % 2 == 0) ? 4 : 8;
			this.setBlockStateRotated(world, pillarNS, 1, bottom + 7, 5, ladderUpDir, sbb);
		}
	}

	private void decorateWraparoundWallSteps(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, boolean hasTreasure, BoundingBox sbb) {
		BlockState topPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.TOP);
		BlockState bottomPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

		// far set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 10 - (z / 2);
			this.setBlockStateRotated(world, ((z % 2 == 0) ? topPlatform : bottomPlatform), 9, y, z, ladderUpDir, sbb);
		}

		// right set of stairs
		for (int x = 1; x < 9; x++) {
			int y = bottom + 2 + ((x - 1) / 2);
			this.setBlockStateRotated(world, ((x % 2 == 0) ? topPlatform : bottomPlatform), x, y, 9, ladderUpDir, sbb);
		}

		// entry stairs
		this.setBlockStateRotated(world, topPlatform, 1, bottom + 1, 8, ladderUpDir, sbb);
		this.setBlockStateRotated(world, deco.platformState, 1, bottom + 1, 7, ladderUpDir, sbb);

		// clear floor above
		for (int z = 2; z < 7; z++) {
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 1, bottom + 5, 5, getChestDirection(ladderUpDir).getCounterClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
			//int beamMetaNS = ((this.coordBaseMode + ladderUpDir) % 2 == 0) ? 4 : 8;
			final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
			this.setBlockStateRotated(world, pillarNS, 1, bottom + 4, 5, ladderUpDir, sbb);
		}
	}

	private void decorateWraparoundWallStepsPillars(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, BoundingBox sbb) {
		Rotation rotation = ladderDownDir;
		final BlockState pillarEW = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		this.decorateWraparoundWallSteps(world, bottom, top, ladderUpDir, false, sbb);
		this.decoratePillars(world, bottom, top, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 1, 3, 9, bottom + 1, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 7, 7, 9, bottom + 7, 7, pillarNS, rotation);

		this.fillBlocksRotated(world, sbb, 1, bottom + 2, 3, 2, bottom + 2, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 6, 3, 2, bottom + 6, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 4, 7, 2, bottom + 4, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 8, 7, 2, bottom + 8, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 6, 8, 3, bottom + 6, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 8, 8, 7, bottom + 8, 9, pillarEW, rotation);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 7, bottom + 7, 1, getChestDirection(ladderUpDir).getClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
		}
	}

	private void decoratePlatform(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, BoundingBox sbb) {
		BlockState topPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.TOP);
		BlockState bottomPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

		this.decoratePillars(world, bottom, top, ladderDownDir, sbb);
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 3, 7, bottom + 5, 7, deco.floorState, ladderDownDir);

		// one flight
		for (int z = 6; z < 10; z++) {
			int y = bottom - 2 + (z / 2);
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 1, y, z, ladderDownDir, sbb);
		}
		// two flight
		for (int x = 2; x < 6; x++) {
			int y = bottom + 2 + (x / 2);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 9, ladderDownDir, sbb);
		}
		// connector
		this.setBlockStateRotated(world, deco.platformState, 5, bottom + 5, 8, ladderDownDir, sbb);

		// connector
		this.setBlockStateRotated(world, deco.platformState, 5, bottom + 6, 2, ladderUpDir, sbb);
		// two flight
		for (int x = 5; x < 10; x++) {
			int y = bottom + 4 + (x / 2);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 1, ladderUpDir, sbb);
			if (x > 6) {
				this.setBlockStateRotated(world, AIR, x, top, 1, ladderUpDir, sbb);
			}
		}
		// one flight
		for (int z = 2; z < 5; z++) {
			int y = bottom + 8 + (z / 2);
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 9, y, z, ladderUpDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 5, bottom + 6, 5, getChestDirection(ladderUpDir).getClockWise(), ladderDownDir, TFLootTables.AURORA_CACHE, false, sbb);
		}
	}

	private void decorateQuadPillarStairs(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, BoundingBox sbb) {
		this.decoratePillars(world, bottom, top, ladderDownDir, sbb);

		BlockState topPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.TOP);
		BlockState bottomPlatform = deco.platformState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

		// one flight
		for (int z = 6; z < 9; z++) {
			int y = bottom - 2 + (z / 2);
			this.setBlockStateRotated(world, z % 2 == 1 ? topPlatform : bottomPlatform, 2, y, z, ladderDownDir, sbb);
		}
		// two flight
		for (int x = 3; x < 9; x++) {
			int y = bottom + 1 + (x / 2);
			this.setBlockStateRotated(world, x % 2 == 1 ? topPlatform : bottomPlatform, x, y, 8, ladderDownDir, sbb);
		}
		// three flight
		for (int z = 7; z > 1; z--) {
			int y = top - 2 - ((z - 1) / 2);
			if (z < 4) {
				this.setBlockStateRotated(world, AIR, 8, top, z, ladderDownDir, sbb);
			}
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 8, y, z, ladderDownDir, sbb);
		}
		// last flight
		for (int x = 7; x > 3; x--) {
			int y = top + 1 - ((x - 1) / 2);
			this.setBlockStateRotated(world, AIR, x, top, 2, ladderDownDir, sbb);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 2, ladderDownDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			int treasureX = 3;
			int treasureY = bottom + 7;
			int treasureZ = 5;

			this.placeTreasureRotated(world, treasureX, treasureY, treasureZ, getChestDirection(ladderUpDir).getClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
			Direction.Axis axis = getChestDirection(ladderUpDir).getClockWise().getAxis();
			this.fillBlocksRotated(world, sbb, treasureX, treasureY - 1,  treasureZ - 1, treasureX, treasureY - 1, treasureZ + 1, deco.pillarState.setValue(RotatedPillarBlock.AXIS, axis), ladderUpDir);
		}
	}

	private void decoratePillarPlatforms(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, boolean hasTreasure, BoundingBox sbb) {
		// clear
		this.fillAirRotated(world, sbb, 2, top, 2, 8, top, 4, ladderUpDir);
		this.fillAirRotated(world, sbb, 2, top, 2, 4, top, 6, ladderUpDir);

		// extra pillar tops
		this.setBlockStateRotated(world, deco.platformState, 7, top, 3, ladderUpDir, sbb);
		this.setBlockStateRotated(world, deco.platformState, 3, top, 3, ladderUpDir, sbb);

		// platforms
		for (int y = 1; y < 10; y++) {
			int x = (y + 1) % 4 < 2 ? 3 : 7;
			int z = y % 4 < 2 ? 3 : 7;
			this.fillBlocksRotated(world, sbb, x - 1, bottom + y, z - 1, x + 1, bottom + y, z + 1, deco.floorState, ladderUpDir);
		}

		this.decoratePillars(world, bottom, top, ladderUpDir, sbb);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 3, bottom + 5, 2, getChestDirection(ladderUpDir).getCounterClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
		}
	}

	private void decoratePillarPlatformsOutside(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, boolean hasTreasure, BoundingBox sbb) {
		// platforms
		for (Rotation r : RotationUtil.ROTATIONS) {
			if (r == Rotation.COUNTERCLOCKWISE_90) continue;  // to make space for floor entrance
			Rotation rotation = ladderUpDir.getRotated(r);
			this.fillBlocksRotated(world, sbb, 1, bottom + 1, 1, 3, bottom + 1	, 3, deco.platformState, rotation);
			this.fillBlocksRotated(world, sbb, 4, bottom, 1, 6, bottom, 3, deco.floorState, rotation);
		}

		buildStairway(world, bottom, top, ladderUpDir, sbb);

		this.decoratePillars(world, bottom, top, ladderUpDir, sbb);

		// treasure!
		if (hasTreasure) {
			int treasureX = 3;
			int treasureY = bottom + 5;
			int treasureZ = 2;
			this.placeTreasureRotated(world, treasureX, treasureY, treasureZ, getChestDirection(ladderUpDir).getCounterClockWise(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
			Direction.Axis axis = getChestDirection(ladderUpDir).getClockWise().getAxis();
			this.fillBlocksRotated(world, sbb, treasureX, treasureY - 1,  treasureZ - 1, treasureX, treasureY - 1, treasureZ, deco.pillarState.setValue(RotatedPillarBlock.AXIS, axis), ladderUpDir);
		}
	}

	public void buildStairway(WorldGenLevel world, int bottom, int top, Rotation ladderUpDir, BoundingBox sbb) {
		// Set the rotation direction by rotating 180 degrees clockwise
		Rotation rotation = ladderUpDir.getRotated(Rotation.CLOCKWISE_180);
		fillAirRotated(world, sbb, 8, top, 1, 9, top + 2, 9, rotation);

		// Define the slab types
		BlockState slabTypeTop = deco.platformState.setValue(SlabBlock.TYPE, SlabType.TOP);
		BlockState slabTypeBottom = deco.platformState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

		// Build the stairway using slabs
		for (int step = 0; step < 22; step++) {
			// Determine if the slab is top or bottom based on the step number
			BlockState slab = step % 2 == 0 ? slabTypeTop : slabTypeBottom;

			int firstDirection = Math.min(step, 7); // First direction movement
			int secondDirection = Math.min(step - firstDirection, 8); // Second direction movement
			int thirdDirection = Math.min(step - firstDirection - secondDirection, 8); // Third direction movement

			// Determine width adjustments for the slab placement
			int firstWidthAdjustment = firstDirection < 7 ? 1 : 0;
			int secondWidthAdjustment = secondDirection > 0 && secondDirection < 7 ? 1 : 0;
			int thirdWidthAdjustment = thirdDirection > 0 ? 1 : 0;

			// Calculate height adjustment based on full blocks
			int heightAdjustment = (secondDirection > 0 ? 1 : 0) + (thirdDirection > 0 ? 1 : 0);

			// Place the slab blocks in the world
			fillBlocksRotated(world, sbb,
				9 - secondDirection - firstWidthAdjustment,
				top - step / 2 + heightAdjustment,
				8 - firstDirection + thirdDirection,
				9 - secondDirection + thirdWidthAdjustment,
				top - step / 2 + heightAdjustment,
				8 - firstDirection + thirdDirection + secondWidthAdjustment,
				slab,
				rotation
			);
		}

		// Place full blocks to complete the stairway
		placeFullBlocks(world, bottom, top, sbb, rotation);
	}
	private void placeFullBlocks(WorldGenLevel world, int bottom, int top, BoundingBox sbb, Rotation rotation) {
		fillBlocksRotated(world, sbb, 8, top, 8, 9, top, 9, deco.floorState, rotation);
		fillBlocksRotated(world, sbb, 8, top - 3, 1, 9, top - 3, 2, deco.floorState, rotation);
		fillBlocksRotated(world, sbb, 1, bottom + 4, 1, 2, bottom + 4, 2, deco.floorState, rotation);
		fillBlocksRotated(world, sbb, 1, bottom + 1, 7, 2, bottom + 1, 9, deco.floorState, rotation);
	}


	private void decoratePillarParkour(WorldGenLevel world, RandomSource rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, BoundingBox sbb) {
		Rotation rotation = ladderDownDir;

		final BlockState pillarEW = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		final BlockState pillarNS = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		// 4 pillars
		this.decoratePillars(world, bottom, top, rotation, sbb);

		// center pillar
		this.setBlockStateRotated(world, deco.pillarState, 5, bottom + 1, 5, rotation, sbb);

		// pillar 2
		this.fillBlocksRotated(world, sbb, 5, bottom + 2, 7, 5, bottom + 2, 9, pillarEW, rotation);

		// gap 3
		this.fillBlocksRotated(world, sbb, 1, bottom + 3, 7, 2, bottom + 3, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 3, 8, 3, bottom + 3, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 7, 7, 2, bottom + 7, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 7, 8, 3, bottom + 7, 9, pillarEW, rotation);
		this.fillAirRotated(world, sbb, 3, bottom + 4, 7, 3, bottom + 6, 7, rotation);

		// pillar 4
		this.fillBlocksRotated(world, sbb, 1, bottom + 4, 5, 2, bottom + 4, 5, pillarNS, rotation);

		// gap 5
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 2, bottom + 5, 3, pillarNS, rotation);
		this.fillAirRotated(world, sbb, 3, bottom + 6, 3, 3, bottom + 8, 3, rotation);

		// pillar 6
		this.fillBlocksRotated(world, sbb, 5, bottom + 6, 1, 5, bottom + 6, 2, pillarEW, rotation);

		// gap 7
		this.fillAirRotated(world, sbb, 7, bottom + 8, 3, 7, bottom + 10, 3, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 7, 1, 7, bottom + 7, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 7, 3, 9, bottom + 7, 3, pillarNS, rotation);

		// pillar 8
		this.fillBlocksRotated(world, sbb, 8, bottom + 8, 5, 9, bottom + 8, 5, pillarNS, rotation);

		// gap 9 (no gap?)
		//this.fillAirRotated(world, sbb, 7, bottom + 10, 7, 7, bottom + 10, 7, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 9, 7, 9, bottom + 9, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 9, 8, 7, bottom + 9, 9, pillarEW, rotation);

		// holes in ceiling
		this.fillAirRotated(world, sbb, 6, top, 6, 8, top, 8, ladderUpDir);
		this.fillAirRotated(world, sbb, 2, top, 6, 5, top, 8, ladderUpDir);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 8, bottom + 8, 7, getChestDirection(ladderUpDir).getOpposite(), ladderUpDir, TFLootTables.AURORA_CACHE, false, sbb);
		}
	}

	/**
	 * Attach a roof to this tower.
	 * <p>
	 * This function keeps trying roofs starting with the largest and fanciest, and then keeps trying smaller and plainer ones
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();
		tryToFitRoof(list, rand, new IceTowerRoofComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ()));
	}

	/**
	 * Add a beard to this structure.  There is only one type of beard.
	 */
	@Override
	public void makeABeard(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();
		IceTowerBeardComponent beard;
		beard = new IceTowerBeardComponent(index + 1, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(beard);
		beard.addChildren(this, list, rand);
	}
}
