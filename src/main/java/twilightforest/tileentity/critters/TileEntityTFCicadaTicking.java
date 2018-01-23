package twilightforest.tileentity.critters;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import twilightforest.TFConfig;
import twilightforest.TFSounds;


public class TileEntityTFCicadaTicking extends TileEntityTFCicada implements ITickable {
	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	private int singDuration;
	private boolean singing;
	private int singDelay;

	@Override
	public void update() {
		if (isClient) {
			if (yawDelay > 0) {
				yawDelay--;
			} else {
				if (currentYaw == 0 && desiredYaw == 0) {
					// make it rotate!
					yawDelay = 200 + world.rand.nextInt(200);
					desiredYaw = world.rand.nextInt(15) - world.rand.nextInt(15);
				}

				if (currentYaw < desiredYaw) {
					currentYaw++;
				}
				if (currentYaw > desiredYaw) {
					currentYaw--;
				}
				if (currentYaw == desiredYaw) {
					desiredYaw = 0;
				}
			}

			if (singDelay > 0) {
				singDelay--;
			} else {
				if (singing && singDuration == 0) {
					playSong();
				}
				if (singing && singDuration >= 100) {
					singing = false;
					singDuration = 0;
				}
				if (singing && singDuration < 100) {
					singDuration++;
					doSingAnimation();
				}
				if (!singing && singDuration <= 0) {
					singing = true;
					singDelay = 100 + world.rand.nextInt(100);
				}
			}
		}
	}

	private void doSingAnimation() {
		if (world.rand.nextInt(5) == 0) {
			double rx = pos.getX() + world.rand.nextFloat();
			double ry = pos.getY() + world.rand.nextFloat();
			double rz = pos.getZ() + world.rand.nextFloat();
			world.spawnParticle(EnumParticleTypes.NOTE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}

	private void playSong() {
		if (!TFConfig.silentCicadas) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), TFSounds.CICADA, SoundCategory.NEUTRAL, 1.0f, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F, false);
		}
	}
}
