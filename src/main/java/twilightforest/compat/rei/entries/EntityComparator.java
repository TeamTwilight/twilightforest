package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparatorRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface EntityComparator extends EntryComparatorRegistry<Entity, EntityType<?>> {

	static EntryComparator<Entity> entityTypeNbt() {
		EntryComparator<Tag> nbt = EntryComparator.nbt();
		return (context, entity) -> {
			CompoundTag tag = entity.saveWithoutId(new CompoundTag());
			return tag.isEmpty() ? 0L : nbt.hash(context, tag);
		};
	}
}
