package twilightforest.util.iterators;

import net.neoforged.neoforge.common.util.ConcatenatedListView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class ZippedIteratorTests {
	private final List<String> animals = List.of("Aardvark", "Baboon", "Capybara", "Dolphin", "Elephant");
	private final List<String> fruits = List.of("Apple", "Banana", "Cantaloupe");
	private final List<String> empty = List.of();
	private final List<String> zipped = List.of("Aardvark", "Apple", "Baboon", "Banana", "Capybara", "Cantaloupe", "Dolphin", "Elephant");

	@Test
	public void testUnbalancedLists() {
		// Slightly unbalanced lists, testing tolerance for input size asymmetry

		List<String> zipped = new ArrayList<>();

		for (String somethingAlive : ZippedIterator.fromIterables(this.animals, this.fruits)) {
			zipped.add(somethingAlive);
		}

		assertLinesMatch(this.zipped, zipped);
	}

	@Test
	public void testEmptyLists() {
		// Critically unbalanced iterators, empty being first, second, or both

		List<String> collected = new ArrayList<>();

		for (String animal : ZippedIterator.fromIterables(this.empty, this.empty)) {
			collected.add(animal);
		}

		assertLinesMatch(this.empty, collected);

		for (String animal : ZippedIterator.fromIterables(this.animals, this.empty)) {
			collected.add(animal);
		}

		assertLinesMatch(this.animals, collected);

		for (String fruit : ZippedIterator.fromIterables(this.empty, this.fruits)) {
			collected.add(fruit);
		}

		assertLinesMatch(ConcatenatedListView.of(this.animals, this.fruits), collected);
	}
}
