package twilightforest.structures;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFHedgeSpider;
import twilightforest.entity.EntityTFHostileWolf;
import twilightforest.entity.EntityTFSwarmSpider;

import java.util.Random;


public class ComponentTFHedgeMaze extends StructureTFComponentOld {

	private static final int MSIZE = 16;
	private static final int RADIUS = (MSIZE / 2 * 3) + 1;
	private static final int DIAMETER = 2 * RADIUS;
	private static final int FLOOR_LEVEL = 3;

	public ComponentTFHedgeMaze() {
		super();
	}

	public ComponentTFHedgeMaze(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, i);

		this.setCoordBaseMode(EnumFacing.SOUTH);

		// the maze is 50 x 50 for now
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -RADIUS, -3, -RADIUS, RADIUS * 2, 10, RADIUS * 2, EnumFacing.SOUTH);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		TFMaze maze = new TFMaze(MSIZE, MSIZE);

		maze.oddBias = 2;
		maze.torchBlockState = TFBlocks.firefly.getDefaultState();
		maze.wallBlockState = TFBlocks.hedge.getDefaultState();
		maze.type = 4;
		maze.tall = 3;
		maze.roots = 3;

		// set the seed to a fixed value based on this maze's x and z
		maze.setSeed(world.getSeed() + this.boundingBox.minX * this.boundingBox.minZ);

		// just add grass below the maze for now
		// grass underneath
		for (int fx = 0; fx <= DIAMETER; fx++) {
			for (int fz = 0; fz <= DIAMETER; fz++) {
				setBlockState(world, Blocks.GRASS.getDefaultState(), fx, FLOOR_LEVEL - 1, fz, sbb);
			}
		}

		IBlockState northJacko = Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.NORTH);
		IBlockState southJacko = Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.SOUTH);
		IBlockState westJacko = Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.WEST);
		IBlockState eastJacko = Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.EAST);

		// plunk down some jack-o-lanterns outside for decoration
		setBlockState(world, westJacko, 0, FLOOR_LEVEL, 24, sbb);
		setBlockState(world, westJacko, 0, FLOOR_LEVEL, 29, sbb);
		setBlockState(world, eastJacko, 50, FLOOR_LEVEL, 24, sbb);
		setBlockState(world, eastJacko, 50, FLOOR_LEVEL, 29, sbb);

		setBlockState(world, northJacko, 24, FLOOR_LEVEL, 0, sbb);
		setBlockState(world, northJacko, 29, FLOOR_LEVEL, 0, sbb);
		setBlockState(world, southJacko, 24, FLOOR_LEVEL, 50, sbb);
		setBlockState(world, southJacko, 29, FLOOR_LEVEL, 50, sbb);


		int nrooms = MSIZE / 3;
		int rcoords[] = new int[nrooms * 2];

		for (int i = 0; i < nrooms; i++) {
			int rx, rz;
			do {
				rx = maze.rand.nextInt(MSIZE - 2) + 1;
				rz = maze.rand.nextInt(MSIZE - 2) + 1;
			} while (isNearRoom(rx, rz, rcoords));

			maze.carveRoom1(rx, rz);

			rcoords[i * 2] = rx;
			rcoords[i * 2 + 1] = rz;
		}

		maze.generateRecursiveBacktracker(0, 0);

		maze.add4Exits();

		maze.copyToStructure(world, 1, FLOOR_LEVEL, 1, this, sbb);

		decorate3x3Rooms(world, rcoords, sbb);


		return true;
	}

	/**
	 * @return true if the specified dx and dz are within 3 of a room specified in rcoords
	 */
	private boolean isNearRoom(int dx, int dz, int[] rcoords) {
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

			if (Math.abs(dx - rx) < 3 && Math.abs(dz - rz) < 3) {
				return true;
			}
		}
		return false;
	}

	private void decorate3x3Rooms(World world, int[] rcoords, StructureBoundingBox sbb) {
		for (int i = 0; i < rcoords.length / 2; i++) {
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];

			// MAGIC NUMBERS!!! convert the maze coordinates into coordinates for our structure
			dx = dx * 3 + 3;
			dz = dz * 3 + 3;

			decorate3x3Room(world, dx, dz, sbb);
		}
	}

	/**
	 * Decorates a room in the maze.  Makes assumptions that the room is 3x3 cells and thus 11x11 blocks large.
	 */
	private void decorate3x3Room(World world, int x, int z, StructureBoundingBox sbb) {
		// make a new RNG for this room!
		Random roomRNG = new Random(world.getSeed() ^ x + z);

		// a few jack-o-lanterns
		roomJackO(world, roomRNG, x, z, 8, sbb);
		if (roomRNG.nextInt(4) == 0) {
			roomJackO(world, roomRNG, x, z, 8, sbb);
		}

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

		ResourceLocation mobID;

		switch (rand.nextInt(3)) {
			case 1:
				mobID = EntityList.getKey(EntityTFSwarmSpider.class);
				break;
			case 2:
				mobID = EntityList.getKey(EntityTFHostileWolf.class);
				break;
			case 0:
			default:
				mobID = EntityList.getKey(EntityTFHedgeSpider.class);
		}

		setSpawner(world, rx, FLOOR_LEVEL, rz, sbb, mobID);
	}

	/**
	 * Place a treasure chest within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomTreasure(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		placeTreasureAtCurrentPosition(world, rand, rx, FLOOR_LEVEL, rz, TFTreasure.hedgemaze, sbb);
	}


	/**
	 * Place a lit pumpkin lantern within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomJackO(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		setBlockState(world, Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.getHorizontal(rand.nextInt(4))),
				rx, FLOOR_LEVEL, rz, sbb);
	}


}
