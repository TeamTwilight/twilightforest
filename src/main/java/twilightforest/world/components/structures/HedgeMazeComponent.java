package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;


public class HedgeMazeComponent extends TFStructureComponentOld {

	private static final int MSIZE = 16;
	private static final int RADIUS = (MSIZE / 2 * 3) + 1;
	private static final int DIAMETER = 2 * RADIUS;
	private static final int FLOOR_LEVEL = 0;

	public HedgeMazeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFHedge.get(), nbt);

		this.boundingBox = BoundingBoxUtils.NBTToBoundingBox(nbt);
	}

	public HedgeMazeComponent(int i, int x, int y, int z) {
		super(TFStructurePieceTypes.TFHedge.get(), i, x, y, z);

		this.setOrientation(Direction.SOUTH);

		// the maze is 50 x 50 for now
		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -RADIUS, -3, -RADIUS, RADIUS * 2, 10, RADIUS * 2, Direction.SOUTH, false);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		TFMaze maze = new TFMaze(MSIZE, MSIZE, rand);

		maze.oddBias = 2;
		maze.torchBlockState = TFBlocks.FIREFLY.get().defaultBlockState();
		maze.wallBlockState = TFBlocks.HEDGE.get().defaultBlockState();
		maze.type = 4;
		maze.tall = 3;
		maze.roots = 3;

		// set the seed to a fixed value based on this maze's x and z
		maze.setSeed(world.getSeed() + (long) this.boundingBox.minX() * this.boundingBox.minZ());

		// just add grass below the maze for now
		// grass underneath
		for (int fx = 0; fx <= DIAMETER; fx++) {
			for (int fz = 0; fz <= DIAMETER; fz++) {
				placeBlock(world, Blocks.GRASS_BLOCK.defaultBlockState(), fx, FLOOR_LEVEL - 1, fz, sbb);
			}
		}

		BlockState northJacko = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.SOUTH);
		BlockState southJacko = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.NORTH);
		BlockState westJacko = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.EAST);
		BlockState eastJacko = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.WEST);

		// plunk down some jack-o-lanterns outside for decoration
		placeBlock(world, westJacko, 0, FLOOR_LEVEL, 24, sbb);
		placeBlock(world, westJacko, 0, FLOOR_LEVEL, 29, sbb);
		placeBlock(world, eastJacko, 50, FLOOR_LEVEL, 24, sbb);
		placeBlock(world, eastJacko, 50, FLOOR_LEVEL, 29, sbb);

		placeBlock(world, northJacko, 24, FLOOR_LEVEL, 0, sbb);
		placeBlock(world, northJacko, 29, FLOOR_LEVEL, 0, sbb);
		placeBlock(world, southJacko, 24, FLOOR_LEVEL, 50, sbb);
		placeBlock(world, southJacko, 29, FLOOR_LEVEL, 50, sbb);

		int nrooms = MSIZE / 3;
		int[] rcoords = new int[nrooms * 2];

		while (!maze.allCellsNonZero()) {
			maze.resetCells();
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
		}

		maze.add4Exits();

		maze.copyToStructure(world, manager, generator, 1, FLOOR_LEVEL, 1, this, sbb);

		decorate3x3Rooms(world, rcoords, sbb);
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

	private void decorate3x3Rooms(WorldGenLevel world, int[] rcoords, BoundingBox sbb) {
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
	private void decorate3x3Room(WorldGenLevel world, int x, int z, BoundingBox sbb) {
		// make a new RNG for this room!
		RandomSource roomRNG = RandomSource.create(world.getSeed() ^ x + z);

		// a few jack-o-lanterns
		roomJackO(world, roomRNG, x, z, 8, sbb);
		if (roomRNG.nextInt(4) == 0) {
			roomJackO(world, roomRNG, x, z, 8, sbb);
		}

		// all rooms should have 1 spawner
		roomSpawner(world, roomRNG, x, z, 8, sbb);

		// and 1-2 chests
		roomTreasure(world, roomRNG, x, z, 8, sbb, TFLootTables.HEDGE_MAZE);
		if (roomRNG.nextInt(4) == 0) {
			roomTreasure(world, roomRNG, x, z, 8, sbb, TFLootTables.HEDGE_CLOTH);
		}
	}

	/**
	 * Place a spawner within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomSpawner(WorldGenLevel world, RandomSource rand, int x, int z, int diameter, BoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		EntityType<?> mobID = switch (rand.nextInt(3)) {
			case 1 -> TFEntities.SWARM_SPIDER.get();
			case 2 -> TFEntities.HOSTILE_WOLF.get();
			default -> TFEntities.HEDGE_SPIDER.get();
		};

		setSpawner(world, rx, FLOOR_LEVEL, rz, sbb, mobID);
	}

	/**
	 * Place a treasure chest within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomTreasure(WorldGenLevel world, RandomSource rand, int x, int z, int diameter, BoundingBox sbb, ResourceKey<LootTable> table) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		int xDiff = x - rx;
		int zDiff = z - rz;

		BlockPos pos = new BlockPos(getWorldX(rx, rz), getWorldY(FLOOR_LEVEL), getWorldZ(rx, rz));
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != (Blocks.CHEST)) {
			Direction facing;
			if (Math.abs(xDiff) > Math.abs(zDiff)) facing = xDiff < 0 ? Direction.WEST : Direction.EAST;
			else facing = zDiff < 0 ? Direction.NORTH : Direction.SOUTH;
			TFLootTables.generateChest(world, pos, facing, false, table);
		}
	}

	/**
	 * Place a lit pumpkin lantern within diameter / 2 squares of the specified x and z coordinates
	 */
	private void roomJackO(WorldGenLevel world, RandomSource rand, int x, int z, int diameter, BoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - (diameter / 2);
		int rz = z + rand.nextInt(diameter) - (diameter / 2);

		placeBlock(world, Blocks.JACK_O_LANTERN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, Direction.from2DDataValue(rand.nextInt(4))),
			rx, FLOOR_LEVEL, rz, sbb);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);

		BoundingBoxUtils.boundingBoxToExistingNBT(this.boundingBox, tagCompound);
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BEARD_BOX;
	}
}
