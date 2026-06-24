package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeItemEntity;
import twilightforest.compat.jei.JEICompat;

import java.util.Objects;

public class FakeItemEntityHelper implements IIngredientHelper<FakeItemEntity> {

	@Override
	public IIngredientType<FakeItemEntity> getIngredientType() {
		return JEICompat.FAKE_ITEM_ENTITY;
	}

	@Override
	public String getDisplayName(FakeItemEntity ingredient) {
		return ingredient.stack().getItem().getDescription().toString();
	}

	// we cannot delete this function on 1.21.1 but the whole class is not marked as deprecated so it should be fine
	// Use Object getUid(FakeEntityType, UidContext) for later versions
	@Override
	@SuppressWarnings("removal")
	public String getUniqueId(FakeItemEntity ingredient, UidContext context) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient.stack().getItem())).toString();
	}

	@Override
	public Identifier getIdentifier(FakeItemEntity ingredient) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient.stack().getItem()));
	}

	@Override
	public FakeItemEntity copyIngredient(FakeItemEntity ingredient) {
		return ingredient;
	}

	@Override
	public String getErrorInfo(@Nullable FakeItemEntity ingredient) {
		if (ingredient == null) {
			return "null";
		}
		return ingredient.stack().toString();
	}
}
