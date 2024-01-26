package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.impl.common.entry.comparison.EntryComparatorRegistryImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityComparatorImpl extends EntryComparatorRegistryImpl<Entity, EntityType<?>> implements EntityComparator {

	public static EntityComparatorImpl INSTANCE = new EntityComparatorImpl();

	@Override
	public EntityType<?> getEntry(Entity entity) {
		return entity.getType();
	}

	@Override
	public EntryComparator<Entity> defaultComparator() {
		return EntityComparator.entityTypeNbt();
	}
}
