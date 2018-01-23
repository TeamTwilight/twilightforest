package twilightforest.tileentity.critters;


import net.minecraft.util.ITickable;

public class TileEntityTFMoonwormTicking extends TileEntityTFMoonworm implements ITickable {
	public int yawDelay;
	public int currentYaw;
	public int desiredYaw;

	public TileEntityTFMoonwormTicking() {
		currentYaw = -1;
		yawDelay = 0;
		desiredYaw = 0;
	}

	@Override
	public void update() {
		if (isClient) {
			if (currentYaw == -1) {
				currentYaw = world.rand.nextInt(4) * 90;
			}

			if (yawDelay > 0) {
				yawDelay--;
			} else {
				if (desiredYaw == 0) {
					// make it rotate!
					yawDelay = 200 + world.rand.nextInt(200);
					desiredYaw = world.rand.nextInt(4) * 90;
				}

				currentYaw++;

				if (currentYaw > 360) {
					currentYaw = 0;
				}

				if (currentYaw == desiredYaw) {
					desiredYaw = 0;
				}
			}
		}
	}

}
