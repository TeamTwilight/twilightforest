package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeEntityType;
import twilightforest.compat.jei.JEICompat;

import java.util.Objects;

public class EntityHelper implements IIngredientHelper<FakeEntityType> {

	@Override
	public IIngredientType<FakeEntityType> getIngredientType() {
		return JEICompat.ENTITY_TYPE;
	}

	@Override
	public String getDisplayName(FakeEntityType type) {
		return type.type().getDescription().getString();
	}

	// we cannot delete this function on 1.21.1 but the whole class is not marked as deprecated so it should be fine
	// Use Object getUid(FakeEntityType, UidContext) for later versions
	@Override
	@SuppressWarnings("removal")
	public String getUniqueId(FakeEntityType type, UidContext context) {
		return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type.type())).toString();
	}

	@Override
	public Identifier getIdentifier(FakeEntityType type) {
		return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type.type()));
	}

	@Override
	public FakeEntityType copyIngredient(FakeEntityType type) {
		return type;
	}

	@Override
	public String getErrorInfo(@Nullable FakeEntityType type) {
		if (type == null) {
			return "null";
		}
		Identifier name = BuiltInRegistries.ENTITY_TYPE.getKey(type.type());
		if (name == null) {
			return "unnamed sadface :(";
		}
		return name.toString();
	}
}