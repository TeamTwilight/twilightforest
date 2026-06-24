package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

public class CicadaBlockEntity extends BlockEntity {
	public final float randRot = RandomSource.create().nextInt(4) * 90.0F;
	public int currentYaw;
	private int yawDelay;
	private int desiredYaw;
	private int singDuration;
	private boolean singing;
	private int singDelay;

	public CicadaBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CICADA.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CicadaBlockEntity te) {
		if (level.isClientSide()) {
			if (te.yawDelay > 0) {
				te.yawDelay--;
			} else {
				if (te.currentYaw == 0 && te.desiredYaw == 0) {
					// make it rotate!
					te.yawDelay = 200 + level.getRandom().nextInt(200);
					te.desiredYaw = level.getRandom().nextInt(15) - level.getRandom().nextInt(15);
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
				if (te.singing) {
					te.singDuration++;
					te.doSingAnimation();
				}
				if (!te.singing && te.singDuration <= 0) {
					te.singing = true;
					te.singDelay = 100 + level.getRandom().nextInt(100);
				}
			}
		}
	}

	private void doSingAnimation() {
		if (this.getLevel().getRandom().nextInt(5) == 0) {
			double rx = this.getBlockPos().getX() + this.getLevel().getRandom().nextFloat();
			double ry = this.getBlockPos().getY() + this.getLevel().getRandom().nextFloat();
			double rz = this.getBlockPos().getZ() + this.getLevel().getRandom().nextFloat();
			this.getLevel().addParticle(ParticleTypes.NOTE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}

	private void playSong() {
		if (!TFConfig.silentCicadas) {
			this.getLevel().playLocalSound(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), TFSounds.CICADA.get(), SoundSource.NEUTRAL, 1.0f, (this.getLevel().getRandom().nextFloat() - this.getLevel().getRandom().nextFloat()) * 0.2F + 1.0F, false);
		}
	}
}
