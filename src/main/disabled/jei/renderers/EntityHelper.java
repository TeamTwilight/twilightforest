package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeEntityType;
import twilightforest.compat.jei.JEICompat;

import java.util.Objects;

public class EntityHelper implements IIngredientHelper<@NotNull FakeEntityType> {
	@Override
	public IIngredientType<@NotNull FakeEntityType> getIngredientType() {
		return JEICompat.ENTITY_TYPE;
	}

	@Override
	public String getDisplayName(FakeEntityType type) {
		return type.type().getDescription().getString();
	}

	@Override
	public Object getUid(FakeEntityType ingredient, UidContext context) {
		return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(ingredient.type())).toString();
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