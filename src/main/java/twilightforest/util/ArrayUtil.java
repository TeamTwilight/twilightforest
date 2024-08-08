package twilightforest.util;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Class for handling array accesses
public final class ArrayUtil {
	private ArrayUtil() {
	}

	public static <T> T clamped(T[] array, int index) {
		return array[Mth.clamp(index, 0, array.length - 1)];
	}

	// TODO: handle negatives
	public static <T> T wrapped(T[] array, int index) {
		return array[index % array.length];
	}

	@Nullable
	public static <T> T orNull(T[] array, int index) {
		if (index >= 0 && index < array.length) {
			return array[index];
		} else {
			return null;
		}
	}

	@Nullable
	public static <T> T randomOrNull(T@Nullable[] array, RandomSource random) {
		return array == null || array.length == 0 ? null : Util.getRandom(array, random);
	}

	public static <T> List<T> safeShuffledCopy(T@Nullable[] array, RandomSource random) {
		return array == null ? List.of() : Util.shuffledCopy(array, random);
	}
}
