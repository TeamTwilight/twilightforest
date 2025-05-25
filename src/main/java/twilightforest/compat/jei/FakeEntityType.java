package twilightforest.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import java.util.Objects;

//I have to wrap the entitytype in a class like this because otherwise it conflicts with other mods that also try to add entity ingredients
public record FakeEntityType(ResourceKey<EntityType<?>> type) {
	public static final IIngredientType<FakeEntityType> ENTITY_TYPE = () -> FakeEntityType.class;
	public static final Codec<FakeEntityType> CODEC = ResourceKey.codec(Registries.ENTITY_TYPE).xmap(
		FakeEntityType::new,
		FakeEntityType::type
	);

	@SuppressWarnings("deprecation")
	public FakeEntityType(EntityType<?> entityType) {
		this(Objects.requireNonNull(entityType.builtInRegistryHolder().getKey()));
	}
}
