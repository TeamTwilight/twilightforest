package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparatorRegistry;
import me.shedaniel.rei.impl.Internals;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface EntityComparator extends EntryComparatorRegistry<Entity, EntityType<?>> {

	static EntryComparator<Entity> entityTypeNbt() {
		EntryComparator<Tag> nbt = new NbtHasher(new String[0]);
		return (context, entity) -> {
			CompoundTag tag = entity.saveWithoutId(new CompoundTag());
			return tag.isEmpty() ? 0L : nbt.hash(context, tag);
		};
	}
}
