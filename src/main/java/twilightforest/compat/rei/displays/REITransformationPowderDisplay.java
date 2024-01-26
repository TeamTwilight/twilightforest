package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.item.recipe.TransformPowderRecipe;
import twilightforest.util.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REITransformationPowderDisplay extends BasicDisplay {

	public final boolean isReversible;

	public final EntityType<?> inputType;
	public final EntityType<?> resultType;

	public REITransformationPowderDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, RecipeHolder<TransformPowderRecipe> recipe) {
		super(inputs, outputs, Optional.of(recipe.id()));
		this.isReversible = recipe.value().isReversible();
		this.inputType = recipe.value().input();
		this.resultType = recipe.value().result();
	}

	@Nullable
	public static REITransformationPowderDisplay of(RecipeHolder<TransformPowderRecipe> recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(recipe.value().input(), Minecraft.getInstance().level).ifPresent(entity -> {
			inputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			inputs.add(EntryIngredients.of(DeferredSpawnEggItem.byId(entity.getType())));
		});

		getEntity(recipe.value().result(), Minecraft.getInstance().level).ifPresent(entity -> {
			outputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			outputs.add(EntryIngredients.of(DeferredSpawnEggItem.byId(entity.getType())));
		});

		if (!inputs.isEmpty() && !outputs.isEmpty()) {
			if (recipe.value().isReversible()) {
				inputs.addAll(outputs);
				outputs.addAll(inputs);
			}

			return new REITransformationPowderDisplay(inputs, outputs, recipe);
		}

		return null;
	}

	public static Optional<Entity> getEntity(EntityType<?> type, @Nullable Level level) {
		return Optional.ofNullable(EntityRenderingUtil.fetchEntity(type, level));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REITransformationPowderCategory.TRANSFORMATION;
	}
}
