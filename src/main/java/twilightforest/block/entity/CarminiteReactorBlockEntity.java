package twilightforest.block.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import twilightforest.TFSounds;
import twilightforest.block.CarminiteReactorBlock;
import twilightforest.block.TFBlocks;
import twilightforest.data.BlockTagGenerator;
import twilightforest.entity.TFEntities;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.util.TFDamageSources;

import java.util.Random;

public class CarminiteReactorBlockEntity extends BlockEntity {

	private int counter = 0;

	private int secX, secY, secZ;
	private int terX, terY, terZ;


	public CarminiteReactorBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CARMINITE_REACTOR.get(), pos, state);
		Random rand = new Random();

		// determine the two smaller bursts
		this.secX = 3 * (rand.nextBoolean() ? 1 : -1);
		this.secY = 3 * (rand.nextBoolean() ? 1 : -1);
		this.secZ = 3 * (rand.nextBoolean() ? 1 : -1);

		this.terX = 3 * (rand.nextBoolean() ? 1 : -1);
		this.terY = 3 * (rand.nextBoolean() ? 1 : -1);
		this.terZ = 3 * (rand.nextBoolean() ? 1 : -1);

		if (secX == terX && secY == terY && secZ == terZ) {
			terX = -terX;
			terY = -terY;
			terZ = -terZ;
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CarminiteReactorBlockEntity te) {

		if(state.getValue(CarminiteReactorBlock.ACTIVE)) {
			te.counter++;

			if (!level.isClientSide) {

				// every 2 seconds for 10 seconds, destroy a new radius
				int offset = 10;

				if (te.counter % 5 == 0) {
					if (te.counter == 5) {
						BlockState fakeGold = TFBlocks.FAKE_GOLD.get().defaultBlockState();
						BlockState fakeDiamond = TFBlocks.FAKE_DIAMOND.get().defaultBlockState();

						// transformation!
						te.createFakeBlock(pos.offset(1, 1, 1), fakeDiamond);
						te.createFakeBlock(pos.offset(1, 1, -1), fakeDiamond);
						te.createFakeBlock(pos.offset(-1, 1, 1), fakeDiamond);
						te.createFakeBlock(pos.offset(-1, 1, -1), fakeDiamond);
						te.createFakeBlock(pos.offset(0, 1, 0), fakeDiamond);

						te.createFakeBlock(pos.offset(0, 1, 1), fakeGold);
						te.createFakeBlock(pos.offset(0, 1, -1), fakeGold);
						te.createFakeBlock(pos.offset(1, 1, 0), fakeGold);
						te.createFakeBlock(pos.offset(-1, 1, 0), fakeGold);

						te.createFakeBlock(pos.offset(1, 0, 1), fakeGold);
						te.createFakeBlock(pos.offset(1, 0, -1), fakeGold);
						te.createFakeBlock(pos.offset(-1, 0, 1), fakeGold);
						te.createFakeBlock(pos.offset(-1, 0, -1), fakeGold);

						te.createFakeBlock(pos.offset(0, 0, 1), fakeDiamond);
						te.createFakeBlock(pos.offset(0, 0, -1), fakeDiamond);
						te.createFakeBlock(pos.offset(1, 0, 0), fakeDiamond);
						te.createFakeBlock(pos.offset(-1, 0, 0), fakeDiamond);
						te.createFakeBlock(pos.offset(0, -1, 0), fakeDiamond);

						te.createFakeBlock(pos.offset(1, -1, 1), fakeDiamond);
						te.createFakeBlock(pos.offset(1, -1, -1), fakeDiamond);
						te.createFakeBlock(pos.offset(-1, -1, 1), fakeDiamond);
						te.createFakeBlock(pos.offset(-1, -1, -1), fakeDiamond);

						te.createFakeBlock(pos.offset(0, -1, 1), fakeGold);
						te.createFakeBlock(pos.offset(0, -1, -1), fakeGold);
						te.createFakeBlock(pos.offset(1, -1, 0), fakeGold);
						te.createFakeBlock(pos.offset(-1, -1, 0), fakeGold);

					}


					// primary burst
					int primary = te.counter - 80;

					if (primary >= offset && primary <= 249) {
						te.drawBlob(pos, (primary - offset) / 40, Blocks.AIR.defaultBlockState(), primary - offset, false);
					}
					if (primary <= 200) {
						te.drawBlob(pos, primary / 40, TFBlocks.REACTOR_DEBRIS.get().defaultBlockState(), te.counter, false);
					}

					// secondary burst
					int secondary = te.counter - 120;

					if (secondary >= offset && secondary <= 129) {
						te.drawBlob(pos.offset(te.secX, te.secY, te.secZ), (secondary - offset) / 40, Blocks.AIR.defaultBlockState(), secondary - offset, false);
					}
					if (secondary >= 0 && secondary <= 160) {
						te.drawBlob(pos.offset(te.secX, te.secY, te.secZ), secondary / 40, Blocks.AIR.defaultBlockState(), secondary, true);
					}

					// tertiary burst
					int tertiary = te.counter - 160;

					if (tertiary >= offset && tertiary <= 129) {
						te.drawBlob(pos.offset(te.terX, te.terY, te.terZ), (tertiary - offset) / 40, Blocks.AIR.defaultBlockState(), tertiary - offset, false);
					}
					if (tertiary >= 0 && tertiary <= 160) {
						te.drawBlob(pos.offset(te.terX, te.terY, te.terZ), tertiary / 40, Blocks.AIR.defaultBlockState(), tertiary, true);
					}

				}


				if (te.counter >= 350) {
					// spawn mini ghasts near the secondary & tertiary points
					for (int i = 0; i < 3; i++) {
						te.spawnGhastNear(pos.getX() + te.secX, pos.getY() + te.secY, pos.getZ() + te.secZ);
						te.spawnGhastNear(pos.getX() + te.terX, pos.getY() + te.terY, pos.getZ() + te.terZ);
					}

					// deactivate & explode
					level.explode(null, TFDamageSources.REACTOR, (ExplosionDamageCalculator) null, pos.getX(), pos.getY(), pos.getZ(), 2.0F, true, Explosion.BlockInteraction.BREAK);
					level.removeBlock(pos, false);
				}

			} else {
				if (te.counter % 5 == 0 && te.counter <= 250) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.REACTOR_AMBIENT, SoundSource.BLOCKS, te.counter / 100F, te.counter / 100F, false);
				}
			}
		}
	}


	private void spawnGhastNear(int x, int y, int z) {
		CarminiteGhastling ghast = new CarminiteGhastling(TFEntities.CARMINITE_GHASTLING, level);
		ghast.moveTo(x - 1.5 + level.random.nextFloat() * 3.0, y - 1.5 + level.random.nextFloat() * 3.0, z - 1.5 + level.random.nextFloat() * 3.0, level.random.nextFloat() * 360F, 0.0F);
		level.addFreshEntity(ghast);
	}

	private void drawBlob(BlockPos pos, int rad, BlockState state, int fuzz, boolean netherTransform) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			// transform fuzz
			int fuzzX = (fuzz + dx) % 8;

			for (byte dy = 0; dy <= rad; dy++) {
				int fuzzY = (fuzz + dy) % 8;

				for (byte dz = 0; dz <= rad; dz++) {
					// determine how far we are from the center.
					byte dist;
					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}

					// if we're inside the blob, fill it
					if (dist == rad && !(dx == 0 && dy == 0 && dz == 0)) {
						// do eight at a time for easiness!
						switch (fuzzX) {
							case 0:
								transformBlock(pos.offset(dx, dy, dz), state, fuzzY, netherTransform);
								break;
							case 1:
								transformBlock(pos.offset(dx, dy, -dz), state, fuzzY, netherTransform);
								break;
							case 2:
								transformBlock(pos.offset(-dx, dy, dz), state, fuzzY, netherTransform);
								break;
							case 3:
								transformBlock(pos.offset(-dx, dy, -dz), state, fuzzY, netherTransform);
								break;
							case 4:
								transformBlock(pos.offset(dx, -dy, dz), state, fuzzY, netherTransform);
								break;
							case 5:
								transformBlock(pos.offset(dx, -dy, -dz), state, fuzzY, netherTransform);
								break;
							case 6:
								transformBlock(pos.offset(-dx, -dy, dz), state, fuzzY, netherTransform);
								break;
							case 7:
								transformBlock(pos.offset(-dx, -dy, -dz), state, fuzzY, netherTransform);
								break;
						}
					}
				}
			}
		}
	}

	private void transformBlock(BlockPos pos, BlockState state, int fuzz, boolean netherTransform) {
		BlockState stateThere = level.getBlockState(pos);

		if (stateThere.getBlock() != Blocks.AIR && (stateThere.is(BlockTagGenerator.CARMINITE_REACTOR_IMMUNE) || stateThere.getDestroySpeed(level, pos) == -1)) {
			// don't destroy unbreakable stuff
			return;
		}

		if (fuzz == 0 && stateThere.getBlock() != Blocks.AIR) {
			// make pop thing for original block
			level.levelEvent(2001, pos, Block.getId(stateThere));
		}

		if (netherTransform && stateThere.getBlock() != Blocks.AIR) {
			level.setBlock(pos, (level.random.nextInt(8) == 0 ? Blocks.NETHER_QUARTZ_ORE.defaultBlockState() : Blocks.NETHERRACK.defaultBlockState()), 3);
			// fire on top?
			if (level.isEmptyBlock(pos.above()) && fuzz % 3 == 0) {
				level.setBlock(pos.above(), Blocks.FIRE.defaultBlockState(), 3);
			}
		} else {
			level.setBlock(pos, state, 3);
		}
	}

	private void createFakeBlock(BlockPos pos, BlockState state) {
		BlockState stateThere = level.getBlockState(pos);

		// don't destroy unbreakable stuff
		if (stateThere.getBlock() != Blocks.AIR &&
				!(stateThere.is(BlockTagGenerator.CARMINITE_REACTOR_IMMUNE) ||
						(stateThere.getDestroySpeed(level, pos) == -1))) {
			level.setBlock(pos, state, 2);
		}
	}
}
