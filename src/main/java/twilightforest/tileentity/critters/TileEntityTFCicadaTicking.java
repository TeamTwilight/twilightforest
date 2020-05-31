package twilightforest.tileentity.critters;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFCicadaTicking extends TileEntity implements ITickableTileEntity {
	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	private int singDuration;
	private boolean singing;
	private int singDelay;

	public TileEntityTFCicadaTicking() {
		super(TFTileEntities.CICADA.get());
	}

	@Override
	public void tick() {
		if (world.isRemote) {
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
			world.addParticle(ParticleTypes.NOTE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}

	private void playSong() {
		if (!TFConfig.CLIENT_CONFIG.silentCicadas.get()) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), TFSounds.CICADA, SoundCategory.NEUTRAL, 1.0f, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F, false);
		}
	}
}
