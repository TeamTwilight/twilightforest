package twilightforest.world.components.structures.darktower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFMaze;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.TFStructureDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DarkTowerMainComponent extends DarkTowerWingComponent {
	private boolean placedKeys = false;

	public DarkTowerMainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFDTMai.get(), nbt);
	}

	public DarkTowerMainComponent(RandomSource rand, int index, int x, int y, int z) {
		this(rand, index, x + 10, y, z + 10, Direction.NORTH);
	}

	public DarkTowerMainComponent(RandomSource rand, int index, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFDTMai.get(), index, x, y, z, 19, 56 + ((rand.nextInt(32) / 5) * 5), rotation);

		// check to make sure we can build the whole tower
		if (this.boundingBox.maxY() > 245) {
			int amtToLower = (((this.boundingBox.maxY() - 245) / 5) * 5) + 5;

			TwilightForestMod.LOGGER.info("Lowering Dark Tower max height by {} to be within world bounds", amtToLower);

			this.height -= amtToLower;
			this.boundingBox = this.boundingBox.moved(0, -amtToLower, 0);
		}

		// decorator
		if (this.deco == null) {
			this.deco = new StructureDecoratorDarkTower();
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// if this is not the first main part, add one
		if (this.getGenDepth() > 0) {
			addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
		}

		// one direction gets a new main tower, unless we're at tower #3
		Rotation mainDir = null;

		if (this.getGenDepth() < 2) {
			mainDir = RotationUtil.ROTATIONS[rand.nextInt(RotationUtil.ROTATIONS.length)];

			// make sub towers
			for (Rotation rotation : RotationUtil.ROTATIONS) {
				if (rotation == mainDir) {
					continue;
				}

				int[] dest = getValidOpening(rand, rotation);

				int childHeight = validateChildHeight(21 + rand.nextInt(10));

				makeTowerWing(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], 11, childHeight, rotation);
			}
		} else {
			// make boss trap towers
			for (Rotation i : RotationUtil.ROTATIONS) {
				int[] dest = getValidOpening(rand, i);
				makeBossTrapWing(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], i);
			}
		}

		if (this.getGenDepth() > 0) {
			// sub towers at base of tower
			for (Rotation i : RotationUtil.ROTATIONS) {
				if (i == Rotation.CLOCKWISE_180) {
					continue;
				}

				int[] dest = getValidOpening(rand, i);

				// move opening to tower base
				dest[1] = 1;

				int childHeight = validateChildHeight(21 + rand.nextInt(10));

				makeTowerWing(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], 11, childHeight, i);
			}

			// add a beard
			makeABeard(parent, list, rand);
		} else {
			// 2 entrance towers towers at base of tower
			for (Rotation rotation : new Rotation[]{Rotation.NONE, Rotation.CLOCKWISE_180}) {

				int[] dest = getValidOpening(rand, rotation);

				// move opening to tower base
				dest[1] = 1;

				int childHeight = validateChildHeight(10 + rand.nextInt(5));

				makeEntranceTower(list, rand, 5, dest[0], dest[1], dest[2], 9, childHeight, rotation);
			}
		}

		// actually make main tower
		if (mainDir != null) {
			int[] dest = getValidOpening(rand, mainDir);
			makeNewLargeTower(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], mainDir);
		}

		// add a roof?
		makeARoof(parent, list, rand);

		// flag certain towers for keys
		if (!this.placedKeys && this.getGenDepth() < 2) {
			// count how many size 9 towers we have hanging off us
			ArrayList<DarkTowerWingComponent> possibleKeyTowers = new ArrayList<DarkTowerWingComponent>();

			if (list instanceof StructurePiecesBuilder start) {
				for (StructurePiece piece : start.pieces) {
					if (piece instanceof DarkTowerWingComponent wing && wing.size == 9 && wing.getGenDepth() == this.getGenDepth()) {
						possibleKeyTowers.add(wing);
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (possibleKeyTowers.size() < 1) {
					TwilightForestMod.LOGGER.warn("Dark forest tower could not find four small towers to place keys in.");
					break;
				}

				int towerNum = rand.nextInt(possibleKeyTowers.size());

				possibleKeyTowers.get(towerNum).setKeyTower(true);
				possibleKeyTowers.remove(towerNum);
			}

			this.placedKeys = true;
		}
	}

	/**
	 * Make a bridge that leads to an entrance tower
	 */
	private boolean makeEntranceTower(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int childSize, int childHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 5, direction);

		DarkTowerBridgeComponent bridge = new DarkTowerEntranceBridgeComponent(index, dx[0], dx[1], dx[2], childSize, childHeight, direction);
		// if I'm doing this right, the main towers can't intersect
		list.addPiece(bridge);
		bridge.addChildren(this, list, rand);
		addOpening(x, y, z, rotation);
		return true;
	}

	/**
	 * Make a bridge that leads to a new large-size tower
	 */
	private boolean makeNewLargeTower(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, Rotation rotation) {

		int wingSize = 15;
		int wingHeight = 56;

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 5, direction);

		DarkTowerMainBridgeComponent bridge = new DarkTowerMainBridgeComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// if I'm doing this right, the main towers can't intersect
		list.addPiece(bridge);
		bridge.addChildren(this, list, rand);
		// lock the door!
		addOpening(x, y, z, rotation, EnumDarkTowerDoor.LOCKED);
		return true;
	}

	/**
	 * Make a bridge that leads to a boss trap tower
	 */
	private boolean makeBossTrapWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, Rotation rotation) {

		int wingSize = 11;
		int wingHeight = 9;

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 5, direction);

		DarkTowerBossBridgeComponent bridge = new DarkTowerBossBridgeComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// if I'm doing this right, the main towers can't intersect
		list.addPiece(bridge);
		bridge.addChildren(this, list, rand);
		// lock the door!
		addOpening(x, y, z, rotation);
		return true;
	}

	/**
	 * Attach a roof to this tower.
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (this.getGenDepth() < 2) {
			super.makeARoof(parent, list, rand);
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// make walls
		makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		// clear inside
		generateAirBox(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		if (this.getGenDepth() == 0) {
			// deco to ground
			for (int x = 0; x < this.size; x++) {
				for (int z = 0; z < this.size; z++) {
					this.placeBlock(world, deco.accentState, x, -1, z, sbb);
				}
			}
		}

		// nullify sky light
//		this.nullifySkyLightForBoundingBox(world);

		// how many total floors do we have?
		int totalFloors = this.height / 5;

		// is the tower a beam maze?  false = builder platforms
		boolean beamMaze = decoRNG.nextBoolean();

		// how many floors should the center occupy?
		int centerFloors = beamMaze ? 4 : totalFloors / 2;

		// how many floors do we get on the top/bottom portion?
		int bottomFloors = (totalFloors - centerFloors) / 2;

		// where do the top floors start?
		int topFloorsStartY = height - (bottomFloors * 5 + 1);

		// add bottom and top floors
		addThreeQuarterFloors(world, manager, generator, decoRNG, sbb, 0, bottomFloors * 5);
		if (this.getGenDepth() < 2) {
			addThreeQuarterFloors(world, manager, generator, decoRNG, sbb, topFloorsStartY, height - 1);
		} else {
			addThreeQuarterFloorsDecorateBoss(world, decoRNG, sbb, topFloorsStartY, height - 1);
			// boss destruction

			destroyTower(world, decoRNG, 12, height + 4, 3, 4, sbb);
			destroyTower(world, decoRNG, 3, height + 4, 12, 4, sbb);
			destroyTower(world, decoRNG, 3, height + 4, 3, 4, sbb);
			destroyTower(world, decoRNG, 12, height + 4, 12, 4, sbb);
			destroyTower(world, decoRNG, 8, height + 4, 8, 5, sbb);

			// make spawner where it will hopefully last
			decorateBossSpawner(world, sbb, Rotation.NONE, height - 6);
		}

		if (beamMaze) {
			addTimberMaze(world, decoRNG, sbb, (bottomFloors * 5), topFloorsStartY);
		} else {
			addBuilderPlatforms(world, decoRNG, sbb, (bottomFloors * 5), topFloorsStartY);
		}

		// openings
		makeOpenings(world, sbb);
	}

	/**
	 * Add a bunch of 3/4 floors
	 */
	protected void addThreeQuarterFloors(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource decoRNG, BoundingBox sbb, int bottom, int top) {

		int spacing = 5;
		Rotation rotation = RotationUtil.ROTATIONS[Math.abs((this.boundingBox.minY() + bottom) % 4)];
		if (bottom == 0) {
			makeLargeStairsUp(world, sbb, rotation, 0);
			rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
			makeBottomEntrance(world, sbb, rotation, bottom);
			bottom += spacing;
		}

		// fill with 3/4 floors
		for (int y = bottom; y < top; y += spacing) {
			boolean isBottomFloor = (y == bottom && bottom != spacing);
			boolean isTopFloor = (y >= top - spacing);
			boolean isTowerTopFloor = (y >= height - spacing - 2);

			makeThreeQuarterFloor(world, sbb, rotation, y, isBottomFloor, isTowerTopFloor);

			if (!isTopFloor) {
				makeLargeStairsUp(world, sbb, rotation, y);
			}

			if (!isTopFloor || isTowerTopFloor) {
				decorateFloor(world, manager, generator, decoRNG, sbb, rotation, y, isBottomFloor, isTopFloor);
			}

			rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
		}
	}

	/**
	 * Add a bunch of 3/4 floors
	 */
	protected void addThreeQuarterFloorsDecorateBoss(WorldGenLevel world, RandomSource decoRNG, BoundingBox sbb, int bottom, int top) {
		int spacing = 5;
		Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY() + bottom) % 4];
		if (bottom == 0) {
			makeLargeStairsUp(world, sbb, rotation, 0);
			rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
			bottom += spacing;
		}

		// fill with 3/4 floors
		for (int y = bottom; y < top; y += spacing) {
			boolean isBottomFloor = (y == bottom && bottom != spacing);
			boolean isTopFloor = (y >= top - spacing);
			boolean isTowerTopFloor = (y >= height - spacing - 2);

			makeThreeQuarterFloor(world, sbb, rotation, y, isBottomFloor, isTowerTopFloor);

			if (!isTopFloor) {
				makeLargeStairsUp(world, sbb, rotation, y);
				decorateExperiment(world, sbb, rotation, y);
			}

			rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
		}
	}

	@SuppressWarnings("fallthrough")
	private void decorateFloor(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTop) {
		// pick an appropriate decoration and use it
		// FIXME: if minY <= 64, some cases gets double weight

		if (isTop) {
			// there are a limited amount that can go at the top
			switch (decoRNG.nextInt(3)) {
				case 1 -> decorateBotanical(world, generator, decoRNG, sbb, rotation, y);
				case 2 -> decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
				default -> decorateAquarium(world, sbb, rotation, y);
			}
		} else if (isBottom) {
			// similarly some don't work on the bottom
			switch (decoRNG.nextInt(4)) {
				default:
				case 0:
					decorateAquarium(world, sbb, rotation, y);
					break;
				case 1:
					decorateBotanical(world, generator, decoRNG, sbb, rotation, y);
					break;
				case 2:
					if (y + this.boundingBox.minY() > 64) {
						decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
						break;
					}
				case 3:
					decorateForge(world, decoRNG, sbb, rotation, y);
					break;
			}
		} else {
			// but in the middle, anything goes
			switch (decoRNG.nextInt(8)) {
				default:
				case 0:
				case 1:
					decorateReappearingMaze(world, manager, generator, decoRNG, sbb, rotation, y);
					break;
				case 2:
					decorateUnbuilderMaze(world, decoRNG, sbb, rotation, y);
					break;
				case 3:
					decorateAquarium(world, sbb, rotation, y);
					break;
				case 4:
					decorateBotanical(world, generator, decoRNG, sbb, rotation, y);
					break;
				case 5:
					if (y + this.boundingBox.minY() > 64) {
						decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
						break;
					}
				case 6:
					decorateLounge(world, generator, decoRNG, sbb, rotation, y);
					break;
				case 7:
					decorateForge(world, decoRNG, sbb, rotation, y);
					break;
			}
		}
	}

	/**
	 * Make a single three quarter floor
	 *
	 */
	protected void makeThreeQuarterFloor(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTowerTopFloor) {
		int half = size / 2;
		// fill the floor
		this.fillBlocksRotated(world, sbb, half + 1, y, 1, size - 2, y, half + 1, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 1, y, half + 1, size - 2, y, size - 2, deco.blockState, rotation);

		// don't make part of the fence if we have stairs coming up
		int startZ = isBottom ? 1 : 3;

		this.fillBlocksRotated(world, sbb, 1, y, half, half, y, half, deco.accentState, rotation);
		this.fillBlocksRotated(world, sbb, half, y, startZ, half, y, half, deco.accentState, rotation);
		this.fillBlocksRotated(world, sbb, 1, y + 1, half, half, y + 1, half, deco.fenceState, rotation);
		this.fillBlocksRotated(world, sbb, half, y + 1, startZ, half, y + 1, half, deco.fenceState, rotation);

		// little notch if we're at the tower top
		if (isTowerTopFloor) {
			this.fillBlocksRotated(world, sbb, 1, y, half - 2, 3, y, half, deco.accentState, rotation);
			this.fillBlocksRotated(world, sbb, 1, y + 1, half - 2, 3, y + 1, half, deco.fenceState, rotation);
			this.fillBlocksRotated(world, sbb, 1, y, half - 1, 2, y, half, deco.fenceState, rotation);
			this.fillBlocksRotated(world, sbb, 1, y + 1, half - 1, 2, y + 1, half, AIR, rotation);
		}
	}

	protected void makeLargeStairsUp(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		// stairs!
		//this.fillAirRotated(world, sbb, x, y, 1, x, y + 1, 2, rotation);

		for (int i = 0; i < 5; i++) {
			int z = size / 2 - i + 4;
			int sy = y + i + 1;

			setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), 1, sy, z, rotation, sbb);
			setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), 2, sy, z, rotation, sbb);
			setBlockStateRotated(world, deco.blockState, 1, sy, z - 1, rotation, sbb);
			setBlockStateRotated(world, deco.blockState, 2, sy, z - 1, rotation, sbb);
			setBlockStateRotated(world, deco.blockState, 3, sy, z - 1, rotation, sbb);

			if (i > 0 && i < 4) {
				setBlockStateRotated(world, deco.accentState, 3, sy, z, rotation, sbb);
				setBlockStateRotated(world, deco.fenceState, 3, sy + 1, z, rotation, sbb);
				setBlockStateRotated(world, deco.fenceState, 3, sy + 2, z, rotation, sbb);
			} else if (i == 0) {
				setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, false), 3, sy, z, rotation, sbb);
			}
		}
	}

	private void decorateReappearingMaze(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y) {
		// make maze object
		int mazeSize = 6;
		TFMaze maze = new TFMaze(mazeSize, mazeSize, decoRNG);

		// set the seed to a fixed value based on this maze's x and z
		maze.setSeed(world.getSeed() + this.boundingBox.minX() * 90342903L + y * 90342903L ^ this.boundingBox.minZ());

		// tell it not to make outside walls by making them "ROOMS"
		for (int i = 0; i < 13; i++) {
			maze.putRaw(i, 0, TFMaze.ROOM);
			maze.putRaw(i, 12, TFMaze.ROOM);
			maze.putRaw(0, i, TFMaze.ROOM);
			maze.putRaw(12, i, TFMaze.ROOM);
		}

		// we need to set this before generation
		maze.doorRarity = 0.3F;

		// set some areas out of bounds and make the maze depending on where we start
		switch (rotation) {
			case NONE -> {
				for (int x = 1; x < 6; x++) {
					for (int z = 1; z < 6; z++) {
						maze.putRaw(x, z, TFMaze.ROOM);
					}
				}
				maze.putRaw(1, 6, TFMaze.ROOM);
				maze.putRaw(1, 7, TFMaze.ROOM);
				maze.putRaw(1, 8, TFMaze.ROOM);
				maze.putRaw(1, 9, TFMaze.ROOM);
				maze.putRaw(1, 10, TFMaze.DOOR);
				maze.putRaw(6, 1, TFMaze.ROOM);
				maze.putRaw(7, 1, TFMaze.ROOM);
				maze.putRaw(8, 1, TFMaze.DOOR);
				maze.generateRecursiveBacktracker(0, 5);
			}
			case CLOCKWISE_90 -> {
				for (int x = 7; x < 12; x++) {
					for (int z = 1; z < 6; z++) {
						maze.putRaw(x, z, TFMaze.ROOM);
					}
				}
				maze.putRaw(6, 1, TFMaze.ROOM);
				maze.putRaw(5, 1, TFMaze.ROOM);
				maze.putRaw(4, 1, TFMaze.ROOM);
				maze.putRaw(3, 1, TFMaze.ROOM);
				maze.putRaw(2, 1, TFMaze.DOOR);
				maze.putRaw(11, 6, TFMaze.ROOM);
				maze.putRaw(11, 7, TFMaze.ROOM);
				maze.putRaw(11, 8, TFMaze.DOOR);
				maze.generateRecursiveBacktracker(0, 0);
			}
			case CLOCKWISE_180 -> {
				for (int x = 7; x < 12; x++) {
					for (int z = 7; z < 12; z++) {
						maze.putRaw(x, z, TFMaze.ROOM);
					}
				}
				maze.putRaw(11, 6, TFMaze.ROOM);
				maze.putRaw(11, 5, TFMaze.ROOM);
				maze.putRaw(11, 4, TFMaze.ROOM);
				maze.putRaw(11, 3, TFMaze.ROOM);
				maze.putRaw(11, 2, TFMaze.DOOR);
				maze.putRaw(6, 11, TFMaze.ROOM);
				maze.putRaw(5, 11, TFMaze.ROOM);
				maze.putRaw(4, 11, TFMaze.DOOR);
				maze.generateRecursiveBacktracker(5, 0);
			}
			case COUNTERCLOCKWISE_90 -> {
				for (int x = 1; x < 6; x++) {
					for (int z = 7; z < 12; z++) {
						maze.putRaw(x, z, TFMaze.ROOM);
					}
				}
				maze.putRaw(6, 11, TFMaze.ROOM);
				maze.putRaw(7, 11, TFMaze.ROOM);
				maze.putRaw(8, 11, TFMaze.ROOM);
				maze.putRaw(9, 11, TFMaze.ROOM);
				maze.putRaw(10, 11, TFMaze.DOOR);
				maze.putRaw(1, 6, TFMaze.ROOM);
				maze.putRaw(1, 5, TFMaze.ROOM);
				maze.putRaw(1, 4, TFMaze.DOOR);
				maze.generateRecursiveBacktracker(5, 5);
			}
		}

		// copy the maze to us!
		maze.wallBlockState = deco.blockState;
		maze.headBlockState = deco.accentState;
		maze.pillarBlockState = deco.accentState;
		maze.doorBlockState = TFBlocks.REAPPEARING_BLOCK.get().defaultBlockState();

		maze.torchRarity = 0;
		maze.tall = 3;
		maze.head = 1;
		maze.oddBias = 2;

		maze.copyToStructure(world, manager, generator, 0, y + 1, 0, this, sbb);

		decorateMazeDeadEnds(world, decoRNG, maze, y, rotation, sbb);
	}

	/**
	 * Find dead ends and put something there
	 */
	protected void decorateMazeDeadEnds(WorldGenLevel world, RandomSource decoRNG, TFMaze maze, int y, Rotation rotation, BoundingBox sbb) {
		for (int x = 0; x < maze.width; x++) {
			for (int z = 0; z < maze.depth; z++) {
				// dead ends
				if (!maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					decorateDeadEnd(world, x, y, z, 3, sbb);
				}
				if (maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					decorateDeadEnd(world, x, y, z, 1, sbb);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && !maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					decorateDeadEnd(world, x, y, z, 0, sbb);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1)) {
					decorateDeadEnd(world, x, y, z, 2, sbb);
				}
			}
		}
	}

	/**
	 * Decorate a specific maze dead end
	 */
	private void decorateDeadEnd(WorldGenLevel world, int mx, int y, int mz, int facing, BoundingBox sbb) {
		int x = mx * 3 + 1;
		int z = mz * 3 + 1;

		switch (facing) {
			case 0 -> {
				placeBlock(world, deco.accentState, x, y + 1, z + 1, sbb);
				placeBlock(world, deco.accentState, x + 1, y + 1, z + 1, sbb);
				this.setDoubleLootChest(world, x, y + 2, z + 1, x + 1, y + 2, z + 1, Direction.SOUTH, TFLootTables.DARKTOWER_CACHE, sbb, false);
			}
			case 1 -> {
				placeBlock(world, deco.accentState, x, y + 1, z, sbb);
				placeBlock(world, deco.accentState, x, y + 1, z + 1, sbb);
				this.setDoubleLootChest(world, x, y + 2, z, x, y + 2, z + 1, Direction.WEST, TFLootTables.DARKTOWER_CACHE, sbb, false);
			}
			case 2 -> {
				placeBlock(world, deco.accentState, x, y + 1, z, sbb);
				placeBlock(world, deco.accentState, x + 1, y + 1, z, sbb);
				this.setDoubleLootChest(world, x + 1, y + 2, z, x, y + 2, z, Direction.NORTH, TFLootTables.DARKTOWER_CACHE, sbb, false);
			}
			case 3 -> {
				placeBlock(world, deco.accentState, x + 1, y + 1, z, sbb);
				placeBlock(world, deco.accentState, x + 1, y + 1, z + 1, sbb);
				this.setDoubleLootChest(world, x + 1, y + 2, z + 1, x + 1, y + 2, z, Direction.EAST, TFLootTables.DARKTOWER_CACHE, sbb, false);
			}
		}
	}

	private void decorateUnbuilderMaze(WorldGenLevel world, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y) {

		// fill in posts
		for (int x = size / 2; x < size - 1; x++) {
			for (int z = 3; z < size - 1; z++) {
				if (x % 2 == 1 && z % 2 == 1) {
					for (int py = 1; py < 5; py++) {
						setBlockStateRotated(world, deco.pillarState, x, y + py, z, rotation, sbb);
					}
				} else if (x % 2 == 1 || z % 2 == 1) {
					for (int py = 1; py < 5; py++) {
						setBlockStateRotated(world, deco.fenceState, x, y + py, z, rotation, sbb);
					}

					if (x != (size / 2) + 2 && x != size - 1 && z != size - 1) {
						int ay = decoRNG.nextInt(4) + 1;
						setBlockStateRotated(world, AIR, x, y + ay, z, rotation, sbb);

						if (x > size - 7) {
							ay = decoRNG.nextInt(3) + 1;
							setBlockStateRotated(world, AIR, x, y + ay, z, rotation, sbb);
						}
					}
				}
			}
		}

		final BlockState antiBuilderBlockState = TFBlocks.ANTIBUILDER.get().defaultBlockState();

		// place unbuilders
		setBlockStateRotated(world, antiBuilderBlockState, 15, y + 2, 7, rotation, sbb);
		setBlockStateRotated(world, antiBuilderBlockState, 11, y + 3, 7, rotation, sbb);
		setBlockStateRotated(world, antiBuilderBlockState, 15, y + 2, 13, rotation, sbb);
		setBlockStateRotated(world, antiBuilderBlockState, 11, y + 3, 13, rotation, sbb);
		setBlockStateRotated(world, antiBuilderBlockState, 5, y + 3, 13, rotation, sbb);
	}

	private void decorateLounge(WorldGenLevel world, ChunkGenerator generator, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y) {
		//  brewing area in corner - walls
		this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, deco.pillarState, rotation);

		// floors
		this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, Direction.WEST, false), rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, Direction.SOUTH, false), rotation);

		// furnaces
		this.makeDispenserPillar(world, deco, 13, y, 1, Direction.SOUTH, rotation, sbb);
		this.makeDispenserPillar(world, deco, 15, y, 1, Direction.SOUTH, rotation, sbb);
		this.makeDispenserPillar(world, deco, 17, y, 3, Direction.WEST, rotation, sbb);
		this.makeDispenserPillar(world, deco, 17, y, 5, Direction.WEST, rotation, sbb);

		// framing pillars
		this.makeStonePillar(world, deco, 12, y, 1, rotation, sbb);
		this.makeStonePillar(world, deco, 17, y, 6, rotation, sbb);

		// cauldron and brewing stand
		this.setBlockStateRotated(world, Blocks.BREWING_STAND.defaultBlockState(), 13, y + 2, 5, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), 15, y + 2, 3, rotation, sbb);

		// bookshelves in corner
		this.fillBlocksRotated(world, sbb, 10, y + 1, 17, 17, y + 4, 17, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 17, y + 1, 10, 17, y + 4, 17, deco.blockState, rotation);

		this.fillBlocksRotated(world, sbb, 11, y + 1, 17, 12, y + 4, 17, Blocks.BOOKSHELF.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, 14, y + 1, 17, 15, y + 4, 17, Blocks.BOOKSHELF.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, 17, y + 1, 11, 17, y + 4, 12, Blocks.BOOKSHELF.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, 17, y + 1, 14, 17, y + 4, 15, Blocks.BOOKSHELF.defaultBlockState(), rotation);

		// table
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, true), 13, y + 1, 14, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, true), 14, y + 1, 14, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, true), 14, y + 1, 13, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, true), 13, y + 1, 13, rotation, sbb);

		// chair 1
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, false), 11, y + 1, 13, rotation, sbb);

		// chair 2
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), 13, y + 1, 11, rotation, sbb);

		// center lamp
		setBlockStateRotated(world, Blocks.REDSTONE_LAMP.defaultBlockState(), 8, y + 3, 8, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.CEILING, decoRNG.nextBoolean() ? Direction.EAST : Direction.NORTH, false), 8, y + 2, 8, rotation, sbb);

		// planter for trees
		placeTreePlanter(world, generator, decoRNG.nextInt(5), 6, y + 1, 12, rotation, sbb);
	}

	private void makeDispenserPillar(WorldGenLevel world, TFStructureDecorator forgeDeco, int x, int y, int z, Direction stairMeta, Rotation rotation, BoundingBox sbb) {
		this.setBlockStateRotated(world, getStairState(forgeDeco.stairState, stairMeta, true), x, y + 2, z, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, stairMeta.getOpposite()), x, y + 3, z, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(forgeDeco.stairState, stairMeta, false), x, y + 4, z, rotation, sbb);
	}

	private void decorateBossSpawner(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		this.setBlockStateRotated(world, TFBlocks.UR_GHAST_BOSS_SPAWNER.get().defaultBlockState(), 9, y + 4, 9, rotation, sbb);
	}

	private void decorateExperiment(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		final BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
		final BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
		final BlockState redstone = Blocks.REDSTONE_BLOCK.defaultBlockState();
		final BlockState inactiveReactor = TFBlocks.CARMINITE_REACTOR.get().defaultBlockState();

		//  crafting area in corner - walls
		this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, deco.pillarState, rotation);

		// floors
		this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, Direction.WEST, false), rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, Direction.SOUTH, false), rotation);

		this.makeWoodPillar(world, 13, y, 1, rotation, sbb);
		this.makeWoodPillar(world, 15, y, 1, rotation, sbb);
		this.makeWoodPillar(world, 17, y, 3, rotation, sbb);
		this.makeWoodPillar(world, 17, y, 5, rotation, sbb);

		// framing pillars
		this.makeStonePillar(world, deco, 12, y, 1, rotation, sbb);
		this.makeStonePillar(world, deco, 17, y, 6, rotation, sbb);

		// workbench
		setBlockStateRotated(world, Blocks.CRAFTING_TABLE.defaultBlockState(), 14, y + 2, 4, rotation, sbb);

		// recipes in frames?
		placeItemFrameRotated(world, 13, y + 2, 1, rotation, Direction.SOUTH, new ItemStack(TFItems.BORER_ESSENCE.get()), sbb);
		placeItemFrameRotated(world, 14, y + 2, 1, rotation, Direction.SOUTH, new ItemStack(Items.REDSTONE), sbb);
		placeItemFrameRotated(world, 15, y + 2, 1, rotation, Direction.SOUTH, new ItemStack(TFItems.BORER_ESSENCE.get()), sbb);
		placeItemFrameRotated(world, 13, y + 3, 1, rotation, Direction.SOUTH, new ItemStack(Items.REDSTONE), sbb);
		placeItemFrameRotated(world, 14, y + 3, 1, rotation, Direction.SOUTH, new ItemStack(Items.GHAST_TEAR), sbb);
		placeItemFrameRotated(world, 15, y + 3, 1, rotation, Direction.SOUTH, new ItemStack(Items.REDSTONE), sbb);
		placeItemFrameRotated(world, 13, y + 4, 1, rotation, Direction.SOUTH, new ItemStack(TFItems.BORER_ESSENCE.get()), sbb);
		placeItemFrameRotated(world, 14, y + 4, 1, rotation, Direction.SOUTH, new ItemStack(Items.REDSTONE), sbb);
		placeItemFrameRotated(world, 15, y + 4, 1, rotation, Direction.SOUTH, new ItemStack(TFItems.BORER_ESSENCE.get()), sbb);

		placeItemFrameRotated(world, 17, y + 2, 3, rotation, Direction.WEST, new ItemStack(TFBlocks.ENCASED_TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 2, 4, rotation, Direction.WEST, new ItemStack(TFBlocks.TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 2, 5, rotation, Direction.WEST, new ItemStack(TFBlocks.ENCASED_TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 3, 3, rotation, Direction.WEST, new ItemStack(TFBlocks.TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 3, 4, rotation, Direction.WEST, new ItemStack(TFItems.CARMINITE.get()), sbb);
		placeItemFrameRotated(world, 17, y + 3, 5, rotation, Direction.WEST, new ItemStack(TFBlocks.TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 4, 3, rotation, Direction.WEST, new ItemStack(TFBlocks.ENCASED_TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 4, 4, rotation, Direction.WEST, new ItemStack(TFBlocks.TOWERWOOD.get()), sbb);
		placeItemFrameRotated(world, 17, y + 4, 5, rotation, Direction.WEST, new ItemStack(TFBlocks.ENCASED_TOWERWOOD.get()), sbb);

		if (y < this.height - 13) {
			// device bottom
			setBlockStateRotated(world, obsidian, 13, y + 1, 13, rotation, sbb);
			setBlockStateRotated(world, obsidian, 15, y + 1, 13, rotation, sbb);
			setBlockStateRotated(world, obsidian, 13, y + 1, 15, rotation, sbb);
			setBlockStateRotated(world, obsidian, 15, y + 1, 15, rotation, sbb);
			setBlockStateRotated(world, netherrack, 13, y + 1, 14, rotation, sbb);
			setBlockStateRotated(world, netherrack, 14, y + 1, 13, rotation, sbb);
			setBlockStateRotated(world, netherrack, 15, y + 1, 14, rotation, sbb);
			setBlockStateRotated(world, netherrack, 14, y + 1, 15, rotation, sbb);
			setBlockStateRotated(world, redstone, 14, y + 1, 14, rotation, sbb);

			// middle
			setBlockStateRotated(world, netherrack, 13, y + 2, 13, rotation, sbb);
			setBlockStateRotated(world, netherrack, 15, y + 2, 13, rotation, sbb);
			setBlockStateRotated(world, netherrack, 13, y + 2, 15, rotation, sbb);
			setBlockStateRotated(world, netherrack, 15, y + 2, 15, rotation, sbb);
			setBlockStateRotated(world, inactiveReactor, 14, y + 2, 14, rotation, sbb);

			//device top
			setBlockStateRotated(world, obsidian, 13, y + 3, 13, rotation, sbb);
			setBlockStateRotated(world, obsidian, 15, y + 3, 13, rotation, sbb);
			setBlockStateRotated(world, obsidian, 13, y + 3, 15, rotation, sbb);
			setBlockStateRotated(world, obsidian, 15, y + 3, 15, rotation, sbb);
			setBlockStateRotated(world, netherrack, 13, y + 3, 14, rotation, sbb);
			setBlockStateRotated(world, netherrack, 14, y + 3, 13, rotation, sbb);
			setBlockStateRotated(world, netherrack, 15, y + 3, 14, rotation, sbb);
			setBlockStateRotated(world, netherrack, 14, y + 3, 15, rotation, sbb);
			setBlockStateRotated(world, redstone, 14, y + 3, 14, rotation, sbb);
		}

		// short piston plunger 1
		setBlockStateRotated(world, deco.accentState, 14, y + 1, 17, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.EAST, false), 13, y + 1, 17, rotation, sbb);
		setBlockStateRotated(world, Blocks.PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH), 14, y + 2, 17, rotation, sbb);
		setBlockStateRotated(world, redstone, 14, y + 2, 16, rotation, sbb);

		// short piston plunger 2
		setBlockStateRotated(world, deco.accentState, 17, y + 1, 14, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.SOUTH, false), 17, y + 1, 13, rotation, sbb);
		setBlockStateRotated(world, Blocks.PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST), 17, y + 2, 14, rotation, sbb);
		setBlockStateRotated(world, redstone, 16, y + 2, 14, rotation, sbb);

		// long piston plunger 1
		setBlockStateRotated(world, redstone, 14, y + 2, 11, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 14, y + 1, 11, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.EAST, true), 13, y + 1, 11, rotation, sbb);
		setBlockStateRotated(world, Blocks.PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH), 14, y + 2, 10, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 14, y + 1, 9, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.EAST, false), 13, y + 1, 9, rotation, sbb);
		setBlockStateRotated(world, Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH), 14, y + 2, 9, rotation, sbb);

		// long piston plunger 2
		setBlockStateRotated(world, redstone, 11, y + 2, 14, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 11, y + 1, 14, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.SOUTH, true), 11, y + 1, 13, rotation, sbb);
		setBlockStateRotated(world, Blocks.PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST), 10, y + 2, 14, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 9, y + 1, 14, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.WALL, Direction.SOUTH, false), 9, y + 1, 13, rotation, sbb);
		setBlockStateRotated(world, Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST), 9, y + 2, 14, rotation, sbb);
	}

	private void makeWoodPillar(WorldGenLevel world, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		final BlockState log = TFBlocks.DARK_LOG.get().defaultBlockState();
		this.setBlockStateRotated(world, log, x, y + 2, z, rotation, sbb);
		this.setBlockStateRotated(world, log, x, y + 3, z, rotation, sbb);
		this.setBlockStateRotated(world, log, x, y + 4, z, rotation, sbb);
	}

	private void placeItemFrameRotated(WorldGenLevel world, int x, int y, int z, Rotation rotation, Direction direction, ItemStack itemStack, BoundingBox sbb) {

		int dx = getXWithOffsetRotated(x, z, rotation);
		int dy = getWorldY(y);
		int dz = getZWithOffsetRotated(x, z, rotation);
		Direction facing = this.rotation.getRotated(rotation).rotate(direction).getOpposite();
		final BlockPos pos = new BlockPos(dx, dy, dz).relative(facing);
		if (sbb.isInside(pos)) {
			ItemFrame frame = new ItemFrame(world.getLevel(), pos, facing);
			if (!itemStack.isEmpty()) {
				frame.setItem(itemStack, false);
			}
			// check if the frame is on a valid surface or not?  The wall may not have been generated yet, on a chunk boundry
			world.addFreshEntity(frame);
		}
	}

	private void decorateAquarium(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		// main aquarium
		makePillarFrame(world, sbb, this.deco, rotation, 12, y, 3, 4, 4, 13, false);
		this.fillBlocksRotated(world, sbb, 13, y + 4, 4, 14, y + 4, 14, Blocks.WATER.defaultBlockState(), rotation);

		// little one
		makePillarFrame(world, sbb, this.deco, rotation, 6, y, 12, 4, 4, 4, false);
		this.fillBlocksRotated(world, sbb, 6, y + 5, 12, 9, y + 5, 15, deco.accentState, rotation);
		this.fillBlocksRotated(world, sbb, 7, y + 4, 13, 8, y + 5, 14, Blocks.WATER.defaultBlockState(), rotation);
	}

	private void decorateForge(WorldGenLevel world, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y) {
		TFStructureDecorator forgeDeco = this.deco;

		// stone walls in corner
		this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, forgeDeco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, forgeDeco.pillarState, rotation);

		this.fillBlocksRotated(world, sbb, 12, y + 1, 17, 17, y + 4, 17, forgeDeco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 17, y + 1, 12, 17, y + 4, 17, forgeDeco.pillarState, rotation);

		// floors
		this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, forgeDeco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, Direction.WEST, false), rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, Direction.SOUTH, false), rotation);

		this.fillBlocksRotated(world, sbb, 13, y + 1, 13, 16, y + 1, 16, forgeDeco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 12, 12, y + 1, 16, getStairState(deco.stairState, Direction.WEST, false), rotation);
		this.fillBlocksRotated(world, sbb, 12, y + 1, 12, 16, y + 1, 12, getStairState(deco.stairState, Direction.NORTH, false), rotation);

		// furnaces
		this.makeFurnacePillar(world, decoRNG, 13, y, 1, Direction.SOUTH, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 15, y, 1, Direction.SOUTH, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 17, y, 3, Direction.WEST, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 17, y, 5, Direction.WEST, rotation, sbb);

		this.makeFurnacePillar(world, decoRNG, 13, y, 17, Direction.NORTH, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 15, y, 17, Direction.NORTH, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 17, y, 13, Direction.WEST, rotation, sbb);
		this.makeFurnacePillar(world, decoRNG, 17, y, 15, Direction.WEST, rotation, sbb);

		// framing pillars
		//this.makeStonePillar(world, forgeDeco, 12, y, 1, Direction.SOUTH, rotation, sbb);
		this.makeStonePillar(world, forgeDeco, 17, y, 6, rotation, sbb);

		this.makeStonePillar(world, forgeDeco, 12, y, 17, rotation, sbb);
		this.makeStonePillar(world, forgeDeco, 17, y, 12, rotation, sbb);

		// extra pillars
		this.makeStonePillar(world, forgeDeco, 17, y, 9, rotation, sbb);
		this.makeStonePillar(world, forgeDeco, 9, y, 17, rotation, sbb);

		// anvils
		this.setBlockStateRotated(world, List.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL).get(decoRNG.nextInt(3)).defaultBlockState()
				.setValue(AnvilBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(decoRNG)), 13, y + 2, 5, rotation, sbb);

		this.setBlockStateRotated(world, List.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL).get(decoRNG.nextInt(3)).defaultBlockState()
				.setValue(AnvilBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(decoRNG)), 13, y + 2, 13, rotation, sbb);

		// fire pit
		makeFirePit(world, forgeDeco, 6, y + 1, 12, rotation, sbb);
	}

	private void makeFurnacePillar(WorldGenLevel world, RandomSource rand, int x, int y, int z, Direction direction, Rotation rotation, BoundingBox sbb) {

		this.setBlockStateRotated(world, getStairState(deco.stairState, direction, true), x, y + 2, z, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.FURNACE.defaultBlockState().setValue(AbstractFurnaceBlock.FACING, direction.getOpposite()), x, y + 3, z, rotation, sbb);

		// randomly put some charcoal in the furnace burn slot
		int amount = rand.nextBoolean() ? rand.nextInt(5) + 4 : 0;
		if (amount > 0) {
			int dx = getXWithOffsetRotated(x, z, rotation);
			int dy = getWorldY(y + 3);
			int dz = getZWithOffsetRotated(x, z, rotation);

			BlockPos pos = new BlockPos(dx, dy, dz);

			if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() == Blocks.FURNACE) {
				// put charcoal in the oven
				Container inv = (Container) world.getBlockEntity(pos);

				inv.setItem(1, new ItemStack(Items.CHARCOAL, amount));
			}
		}

		this.setBlockStateRotated(world, getStairState(deco.stairState, direction, false), x, y + 4, z, rotation, sbb);
	}

	private void makeStonePillar(WorldGenLevel world, TFStructureDecorator forgeDeco, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		for (int py = 1; py <= 4; py++) {
			this.setBlockStateRotated(world, forgeDeco.pillarState, x, y + py, z, rotation, sbb);
		}
		// we used to have caps here, but the code was too complex to be worthwhile
	}

	private void makeFirePit(WorldGenLevel world, TFStructureDecorator myDeco, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), x - 1, y, z, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, false), x + 1, y, z, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), x, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, false), x, y, z - 1, rotation, sbb);
		setBlockStateRotated(world, myDeco.pillarState, x + 1, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, myDeco.pillarState, x + 1, y, z - 1, rotation, sbb);
		setBlockStateRotated(world, myDeco.pillarState, x - 1, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, myDeco.pillarState, x - 1, y, z - 1, rotation, sbb);

		setBlockStateRotated(world, Blocks.NETHERRACK.defaultBlockState(), x, y, z, rotation, sbb);
		setBlockStateRotated(world, Blocks.FIRE.defaultBlockState(), x, y + 1, z, rotation, sbb);
	}

	private void decorateNetherwart(WorldGenLevel world, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y, boolean isTop) {
		TFStructureDecorator netherDeco = this.deco;

//		// lava container
//		makePillarFrame(world, sbb, netherDeco, rotation, 12, y, 3, 4, 4, 4, true);
//		this.fillBlocksRotated(world, sbb, 13, y + 1, 4, 14, y + 1, 5, Blocks.LAVAMOVING, 0, rotation);
//		this.fillBlocksRotated(world, sbb, 12, y + 3, 3, 15, y + 4, 6, 0, 0, rotation);

		//makeNetherburst(world, decoRNG, 8, 100, 20, 12, y + 3, 3, rotation, sbb);

		// wart container
		makePillarFrame(world, sbb, netherDeco, rotation, 12, y, 9, 4, 4, 7, true);
		this.fillBlocksRotated(world, sbb, 13, y + 1, 10, 14, y + 1, 14, Blocks.SOUL_SAND.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, 13, y + 2, 10, 14, y + 2, 14, Blocks.NETHER_WART.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, 13, y + 4, 10, 14, y + 4, 14, Blocks.SOUL_SAND.defaultBlockState(), rotation);

		// blaze container
		makePillarFrame(world, sbb, netherDeco, rotation, 5, y, 12, 3, (isTop ? 4 : 9), 3, true);
		this.setBlockStateRotated(world, netherDeco.blockState, 6, y + 1, 13, rotation, sbb);
		this.setBlockStateRotated(world, netherDeco.blockState, 6, y + (isTop ? 4 : 9), 13, rotation, sbb);

		this.setSpawnerRotated(world, 6, y + 3, 13, rotation, EntityType.BLAZE, sbb);

		// destruction blob
		destroyTower(world, decoRNG, 12, y, 3, 2, sbb);
	}

	private void decorateBotanical(WorldGenLevel world, ChunkGenerator generator, RandomSource decoRNG, BoundingBox sbb, Rotation rotation, int y) {
		// main part
		makePillarFrame(world, sbb, this.deco, rotation, 12, y, 12, 4, 4, 4, true);
		this.fillBlocksRotated(world, sbb, 13, y + 1, 13, 14, y + 1, 14, deco.blockState, rotation);
		this.fillBlocksRotated(world, sbb, 13, y + 4, 13, 14, y + 4, 14, deco.blockState, rotation);

		placeRandomPlant(world, decoRNG, 13, y + 2, 13, rotation, sbb);
		placeRandomPlant(world, decoRNG, 13, y + 2, 14, rotation, sbb);
		placeRandomPlant(world, decoRNG, 14, y + 2, 13, rotation, sbb);
		placeRandomPlant(world, decoRNG, 14, y + 2, 14, rotation, sbb);

		// toolbench
		for (int py = 1; py <= 4; py++) {
			setBlockStateRotated(world, deco.pillarState, 12, y + py, 4, rotation, sbb);
			setBlockStateRotated(world, deco.pillarState, 15, y + py, 4, rotation, sbb);
		}
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, true), 13, y + 1, 4, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, true), 14, y + 1, 4, rotation, sbb);

		//setBlockStateRotated(world, Blocks.CHEST, 0, 13, y + 2, 4, rotation, sbb);
		placeTreasureRotated(world, 13, y + 2, 4, getOrientation(), rotation, TFLootTables.DARKTOWER_CACHE, sbb);
		setBlockStateRotated(world, Blocks.CRAFTING_TABLE.defaultBlockState(), 14, y + 2, 4, rotation, sbb);

		BlockState slab = Blocks.SPRUCE_SLAB.defaultBlockState()
				.setValue(SlabBlock.TYPE, SlabType.TOP);

		// bench 2
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, true), 12, y + 1, 7, rotation, sbb);
		setBlockStateRotated(world, slab, 13, y + 1, 7, rotation, sbb);
		setBlockStateRotated(world, slab, 14, y + 1, 7, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, true), 15, y + 1, 7, rotation, sbb);

		// bench 2
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, true), 12, y + 1, 10, rotation, sbb);
		setBlockStateRotated(world, slab, 13, y + 1, 10, rotation, sbb);
		setBlockStateRotated(world, slab, 14, y + 1, 10, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, true), 15, y + 1, 10, rotation, sbb);

		// plants on benches
		for (int x = 12; x <= 15; x++) {
			placeRandomPlant(world, decoRNG, x, y + 2, 7, rotation, sbb);
			placeRandomPlant(world, decoRNG, x, y + 2, 10, rotation, sbb);
		}

		// planter for trees
		placeTreePlanter(world, generator, decoRNG.nextInt(5), 6, y + 1, 12, rotation, sbb);
	}

	private void placeTreePlanter(WorldGenLevel world, ChunkGenerator generator, int treeNum, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		setBlockStateRotated(world, deco.pillarState, x + 1, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.pillarState, x + 1, y, z - 1, rotation, sbb);
		setBlockStateRotated(world, deco.pillarState, x - 1, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.pillarState, x - 1, y, z - 1, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), x - 1, y, z, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.EAST, false), x + 1, y, z, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, false), x, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, false), x, y, z - 1, rotation, sbb);

		setBlockStateRotated(world, Blocks.DIRT.defaultBlockState(), x, y, z, rotation, sbb);

		int dx = getXWithOffsetRotated(x, z, rotation);
		int dy = getWorldY(y + 1);
		int dz = getZWithOffsetRotated(x, z, rotation);
		if (sbb.isInside(new BlockPos(dx, dy, dz))) {
			ResourceKey<ConfiguredFeature<?, ?>> treeGen = switch (treeNum) {
				case 1 ->
						// jungle tree
						// made a custom one so it doesnt cut through the floor
						TFConfiguredFeatures.SMALLER_JUNGLE_TREE;
				case 2 ->
						// birch
						TreeFeatures.BIRCH;
				case 3 -> TFConfiguredFeatures.TWILIGHT_OAK_TREE;
				case 4 -> TFConfiguredFeatures.RAINBOW_OAK_TREE;
				default ->
						// oak tree
						TreeFeatures.OAK;
			};
			// grow a tree

			for (int i = 0; i < 100; i++) {
				if (world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(treeGen).place(world, generator, world.getRandom(), new BlockPos(dx, dy, dz))) {
					break;
				}
			}
		}
	}

	private void placeRandomPlant(WorldGenLevel world, RandomSource decoRNG, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		Optional<Block> optional = BuiltInRegistries.BLOCK
				.getTag(BlockTagGenerator.DARK_TOWER_ALLOWED_POTS)
				.flatMap(tag -> tag.getRandomElement(decoRNG))
				.map(Holder::value);
		setBlockStateRotated(world, decoRNG.nextInt(10) != 0 && optional.isPresent() ? optional.get().defaultBlockState() : Blocks.FLOWER_POT.defaultBlockState(), x, y, z, rotation, sbb);
	}

	private void makeBottomEntrance(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int y) {
		makeFirePit(world, this.deco, 13, y + 1, 3, rotation, sbb);
		makeFirePit(world, this.deco, 3, y + 1, 13, rotation, sbb);
		makeFirePit(world, this.deco, 13, y + 1, 13, rotation, sbb);

		makePillarFrame(world, sbb, this.deco, rotation, 7, y, 7, 3, 4, 3, false);
	}

	/**
	 * Add a bunch of timber beams
	 */
	protected void addTimberMaze(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int bottom, int top) {

		int spacing = 5;
		Rotation floorside = Rotation.NONE;
		if (bottom == 0) {
			bottom += spacing;
		}

		// fill with beam maze
		for (int y = bottom; y < top; y += spacing) {
			floorside = floorside.getRotated(Rotation.CLOCKWISE_90);
			makeTimberBeams(world, rand, sbb, floorside, y, y == bottom && bottom != spacing, y >= (top - spacing), top);
		}
	}

	/**
	 * Make a lattice of log blocks
	 *
	 */
	protected void makeTimberBeams(WorldGenLevel world, RandomSource rand, BoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTop, int top) {
		BlockState beamID = TFBlocks.TWILIGHT_OAK_LOG.get().defaultBlockState();
		BlockState beamStateNS = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		BlockState beamStateUD = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		BlockState beamStateEW = beamID.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		// three beams going n/s
		for (int z = 1; z < size - 1; z++) {
			setBlockStateRotated(world, beamStateNS, 4, y, z, rotation, sbb);
			setBlockStateRotated(world, beamStateNS, 9, y, z, rotation, sbb);
			setBlockStateRotated(world, beamStateNS, 14, y, z, rotation, sbb);
		}

		// a few random cross beams
		int z = pickBetweenExcluding(3, size - 3, rand, 4, 9, 14);
		for (int x = 5; x < 9; x++) {
			setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
		}

		z = pickBetweenExcluding(3, size - 3, rand, 4, 9, 14);
		for (int x = 10; x < 14; x++) {
			setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
		}

		// beams going down
		int x1 = 4;
		int z1 = pickFrom(rand, 4, 9, 14);
		int x2 = 9;
		int z2 = pickFrom(rand, 4, 9, 14);
		int x3 = 14;
		int z3 = pickFrom(rand, 4, 9, 14);

		for (int by = 1; by < 5; by++) {
			if (!isBottom || checkPost(world, x1, y - 5, z1, rotation, sbb)) {
				setBlockStateRotated(world, beamStateUD, x1, y - by, z1, rotation, sbb);
				setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), x1 + 1, y - by, z1, rotation, sbb);
			}
			if (!isBottom || checkPost(world, x2, y - 5, z2, rotation, sbb)) {
				setBlockStateRotated(world, beamStateUD, x2, y - by, z2, rotation, sbb);
			}
			if (!isBottom || checkPost(world, x3, y - 5, z3, rotation, sbb)) {
				setBlockStateRotated(world, beamStateUD, x3, y - by, z3, rotation, sbb);
				setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST), x3 - 1, y - by, z3, rotation, sbb);
			}
		}

		// do we need a beam going up?
		if (isTop) {
			Rotation topFloorRotation = RotationUtil.ROTATIONS[(this.boundingBox.minY() + top + 1) % 4];

			int ladderX = 4;
			int ladderZ = 10;
			for (int by = 1; by < 5; by++) {
				setBlockStateRotated(world, beamStateUD, ladderX, y + by, 9, topFloorRotation, sbb);
				setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH), ladderX, y + by, ladderZ, topFloorRotation, sbb);
			}

			// fence thing
			setBlockStateRotated(world, AIR, ladderX, y + 6, 9, topFloorRotation, sbb);
			setBlockStateRotated(world, deco.fenceState, ladderX + 1, y + 5, ladderZ, topFloorRotation, sbb);
			setBlockStateRotated(world, deco.fenceState, ladderX - 1, y + 5, ladderZ, topFloorRotation, sbb);
			setBlockStateRotated(world, deco.fenceState, ladderX + 1, y + 6, ladderZ, topFloorRotation, sbb);
			setBlockStateRotated(world, deco.fenceState, ladderX - 1, y + 6, ladderZ, topFloorRotation, sbb);
		}

		if (!isBottom && !isTop) {
			// spawners
			int sx = pickFrom(rand, 6, 7, 11);
			int sz = pickFrom(rand, 6, 11, 12);

			makeMiniGhastSpawner(world, y, sx, sz, sbb);
		}

		// lamps
		int lx = pickFrom(rand, 2, 12, 16);
		int lz = 2 + rand.nextInt(15);

		setBlockStateRotated(world, Blocks.REDSTONE_LAMP.defaultBlockState(), lx, y + 2, lz, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.CEILING, rand.nextBoolean() ? Direction.EAST : Direction.NORTH, false), lx, y + 1, lz, rotation, sbb);
	}

	/**
	 * Make a mini ghast spawner and then set the spawn range and max entities for that spawner
	 */
	private void makeMiniGhastSpawner(WorldGenLevel world, int y, int sx, int sz, BoundingBox sbb) {
		setSpawner(world, sx, y + 2, sz, sbb, TFEntities.CARMINITE_GHASTLING.get(), spawner -> {
			var base = spawner.getSpawner();

			base.spawnRange = 16;
			base.maxNearbyEntities = 2;
			base.spawnCount = 1;
		});
	}

	/**
	 * Add a bunch of platforms accessible with the block builder
	 */
	protected void addBuilderPlatforms(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int bottom, int top) {

		int spacing = 5;
		Rotation floorside = Rotation.NONE;
		if (bottom == 0) {
			bottom += spacing;
		}

		// fill platforms, aside from bottom and top platform
		for (int y = bottom; y < (top - spacing); y += spacing) {
			makeBuilderPlatforms(world, rand, sbb, floorside, y);
			floorside = floorside.getRotated(Rotation.CLOCKWISE_90);
			floorside = floorside.getRotated(RotationUtil.ROTATIONS[rand.nextInt(3)]);
		}

		// add the bottom platforms
		makeBuilderPlatform(world, rand, Rotation.CLOCKWISE_90, bottom, 5, true, sbb);
		makeBuilderPlatform(world, rand, Rotation.COUNTERCLOCKWISE_90, bottom, 5, true, sbb);

		for (int y = bottom - 4; y < bottom; y++) {
			setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y, 5, Rotation.CLOCKWISE_90, sbb);
			setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y, 5, Rotation.COUNTERCLOCKWISE_90, sbb);
		}

		// top platform
		addTopBuilderPlatform(world, rand, top, spacing, sbb);
	}

	/**
	 * Make platform with a block builder
	 */
	protected void makeBuilderPlatforms(WorldGenLevel world, RandomSource rand, BoundingBox sbb, Rotation rotation, int y) {
		int z = size / 2 + rand.nextInt(5) - rand.nextInt(5);

		// bottom platform
		makeBuilderPlatform(world, rand, rotation, y, z, false, sbb);

		// ladder
		setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y + 1, z, rotation, sbb);
		setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y + 2, z, rotation, sbb);
		setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y + 3, z, rotation, sbb);
		setBlockStateRotated(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 1, y + 4, z, rotation, sbb);

		makeBuilderPlatform(world, rand, rotation, y + 5, z, true, sbb);

		if (y % 2 == 1) {
			// reverter blocks
			int sx = pickFrom(rand, 5, 9, 13);
			int sz = (sx == 9) ? (rand.nextBoolean() ? 5 : 13) : 9;

			final BlockState antibuilder = TFBlocks.ANTIBUILDER.get().defaultBlockState();
			setBlockStateRotated(world, antibuilder, sx, y + 2, sz, rotation, sbb);
		} else {
			// lamp cluster
			int sx = rand.nextBoolean() ? 5 : 13;
			int sz = rand.nextBoolean() ? 5 : 13;

			makeLampCluster(world, rand, sx, y, sz, rotation, sbb);
		}
	}

	/**
	 * Add the top floating platform in the builder platforms area
	 */
	private void addTopBuilderPlatform(WorldGenLevel world, RandomSource rand, int top, int spacing, BoundingBox sbb) {
		Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY() + top + 1) % 4];

		// platform
		this.fillBlocksRotated(world, sbb, 5, top - spacing, 9, 7, top - spacing, 11, deco.accentState, rotation);
		// ladder ascender
		this.fillBlocksRotated(world, sbb, 6, top - spacing, 9, 6, top, 9, deco.accentState, rotation);
		this.fillBlocksRotated(world, sbb, 6, top - spacing + 1, 10, 6, top - 1, 10, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH), rotation);
		setBlockStateRotated(world, AIR, 6, top + 1, 9, rotation, sbb);
		setBlockStateRotated(world, deco.fenceState, 5, top, 10, rotation, sbb);
		setBlockStateRotated(world, deco.fenceState, 7, top, 10, rotation, sbb);
		setBlockStateRotated(world, deco.fenceState, 5, top + 1, 10, rotation, sbb);
		setBlockStateRotated(world, deco.fenceState, 7, top + 1, 10, rotation, sbb);
		// builder & lever
		final BlockState inactiveBuilder = TFBlocks.CARMINITE_BUILDER.get().defaultBlockState();
		setBlockStateRotated(world, inactiveBuilder, 7, top - spacing, 10, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.FLOOR, rand.nextBoolean() ? Direction.EAST : Direction.NORTH, false), 7, top - spacing + 1, 11, rotation, sbb);
	}

	private void makeBuilderPlatform(WorldGenLevel world, RandomSource rand, Rotation rotation, int y, int z, boolean hole, BoundingBox sbb) {
		// top platform
		setBlockStateRotated(world, deco.accentState, 1, y, z - 1, rotation, sbb);
		if (!hole) {
			setBlockStateRotated(world, deco.accentState, 1, y, z, rotation, sbb);
		}
		setBlockStateRotated(world, deco.accentState, 1, y, z + 1, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 2, y, z - 1, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 2, y, z, rotation, sbb);
		setBlockStateRotated(world, deco.accentState, 2, y, z + 1, rotation, sbb);

		// builder & lever
		final BlockState inactiveBuilder = TFBlocks.CARMINITE_BUILDER.get().defaultBlockState();
		setBlockStateRotated(world, inactiveBuilder, 2, y, hole ? z + 1 : z - 1, rotation, sbb);
		setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), AttachFace.FLOOR, rand.nextBoolean() ? Direction.EAST : Direction.NORTH, false), 2, y + 1, z, rotation, sbb);
	}

	/**
	 * Make a cluster of redstone lamps w/ switches as decoration for the empty areas in towers
	 */
	private void makeLampCluster(WorldGenLevel world, RandomSource rand, int sx, int y, int sz, Rotation rotation, BoundingBox sbb) {
		int radius = 4;

		// make lamps
		for (int i = 0; i < 5; i++) {
			int lx = sx;
			int ly = y;
			int lz = sz;

			for (int move = 0; move < 10; move++) {
				// place lamp
				setBlockStateRotated(world, Blocks.REDSTONE_LAMP.defaultBlockState(), lx, ly, lz, rotation, sbb);

				// move randomly
				int direction = rand.nextInt(8);

				if (direction > 5) {
					direction -= 2;
				}
				Direction facing = Direction.values()[direction];

				lx += facing.getStepX();
				ly += facing.getStepY();
				lz += facing.getStepZ();

				// if we are out of bounds, stop iterating
				if (lx > sx + radius || lx < sx - radius || ly > y + radius || ly < y - radius || lz > sz + radius || lz < sz - radius) {
					break;
				}
			}
		}

		// make switches
		for (int i = 0; i < 5; i++) {
			int lx = sx;
			int ly = y;
			int lz = sz;

			// we need to always call rand.nextInt the same amount of times
			Direction[] directions = new Direction[10];
			for (int move = 0; move < 10; move++) {
				int direction = rand.nextInt(8);

				if (direction > 5) {
					direction -= 2;
				}
				directions[move] = Direction.values()[direction];
			}

			for (int move = 0; move < 10; move++) {
				// move randomly
				Direction direction = directions[move];

				lx += direction.getStepX();
				ly += direction.getStepY();
				lz += direction.getStepZ();

				// if we are out of bounds, stop iterating
				if (lx > sx + radius || lx < sx - radius || ly > y + radius || ly < y - radius || lz > sz + radius || lz < sz - radius) {
					break;
				}

				// if there is no lamp, place a switch and quit
				if (getBlockStateFromPosRotated(world, lx, ly, lz, sbb, rotation).getBlock() != Blocks.REDSTONE_LAMP) {
					AttachFace face;
					Direction orientation;
					switch (direction) {
						case NORTH -> {
							face = AttachFace.WALL;
							orientation = Direction.SOUTH;
						}
						case SOUTH -> {
							face = AttachFace.WALL;
							orientation = Direction.NORTH;
						}
						case EAST -> {
							face = AttachFace.WALL;
							orientation = Direction.WEST;
						}
						case WEST -> {
							face = AttachFace.WALL;
							orientation = Direction.EAST;
						}
						case UP -> {
							face = AttachFace.FLOOR;
							orientation = Direction.EAST;
						}
						default -> {
							face = AttachFace.CEILING;
							orientation = Direction.NORTH;
						}
					}

					setBlockStateRotated(world, getLeverState(Blocks.LEVER.defaultBlockState(), face, orientation, false), lx, ly, lz, rotation, sbb);
					break;
				}
			}
		}
	}
}
