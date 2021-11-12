package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.client.particle.TFParticleType;

public class FireflyBlockEntity extends BlockEntity {

	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	public float glowIntensity;
	private boolean glowing;
	private int glowDelay;

	public FireflyBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.FIREFLY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FireflyBlockEntity te) {
		if (level.isClientSide) {
			if (te.anyPlayerInRange() && level.random.nextInt(20) == 0) {
				te.spawnParticles();
			}

			if (te.yawDelay > 0) {
				te.yawDelay--;
			} else {
				if (te.currentYaw == 0 && te.desiredYaw == 0) {
					// make it rotate!
					te.yawDelay = 200 + level.random.nextInt(200);
					te.desiredYaw = level.random.nextInt(15) - level.random.nextInt(15);
				}

				if (te.currentYaw < te.desiredYaw) {
					te.currentYaw++;
				}
				if (te.currentYaw > te.desiredYaw) {
					te.currentYaw--;
				}
				if (te.currentYaw == te.desiredYaw) {
					te.desiredYaw = 0;
				}
			}

			if (te.glowDelay > 0) {
				te.glowDelay--;
			} else {
				if (te.glowing && te.glowIntensity >= 1.0) {
					te.glowing = false;
				}
				if (te.glowing && te.glowIntensity < 1.0) {
					te.glowIntensity += 0.05;
				}
				if (!te.glowing && te.glowIntensity > 0) {
					te.glowIntensity -= 0.05;
				}
				if (!te.glowing && te.glowIntensity <= 0) {
					te.glowing = true;
					te.glowDelay = level.random.nextInt(50);
				}
			}
		}
	}

	private boolean anyPlayerInRange() {
		return level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, 16D, false) != null;
	}

	private void spawnParticles() {
		double rx = worldPosition.getX() + level.random.nextFloat();
		double ry = worldPosition.getY() + level.random.nextFloat();
		double rz = worldPosition.getZ() + level.random.nextFloat();
//    	ModLoader.getMinecraftInstance().effectRenderer.addEffect(fireflyfx);
		// ^ keeping here only for pure lolz
		level.addParticle(TFParticleType.FIREFLY.get(), rx, ry, rz, 0, 0, 0);
	}
}
