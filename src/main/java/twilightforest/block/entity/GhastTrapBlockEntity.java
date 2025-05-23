package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.GhastTrapBlock;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import java.util.ArrayList;
import java.util.List;

public class GhastTrapBlockEntity extends BlockEntity {

	private final List<CarminiteGhastling> dyingGhasts = new ArrayList<>();
	private final RandomSource rand = RandomSource.create();
	private int counter = 0;

	public GhastTrapBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.GHAST_TRAP.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		if (!level.isDebug()) {
			if (state.getValue(GhastTrapBlock.ACTIVE)) te.tickActive(level, pos, state, te);
			else te.tickInactive(level, pos, state, te);
		}
	}

	private void tickInactive(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		// check to see if there are any dying mini ghasts within our scan range
		AABB aabb = new AABB(pos).inflate(10D, 16D, 10D);
		List<CarminiteGhastling> nearbyGhasts = level.getEntitiesOfClass(CarminiteGhastling.class, aabb);

		for (CarminiteGhastling ghast : nearbyGhasts) {
			if (ghast.deathTime > 0) {
				te.makeParticlesTo(level, ghast.getEyePosition());
				if (!te.dyingGhasts.contains(ghast)) te.dyingGhasts.add(ghast);
			}
		}

		// display charge level, up to 3
		int chargeLevel = Math.min(3, te.dyingGhasts.size());
		te.counter++;

		if (level.isClientSide()) {
			// occasionally make a redstone line to a mini ghast
			if (te.counter % 20 == 0 && !nearbyGhasts.isEmpty()) {
				te.makeParticlesTo(level, nearbyGhasts.get(te.rand.nextInt(nearbyGhasts.size())).getEyePosition());
			}
			if (chargeLevel >= 1 && te.counter % 10 == 0) {
				TFBlocks.GHAST_TRAP.get().sparkle(level, pos);
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}
			if (chargeLevel >= 2) {
				level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.1 + te.rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + te.rand.nextFloat() * 0.8, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05, 0.00, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05);
				if (te.counter % 10 == 0) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT.get(), SoundSource.BLOCKS, 1.2F, 0.8F, false);
				}
			}
			if (chargeLevel == 3) {
				level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.1 + te.rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + te.rand.nextFloat() * 0.8, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05, 0.05, (te.rand.nextFloat() - te.rand.nextFloat()) * 0.05);
				TFBlocks.GHAST_TRAP.get().sparkle(level, pos);
				if (te.counter % 5 == 0) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_AMBIENT.get(), SoundSource.BLOCKS, 1.5F, 2F, false);
				}
			}
		}
	}

	private void makeParticlesTo(Level level, Vec3 to) {
		Vec3 top = Vec3.atBottomCenterOf(this.getBlockPos().above());
		Vec3 diff = top.subtract(to);

		for (int i = 0; i < 5; i++) {
			level.addParticle(TFParticleType.GHAST_TRAP.get(), top.x, top.y, top.z, -diff.x, -diff.y, -diff.z);
		}
	}

	public boolean isCharged() {
		return this.dyingGhasts.size() >= 3;
	}

	@Override
	public boolean triggerEvent(int event, int payload) {
		if (event == GhastTrapBlock.ACTIVATE_EVENT) {
			this.counter = 0;
			this.dyingGhasts.clear();
			return true;
		}
		if (event == GhastTrapBlock.DEACTIVATE_EVENT) {
			this.counter = 0;
			return true;
		}
		return false;
	}

	private void tickActive(Level level, BlockPos pos, BlockState state, GhastTrapBlockEntity te) {
		++te.counter;

		if (!(level instanceof ServerLevel serverLevel)) {
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
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_WARMUP.get(), SoundSource.BLOCKS, 1.0F, 4.0F, false);
			} else if (te.counter < 80) {
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_ON.get(), SoundSource.BLOCKS, 1.0F, 4.0F, false);
			} else {
				level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.GHAST_TRAP_SPINDOWN.get(), SoundSource.BLOCKS, 1.0F, 4.0F, false);
			}
		} else {
			// trap nearby ghasts
			AABB aabb = new AABB(pos.above(16).getCenter(), pos.above(16).offset(1, 1, 1).getCenter()).inflate(6D, 16D, 6D);
			List<Mob> nearbyGhasts = level.getEntitiesOfClass(Mob.class, aabb, mob -> mob instanceof Ghast || mob instanceof UrGhast);

			for (Mob ghast : nearbyGhasts) {
				//stop boss tantrum
				if (ghast instanceof UrGhast urghast) {
					urghast.setInTantrum(false);
					urghast.setInTrap();
					ghast.noPhysics = true; // turn this on so we can pull it in close

					// move boss to this point
					double mx = (ghast.getX() - pos.getX() - 0.5) * -0.1;
					double my = (ghast.getY() - pos.getY() - 2.5) * -0.1;
					double mz = (ghast.getZ() - pos.getZ() - 0.5) * -0.1;
					ghast.setDeltaMovement(mx, my, mz);

					if (te.rand.nextInt(10) == 0) {
						ghast.hurtServer(serverLevel, serverLevel.damageSources().generic(), 7);
						urghast.resetDamageUntilNextPhase();
					}

				} else {
					// move ghasts to this point
					double mx = (ghast.getX() - pos.getX() - 0.5) * -0.1;
					double my = (ghast.getY() - pos.getY() - 1.5) * -0.1;
					double mz = (ghast.getZ() - pos.getZ() - 0.5) * -0.1;
					ghast.setDeltaMovement(mx, my, mz);

					if (te.rand.nextInt(10) == 0) {
						ghast.hurtServer(serverLevel, serverLevel.damageSources().generic(), 10);
					}
				}

			}

			if (te.counter >= 120) {
				level.setBlockAndUpdate(pos, state.setValue(GhastTrapBlock.ACTIVE, false));
				level.blockEvent(pos, state.getBlock(), GhastTrapBlock.DEACTIVATE_EVENT, 0);
			}
		}
	}
}
