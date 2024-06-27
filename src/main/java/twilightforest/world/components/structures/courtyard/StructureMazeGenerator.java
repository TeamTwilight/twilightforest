package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.enums.Diagonals;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponent;

public abstract class StructureMazeGenerator extends TFStructureComponent {
	protected final int[][] maze;
	private final int[][] cornerClipping = new int[4][2];
	private final int widthInCellCount;
	private final int heightInCellCount;
	private final StructureTemplateManager structureManager;
	protected BoundingBox sizeConstraints;

	public StructureMazeGenerator(StructureTemplateManager structureManager, StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);

		this.widthInCellCount = nbt.getInt("mazeWidth");
		this.heightInCellCount = nbt.getInt("mazeHeight");

		maze = new int[this.widthInCellCount - 1][this.heightInCellCount - 1];

		ListTag mazeX = nbt.getList("maze", 9);

		for (int x = 0; x < widthInCellCount - 1; x++) {
			Tag mazeY = mazeX.get(x);

			if (mazeY instanceof ListTag)
				for (int y = 0; y < heightInCellCount - 1; y++) maze[x][y] = ((ListTag) mazeY).getInt(y);
		}

		this.sizeConstraints = BoundingBoxUtils.NBTToBoundingBox(nbt.getCompound("constraints"));
		this.structureManager = structureManager;
	}

	StructureMazeGenerator(StructurePieceType type, RandomSource rand, int i, int widthInCellCount, int heightInCellCount, int x, int y, int z, StructureTemplateManager structureManager) {
		super(type, i, x, y, z);
		this.widthInCellCount = widthInCellCount;
		this.heightInCellCount = heightInCellCount;
		this.structureManager = structureManager;
		this.maze = new int[widthInCellCount - 1][heightInCellCount - 1];
		generateMaze(this.maze, this.cornerClipping, rand, this.widthInCellCount, this.heightInCellCount, 2);
		this.sizeConstraints = this.getBoundingBox();
	}

	// Actually assemble maze
	@Override
	public void addChildren(StructurePiece structureComponent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(structureComponent, list, random);
		final int offset = 6;

		final Rotation[] rotations = Rotation.values();

		this.processInnerWallsAndFloor(structureComponent, list, random, offset, rotations);

		this.processOuterWalls(structureComponent, list, random, offset, rotations);
	}

	@SuppressWarnings("SameParameterValue")
	private static void generateMaze(int[][] maze, int[][] cornerClippings, RandomSource random, int widthInCellCount, int heightInCellCount, int maximumClipping) {
		// Trying to keep this optimized for speed I guess

		// Generates a connection map for the walls. It modifies the two-dimensional int array, inserting packed binary strings as ints.
		// A One in its binary interpretation means the wall connects in this direction.
		// As a result of this being a "connectome" of maze walls. It is "Size In Cell count" - 1.

		final WallFacing[][] rotations = new WallFacing[maze.length][maze[0].length];

		for (int x = 0; x < widthInCellCount - 1; x++) {
			for (int y = 0; y < heightInCellCount - 1; y++) {
				rotations[x][y] = WallFacing.values()[random.nextInt(WallFacing.values().length)];
				// set the initial base byte
				maze[x][y] |= rotations[x][y].BYTE;
			}
		}

		final int[][] mazeLocal = maze.clone();

		int halfWayPointX = (widthInCellCount / 2) - 1;
		int halfWayPointY = (heightInCellCount / 2) - 1;

		for (int y = 0; y < heightInCellCount - 1; y++) {
			for (int x = 0; x < widthInCellCount - 1; x++) {
				if (x == halfWayPointX && y == halfWayPointY) continue;

				// Did we pick west and will we not get an AIOOBException accessing array
				if (rotations[x][y] == WallFacing.WEST && x > 0) {
					// If neighbor does not connect to west, connect it to east
					if (!rotations[x][y].unpackAndTest(maze[x - 1][y]))
						maze[x - 1][y] |= rotations[x][y].OPPOSITE;
					else { // else we cut the connection
						// remove connection for the maze part we're looking at
						maze[x][y] &= rotations[x][y].INVERTED;
						// remove connection for the adjacent maze part
						maze[x - 1][y] &= rotations[x - 1][y].INVERTED_OPPOSITE;
					}
				}
				if (rotations[x][y] == WallFacing.NORTH && y > 0) {
					if (!rotations[x][y].unpackAndTest(maze[x][y - 1]))
						maze[x][y - 1] |= rotations[x][y].OPPOSITE;
					else {
						maze[x][y] &= rotations[x][y].INVERTED;
						maze[x][y - 1] &= rotations[x][y - 1].INVERTED_OPPOSITE;
					}
				}
				if (rotations[x][y] == WallFacing.EAST && x < widthInCellCount - 2) {
					if (!rotations[x][y].unpackAndTest(maze[x + 1][y]))
						maze[x + 1][y] |= rotations[x][y].OPPOSITE;
					else {
						maze[x][y] &= rotations[x][y].INVERTED;
						maze[x + 1][y] &= rotations[x + 1][y].INVERTED_OPPOSITE;
					}
				}
				if (rotations[x][y] == WallFacing.SOUTH && y < heightInCellCount - 2) {
					if (!rotations[x][y].unpackAndTest(maze[x][y + 1]))
						maze[x][y + 1] |= rotations[x][y].OPPOSITE;
					else {
						maze[x][y] &= rotations[x][y].INVERTED;
						maze[x][y + 1] &= rotations[x][y + 1].INVERTED_OPPOSITE;
					}
				}
			}
		}//*/

		for (WallFacing facing : WallFacing.values())
			maze[halfWayPointX + facing.xOffset][halfWayPointY + facing.zOffset] &= facing.INVERTED_OPPOSITE;

		maze[halfWayPointX][halfWayPointY] = 0b10000;

		for (int x = 1; x < maze.length; x++) {
			for (int y = 1; y < maze[x].length; y++) {
				if (mazeLocal[x][y] == 0) {
					if (mazeLocal[x - 1][y] == 0) {
						maze[x][y] |= WallFacing.WEST.BYTE;
						maze[x - 1][y] |= WallFacing.WEST.OPPOSITE;
					}

					if (mazeLocal[x][y - 1] == 0) {
						maze[x][y] |= WallFacing.NORTH.BYTE;
						maze[x][y - 1] |= WallFacing.NORTH.OPPOSITE;
					}
				}
			}
		}

		for (Diagonals diagonals : Diagonals.values()) {
			cornerClippings[diagonals.ordinal()][0] = random.nextInt(maximumClipping) + 1;
			cornerClippings[diagonals.ordinal()][1] = random.nextInt(maximumClipping) + 1;

			for (int y = 0; y < cornerClippings[diagonals.ordinal()][0]; y++)
				for (int x = 0; x < cornerClippings[diagonals.ordinal()][1]; x++)
					maze[diagonals.operationX.convert(x, widthInCellCount - 2)][diagonals.operationY.convert(y, heightInCellCount - 2)] |= 0b10000;
		}
	}

	@SuppressWarnings({"fallthrough"})
	private void processInnerWallsAndFloor(StructurePiece structureComponent, StructurePieceAccessor list, RandomSource random, final int offset, final Rotation[] rotations) {
		for (int x = 0; x < widthInCellCount - 1; x++) {
			for (int y = 0; y < heightInCellCount - 1; y++) {
				final boolean xCenter = x == (widthInCellCount / 2) - 1;
				final boolean yCenter = y == (heightInCellCount / 2) - 1;
				// -------- HEDGE
				if ((!(xCenter || yCenter)) && (maze[x][y] & 0b10000) == 0b10000) continue;

				int rotation = 0;

				int xBB = this.sizeConstraints.minX() + (x * 12) + offset;
				int yBB = this.sizeConstraints.minY() + 1;
				int zBB = this.sizeConstraints.minZ() + (y * 12) + offset;

				if (!(xCenter && yCenter)) {
					StructurePiece structure;

					switch (maze[x][y] & 0b1111) { // These are inconsistent because I was stupid with the structures I saved to .nbt
						case 0b0010:    // FACE SOUTH
							rotation++; // rotate 270
						case 0b0001:    // FACE EAST
							rotation++; // rotate 180
						case 0b1000:    // FACE NORTH
							rotation++; // rotate 90
						case 0b0100:    // FACE WEST
							final Rotation rotationCap = rotations[rotation];

							if (random.nextBoolean())
								structure = new NagaCourtyardHedgeCapComponent(structureManager, (x * widthInCellCount) + y, xBB, yBB, zBB, rotationCap);
							else
								structure = new NagaCourtyardHedgeCapPillarComponent(structureManager, (x * widthInCellCount) + y, xBB, yBB, zBB, rotationCap);

							break;
						case 0b1001:    // NORTH EAST
							rotation++;
						case 0b1100:    // NORTH WEST
							rotation++;
						case 0b0110:    // SOUTH WEST
							rotation++;
						case 0b0011:    // SOUTH EAST
							final Rotation rotationCorner = rotations[rotation];

							structure = new NagaCourtyardHedgeCornerComponent(structureManager, maze[x][y], xBB, yBB, zBB, rotationCorner);
							break;
						case 0b1101:    // NOT SOUTH
							rotation++;
						case 0b1110:    // NOT EAST
							rotation++;
						case 0b0111:    // NOT NORTH
							rotation++;
						case 0b1011:    // NOT WEST
							final Rotation rotationT = rotations[rotation];

							structure = new NagaCourtyardHedgeTJunctionComponent(structureManager, maze[x][y], xBB, yBB, zBB, rotationT);
							break;
						case 0b1010:    // NORTH AND SOUTH
							rotation++;
						case 0b0101:    // EAST AND WEST
							final Rotation rotationLine = rotations[rotation];

							structure = new NagaCourtyardHedgeLineComponent(structureManager, maze[x][y], xBB, yBB, zBB, rotationLine);
							break;
						case 0b1111:
							structure = new NagaCourtyardHedgeIntersectionComponent(structureManager, maze[x][y], xBB, yBB, zBB, Rotation.NONE);
							break;
						default:
							if (random.nextInt(150) == 0) {
								structure = new CourtyardTerraceStatue(maze[x][y], xBB - 6, yBB - 3, zBB - 6, Rotation.NONE, this.structureManager);
							} else if (random.nextBoolean())
								structure = new CourtyardTerraceBrazier(maze[x][y], xBB - 6, yBB - 3, zBB - 6, Rotation.NONE, this.structureManager);
							else {
								structure = new CourtyardTerraceDuct(maze[x][y], xBB - 6, yBB - 3, zBB - 6, Rotation.NONE, this.structureManager);
							}

							break;
					}

					list.addPiece(structure);
					structure.addChildren(structureComponent, list, random);
				}

				// -------- Hedge Connectors

				xBB = this.sizeConstraints.minX() + (x * 12) + offset;
				zBB = this.sizeConstraints.minZ() + (y * 12) + offset;

				final boolean connectWest = WallFacing.WEST.unpackAndTest(maze[x][y]);
				final boolean connectNorth = WallFacing.NORTH.unpackAndTest(maze[x][y]);
				final boolean connectEast = WallFacing.EAST.unpackAndTest(maze[x][y]);
				final boolean connectSouth = WallFacing.SOUTH.unpackAndTest(maze[x][y]);

				if (connectWest) {
					NagaCourtyardHedgePadderComponent padding = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB - 1, yBB, zBB, Rotation.NONE);
					list.addPiece(padding);
					padding.addChildren(structureComponent, list, random);

					if (x > 0 && (maze[x - 1][y] & 0b10000) != 0b10000) {
						NagaCourtyardHedgePadderComponent padding2 = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB - 7, yBB, zBB, Rotation.NONE);
						list.addPiece(padding2);
						padding2.addChildren(structureComponent, list, random);
					}

					NagaCourtyardHedgeLineComponent structureLine = new NagaCourtyardHedgeLineComponent(structureManager, maze[x][y], xBB - 6, yBB, zBB, Rotation.NONE);
					list.addPiece(structureLine);
					structureLine.addChildren(structureComponent, list, random);
				}

				if (connectNorth) {
					NagaCourtyardHedgePadderComponent padding = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB + 4, yBB, zBB - 1, Rotation.CLOCKWISE_90);
					list.addPiece(padding);
					padding.addChildren(structureComponent, list, random);

					if (y > 0 && (maze[x][y - 1] & 0b10000) != 0b10000) {
						NagaCourtyardHedgePadderComponent padding2 = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB + 4, yBB, zBB - 7, Rotation.CLOCKWISE_90);
						list.addPiece(padding2);
						padding2.addChildren(structureComponent, list, random);
					}

					NagaCourtyardHedgeLineComponent structureLine = new NagaCourtyardHedgeLineComponent(structureManager, maze[x][y], xBB, yBB, zBB - 6, Rotation.CLOCKWISE_90);
					list.addPiece(structureLine);
					structureLine.addChildren(structureComponent, list, random);
				}

				if ((x >= widthInCellCount - 2 || (maze[x + 1][y] & 0b10000) == 0b10000) && connectEast) {
					NagaCourtyardHedgePadderComponent padding = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB + 5, yBB, zBB, Rotation.NONE);
					list.addPiece(padding);
					padding.addChildren(structureComponent, list, random);

					NagaCourtyardHedgeLineComponent structureLine = new NagaCourtyardHedgeLineComponent(structureManager, maze[x][y], xBB + 6, yBB, zBB, Rotation.NONE);
					list.addPiece(structureLine);
					structureLine.addChildren(structureComponent, list, random);
				}

				if ((y >= heightInCellCount - 2 || (maze[x][y + 1] & 0b10000) == 0b10000) && connectSouth) {
					NagaCourtyardHedgePadderComponent padding = new NagaCourtyardHedgePadderComponent(structureManager, maze[x][y], xBB + 4, yBB, zBB + 5, Rotation.CLOCKWISE_90);
					list.addPiece(padding);
					padding.addChildren(structureComponent, list, random);

					NagaCourtyardHedgeLineComponent structureLine = new NagaCourtyardHedgeLineComponent(structureManager, maze[x][y], xBB, yBB, zBB + 6, Rotation.CLOCKWISE_90);
					list.addPiece(structureLine);
					structureLine.addChildren(structureComponent, list, random);
				}//*/

				final boolean hasNoTerrace = (maze[x][y] & 0b1111) != 0;

				//final boolean westOfCenter  = x == (widthInCellCount  / 2) - 2;
				//final boolean northOfCenter = y == (heightInCellCount / 2) - 2;
				//final boolean eastOfCenter  = x ==  widthInCellCount  / 2;
				//final boolean southOfCenter = y ==  heightInCellCount / 2;

				final boolean westHasNoTerraceOrIsSafe = /*(!(eastOfCenter && yCenter      )) &&*/ (x == 0 || (maze[x - 1][y] & 0b10000) == 0b10000 || (maze[x - 1][y] & 0b1111) != 0);
				final boolean northHasNoTerraceOrIsSafe = /*(!(xCenter      && southOfCenter)) &&*/ (y == 0 || (maze[x][y - 1] & 0b10000) == 0b10000 || (maze[x][y - 1] & 0b1111) != 0);
				final boolean eastHasNoTerraceOrIsSafe = /*(!(westOfCenter && yCenter      )) &&*/ (x == widthInCellCount - 2 || (maze[x + 1][y] & 0b10000) == 0b10000);
				final boolean southHasNoTerraceOrIsSafe = /*(!(xCenter      && northOfCenter)) &&*/ (y == heightInCellCount - 2 || (maze[x][y + 1] & 0b10000) == 0b10000);

				final boolean westNorthHasNoTerraceOrIsSafe = /*(!(eastOfCenter && southOfCenter)) &&*/ ((x == 0 || y == 0 || maze[x - 1][y - 1] != 0));
				final boolean westSouthHasNoTerraceOrIsSafe = /*(!(eastOfCenter && northOfCenter)) &&*/ ((x == 0 || y >= heightInCellCount - 2 || maze[x - 1][y + 1] != 0));
				final boolean eastNorthHasNoTerraceOrIsSafe = /*(!(westOfCenter && southOfCenter)) &&*/ ((x >= widthInCellCount - 2 || y == 0 || maze[x + 1][y - 1] != 0));
				final boolean eastSouthHasNoTerraceOrIsSafe = /*(!(westOfCenter && northOfCenter)) &&*/ ((x >= widthInCellCount - 2 || y >= heightInCellCount - 2 || maze[x + 1][y + 1] != 0));

				// -------- PATHS - cardinal

				if (xCenter && yCenter) {
					CourtyardPathPiece path = new CourtyardPathPiece(maze[x][y], xBB - 1, yBB - 1, zBB - 1, structureManager);
					list.addPiece(path);
					path.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && westHasNoTerraceOrIsSafe && !connectWest) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB - 7, yBB - 1, zBB - 1, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && northHasNoTerraceOrIsSafe && !connectNorth) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB - 1, yBB - 1, zBB - 7, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && eastHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB + 5, yBB - 1, zBB - 1, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && southHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB - 1, yBB - 1, zBB + 5, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				// -------- PATHS - Diagonal

				if (hasNoTerrace && westHasNoTerraceOrIsSafe && northHasNoTerraceOrIsSafe && westNorthHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB - 7, yBB - 1, zBB - 7, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && westHasNoTerraceOrIsSafe && southHasNoTerraceOrIsSafe && westSouthHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB - 7, yBB - 1, zBB + 5, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && eastHasNoTerraceOrIsSafe && northHasNoTerraceOrIsSafe && eastNorthHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB + 5, yBB - 1, zBB - 7, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}

				if (hasNoTerrace && eastHasNoTerraceOrIsSafe && southHasNoTerraceOrIsSafe && eastSouthHasNoTerraceOrIsSafe) {
					CourtyardPathPiece path2 = new CourtyardPathPiece(maze[x][y], xBB + 5, yBB - 1, zBB + 5, structureManager);
					list.addPiece(path2);
					path2.addChildren(structureComponent, list, random);
				}//*/
			}
		}
	}

	private void processOuterWalls(StructurePiece structureComponent, StructurePieceAccessor list, RandomSource random, final int offset, final Rotation[] rotations) {
		// -------- WALLS
		for (Diagonals diagonal : Diagonals.values()) {
			// Walls at corner notches going with X Axis, crossing Z Axis. Sideways.

			int zBoundX = (diagonal.isTop()
				? this.sizeConstraints.minZ() + (cornerClipping[diagonal.ordinal()][0] * 12) - 3
				: this.sizeConstraints.maxZ() - (cornerClipping[diagonal.ordinal()][0] * 12) + 1);

			CourtyardWallPadder paddingStartX =
				new CourtyardWallPadder(
					(cornerClipping[diagonal.ordinal()][1] * 2) + 1,
					(diagonal.isLeft() ? this.sizeConstraints.minX() + 2 : this.sizeConstraints.maxX() - 2),
					this.sizeConstraints.minY() + 1,
					zBoundX,
					Rotation.NONE,
					this.structureManager);

			list.addPiece(paddingStartX);
			paddingStartX.addChildren(structureComponent, list, random);

			int xPadOffset = diagonal.isLeft() ? 11 : -1;

			for (int i = 0; i < cornerClipping[diagonal.ordinal()][1] - 1; i++) {
				int xBound = (diagonal.isLeft() ? this.sizeConstraints.minX() + (i * 12) + 3 : this.sizeConstraints.maxX() - (i * 12) - 13);

				CourtyardWall wall = new CourtyardWall(i * 2, xBound, this.sizeConstraints.minY() + 1, zBoundX, Rotation.NONE, this.structureManager);
				list.addPiece(wall);
				wall.addChildren(structureComponent, list, random);

				CourtyardWallPadder padding = new CourtyardWallPadder((i * 2) + 1, xBound + xPadOffset, this.sizeConstraints.minY() + 1, zBoundX, Rotation.NONE, this.structureManager);
				list.addPiece(padding);
				padding.addChildren(structureComponent, list, random);
			}

			// Walls at corner notches going with Z Axis, crossing X Axis. Up/Down.

			int xBoundZ = (diagonal.isLeft()
				? this.sizeConstraints.minX() + (cornerClipping[diagonal.ordinal()][1] * 12) - 1
				: this.sizeConstraints.maxX() - (cornerClipping[diagonal.ordinal()][1] * 12) + 3);

			CourtyardWallPadder paddingStartZ =
				new CourtyardWallPadder(
					(cornerClipping[diagonal.ordinal()][1] * 2) + 1,
					xBoundZ,
					this.sizeConstraints.minY() + 1,
					(diagonal.isTop() ? this.sizeConstraints.minZ() + 2 : this.sizeConstraints.maxZ() - 2),
					Rotation.CLOCKWISE_90,
					this.structureManager);

			list.addPiece(paddingStartZ);
			paddingStartZ.addChildren(structureComponent, list, random);

			int zPadOffset = diagonal.isTop() ? 11 : -1;

			for (int i = 0; i < cornerClipping[diagonal.ordinal()][0] - 1; i++) {
				int zBound = (diagonal.isTop() ? this.sizeConstraints.minZ() + (i * 12) + 3 : this.sizeConstraints.maxZ() - (i * 12) - 13);

				CourtyardWall wall = new CourtyardWall(i * 2, xBoundZ, this.sizeConstraints.minY() + 1, zBound, Rotation.CLOCKWISE_90, this.structureManager);
				list.addPiece(wall);
				wall.addChildren(structureComponent, list, random);

				CourtyardWallPadder padding = new CourtyardWallPadder((i * 2) + 1, xBoundZ, this.sizeConstraints.minY() + 1, zBound + zPadOffset, Rotation.CLOCKWISE_90, this.structureManager);
				list.addPiece(padding);
				padding.addChildren(structureComponent, list, random);
			}

			// WALL CORNERS

			int wallCornerInnerX = this.sizeConstraints.minX() + (diagonal.operationX.convert(cornerClipping[diagonal.ordinal()][1], widthInCellCount - 1) * 12);// + (diagonal.isLeft() ? -3 : 3);
			int wallCornerInnerZ = this.sizeConstraints.minZ() + (diagonal.operationY.convert(cornerClipping[diagonal.ordinal()][0], heightInCellCount - 1) * 12);// + (diagonal.isTop() ? -3 : 3);

			Rotation cornerRotation = rotations[diagonal.ordinal() % rotations.length];

			boolean shiftX = cornerRotation == Rotation.CLOCKWISE_180 || cornerRotation == Rotation.COUNTERCLOCKWISE_90;
			boolean shiftZ = cornerRotation == Rotation.CLOCKWISE_90 || cornerRotation == Rotation.CLOCKWISE_180;

			// These touch upper/lower borders

			CourtyardWallCornerOuter corner1 = new CourtyardWallCornerOuter(diagonal.ordinal() * 3,
				wallCornerInnerX + (shiftZ ? (shiftX ? 1 : 7) : (shiftX ? -3 : 3)),
				this.sizeConstraints.minY() + 1,
				(diagonal.isTop() ? this.sizeConstraints.minZ() : this.sizeConstraints.maxZ() - 1) + (shiftZ ? (shiftX ? 4 : 0) : (shiftX ? 1 : -3)),
				cornerRotation,
				this.structureManager);
			list.addPiece(corner1);
			corner1.addChildren(structureComponent, list, random);

			// These touch side borders

			CourtyardWallCornerOuter corner2 = new CourtyardWallCornerOuter((diagonal.ordinal() * 3) + 1,
				(diagonal.isLeft() ? this.sizeConstraints.minX() : this.sizeConstraints.maxX() - 1) + (shiftZ ? (shiftX ? 1 : 4) : (shiftX ? -3 : 0)),
				this.sizeConstraints.minY() + 1,
				wallCornerInnerZ + (shiftZ ? (shiftX ? 7 : 3) : (shiftX ? 1 : -3)),
				cornerRotation,
				this.structureManager);
			list.addPiece(corner2);
			corner2.addChildren(structureComponent, list, random);

			// These are inner corners

			CourtyardWallCornerInner innerCorner = new CourtyardWallCornerInner((diagonal.ordinal() * 3) + 3,
				wallCornerInnerX + (shiftZ ? (shiftX ? -1 : 13) : (shiftX ? -9 : 5)),// + (diagonal.isLeft() ? -6 : 2),
				this.sizeConstraints.minY() + 1,
				wallCornerInnerZ + (shiftZ ? (shiftX ? 13 : 5) : (shiftX ? -1 : -9)),// + (diagonal.isTop() ? -6 : 2),
				cornerRotation,
				this.structureManager);
			list.addPiece(innerCorner);
			innerCorner.addChildren(structureComponent, list, random);
		}

		// Top / North

		for (int i = cornerClipping[3][1]; i < (widthInCellCount - 1) - cornerClipping[0][1]; i++) {
			CourtyardWall wall = new CourtyardWall(i, this.sizeConstraints.minX() + (i * 12) + offset - 3, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() - 3, Rotation.NONE, this.structureManager);
			list.addPiece(wall);
			wall.addChildren(structureComponent, list, random);

			CourtyardWallPadder padding = new CourtyardWallPadder(i, this.sizeConstraints.minX() + (i * 12) + offset - 4, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() - 3, Rotation.NONE, this.structureManager);
			list.addPiece(padding);
			padding.addChildren(structureComponent, list, random);
		}

		CourtyardWallPadder padding2 = new CourtyardWallPadder((widthInCellCount - 1) - cornerClipping[0][1], this.sizeConstraints.minX() + (((widthInCellCount - 1) - cornerClipping[0][1]) * 12) + offset - 4, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() - 3, Rotation.NONE, this.structureManager);
		list.addPiece(padding2);
		padding2.addChildren(structureComponent, list, random);

		// Bottom / South

		for (int i = cornerClipping[2][1]; i < (widthInCellCount - 1) - cornerClipping[1][1]; i++) {
			CourtyardWall wall = new CourtyardWall(i, this.sizeConstraints.minX() + (i * 12) + offset - 3, this.sizeConstraints.minY() + 1, this.sizeConstraints.maxZ() + 1, Rotation.NONE, this.structureManager);
			list.addPiece(wall);
			wall.addChildren(structureComponent, list, random);

			CourtyardWallPadder padding = new CourtyardWallPadder(i, this.sizeConstraints.minX() + (i * 12) + offset - 4, this.sizeConstraints.minY() + 1, this.sizeConstraints.maxZ() + 1, Rotation.NONE, this.structureManager);
			list.addPiece(padding);
			padding.addChildren(structureComponent, list, random);
		}

		CourtyardWallPadder padding5 = new CourtyardWallPadder((widthInCellCount - 1) - cornerClipping[1][1], this.sizeConstraints.minX() + (((widthInCellCount - 1) - cornerClipping[1][1]) * 12) + offset - 4, this.sizeConstraints.minY() + 1, this.sizeConstraints.maxZ() + 1, Rotation.NONE, this.structureManager);
		list.addPiece(padding5);
		padding5.addChildren(structureComponent, list, random);

		// Left / West

		for (int i = cornerClipping[3][0]; i < (heightInCellCount - 1) - cornerClipping[2][0]; i++) {
			CourtyardWall wall = new CourtyardWall(i, this.sizeConstraints.minX() - 1, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (i * 12) + offset - 3, Rotation.CLOCKWISE_90, this.structureManager);
			list.addPiece(wall);
			wall.addChildren(structureComponent, list, random);

			CourtyardWallPadder padding = new CourtyardWallPadder(i, this.sizeConstraints.minX() - 1, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (i * 12) + offset - 4, Rotation.CLOCKWISE_90, this.structureManager);
			list.addPiece(padding);
			padding.addChildren(structureComponent, list, random);
		}

		CourtyardWallPadder padding8 = new CourtyardWallPadder((heightInCellCount - 1) - cornerClipping[2][0], this.sizeConstraints.minX() - 1, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (((heightInCellCount - 1) - cornerClipping[2][0]) * 12) + offset - 4, Rotation.CLOCKWISE_90, this.structureManager);
		list.addPiece(padding8);
		padding8.addChildren(structureComponent, list, random);

		// Right / East

		for (int i = cornerClipping[0][0]; i < (heightInCellCount - 1) - cornerClipping[1][0]; i++) {
			CourtyardWall wall = new CourtyardWall(i, this.sizeConstraints.maxX() + 3, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (i * 12) + offset - 3, Rotation.CLOCKWISE_90, this.structureManager);
			list.addPiece(wall);
			wall.addChildren(structureComponent, list, random);

			CourtyardWallPadder padding = new CourtyardWallPadder(i, this.sizeConstraints.maxX() + 3, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (i * 12) + offset - 4, Rotation.CLOCKWISE_90, this.structureManager);
			list.addPiece(padding);
			padding.addChildren(structureComponent, list, random);
		}

		CourtyardWallPadder padding11 = new CourtyardWallPadder(((heightInCellCount - 1) - cornerClipping[1][0]), this.sizeConstraints.maxX() + 3, this.sizeConstraints.minY() + 1, this.sizeConstraints.minZ() + (((heightInCellCount - 1) - cornerClipping[1][0]) * 12) + offset - 4, Rotation.CLOCKWISE_90, this.structureManager);
		list.addPiece(padding11);
		padding11.addChildren(structureComponent, list, random);
	}

	private static String getStringFromFacings(int directions) {
		return switch (directions & 0b1111) {
			case 0b0010 -> " ╷ ";
			case 0b0001 -> " ╶─";
			case 0b1000 -> " ╵ ";
			case 0b0100 -> "─╴ ";
			case 0b1001 -> " └─";
			case 0b1100 -> "─┘ ";
			case 0b0110 -> "─┐ ";
			case 0b0011 -> " ┌─";
			case 0b1101 -> "─┴─";
			case 0b1110 -> "─┤ ";
			case 0b0111 -> "─┬─";
			case 0b1011 -> " ├─";
			case 0b1010 -> " │ ";
			case 0b0101 -> "───";
			case 0b1111 -> "─┼─";
			default -> " • ";
		};
	}

	protected enum WallFacing {
		EAST(0b0001, 0b0100, 0b1110, 0b1011, Direction.EAST, 1, 0),
		SOUTH(0b0010, 0b1000, 0b1101, 0b0111, Direction.SOUTH, 0, 1),
		WEST(0b0100, 0b0001, 0b1011, 0b1110, Direction.WEST, -1, 0),
		NORTH(0b1000, 0b0010, 0b0111, 0b1101, Direction.NORTH, 0, -1);

		private final int BYTE;
		private final int OPPOSITE;
		private final int INVERTED;
		private final int INVERTED_OPPOSITE;
		private final int xOffset;
		private final int zOffset;

		WallFacing(int bite, int oppositeBite, int inverted, int invertedOpposite, Direction enumFacing, int xOffset, int zOffset) {
			this.BYTE = bite;
			this.OPPOSITE = oppositeBite;
			this.INVERTED = inverted;
			this.INVERTED_OPPOSITE = invertedOpposite;
			this.xOffset = xOffset;
			this.zOffset = zOffset;
		}

		private boolean unpackAndTest(int directions) {
			return (this.BYTE & directions) == this.BYTE;
		}

	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		ListTag mazeX = new ListTag();

		for (int x = 0; x < widthInCellCount - 1; x++) {
			ListTag mazeY = new ListTag();

			for (int y = 0; y < heightInCellCount - 1; y++)
				mazeY.add(IntTag.valueOf(maze[x][y]));

			mazeX.add(mazeY);
		}

		tagCompound.putInt("mazeWidth", widthInCellCount);
		tagCompound.putInt("mazeHeight", heightInCellCount);
		tagCompound.put("maze", mazeX);
		tagCompound.put("constraints", BoundingBoxUtils.boundingBoxToNBT(this.sizeConstraints));
	}
}
