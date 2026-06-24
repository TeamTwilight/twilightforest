package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeItemEntity;
import twilightforest.compat.jei.JEICompat;

import java.util.Objects;

public class FakeItemEntityHelper implements IIngredientHelper<@NotNull FakeItemEntity> {
	@Override
	public IIngredientType<@NotNull FakeItemEntity> getIngredientType() {
		return JEICompat.FAKE_ITEM_ENTITY;
	}

	@Override
	public String getDisplayName(FakeItemEntity ingredient) {
		return ingredient.stack().getItem().getName(ingredient.stack()).getString();
	}

	@Override
	public String getUid(FakeItemEntity ingredient, UidContext context) {
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
