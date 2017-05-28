package twilightforest.structures;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.MazestoneVariant;


public class ComponentTFHillMaze extends StructureTFComponent {

	private static final int FLOOR_LEVEL = 1;
	private int hillSize;
	
	public ComponentTFHillMaze(int i, int x, int y, int z, int hsize) {
		super(i);
		this.hillSize = hsize;
		
		this.setCoordBaseMode(EnumFacing.SOUTH);
		
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -getRadius(), 0, -getRadius(), getRadius() * 2, 5, getRadius() * 2, EnumFacing.SOUTH);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// clear the area
		fillWithBlocks(world, sbb, 0, 1, 0, getDiameter(), 3, getDiameter(), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		fillWithBlocks(world, sbb, 0, 0, 0, getDiameter(), 0, getDiameter(), TFBlocks.mazestone.getDefaultState(), TFBlocks.mazestone.getDefaultState(), false);
		fillWithBlocks(world, sbb, 0, 4, 0, getDiameter(), 4, getDiameter(), TFBlocks.mazestone.getDefaultState(), TFBlocks.mazestone.getDefaultState(), true);
	
		
		// make maze object
		TFMaze maze = new TFMaze(getMazeSize(), getMazeSize());
		
		maze.wallBlockState = TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE);
		maze.torchRarity = 0.05F;

		// set the seed to a fixed value based on this maze's x and z
		maze.setSeed(world.getSeed() + this.boundingBox.minX * this.boundingBox.minZ);

		
		int nrooms = getMazeSize() / 3;
		int rcoords[] = new int[nrooms * 2];

		// add rooms, trying to keep them separate from existing rooms
		for (int i = 0; i < nrooms; i++)
		{
			int rx, rz;
			do {
				rx = maze.rand.nextInt(getMazeSize() - 2) + 1;
				rz = maze.rand.nextInt(getMazeSize() - 2) + 1;
			} while(isNearRoom(rx, rz, rcoords));

			maze.carveRoom1(rx, rz);

			rcoords[i * 2] = rx;
			rcoords[i * 2 + 1] = rz;
		}
		
		// make actual maze
		maze.generateRecursiveBacktracker(0, 0);
		
		maze.add4Exits();
		
		maze.copyToStructure(world, 0, 1, 0, this, sbb);
		
		
		decorate3x3Rooms(world, rcoords, sbb);


		return true;

	}
	
	public int getMazeSize() {
		return hillSize == 1 ? 11 : hillSize == 2 ? 19 : 27;
	}
	
	public int getRadius() {
		return getMazeSize() * 2;
	}
	
	public int getDiameter() {
		return getMazeSize() * 4;
	}
	
	/**
	 * @return true if the specified dx and dz are within 3 of a room specified in rcoords
	 */
	protected boolean isNearRoom(int dx, int dz, int[] rcoords) {
		// if proposed coordinates are covering the origin, return true to stop the room from causing the maze to fail
		if (dx == 1 && dz == 1) {
			return true;
		}
		
		for (int i = 0; i < rcoords.length / 2; i++)
		{
			int rx = rcoords[i * 2];
			int rz = rcoords[i * 2 + 1];
			
			if (rx == 0 && rz == 0) {
				continue;
			}
			
			if (Math.abs(dx - rx) < 3 && Math.abs(dz - rz) < 3) {
				return true;
			}
		}
		return false;
	}

	private void decorate3x3Rooms(World world, int[] rcoords, StructureBoundingBox sbb)
	{
		for (int i = 0; i < rcoords.length / 2; i++)
		{
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];
			
			// MAGIC NUMBERS!!! convert the maze coordinates into coordinates for our structure
			dx = dx * 4 + 2;
			dz = dz * 4 + 2;

			decorate3x3Room(world, dx, dz, sbb);
		}
	}

	/**
	 * Decorates a room in the maze.  Makes assumptions that the room is 3x3 cells and thus 11x11 blocks large.
	 */
	private void decorate3x3Room(World world, int x, int z, StructureBoundingBox sbb)
	{
		// make a new RNG for this room!
		Random roomRNG = new Random(world.getSeed() ^ x + z);
		
		// all rooms should have 1 spawner
		roomSpawner(world, roomRNG, x, z, 8, sbb);

		// and 1-2 chests
		roomTreasure(world, roomRNG, x, z, 8, sbb);
		if (roomRNG.nextInt(4) == 0) {
			roomTreasure(world, roomRNG, x, z, 8, sbb);
		}
	}

	/**
	 * Place a spawner within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomSpawner(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		String mobID; 

		switch (rand.nextInt(3)) {
		case 1 :
			mobID = "Skeleton";
			break;
		case 2 :
			mobID = "Zombie";
			break;
		case 0 : 
		default:
			mobID = "Spider";
		}

		setSpawner(world, rx, FLOOR_LEVEL, rz, sbb, mobID);
	}

	/**
	 * Place a treasure chest within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomTreasure(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		placeTreasureAtCurrentPosition(world, rand, rx, FLOOR_LEVEL, rz, TFTreasure.labyrinth_room, sbb);
	}
}
