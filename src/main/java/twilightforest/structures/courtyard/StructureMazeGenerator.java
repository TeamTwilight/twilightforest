package twilightforest.structures.courtyard;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
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

    StructureMazeGenerator() {
        super();
    }

    StructureMazeGenerator(TFFeature feature, Random rand, int i, int widthInCellCount, int heightInCellCount) {
        super(feature, i);
        this.widthInCellCount = widthInCellCount;
        this.heightInCellCount = heightInCellCount;
        this.maze = new int[widthInCellCount-1][heightInCellCount-1];
        generateMaze(this.maze, this.cornerClipping, rand, this.widthInCellCount, this.heightInCellCount, 3);
    }

    // Actually assemble maze
    @SuppressWarnings("ConstantConditions")
    @Override
    public void buildComponent(StructureComponent structureComponent, List<StructureComponent> list, Random random) {
        super.buildComponent(structureComponent, list, random);
        final int offset = 6;

        final Rotation[] rotations = Rotation.values();

        for (int x = 0; x < widthInCellCount - 1; x++) {
            for (int y = 0; y < heightInCellCount - 1; y++) {
                // -------- HEDGE
                if ((maze[x][y] & 0b10000) == 0b10000) continue;

                StructureTFComponent structure;

                int rotation = 0;

                int xBB = boundingBox.minX + (x * 12) + offset;
                int yBB = boundingBox.minY + 1;
                int zBB = boundingBox.minZ + (y * 12) + offset;

                switch (maze[x][y] & 0b1111) { // These are inconsistent because I was stupid with the structures I saved to .nbt
                    case 0b0010:    // FACE SOUTH
                        rotation++; // rotate 270
                    case 0b0001:    // FACE EAST
                        rotation++; // rotate 180
                    case 0b1000:    // FACE NORTH
                        rotation++; // rotate 90
                    case 0b0100:    // FACE WEST
                        final Rotation rotationCap = rotations[rotation];

                        structure = new ComponentNagaCourtyardHedgeCap(getFeatureType(), (x * widthInCellCount) + y, xBB, yBB, zBB, rotationCap);
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
                        structure = new ComponentNagaCourtyardHedgeIntersection(getFeatureType(), maze[x][y], xBB, yBB, zBB);
                        break;
                    default:
                        structure = new ComponentNagaCourtyardTerrace(getFeatureType(), maze[x][y], xBB-6, yBB-3, zBB-6, Rotation.NONE);
                        break;
                }

                list.add(structure);
                structure.buildComponent(structureComponent, list, random);

                // -------- Hedge Connectors

                xBB = boundingBox.minX + (x * 12) + offset;
                zBB = boundingBox.minZ + (y * 12) + offset;

                boolean hasNoTerrace = (maze[x][y] & 0b1111) != 0;

                boolean westHasNoTerraceOrIsSafe  = x == 0 || (maze[x-1][y] & 0b1111) != 0 || (maze[x-1][y] & 0b10000) == 0b10000;
                boolean northHasNoTerraceOrIsSafe = y == 0 || (maze[x][y-1] & 0b1111) != 0 || (maze[x][y-1] & 0b10000) == 0b10000;
                boolean eastHasNoTerraceOrIsSafe  = x == widthInCellCount -2 || (maze[x+1][y] & 0b10000) == 0b10000;
                boolean southHasNoTerraceOrIsSafe = y == heightInCellCount-2 || (maze[x][y+1] & 0b10000) == 0b10000;

                boolean westNorthHasNoTerraceOrIsSafe = (x == 0 || y == 0 || maze[x - 1][y - 1] != 0);
                boolean westSouthHasNoTerraceOrIsSafe = (x == 0 || y >= heightInCellCount-2 || maze[x - 1][y + 1] != 0);
                boolean eastNorthHasNoTerraceOrIsSafe = (x >= widthInCellCount -2 || y == 0 || maze[x + 1][y - 1] != 0);
                boolean eastSouthHasNoTerraceOrIsSafe = (x >= widthInCellCount -2 || y >= heightInCellCount-2 || maze[x + 1][y + 1] != 0);

                if (hasNoTerrace) {
                    ComponentNagaCourtyardPath path = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 1, yBB - 1, zBB - 1);
                    list.add(path);
                    path.buildComponent(structureComponent, list, random);
                }

                if (WallFacing.WEST.unpackAndTest(maze[x][y])) {
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

                if (WallFacing.NORTH.unpackAndTest(maze[x][y])) {
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

                if ((x >= widthInCellCount - 2 || (maze[x+1][y] & 0b10000) == 0b10000) && WallFacing.EAST.unpackAndTest(maze[x][y])) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 5, yBB, zBB, Rotation.NONE);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB + 6, yBB, zBB, Rotation.NONE);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }

                if ((y >= heightInCellCount - 2 || (maze[x][y+1] & 0b10000) == 0b10000) && WallFacing.SOUTH.unpackAndTest(maze[x][y])) {
                    ComponentNagaCourtyardHedgePadder padding = new ComponentNagaCourtyardHedgePadder(getFeatureType(), maze[x][y], xBB + 4, yBB, zBB + 5, Rotation.CLOCKWISE_90);
                    list.add(padding);
                    padding.buildComponent(structureComponent, list, random);

                    ComponentNagaCourtyardHedgeLine structureLine = new ComponentNagaCourtyardHedgeLine(getFeatureType(), maze[x][y], xBB, yBB, zBB + 6, Rotation.CLOCKWISE_90);
                    list.add(structureLine);
                    structureLine.buildComponent(structureComponent, list, random);
                }//*/

                // -------- PATHS - cardinal

                if (hasNoTerrace && westHasNoTerraceOrIsSafe) {
                    ComponentNagaCourtyardPath path2 = new ComponentNagaCourtyardPath(getFeatureType(), maze[x][y], xBB - 7, yBB - 1, zBB - 1);
                    list.add(path2);
                    path2.buildComponent(structureComponent, list, random);
                }

                if (hasNoTerrace && northHasNoTerraceOrIsSafe) {
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

        // -------- WALLS

        for (Diagonals diagonal : Diagonals.values()) {
            // Walls at corner notches going with X Axis, crossing Z Axis

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

            // Walls at corner notches going with Z Axis, crossing X Axis

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

                ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i*2, xBoundZ, boundingBox.minY, zBound, Rotation.CLOCKWISE_90);
                list.add(wall);
                wall.buildComponent(structureComponent, list, random);

                ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), (i*2)+1, xBoundZ, boundingBox.minY, zBound + zPadOffset, Rotation.CLOCKWISE_90);
                list.add(padding);
                padding.buildComponent(structureComponent, list, random);
            }

            Rotation rotation = rotations[diagonal.ordinal() % rotations.length];
        } //*/

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
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 3, Rotation.CLOCKWISE_90);
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
            ComponentNagaCourtyardWall wall = new ComponentNagaCourtyardWall(getFeatureType(), i, boundingBox.maxX + 3, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 3, Rotation.CLOCKWISE_90);
            list.add(wall);
            wall.buildComponent(structureComponent, list, random);

            ComponentNagaCourtyardWallPadder padding = new ComponentNagaCourtyardWallPadder(getFeatureType(), i, boundingBox.maxX + 3, boundingBox.minY, boundingBox.minZ + (i * 12) + offset - 4, Rotation.CLOCKWISE_90);
            list.add(padding);
            padding.buildComponent(structureComponent, list, random);
        }

        ComponentNagaCourtyardWallPadder padding11 = new ComponentNagaCourtyardWallPadder(getFeatureType(), ((heightInCellCount-1) - cornerClipping[1][0]), boundingBox.maxX + 3, boundingBox.minY, boundingBox.minZ + (((heightInCellCount-1) - cornerClipping[1][0]) * 12) + offset - 4, Rotation.CLOCKWISE_90);
        list.add(padding11);
        padding11.buildComponent(structureComponent, list, random);

        // WALL CORNERS

        // TOP RIGHT WALL CORNERS

        ComponentNagaCourtyardWallCorner wallCorner1 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.TOP_RIGHT.operationX.convert(cornerClipping[0][1], widthInCellCount  - 1) * 12) + 3,
                boundingBox.minY,
                boundingBox.minZ - 3,
                Rotation.NONE);
        list.add(wallCorner1);
        wallCorner1.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCorner wallCorner2 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.maxX - 1,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.TOP_RIGHT.operationY.convert(cornerClipping[0][0], heightInCellCount - 1) * 12) - 3,
                Rotation.NONE);
        list.add(wallCorner2);
        wallCorner2.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCornerAlt wallCorner3 = new ComponentNagaCourtyardWallCornerAlt(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.TOP_RIGHT.operationX.convert(cornerClipping[0][1], widthInCellCount  - 1) * 12) + 5,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.TOP_RIGHT.operationY.convert(cornerClipping[0][0], heightInCellCount - 1) * 12) - 9,
                Rotation.NONE);
        list.add(wallCorner3);
        wallCorner3.buildComponent(structureComponent, list, random);

        // BOTTOM RIGHT WALL CORNERS

        ComponentNagaCourtyardWallCorner wallCorner4 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.BOTTOM_RIGHT.operationX.convert(cornerClipping[1][1], widthInCellCount  - 1) * 12) + 7,
                boundingBox.minY,
                boundingBox.maxZ - 1,
                Rotation.CLOCKWISE_90);
        list.add(wallCorner4);
        wallCorner4.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCorner wallCorner5 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.maxX + 3,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.BOTTOM_RIGHT.operationY.convert(cornerClipping[1][0], heightInCellCount - 1) * 12) + 3,
                Rotation.CLOCKWISE_90);
        list.add(wallCorner5);
        wallCorner5.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCornerAlt wallCorner6 = new ComponentNagaCourtyardWallCornerAlt(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.BOTTOM_RIGHT.operationX.convert(cornerClipping[1][1], widthInCellCount  - 1) * 12) + 13,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.BOTTOM_RIGHT.operationY.convert(cornerClipping[1][0], heightInCellCount - 1) * 12) + 5,
                Rotation.CLOCKWISE_90);
        list.add(wallCorner6);
        wallCorner6.buildComponent(structureComponent, list, random);

        // BOTTOM LEFT WALL CORNERS

        ComponentNagaCourtyardWallCorner wallCorner7 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.BOTTOM_LEFT.operationX.convert(cornerClipping[2][1], widthInCellCount  - 1) * 12) + 1,
                boundingBox.minY,
                boundingBox.maxZ + 3,
                Rotation.CLOCKWISE_180);
        list.add(wallCorner7);
        wallCorner7.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCorner wallCorner8 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX + 1,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.BOTTOM_LEFT.operationY.convert(cornerClipping[2][0], heightInCellCount - 1) * 12) + 7,
                Rotation.CLOCKWISE_180);
        list.add(wallCorner8);
        wallCorner8.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCornerAlt wallCorner9 = new ComponentNagaCourtyardWallCornerAlt(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.BOTTOM_LEFT.operationX.convert(cornerClipping[2][1], widthInCellCount  - 1) * 12) - 1,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.BOTTOM_LEFT.operationY.convert(cornerClipping[2][0], heightInCellCount - 1) * 12) + 13,
                Rotation.CLOCKWISE_180);
        list.add(wallCorner9);
        wallCorner9.buildComponent(structureComponent, list, random);

        // TOP LEFT WALL CORNERS

        ComponentNagaCourtyardWallCorner wallCorner10 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.TOP_LEFT.operationX.convert(cornerClipping[3][1], widthInCellCount  - 1) * 12) - 3,
                boundingBox.minY,
                boundingBox.minZ + 1,
                Rotation.COUNTERCLOCKWISE_90);
        list.add(wallCorner10);
        wallCorner10.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCorner wallCorner11 = new ComponentNagaCourtyardWallCorner(getFeatureType(), 0,
                boundingBox.minX - 3,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.TOP_LEFT.operationY.convert(cornerClipping[3][0], heightInCellCount - 1) * 12) + 1,
                Rotation.COUNTERCLOCKWISE_90);
        list.add(wallCorner11);
        wallCorner11.buildComponent(structureComponent, list, random);

        ComponentNagaCourtyardWallCornerAlt wallCorner12 = new ComponentNagaCourtyardWallCornerAlt(getFeatureType(), 0,
                boundingBox.minX + (Diagonals.TOP_LEFT.operationX.convert(cornerClipping[3][1], widthInCellCount  - 1) * 12) - 9,
                boundingBox.minY,
                boundingBox.minZ + (Diagonals.TOP_LEFT.operationY.convert(cornerClipping[3][0], heightInCellCount - 1) * 12) - 1,
                Rotation.COUNTERCLOCKWISE_90);
        list.add(wallCorner12);
        wallCorner12.buildComponent(structureComponent, list, random); //*/
    }

    private static void generateMaze(int[][] maze, int[][] cornerClippings, Random random, int widthInCellCount, int heightInCellCount, @SuppressWarnings("SameParameterValue") int maximumClipping) {
        // Trying to keep this optimized for speed I guess

        // Generates a connection map for the walls. It modifies the two-dimensional int array, inserting packed ints.
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

        StringBuilder chartX2 = new StringBuilder();

        for (int i = 0; i < heightInCellCount-1; i++) {
            StringBuilder chartY2 = new StringBuilder("\n");

            for (int j = 0; j < widthInCellCount-1; j++) {
                int value = maze[j][i];

                if ((value & 0b10000) != 0b10000) chartY2.append(getStringFromFacings(value & 0b1111));
                else chartY2.append("   ");
            }

            chartX2.append(chartY2);
        }

        System.out.println(chartX2);

        final int[][] mazeLocal = maze.clone();

        for (int y = 0; y < heightInCellCount-1; y++) {
            for (int x = 0; x < widthInCellCount-1; x++) {
                // Did we pick west and will we not get an AIOOBException accessing array
                if (rotations[x][y] == WallFacing.WEST && x > 0) {
                    // If neighbor does not connect to west, connect it to east
                    if (!rotations[x][y].unpackAndTest(maze[x-1][y])) maze[x-1][y] |= rotations[x][y].OPPOSITE;
                    else { // else we cut the connection
                        // remove connection for the maze part we're looking at
                        maze[x][y] &= rotations[x][y].INVERTED;
                        // remove connection for the adjacent maze part
                        maze[x-1][y] &= rotations[x-1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.NORTH && y > 0 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y-1])) maze[x][y-1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y-1] &= rotations[x][y-1].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.EAST && x < widthInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x+1][y])) maze[x+1][y] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x+1][y] &= rotations[x+1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.SOUTH && y < heightInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y+1])) maze[x][y+1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y+1] &= rotations[x][y+1].INVERTED_OPPOSITE;
                    }
                }
            }
        }//*/

        for (int x = 1; x < widthInCellCount-1; x++) {
            for (int y = 1; y < heightInCellCount-1; y++) {
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

        /*for (int x = 0; x < widthInCellCount-1; x++) {
            for (int y = 0; y < heightInCellCount-1; y++) {
                // Did we pick west and will we not get an AOOBE accessing array
                if (rotations[x][y] == WallFacing.WEST && x > 0) {
                    // If neighbor does not connect to west, connect it to east
                    if (!rotations[x][y].unpackAndTest(maze[x-1][y])) maze[x-1][y] |= rotations[x][y].OPPOSITE;
                    else { // else we cut the connection
                        // remove connection for the maze part we're looking at
                        maze[x][y] &= rotations[x][y].INVERTED;
                        // remove connection for the adjacent maze part
                        maze[x-1][y] &= rotations[x-1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.NORTH && y > 0 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y-1])) maze[x][y-1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y-1] &= rotations[x][y-1].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.EAST && x < widthInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x+1][y])) maze[x+1][y] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x+1][y] &= rotations[x+1][y].INVERTED_OPPOSITE;
                    }
                }
                if (rotations[x][y] == WallFacing.SOUTH && y < heightInCellCount-2 ) {
                    if (!rotations[x][y].unpackAndTest(maze[x][y+1])) maze[x][y+1] |= rotations[x][y].OPPOSITE;
                    else {
                        maze[x][y] &= rotations[x][y].INVERTED;
                        maze[x][y+1] &= rotations[x][y+1].INVERTED_OPPOSITE;
                    }
                }
            }
        }*/

        for (Diagonals diagonals : Diagonals.values()) {
            cornerClippings[diagonals.ordinal()][0] = random.nextInt(maximumClipping)+1;
            cornerClippings[diagonals.ordinal()][1] = random.nextInt(maximumClipping)+1;

            //maze[diagonals.operationY.convert(cornerClippings[diagonals.ordinal()][0], heightInCellCount)];

            for (int y = 0; y < cornerClippings[diagonals.ordinal()][0]; y++) {
                for (int x = 0; x < cornerClippings[diagonals.ordinal()][1]; x++) {
                    maze
                            [diagonals.operationX.convert(x, widthInCellCount-2)]
                            [diagonals.operationY.convert(y, heightInCellCount-2)]
                            |= 0b10000;
                }
            }
        }

        StringBuilder chartX = new StringBuilder();
        StringBuilder debugX = new StringBuilder();

        for (int i = 0; i < heightInCellCount-1; i++) {
            StringBuilder chartY = new StringBuilder("\n");
            StringBuilder debugY = new StringBuilder("\n");

            for (int j = 0; j < widthInCellCount-1; j++) {
                int value = maze[j][i];

                if ((value & 0b10000) != 0b10000) chartY.append(getStringFromFacings(value & 0b1111));
                else chartY.append("   ");

                debugY.append(value >= 0b10000 ? "      " : value > 0b111 ? " 0" : value > 0b11 ? " 00" : value > 0b1 ? " 000" : " 0000");
                if ((value & 0b10000) != 0b10000) debugY.append(Integer.toBinaryString(value));
            }

            chartX.append(chartY);
            debugX.append(debugY);
        }

        System.out.println(chartX);
        System.out.println(debugX);
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
        EAST (0b0001, 0b0100, 0b1110, 0b1011, EnumFacing.EAST ),
        SOUTH(0b0010, 0b1000, 0b1101, 0b0111, EnumFacing.SOUTH),
        WEST (0b0100, 0b0001, 0b1011, 0b1110, EnumFacing.WEST ),
        NORTH(0b1000, 0b0010, 0b0111, 0b1101, EnumFacing.NORTH);

        private final int BYTE;
        private final int OPPOSITE;
        private final int INVERTED;
        private final int INVERTED_OPPOSITE;
        private final EnumFacing enumFacing;

        WallFacing(int bite, int oppositeBite, int inverted, int invertedOpposite, EnumFacing enumFacing) {
            this.BYTE = bite;
            this.OPPOSITE = oppositeBite;
            this.INVERTED = inverted;
            this.INVERTED_OPPOSITE = invertedOpposite;
            this.enumFacing = enumFacing;
        }

        protected boolean unpackAndTest(int directions) {
            return (this.BYTE & directions) == this.BYTE;
        }

        protected boolean unpackAndTestOpposite(int directions) {
            return (this.OPPOSITE & directions) == this.OPPOSITE;
        }

        public EnumFacing getEnumFacing() {
            return enumFacing;
        }
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);

        NBTTagList mazeX = new NBTTagList();

        for (int x = 0; x < widthInCellCount-1; x++) {
            NBTTagList mazeY = new NBTTagList();

            for (int y = 0; y < heightInCellCount-1; y++) mazeY.appendTag(new NBTTagInt(maze[x][y]));

            mazeX.appendTag(mazeY);
        }

        tagCompound.setInteger("mazeWidth", widthInCellCount);
        tagCompound.setInteger("mazeHeight", heightInCellCount);
        tagCompound.setTag("maze", mazeX);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
        super.readStructureFromNBT(tagCompound, templateManager);

        this.widthInCellCount = tagCompound.getInteger("mazeWidth");
        this.heightInCellCount = tagCompound.getInteger("mazeHeight");

        maze = new int[this.widthInCellCount-1][this.heightInCellCount-1];

        NBTTagList mazeX = tagCompound.getTagList("maze", 9);

        for (int x = 0; x < widthInCellCount-1; x++) {
            NBTBase mazeY = mazeX.get(x);

            if (mazeY instanceof NBTTagList)
                for (int y = 0; y < heightInCellCount - 1; y++) maze[x][y] = ((NBTTagList) mazeY).getIntAt(y);
        }
    }
}
