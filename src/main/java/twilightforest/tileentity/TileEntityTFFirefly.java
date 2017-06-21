package twilightforest.tileentity;

import twilightforest.entity.passive.EntityTFTinyFirefly;

public class TileEntityTFFirefly extends TileEntityTFCritter {
	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	public float glowIntensity;
	private boolean glowing;
	private int glowDelay;

	@Override
	public void update() {
		super.update();

		if (anyPlayerInRange() && world.rand.nextInt(20) == 0) {
			doFireflyFX();
		}

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

		if (glowDelay > 0) {
			glowDelay--;
		} else {
			if (glowing && glowIntensity >= 1.0) {
				glowing = false;
			}
			if (glowing && glowIntensity < 1.0) {
				glowIntensity += 0.05;
			}
			if (!glowing && glowIntensity > 0) {
				glowIntensity -= 0.05;
			}
			if (!glowing && glowIntensity <= 0) {
				glowing = true;
				glowDelay = world.rand.nextInt(50);
			}
		}
	}

	private boolean anyPlayerInRange() {
		return world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 16D, false) != null;
	}

	private void doFireflyFX() {
		double rx = pos.getX() + world.rand.nextFloat();
		double ry = pos.getY() + world.rand.nextFloat();
		double rz = pos.getZ() + world.rand.nextFloat();
//    	ModLoader.getMinecraftInstance().effectRenderer.addEffect(fireflyfx);
		// ^ keeping here only for pure lolz
		EntityTFTinyFirefly tinyfly = new EntityTFTinyFirefly(world, rx, ry, rz);
		world.addWeatherEffect(tinyfly);
	}


}
