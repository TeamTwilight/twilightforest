package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFMaze;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class MinotaurMazeComponent extends TFStructureComponentOld {

	final TFMaze maze;
	final int[] rcoords;
	private final int level;

	public MinotaurMazeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMaze.value(), nbt);

		this.level = nbt.getInt("mazeLevel");
		this.rcoords = nbt.getIntArray("roomCoords");

		// recreate maze object
		maze = new TFMaze(getMazeSize(), getMazeSize(), RandomSource.create());
		setFixedMazeSeed();

		// blank out rcoords above 1 so that the room generation works properly
		//TODO: re-do this. :)
		for (int i = 2; i < rcoords.length; i++) {
			this.rcoords[i] = 0;
		}

		// recreate rooms
		this.addRoomsToMaze(this.rcoords[0], this.rcoords[1], (this.rcoords.length + 1) / 2);

		// regenerate maze
		maze.generateRecursiveBacktracker(0, 0);
	}

	public MinotaurMazeComponent(int index, int x, int y, int z, int entranceX, int entranceZ, int level, RandomSource random) {
		super(TFStructurePieceTypes.TFMMaze.value(), index, x, y, z);
		this.setOrientation(Direction.SOUTH);
		this.level = level;
		this.boundingBox = TFLandmark.getComponentToAddBoundingBox(x, y, z, -getRadius(), 0, -getRadius(), getRadius() * 2 + 2, 5, getRadius() * 2 + 2, Direction.SOUTH, false);

		// make maze object
		maze = new TFMaze(getMazeSize(), getMazeSize(), random);

		// set the seed to a fixed value based on this maze's x and z
		setFixedMazeSeed();

		// rooms
		int nrooms = 7;
		rcoords = new int[nrooms * 2];

		addRoomsToMaze(entranceX, entranceZ, nrooms);

		// make actual maze
		maze.generateRecursiveBacktracker(0, 0);
	}

	private void addRoomsToMaze(int entranceX, int entranceZ, int nrooms) {
		// make one entrance room always
		rcoords[0] = entranceX;
		rcoords[1] = entranceZ;
		maze.carveRoom1(entranceX, entranceZ);

		// add room coordinates, trying to keep them separate from existing rooms
		for (int i = 1; i < nrooms; i++) {
			int rx, rz;
			do {
				rx = maze.rand.nextInt(getMazeSize() - 2) + 1;
				rz = maze.rand.nextInt(getMazeSize() - 2) + 1;
			} while (isNearRoom(rx, rz, rcoords, i == 1 ? 7 : 4));

			maze.carveRoom1(rx, rz);

			rcoords[i * 2] = rx;
			rcoords[i * 2 + 1] = rz;
		}
	}

	private void setFixedMazeSeed() {
		maze.setSeed(this.boundingBox.minX() * 90342903L + this.boundingBox.minY() * 90342903L ^ this.boundingBox.minZ());
	}

	public MinotaurMazeComponent(int index, int x, int y, int z, int level, RandomSource random) {
		this(index, x, y, z, 11, 11, level, random);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("mazeLevel", this.level);
		tagCompound.putIntArray("roomCoords", this.rcoords);
	}

	protected MazeRoomComponent makeRoom(RandomSource random, int i, int dx, int dz) {
		MazeRoomComponent room;

		int worldX = boundingBox.minX() + dx * 5 - 4;
		int worldY = boundingBox.minY();
		int worldZ = boundingBox.minZ() + dz * 5 - 4;

		if (i == 0) {
			// default room
			room = new MazeRoomComponent(TFStructurePieceTypes.TFMMR.value(), 3 + i, random, worldX, worldY, worldZ);
		} else if (i == 1) {
			if (this.level == 1) {
				// exit room
				room = new MazeRoomExitComponent(3 + i, random, worldX, worldY, worldZ);
			} else {
				// boss room
				room = new MazeRoomBossComponent(3 + i, random, worldX, worldY, worldZ);
			}
		} else if (i == 2 || i == 3) {
			if (this.level == 1) {
				// collapsed room
				room = new MazeRoomCollapseComponent(3 + i, random, worldX, worldY, worldZ);
			} else {
				// mush-room
				room = new MazeMushRoomComponent(3 + i, random, worldX, worldY, worldZ);
			}
		} else if (i == 4) {
			if (this.level == 1) {
				// fountain room
				room = new MazeRoomFountainComponent(3 + i, random, worldX, worldY, worldZ);
			} else {
				// vault
				room = new MazeRoomVaultComponent(3 + i, random, worldX, worldY, worldZ);

			}
		} else {
			room = new MazeRoomSpawnerChestsComponent(3 + i, random, worldX, worldY, worldZ);
		}

		return room;
	}

	/**
	 * Find dead ends and put something there
	 *
	 */
	protected void decorateDeadEndsCorridors(RandomSource random, StructurePieceAccessor list) {
		for (int x = 0; x < maze.width; x++) {
			for (int z = 0; z < maze.depth; z++) {
				TFStructureComponentOld component = null;

				// dead ends
				if (!maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, Direction.EAST);
				}
				if (maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, Direction.WEST);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && !maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, Direction.SOUTH);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, Direction.NORTH);
				}

				// corridors
				if (!maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)
						&& maze.isWall(x - 1, z, x - 1, z - 1) && maze.isWall(x - 1, z, x - 1, z + 1) && maze.isWall(x + 1, z, x + 1, z - 1) && maze.isWall(x + 1, z, x + 1, z + 1)) {
					component = makeCorridor(random, x, z, Direction.WEST);
				}
				if (!maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1) && maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z)
						&& maze.isWall(x, z - 1, x - 1, z - 1) && maze.isWall(x, z - 1, x + 1, z - 1) && maze.isWall(x, z + 1, x - 1, z + 1) && maze.isWall(x, z + 1, x + 1, z + 1)) {
					component = makeCorridor(random, x, z, Direction.SOUTH);
				}

				if (component != null) {
					list.addPiece(component);
					component.addChildren(this, list, random);
				}
			}
		}
	}

	/**
	 * Add a dead end structure at the specified coords
	 */
	protected MazeDeadEndComponent makeDeadEnd(RandomSource random, int dx, int dz, Direction rotation) {
		int worldX = boundingBox.minX() + dx * 5 + 1;
		int worldY = boundingBox.minY();
		int worldZ = boundingBox.minZ() + dz * 5 + 1;

		int decorationType = random.nextInt(8);
		//tama why
		//decorationType = decorationType >= 3 ? 0 : decorationType;

		return switch (decorationType) {
			case 1 -> new MazeDeadEndChestComponent(TFStructurePieceTypes.TFMMDEC.value(), 4, worldX, worldY, worldZ, rotation);
			case 2 -> random.nextBoolean() ? new MazeDeadEndTripwireChestComponent(4, worldX, worldY, worldZ, rotation) : new MazeDeadEndTrappedChestComponent(4, worldX, worldY, worldZ, rotation);
			case 3 -> new MazeDeadEndTorchesComponent(4, worldX, worldY, worldZ, rotation);
			case 4 -> new MazeDeadEndFountainComponent(TFStructurePieceTypes.TFMMDEF.value(), 4, worldX, worldY, worldZ, rotation);
			case 5 -> new MazeDeadEndFountainLavaComponent(4, worldX, worldY, worldZ, rotation);
			case 6 -> new MazeDeadEndPaintingComponent(4, worldX, worldY, worldZ, rotation);
			case 7 -> this.level == 1 ? new MazeDeadEndRootsComponent(TFStructurePieceTypes.TFMMDER.value(), 4, worldX, worldY, worldZ, rotation) : new MazeDeadEndShroomsComponent(4, worldX, worldY, worldZ, rotation);
			default -> // blank with fence doorway
					new MazeDeadEndComponent(TFStructurePieceTypes.TFMMDE.value(), 4, worldX, worldY, worldZ, rotation);
		};
	}

	protected MazeCorridorComponent makeCorridor(RandomSource random, int dx, int dz, Direction rotation) {
		int worldX = boundingBox.minX() + dx * 5 + 1;
		int worldY = boundingBox.minY();
		int worldZ = boundingBox.minZ() + dz * 5 + 1;

		int decorationType = random.nextInt(5);

		return switch (decorationType) {
			case 1 -> new MazeCorridorComponent(TFStructurePieceTypes.TFMMC.value(), 4, worldX, worldY, worldZ, rotation);
			case 2 -> new MazeCorridorIronFenceComponent(4, worldX, worldY, worldZ, rotation);
			case 3 -> null; // painting
			case 4 -> this.level == 1 ? new MazeCorridorRootsComponent(4, worldX, worldY, worldZ, rotation) : new MazeCorridorShroomsComponent(4, worldX, worldY, worldZ, rotation);
			default -> null;
		};
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(structurecomponent, list, random);

		// add a second story
		if (this.level == 1) {
			int centerX = boundingBox.minX() + ((boundingBox.maxX() - boundingBox.minX()) / 2);
			int centerZ = boundingBox.minZ() + ((boundingBox.maxZ() - boundingBox.minZ()) / 2);

			MinotaurMazeComponent maze = new MinotaurMazeComponent(1, centerX, boundingBox.minY() - 10, centerZ, rcoords[2], rcoords[3], 2, random);
			list.addPiece(maze);
			maze.addChildren(this, list, random);
		}

		// add rooms where we have our coordinates
		for (int i = 0; i < rcoords.length / 2; i++) {
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];

			// add the room as a component
			MazeRoomComponent room = makeRoom(random, i, dx, dz);
			list.addPiece(room);
			room.addChildren(this, list, random);
		}

		// find dead ends and corridors and make components for them
		decorateDeadEndsCorridors(random, list);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
		BlockState stone = Blocks.STONE.defaultBlockState();

		// level 2 maze surrounded by bedrock
		if (level == 2) {
			generateBox(world, sbb, 0, -1, 0, getDiameter() + 2, 6, getDiameter() + 2, bedrock, AIR, false);
		}

		// clear the area
		generateAirBox(world, sbb, 1, 1, 1, getDiameter(), 4, getDiameter());
		boolean onlyReplaceCeiling = this.level == 1;
		generateBox(world, sbb, 1, 5, 1, getDiameter() + 1, 5, getDiameter() + 1, TFBlocks.MAZESTONE.value().defaultBlockState(), stone, onlyReplaceCeiling);
		generateBox(world, sbb, 1, 0, 1, getDiameter() + 1, 0, getDiameter() + 1, TFBlocks.MAZESTONE_MOSAIC.value().defaultBlockState(), stone, false);

		maze.headBlockState = TFBlocks.DECORATIVE_MAZESTONE.value().defaultBlockState();
		maze.wallBlockState = TFBlocks.MAZESTONE_BRICK.value().defaultBlockState();
		maze.rootBlockState = TFBlocks.DECORATIVE_MAZESTONE.value().defaultBlockState();
		maze.pillarBlockState = TFBlocks.CUT_MAZESTONE.value().defaultBlockState();
		maze.wallBlocks = new MazestoneProcessor();
		maze.torchRarity = 0.05F;
		maze.tall = 2;
		maze.head = 1;
		maze.roots = 1;
		maze.oddBias = 4;

		maze.copyToStructure(world, manager, generator, 1, 2, 1, this, sbb);
	}

	public int getMazeSize() {
		return 22;
	}

	public int getRadius() {
		return (int) (getMazeSize() * 2.5);
	}

	public int getDiameter() {
		return getMazeSize() * 5;
	}

	/**
	 * @return true if the specified dx and dz are within 3 of a room specified in rcoords
	 */
	protected boolean isNearRoom(int dx, int dz, int[] rcoords, int range) {
		// if proposed coordinates are covering the origin, return true to stop the room from causing the maze to fail
		if (dx == 1 && dz == 1) {
			return true;
		}

		for (int i = 0; i < rcoords.length / 2; i++) {
			int rx = rcoords[i * 2];
			int rz = rcoords[i * 2 + 1];

			if (rx == 0 && rz == 0) {
				continue;
			}

			if (Math.abs(dx - rx) < range && Math.abs(dz - rz) < range) {
				return true;
			}
		}
		return false;
	}
}
