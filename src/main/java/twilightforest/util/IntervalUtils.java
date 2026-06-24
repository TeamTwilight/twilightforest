package twilightforest.util;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class IntervalUtils {
	public static List<Pair<Integer, Integer>> mergeAndSortIntervals(List<Pair<Integer, Integer>> intervals) {
		if (intervals.isEmpty()) return intervals;

		intervals.sort(Comparator.comparingInt(Pair::getFirst));

		List<Pair<Integer, Integer>> merged = new ArrayList<>();
		Pair<Integer, Integer> current = intervals.getFirst();

		for (int i = 1; i < intervals.size(); i++) {
			Pair<Integer, Integer> next = intervals.get(i);
			if (current.getSecond() >= next.getFirst()) {
				current = Pair.of(current.getFirst(), Math.max(current.getSecond(), next.getSecond()));
			} else {
				merged.add(current);
				current = next;
			}
		}
		merged.add(current);
		return merged;
	}

	// subtracts intervals in sortedB from sortedA and returns the result
	public static List<Pair<Integer, Integer>> subtractIntervals(List<Pair<Integer, Integer>> sortedA, List<Pair<Integer, Integer>> sortedB) {
		List<Pair<Integer, Integer>> result = new ArrayList<>();
		int i = 0, j = 0;

		while (i < sortedA.size() && j < sortedB.size()) {
			Pair<Integer, Integer> a = sortedA.get(i);
			Pair<Integer, Integer> b = sortedB.get(j);

			if (a.getSecond() <= b.getFirst()) {
				result.add(a);
				i++;
			}
			else if (a.getFirst() >= b.getSecond()) {
				j++;
			}
			else {
				if (a.getFirst() < b.getFirst()) {
					result.add(Pair.of(a.getFirst(), b.getFirst()));
				}
				if (a.getSecond() <= b.getSecond()) {
					i++;
				} else {
					sortedA.set(i, Pair.of(b.getSecond(), a.getSecond()));
					j++;
				}
			}
		}

		while (i < sortedA.size()) {
			result.add(sortedA.get(i++));
		}
		return result;
	}
}
