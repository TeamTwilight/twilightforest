package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleMuralComponent extends TFStructureComponentOld {

	private int height;
	private int width;

	// we will model the mural in this byte array
	private volatile byte[][] mural;

	public FinalCastleMuralComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCMur.get(), nbt);
	}

	@SuppressWarnings("this-escape")
	public FinalCastleMuralComponent(int i, int x, int y, int z, int width, int height, Direction direction) {
		super(TFStructurePieceTypes.TFFCMur.get(), i, x, y, z);
		this.setOrientation(direction);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -height / 2, -width / 2, 1, height - 1, width - 1, direction);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		if (this.mural == null) {
			synchronized (this) {
				if (this.mural == null) {
					this.height = this.boundingBox.getYSpan();
					this.width = (this.getOrientation() == Direction.SOUTH || this.getOrientation() == Direction.NORTH) ? this.boundingBox.getZSpan() : this.boundingBox.getXSpan();

					// only make it once
					byte[][] mural = new byte[width][height];

					int startX = width / 2 - 1;
					int startY = 2;

					// make mural, fill in dot by start
					for (int x = -1; x < 2; x++) {
						for (int y = -1; y < 2; y++) {
							mural[startX + x][startY + y] = 1;
						}
					}

					// side branches
					makeHorizontalTree(decoRNG, mural, startX + 1, startY, decoRNG.nextInt(width / 6) + width / 6, true);
					makeHorizontalTree(decoRNG, mural, startX - 1, startY, decoRNG.nextInt(width / 6) + width / 6, false);

					// main tree
					makeVerticalTree(decoRNG, mural, startX, startY + 1, decoRNG.nextInt(height / 6) + height / 6, true);

					this.mural = mural;

					// stripes
					makeStripes(decoRNG);
				}
			}
		}

		final BlockState castleMagic = TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().defaultBlockState();

		// copy mural to world
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (mural[x][y] > 0) {

					this.placeBlock(world, castleMagic, 0, y, x, sbb);
				} else {
					//this.setBlockState(world, TFBlocks.forceField, 0, 0, y, x, sbb);
				}
			}
		}
	}

	private void makeHorizontalTree(RandomSource decoRNG, byte[][] mural, int centerX, int centerY, int branchLength, boolean positive) {
		this.fillHorizontalLine(mural, centerX, centerY, branchLength, positive);

		this.makeHorizontalBranch(mural, decoRNG, centerX, centerY, branchLength, positive, true);
		this.makeHorizontalBranch(mural, decoRNG, centerX, centerY, branchLength, positive, false);
	}

	private void makeVerticalTree(RandomSource decoRNG, byte[][] mural, int centerX, int centerY, int branchLength, boolean positive) {
		this.fillVerticalLine(mural, centerX, centerY, branchLength, positive);

		this.makeVerticalBranch(mural, decoRNG, centerX, centerY, branchLength, positive, true);
		this.makeVerticalBranch(mural, decoRNG, centerX, centerY, branchLength, positive, false);
	}

	private boolean makeHorizontalBranch(byte[][] mural, RandomSource rand, int sx, int sy, int length, boolean plusX, boolean plusY) {
		int downLine = (length / 2) + 1 + rand.nextInt(Math.max(length / 2, 2));
		int branchLength = rand.nextInt(width / 8) + width / 8;

		// check if the area is clear
		boolean clear = true;
		for (int i = 0; i <= branchLength; i++) {
			int cx = sx + (plusX ? downLine - 1 + i : -(downLine - 1 + i));
			int cy = sy + (plusY ? 2 : -2);
			if (cx < 0 || cx >= width || cy < 0 || cy >= height || mural[cx][cy] > 0) {
				clear = false;
				break;
			}
		}
		if (clear) {
			int bx = sx + (plusX ? downLine : -downLine);
			int by = sy;

			// jag
			this.fillVerticalLine(mural, bx, by, 1, plusY);

			by += (plusY ? 2 : -2);

			this.fillHorizontalLine(mural, bx, by, branchLength, plusX);

			// recurse?
			if (bx > 0 && bx < width && by > 0 && by < height) {
				if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, true)) {
					if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, true)) {
						if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, true)) {
							this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, true);
						}
					}
				}
				if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, false)) {
					if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, false)) {
						if (!this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, false)) {
							this.makeHorizontalBranch(mural, rand, bx, by, branchLength, plusX, false);
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean makeVerticalBranch(byte[][] mural, RandomSource rand, int sx, int sy, int length, boolean plusY, boolean plusX) {
		int downLine = (length / 2) + 1 + rand.nextInt(Math.max(length / 2, 2));
		int branchLength = rand.nextInt(height / 8) + height / 8;

		// check if the area is clear
		boolean clear = true;
		for (int i = 0; i <= branchLength; i++) {
			int cx = sx + (plusX ? 2 : -2);
			int cy = sy + (plusY ? downLine - 1 + i : -(downLine - 1 + i));
			if (cx < 0 || cx >= width || cy < 0 || cy >= height || mural[cx][cy] > 0) {
				clear = false;
				break;
			}
		}
		if (clear) {
			int bx = sx;
			int by = sy + (plusY ? downLine : -downLine);

			// jag
			this.fillHorizontalLine(mural, bx, by, 1, plusX);

			bx += (plusX ? 2 : -2);

			this.fillVerticalLine(mural, bx, by, branchLength, plusY);

			// recurse?
			if (bx > 0 && bx < width && by > 0 && by < height) {
				if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, true)) {
					if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, true)) {
						if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, true)) {
							this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, true);
						}
					}
				}
				if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, false)) {
					if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, false)) {
						if (!this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, false)) {
							this.makeVerticalBranch(mural, rand, bx, by, branchLength, plusY, false);
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void fillHorizontalLine(byte[][] mural, int sx, int sy, int length, boolean positive) {
		int x = sx;
		int y = sy;

		for (int i = 0; i <= length; i++) {
			if (x >= 0 && x < width && y >= 0 && y < height) {
				mural[x][y] = (byte) (positive ? 1 : 4);

				x += positive ? 1 : -1;
			}
		}
	}

	private void fillVerticalLine(byte[][] mural, int sx, int sy, int length, boolean positive) {
		int x = sx;
		int y = sy;

		for (int i = 0; i <= length; i++) {
			if (x >= 0 && x < width && y >= 0 && y < height) {
				mural[x][y] = (byte) (positive ? 2 : 3);

				y += positive ? 1 : -1;
			}
		}
	}

	private void makeStripes(RandomSource decoRNG) {
		// stagger slightly on our way down
		for (int y = this.height - 2; y > this.height / 3; y -= (2 + decoRNG.nextInt(2))) {
			makeSingleStripe(y);
		}
	}

	private void makeSingleStripe(int y) {
		for (int x = 0; x < this.width - 2; x++) {
			if (mural[x + 1][y] == 0 && mural[x + 1][y + 1] == 0) {
				mural[x][y] = 1;
			} else {
				break;
			}
		}
		for (int x = this.width - 1; x > 2; x--) {
			if (mural[x - 1][y] == 0 && mural[x - 1][y + 1] == 0) {
				mural[x][y] = 1;
			} else {
				break;
			}
		}
	}
}
