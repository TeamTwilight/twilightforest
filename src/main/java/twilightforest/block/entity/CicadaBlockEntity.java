package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFConfig;
import twilightforest.TFSounds;

public class CicadaBlockEntity extends BlockEntity {
	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	private int singDuration;
	private boolean singing;
	private int singDelay;

	public CicadaBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CICADA.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CicadaBlockEntity te) {
		if (level.isClientSide) {
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

			if (te.singDelay > 0) {
				te.singDelay--;
			} else {
				if (te.singing && te.singDuration == 0) {
					te.playSong();
				}
				if (te.singing && te.singDuration >= 100) {
					te.singing = false;
					te.singDuration = 0;
				}
				if (te.singing && te.singDuration < 100) {
					te.singDuration++;
					te.doSingAnimation();
				}
				if (!te.singing && te.singDuration <= 0) {
					te.singing = true;
					te.singDelay = 100 + level.random.nextInt(100);
				}
			}
		}
	}

	private void doSingAnimation() {
		if (level.random.nextInt(5) == 0) {
			double rx = worldPosition.getX() + level.random.nextFloat();
			double ry = worldPosition.getY() + level.random.nextFloat();
			double rz = worldPosition.getZ() + level.random.nextFloat();
			level.addParticle(ParticleTypes.NOTE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}

	private void playSong() {
		if (!TFConfig.CLIENT_CONFIG.silentCicadas.get()) {
			level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), TFSounds.CICADA, SoundSource.NEUTRAL, 1.0f, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F, false);
		}
	}
}
