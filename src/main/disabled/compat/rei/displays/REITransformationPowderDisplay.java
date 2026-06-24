package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REITransformationPowderDisplay extends BasicDisplay {

	public final boolean isReversible;

	private REITransformationPowderDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, boolean reversible) {
		super(inputs, outputs);
		this.isReversible = reversible;
	}

	private REITransformationPowderDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CompoundTag tag) {
		this(inputs, outputs, tag.getBoolean("isReversible"));
	}

	@Nullable
	public static REITransformationPowderDisplay of(RecipeViewerConstants.TransformationPowderInfo recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(recipe.input(), Minecraft.getInstance().level).ifPresent(entity -> {
			inputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(entity.getType());
			if (inputEgg != null) {
				inputs.add(EntryIngredients.of(inputEgg));
			}
		});

		getEntity(recipe.output(), Minecraft.getInstance().level).ifPresent(entity -> {
			outputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(entity.getType());
			if (outputEgg != null) {
				outputs.add(EntryIngredients.of(outputEgg));
			}
		});

		if (!inputs.isEmpty() && !outputs.isEmpty()) {
			if (recipe.reversible()) {
				inputs.addAll(outputs);
				outputs.addAll(inputs);
			}

			return new REITransformationPowderDisplay(inputs, outputs, recipe.reversible());
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

	public static BasicDisplay.Serializer<REITransformationPowderDisplay> serializer() {
		return BasicDisplay.Serializer.ofRecipeLess(REITransformationPowderDisplay::new, (display, tag) -> tag.putBoolean("isReversible", display.isReversible));
	}
}
