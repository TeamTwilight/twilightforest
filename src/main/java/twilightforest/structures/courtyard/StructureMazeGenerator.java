package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.enums.Diagonals;
import twilightforest.structures.StructureTFComponent;

import java.util.List;
import java.util.Random;

public abstract class StructureMazeGenerator extends StructureTFComponent {
    protected int[][] maze;
    private int[][] cornerClipping = new int[4][2];
    private int widthInCellCount;
    private int heightInCellCount;

    public StructureMazeGenerator(IStructurePieceType piece, CompoundNBT nbt) {
        super(piece, nbt);

		this.widthInCellCount = nbt.getInt("mazeWidth");
		this.heightInCellCount = nbt.getInt("mazeHeight");

		maze = new int[this.widthInCellCount-1][this.heightInCellCount-1];

		ListNBT mazeX = nbt.getList("maze", 9);

		for (int x = 0; x < widthInCellCount-1; x++) {
			INBT mazeY = mazeX.get(x);

			if (mazeY instanceof ListNBT)
				for (int y = 0; y < heightInCellCount - 1; y++) maze[x][y] = ((ListNBT) mazeY).getInt(y);
		}
    }

    StructureMazeGenerator(IStructurePieceType type, TFFeature feature, Random rand, int i, int widthInCellCount, int heightInCellCount) {
        super(type, feature, i);
        this.widthInCellCount = widthInCellCount;
        this.heightInCellCount = heightInCellCount;
        this.maze = new int[widthInCellCount-1][heightInCellCount-1];
        generateMaze(this.maze, this.cornerClipping, rand, this.widthInCellCount, this.heightInCellCount, 2);
    }

	// Actually assemble maze
    @Override
    public void buildComponent(StructurePiece structureComponent, List<StructurePiece> list, Random random) {
		super.buildComponent(structureComponent, list, random);
		final int offset = 6;

		final Rotation[] rotations = Rotation.values();

		this.processInnerWallsAndFloor(structureComponent, list, random, offset, rotations);

		this.processOuterWalls(structureComponent, list, random, offset, rotations);
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateMaze(int[][] maze, int[][] cornerClippings, Random random, int widthInCellCount, int heightInCellCount, int maximumClipping) {
        // Trying to keep this optimized for speed I guess

        // Generates a connection map for the walls. It modifies the two-dimensional int array, inserting packed binary strings as ints.
        // A One in its binary interpretation means the wall connects in this direction.
        // As a result of this being a "connectome" of maze walls. It is "Size In Cell count" - 1.

        final WallFacing rotations[][] = new WallFacing[maze.length][maze[0].length];

        for (int x = 0; x < widthInCellCount-1; x++) {
            for (int y = 0; y < heightInCellCount-1; y++) {
                rotations[x][y] = WallFacing.values()[random.nextInt(WallFacing.values().length)];
                // set the initial base byte
                maze[x][y] |= rotations[x][y].BYTE;
            }
        }

        final int[][] mazeLocal = maze.clone();

        int halfWayPointX = (widthInCellCount / 2) - 1;
        int halfWayPointY = (heightInCellCount / 2) - 1;

        for (int y = 0; y < heightInCellCount-1; y++) {
            for (int x = 0; x < widthInCellCount-1; x++) {
                if (x == halfWayPointX && y == halfWayPointY) continue;

                // Did we pick west and will we not get an AIOOBException accessing array
                if (rotations[x][y] == WallFacing.WEST && x > 0) {
                    // If neighbor does not connect to west, connect it to east
                    if (!rotations[x][y].unpackAndTest(maze[x-1][y]))
                        maze[x-1][y] |= rotations[x][y].OPPOSITE;
                    else { // else we cut the connection
                        // remove connection for the maze part we're looking at
                        maze[x][y] &= rotations[x][y].INVERTED;
                        // remove connection for the adjacent maze part
                        maze[x-1][y] &= rotations[x-1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.NORTH && y > 0 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y-1]))
                        maze[x][y-1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y-1] &= rotations[x][y-1].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.EAST && x < widthInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x+1][y]))
                        maze[x+1][y] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x+1][y] &= rotations[x+1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.SOUTH && y < heightInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y+1]))
                        maze[x][y+1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y+1] &= rotations[x][y+1].INVERTED_OPPOSITE;
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
                    if (mazeLocal[x-1][y] == 0) {
                        maze[x][y]   |= WallFacing.WEST.BYTE;
                        maze[x-1][y] |= WallFacing.WEST.OPPOSITE;
                    }

                    if (mazeLocal[x][y-1] == 0) {
                        maze[x][y]   |= WallFacing.NORTH.BYTE;
                        maze[x][y-1] |= WallFacing.NORTH.OPPOSITE;
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

    @SuppressWarnings({"ConstantConditions", "fallthrough"})
    private void processInnerWallsAndFloor(StructurePiece structureComponent, List<StructurePiece> list, Random random, final int offset, final Rotation[] rotations) {
        for (int x = 0; x < widthInCellCount - 1; x++) {
            for (int y = 0; y < heightInCellCount - 1; y++) {
                final boolean xCenter = x == (widthInCellCount  / 2) - 1;
                final boolean yCenter = y == (heightInCellCount / 2) - 1;
                // -------- HEDGE
                if ((!(xCenter || yCenter)) && (maze[x][y] & 0b10000) == 0b10000) continue;

                int rotation = 0;

                int xBB = boundingBox.minX + (x * 12) + offset;
                int yBB = boundingBox.minY + 1;
                int zBB = boundingBox.minZ + (y * 12) + offset;

                if (!(xCenter && yCenter)) {
                    StructureTFComponent structure;

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
                                structure = new ComponentNagaCourtyardHedgeCap(getFeatureType(), (x * widthInCellCount) + y, xBB, yBB, zBB, rotationCap);
                            else
                                structure = new ComponentNagaCourtyardHedgeCapPillar(getFeatureType(), (x * widthInCellCount) + y, xBB, yBB, zBB, rotationCap);

                            break;
                        case 0b1001:    // NORTH EAST
                            rotation++;
                        case 0b1100:    // NORTH WEST
                            rotation++;
                        case 0b0110:    // SOUTH WEST
                            rotation++;
                        case 0b0011:    // SOUTH EAST
                            final Rotation rotationCorner = rotations[rotation];

                            structure = new ComponentNagaCourtyardHedgeCorner(getFeatureType(), maze[x][y], xBB, yBB, zBB, rotationCorner);
                            break;
                        case 0b1101:    // NOT SOUTH
                            rotation++;
                        case 0b1110:    // NOT EAST
                            rotation++;
                        case 0b0111:    // NOT NORTH
                            rotation++;
                        case 0b1011:    // NOT WEST
                            final Rotation rotationT = rotations[rotation];

                            structure = new ComponentNagaCourtyardHedgeTJunction(getFeatureType(), maze[x][y], xBB, yBB, zBB, rotationT);
                            break;
                        case 0b1010:    // NORTH AND SOUTH
                            rotation++;
                        case 0b0101:    // EAST AND WEST
                            final Rotation rotationLine = rotations[rotation];

                            structure = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB, yBB, zBB, rotationLine);
                            break;
                        case 0b1111:
                            structure = new ComponentNagaCourtyardHedgeIntersection(getFeatureType(), maze[x][y], xBB, yBB, zBB, Rotation.NONE);
                            break;
                        default:
                            if (random.nextInt(150) == 0) {
                                structure = new ComponentNagaCourtyardTerraceStatue(getFeatureType(), maze[x][y], xBB - 6, yBB - 3, zBB - 6, rotations[random.nextInt(rotations.length)]);
                            } else if (random.nextBoolean())
                                structure = new ComponentNagaCourtyardTerraceBrazier(getFeatureType(), maze[x][y], xBB - 6, yBB - 3, zBB - 6, Rotation.NONE);
                            else {
                                structure = new ComponentNagaCourtyardTerraceDuct(getFeatureType(), maze[x][y], xBB - 6, yBB - 3, zBB - 6, rotations[random.nextInt(rotations.length)]);
                            }

                            break;
                    }

                    list.add(structure);
                    structure.buildComponent(structureComponent, list, random);
                }

                // -------- Hedge Connectors

                xBB = boundingBox.minX + (x * 12) + offset;
                zBB = boundingBox.minZ + (y * 12) + offset;

                final boolean connectWest  = WallFacing.WEST .unpackAndTest(maze[x][y]);
                final boolean connectNorth = WallFacing.NORTH.unpackAndTest(maze[x][y]);
                final boolean connectEast  = WallFacing.EAST .unpackAndTest(maze[x][y]);
                final boolean connectSouth = WallFacing.SOUTH.unpackAndTest(maze[x][y]);

                if (connectWest) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB - 1, yBB, zBB, Rotation.NONE);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    if (x > 0 && (maze[x-1][y] & 0b10000) != 0b10000) {
                        ComponentNagaCourtyardHedgePadder padding2 = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB - 7, yBB, zBB, Rotation.NONE);
                        list.add(padding2);
                        padding2.buildComponent(structureComponent, list, random);
                    }

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB - 6, yBB, zBB, Rotation.NONE);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }

                if (connectNorth) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 4, yBB, zBB - 1, Rotation.CLOCKWISE_90);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    if (y > 0 && (maze[x][y-1] & 0b10000) != 0b10000) {
                        ComponentNagaCourtyardHedgePadder padding2 = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 4, yBB, zBB - 7, Rotation.CLOCKWISE_90);
                        list.add(padding2);
                        padding2.buildComponent(structureComponent, list, random);
                    }

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB, yBB, zBB - 6, Rotation.CLOCKWISE_90);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }

                if ((x >= widthInCellCount - 2 || (maze[x+1][y] & 0b10000) == 0b10000) && connectEast) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 5, yBB, zBB, Rotation.NONE);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB + 6, yBB, zBB, Rotation.NONE);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }

                if ((y >= heightInCellCount - 2 || (maze[x][y+1] & 0b10000) == 0b10000) && connectSouth) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 4, yBB, zBB + 5, Rotation.CLOCKWISE_90);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB, yBB, zBB + 6, Rotation.CLOCKWISE_90);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }//*/

                final boolean hasNoTerrace = (maze[x][y] & 0b1111) != 0;

                //final boolean westOfCenter  = x == (widthInCellCount  / 2) - 2;
                //final boolean northOfCenter = y == (heightInCellCount / 2) - 2;
                //final boolean eastOfCenter  = x ==  widthInCellCount  / 2;
                //final boolean southOfCenter = y ==  heightInCellCount / 2;

                final boolean westHasNoTerraceOrIsSafe  = /*(!(eastOfCenter && yCenter      )) &&*/ (x == 0                   || (maze[x-1][y] & 0b10000) == 0b10000 || (maze[x-1][y] & 0b1111) != 0);
                final boolean northHasNoTerraceOrIsSafe = /*(!(xCenter      && southOfCenter)) &&*/ (y == 0                   || (maze[x][y-1] & 0b10000) == 0b10000 || (maze[x][y-1] & 0b1111) != 0);
                final boolean eastHasNoTerraceOrIsSafe  = /*(!(westOfCenter && yCenter      )) &&*/ (x == widthInCellCount -2 || (maze[x+1][y] & 0b10000) == 0b10000);
                final boolean southHasNoTerraceOrIsSafe = /*(!(xCenter      && northOfCenter)) &&*/ (y == heightInCellCount-2 || (maze[x][y+1] & 0b10000) == 0b10000);

                final boolean westNorthHasNoTerraceOrIsSafe = /*(!(eastOfCenter && southOfCenter)) &&*/ ((x == 0                   || y == 0                   || maze[x - 1][y - 1] != 0));
                final boolean westSouthHasNoTerraceOrIsSafe = /*(!(eastOfCenter && northOfCenter)) &&*/ ((x == 0                   || y >= heightInCellCount-2 || maze[x - 1][y + 1] != 0));
                final boolean eastNorthHasNoTerraceOrIsSafe = /*(!(westOfCenter && southOfCenter)) &&*/ ((x >= widthInCellCount -2 || y == 0                   || maze[x + 1][y - 1] != 0));
                final boolean eastSouthHasNoTerraceOrIsSafe = /*(!(westOfCenter && northOfCenter)) &&*/ ((x >= widthInCellCount -2 || y >= heightInCellCount-2 || maze[x + 1][y + 1] != 0));

                // -------- PATHS - cardinal

                if (xCenter && yCenter) {
                    ComponentNagaCourtyardPath path = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 1, yBB - 1, zBB - 1);
                    list.add(path);
                    path.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && westHasNoTerraceOrIsSafe && !connectWest) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 7, yBB - 1, zBB - 1);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && northHasNoTerraceOrIsSafe && !connectNorth) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 1, yBB - 1, zBB - 7);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && eastHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB + 5, yBB - 1, zBB - 1);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && southHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 1, yBB - 1, zBB + 5);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                // -------- PATHS - Diagonal

                if (hasNoTerrace && westHasNoTerraceOrIsSafe && northHasNoTerraceOrIsSafe && westNorthHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 7, yBB - 1, zBB - 7);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && westHasNoTerraceOrIsSafe && southHasNoTerraceOrIsSafe && westSouthHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 7, yBB - 1, zBB + 5);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && eastHasNoTerraceOrIsSafe && northHasNoTerraceOrIsSafe && eastNorthHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB + 5, yBB - 1, zBB - 7);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && eastHasNoTerraceOrIsSafe && southHasNoTerraceOrIsSafe && eastSouthHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB + 5, yBB - 1, zBB + 5);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }//*/
            }
        }
    }

    private void processOuterWalls(StructurePiece structureComponent, List<StructurePiece> list, Random random, final int offset, final Rotation[] rotations) {
        // -------- WALLS
        for (Diagonals diagonal : Diagonals.values()) {
            // Walls at corner notches going with X Axis, crossing Z Axis. Sideways.

            int zBoundX = (diagonal.isTop()
                    ? boundingBox.minZ + (cornerClipping[diagonal.ordinal()][0] * 12) - 3
                    : boundingBox.maxZ - (cornerClipping[diagonal.ordinal()][0] * 12) + 1 );

            ComponentNagaCourtyardWallPadder paddingStartX =
                    new ComponentNagaCourtyardWallPadder(
                            getFeatureType(),
                            ( cornerClipping[diagonal.ordinal()][1] * 2 ) + 1,
                            ( diagonal.isLeft() ? boundingBox.minX + 2 : boundingBox.maxX - 2 ),
                            boundingBox.minY,
                            zBoundX,
                            Rotation.NONE );

            list.add(paddingStartX);
            paddingStartX.buildComponent(structureComponent, list, random);

            int xPadOffset = diagonal.isLeft() ? 11 : -1;

            for (int i = 0; i < cornerClipping[diagonal.ordinal()][1] - 1; i++) {
                int xBound = (diagonal.isLeft() ? boundingBox.minX + (i*12) + 3 : boundingBox.maxX - (i*12) - 13 );

                ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i*2, xBound, boundingBox.minY, zBoundX, Rotation.NONE);
                list.add(wall);
                wall.buildComponent(structureComponent, list, random);

                ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), (i*2)+1, xBound + xPadOffset, boundingBox.minY, zBoundX, Rotation.NONE);
                list.add(padding);
                padding.buildComponent(structureComponent, list, random);
            }

            // Walls at corner notches going with Z Axis, crossing X Axis. Up/Down.

            int xBoundZ = (diagonal.isLeft()
                    ? boundingBox.minX + (cornerClipping[diagonal.ordinal()][1] * 12) - 1
                    : boundingBox.maxX - (cornerClipping[diagonal.ordinal()][1] * 12) + 3 );

            ComponentNagaCourtyardWallPadder paddingStartZ =
                    new ComponentNagaCourtyardWallPadder(
                            getFeatureType(),
                            ( cornerClipping[diagonal.ordinal()][1] * 2 ) + 1,
                            xBoundZ,
                            boundingBox.minY,
                            ( diagonal.isTop() ? boundingBox.minZ + 2 : boundingBox.maxZ - 2 ),
                            Rotation.CLOCKWISE_90 );

            list.add(paddingStartZ);
            paddingStartZ.buildComponent(structureComponent, list, random);

            int zPadOffset = diagonal.isTop() ? 11 : -1;

            for (int i = 0; i < cornerClipping[diagonal.ordinal()][0] - 1; i++) {
                int zBound = (diagonal.isTop() ? boundingBox.minZ + (i*12) + 3 : boundingBox.maxZ - (i*12) - 13 );

                ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i*2, xBoundZ - 10, boundingBox.minY, zBound, Rotation.CLOCKWISE_90);
                list.add(wall);
                wall.buildComponent(structureComponent, list, random);

                ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), (i*2)+1, xBoundZ, boundingBox.minY, zBound + zPadOffset, Rotation.CLOCKWISE_90);
                list.add(padding);
                padding.buildComponent(structureComponent, list, random);
            }

            // WALL CORNERS

            int wallCornerInnerX = boundingBox.minX + (diagonal.operationX.convert(cornerClipping[diagonal.ordinal()][1], widthInCellCount - 1) * 12) + (diagonal.isLeft() ? -3 : 3);
            int wallCornerInnerZ = boundingBox.minZ + (diagonal.operationY.convert(cornerClipping[diagonal.ordinal()][0], heightInCellCount - 1) * 12) + (diagonal.isTop() ? -3 : 3);

            // These touch upper/lower borders

            ComponentNagaCourtyardWallCorner corner1 = new ComponentNagaCourtyardWallCorner(getFeatureType(), diagonal.ordinal() * 3,
                    wallCornerInnerX,
                    boundingBox.minY,
                    diagonal.isTop() ? boundingBox.minZ - 3 : boundingBox.maxZ - 1,
                    rotations[diagonal.ordinal() % rotations.length]);
            list.add(corner1);
            corner1.buildComponent(structureComponent, list, random);

            // These touch side borders

            ComponentNagaCourtyardWallCorner corner2 = new ComponentNagaCourtyardWallCorner(getFeatureType(), (diagonal.ordinal() * 3) + 1,
                    diagonal.isLeft() ? boundingBox.minX - 3 : boundingBox.maxX - 1,
                    boundingBox.minY,
                    wallCornerInnerZ,
                    rotations[diagonal.ordinal() % rotations.length]);
            list.add(corner2);
            corner2.buildComponent(structureComponent, list, random);

            // These are inner anti-corners

            ComponentNagaCourtyardWallCornerAlt innerCorner = new ComponentNagaCourtyardWallCornerAlt(getFeatureType(), (diagonal.ordinal() * 3) + 3,
                    wallCornerInnerX + (diagonal.isLeft() ? -6 : 2),
                    boundingBox.minY,
                    wallCornerInnerZ + (diagonal.isTop() ? -6 : 2),
                    rotations[diagonal.ordinal() % rotations.length]);
            list.add(innerCorner);
            innerCorner.buildComponent(structureComponent, list, random);
        }

        // Top / North

        for (int i = cornerClipping[3][1]; i < (widthInCellCount-1) - cornerClipping[0][1]; i++) {
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.minX + (i * 12) + offset - 3, boundingBox.minY, boundingBox.minZ - 3, Rotation.NONE);
            list.add(wall);
            wall.buildComponent(structureComponent, list, random);

            ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), i, boundingBox.minX + (i * 12) + offset - 4, boundingBox.minY, boundingBox.minZ - 3, Rotation.NONE);
            list.add(padding);
            padding.buildComponent(structureComponent, list, random);
        }

        ComponentNagaCourtyardWallPadder padding2 = new ComponentNagaCourtyardWallPadder(getFeatureType(), (widthInCellCount-1) - cornerClipping[0][1], boundingBox.minX + (((widthInCellCount-1) - cornerClipping[0][1]) * 12) + offset - 4, boundingBox.minY, boundingBox.minZ - 3, Rotation.NONE);
        list.add(padding2);
        padding2.buildComponent(structureComponent, list, random);

        // Bottom / South

        for (int i = cornerClipping[2][1]; i < (widthInCellCount-1) - cornerClipping[1][1]; i++) {
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.minX + (i * 12) + offset - 3, boundingBox.minY, boundingBox.maxZ + 1, Rotation.NONE);
            list.add(wall);
            wall.buildComponent(structureComponent, list, random);

            ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), i, boundingBox.minX + (i * 12) + offset - 4, boundingBox.minY, boundingBox.maxZ + 1, Rotation.NONE);
            list.add(padding);
            padding.buildComponent(structureComponent, list, random);
        }

        ComponentNagaCourtyardWallPadder padding5 = new ComponentNagaCourtyardWallPadder(getFeatureType(), (widthInCellCount-1) - cornerClipping[1][1], boundingBox.minX + (((widthInCellCount-1) - cornerClipping[1][1]) * 12) + offset - 4, boundingBox.minY, boundingBox.maxZ + 1, Rotation.NONE);
        list.add(padding5);
        padding5.buildComponent(structureComponent, list, random);

        // Left / West

        for (int i = cornerClipping[3][0]; i < (heightInCellCount-1) - cornerClipping[2][0]; i++) {
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.minX - 11, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 3, Rotation.CLOCKWISE_90);
            list.add(wall);
            wall.buildComponent(structureComponent, list, random);

            ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), i, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 4, Rotation.CLOCKWISE_90);
            list.add(padding);
            padding.buildComponent(structureComponent, list, random);
        }

        ComponentNagaCourtyardWallPadder padding8 = new ComponentNagaCourtyardWallPadder(getFeatureType(), (heightInCellCount-1) - cornerClipping[2][0], boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + (((heightInCellCount-1) - cornerClipping[2][0]) * 12) + offset - 4, Rotation.CLOCKWISE_90);
        list.add(padding8);
        padding8.buildComponent(structureComponent, list, random);

        // Right / East

        for (int i = cornerClipping[0][0]; i < (heightInCellCount-1) - cornerClipping[1][0]; i++) {
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.maxX - 7, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 3, Rotation.CLOCKWISE_90);
            list.add(wall);
            wall.buildComponent(structureComponent, list, random);

            ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), i, boundingBox.maxX + 3, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 4, Rotation.CLOCKWISE_90);
            list.add(padding);
            padding.buildComponent(structureComponent, list, random);
        }

        ComponentNagaCourtyardWallPadder padding11 = new ComponentNagaCourtyardWallPadder(getFeatureType(), ((heightInCellCount-1) - cornerClipping[1][0]), boundingBox.maxX + 3, boundingBox.minY, boundingBox.minZ + (((heightInCellCount-1) - cornerClipping[1][0]) * 12) + offset - 4, Rotation.CLOCKWISE_90);
        list.add(padding11);
        padding11.buildComponent(structureComponent, list, random);
    }

    private static String getStringFromFacings(int directions) {
        switch (directions & 0b1111) {
            case 0b0010:
                return " ╷ ";
            case 0b0001:
                return " ╶─";
            case 0b1000:
                return " ╵ ";
            case 0b0100:
                return "─╴ ";
            case 0b1001:
                return " └─";
            case 0b1100:
                return "─┘ ";
            case 0b0110:
                return "─┐ ";
            case 0b0011:
                return " ┌─";
            case 0b1101:
                return "─┴─";
            case 0b1110:
                return "─┤ ";
            case 0b0111:
                return "─┬─";
            case 0b1011:
                return " ├─";
            case 0b1010:
                return " │ ";
            case 0b0101:
                return "───";
            case 0b1111:
                return "─┼─";
            default:
                return " • ";
        }
    }

    protected enum WallFacing {
        EAST (0b0001, 0b0100, 0b1110, 0b1011, Direction.EAST ,  1,  0),
        SOUTH(0b0010, 0b1000, 0b1101, 0b0111, Direction.SOUTH,  0,  1),
        WEST (0b0100, 0b0001, 0b1011, 0b1110, Direction.WEST , -1,  0),
        NORTH(0b1000, 0b0010, 0b0111, 0b1101, Direction.NORTH,  0, -1);

        private final int BYTE;
        private final int OPPOSITE;
        private final int INVERTED;
        private final int INVERTED_OPPOSITE;
        private final Direction enumFacing;
        private final int xOffset;
        private final int zOffset;

        WallFacing(int bite, int oppositeBite, int inverted, int invertedOpposite, Direction enumFacing, int xOffset, int zOffset) {
            this.BYTE = bite;
            this.OPPOSITE = oppositeBite;
            this.INVERTED = inverted;
            this.INVERTED_OPPOSITE = invertedOpposite;
            this.enumFacing = enumFacing;
            this.xOffset = xOffset;
            this.zOffset = zOffset;
        }

        protected boolean unpackAndTest(int directions) {
            return (this.BYTE & directions) == this.BYTE;
        }

        protected boolean unpackAndTestOpposite(int directions) {
            return (this.OPPOSITE & directions) == this.OPPOSITE;
        }

        public Direction getEnumFacing() {
            return enumFacing;
        }
    }

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		ListNBT mazeX = new ListNBT();

		for (int x = 0; x < widthInCellCount - 1; x++) {
			ListNBT mazeY = new ListNBT();

			for (int y = 0; y < heightInCellCount - 1; y++)
				mazeY.add(IntNBT.valueOf(maze[x][y]));

			mazeX.add(mazeY);
		}

		tagCompound.putInt("mazeWidth", widthInCellCount);
		tagCompound.putInt("mazeHeight", heightInCellCount);
		tagCompound.put("maze", mazeX);
	}
}
