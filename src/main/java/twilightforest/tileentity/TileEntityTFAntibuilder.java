package twilightforest.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import twilightforest.TFConfig;
import twilightforest.block.BlockTFTowerTranslucent;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerTranslucentVariant;

import java.util.List;
import java.util.Random;

public class TileEntityTFAntibuilder extends TileEntity implements ITickable {
	private static final int REVERT_CHANCE = 10;

	public int radius = 4;
	public int diameter = 2 * radius + 1;
	private double requiredPlayerRange = 16;
	public final Random rand = new Random();
	private int tickCount;

	private boolean slowScan;
	private int ticksSinceChange;

	private IBlockState[] blockData;

	@Override
	public void update() {
		if (this.anyPlayerInRange()) {
			this.tickCount++;

			if (this.world.isRemote) {
				double var1 = (double) ((float) this.pos.getX() + this.world.rand.nextFloat());
				double var3 = (double) ((float) this.pos.getY() + this.world.rand.nextFloat());
				double var5 = (double) ((float) this.pos.getZ() + this.world.rand.nextFloat());
//				this.world.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
				this.world.spawnParticle(EnumParticleTypes.REDSTONE, var1, var3, var5, 0.0D, 0.0D, 0.0D);


				// occasionally make a little red dust line to outline our radius
				if (this.rand.nextInt(10) == 0) {
					makeRandomOutline();
					makeRandomOutline();
					makeRandomOutline();
				}
			} else {

				// new plan, take a snapshot of the world when we are first activated, and then rapidly revert changes
				if (blockData == null && world.isAreaLoaded(this.pos, this.radius)) {
					captureBlockData();
					this.slowScan = true;
				}

				if (blockData != null && (!this.slowScan || this.tickCount % 20 == 0)) {
					if (scanAndRevertChanges()) {
						this.slowScan = false;
						this.ticksSinceChange = 0;
					} else {
						ticksSinceChange++;

						if (ticksSinceChange > 20) {
							this.slowScan = true;
						}
					}
				}
			}
		} else {
			// remove data
			this.blockData = null;
			this.tickCount = 0;
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
		double sx = this.pos.getX();
		double sy = this.pos.getY();
		double sz = this.pos.getZ();
		// dest
		double dx = this.pos.getX();
		double dy = this.pos.getY();
		double dz = this.pos.getZ();

		switch (outline) {
			case 0:
			case 8:
				sx -= radius;
				dx += radius + 1;
				sz -= radius;
				dz -= radius;
				break;
			case 1:
			case 9:
				sx -= radius;
				dx -= radius;
				sz -= radius;
				dz += radius + 1;
				break;
			case 2:
			case 10:
				sx -= radius;
				dx += radius + 1;
				sz += radius + 1;
				dz += radius + 1;
				break;
			case 3:
			case 11:
				sx += radius + 1;
				dx += radius + 1;
				sz -= radius;
				dz += radius + 1;
				break;
			case 4:
				sx -= radius;
				dx -= radius;
				sz -= radius;
				dz -= radius;
				break;
			case 5:
				sx += radius + 1;
				dx += radius + 1;
				sz -= radius;
				dz -= radius;
				break;
			case 6:
				sx += radius + 1;
				dx += radius + 1;
				sz += radius + 1;
				dz += radius + 1;
				break;
			case 7:
				sx -= radius;
				dx -= radius;
				sz += radius + 1;
				dz += radius + 1;
				break;
		}

		switch (outline) {
			case 0:
			case 1:
			case 2:
			case 3:
				sy += radius + 1;
				dy += radius + 1;
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				sy -= radius;
				dy += radius + 1;
				break;
			case 8:
			case 9:
			case 10:
			case 11:
				sy -= radius;
				dy -= radius;
				break;
		}

		if (rand.nextBoolean()) {
			drawParticleLine(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dx, dy, dz);
		} else {
			drawParticleLine(sx, sy, sz, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
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
			world.spawnParticle(EnumParticleTypes.REDSTONE, tx, ty, tz, 0, 0, 0);
		}
	}

	private boolean scanAndRevertChanges() {
		int index = 0;
		boolean reverted = false;

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					IBlockState stateThere = world.getBlockState(pos.add(x, y, z));

					if (blockData[index].getBlock() != stateThere.getBlock()) {
						if (revertBlock(pos.add(x, y, z), stateThere, blockData[index])) {
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

	private boolean revertBlock(BlockPos pos, IBlockState stateThere, IBlockState replaceWith) {
		if (stateThere.getBlock() == Blocks.AIR && !replaceWith.getMaterial().blocksMovement()) {
			return false;
		}
		if (stateThere.getBlockHardness(world, pos) < 0 || isUnrevertable(stateThere, replaceWith)) {
			return false;
		} else if (this.rand.nextInt(REVERT_CHANCE) == 0) {
			// don't revert everything instantly
			if (replaceWith.getBlock() != Blocks.AIR) {
				replaceWith = TFBlocks.tower_translucent.getDefaultState().withProperty(BlockTFTowerTranslucent.VARIANT, TowerTranslucentVariant.REVERTER_REPLACEMENT);
			}

			world.setBlockState(pos, replaceWith, 2);

			// play a little animation
			if (stateThere.getBlock() == Blocks.AIR) {
				world.playEvent(2001, pos, Block.getStateId(replaceWith));
			} else if (replaceWith.getBlock() == Blocks.AIR) {
				world.playEvent(2001, pos, Block.getStateId(stateThere));
				stateThere.getBlock().dropBlockAsItem(world, pos, stateThere, 0);
			}
		}

		return true;
	}

	private boolean isUnrevertable(IBlockState stateThere, IBlockState replaceWith) {
		if (stateThere.getBlock() == TFBlocks.tower_device || replaceWith.getBlock() == TFBlocks.tower_device) {
			return true;
		}
		if ((stateThere.getBlock() == TFBlocks.tower_translucent && stateThere.getValue(BlockTFTowerTranslucent.VARIANT) != TowerTranslucentVariant.REVERTER_REPLACEMENT)
				|| (replaceWith.getBlock() == TFBlocks.tower_translucent && replaceWith.getValue(BlockTFTowerTranslucent.VARIANT) != TowerTranslucentVariant.REVERTER_REPLACEMENT)) {
			return true;
		}
		if (stateThere.getBlock() == Blocks.REDSTONE_LAMP && replaceWith.getBlock() == Blocks.LIT_REDSTONE_LAMP) {
			return true;
		}
		if (stateThere.getBlock() == Blocks.LIT_REDSTONE_LAMP && replaceWith.getBlock() == Blocks.REDSTONE_LAMP) {
			return true;
		}
		if (stateThere.getBlock() == Blocks.WATER || replaceWith.getBlock() == Blocks.FLOWING_WATER) {
			return true;
		}
		if (stateThere.getBlock() == Blocks.FLOWING_WATER || replaceWith.getBlock() == Blocks.WATER) {
			return true;
		}
		if (replaceWith.getBlock() == Blocks.TNT) {
			return true;
		}

		List<IBlockState> blacklist = TFConfig.getAntiBuilderBlacklist();
		return blacklist.contains(stateThere) || blacklist.contains(replaceWith);
	}

	private void captureBlockData() {
		blockData = new IBlockState[diameter * diameter * diameter];

		int index = 0;

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					blockData[index] = world.getBlockState(pos.add(x, y, z));
					index++;
				}
			}
		}
	}

	private boolean anyPlayerInRange() {
		return this.world.getClosestPlayer(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, this.requiredPlayerRange, false) != null;
	}
}
