package twilightforest.compat.rei.entries;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NbtHasher implements EntryComparator<Tag> {
	private final Predicate<String> filter;

	NbtHasher(@Nullable String[] ignoredKeys) {
		if (ignoredKeys == null || ignoredKeys.length == 0) {
			this.filter = key -> true;
		} else if (ignoredKeys.length == 1) {
			String s = ignoredKeys[0];
			this.filter = key -> !Objects.equals(s, key);
		} else {
			Set<String> set = new HashSet<>(Arrays.asList(ignoredKeys));
			this.filter = Predicates.not(set::contains);
		}
	}

	private boolean shouldHash(String key) {
		return filter.test(key);
	}

	@Override
	public long hash(ComparisonContext context, Tag value) {
		return hashTag(value);
	}

	private int hashTag(@Nullable Tag tag) {
		if (tag == null) return 0;
		if (tag instanceof ListTag list) return hashListTag(list);
		if (tag instanceof CompoundTag compound) return hashCompoundTag(compound);
		return tag.hashCode();
	}

	private int hashListTag(ListTag tag) {
		int i = tag.size();
		for (Tag innerTag : tag) {
			i = i * 31 + hashTag(innerTag);
		}
		return i;
	}

	private int hashCompoundTag(CompoundTag tag) {
		int i = 1;
		for (Map.Entry<String, Tag> entry : tag.tags.entrySet()) {
			if (shouldHash(entry.getKey())) {
				i = i * 31 + (Objects.hashCode(entry.getKey()) ^ hashTag(entry.getValue()));
			}
		}
		return i;
	}
}
