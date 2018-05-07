package twilightforest.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.world.TFGenCanopyTree;

import java.util.Random;


/**
 * This is a maze of cells and walls.
 * <p>
 * The cells are at odd numbered x and y values, and the walls are at even numbered ones.  This does make the storage slightly inefficient, but oh wells.
 *
 * @author Ben
 */
public class TFMaze {

	public int width; // cells wide (x)
	public int depth; // cells deep (z)

	public int oddBias; // corridor thickness, default 3
	public int evenBias; // wall thickness here.  NYI 

	public int tall; // wall blocks tall
	public int head;// blocks placed above the maze
	public int roots;// blocks placed under the maze (used for hedge mazes)

	public int worldX; // set when we first copy the maze into the world
	public int worldY;
	public int worldZ;

	public int type; // 1-3 = various sizes hollow hills

	public IBlockState wallBlockState;

	public IBlockState wallVar0State;
	public float wallVarRarity;

	public IBlockState headBlockState;

	public IBlockState rootBlockState;

	public IBlockState pillarBlockState;

	public IBlockState doorBlockState;
	public float doorRarity;

	public IBlockState torchBlockState;
	public float torchRarity;

	protected int rawWidth;
	protected int rawDepth;
	protected int[] storage;

	public static final int OUT_OF_BOUNDS = Integer.MIN_VALUE;
	public static final int OOB = OUT_OF_BOUNDS;
	public static final int ROOM = 5;
	public static final int DOOR = 6;

	public Random rand;

	public TFMaze(int cellsWidth, int cellsDepth) {
		// default values
		oddBias = 3;
		evenBias = 1;
		tall = 3;
		head = 0;
		roots = 0;
		wallBlockState = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CHISELED);
		rootBlockState = TFBlocks.maze_stone.getDefaultState();
		torchBlockState = Blocks.TORCH.getDefaultState();
		pillarBlockState = null;

		torchRarity = 0.75F;
		doorRarity = 0F;

		this.width = cellsWidth;
		this.depth = cellsDepth;

		this.rawWidth = width * 2 + 1;
		this.rawDepth = depth * 2 + 1;
		storage = new int[rawWidth * rawDepth];

		rand = new Random();
	}

	/**
	 * Gets the value from a cell in the maze
	 */
	private int getCell(int x, int z) {
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
			return getRaw(sx * 2 + 0, sz * 2 + 1);
		}
		if (dx == sx && dz == sz + 1) {
			return getRaw(sx * 2 + 1, sz * 2 + 2);
		}
		if (dx == sx && dz == sz - 1) {
			return getRaw(sx * 2 + 1, sz * 2 + 0);
		}

		TwilightForestMod.LOGGER.info("Wall check out of bounds; s = " + sx + ", " + sz + "; d = " + dx + ", " + dz);

		return OUT_OF_BOUNDS;
	}

	public void putWall(int sx, int sz, int dx, int dz, int value) {
		if (dx == sx + 1 && dz == sz) {
			putRaw(sx * 2 + 2, sz * 2 + 1, value);
		}
		if (dx == sx - 1 && dz == sz) {
			putRaw(sx * 2 + 0, sz * 2 + 1, value);
		}
		if (dx == sx && dz == sz + 1) {
			putRaw(sx * 2 + 1, sz * 2 + 2, value);
		}
		if (dx == sx && dz == sz - 1) {
			putRaw(sx * 2 + 1, sz * 2 + 0, value);
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

	/**
	 * Sets the random seed to a specific value
	 */
	public void setSeed(long newSeed) {
		rand.setSeed(newSeed);
	}

	/**
	 * Copies the maze into the world by placing walls.
	 */
	public void copyToWorld(World world, int dx, int dy, int dz) {
		worldX = dx;
		worldY = dy;
		worldZ = dz;

		for (int x = 0; x < rawWidth; x++) {
			for (int z = 0; z < rawDepth; z++) {
				if (getRaw(x, z) == 0) {
					int mdx = dx + (x / 2 * (evenBias + oddBias));
					int mdz = dz + (z / 2 * (evenBias + oddBias));

					if (isEven(x) && isEven(z)) {
						if (type == 4 && shouldTree(x, z)) {
							// occasionally make a tree 
							(new TFGenCanopyTree()).generate(world, rand, new BlockPos(mdx, dy, mdz));
						} else {
							// make a block!
							for (int y = 0; y < head; y++) {
								putHeadBlock(world, mdx, dy + tall + y, mdz);
							}
							for (int y = 0; y < tall; y++) {
								putWallBlock(world, mdx, dy + y, mdz);
							}
							for (int y = 1; y <= roots; y++) {
								putRootBlock(world, mdx, dy - y, mdz);
							}
						}
					}
					if (isEven(x) && !isEven(z)) {
						// make a | vertical | wall!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								for (int y = 0; y < head; y++) {
									putHeadBlock(world, mdx + even, dy + tall + y, mdz + odd);
								}
								for (int y = 0; y < tall; y++) {
									putWallBlock(world, mdx + even, dy + y, mdz + odd);
								}
								for (int y = 1; y <= roots; y++) {
									putRootBlock(world, mdx + even, dy - y, mdz + odd);
								}
							}
						}
					}
					if (!isEven(x) && isEven(z)) {
						// make a - horizontal - wall!
						for (int even = 0; even < evenBias; even++) {
							for (int odd = 1; odd <= oddBias; odd++) {
								for (int y = 0; y < head; y++) {
									putHeadBlock(world, mdx + odd, dy + tall + y, mdz + even);
								}
								for (int y = 0; y < tall; y++) {
									putWallBlock(world, mdx + odd, dy + y, mdz + even);
								}
								for (int y = 1; y <= roots; y++) {
									putRootBlock(world, mdx + odd, dy - y, mdz + even);
								}
							}
						}
					}
				}
			}
		}

		placeTorches(world);
	}

	/**
	 * Copies the maze into the world by carving out empty spaces.
	 */
	public void carveToWorld(World world, int dx, int dy, int dz) {
		worldX = dx;
		worldY = dy;
		worldZ = dz;

		for (int x = 0; x < rawWidth; x++) {
			for (int z = 0; z < rawDepth; z++) {
				if (getRaw(x, z) != 0) {
					int mdx = dx + (x / 2 * (evenBias + oddBias));
					int mdz = dz + (z / 2 * (evenBias + oddBias));

					if (isEven(x) && isEven(z)) {
						// carve a one-block wide pillar
						for (int y = 0; y < tall; y++) {
							carveBlock(world, mdx, dy + y, mdz);
						}
					} else if (isEven(x) && !isEven(z)) {
						// carve a | vertical | wall
						for (int i = 1; i <= oddBias; i++) {
							for (int y = 0; y < tall; y++) {
								carveBlock(world, mdx, dy + y, mdz + i);
							}
						}
					} else if (!isEven(x) && isEven(z)) {
						// carve a - horizontal - wall!
						for (int i = 1; i <= oddBias; i++) {
							for (int y = 0; y < tall; y++) {
								carveBlock(world, mdx + i, dy + y, mdz);
							}
						}
					} else if (!isEven(x) && !isEven(z)) // this should always be true at this point
					{
						// carve an open space
						for (int mx = 1; mx <= oddBias; mx++) {
							for (int mz = 1; mz <= oddBias; mz++) {
								for (int y = 0; y < tall; y++) {
									carveBlock(world, mdx + mx, dy + y, mdz + mz);
								}
							}
						}
					}
				}
			}
		}

		placeTorches(world);
	}


	/**
	 * Copy the maze into a StructureTFComponentOld
	 */
	public void copyToStructure(World world, int dx, int dy, int dz, StructureTFComponentOld component, StructureBoundingBox sbb) {
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
//							(new TFGenCanopyTree()).generate(world, rand, mdx, dy, mdz);
							putCanopyTree(world, mdx, dy, mdz, component, sbb);
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
						if (shouldTorch(x, z) && component.getBlockStateFromPos(world, mdx, mdy, mdz, sbb).getBlock() == wallBlockState.getBlock()) {
							component.setBlockState(world, torchBlockState, mdx, mdy, mdz, sbb);
						}
					}
				}
			}
		}

	}

	private void makeWallThing(World world, int dy, StructureTFComponentOld component, StructureBoundingBox sbb, int mdx, int mdz, int even, int odd) {
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
	private void putPillarBlock(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		component.setBlockState(world, pillarBlockState, x, y, z, sbb);
	}

	/**
	 * Puts a wall block in the world, at the specified world coordinates.
	 */
	private void putWallBlock(World world, int x, int y, int z) {
		world.setBlockState(new BlockPos(x, y, z), wallBlockState, 2);
	}

	/**
	 * Puts a wall block in the structure, at the specified structure coordinates.
	 */
	private void putWallBlock(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		if (wallVarRarity > 0 && rand.nextFloat() < this.wallVarRarity) {
			component.setBlockState(world, wallVar0State, x, y, z, sbb);
		} else {
			component.setBlockState(world, wallBlockState, x, y, z, sbb);
		}
	}

	/**
	 * Puts a wall block in the structure, at the specified structure coordinates.
	 */
	private void putDoorBlock(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		component.setBlockState(world, doorBlockState, x, y, z, sbb);
	}

	/**
	 * Carves a block into the world.
	 * TODO: check what's there?  maybe only certain blocks?
	 */
	private void carveBlock(World world, int x, int y, int z) {
		world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 2);
	}

	private void putHeadBlock(World world, int x, int y, int z) {
		world.setBlockState(new BlockPos(x, y, z), headBlockState, 2);
	}

	private void putHeadBlock(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		component.setBlockState(world, headBlockState, x, y, z, sbb);
	}


	/**
	 * Puts a root block in the world, at the specified world coordinates.
	 */
	private void putRootBlock(World world, int x, int y, int z) {
		world.setBlockState(new BlockPos(x, y, z), rootBlockState, 2);
	}

	/**
	 * Puts a root block in the structure, at the specified structure coordinates.
	 */
	private void putRootBlock(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		component.setBlockState(world, rootBlockState, x, y, z, sbb);
	}

	/**
	 * Puts a canopy tree in the world at the specified structure coordinates.
	 */
	private void putCanopyTree(World world, int x, int y, int z, StructureTFComponentOld component, StructureBoundingBox sbb) {
		BlockPos pos = component.getBlockPosWithOffset(x, y, z);

		// only place it if we're actually generating the chunk the tree is in (or at least the middle of the tree)
		if (sbb.isVecInside(pos)) {
			(new TFGenCanopyTree()).generate(world, rand, pos);
		}
	}

	private final boolean isEven(int n) {
		return n % 2 == 0;
	}

	/**
	 * Called after copyToWorld.  Places torches in the maze as appropriate
	 */
	private void placeTorches(World world) {

		int torchHeight = 1;

		for (int x = 0; x < rawWidth; x++) {
			for (int z = 0; z < rawDepth; z++) {
				if (getRaw(x, z) == 0) {
					int mdx = worldX + (x / 2 * (evenBias + oddBias));
					int mdy = worldY + torchHeight;
					int mdz = worldZ + (z / 2 * (evenBias + oddBias));

					BlockPos pos = new BlockPos(mdx, mdy, mdz);

					if (isEven(x) && isEven(z)) {
						if (shouldTorch(x, z) && world.getBlockState(pos).getBlock() == wallBlockState.getBlock()) {
							world.setBlockState(pos, torchBlockState, 2);
						}
					}
				}
			}
		}
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
		if ((getRaw(rx + 1, rz) == 0 && getRaw(rx - 1, rz) == 0) || (getRaw(rx, rz + 1) == 0 && getRaw(rx, rz - 1) == 0)) {
			return false;
		}

		// otherwise, I suppose yes
		return true;
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
	 * If the worldX is set properly, this returns where in that world the maze coordinate x lies
	 *
	 * @param x
	 * @return
	 */
	int getWorldX(int x) {
		return worldX + (x * (evenBias + oddBias)) + 1;
	}

	/**
	 * If the worldZ is set properly, this returns where in that world the maze coordinate z lies
	 *
	 * @param z
	 * @return
	 */
	int getWorldZ(int z) {
		return worldZ + (z * (evenBias + oddBias)) + 1;
	}


	/**
	 * Carves a room into the maze.  The coordinates given are cell coordinates.
	 */
	public void carveRoom0(int cx, int cz) {

		putCell(cx, cz, 5);

		putCell(cx + 1, cz, 5);
		putWall(cx, cz, cx + 1, cz, 5);
		putCell(cx - 1, cz, 5);
		putWall(cx, cz, cx - 1, cz, 5);
		putCell(cx, cz + 1, 5);
		putWall(cx, cz, cx, cz + 1, 5);
		putCell(cx, cz - 1, 5);
		putWall(cx, cz, cx, cz - 1, 5);
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
	 *
	 * @param sx
	 * @param sz
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
}
