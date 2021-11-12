package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import twilightforest.TFSounds;
import twilightforest.block.GhastTrapBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.monster.CarminiteGhastling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GhastTrapBlockEntity extends BlockEntity {

	private int counter = 0;
	private final List<CarminiteGhastling> dyingGhasts = new ArrayList<>();
	private final Random rand = new Random();

	public GhastTrapBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.GHAST_TRAP.get(), pos, state);
	}

	private static void tickInactive(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		// check to see if there are any dying mini ghasts within our scan range
		AABB aabb = new AABB(pos).inflate(10D, 16D, 10D);

		List<CarminiteGhastling> nearbyGhasts = level.getEntitiesOfClass(CarminiteGhastling.class, aabb);

		for (CarminiteGhastling ghast : nearbyGhasts) {
			if (ghast.deathTime > 0) {
				te.makeParticlesTo(ghast);

				if (!te.dyingGhasts.contains(ghast)) {
					te.dyingGhasts.add(ghast);
				}
			}
		}

		// display charge level, up to 3
		int chargeLevel = Math.min(3, te.dyingGhasts.size());

		te.counter++;

		if (level.isClientSide) {
			// occasionally make a redstone line to a mini ghast
			if (te.counter % 20 == 0 && nearbyGhasts.size() > 0) {
				CarminiteGhastling highlight = nearbyGhasts.get(te.rand.nextInt(nearbyGhasts.size()));
				te.makeParticlesTo(highlight);
			}

			if (chargeLevel >= 1 && te.counter % 10 == 0) {
				TFBlocks.GHAST_TRAP.get().sparkle(level, pos);
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}
			if (chargeLevel >= 2) {
				level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.1 + te.rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + te.rand.nextFloat() * 0.8, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05, 0.00, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05);
				if (te.counter % 10 == 0) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT, SoundSource.BLOCKS, 1.2F, 0.8F, false);
				}
			}
			if (chargeLevel >= 3) {
				level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.1 + te.rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + te.rand.nextFloat() * 0.8, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05, 0.05, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05);
				TFBlocks.GHAST_TRAP.get().sparkle(level, pos);
				if (te.counter % 5 == 0) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT, SoundSource.BLOCKS, 1.5F, 2F, false);
				}
			}
		}
	}

	private void makeParticlesTo(Entity highlight) {

		double sx = worldPosition.getX() + 0.5D;
		double sy = worldPosition.getY() + 1.0D;
		double sz = worldPosition.getZ() + 0.5D;

		double dx = sx - highlight.getX();
		double dy = sy - highlight.getY() - highlight.getEyeHeight();
		double dz = sz - highlight.getZ();

		for (int i = 0; i < 5; i++) {
			level.addParticle(TFParticleType.GHAST_TRAP.get(), sx, sy, sz, -dx, -dy, -dz);
		}
	}

	public boolean isCharged() {
		return dyingGhasts.size() >= 3;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		if (state.getValue(GhastTrapBlock.ACTIVE)) {
			tickActive(level, pos, state, te);
		} else {
			tickInactive(level, pos, state, te);
		}
	}

	@Override
	public boolean triggerEvent(int event, int payload) {
		if (event == GhastTrapBlock.ACTIVATE_EVENT) {
			counter = 0;
			dyingGhasts.clear();
			return true;
		}
		if (event == GhastTrapBlock.DEACTIVATE_EVENT) {
			counter = 0;
			return true;
		}
		return false;
	}

	private static void tickActive(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		++te.counter;

		if (level.isClientSide) {
			// smoke when done
			if (te.counter > 100 && te.counter % 4 == 0) {
				level.addParticle(TFParticleType.HUGE_SMOKE.get(), pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5, Math.cos(te.counter / 10.0) * 0.05, 0.25D, Math.sin(te.counter / 10.0) * 0.05);

			} else if (te.counter < 100) {

				double x = pos.getX() + 0.5D;
				double y = pos.getY() + 1.0D;
				double z = pos.getZ() + 0.5D;

				double dx = Math.cos(te.counter / 10.0) * 2.5;
				double dy = 20D;
				double dz = Math.sin(te.counter / 10.0) * 2.5;

				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, dx, dy, dz);
				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, -dx, dy, -dz);
				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, -dx, dy / 2, dz);
				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, dx, dy / 2, -dz);
				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, dx / 2, dy / 4, dz / 2);
				level.addParticle(TFParticleType.GHAST_TRAP.get(), x, y, z, -dx / 2, dy / 4, -dz / 2);
			}

			// appropriate sound
			if (te.counter < 30) {
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_WARMUP, SoundSource.BLOCKS, 1.0F, 4.0F, false);
			} else if (te.counter < 80) {
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_ON, SoundSource.BLOCKS, 1.0F, 4.0F, false);
			} else {
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_SPINDOWN, SoundSource.BLOCKS, 1.0F, 4.0F, false);
			}
		} else {
			// trap nearby ghasts
			AABB aabb = new AABB(pos.above(16), pos.above(16).offset(1, 1, 1)).inflate(6D, 16D, 6D);

			List<Ghast> nearbyGhasts = level.getEntitiesOfClass(Ghast.class, aabb);

			for (Ghast ghast : nearbyGhasts) {
				//stop boss tantrum
				if (ghast instanceof UrGhast urghast) {
					urghast.setInTantrum(false);
					ghast.noPhysics = true; // turn this on so we can pull it in close

					// move boss to this point
					double mx = (ghast.getX() - pos.getX() - 0.5) * -0.1;
					double my = (ghast.getY() - pos.getY() - 2.5) * -0.1;
					double mz = (ghast.getZ() - pos.getZ() - 0.5) * -0.1;
					ghast.setDeltaMovement(mx, my, mz);

					if (te.rand.nextInt(10) == 0) {
						ghast.hurt(DamageSource.GENERIC, 7);
						urghast.resetDamageUntilNextPhase();
					}

				} else {
					// move ghasts to this point
					double mx = (ghast.getX() - pos.getX() - 0.5) * -0.1;
					double my = (ghast.getY() - pos.getY() - 1.5) * -0.1;
					double mz = (ghast.getZ() - pos.getZ() - 0.5) * -0.1;
					ghast.setDeltaMovement(mx, my, mz);

					if (te.rand.nextInt(10) == 0) {
						ghast.hurt(DamageSource.GENERIC, 10);
					}
				}

				if (ghast instanceof CarminiteGhastguard) {
					((CarminiteGhastguard) ghast).setInTrap();
				}

			}

			if (te.counter >= 120) {
				level.setBlockAndUpdate(pos, state.setValue(GhastTrapBlock.ACTIVE, false));
				level.blockEvent(pos, state.getBlock(), GhastTrapBlock.DEACTIVATE_EVENT, 0);
			}
		}
	}
}
