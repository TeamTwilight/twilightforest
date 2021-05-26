package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.TFMaze;

import java.util.List;
import java.util.Random;

public class ComponentTFMinotaurMaze extends StructureTFComponentOld {

	TFMaze maze;
	int rcoords[];
	private int level;

	public ComponentTFMinotaurMaze(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMaze, nbt);

		this.level = nbt.getInt("mazeLevel");
		this.rcoords = nbt.getIntArray("roomCoords");

		// recreate maze object
		maze = new TFMaze(getMazeSize(), getMazeSize());
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

	public ComponentTFMinotaurMaze(TFFeature feature, int index, int x, int y, int z, int entranceX, int entranceZ, int level) {
		super(TFMinotaurMazePieces.TFMMaze, feature, index);
		this.setCoordBaseMode(Direction.SOUTH);
		this.level = level;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -getRadius(), 0, -getRadius(), getRadius() * 2, 5, getRadius() * 2, Direction.SOUTH);

		// make maze object
		maze = new TFMaze(getMazeSize(), getMazeSize());

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
		maze.setSeed(this.boundingBox.minX * 90342903 + this.boundingBox.minY * 90342903 ^ this.boundingBox.minZ);
	}

	public ComponentTFMinotaurMaze(TFFeature feature, int index, int x, int y, int z, int level) {
		this(feature, index, x, y, z, 11, 11, level);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("mazeLevel", this.level);
		tagCompound.putIntArray("roomCoords", this.rcoords);
	}

	protected ComponentTFMazeRoom makeRoom(Random random, int i, int dx, int dz) {
		ComponentTFMazeRoom room;

		int worldX = boundingBox.minX + dx * 5 - 4;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 - 4;

		if (i == 0) {
			// default room
			room = new ComponentTFMazeRoom(TFMinotaurMazePieces.TFMMR, getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
		} else if (i == 1) {
			if (this.level == 1) {
				// exit room
				room = new ComponentTFMazeRoomExit(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
			} else {
				// boss room
				room = new ComponentTFMazeRoomBoss(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
			}
		} else if (i == 2 || i == 3) {
			if (this.level == 1) {
				// collapsed room
				room = new ComponentTFMazeRoomCollapse(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
			} else {
				// mush-room
				room = new ComponentTFMazeMushRoom(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
			}
		} else if (i == 4) {
			if (this.level == 1) {
				// fountain room
				room = new ComponentTFMazeRoomFountain(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
			} else {
				// vault
				room = new ComponentTFMazeRoomVault(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);

			}
		} else {
			room = new ComponentTFMazeRoomSpawnerChests(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
		}

		return room;
	}

	/**
	 * Find dead ends and put something there
	 *
	 * @param random
	 * @param list
	 */
	protected void decorateDeadEndsCorridors(Random random, List<StructurePiece> list) {
		for (int x = 0; x < maze.width; x++) {
			for (int z = 0; z < maze.depth; z++) {
				StructureTFComponentOld component = null;

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
					list.add(component);
					component.buildComponent(this, list, random);
				}
			}
		}
	}

	/**
	 * Add a dead end structure at the specified coords
	 */
	protected ComponentTFMazeDeadEnd makeDeadEnd(Random random, int dx, int dz, Direction rotation) {
		int worldX = boundingBox.minX + dx * 5 + 1;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 + 1;

		int decorationType = random.nextInt(8);
		//tama why
		//decorationType = decorationType >= 3 ? 0 : decorationType;

		switch (decorationType) {
			default:
			case 0:
				// blank with fence doorway
				return new ComponentTFMazeDeadEnd(TFMinotaurMazePieces.TFMMDE, getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 1:
				return new ComponentTFMazeDeadEndChest(TFMinotaurMazePieces.TFMMDEC, getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 2:
				return random.nextBoolean() ? new ComponentTFMazeDeadEndTripwireChest(getFeatureType(), 4, worldX, worldY, worldZ, rotation) : new ComponentTFMazeDeadEndTrappedChest(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 3:
				return new ComponentTFMazeDeadEndTorches(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 4:
				return new ComponentTFMazeDeadEndFountain(TFMinotaurMazePieces.TFMMDEF, getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 5:
				return new ComponentTFMazeDeadEndFountainLava(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 6:
				return new ComponentTFMazeDeadEndPainting(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 7:
				return this.level == 1 ? new ComponentTFMazeDeadEndRoots(TFMinotaurMazePieces.TFMMDER, getFeatureType(), 4, worldX, worldY, worldZ, rotation) : new ComponentTFMazeDeadEndShrooms(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
		}
	}

	protected ComponentTFMazeCorridor makeCorridor(Random random, int dx, int dz, Direction rotation) {
		int worldX = boundingBox.minX + dx * 5 + 1;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 + 1;

		int decorationType = random.nextInt(5);

		switch (decorationType) {
			default:
			case 0:
				return null;
			case 1:
				return new ComponentTFMazeCorridor(TFMinotaurMazePieces.TFMMC, getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 2:
				return new ComponentTFMazeCorridorIronFence(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 3:
				return null; // painting
			case 4:
				return this.level == 1 ? new ComponentTFMazeCorridorRoots(getFeatureType(), 4, worldX, worldY, worldZ, rotation) : new ComponentTFMazeCorridorShrooms(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
		}
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		super.buildComponent(structurecomponent, list, random);

		// add a second story
		if (this.level == 1) {
			int centerX = boundingBox.minX + ((boundingBox.maxX - boundingBox.minX) / 2);
			int centerZ = boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2);

			ComponentTFMinotaurMaze maze = new ComponentTFMinotaurMaze(getFeatureType(), 1, centerX, boundingBox.minY - 10, centerZ, rcoords[2], rcoords[3], 2);
			list.add(maze);
			maze.buildComponent(this, list, random);
		}

		// add rooms where we have our coordinates
		for (int i = 0; i < rcoords.length / 2; i++) {
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];

			// add the room as a component
			ComponentTFMazeRoom room = makeRoom(random, i, dx, dz);
			list.add(room);
			room.buildComponent(this, list, random);
		}

		// find dead ends and corridors and make components for them
		decorateDeadEndsCorridors(random, list);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		BlockState bedrock = Blocks.BEDROCK.getDefaultState();
		BlockState stone = Blocks.STONE.getDefaultState();

		// level 2 maze surrounded by bedrock
		if (level == 2) {
			fillWithBlocks(world, sbb, 0, -1, 0, getDiameter() + 2, 6, getDiameter() + 2, bedrock, AIR, false);
		}

		// clear the area
		fillWithAir(world, sbb, 1, 1, 1, getDiameter(), 4, getDiameter());
		boolean onlyReplaceCeiling = this.level == 1 && !TFConfig.COMMON_CONFIG.DIMENSION.skylightForest.get();
		fillWithBlocks(world, sbb, 1, 5, 1, getDiameter(), 5, getDiameter(), TFBlocks.maze_stone.get().getDefaultState(), stone, onlyReplaceCeiling);
		fillWithBlocks(world, sbb, 1, 0, 1, getDiameter(), 0, getDiameter(), TFBlocks.maze_stone_mosaic.get().getDefaultState(), stone, false);

		//
		maze.headBlockState = TFBlocks.maze_stone_decorative.get().getDefaultState();
		maze.wallBlockState = TFBlocks.maze_stone_brick.get().getDefaultState();
		maze.rootBlockState = TFBlocks.maze_stone_decorative.get().getDefaultState();
		maze.pillarBlockState = TFBlocks.maze_stone_chiseled.get().getDefaultState();
		maze.wallBlocks = new StructureTFMazeStones();
		maze.torchRarity = 0.05F;
		maze.tall = 2;
		maze.head = 1;
		maze.roots = 1;
		maze.oddBias = 4;

		maze.copyToStructure(world, manager, generator, 1, 2, 1, this, sbb);

		return true;
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
