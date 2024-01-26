package twilightforest.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/**
 * Special iterator for "zip"-merging two iterators into a single iterable, for usage in for-loops.
 * Continues until both Iterators are exhausted.
 * Take special care to not interact with either iterator instances, once passed into this class.
 */
public class ZippedIterator<E> implements Iterator<E>, Iterable<E> {
    private final Iterator<E> first;
    private final Iterator<E> second;

    private boolean firstNext;

    public static <E> ZippedIterator<E> fromIterables(Iterable<E> first, Iterable<E> second) {
        return new ZippedIterator<>(first.iterator(), second.iterator());
    }

    public ZippedIterator(Iterator<E> first, Iterator<E> second) {
        this.first = first;
        this.second = second;

        this.firstNext = first.hasNext();
    }

    @Override
    public boolean hasNext() {
        return this.first.hasNext() || this.second.hasNext();
    }

    @Override
    public E next() {
        Iterator<E> current = this.firstNext ? this.first : this.second;
        Iterator<E> other = this.firstNext ? this.second : this.first;

        // Should the other iterator be switched to? Flip iterator selector if so
        if (other.hasNext()) this.firstNext = !this.firstNext;
        // If the current iterator is also empty, then this iterator's hasNext() will become false, causing termination

        return current.next();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this;
    }

    // For in-IDE testing
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    public static void main(String... args) {
        List<String> animals = List.of("Aardvark", "Baboon", "Capybara", "Dolphin", "Elephant");
        List<String> fruits = List.of("Apple", "Banana", "Cantaloupe");
        List<String> empty = List.of();

        {
            // Slightly unbalanced lists, testing tolerance for asymmetry

            StringJoiner stringListConcat = new StringJoiner(", ");

            for (String somethingAlive : ZippedIterator.fromIterables(animals, fruits)) {
                System.out.println(somethingAlive);
                stringListConcat.add(somethingAlive);
            }

            assert "Aardvark, Apple, Baboon, Banana, Capybara, Cantaloupe, Dolphin, Elephant".equals(stringListConcat.toString());
        }

        {
            // Testing critically unbalanced iterators, empty being either first or second

            StringJoiner stringListConcatPartial = new StringJoiner(", ");

            for (String animal : ZippedIterator.fromIterables(animals, empty)) {
                stringListConcatPartial.add(animal);
            }

            assert "Aardvark, Baboon, Capybara, Dolphin, Elephant".equals(stringListConcatPartial.toString());

            for (String fruit : ZippedIterator.fromIterables(empty, fruits)) {
                stringListConcatPartial.add(fruit);
            }

            assert "Aardvark, Baboon, Capybara, Dolphin, Elephant, Apple, Banana, Cantaloupe".equals(stringListConcatPartial.toString());
        }

        System.out.println("TESTS PASSED");
    }
}
