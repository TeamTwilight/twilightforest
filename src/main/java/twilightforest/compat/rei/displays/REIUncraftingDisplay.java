package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.rei.TFREIServerPlugin;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.List;
import java.util.Optional;

public class REIUncraftingDisplay extends BasicDisplay {

	private final RecipeHolder<? extends CraftingRecipe> recipe;

	private REIUncraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, RecipeHolder<? extends CraftingRecipe> recipe) {
		super(inputs, outputs, Optional.of(recipe.id()));
		this.recipe = recipe;
	}

	public static REIUncraftingDisplay ofUncrafting(RecipeHolder<UncraftingRecipe> recipe) {
		RegistryAccess registryAccess = registryAccess();

		List<EntryIngredient> inputs = EntryIngredients.ofIngredients(recipe.value().getIngredients());
		List<EntryIngredient> outputs = List.of(EntryIngredients.of(recipe.value().getResultItem(registryAccess)));

		return new REIUncraftingDisplay(inputs, outputs, recipe);
	}

	public static REIUncraftingDisplay of(RecipeHolder<CraftingRecipe> recipe) {
		boolean isUncraftingRecipe = recipe.value() instanceof UncraftingRecipe;
		RegistryAccess registryAccess = registryAccess();

		List<EntryIngredient> inputs = isUncraftingRecipe ? EntryIngredients.ofIngredients(recipe.value().getIngredients()) : List.of(EntryIngredients.of(recipe.value().getResultItem(registryAccess)));
		List<EntryIngredient> outputs = isUncraftingRecipe ? List.of(EntryIngredients.of(recipe.value().getResultItem(registryAccess))) : EntryIngredients.ofIngredients(recipe.value().getIngredients());

		return new REIUncraftingDisplay(inputs, outputs, recipe);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return TFREIServerPlugin.UNCRAFTING;
	}

	public CraftingRecipe getRecipe() {
		return this.recipe.value();
	}

	public ResourceLocation getRecipeId() {
		return this.recipe.id();
	}

	public enum Serializer implements DisplaySerializer<REIUncraftingDisplay> {
		INSTANCE;

		@Override
		public CompoundTag save(CompoundTag tag, REIUncraftingDisplay display) {
			tag.putString("recipe_id", display.getRecipeId().toString());
			return tag;
		}

		@Nullable
		@Override
		@SuppressWarnings("unchecked")
		public REIUncraftingDisplay read(CompoundTag tag) {
			ResourceLocation location = ResourceLocation.tryParse(tag.getString("recipe_id"));

			if (location != null) {
				Optional<RecipeHolder<?>> recipe = RecipeManagerContext.getInstance().getRecipeManager().byKey(location);
				if (recipe.isPresent()) {
					return REIUncraftingDisplay.of((RecipeHolder<CraftingRecipe>) recipe.get());
				}
			}

			return null;
		}
	}
}
