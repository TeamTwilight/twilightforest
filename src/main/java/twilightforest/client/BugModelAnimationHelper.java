package twilightforest.client;

import java.util.Random;

public class BugModelAnimationHelper {
    private static Random rand = new Random();

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
        if (currentRotation == -1) {
            currentRotation = rand.nextInt(4) * 90;
        }

        if (yawDelay > 0) {
            yawDelay--;
        } else {
            if (currentYaw == 0 && desiredYaw == 0) {
                // make it rotate!
                yawDelay = 200 + rand.nextInt(200);
                desiredYaw = rand.nextInt(15) - rand.nextInt(15);
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
                glowDelay = rand.nextInt(50);
            }
        }

        if (yawWriggleDelay > 0) {
            yawWriggleDelay--;
        } else {
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
}
