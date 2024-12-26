package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFConfiguredFeatures;

import java.util.stream.IntStream;

/**
 * This is a maze of cells and walls.
 * <p>
 * The cells are at odd numbered x and y values, and the walls are at even numbered ones.  This does make the storage slightly inefficient, but oh wells.
 *
 * @author Ben
 */
public class TFMaze implements Cloneable {

	public final int width; // cells wide (x)
	public final int depth; // cells deep (z)

	public int oddBias; // corridor thickness, default 3
	public final int evenBias; // wall thickness here.  NYI

	public int tall; // wall blocks tall
	public int head;// blocks placed above the maze
	public int roots;// blocks placed under the maze (used for hedge mazes)

	public int type; // 1-3 = various sizes hollow hills

	public StructurePiece.BlockSelector wallBlocks;

	public BlockState wallBlockState;

	public BlockState headBlockState;

	public BlockState rootBlockState;

	public BlockState pillarBlockState;

	public BlockState doorBlockState;
	public float doorRarity;

	public BlockState torchBlockState;
	public float torchRarity;

	protected final int rawWidth;
	protected final int rawDepth;
	protected final int[] storage;

	public static final int OUT_OF_BOUNDS = Integer.MIN_VALUE;
	public static final int OOB = OUT_OF_BOUNDS;
	public static final int ROOM = 5;
	public static final int DOOR = 6;

	public RandomSource rand;

	public TFMaze(int cellsWidth, int cellsDepth, RandomSource random) {
		// default values
		oddBias = 3;
		evenBias = 1;
		tall = 3;
		head = 0;
		roots = 0;
		wallBlockState = TFBlocks.CUT_MAZESTONE.get().defaultBlockState();
		rootBlockState = TFBlocks.MAZESTONE.get().defaultBlockState();
		torchBlockState = Blocks.TORCH.defaultBlockState();
		pillarBlockState = null;

		torchRarity = 0.75F;
		doorRarity = 0F;

		this.width = cellsWidth;
		this.depth = cellsDepth;

		this.rawWidth = width * 2 + 1;
		this.rawDepth = depth * 2 + 1;
		storage = new int[rawWidth * rawDepth];

		this.rand = random;
	}

	/**
	 * Gets the value from a cell in the maze
	 */
	public int getCell(int x, int z) {
		return getRaw(x * 2 + 1, z * 2 + 1);
	}

	/**
	 * Puts a value into a cell in the maze
	 */
	private void putCell(int x, int z, int value) {
		putRaw(x * 2 + 1, z * 2 + 1, value);
	}

	/**
	 * Returns true if the specified cell equals the specified value
	 */
	private boolean cellEquals(int x, int z, int value) {
		return getCell(x, z) == value;
	}

	/**
	 * Gets the wall value, or OUT_OF_BOUNDS if the area is out of bounds or the coordinates are not orthogonally adjacent.
	 */
	private int getWall(int sx, int sz, int dx, int dz) {
		if (dx == sx + 1 && dz == sz) {
			return getRaw(sx * 2 + 2, sz * 2 + 1);
		}
		if (dx == sx - 1 && dz == sz) {
			return getRaw(sx * 2, sz * 2 + 1);
		}
		if (dx == sx && dz == sz + 1) {
			return getRaw(sx * 2 + 1, sz * 2 + 2);
		}
		if (dx == sx && dz == sz - 1) {
			return getRaw(sx * 2 + 1, sz * 2);
		}

		TwilightForestMod.LOGGER.info("Wall check out of bounds; s = {}, {}; d = {}, {}", sx, sz, dx, dz);

		return OUT_OF_BOUNDS;
	}

	public void putWall(int sx, int sz, int dx, int dz, int value) {
		if (dx == sx + 1 && dz == sz) {
			putRaw(sx * 2 + 2, sz * 2 + 1, value);
		}
		if (dx == sx - 1 && dz == sz) {
			putRaw(sx * 2, sz * 2 + 1, value);
		}
		if (dx == sx && dz == sz + 1) {
			putRaw(sx * 2 + 1, sz * 2 + 2, value);
		}
		if (dx == sx && dz == sz - 1) {
			putRaw(sx * 2 + 1, sz * 2, value);
		}
	}

	/**
	 * Returns true if there is a wall there
	 */
	public boolean isWall(int sx, int sz, int dx, int dz) {
		return getWall(sx, sz, dx, dz) == 0;
	}

	/**
	 * Puts a value into the raw storage.
	 */
	public void putRaw(int rawx, int rawz, int value) {
		if (rawx >= 0 && rawx < rawWidth && rawz >= 0 && rawz < rawDepth) {
			storage[rawz * rawWidth + rawx] = value;
		}
	}

	/**
	 * Gets a value from raw storage
	 */
	private int getRaw(int rawx, int rawz) {
		if (rawx < 0 || rawx >= rawWidth || rawz < 0 || rawz >= rawDepth) {
			return OUT_OF_BOUNDS;
		} else {
			return storage[rawz * rawWidth + rawx];
		}
	}

	public boolean allCellsNonZero() {
		return IntStream.range(0, width)
			.allMatch(x -> IntStream.range(0, depth)
				.allMatch(z -> getCell(x, z) != 0));
	}


	public void resetCells() {
		IntStream.range(0, width)
			.forEach(x -> IntStream.range(0, depth)
				.forEach(z -> putCell(x, z, 0)));
	}

	/**
	 * Sets the random seed to a specific value
	 */
	public void setSeed(long newSeed) {
		rand.setSeed(newSeed);
	}

	/**
	 * Copy the maze into a StructureTFComponentOld
	 */
	public void copyToStructure(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, int dx, int dy, int dz, TFStructureComponentOld component, BoundingBox sbb) {
		for (int x = 0; x < rawWidth; x++) {
			for (int z = 0; z < rawDepth; z++) {
				// only draw walls.  if the data is 0 the there's a wall
				if (getRaw(x, z) == 0) {
					int mdx = dx + (x / 2 * (evenBias + oddBias));
					int mdz = dz + (z / 2 * (evenBias + oddBias));

					if (evenBias > 1) {
						mdx--;
						mdz--;
					}

					if (isEven(x) && isEven(z)) {
						if (type == 4 && shouldTree(x, z)) {
							// occasionally make a tree
							putCanopyTree(world, generator, mdx, dy, mdz, component, sbb);
						} else {
							// make a block!
							for (int even = 0; even < evenBias; even++) {
								for (int even2 = 0; even2 < evenBias; even2++) {
									for (int y = 0; y < head; y++) {
										putHeadBlock(world, mdx + even, dy + tall + y, mdz + even2, component, sbb);
									}
									for (int y = 0; y < tall; y++) {
										if (shouldPillar(x, z)) {
											putPillarBlock(world, mdx + even, dy + y, mdz + even2, component, sbb);
										} else {
											putWallBlock(world, mdx + even, dy + y, mdz + even2, component, sbb);
										}
									}
									for (int y = 1; y <= roots; y++) {
										putRootBlock(world, mdx + even, dy - y, mdz + even2, component, sbb);
									}
								}
							}
						}
					}
					if (isEven(x) && !isEven(z)) {
						// make a | vertical | wall!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								makeWallThing(world, dy, component, sbb, mdx, mdz, even, odd);
							}
						}
					}
					if (!isEven(x) && isEven(z)) {
						// make a - horizontal - wall!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								makeWallThing(world, dy, component, sbb, mdx, mdz, odd, even);
							}
						}
					}
				} else if (getRaw(x, z) == DOOR) {
					int mdx = dx + (x / 2 * (evenBias + oddBias));
					int mdz = dz + (z / 2 * (evenBias + oddBias));

					if (evenBias > 1) {
						mdx--;
						mdz--;
					}

					if (isEven(x) && !isEven(z)) {
						// make a | vertical | door!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								for (int y = 0; y < head; y++) {
									putHeadBlock(world, mdx + even, dy + tall + y, mdz + odd, component, sbb);
								}
								for (int y = 0; y < tall; y++) {
									putDoorBlock(world, mdx + even, dy + y, mdz + odd, component, sbb);
								}
								for (int y = 1; y <= roots; y++) {
									putRootBlock(world, mdx + even, dy - y, mdz + odd, component, sbb);
								}
							}
						}
					}
					if (!isEven(x) && isEven(z)) {
						// make a - horizontal - door!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								for (int y = 0; y < head; y++) {
									putHeadBlock(world, mdx + odd, dy + tall + y, mdz + even, component, sbb);
								}
								for (int y = 0; y < tall; y++) {
									putDoorBlock(world, mdx + odd, dy + y, mdz + even, component, sbb);
								}
								for (int y = 1; y <= roots; y++) {
									putRootBlock(world, mdx + odd, dy - y, mdz + even, component, sbb);
								}
							}
						}
					}
				}
			}
		}

		// instead of putting placetorches in a seperate function, I just put it here.
		for (int x = 0; x < rawWidth; x++) {
			for (int z = 0; z < rawDepth; z++) {
				if (getRaw(x, z) == 0) {
					int mdx = dx + (x / 2 * (evenBias + oddBias));
					int mdy = dy + 1;
					int mdz = dz + (z / 2 * (evenBias + oddBias));

					if (isEven(x) && isEven(z)) {
						if (shouldTorch(x, z) && component.getBlock(world, mdx, mdy, mdz, sbb).getBlock() == wallBlockState.getBlock()) {
							component.placeBlock(world, torchBlockState, mdx, mdy, mdz, sbb);
						}
					}
				}
			}
		}
	}

	private void makeWallThing(WorldGenLevel world, int dy, TFStructureComponentOld component, BoundingBox sbb, int mdx, int mdz, int even, int odd) {
		for (int y = 0; y < head; y++) {
			putHeadBlock(world, mdx + even, dy + tall + y, mdz + odd, component, sbb);
		}
		for (int y = 0; y < tall; y++) {
			putWallBlock(world, mdx + even, dy + y, mdz + odd, component, sbb);
		}
		for (int y = 1; y <= roots; y++) {
			putRootBlock(world, mdx + even, dy - y, mdz + odd, component, sbb);
		}
	}

	/**
	 * Puts a wall block in the structure, if pillar blocks are properly specified
	 */
	private void putPillarBlock(WorldGenLevel world, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		component.placeBlock(world, pillarBlockState, x, y, z, sbb);
	}

	/**
	 * Puts a wall block in the world, at the specified world coordinates.
	 */
	private void putWallBlock(WorldGenLevel world, int x, int y, int z) {
		world.setBlock(new BlockPos(x, y, z), wallBlockState, Block.UPDATE_CLIENTS);
	}

	/**
	 * Puts a wall block in the structure, at the specified structure coordinates.
	 */
	private void putWallBlock(WorldGenLevel world, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		if (wallBlocks != null) {
			wallBlocks.next(world.getRandom(), x, y, z, true);
			component.placeBlock(world, wallBlocks.getNext(), x, y, z, sbb);
		} else {
			component.placeBlock(world, wallBlockState, x, y, z, sbb);
		}
	}

	/**
	 * Puts a wall block in the structure, at the specified structure coordinates.
	 */
	private void putDoorBlock(WorldGenLevel world, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		component.placeBlock(world, doorBlockState, x, y, z, sbb);
	}

	private void putHeadBlock(WorldGenLevel world, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		component.placeBlock(world, headBlockState, x, y, z, sbb);
	}

	/**
	 * Puts a root block in the structure, at the specified structure coordinates.
	 */
	private void putRootBlock(WorldGenLevel world, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		component.placeBlock(world, rootBlockState, x, y, z, sbb);
	}

	/**
	 * Puts a canopy tree in the world at the specified structure coordinates.
	 */
	private void putCanopyTree(WorldGenLevel world, ChunkGenerator generator, int x, int y, int z, TFStructureComponentOld component, BoundingBox sbb) {
		BlockPos pos = component.getBlockPosWithOffset(x, y, z);

		// only place it if we're actually generating the chunk the tree is in (or at least the middle of the tree)
		if (sbb.isInside(pos)) {
			if (!world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(TFConfiguredFeatures.CANOPY_TREE).place(world, generator, world.getRandom(), pos)) {
				makeWallThing(world, y, component, sbb, x, z, 0, 0);
			}
		}
	}

	private boolean isEven(int n) {
		return n % 2 == 0;
	}

	/**
	 * Should we put a torch here?  Intended to be called on the in-between spots where x and y are even.
	 */
	public boolean shouldTorch(int rx, int rz) {
		// if there is out of bounds in any direction, no
		if (getRaw(rx + 1, rz) == OOB || getRaw(rx - 1, rz) == OOB || getRaw(rx, rz + 1) == OOB || getRaw(rx, rz - 1) == OOB) {
			return false;
		}

		// if there are walls in two opposite directions, no
		if ((getRaw(rx + 1, rz) == 0 && getRaw(rx - 1, rz) == 0) || (getRaw(rx, rz + 1) == 0 && getRaw(rx, rz - 1) == 0)) {
			return false;
		}

		// otherwise, I suppose yes

		// check rarity
		return rand.nextFloat() <= this.torchRarity;
	}

	/**
	 * Should we put a torch here?  Intended to be called on the in-between spots where x and y are even.
	 */
	public boolean shouldPillar(int rx, int rz) {
		// if the pillar block is not defined, no
		if (pillarBlockState == null) {
			return false;
		}

		// if there is out of bounds in any direction, no
		if (getRaw(rx + 1, rz) == OOB || getRaw(rx - 1, rz) == OOB || getRaw(rx, rz + 1) == OOB || getRaw(rx, rz - 1) == OOB) {
			return false;
		}

		// if there are walls in two opposite directions, no
		return (getRaw(rx + 1, rz) != 0 || getRaw(rx - 1, rz) != 0) && (getRaw(rx, rz + 1) != 0 || getRaw(rx, rz - 1) != 0);

		// otherwise, I suppose yes
	}

	/**
	 * Should we put a tree instead of a post?
	 * Essentially the answer is yes for the corners and the exits.
	 */
	public boolean shouldTree(int rx, int rz) {
		if ((rx == 0 || rx == rawWidth - 1) && (getRaw(rx, rz + 1) != 0 || getRaw(rx, rz - 1) != 0)) {
			return true;
		}
		if ((rz == 0 || rz == rawDepth - 1) && (getRaw(rx + 1, rz) != 0 || getRaw(rx - 1, rz) != 0)) {
			return true;
		}

		return rand.nextInt(50) == 0;
	}

	/**
	 * This room is a 3x3 cell room with exits in every direction.
	 */
	public void carveRoom1(int cx, int cz) {
		int rx = cx * 2 + 1;
		int rz = cz * 2 + 1;

		// remove walls and cells
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				putRaw(rx + i, rz + j, ROOM);
			}
		}

		// mark the exit areas as unmazed
		putCell(rx, rz + 1, 0);
		putCell(rx, rz - 1, 0);
		putCell(rx + 1, rz, 0);
		putCell(rx - 1, rz, 0);

		// make 4 exits (if not at the edge of the maze)
		if (getRaw(rx, rz + 4) != OUT_OF_BOUNDS) {
			putRaw(rx, rz + 3, ROOM);
		}
		if (getRaw(rx, rz - 4) != OUT_OF_BOUNDS) {
			putRaw(rx, rz - 3, ROOM);
		}
		if (getRaw(rx + 4, rz) != OUT_OF_BOUNDS) {
			putRaw(rx + 3, rz, ROOM);
		}
		if (getRaw(rx - 4, rz) != OUT_OF_BOUNDS) {
			putRaw(rx - 3, rz, ROOM);
		}
	}

	/**
	 * Adds four exits into the maze.
	 */
	public void add4Exits() {
		int hx = rawWidth / 2 + 1;
		int hz = rawDepth / 2 + 1;

		putRaw(hx, 0, ROOM);
		putRaw(hx, rawDepth - 1, ROOM);
		putRaw(0, hz, ROOM);
		putRaw(rawWidth - 1, hz, ROOM);
	}

	/**
	 * Generates a maze using the recursive backtracking algorithm.
	 *
	 * @param sx The starting x coordinate
	 * @param sz The starting y coordinate
	 */
	public void generateRecursiveBacktracker(int sx, int sz) {
		rbGen(sx, sz);
	}

	/**
	 * Mark the cell as visited.  If we have any unvisited neighbors, pick one randomly, carve the wall between them, then call this function on that neighbor.
	 */
	public void rbGen(int sx, int sz) {
		// mark cell as visited
		putCell(sx, sz, 1);

		// count the unvisted neighbors
		int unvisited = 0;
		if (cellEquals(sx + 1, sz, 0)) {
			unvisited++;
		}
		if (cellEquals(sx - 1, sz, 0)) {
			unvisited++;
		}
		if (cellEquals(sx, sz + 1, 0)) {
			unvisited++;
		}
		if (cellEquals(sx, sz - 1, 0)) {
			unvisited++;
		}

		// if there are no unvisited neighbors, return
		if (unvisited == 0) {
			return;
		}

		// otherwise, pick a random neighbor to visit
		int rn = rand.nextInt(unvisited);
		int dx, dz;
		dx = dz = 0;

		if (cellEquals(sx + 1, sz, 0)) {
			if (rn == 0) {
				dx = sx + 1;
				dz = sz;
			}
			rn--;
		}
		if (cellEquals(sx - 1, sz, 0)) {
			if (rn == 0) {
				dx = sx - 1;
				dz = sz;
			}
			rn--;
		}
		if (cellEquals(sx, sz + 1, 0)) {
			if (rn == 0) {
				dx = sx;
				dz = sz + 1;
			}
			rn--;
		}
		if (cellEquals(sx, sz - 1, 0)) {
			if (rn == 0) {
				dx = sx;
				dz = sz - 1;
			}
		}

		// carve wall or door
		if (rand.nextFloat() <= this.doorRarity) {
			putWall(sx, sz, dx, dz, DOOR);
		} else {
			putWall(sx, sz, dx, dz, 2);
		}

		// call function recursively at the destination
		rbGen(dx, dz);

		// the destination has run out of free spaces, let's try this square again, up to 2 more times
		rbGen(sx, sz);
		rbGen(sx, sz);
	}

	@Override
	public TFMaze clone() {
		try {
			TFMaze clone = (TFMaze) super.clone();
			synchronized (this) {
				clone.rand = this.rand.fork();
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
