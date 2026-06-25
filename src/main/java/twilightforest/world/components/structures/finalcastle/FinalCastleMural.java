package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class FinalCastleMural {
	final int height;
	final int width;

	// we will model the mural in this byte array
	private final byte[] mural;

	public FinalCastleMural(CompoundTag compoundTag) {
		this.width = compoundTag.getIntOr("muralWidth", 0);
		this.height = compoundTag.getIntOr("muralHeight", 0);

		if (this.width == 0 || this.height == 0) {
			this.mural = new byte[0];
		} else {
			this.mural = compoundTag.getByteArray("muralBytes").orElse(new byte[0]);
		}
	}

	public void writeIntoTag(CompoundTag compoundTag) {
		compoundTag.putInt("muralWidth", this.width);
		compoundTag.putInt("muralHeight", this.height);
		compoundTag.putByteArray("muralBytes", this.mural);
	}

	public FinalCastleMural(BoundingBox boundingBox, Direction orientation, long seed) {
		this.height = boundingBox.getYSpan();
		this.width = (orientation == Direction.SOUTH || orientation == Direction.NORTH) ? boundingBox.getZSpan() : boundingBox.getXSpan();

		this.mural = new byte[this.width * this.height];
		this.generateMural(seed);
	}

	private void generateMural(long seed) {
		RandomSource decoRNG = RandomSource.create(seed);

		int startX = this.width / 2 - 1;
		int startY = 2;

		// make mural, fill in dot by start
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				this.set(startX + x, startY + y, (byte) 1);
			}
		}

		// side branches
		this.makeHorizontalTree(decoRNG, startX + 1, startY, decoRNG.nextInt(this.width / 6) + this.width / 6, true);
		this.makeHorizontalTree(decoRNG, startX - 1, startY, decoRNG.nextInt(this.width / 6) + this.width / 6, false);

		// main tree
		this.makeVerticalTree(decoRNG, startX, startY + 1, decoRNG.nextInt(this.height / 6) + this.height / 6, true);

		// stripes
		this.makeStripes(decoRNG);
	}

	private void makeHorizontalTree(RandomSource decoRNG, int centerX, int centerY, int branchLength, boolean positive) {
		this.fillHorizontalLine(centerX, centerY, branchLength, positive);

		this.makeHorizontalBranch(decoRNG, centerX, centerY, branchLength, positive, true);
		this.makeHorizontalBranch(decoRNG, centerX, centerY, branchLength, positive, false);
	}

	private void makeVerticalTree(RandomSource decoRNG, int centerX, int centerY, int branchLength, boolean positive) {
		this.fillVerticalLine(centerX, centerY, branchLength, positive);

		this.makeVerticalBranch(decoRNG, centerX, centerY, branchLength, positive, true);
		this.makeVerticalBranch(decoRNG, centerX, centerY, branchLength, positive, false);
	}

	private boolean makeHorizontalBranch(RandomSource rand, int sx, int sy, int length, boolean plusX, boolean plusY) {
		int downLine = (length / 2) + 1 + rand.nextInt(Math.max(length / 2, 2));
		int branchLength = rand.nextInt(this.width / 8) + this.width / 8;

		// check if the area is clear
		boolean clear = true;
		for (int i = 0; i <= branchLength; i++) {
			int cx = sx + (plusX ? downLine - 1 + i : -(downLine - 1 + i));
			int cy = sy + (plusY ? 2 : -2);
			if (cx < 0 || cx >= this.width || cy < 0 || cy >= this.height || this.get(cx, cy) > 0) {
				clear = false;
				break;
			}
		}
		if (clear) {
			int bx = sx + (plusX ? downLine : -downLine);
			int by = sy;

			// jag
			this.fillVerticalLine(bx, by, 1, plusY);

			by += (plusY ? 2 : -2);

			this.fillHorizontalLine(bx, by, branchLength, plusX);

			// recurse?
			if (bx > 0 && bx < this.width && by > 0 && by < this.height) {
				if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, true)) {
					if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, true)) {
						if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, true)) {
							this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, true);
						}
					}
				}
				if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, false)) {
					if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, false)) {
						if (!this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, false)) {
							this.makeHorizontalBranch(rand, bx, by, branchLength, plusX, false);
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean makeVerticalBranch(RandomSource rand, int sx, int sy, int length, boolean plusY, boolean plusX) {
		int downLine = (length / 2) + 1 + rand.nextInt(Math.max(length / 2, 2));
		int branchLength = rand.nextInt(this.height / 8) + this.height / 8;

		// check if the area is clear
		boolean clear = true;
		for (int i = 0; i <= branchLength; i++) {
			int cx = sx + (plusX ? 2 : -2);
			int cy = sy + (plusY ? downLine - 1 + i : -(downLine - 1 + i));
			if (cx < 0 || cx >= this.width || cy < 0 || cy >= this.height || this.get(cx, cy) > 0) {
				clear = false;
				break;
			}
		}
		if (clear) {
			int bx = sx;
			int by = sy + (plusY ? downLine : -downLine);

			// jag
			this.fillHorizontalLine(bx, by, 1, plusX);

			bx += (plusX ? 2 : -2);

			this.fillVerticalLine(bx, by, branchLength, plusY);

			// recurse?
			if (bx > 0 && bx < this.width && by > 0 && by < this.height) {
				if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, true)) {
					if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, true)) {
						if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, true)) {
							this.makeVerticalBranch(rand, bx, by, branchLength, plusY, true);
						}
					}
				}
				if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, false)) {
					if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, false)) {
						if (!this.makeVerticalBranch(rand, bx, by, branchLength, plusY, false)) {
							this.makeVerticalBranch(rand, bx, by, branchLength, plusY, false);
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void fillHorizontalLine(int sx, int sy, int length, boolean positive) {
		int x = sx;
		int y = sy;

		for (int i = 0; i <= length; i++) {
			if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
				this.set(x, y, (byte) (positive ? 1 : 4));

				x += positive ? 1 : -1;
			}
		}
	}

	private void fillVerticalLine(int sx, int sy, int length, boolean positive) {
		int x = sx;
		int y = sy;

		for (int i = 0; i <= length; i++) {
			if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
				this.set(x, y, (byte) (positive ? 2 : 3));

				y += positive ? 1 : -1;
			}
		}
	}

	private void makeStripes(RandomSource decoRNG) {
		// stagger slightly on our way down
		for (int y = this.height - 2; y > this.height / 3; y -= (2 + decoRNG.nextInt(2))) {
			this.makeSingleStripe(y);
		}
	}

	private void makeSingleStripe(int y) {
		for (int x = 0; x < this.width - 2; x++) {
			if (this.get(x + 1, y) == 0 && this.get(x + 1, y + 1) == 0) {
				this.set(x, y, (byte) 1);
			} else {
				break;
			}
		}
		for (int x = this.width - 1; x > 2; x--) {
			if (this.get(x - 1, y) == 0 && this.get(x - 1, y + 1) == 0) {
				this.set(x, y, (byte) 1);
			} else {
				break;
			}
		}
	}

	public void set(int x, int y, byte value) {
		int index = x * this.height + y;
		this.mural[index] = value;
	}

	public byte get(int x, int y) {
		int index = x * this.height + y;
		return this.mural[index];
	}
}
