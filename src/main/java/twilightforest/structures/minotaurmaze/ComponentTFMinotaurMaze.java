package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.TFMaze;

import java.util.List;
import java.util.Random;


public class ComponentTFMinotaurMaze extends StructureTFComponentOld {

	TFMaze maze;
	int rcoords[];
	private int level;

	public ComponentTFMinotaurMaze() {
		super();
	}

	public ComponentTFMinotaurMaze(TFFeature feature, int index, int x, int y, int z, int entranceX, int entranceZ, int level) {
		super(feature, index);
		this.setCoordBaseMode(EnumFacing.SOUTH);
		this.level = level;
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -getRadius(), 0, -getRadius(), getRadius() * 2, 5, getRadius() * 2, EnumFacing.SOUTH);

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

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("mazeLevel", this.level);
		tagCompound.setIntArray("roomCoords", this.rcoords);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.level = tagCompound.getInteger("mazeLevel");
		this.rcoords = tagCompound.getIntArray("roomCoords");

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

	protected ComponentTFMazeRoom makeRoom(Random random, int i, int dx, int dz) {
		ComponentTFMazeRoom room = null;

		int worldX = boundingBox.minX + dx * 5 - 4;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 - 4;

		if (i == 0) {
			// default room
			room = new ComponentTFMazeRoom(getFeatureType(), 3 + i, random, worldX, worldY, worldZ);
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
	protected void decorateDeadEndsCorridors(Random random, List<StructureComponent> list) {
		for (int x = 0; x < maze.width; x++) {
			for (int z = 0; z < maze.depth; z++) {
				StructureTFComponentOld component = null;

				// dead ends
				if (!maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, EnumFacing.EAST);
				}
				if (maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, EnumFacing.WEST);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && !maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, EnumFacing.SOUTH);
				}
				if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1)) {
					component = makeDeadEnd(random, x, z, EnumFacing.NORTH);
				}

				// corridors
				if (!maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1)
						&& maze.isWall(x - 1, z, x - 1, z - 1) && maze.isWall(x - 1, z, x - 1, z + 1) && maze.isWall(x + 1, z, x + 1, z - 1) && maze.isWall(x + 1, z, x + 1, z + 1)) {
					component = makeCorridor(random, x, z, EnumFacing.WEST);
				}
				if (!maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1) && maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z)
						&& maze.isWall(x, z - 1, x - 1, z - 1) && maze.isWall(x, z - 1, x + 1, z - 1) && maze.isWall(x, z + 1, x - 1, z + 1) && maze.isWall(x, z + 1, x + 1, z + 1)) {
					component = makeCorridor(random, x, z, EnumFacing.SOUTH);
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
	protected ComponentTFMazeDeadEnd makeDeadEnd(Random random, int dx, int dz, EnumFacing rotation) {
		int worldX = boundingBox.minX + dx * 5 + 1;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 + 1;

		int decorationType = random.nextInt(8);

		switch (decorationType) {
			default:
			case 0:
				// blank with fence doorway
				return new ComponentTFMazeDeadEnd(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 1:
				return new ComponentTFMazeDeadEndChest(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 2:
				return new ComponentTFMazeDeadEndTrappedChest(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 3:
				return new ComponentTFMazeDeadEndTorches(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 4:
				return new ComponentTFMazeDeadEndFountain(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 5:
				return new ComponentTFMazeDeadEndFountainLava(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 6:
				return new ComponentTFMazeDeadEndPainting(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
			case 7:
				return this.level == 1 ? new ComponentTFMazeDeadEndRoots(getFeatureType(), 4, worldX, worldY, worldZ, rotation) : new ComponentTFMazeDeadEndShrooms(getFeatureType(), 4, worldX, worldY, worldZ, rotation);

		}

	}

	protected ComponentTFMazeCorridor makeCorridor(Random random, int dx, int dz, EnumFacing rotation) {
		int worldX = boundingBox.minX + dx * 5 + 1;
		int worldY = boundingBox.minY;
		int worldZ = boundingBox.minZ + dz * 5 + 1;

		int decorationType = random.nextInt(5);

		switch (decorationType) {
			default:
			case 0:
				return null;
			case 1:
				return new ComponentTFMazeCorridor(getFeatureType(), 4, worldX, worldY, worldZ, rotation);
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
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
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
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// level 2 maze surrounded by bedrock
		if (level == 2) {
			fillWithBlocks(world, sbb, 0, -1, 0, getDiameter() + 2, 6, getDiameter() + 2, Blocks.BEDROCK.getDefaultState(), AIR, false);
		}

		// clear the area
		fillWithAir(world, sbb, 1, 1, 1, getDiameter(), 4, getDiameter());
//		fillWithBlocks(world, sbb, 0, 0, 0, getDiameter(), 0, getDiameter(), TFBlocks.mazestone, Blocks.STONE, false);
//		fillWithBlocks(world, sbb, 0, 5, 0, getDiameter(), 5, getDiameter(), TFBlocks.mazestone, Blocks.STONE, true);
		fillWithBlocks(world, sbb, 1, 5, 1, getDiameter(), 5, getDiameter(), TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.PLAIN), Blocks.STONE.getDefaultState(), this.level == 1);
		fillWithBlocks(world, sbb, 1, 0, 1, getDiameter(), 0, getDiameter(), TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.MOSAIC), Blocks.STONE.getDefaultState(), false);

		//
		maze.headBlockState = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE);
		maze.wallBlockState = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK);
		maze.rootBlockState = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE);
		maze.pillarBlockState = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CHISELED);
		maze.wallVar0State = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CRACKED);
		maze.wallVarRarity = 0.2F;
		maze.torchRarity = 0.05F;
		maze.tall = 2;
		maze.head = 1;
		maze.roots = 1;
		maze.oddBias = 4;

		maze.copyToStructure(world, 1, 2, 1, this, sbb);

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
