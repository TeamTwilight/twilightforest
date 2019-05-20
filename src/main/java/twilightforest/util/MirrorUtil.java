package twilightforest.util;

import net.minecraft.util.Mirror;

import java.util.Random;

public class MirrorUtil {
    private static final Mirror[] MIRRORS = { Mirror.NONE, Mirror.LEFT_RIGHT, Mirror.FRONT_BACK };

    public static Mirror getRandomMirror(Random random) {
        return MIRRORS[random.nextInt(MIRRORS.length)];
    }
}