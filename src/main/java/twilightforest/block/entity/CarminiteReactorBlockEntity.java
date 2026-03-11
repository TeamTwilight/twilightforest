package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.CarminiteReactorBlock;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.init.*;

import java.util.Optional;

public class CarminiteReactorBlockEntity extends BlockEntity {

	private final int secX;
	private final int secY;
	private final int secZ;
	private int counter = 0;
	private int terX, terY, terZ;


	public CarminiteReactorBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CARMINITE_REACTOR.get(), pos, state);
		RandomSource rand = RandomSource.create();

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

		if (!level.isDebug() && state.getValue(CarminiteReactorBlock.ACTIVE)) {
			te.counter++;

			if (!level.isClientSide()) {

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
					// deactivate & explode
					level.destroyBlock(pos, false);
					level.explode(null, TFDamageTypes.getDamageSource(level, TFDamageTypes.REACTOR), null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, Level.ExplosionInteraction.BLOCK);

					// spawn mini ghasts near the secondary & tertiary points
					for (int i = 0; i < 3; i++) {
						te.spawnGhastNear(level, pos.getX() + te.secX, pos.getY() + te.secY, pos.getZ() + te.secZ);
						te.spawnGhastNear(level, pos.getX() + te.terX, pos.getY() + te.terY, pos.getZ() + te.terZ);
					}
				}

			} else {
				if (te.counter % 5 == 0 && te.counter <= 250) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.REACTOR_AMBIENT.get(), SoundSource.BLOCKS, te.counter / 100F, te.counter / 100F, false);
				}
			}
		}
	}

	private void spawnGhastNear(Level level, int x, int y, int z) {
		CarminiteGhastling ghast = TFEntities.CARMINITE_GHASTLING.get().create(level);
		if (ghast != null) {
			ghast.moveTo(x - 1.5 + level.getRandom().nextFloat() * 3.0, y - 1.5 + level.getRandom().nextFloat() * 3.0, z - 1.5 + level.getRandom().nextFloat() * 3.0, level.getRandom().nextFloat() * 360F, 0.0F);
			level.addFreshEntity(ghast);
		}
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
							case 0 -> this.transformBlock(pos.offset(dx, dy, dz), state, fuzzY, netherTransform);
							case 1 -> this.transformBlock(pos.offset(dx, dy, -dz), state, fuzzY, netherTransform);
							case 2 -> this.transformBlock(pos.offset(-dx, dy, dz), state, fuzzY, netherTransform);
							case 3 -> this.transformBlock(pos.offset(-dx, dy, -dz), state, fuzzY, netherTransform);
							case 4 -> this.transformBlock(pos.offset(dx, -dy, dz), state, fuzzY, netherTransform);
							case 5 -> this.transformBlock(pos.offset(dx, -dy, -dz), state, fuzzY, netherTransform);
							case 6 -> this.transformBlock(pos.offset(-dx, -dy, dz), state, fuzzY, netherTransform);
							case 7 -> this.transformBlock(pos.offset(-dx, -dy, -dz), state, fuzzY, netherTransform);
						}
					}
				}
			}
		}
	}

	private void transformBlock(BlockPos pos, BlockState state, int fuzz, boolean netherTransform) {
		BlockState stateThere = this.getLevel().getBlockState(pos);

		if (stateThere.getBlock() != Blocks.AIR && (stateThere.is(BlockTagGenerator.CARMINITE_REACTOR_IMMUNE) || stateThere.getDestroySpeed(this.getLevel(), pos) == -1)) {
			// don't destroy unbreakable stuff
			return;
		}

		if (fuzz == 0 && stateThere.getBlock() != Blocks.AIR) {
			// make pop thing for original block
			this.getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(stateThere));
		}

		if (netherTransform && stateThere.getBlock() != Blocks.AIR) {
			Optional<Block> optional = BuiltInRegistries.BLOCK
				.getTag(BlockTagGenerator.CARMINITE_REACTOR_ORES)
				.flatMap(tag -> tag.getRandomElement(this.getLevel().getRandom()))
				.map(Holder::value);
			this.getLevel().setBlock(pos, (this.getLevel().getRandom().nextInt(8) == 0 && optional.isPresent() ? optional.get().defaultBlockState() : Blocks.NETHERRACK.defaultBlockState()), Block.UPDATE_ALL);
			// fire on top?
			if (this.getLevel().isEmptyBlock(pos.above()) && fuzz % 3 == 0) {
				this.getLevel().setBlock(pos.above(), Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
			}
		} else {
			this.getLevel().setBlock(pos, state, Block.UPDATE_ALL);
		}
	}

	private void createFakeBlock(BlockPos pos, BlockState state) {
		BlockState stateThere = this.getLevel().getBlockState(pos);

		// don't destroy unbreakable stuff
		if (!(stateThere.is(BlockTagGenerator.CARMINITE_REACTOR_IMMUNE) ||
				(stateThere.getDestroySpeed(this.getLevel(), pos) == -1))) {
			this.getLevel().setBlock(pos, state, Block.UPDATE_CLIENTS);
		}
	}
}
