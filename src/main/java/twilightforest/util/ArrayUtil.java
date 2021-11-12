package twilightforest.util;

import net.minecraft.util.Mth;

// Class for handling array accesses
public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static <T> T clamped(T[] array, int index) {
        return array[Mth.clamp(index, 0, array.length)];
    }

    public static <T> T wrapped(T[] array, int index) {
        return array[index % array.length];
    }
}
