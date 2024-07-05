package twilightforest.compat.jei;

import net.minecraft.world.entity.EntityType;

//I have to wrap the entitytype in a class like this because otherwise it conflicts with other mods that also try to add entity ingredients
public record FakeEntityType(EntityType<?> type) {
}
