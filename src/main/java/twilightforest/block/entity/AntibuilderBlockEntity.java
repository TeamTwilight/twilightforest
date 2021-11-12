package twilightforest.block.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import twilightforest.block.TFBlocks;
import twilightforest.data.BlockTagGenerator;

import java.util.Random;

public class AntibuilderBlockEntity extends BlockEntity {
	private static final int REVERT_CHANCE = 10;

	private static final int RADIUS = 4;
	private static final int DIAMETER = 2 * RADIUS + 1;
	private static final double PLAYER_RANGE = 16.0;

	private final Random rand = new Random();

	private int tickCount;
	private boolean slowScan;
	private int ticksSinceChange;

	private BlockState[] blockData;

	public AntibuilderBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.ANTIBUILDER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AntibuilderBlockEntity te) {
		if (te.anyPlayerInRange()) {
			te.tickCount++;

			if (level.isClientSide) {
				double x = pos.getX() + level.random.nextFloat();
				double y = pos.getY() + level.random.nextFloat();
				double z = pos.getZ() + level.random.nextFloat();
				level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);

				// occasionally make a little red dust line to outline our radius
				if (te.rand.nextInt(10) == 0) {
					te.makeRandomOutline();
					te.makeRandomOutline();
					te.makeRandomOutline();
				}
			} else {

				// new plan, take a snapshot of the world when we are first activated, and then rapidly revert changes
				if (te.blockData == null && level.isAreaLoaded(pos, AntibuilderBlockEntity.RADIUS)) {
					te.captureBlockData();
					te.slowScan = true;
				}

				if (te.blockData != null && (!te.slowScan || te.tickCount % 20 == 0)) {
					if (te.scanAndRevertChanges()) {
						te.slowScan = false;
						te.ticksSinceChange = 0;
					} else {
						te.ticksSinceChange++;

						if (te.ticksSinceChange > 20) {
							te.slowScan = true;
						}
					}
				}
			}
		} else {
			// remove data
			te.blockData = null;
			te.tickCount = 0;
		}
	}


	/**
	 * Display a random one of the 12 possible outlines
	 */
	private void makeRandomOutline() {
		makeOutline(this.rand.nextInt(12));
	}

	/**
	 * Display a specific outline
	 */
	private void makeOutline(int outline) {
		// src
		double sx = this.worldPosition.getX();
		double sy = this.worldPosition.getY();
		double sz = this.worldPosition.getZ();
		// dest
		double dx = this.worldPosition.getX();
		double dy = this.worldPosition.getY();
		double dz = this.worldPosition.getZ();

		switch (outline) {
			case 0:
			case 8:
				sx -= RADIUS;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz -= RADIUS;
				break;
			case 1:
			case 9:
				sx -= RADIUS;
				dx -= RADIUS;
				sz -= RADIUS;
				dz += RADIUS + 1;
				break;
			case 2:
			case 10:
				sx -= RADIUS;
				dx += RADIUS + 1;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
				break;
			case 3:
			case 11:
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz += RADIUS + 1;
				break;
			case 4:
				sx -= RADIUS;
				dx -= RADIUS;
				sz -= RADIUS;
				dz -= RADIUS;
				break;
			case 5:
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz -= RADIUS;
				break;
			case 6:
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
				break;
			case 7:
				sx -= RADIUS;
				dx -= RADIUS;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
				break;
		}

		switch (outline) {
			case 0:
			case 1:
			case 2:
			case 3:
				sy += RADIUS + 1;
				dy += RADIUS + 1;
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				sy -= RADIUS;
				dy += RADIUS + 1;
				break;
			case 8:
			case 9:
			case 10:
			case 11:
				sy -= RADIUS;
				dy -= RADIUS;
				break;
		}

		if (rand.nextBoolean()) {
			drawParticleLine(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, dx, dy, dz);
		} else {
			drawParticleLine(sx, sy, sz, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
		}
		drawParticleLine(sx, sy, sz, dx, dy, dz);
	}

	private void drawParticleLine(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 16;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);

			double tx = srcX + (destX - srcX) * trailFactor + rand.nextFloat() * 0.005;
			double ty = srcY + (destY - srcY) * trailFactor + rand.nextFloat() * 0.005;
			double tz = srcZ + (destZ - srcZ) * trailFactor + rand.nextFloat() * 0.005;
			level.addParticle(DustParticleOptions.REDSTONE, tx, ty, tz, 0, 0, 0);
		}
	}

	private boolean scanAndRevertChanges() {
		int index = 0;
		boolean reverted = false;

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					BlockState stateThere = level.getBlockState(worldPosition.offset(x, y, z));

					if (blockData[index].getBlock() != stateThere.getBlock()) {
						if (revertBlock(worldPosition.offset(x, y, z), stateThere, blockData[index])) {
							reverted = true;
						} else {
							blockData[index] = stateThere;
						}
					}

					index++;
				}
			}
		}

		return reverted;
	}

	private boolean revertBlock(BlockPos pos, BlockState stateThere, BlockState replaceWith) {
		if (stateThere.isAir() && !replaceWith.getMaterial().blocksMotion()) {
			return false;
		}
		if (stateThere.getDestroySpeed(level, pos) < 0 || isUnrevertable(stateThere, replaceWith)) {
			return false;
		} else if (this.rand.nextInt(REVERT_CHANCE) == 0) {
			// don't revert everything instantly
			if (!replaceWith.isAir()) {
				replaceWith = TFBlocks.ANTIBUILT_BLOCK.get().defaultBlockState();
			}

			if (stateThere.isAir()) {
				level.levelEvent(2001, pos, Block.getId(replaceWith));
			}
			Block.updateOrDestroy(stateThere, replaceWith, level, pos, 2);
		}

		return true;
	}

	private boolean isUnrevertable(BlockState stateThere, BlockState replaceWith) {
		return BlockTagGenerator.ANTIBUILDER_IGNORES.contains(stateThere.getBlock()) || BlockTagGenerator.ANTIBUILDER_IGNORES.contains(replaceWith.getBlock());
	}

	private void captureBlockData() {
		blockData = new BlockState[DIAMETER * DIAMETER * DIAMETER];

		int index = 0;

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					blockData[index] = level.getBlockState(worldPosition.offset(x, y, z));
					index++;
				}
			}
		}
	}

	private boolean anyPlayerInRange() {
		return this.level.hasNearbyAlivePlayer(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D, AntibuilderBlockEntity.PLAYER_RANGE);
	}
}
