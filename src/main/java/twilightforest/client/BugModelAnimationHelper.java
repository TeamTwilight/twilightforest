package twilightforest.client;

import net.minecraft.util.RandomSource;

public class BugModelAnimationHelper {

	private static final RandomSource rand = RandomSource.create();

	private static int yawDelay;
	public static int currentYaw;
	private static int desiredYaw;

	public static int yawWriggleDelay;
	public static int currentRotation = -1;
	public static int desiredRotation;

	public static float glowIntensity;
	private static boolean glowing;
	private static int glowDelay;

	static void animate() {
		tickYaw();
		tickGlow();
		tickRotation();
	}

	private static void tickYaw() {
		if (yawDelay > 0) {
			yawDelay--;
			return;
		}

		if (currentYaw == 0 && desiredYaw == 0) {
			// make it rotate!
			yawDelay = 200 + rand.nextInt(200);
			desiredYaw = rand.nextInt(15) - rand.nextInt(15);
		}

		if (currentYaw < desiredYaw) {
			currentYaw++;
		} else if (currentYaw > desiredYaw) {
			currentYaw--;
		} else if (currentYaw == desiredYaw) {
			desiredYaw = 0;
		}
	}

	private static void tickGlow() {
		if (glowDelay > 0) {
			glowDelay--;
			return;
		}

		if (glowing && glowIntensity >= 1f) {
			glowing = false;
		}
		if (glowing && glowIntensity < 1f) {
			glowIntensity += 0.05f;
		}
		if (!glowing && glowIntensity > 0f) {
			glowIntensity -= 0.05f;
		}
		if (!glowing && glowIntensity <= 0f) {
			glowing = true;
			glowDelay = rand.nextInt(50);
		}
	}

	private static void tickRotation() {
		if (yawWriggleDelay > 0) {
			yawWriggleDelay--;
			return;
		}

		if (currentRotation == -1) {
			currentRotation = rand.nextInt(4) * 90;
		}

		if (desiredRotation == 0) {
			// make it rotate!
			yawWriggleDelay = 200 + rand.nextInt(200);
			desiredRotation = rand.nextInt(4) * 90;
		}

		currentRotation++;

		if (currentRotation > 360) {
			currentRotation = 0;
		}

		if (currentRotation == desiredRotation) {
			desiredRotation = 0;
		}
	}
}
